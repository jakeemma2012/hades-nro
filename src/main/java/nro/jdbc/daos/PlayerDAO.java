package nro.jdbc.daos;

import com.google.gson.Gson;
import nro.consts.ConstMap;
import nro.jdbc.DBService;
import nro.manager.AchiveManager;
import nro.manager.SieuHangManager;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.item.ItemTime;
import nro.models.player.*;
import nro.models.skill.Skill;
import nro.models.task.Achivement;
import nro.models.task.AchivementTemplate;
import nro.server.Manager;
import nro.services.MapService;
import nro.utils.Log;
import nro.utils.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import nro.services.PetService;
import nro.services.Service;
import org.json.simple.parser.JSONParser;

/**
 *
 */
public class PlayerDAO {

    public static boolean updateTimeLogout;

    public static void createNewPlayer(Connection con, int userId, String name, byte gender, int hair,
            PreparedStatement ps) {
        try {
            JSONArray dataInventory = new JSONArray();

            dataInventory.add(20000000000L);
            dataInventory.add(10000);
            dataInventory.add(0);
            String inventory = dataInventory.toJSONString();

            JSONArray dataLocation = new JSONArray();
            dataLocation.add(100);
            dataLocation.add(384);
            dataLocation.add(39 + gender);
            String location = dataLocation.toJSONString();

            JSONArray dataPoint = new JSONArray();
            dataPoint.add(0);// nang dong
            dataPoint.add(gender == 1 ? 200 : 100);// mp
            dataPoint.add(gender == 1 ? 200 : 100);// mpg
            dataPoint.add(0);// critg
            dataPoint.add(0);// limitpower
            dataPoint.add(1000);// stamina
            dataPoint.add(gender == 0 ? 200 : 100);// hp
            dataPoint.add(0);// defg
            dataPoint.add(100_000_000);// tn
            dataPoint.add(1000);// maxsta
            dataPoint.add(gender == 2 ? 15 : 10);// damg
            dataPoint.add(100_000_000);// pow
            dataPoint.add(gender == 0 ? 200 : 100);// hpg
            String point = dataPoint.toJSONString();

            JSONArray dataMagicTree = new JSONArray();
            dataMagicTree.add(0);// isupgr
            dataMagicTree.add(new Date().getTime());
            dataMagicTree.add(10);// LV
            dataMagicTree.add(new Date().getTime());
            dataMagicTree.add(5);// curr_pea
            String magicTree = dataMagicTree.toJSONString();

            /**
             *
             * [
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"-1","option":[],"create_time":"0""}, ... ]
             */
            int idAo = gender == 0 ? 0 : gender == 1 ? 1 : 2;
            int idQuan = gender == 0 ? 6 : gender == 1 ? 7 : 8;
            int idGang = 21 + gender;
            int idjay = 27 + gender;
            int def = gender == 2 ? 3 : 2;
            int hp = gender == 0 ? 30 : 20;
            int sd = gender == 0 ? 7 : ((gender * 2) + 1);
            int mp = gender == 1 ? 14 : 10;
            int spl = 5;
            JSONArray dataBody = new JSONArray();
            for (int i = 0; i < 11; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                JSONArray option = new JSONArray();
                JSONArray option2 = new JSONArray();
                JSONArray poption3 = new JSONArray();
                switch (i) {
                    case 0:
                        option.add(107);
                        option.add(spl);
                        option2.add(47);
                        option2.add(def);
                        poption3.add(30);
                        poption3.add(999);
                        options.add(option);
                        options.add(option2);
                        options.add(poption3);
                        item.put("temp_id", idAo);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1);
                        break;
                    case 1:
                        option.add(107);
                        option.add(spl);
                        option2.add(6);
                        option2.add(hp);
                        poption3.add(30);
                        poption3.add(999);
                        options.add(option);
                        options.add(option2);
                        options.add(poption3);
                        item.put("temp_id", idQuan);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1);
                        break;
                    case 2:
                        option.add(107);
                        option.add(spl);
                        option2.add(0);
                        option2.add(sd);
                        poption3.add(30);
                        poption3.add(999);

                        options.add(option);
                        options.add(option2);
                        options.add(poption3);
                        item.put("temp_id", idGang);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1);
                        break;
                    case 3:
                        option.add(107);
                        option.add(spl);
                        option2.add(7);
                        option2.add(mp);
                        poption3.add(30);
                        poption3.add(999);

                        options.add(option);
                        options.add(option2);
                        options.add(poption3);
                        item.put("temp_id", idjay);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1);
                        break;
                    case 4:
                        option.add(107);
                        option.add(spl);
                        option2.add(14);
                        option2.add(1);
                        poption3.add(30);
                        poption3.add(999);

