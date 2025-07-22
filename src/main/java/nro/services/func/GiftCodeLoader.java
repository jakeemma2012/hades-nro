package nro.services.func;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import nro.jdbc.DBService;
import nro.models.item.Item;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Util;

public class GiftCodeLoader {

    public static HashMap<String, HashMap<String, Object>> giftCodes = new HashMap<>();

    static long lastTimeLoad;
    
    public static void loadGiftCodes() {
      if(Util.canDoWithTime(lastTimeLoad, 10000)) {
          giftCodes.clear();
            try (PreparedStatement ps = DBService.gI().getConnectionForGame().prepareStatement("SELECT * FROM giftcode WHERE Luot >= 1")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HashMap<String, Object> giftCode = new HashMap<>();
                giftCode.put("code", rs.getString("Code"));
                giftCode.put("remaining", rs.getInt("Luot"));
                giftCode.put("slBag", rs.getInt("slBag"));
                String itemsJson = rs.getString("Item");
                giftCode.put("items", parseItems(itemsJson));
                giftCodes.put(rs.getString("Code"), giftCode);
            }
            System.err.println("\nLoad code thành công " + giftCodes.size());
            lastTimeLoad = System.currentTimeMillis();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }
    }

    private static ArrayList<HashMap<String, Object>> parseItems(String itemsJson) {
        ArrayList<HashMap<String, Object>> items = new ArrayList<>();
        try {
            JSONArray jar = new JSONArray(itemsJson);
            for (int i = 0; i < jar.length(); i++) {
                JSONObject itemObj = jar.getJSONObject(i);
                HashMap<String, Object> item = new HashMap<>();
                item.put("id", itemObj.getInt("id"));
                item.put("quantity", itemObj.getInt("quantity"));
                item.put("options", parseOptions(itemObj.getJSONArray("options")));
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    private static ArrayList<ItemOption> parseOptions(JSONArray optionsJson) {
        ArrayList<ItemOption> options = new ArrayList<>();
        if (optionsJson != null) {
            for (int j = 0; j < optionsJson.length(); j++) {
                JSONObject obj = optionsJson.getJSONObject(j);
                int optionID = obj.getInt("id");
                int param = obj.getInt("param");
                options.add(new ItemOption(optionID, param));
            }
        }
        return options;
    }

    public static boolean isValidGiftCode(String inputCode) {
        if (giftCodes.containsKey(inputCode)) {
            HashMap<String, Object> giftCodeInfo = giftCodes.get(inputCode);
            int remaining = (int) giftCodeInfo.get("remaining");
            return remaining > 0;
        }
        return false;
    }

    public static void rewardPlayer(Player player, String code) {
        try {
             if (isValidGiftCode(code)) {
            if (!isUseGiftCode(player, code, false)) {
                HashMap<String, Object> giftCodeInfo = giftCodes.get(code);
                ArrayList<HashMap<String, Object>> items = (ArrayList<HashMap<String, Object>>) giftCodeInfo.get("items");
                StringBuilder sb = new StringBuilder();
                int slBag = (int) giftCodeInfo.get("slBag");
                int remain = (int) giftCodeInfo.get("remaining");
                giftCodeInfo.put("remaining", remain - 1);
                if (InventoryService.gI().getCountEmptyBag(player) > slBag) {
                    
                    sb.append("Bạn vừa nhận được : ");
                    for (HashMap<String, Object> item : items) {
                        int itemId = (int) item.get("id");
                        int quantity = (int) item.get("quantity");
                        ArrayList<ItemOption> options = (ArrayList<ItemOption>) item.get("options");
                        Item vp = ItemService.gI().createNewItem((short) itemId, quantity);
                        if (!options.isEmpty()) {
                            for (ItemOption io : options) {
                                vp.itemOptions.add(new nro.models.item.ItemOption(io.id, io.param));
                            }
                        }
                        sb.append("\n+ x" + quantity + " " + vp.template.name);
                        InventoryService.gI().addItemBag(player, vp, quantity);
                    }
                    isUseGiftCode(player, code, true);
                    markGiftCodeAsUsed((remain - 1), code);
                    
                } else {
                    sb.append("Không đủ " + slBag + " ô trống hành trang !");
                }
                Service.getInstance().sendThongBaoOK(player, sb.toString());
                InventoryService.gI().sendItemBags(player);
            }
        } else {
            Service.getInstance().sendThongBaoOK(player, "Code " + code + " không tồn tại hoặc đã hết lượt nhập !");
        }
        } catch (Exception e) {
            Service.getInstance().sendThongBaoOK(player, "Có lỗi xảy ra! Vui lòng liên hệ với ADMIN !");
            e.printStackTrace();
        }
    }

    private static void markGiftCodeAsUsed(int luot, String code) {
        try (Connection connection = DBService.gI().getConnectionForGame(); 
                PreparedStatement updatePS = connection.prepareStatement("UPDATE giftcode SET Luot = ? WHERE Code = ? LIMIT 1")) {
            updatePS.setInt(1, luot);
            updatePS.setString(2, code);
            updatePS.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isUseGiftCode(Player player, String code, boolean update) {
        try (Connection connection = DBService.gI().getConnectionForGame(); PreparedStatement psSelect = connection.prepareStatement("SELECT * FROM giftcode_his WHERE player_id = ? AND code = ?"); PreparedStatement psInsert = connection.prepareStatement("INSERT INTO giftcode_his (player_id, code, time) VALUES (?, ?, ?)")) {
            if (code != null) {
                psSelect.setInt(1, (int) player.id);
                psSelect.setString(2, code);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        Service.getInstance().sendThongBaoOK(player, "Bạn đã nhập Gift-Code này vào: " + rs.getTimestamp("time"));
                        psInsert.close();
                        psSelect.close();
                        return true;
                    }
                }
            }

            if (update) {
                psInsert.setInt(1, (int) player.id);
                psInsert.setString(2, code);
                psInsert.setString(3, Util.toDateString(Date.from(Instant.now())));
                psInsert.executeUpdate();

                psInsert.close();
                psSelect.close();
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            Service.getInstance().sendThongBaoOK(player, "Lỗi không xác định, hãy thử lại");
            return true;
        }
    }

}

class ItemOption {
    int id;
    int param;
    public ItemOption(int id, int param) {
        this.id = id;
        this.param = param;
    }
}
