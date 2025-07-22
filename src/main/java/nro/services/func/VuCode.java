package nro.services.func;

import nro.jdbc.DBService;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.Service;
import nro.utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import nro.models.item.Item;
import nro.services.ItemService;

public class VuCode {

    private static VuCode instance;

    public static VuCode gI() {
        if (instance == null) {
            instance = new VuCode();
        }
        return instance;
    }

    private static ArrayList<nro.services.func.ItemOption> parseOptions(org.json.JSONArray optionsJson) {
        ArrayList<nro.services.func.ItemOption> options = new ArrayList<>();
        if (optionsJson != null) {
            for (int j = 0; j < optionsJson.length(); j++) {
                org.json.JSONObject obj = optionsJson.getJSONObject(j);
                int optionID = obj.getInt("id");
                int param = obj.getInt("param");
                options.add(new nro.services.func.ItemOption(optionID, param));
            }
        }
        return options;
    }

    private static ArrayList<HashMap<String, Object>> parseItems(String itemsJson) {
        ArrayList<HashMap<String, Object>> items = new ArrayList<>();
        try {
            org.json.JSONArray jar = new org.json.JSONArray(itemsJson);
            for (int i = 0; i < jar.length(); i++) {
                org.json.JSONObject itemObj = jar.getJSONObject(i);
                HashMap<String, Object> item = new HashMap<>();
                item.put("id", itemObj.getInt("id"));
                item.put("quantity", itemObj.getLong("quantity"));
                item.put("options", parseOptions(itemObj.getJSONArray("options")));
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    private void getCode(Player player, String code) {
        try (Connection con = DBService.gI().getConNectForCoe(); PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM giftcode WHERE `Luot` >= 1 AND `Code` = ?;",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);) {
            ps.setString(1, code);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs != null && rs.first()) {
                    HashMap<Integer, HashMap<Integer, Integer>> itemMap = new HashMap<>();
                    HashMap<String, Object> giftCode = new HashMap<>();
                    if (rs.getString("Code").equals(code) && rs.getInt("Luot") > 0) {
                        String itemsJson = rs.getString("Item");
                        giftCode.put("items", parseItems(itemsJson));

                        if (InventoryService.gI().getCountEmptyBag(player) < itemMap.size()) {
                            Service.getInstance().sendThongBaoOK(player,
                                    "Hành trang đầy, cần " + itemMap.size() + " ô trống");
                            return;
                        }
                        boolean isUsed = VuCode.gI().isUsedGiftCode((int) player.id, code, player);
                        if (!isUsed) {
                            int luot = rs.getInt("Luot");
                            int slBag = rs.getInt("slBag");
                            ArrayList<HashMap<String, Object>> items = (ArrayList<HashMap<String, Object>>) giftCode.get("items");
                            StringBuilder sb = new StringBuilder();
                            if (InventoryService.gI().getCountEmptyBag(player) > slBag) {
                                  sb.append("Bạn vừa nhân được : ");
                                for (HashMap<String, Object> item : items) {
                                    int itemId = (int) item.get("id");
                                   long quantity = (long) item.get("quantity");
                                    ArrayList<nro.services.func.ItemOption> options = (ArrayList<nro.services.func.ItemOption>) item.get("options");
                                    Item vp = ItemService.gI().createNewItem((short) itemId, (int)quantity);
                                   if(vp.template.type == 9){
                                         player.inventory.gold += quantity;
                                          sb.append("\n+ x").append(Util.numberToMoney(quantity)).append(" ").append(vp.template.name);
                                          Service.getInstance().sendMoney(player);
                                    } else {
                                         if (!options.isEmpty()) {
                                        for (nro.services.func.ItemOption io : options) {
                                            vp.itemOptions.add(new nro.models.item.ItemOption(io.id, io.param));
                                        }
                                    }
                                    sb.append("\n+ x").append(quantity).append(" ").append(vp.template.name);
                                    InventoryService.gI().addItemBag(player, vp, 9999);
                                    InventoryService.gI().sendItemBags(player);
                                    }
                                }
                                UpdateLuot(luot, code);
                                addUsedGiftCode((int) player.id, code);
                            } else {
                                sb.append("Không đủ " + slBag + " ô trống hành trang !");
                            }
                            Service.getInstance().sendThongBaoOK(player, sb.toString());

                        }
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player,
                            "GiftCode " + code + " đã hết hạn hoặc không tồn tại !");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception errorLog) {
            errorLog.printStackTrace();
        }
    }

    public void giftCode(Player player, String code) {
        try {
            Service.getInstance().sendThongBaoOK(player, "Đang xử lý ...");
            Thread.sleep(1000);
            getCode(player, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isUsedGiftCode(int playerID, String code, Player player) {
        try {
            PreparedStatement stmt = DBService.gI().getConnectionForGame().prepareStatement(
                    "SELECT * FROM giftcode_his WHERE `player_id` = ? AND `code` = ?;",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setInt(1, playerID);
            stmt.setString(2, code);
            ResultSet res = stmt.executeQuery();
            try {
                if (res.first()) {
                    Service.getInstance().sendThongBaoOK(player,
                            "Bạn đã nhập Gift-Code này vào: " + res.getTimestamp("time"));
                    return true;
                }
            } finally {
                res.close();
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addUsedGiftCode(int playerID, String code) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            PreparedStatement stmt = DBService.gI().getConnectionForGame().prepareStatement(
                    "INSERT INTO `giftcode_his` (`player_id`, `code`, `time`) VALUES (?, ?, ?);");
            stmt.setInt(1, playerID);
            stmt.setString(2, code);
            stmt.setTimestamp(3, timestamp);
            stmt.executeUpdate();
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void UpdateLuot(int Luot, String code) {
        try {
            PreparedStatement stmt = DBService.gI().getConnectionForGame().prepareStatement(
                    "UPDATE `giftcode` SET `Luot` = ? WHERE `Code` like ?  LIMIT 1;");
            stmt.setInt(1, Luot - 1);
            stmt.setString(2, code);
            stmt.executeUpdate();
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