                        options.add(option);
                        options.add(option2);
                        options.add(poption3);
                        item.put("temp_id", 12);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 1);
                        break;
                    default:
                        item.put("temp_id", -1);
                        item.put("create_time", 0);
                        item.put("quantity", 1);
                        break;
                }
                item.put("option", options);
                dataBody.add(item);
            }
            String itemsBody = dataBody.toJSONString();

            JSONArray dataBag = new JSONArray();
            for (int i = 0; i < 101; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                JSONArray option = new JSONArray();
                switch (i) {
                    case 0:
                        option.add(30);
                        option.add(1);
                        options.add(option);
                        item.put("temp_id", 457);
                        item.put("create_time", System.currentTimeMillis());
                        item.put("quantity", 10);
                        break;
                    default:
                        item.put("temp_id", -1);
                        item.put("create_time", 0);
                        item.put("quantity", 1);
                        break;
                }
                item.put("option", options);
                dataBag.add(item);
            }
            String itemsBag = dataBag.toJSONString();

            JSONArray dataBox = new JSONArray();

            for (int i = 0; i < 100; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                JSONArray option = new JSONArray();
                if (i == 0) {
                    item.put("temp_id", 12);
                    option.add(14);
                    option.add(1);
                    options.add(option);
                    item.put("create_time", System.currentTimeMillis());
                } else {
                    item.put("temp_id", -1);
                    item.put("create_time", 0);
                }
                item.put("option", options);
                item.put("quantity", 1);
                dataBox.add(item);
            }
            String itemsBox = dataBox.toJSONString();

            JSONArray dataLuckyRound = new JSONArray();
            for (int i = 0; i < 110; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                item.put("temp_id", -1);
                item.put("option", options);
                item.put("create_time", 0);
                item.put("quantity", 1);
                dataLuckyRound.add(item);
            }
            String itemsBoxLuckyRound = dataLuckyRound.toJSONString();

            String friends = "[]";
            String enemies = "[]";

            JSONArray dataIntrinsic = new JSONArray();
            dataIntrinsic.add(0);
            dataIntrinsic.add(0);
            dataIntrinsic.add(0);
            dataIntrinsic.add(0);
            String intrinsic = dataIntrinsic.toJSONString();

            JSONArray dataItemNewTime = new JSONArray();
            dataItemNewTime.add(0);
            dataItemNewTime.add(0);
            dataItemNewTime.add(0);
            dataItemNewTime.add(0);
            dataItemNewTime.add(0);
            dataItemNewTime.add(0);
            String itemTimeNew = dataItemNewTime.toJSONString();

            JSONArray dataMayDo = new JSONArray();
            dataMayDo.add(0);
            String itemMayDo = dataMayDo.toJSONString();

            JSONArray dataTask = new JSONArray();
            dataTask.add(0);
            dataTask.add(0);
            dataTask.add(0);
            String task = dataTask.toJSONString();

            JSONArray dataAchive = new JSONArray();
            for (AchivementTemplate a : AchiveManager.getInstance().getList()) {
                JSONObject jobj = new JSONObject();
                jobj.put("id", a.getId());
                jobj.put("count", 0);
                jobj.put("finish", 0);
                jobj.put("receive", 0);
                dataAchive.add(jobj);
            }
            String achive = dataAchive.toJSONString();

            String mabuEgg = "{}";
            // \"create_time\":1703348157777,\"time_done\":604800000
            JSONArray dataCharms = new JSONArray();
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            String charms = dataCharms.toJSONString();

            int[] skillsArr = gender == 0 ? new int[] { 0, 1, 6, 9, 10, 20, 22, 19, 24 }
                    : gender == 1 ? new int[] { 2, 3, 7, 11, 12, 17, 18, 19, 26 }
                            : new int[] { 4, 5, 8, 13, 14, 21, 23, 19, 25 };
            // [{"temp_id":"4","point":0,"last_time_use":0},]

            // for (int i = 0; i < skillsArr.length; i++) {
            // dataObject.put("temp_id", skillsArr[i]);
            // if (i == 0) {
            // dataObject.put("point", 1);
            // } else {
            // dataObject.put("point", 0);
            // }
            // dataObject.put("last_time_use", 0);
            // dataArray.add(dataObject.toJSONString());
            // dataObject.clear();
            // }
            // String skills = dataArray.toJSONString();
            // dataArray.clear();
            //
            JSONArray dataSkills = new JSONArray();
            for (int i = 0; i < skillsArr.length; i++) {
                JSONArray skill = new JSONArray();
                skill.add(skillsArr[i]);
                skill.add(0);
                if (i == 0) {
                    skill.add(7);
                } else {
                    skill.add(7);
                }
                dataSkills.add(skill);
            }
            String skills = dataSkills.toJSONString();

            JSONArray dataSkillShortcut = new JSONArray();
            dataSkillShortcut.add(gender == 0 ? 0 : gender == 1 ? 2 : 4);
            for (int i = 0; i < 9; i++) {
                dataSkillShortcut.add(-1);
            }
            String skillsShortcut = dataSkillShortcut.toJSONString();

            int[] data = new int[0];
            data = PetService.gI().getDataPetNormal();
            String petInfo = "{\"gender\":\"" + gender
                    + "\",\"is_mabu\":\"0\",\"level\":\"0\",\"name\":\"$Đệ tử\",\"type_fusion\":\"0\",\"left_fusion\":\"1292015613\",\"status\":\"0\"}";
            String petPoint = "{\"mp\":\"" + data[1] + "\",\"max_stamina\":\"1000\",\"mpg\":\"" + data[1]
                    + "\",\"critg\":\"" + data[4] + "\",\"limit_power\":\"0\",\"stamina\":\"1000\",\"hp\":\"" + data[0]
                    + "\",\"damg\":\"" + data[2] + "\",\"power\":\"2000\",\"defg\":\"" + data[3] + "\",\"hpg\":\""
                    + data[0] + "\",\"tiem_nang\":\"0\"}";

            String petBody = "[{\"quantity\":\"0\",\"create_time\":\"0\",\"temp_id\":\"-1\",\"option\":[]},"
                    + "{\"quantity\":\"0\",\"create_time\":\"0\",\"temp_id\":\"-1\",\"option\":[]},"
                    + "{\"quantity\":\"0\",\"create_time\":\"0\",\"temp_id\":\"-1\",\"option\":[]},"
                    + "{\"quantity\":\"0\",\"create_time\":\"0\",\"temp_id\":\"-1\",\"option\":[]},"
                    + "{\"quantity\":\"0\",\"create_time\":\"0\",\"temp_id\":\"-1\",\"option\":[]},"
                    + "{\"quantity\":\"0\",\"create_time\":\"0\",\"temp_id\":\"-1\",\"option\":[]},"
                    + "{\"quantity\":\"0\",\"create_time\":\"0\",\"temp_id\":\"-1\",\"option\":[]}]";

            String petSkill = "[[\"0\",\"1\"],[\"-1\",\"0\"],[\"-1\",\"0\"],[\"-1\",\"0\"]]";

            JSONArray dataBlackBall = new JSONArray();
            for (int i = 1; i <= 7; i++) {
                JSONArray arr = new JSONArray();
                arr.add(0);
                arr.add(0);
                dataBlackBall.add(arr);
            }
            String blackBall = dataBlackBall.toJSONString();

            JSONArray dataEff = new JSONArray();
            dataEff.add(0);
            dataEff.add(0);
            dataEff.add(0);
            dataEff.add(0);
            dataEff.add(0);
            dataEff.add(0);
            String eff_DH = dataEff.toJSONString();

            JSONArray DIEMDANHTHETUAN = new JSONArray();
            DIEMDANHTHETUAN.add(0);
            DIEMDANHTHETUAN.add((System.currentTimeMillis() - 8_640_000_000L));
            DIEMDANHTHETUAN.add(-1);
            DIEMDANHTHETUAN.add((System.currentTimeMillis() - 8_640_000_000L));
            String ddtt = DIEMDANHTHETUAN.toJSONString();

            JSONArray dataCa = new JSONArray();
            dataCa.add(0);
            dataCa.add(0);
            String Ca = dataCa.toJSONString();

            JSONArray dataNap = new JSONArray();
            dataNap.add(0);
            dataNap.add(0);
            dataNap.add(0);
            dataNap.add(0);
            dataNap.add(0);
            dataNap.add(0);
            String NAP = dataNap.toJSONString();

            JSONArray dataDaily = new JSONArray();
            dataDaily.add(0);
            dataDaily.add(0);
            dataDaily.add(0);
            String daily = dataDaily.toJSONString();

            ps = con.prepareStatement("insert into player"
                    + "(account_id, name, head, gender, have_tennis_space_ship, clan_id_sv" + Manager.SERVER + ", "
                    + "data_inventory, data_location, data_point, data_magic_tree, items_body, "
                    + "items_bag, items_box, items_box_lucky_round, friends, enemies, data_intrinsic, data_item_time,"
                    + "data_task, data_mabu_egg, data_charm, skills, skills_shortcut, pet_info, pet_point, pet_body, pet_skill,"
                    + "data_black_ball, thoi_vang, data_side_task,achivements, item_new_time, time_may_do,eff_danhhieu,ddanhttuan,nauCa,pointCa,"
                    + "data_bill_egg,data_egg_linhthu,data_nap,pointRuong,nhanqua)"
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setInt(3, hair);
            ps.setByte(4, gender);
            ps.setBoolean(5, false);
            ps.setInt(6, -1);
            ps.setString(7, inventory);
            ps.setString(8, location);
            ps.setString(9, point);
            ps.setString(10, magicTree);
            ps.setString(11, itemsBody);
            ps.setString(12, itemsBag);
            ps.setString(13, itemsBox);
            ps.setString(14, itemsBoxLuckyRound);
            ps.setString(15, friends);
            ps.setString(16, enemies);
            ps.setString(17, intrinsic);
            ps.setString(18, "[]");
            ps.setString(19, task);
            ps.setString(20, mabuEgg);
            ps.setString(21, charms);
            ps.setString(22, skills);
            ps.setString(23, skillsShortcut);
            ps.setString(24, petInfo);
            ps.setString(25, petPoint);
            ps.setString(26, petBody);
            ps.setString(27, petSkill);
            ps.setString(28, blackBall);
            ps.setInt(29, 10); // gold bar
            ps.setString(30, "{}");
            ps.setString(31, achive);
            ps.setString(32, itemTimeNew);
            ps.setString(33, itemMayDo);
            ps.setString(34, eff_DH);
            ps.setString(35, ddtt);
            ps.setString(36, Ca);
            ps.setInt(37, 0);
            ps.setString(38, "{}");
            ps.setString(39, "{}");
            ps.setString(40, NAP);
            ps.setInt(41, 0);
            ps.setString(42, daily);
            ps.executeUpdate();
            // Log.success("Tạo player mới thành công!");
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi tạo player mới");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                SieuHangManager.InsertNewPlayer(userId);
            } catch (Exception e) {
                Log.error(PlayerDAO.class, e, "Lỗi tạo player mới");
            }
        }
    }

    public static void addVnd(Player player, int ruby) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update account set vnd = (vnd + ?) where id = ?");
            ps.setInt(1, ruby);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Loi player " + player.name);
            e.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void updatePlayer(Player player, Connection connection) {
        if (player.isDisposed() || player.isSaving()) {
            return;
        }
        player.setSaving(true);
        try {
            int n1s = 0;
            int n2s = 0;
            int n3s = 0;
            int tv = 0;
            if (player.loaded) {
                try {
                    JSONArray dataInventory = new JSONArray();
                    // data kim lượng
                    dataInventory.add(player.inventory.gold);
                    dataInventory.add(player.inventory.gem);
                    dataInventory.add(player.inventory.ruby);
                    dataInventory.add(player.inventory.goldLimit);
                    String inventory = dataInventory.toJSONString();

                    int mapId = -1;
                    mapId = player.mapIdBeforeLogout;
                    int x = player.location.x;
                    int y = player.location.y;
                    long hp = player.nPoint.hp;
                    long mp = player.nPoint.mp;
                    if (player.isDie()) {
                        mapId = player.gender + 21;
                        x = 300;
                        y = 336;
                        hp = 1;
                        mp = 1;
                    } else {
                        if (MapService.gI().isMapDoanhTrai(mapId)
                                || MapService.gI().isMapBlackBallWar(mapId)
                                || mapId == 126
                                || mapId == 164
                                || mapId == ConstMap.CON_DUONG_RAN_DOC
                                || mapId == ConstMap.CON_DUONG_RAN_DOC_142
                                || mapId == ConstMap.CON_DUONG_RAN_DOC_143
                                || mapId == ConstMap.HOANG_MAC) {
                            mapId = player.gender + 21;
                            x = 300;
                            y = 336;
                        }
                    }
                    // data vị trí
                    JSONArray dataLocation = new JSONArray();
                    dataLocation.add(x);
                    dataLocation.add(y);
                    dataLocation.add(mapId);
                    String location = dataLocation.toJSONString();
                    // data chỉ số

                    JSONArray dataPoint = new JSONArray();
                    dataPoint.add(0);
                    dataPoint.add(mp);
                    dataPoint.add(player.nPoint.mpg);
                    dataPoint.add(player.nPoint.critg);
                    dataPoint.add(player.nPoint.limitPower);
                    dataPoint.add(player.nPoint.stamina);
                    dataPoint.add(hp);
                    dataPoint.add(player.nPoint.defg);
                    dataPoint.add(player.nPoint.tiemNang);
                    dataPoint.add(player.nPoint.maxStamina);
                    dataPoint.add(player.nPoint.dameg);
                    dataPoint.add(player.nPoint.power);
                    dataPoint.add(player.nPoint.hpg);
                    String point = dataPoint.toJSONString();
                    // data đậu thần
                    JSONArray dataMagicTree = new JSONArray();
                    dataMagicTree.add(player.magicTree.isUpgrade ? 1 : 0);
                    dataMagicTree.add(player.magicTree.lastTimeUpgrade);
                    dataMagicTree.add(player.magicTree.level);
                    dataMagicTree.add(player.magicTree.lastTimeHarvest);
                    dataMagicTree.add(player.magicTree.currPeas);
                    String magicTree = dataMagicTree.toJSONString();

                    // data body
                    JSONArray dataBody = new JSONArray();
                    for (Item item : player.inventory.itemsBody) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);
                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataBody.add(dataItem);
                    }
                    String itemsBody = dataBody.toJSONString();

                    // data bag
                    JSONArray dataBag = new JSONArray();
                    for (Item item : player.inventory.itemsBag) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            JSONArray options = new JSONArray();
                            switch (item.template.id) {
                                case 14:
                                    n1s += item.quantity;
                                    break;
                                case 15:
                                    n2s += item.quantity;
                                    break;
                                case 16:
                                    n3s += item.quantity;
                                    break;
                                case 457:
                                    tv += item.quantity;
                                    break;
                            }
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);

                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataBag.add(dataItem);
                    }
                    String itemsBag = dataBag.toJSONString();

                    // data box
                    JSONArray dataBox = new JSONArray();
                    for (Item item : player.inventory.itemsBox) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            JSONArray options = new JSONArray();
                            switch (item.template.id) {
                                case 14:
                                    n1s += item.quantity;
                                    break;
                                case 15:
                                    n2s += item.quantity;
                                    break;
                                case 16:
                                    n3s += item.quantity;
                                    break;
                                case 457:
                                    tv += item.quantity;
                                    break;
                            }
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);

                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataBox.add(dataItem);
                    }
                    String itemsBox = dataBox.toJSONString();

                    // data box crack ball
                    JSONArray dataCrackBall = new JSONArray();
                    for (Item item : player.inventory.itemsBoxCrackBall) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);
                            JSONArray options = new JSONArray();
                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataCrackBall.add(dataItem);
                    }
                    String itemsBoxLuckyRound = dataCrackBall.toJSONString();

                    // data bạn bè
                    JSONArray dataFriends = new JSONArray();
                    for (Friend f : player.friends) {
                        JSONObject friend = new JSONObject();
                        friend.put("id", f.id);
                        friend.put("name", f.name);
                        friend.put("power", f.power);
                        friend.put("head", f.head);
                        friend.put("body", f.body);
                        friend.put("leg", f.leg);
                        friend.put("bag", f.bag);
                        dataFriends.add(friend);
                    }
                    String friend = dataFriends.toJSONString();

                    // data kẻ thù
                    JSONArray dataEnemies = new JSONArray();
                    for (Friend e : player.enemies) {
                        JSONObject enemy = new JSONObject();
                        enemy.put("id", e.id);
                        enemy.put("name", e.name);
                        enemy.put("power", e.power);
                        enemy.put("head", e.head);
                        enemy.put("body", e.body);
                        enemy.put("leg", e.leg);
                        enemy.put("bag", e.bag);
                        dataEnemies.add(enemy);
                    }
                    String enemy = dataEnemies.toJSONString();

                    // data nội tại
                    JSONArray dataIntrinsic = new JSONArray();
                    dataIntrinsic.add(player.playerIntrinsic.intrinsic.id);
                    dataIntrinsic.add(player.playerIntrinsic.intrinsic.param1);
                    dataIntrinsic.add(player.playerIntrinsic.countOpen);
                    dataIntrinsic.add(player.playerIntrinsic.intrinsic.param2);
                    String intrinsic = dataIntrinsic.toJSONString();

                    // data item time
                    JSONArray dataItemTime = new JSONArray();
                    for (Integer it : new ArrayList<>(player.itemTime.isActive.keySet())) {
                        JSONObject dataTime = new JSONObject();
                        if (player.itemTime.isActive.get(it)) {
                            if (player.itemTime.lastTimes.get(it) > 0) {
                                dataTime.put("Time", player.itemTime.getLastTime(it));
                                dataTime.put("idItem", it);
                                dataItemTime.add(dataTime);
                            }
                        }
                    }
                    String itemTime = dataItemTime.toJSONString();

                    JSONArray dataItemNew = new JSONArray();
                    for (int i = 0; i < 5; i++) {
                        dataItemNew.add(player.receive[i]);
                    }
                    String itemTimeNew = dataItemNew.toJSONString();

                    JSONArray dataItemDh = new JSONArray();
                    for (int i = 0; i < 5; i++) {
                        dataItemDh.add(player.receiveMocSM[i] ? 1 : 0);
                    }
                    String DanhHieu = dataItemDh.toJSONString();

                    JSONArray dataDiemDanhThetuan = new JSONArray();
                    dataDiemDanhThetuan.add(player.DaysDiemDanh);
                    dataDiemDanhThetuan.add(player.lastTimeDiemDanh);
                    dataDiemDanhThetuan.add(player.DaysTheTuan);
                    dataDiemDanhThetuan.add(player.lastTimeRewardsTheTuan);
                    String ddtt = dataDiemDanhThetuan.toJSONString();

                    JSONArray dataMayDo = new JSONArray();
                    dataMayDo.add(player.itemTime.isMayDo
                            ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.timeMayDo))
                            : 0);
                    String itemMayDo = dataMayDo.toJSONString();

                    // data nhiệm vụ
                    JSONArray dataTask = new JSONArray();
                    dataTask.add(player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count);
                    dataTask.add(player.playerTask.taskMain.id);
                    dataTask.add(player.playerTask.taskMain.index);
                    String task = dataTask.toJSONString();

                    // data nhiệm vụ hàng ngày
                    JSONArray dataSideTask = new JSONArray();
                    dataSideTask.add(player.playerTask.sideTask.level);
                    dataSideTask.add(player.playerTask.sideTask.count);
                    dataSideTask.add(player.playerTask.sideTask.leftTask);
                    dataSideTask.add(
                            player.playerTask.sideTask.template != null ? player.playerTask.sideTask.template.id : -1);
                    dataSideTask.add(player.playerTask.sideTask.receivedTime);
                    dataSideTask.add(player.playerTask.sideTask.maxCount);
                    String sideTask = dataSideTask.toJSONString();

                    JSONArray dataAchive = new JSONArray();
                    for (Achivement a : player.playerTask.achivements) {
                        JSONObject jobj = new JSONObject();
                        jobj.put("id", a.getId());
                        jobj.put("count", a.getCount());
                        jobj.put("finish", a.isFinish() ? 1 : 0);
                        jobj.put("receive", a.isReceive() ? 1 : 0);
                        dataAchive.add(jobj);
                    }
                    String achive = dataAchive.toJSONString();

                    // data trứng bư
                    JSONObject dataMaBu = new JSONObject();
                    if (player.mabuEgg != null) {
                        dataMaBu.put("create_time", player.mabuEgg.lastTimeCreate);
                        dataMaBu.put("time_done", player.mabuEgg.timeDone);
                    }
                    String mabuEgg = dataMaBu.toJSONString();
                    // data bill

                    JSONObject dataLinhThu = new JSONObject();
                    if (player.egglinhthu != null) {
                        dataLinhThu.put("create_time", player.egglinhthu.lastTimeCreate);
                        dataLinhThu.put("time_done", player.egglinhthu.timeDone);
                        dataLinhThu.put("type", player.egglinhthu.type);
                    }
                    String eggLinhThu = dataLinhThu.toJSONString();

                    JSONObject dataBill = new JSONObject();
                    if (player.billEgg != null) {
                        dataBill.put("create_time", player.billEgg.lastTimeCreate);
                        dataBill.put("time_done", player.billEgg.timeDone);
                    }
                    String billEgg = dataBill.toJSONString();

                    // data bùa
                    JSONArray dataCharms = new JSONArray();
                    dataCharms.add(player.charms.tdTriTue);
                    dataCharms.add(player.charms.tdManhMe);
                    dataCharms.add(player.charms.tdDaTrau);
                    dataCharms.add(player.charms.tdOaiHung);
                    dataCharms.add(player.charms.tdBatTu);
                    dataCharms.add(player.charms.tdDeoDai);
                    dataCharms.add(player.charms.tdThuHut);
                    dataCharms.add(player.charms.tdDeTu);
                    dataCharms.add(player.charms.tdTriTue3);
                    dataCharms.add(player.charms.tdTriTue4);
                    dataCharms.add(player.charms.tdNoStamina);
                    String charm = dataCharms.toJSONString();

                    // data skill
                    JSONArray dataSkills = new JSONArray();
                    for (Skill skill : player.playerSkill.skills) {
                        // if (skill.skillId != -1) {
                        JSONArray dataskill = new JSONArray();
                        dataskill.add(skill.template.id);
                        dataskill.add(skill.lastTimeUseThisSkill);
                        dataskill.add(skill.point);
                        // } else {
                        // dataObject.put("temp_id", -1);
                        // dataObject.put("point", 0);
                        // dataObject.put("last_time_use", 0);
                        // }
                        dataSkills.add(dataskill);
                    }
                    String skills = dataSkills.toJSONString();

                    JSONArray dataSkillShortcut = new JSONArray();
                    // data skill shortcut
                    for (int skillId : player.playerSkill.skillShortCut) {
                        dataSkillShortcut.add(skillId);
                    }
                    String skillShortcut = dataSkillShortcut.toJSONString();

                    JSONObject jPetInfo = new JSONObject();
                    JSONObject jPetPoint = new JSONObject();
                    JSONArray jPetBody = new JSONArray();
                    JSONArray jPetSkills = new JSONArray();
                    String petInfo = jPetInfo.toJSONString();
                    String petPoint = jPetPoint.toJSONString();
                    String petBody = jPetBody.toJSONString();
                    String petSkill = jPetSkills.toJSONString();

                    JSONArray dataChallenge = new JSONArray();
                    dataChallenge.add(player.goldChallenge);
                    dataChallenge.add(player.levelWoodChest);
                    dataChallenge.add(player.receivedWoodChest ? 1 : 0);
                    String challenge = dataChallenge.toJSONString();

                    JSONArray dataSuKienTet = new JSONArray();
                    dataSuKienTet.add(player.event.getTimeCookTetCake());
                    dataSuKienTet.add(player.event.getTimeCookChungCake());
                    dataSuKienTet.add(player.event.isCookingTetCake() ? 1 : 0);
                    dataSuKienTet.add(player.event.isCookingChungCake() ? 1 : 0);
                    dataSuKienTet.add(player.event.isReceivedLuckyMoney() ? 1 : 0);
                    String skTet = dataSuKienTet.toJSONString();

                    JSONArray dataBuyLimit = new JSONArray();
                    for (int i = 0; i < player.buyLimit.length; i++) {
                        dataBuyLimit.add(player.buyLimit[i]);
                    }
                    String buyLimit = dataBuyLimit.toJSONString();

                    JSONArray dataRwLimit = new JSONArray();
                    for (int i = 0; i < player.getRewardLimit().length; i++) {
                        dataRwLimit.add(player.getRewardLimit()[i]);
                    }
                    String rwLimit = dataRwLimit.toJSONString();

                    // data pet
                    if (player.pet != null) {
                        jPetInfo.put("name", player.pet.name);
                        jPetInfo.put("gender", player.pet.gender);
                        jPetInfo.put("is_mabu", player.pet.typePet);
                        jPetInfo.put("status", player.pet.status);
                        jPetInfo.put("type_fusion", player.fusion.typeFusion);
                        jPetInfo.put("level", player.pet.getLever());
                        int timeLeftFusion = (int) (Fusion.TIME_FUSION
                                - (System.currentTimeMillis() - player.fusion.lastTimeFusion));
                        jPetInfo.put("left_fusion", timeLeftFusion < 0 ? 0 : timeLeftFusion);
                        petInfo = jPetInfo.toJSONString();

                        jPetPoint.put("power", player.pet.nPoint.power);
                        jPetPoint.put("tiem_nang", player.pet.nPoint.tiemNang);
                        jPetPoint.put("stamina", player.pet.nPoint.stamina);
                        jPetPoint.put("max_stamina", player.pet.nPoint.maxStamina);
                        jPetPoint.put("hpg", player.pet.nPoint.hpg);
                        jPetPoint.put("mpg", player.pet.nPoint.mpg);
                        jPetPoint.put("damg", player.pet.nPoint.dameg);
                        jPetPoint.put("defg", player.pet.nPoint.defg);
                        jPetPoint.put("critg", player.pet.nPoint.critg);
                        jPetPoint.put("limit_power", player.pet.nPoint.limitPower);
                        jPetPoint.put("hp", player.pet.nPoint.hp);
                        jPetPoint.put("mp", player.pet.nPoint.mp);
                        petPoint = jPetPoint.toJSONString();

                        for (Item item : player.pet.inventory.itemsBody) {
                            JSONObject dataItem = new JSONObject();
                            if (item.isNotNullItem()) {
                                dataItem.put("temp_id", item.template.id);
                                dataItem.put("quantity", item.quantity);
                                dataItem.put("create_time", item.createTime);
                                JSONArray options = new JSONArray();
                                for (ItemOption io : item.itemOptions) {
                                    JSONArray option = new JSONArray();
                                    option.add(io.optionTemplate.id);
                                    option.add(io.param);
                                    options.add(option);
                                }
                                dataItem.put("option", options);
                            } else {
                                JSONArray options = new JSONArray();
                                dataItem.put("temp_id", -1);
                                dataItem.put("quantity", 0);
                                dataItem.put("create_time", 0);
                                dataItem.put("option", options);
                            }
                            jPetBody.add(dataItem);
                        }
                        petBody = jPetBody.toJSONString();

                        for (Skill s : player.pet.playerSkill.skills) {
                            JSONArray pskill = new JSONArray();
                            if (s.skillId != -1) {
                                pskill.add(s.template.id);
                                pskill.add(s.point);
                            } else {
                                pskill.add(-1);
                                pskill.add(0);
                            }
                            jPetSkills.add(pskill);
                        }
                        petSkill = jPetSkills.toJSONString();
                    }

                    JSONArray dataCa = new JSONArray();
                    dataCa.add(player.itemTime.lastTimeNauCa);
                    dataCa.add(player.itemTime.isNauca ? 1 : 0);
                    String nauCa = dataCa.toJSONString();

                    JSONArray dataNAP = new JSONArray();
                    dataNAP.add(player.TotalPointNap);
                    dataNAP.add(player.ChangePointNap);
                    dataNAP.add(player.m1 ? 1 : 0);
                    dataNAP.add(player.m2 ? 1 : 0);
                    dataNAP.add(player.m3 ? 1 : 0);
                    dataNAP.add(player.m4 ? 1 : 0);
                    String NAP = dataNAP.toJSONString();

                    JSONArray dataBlackBall = new JSONArray();
                    // data thưởng ngọc rồng đen
                    for (int i = 1; i <= 7; i++) {
                        JSONArray data = new JSONArray();
                        data.add(player.rewardBlackBall.timeOutOfDateReward[i - 1]);
                        data.add(player.rewardBlackBall.lastTimeGetReward[i - 1]);
                        dataBlackBall.add(data);
                    }
                    String blackBall = dataBlackBall.toJSONString();

                    JSONArray poi = new JSONArray();
                    poi.add(player.pointBossThuong);
                    poi.add(player.pointBossVip);
                    poi.add(player.pointTrungThu);
                    poi.add(player.hasNhanquaPage);
                    poi.add(player.pointBanhKem);
                    poi.add(player.pointThoiVang);
                    poi.add(player.receiveNoiTai ? 1 : 0);
                    String diem = poi.toJSONString();

                    JSONArray điemaily = new JSONArray();
                    điemaily.add(player.hasBook);
                    điemaily.add(player.hasUron);
                    điemaily.add(player.dameOmega);
                    String daily = điemaily.toJSONString();

                    Gson gson = new Gson();
                    PreparedStatement ps = null;
                    try {
                        ps = connection.prepareStatement("update player set head = ?, have_tennis_space_ship = ?,"
                                + "clan_id_sv" + Manager.SERVER
                                + " = ?, data_inventory = ?, data_location = ?, data_point = ?, data_magic_tree = ?,"
                                + "items_body = ?, items_bag = ?, items_box = ?, items_box_lucky_round = ?, friends = ?,"
                                + "enemies = ?, data_intrinsic = ?, data_item_time = ?, data_task = ?, data_mabu_egg = ?,"
                                + "pet_info = ?, pet_point = ?, pet_body = ?, pet_skill = ? , power = ?, pet_power = ?, "
                                + "data_black_ball = ?, data_side_task = ?, data_charm = ?, skills = ?, skills_shortcut = ?,"
                                + "thoi_vang = ?, 1sao = ?, 2sao = ?, 3sao = ?, collection_book = ?, event_point = ?, firstTimeLogin = ?,"
                                + " challenge = ?, sk_tet = ?, buy_limit = ?, moc_nap = ?,achivements = ? , reward_limit = ?, item_new_time = ?,"
                                + " data_bill_egg = ? , data_egg_linhthu = ? , time_may_do = ?,eff_danhhieu = ?,ddanhttuan= ?,nauCa = ? , pointCa= ?,data_nap = ?,"
                                + "pointRuong= ?,pointBanh= ?,nhanqua = ? where id = ?");

                        ps.setShort(1, player.head);
                        ps.setBoolean(2, player.haveTennisSpaceShip);
                        ps.setShort(3, (short) (player.clan != null ? player.clan.id : -1));
                        ps.setString(4, inventory);
                        ps.setString(5, location);
                        ps.setString(6, point);
                        ps.setString(7, magicTree);
                        ps.setString(8, itemsBody);
                        ps.setString(9, itemsBag);
                        ps.setString(10, itemsBox);
                        ps.setString(11, itemsBoxLuckyRound);
                        ps.setString(12, friend);
                        ps.setString(13, enemy);
                        ps.setString(14, intrinsic);
                        ps.setString(15, itemTime);
                        ps.setString(16, task);
                        ps.setString(17, mabuEgg);
                        ps.setString(18, petInfo);
                        ps.setString(19, petPoint);
                        ps.setString(20, petBody);
                        ps.setString(21, petSkill);
                        ps.setLong(22, player.nPoint.power);
                        ps.setLong(23, player.pet != null ? player.pet.nPoint.power : 0);
                        ps.setString(24, blackBall);
                        ps.setString(25, sideTask);
                        ps.setString(26, charm);
                        ps.setString(27, skills);
                        ps.setString(28, skillShortcut);
                        ps.setInt(29, tv);
                        ps.setInt(30, n1s);
                        ps.setInt(31, n2s);
                        ps.setInt(32, n3s);
                        ps.setString(33, gson.toJson(player.getCollectionBook().getCards()));
                        ps.setInt(34, player.event.getEventPoint());
                        ps.setString(35, Util.toDateString(player.firstTimeLogin));
                        ps.setString(36, challenge);
                        ps.setString(37, skTet);
                        ps.setString(38, buyLimit);
                        ps.setInt(39, player.event.getMocNapDaNhan());
                        ps.setString(40, achive);
                        ps.setString(41, rwLimit);
                        ps.setString(42, itemTimeNew);
                        ps.setString(43, billEgg);
                        ps.setString(44, eggLinhThu);
                        ps.setString(45, itemMayDo);
                        ps.setString(46, DanhHieu);
                        ps.setString(47, ddtt);
                        ps.setString(48, nauCa);
                        ps.setInt(49, player.pointVongQuayThuongDe);
                        ps.setString(50, NAP);
                        ps.setInt(51, player.pointSk);
                        ps.setString(52, diem);
                        ps.setString(53, daily);
                        ps.setInt(54, (int) player.id);
                        ps.executeUpdate();
                        if (updateTimeLogout) {
                            AccountDAO.updateAccountLogout(player.getSession());
                        }
                    } catch (Exception e) {
                        System.out.println("Thằng bị lỗi là thằng: " + player.name);
                        e.printStackTrace();
                    } finally {
                        try {
                            ps.close();
                        } catch (Exception e) {
                            System.out.println("Lỗi Close");
                            // e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.error(PlayerDAO.class, e, "Lỗi save player " + player.name);
                }
            }
        } finally {
            player.setSaving(false);
        }
    }

    public static void saveName(Player player) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update player set name = ? where id = ?");
            ps.setString(1, player.name);
            ps.setInt(2, (int) player.id);
            ps.executeUpdate();
        } catch (Exception e) {
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isExistName(String name) {
        boolean exist = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGame();) {
            ps = con.prepareStatement("select * from player where name = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return exist;
    }

    public static boolean subPoin(Player player, int poin) {
        try (Connection con = DBService.gI().getConnectionForSaveData();
                PreparedStatement ps = con
                        .prepareStatement("update account set pointNap = (pointNap - ?) where id = ?")) {
            ps.setInt(1, poin);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            player.getSession().poinCharging -= poin;
            return true;
        } catch (SQLException e) {
            Log.error(PlayerDAO.class, e, "Lỗi update poin " + player.name);
        }
        return false;
    }

    public static void updatePointBoss(int plId, int index, int pointsToAdd) {
        try {
            PreparedStatement ps = DBService.gI().getConnectionForGetPlayer()
                    .prepareStatement("SELECT pointBanh FROM player WHERE id = ?");
            ps.setInt(1, plId);
            ResultSet rs = ps.executeQuery();

            JSONParser parser = new JSONParser();
            while (rs.next()) {
                String pointBanhStr = rs.getString("pointBanh");

                JSONArray pointBanhArray = (JSONArray) parser.parse(pointBanhStr);

                long firstPoint = Long.parseLong(String.valueOf(pointBanhArray.get(index)));

                pointBanhArray.set(index, firstPoint + pointsToAdd);

                PreparedStatement updatePs = DBService.gI().getConnectionForGetPlayer()
                        .prepareStatement("UPDATE player SET pointBanh = ? WHERE id = ?");
                updatePs.setString(1, pointBanhArray.toJSONString());
                updatePs.setInt(2, plId);
                updatePs.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addPower(int plId, int param) {
        try {
            PreparedStatement ps = DBService.gI().getConnectionForGetPlayer()
                    .prepareStatement("SELECT pet_info, pet_point FROM player WHERE id = ?");
            ps.setInt(1, plId);
            ResultSet rs = ps.executeQuery();
            JSONParser parser = new JSONParser();
            while (rs.next()) {
                String petInfo = rs.getString("pet_info");
                String petPointStr = rs.getString("pet_point");
                JSONObject dataObject = (JSONObject) parser.parse(petInfo);
                if (!dataObject.isEmpty()) {
                    JSONObject petPoint = (JSONObject) parser.parse(petPointStr);
                    long power = Long.parseLong(String.valueOf(petPoint.get("power")));
                    PreparedStatement updatePs = DBService.gI().getConnectionForGetPlayer()
                            .prepareStatement("UPDATE player SET pet_point = ? WHERE id = ?");
                    petPoint.put("power", (power + param));
                    updatePs.setString(1, petPoint.toJSONString());
                    updatePs.setInt(2, plId);
                    updatePs.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean ActiveUser(Player player) {
        try (Connection con = DBService.gI().getConnection();
                PreparedStatement ps = con.prepareStatement("update account set active = 1 where username = ?");) {
            player.getSession().actived = true;
            ps.setString(1, player.getSession().uu);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void subCoin(Player player, int num, String note) {
        PreparedStatement ps = null;

        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update account set coin = (coin - ?) where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            int cur = player.getSession().vnd;
            player.getSession().vnd -= num;
            addHistoryChangeMoney(player, Util.fm.format(cur), Util.fm.format(player.getSession().vnd), note);
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update thỏi vàng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void subGoldBar(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update account set thoi_vang = (thoi_vang - ?) where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            player.getSession().goldBar -= num;
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update thỏi vàng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void addHistoryReceiveGoldBar(Player player, int goldBefore, int goldAfter,
            int goldBagBefore, int goldBagAfter, int goldBoxBefore, int goldBoxAfter) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("insert into history_receive_goldbar(player_id,player_name,gold_before_receive,"
                    + "gold_after_receive,gold_bag_before,gold_bag_after,gold_box_before,gold_box_after) values (?,?,?,?,?,?,?,?)");
            ps.setInt(1, (int) player.id);
            ps.setString(2, player.name);
            ps.setInt(3, goldBefore);
            ps.setInt(4, goldAfter);
            ps.setInt(5, goldBagBefore);
            ps.setInt(6, goldBagAfter);
            ps.setInt(7, goldBoxBefore);
            ps.setInt(8, goldBoxAfter);
            ps.executeUpdate();
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update thỏi vàng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public static void updatePlayerRandomDiemDanh(int id, Item item) {
        String dataItemReward = "";
        if (item.isNotNullItem()) {
            dataItemReward += ";{" + item.template.id + ":" + item.quantity;
            if (!item.itemOptions.isEmpty()) {
                dataItemReward += "|";
                for (ItemOption io : item.itemOptions) {
                    dataItemReward += "[" + io.optionTemplate.id + ":" + io.param + "],";
                }
                dataItemReward = dataItemReward.substring(0, dataItemReward.length() - 1) + "};";
            } else {
                dataItemReward += "}";
            }
        }
        System.err.println("DATA: " + dataItemReward);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            String sql = "UPDATE account "
                    + "JOIN player ON account.id = player.account_id "
                    + "SET account.reward = CONCAT(account.reward, ?) "
                    + "WHERE player.id = ?;";
            ps = con.prepareStatement(sql);
            ps.setString(1, dataItemReward);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public static void updateItemReward(Player player) {
        String dataItemReward = "";
        for (Item item : player.getSession().itemsReward) {
            if (item.isNotNullItem()) {
                dataItemReward += "{" + item.template.id + ":" + item.quantity;
                if (!item.itemOptions.isEmpty()) {
                    dataItemReward += "|";
                    for (ItemOption io : item.itemOptions) {
                        dataItemReward += "[" + io.optionTemplate.id + ":" + io.param + "],";
                    }
                    dataItemReward = dataItemReward.substring(0, dataItemReward.length() - 1) + "};";
                }
            }
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("update account set reward = ? where id = ?");
            ps.setString(1, dataItemReward);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update phần thưởng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public static void saveBag(Connection con, Player player) {
        if (player.loaded) {
            PreparedStatement ps = null;
            try {
                JSONArray dataBag = new JSONArray();
                for (Item item : player.inventory.itemsBag) {
                    JSONObject dataItem = new JSONObject();

                    if (item.isNotNullItem()) {
                        dataItem.put("temp_id", item.template.id);
                        dataItem.put("quantity", item.quantity);
                        dataItem.put("create_time", item.createTime);
                        JSONArray options = new JSONArray();
                        for (ItemOption io : item.itemOptions) {
                            JSONArray option = new JSONArray();
                            option.add(io.optionTemplate.id);
                            option.add(io.param);
                            options.add(option);
                        }
                        dataItem.put("option", options);
                    } else {
                        JSONArray options = new JSONArray();
                        dataItem.put("temp_id", -1);
                        dataItem.put("quantity", 0);
                        dataItem.put("create_time", 0);
                        dataItem.put("option", options);
                    }
                    dataBag.add(dataItem);
                }
                String itemsBag = dataBag.toJSONString();

                ps = con.prepareStatement("update player set items_bag = ? where id = ?");
                ps.setString(1, itemsBag);
                ps.setInt(2, (int) player.id);
                ps.executeUpdate();
                ps.close();
            } catch (Exception e) {
                Log.error(PlayerDAO.class, e, "Lỗi save bag player " + player.name);
            } finally {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void LogNapTIen(String uid, String menhgia, String seri, String code, String trangthai, String tranid,
            String tinhtrang, String loaithe) {
        String UPDATE_PASS = "INSERT INTO naptien(uid, sotien, seri,code, loaithe,time, noidung, tinhtrang, tranid, magioithieu) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
                ps = con.prepareStatement(UPDATE_PASS);
                ps.setString(1, uid);
                ps.setString(2, menhgia);
                ps.setString(3, seri);
                ps.setString(4, code);
                ps.setString(5, loaithe);
                ps.setString(6, new SimpleDateFormat("HH:mm - dd/MM/yyyy").format(new Date()));
                ps.setString(7, trangthai);
                ps.setString(8, tinhtrang);
                ps.setString(9, tranid);
                ps.setString(10, "0");
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void resetManhKinhmoiNgay() {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForAutoSave();) {
            ps = con.prepareStatement("UPDATE player SET nhanqua = '[0,0,0]'");
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
            }
        }
        System.err.println("Reset mảnh Kinh thành công !");
    }

    public static boolean updateQuaPage(Player pl, String plName, int value) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveHistory()) {
            ps = con.prepareStatement("UPDATE player SET pointBanh = JSON_SET(pointBanh, '$[3]', ?) WHERE name = ?;");
            ps.setInt(1, value);
            ps.setString(2, plName);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (Exception e) {
            Service.getInstance().sendThongBao(pl, "Người chơi không tồn tại !");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void addHistoryChangeMoney(Player player, String moneyBF, String moneyAF, String note) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement(
                    "insert into abs_log(player_id,player_name,money_before,money_after,Time,note) values (?,?,?,?,?,?)");
            ps.setInt(1, (int) player.id);
            ps.setString(2, player.name);
            ps.setString(3, moneyBF);
            ps.setString(4, moneyAF);
            ps.setString(5, new SimpleDateFormat("HH:mm - dd/MM/yyyy").format(new Date()));
            ps.setString(6, note);
            ps.executeUpdate();
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update thỏi vàng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

}
