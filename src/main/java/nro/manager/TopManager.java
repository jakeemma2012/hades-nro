package nro.manager;

import lombok.Getter;
import nro.jdbc.DBService;
import nro.jdbc.daos.PlayerDAO;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.services.ItemService;
import nro.utils.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.JSONParser;

/**
 *
 */
public class TopManager {

    @Getter
    private List<Player> list = new ArrayList<>();
    @Getter
    private List<Player> listTopCa = new ArrayList<>();
    @Getter
    private List<Player> listTopMia = new ArrayList<>();
    @Getter
    private List<Player> listTopTrungThu = new ArrayList<>();
    @Getter
    private List<Player> listTopDothan = new ArrayList<>();
    @Getter
    private List<Player> listTopRnadoom = new ArrayList<>();
    @Getter
    private List<Player> listTopOmega = new ArrayList<>();
    @Getter
    private List<Player> ListTOpBanhLKem = new ArrayList<>();
    @Getter
    private List<Player> ListTopThoiVang = new ArrayList<>();
    private static final TopManager INSTANCE = new TopManager();

    public static TopManager getInstance() {
        return INSTANCE;
    }

    public void load() {
        list.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("SELECT * FROM player ORDER BY player.power DESC LIMIT 20");
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;
                JSONObject dataObject = null;

                Player player = new Player();

                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                dataArray = (JSONArray) jv.parse(rs.getString("data_point"));
                player.nPoint.power = Long.parseLong(dataArray.get(11).toString());
                dataArray.clear();

                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();
                dataObject.clear();

                list.add(player);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public void loadTopCa() {
        listTopCa.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("SELECT * FROM player ORDER BY player.pointCa DESC LIMIT 20");
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;
                JSONObject dataObject = null;

                Player player = new Player();

                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                player.pointVongQuayThuongDe = rs.getInt("pointCa");

                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();
                dataObject.clear();

                listTopCa.add(player);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public void loadTopMia() {
        listTopMia.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("SELECT * FROM player ORDER BY player.pointRuong DESC LIMIT 20");
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;
                JSONObject dataObject = null;

                Player player = new Player();

                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                player.pointSk = rs.getInt("pointRuong");

                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();
                dataObject.clear();
                listTopMia.add(player);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public void loadTopTrungThu() {
        listTopTrungThu.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try (Connection con = DBService.gI().getConnectionForGetPlayer()) {
            ps = con.prepareStatement("SELECT * FROM player ORDER BY CAST(JSON_UNQUOTE(JSON_EXTRACT(pointBanh, '$[2]')) AS UNSIGNED) DESC LIMIT 20");
            rs = ps.executeQuery();

            while (rs.next()) {
                Player player = new Player();

                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                // Lấy giá trị thứ 3 từ pointBanh
                String pointBanhStr = rs.getString("pointBanh");
                try {
                    JSONArray pointBanhArray = (JSONArray) new JSONParser().parse(pointBanhStr);
                    player.pointTrungThu = (pointBanhArray.size() > 2) ? Integer.parseInt(pointBanhArray.get(2).toString()) : 0;
                } catch (Exception e) {
                    player.pointTrungThu = 0; // Giá trị mặc định nếu có lỗi
                }

                // Lấy và xử lý items_body
                String itemsBodyStr = rs.getString("items_body");
                try {
                    JSONArray dataArray = (JSONArray) new JSONParser().parse(itemsBodyStr);
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        JSONObject dataObject = (JSONObject) dataArray.get(i);
                        short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));

                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                            JSONArray options = (JSONArray) dataObject.get("option");

                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) options.get(j);
                                item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();
                        }
                        player.inventory.itemsBody.add(item);
                    }
                } catch (Exception e) {
                    // Xử lý lỗi nếu cần
                }

                listTopTrungThu.add(player);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Log.error(PlayerDAO.class, ex);
            }
        }
    }

    public void loadTopThan() {
        listTopDothan.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("SELECT * FROM player");
            rs = ps.executeQuery();

            while (rs.next()) {
                JSONValue jv = new JSONValue();
                Player player = new Player();
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                int count = 0; // Biến đếm

                // Xử lý items_body
                count += processItems(rs.getString("items_body"), player.inventory.itemsBody);

                // Xử lý items_bag
                count += processItems(rs.getString("items_bag"), player.inventory.itemsBag);

                // Xử lý items_box
                count += processItems(rs.getString("items_box"), player.inventory.itemsBox);

                player.count = count; // Cập nhật số lượng vào player
                listTopDothan.add(player);
            }

            // Sắp xếp danh sách theo count từ cao xuống thấp
            listTopDothan.sort((p1, p2) -> Integer.compare(p2.count, p1.count));

            if (listTopDothan.size() > 100) {
                listTopDothan = listTopDothan.subList(0, 100);
            }

        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Log.error(PlayerDAO.class, ex);
            }
        }
    }

    private int processItems(String jsonString, List<Item> itemList) {
        int count = 0; // Biến đếm cục bộ
        try {
            JSONValue jv = new JSONValue();
            JSONArray dataArray = (JSONArray) jv.parse(jsonString);
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject dataObject = (JSONObject) dataArray.get(i);
                short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                if (tempId >= 555 && tempId <= 567) {
                    count++; // Tăng biến đếm
                }

                Item item = null;
                if (tempId != -1) {
                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                    JSONArray options = (JSONArray) dataObject.get("option");
                    for (int j = 0; j < options.size(); j++) {
                        JSONArray opt = (JSONArray) options.get(j);
                        item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                Integer.parseInt(String.valueOf(opt.get(1)))));
                    }
                    item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                    if (ItemService.gI().isOutOfDateTime(item)) {
                        item = ItemService.gI().createItemNull();
                    }
                } else {
                    item = ItemService.gI().createItemNull();
                }
                itemList.add(item);
            }
        } catch (Exception e) {
            // Xử lý lỗi nếu cần
        }
        return count; // Trả về số lượng item đã đếm
    }

    public void loadTopRandom(int so) {
        listTopRnadoom.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("SELECT * FROM player");
            rs = ps.executeQuery();

            while (rs.next()) {
                JSONValue jv = new JSONValue();
                Player player = new Player();
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                int count = 0; // Biến đếm

                // Xử lý items_body
                count += processItems(rs.getString("items_body"), player.inventory.itemsBody, so);

                // Xử lý items_bag
                count += processItems(rs.getString("items_bag"), player.inventory.itemsBag, so);

                // Xử lý items_box
                count += processItems(rs.getString("items_box"), player.inventory.itemsBox, so);

                player.countRd = count; // Cập nhật số lượng vào player
                listTopRnadoom.add(player);
            }

            // Sắp xếp danh sách theo count từ cao xuống thấp
            listTopRnadoom.sort((p1, p2) -> Long.compare(p2.countRd, p1.countRd));

            if (listTopRnadoom.size() > 50) {
                listTopRnadoom = listTopRnadoom.subList(0, 50);
            }

        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Log.error(PlayerDAO.class, ex);
            }
        }
    }

    private int processItems(String jsonString, List<Item> itemList, int idItem) {
        int count = 0; // Biến đếm cục bộ
        try {
            JSONValue jv = new JSONValue();
            JSONArray dataArray = (JSONArray) jv.parse(jsonString);
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject dataObject = (JSONObject) dataArray.get(i);
                short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                Item item = null;
                if (tempId != -1) {
                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                    if (tempId == idItem) {
                        count += item.quantity;
                    }
                    JSONArray options = (JSONArray) dataObject.get("option");
                    for (int j = 0; j < options.size(); j++) {
                        JSONArray opt = (JSONArray) options.get(j);
                        item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                Integer.parseInt(String.valueOf(opt.get(1)))));
                    }
                    item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                    if (ItemService.gI().isOutOfDateTime(item)) {
                        item = ItemService.gI().createItemNull();
                    }
                } else {
                    item = ItemService.gI().createItemNull();
                }
                itemList.add(item);
            }
        } catch (Exception e) {
            // Xử lý lỗi nếu cần
        }
        return count; // Trả về số lượng item đã đếm
    }
    
    
    public void loadTopOmega() {
        listTopOmega.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try (Connection con = DBService.gI().getConnectionForGetPlayer()) {
            ps = con.prepareStatement("SELECT * FROM player ORDER BY CAST(JSON_UNQUOTE(JSON_EXTRACT(nhanqua, '$[2]')) AS UNSIGNED) DESC LIMIT 20");
            rs = ps.executeQuery();

            while (rs.next()) {
                Player player = new Player();

                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                // Lấy giá trị thứ 3 từ pointBanh
                String pointBanhStr = rs.getString("nhanqua");
                try {
                    JSONArray pointBanhArray = (JSONArray) new JSONParser().parse(pointBanhStr);
                    player.dameOmega = Long.parseLong(pointBanhArray.get(2).toString());
                } catch (Exception e) {
                    player.dameOmega = 0; // Giá trị mặc định nếu có lỗi
                }

                // Lấy và xử lý items_body
                String itemsBodyStr = rs.getString("items_body");
                try {
                    JSONArray dataArray = (JSONArray) new JSONParser().parse(itemsBodyStr);
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        JSONObject dataObject = (JSONObject) dataArray.get(i);
                        short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));

                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                            JSONArray options = (JSONArray) dataObject.get("option");

                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) options.get(j);
                                item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();
                        }
                        player.inventory.itemsBody.add(item);
                    }
                } catch (Exception e) {
                    // Xử lý lỗi nếu cần
                }
                listTopOmega.add(player);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Log.error(PlayerDAO.class, ex);
            }
        }
    }
    
     public void loadTopBanhKem() {
        ListTOpBanhLKem.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try (Connection con = DBService.gI().getConnectionForGetPlayer()) {
            ps = con.prepareStatement("SELECT * FROM player ORDER BY CAST(JSON_UNQUOTE(JSON_EXTRACT(pointBanh, '$[4]')) AS UNSIGNED) DESC LIMIT 20");
            rs = ps.executeQuery();

            while (rs.next()) {
                Player player = new Player();

                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                // Lấy giá trị thứ 3 từ pointBanh
                String pointBanhStr = rs.getString("pointBanh");
                try {
                    JSONArray pointBanhArray = (JSONArray) new JSONParser().parse(pointBanhStr);
                    player.pointBanhKem = Integer.parseInt(pointBanhArray.get(4).toString());
                } catch (Exception e) {
                    player.pointBanhKem = 0; // Giá trị mặc định nếu có lỗi
                }

                // Lấy và xử lý items_body
                String itemsBodyStr = rs.getString("items_body");
                try {
                    JSONArray dataArray = (JSONArray) new JSONParser().parse(itemsBodyStr);
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        JSONObject dataObject = (JSONObject) dataArray.get(i);
                        short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));

                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                            JSONArray options = (JSONArray) dataObject.get("option");

                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) options.get(j);
                                item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();
                        }
                        player.inventory.itemsBody.add(item);
                    }
                } catch (Exception e) {
                    // Xử lý lỗi nếu cần
                }
                ListTOpBanhLKem.add(player);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Log.error(PlayerDAO.class, ex);
            }
        }
    }
     
     public void loadTopThoiVang() {
        ListTopThoiVang.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try (Connection con = DBService.gI().getConnectionForGetPlayer()) {
            ps = con.prepareStatement("SELECT * FROM player ORDER BY CAST(JSON_UNQUOTE(JSON_EXTRACT(pointBanh, '$[5]')) AS UNSIGNED) DESC LIMIT 20");
            rs = ps.executeQuery();

            while (rs.next()) {
                Player player = new Player();

                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                String pointBanhStr = rs.getString("pointBanh");
                try {
                    JSONArray pointBanhArray = (JSONArray) new JSONParser().parse(pointBanhStr);
                    player.pointThoiVang = Integer.parseInt(pointBanhArray.get(5).toString());
                } catch (Exception e) {
                    player.pointThoiVang = 0; 
                }

                String itemsBodyStr = rs.getString("items_body");
                try {
                    JSONArray dataArray = (JSONArray) new JSONParser().parse(itemsBodyStr);
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        JSONObject dataObject = (JSONObject) dataArray.get(i);
                        short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));

                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                            JSONArray options = (JSONArray) dataObject.get("option");

                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) options.get(j);
                                item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();
                        }
                        player.inventory.itemsBody.add(item);
                    }
                } catch (Exception e) {
                    // Xử lý lỗi nếu cần
                }
                ListTopThoiVang.add(player);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Log.error(PlayerDAO.class, ex);
            }
        }
    }
}
