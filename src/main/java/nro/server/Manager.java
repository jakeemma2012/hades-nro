package nro.server;

import lombok.Getter;
import nro.attr.Attribute;
import nro.attr.AttributeManager;
import nro.attr.AttributeTemplateManager;
import nro.card.CardManager;
import nro.consts.ConstItem;
import nro.consts.ConstMap;
import nro.consts.ConstPlayer;
import nro.data.DataGame;
import nro.event.Event;
import nro.jdbc.DBService;
import nro.jdbc.daos.AccountDAO;
import nro.jdbc.daos.ShopDAO;
import nro.lib.RandomCollection;
import nro.manager.*;
import nro.models.*;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.intrinsic.Intrinsic;
import nro.models.item.*;
import nro.models.map.*;
import nro.models.mob.MobReward;
import nro.models.mob.MobTemplate;
import nro.models.npc.Npc;
import nro.models.npc.NpcFactory;
import nro.models.npc.NpcTemplate;
import nro.models.player.Referee;
import nro.models.shop.Shop;
import nro.models.skill.NClass;
import nro.models.skill.Skill;
import nro.models.skill.SkillTemplate;
import nro.models.task.SideTaskTemplate;
import nro.models.task.SubTaskMain;
import nro.models.task.TaskMain;
import nro.noti.NotiManager;
import nro.power.CaptionManager;
import nro.power.PowerLimitManager;
import nro.services.ItemService;
import nro.services.MapService;
import nro.utils.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nro.services.func.GiftCodeLoader;

public class Manager {

    private static Manager i;

    public static byte SERVER = 1;
    public static byte SECOND_WAIT_LOGIN = 20;
    public static int MAX_PER_IP = 3;
    public static int MAX_PLAYER = 1000;
    public static byte RATE_EXP_SERVER = 10;
    public static int EVENT_SEVER = 0;
    public static String DOMAIN = "";
    public static String SERVER_NAME = "";
    public static int EVENT_COUNT_THAN_HUY_DIET = 0;
    public static int EVENT_COUNT_QUY_LAO_KAME = 0;
    public static int EVENT_COUNT_THAN_MEO = 0;
    public static int EVENT_COUNT_THUONG_DE = 0;
    public static int EVENT_COUNT_THAN_VU_TRU = 0;
    public static String loginHost;
    public static int loginPort;
    public static int apiPort = 8080;
    public static int bossGroup = 5;
    public static int workerGroup = 10;
    public static String apiKey = "abcdef";
    public static String executeCommand;
    public static boolean debug;
    public static short[][] POINT_MABU_MAP = {
        {196, 259},
        {340, 259},
        {413, 236},
        {532, 259}
    };

    public static final List<String> TOP_PLAYERS = new ArrayList<>();

    public static MapTemplate[] MAP_TEMPLATES;
    public static final List<nro.models.map.Map> MAPS = new ArrayList<>();
    public static final List<ItemOptionTemplate> ITEM_OPTION_TEMPLATES = new ArrayList<>();
    public static final List<MobReward> MOB_REWARDS = new ArrayList<>();
    public static final RandomCollection<ItemLuckyRound> LUCKY_ROUND_REWARDS = new RandomCollection<>();
    public static final RandomCollection<ItemLuckyRound> MAY_GAP_THU_THUONG = new RandomCollection<>();
    public static final List<ItemTemplate> ITEM_TEMPLATES = new ArrayList<>();
    public static final List<MobTemplate> MOB_TEMPLATES = new ArrayList<>();
    public static final List<NpcTemplate> NPC_TEMPLATES = new ArrayList<>();
    public static final List<String> CAPTIONS = new ArrayList<>();
    public static final List<TaskMain> TASKS = new ArrayList<>();
    public static final List<SideTaskTemplate> SIDE_TASKS_TEMPLATE = new ArrayList<>();
    public static final List<Intrinsic> INTRINSICS = new ArrayList<>();
    public static final List<Intrinsic> INTRINSIC_TD = new ArrayList<>();
    public static final List<Intrinsic> INTRINSIC_NM = new ArrayList<>();
    public static final List<Intrinsic> INTRINSIC_XD = new ArrayList<>();
    public static final List<HeadAvatar> HEAD_AVATARS = new ArrayList<>();
    public static final List<FlagBag> FLAGS_BAGS = new ArrayList<>();
    public static final List<CaiTrang> CAI_TRANGS = new ArrayList<>();
    public static final List<NClass> NCLASS = new ArrayList<>();
    public static final List<Npc> NPCS = new ArrayList<>();
    public static List<Shop> SHOPS = new ArrayList<>();
    public static final List<Clan> CLANS = new ArrayList<>();
    public static final ByteArrayOutputStream[] cache = new ByteArrayOutputStream[4];
    public static final RandomCollection<Integer> HONG_DAO_CHIN = new RandomCollection<>();
    public static final RandomCollection<Integer> HOP_QUA_TET = new RandomCollection<>();
    public static int QUANTITY_NTL = 4;
    public List<Item> itemsReward;
    public List<Integer> mocPointItemsRewardsList = new ArrayList<>();

    public static Manager gI() {
        if (i == null) {
            i = new Manager();
        }
        return i;
    }

    private Manager() {
        try {
            loadProperties();
        } catch (IOException ex) {
            Log.error(Manager.class, ex, "Lỗi load properites");
            System.exit(0);
        }
        long st = System.currentTimeMillis();
        for (int i = 0; i <= 26; i++) {
            loadDatabase(i);
        }
        Log.log("Tổng thời gian load database: " + (System.currentTimeMillis() - st) + "(ms)");

        NpcFactory.createNpcConMeo();
        NpcFactory.createNpcRongThieng();
        Event.initEvent(DBService.gI().gameConfig.getEvent());
        if (Event.isEvent()) {
            Event.getInstance().init();
        }
//        initRandomItem();
//        NamekBallManager.gI().initBall();
    }

    private void initRandomItem() {
        HONG_DAO_CHIN.add(50, ConstItem.CHU_GIAI);
        HONG_DAO_CHIN.add(50, ConstItem.HONG_NGOC);

        HOP_QUA_TET.add(10, ConstItem.DIEU_RONG);
        HOP_QUA_TET.add(10, ConstItem.DAO_RANG_CUA);
        HOP_QUA_TET.add(10, ConstItem.QUAT_BA_TIEU);
        HOP_QUA_TET.add(10, ConstItem.BUA_MJOLNIR);
        HOP_QUA_TET.add(10, ConstItem.BUA_STORMBREAKER);
        HOP_QUA_TET.add(10, ConstItem.DINH_BA_SATAN);
        HOP_QUA_TET.add(10, ConstItem.CHOI_PHU_THUY);
        HOP_QUA_TET.add(10, ConstItem.MANH_AO);
        HOP_QUA_TET.add(10, ConstItem.MANH_QUAN);
        HOP_QUA_TET.add(10, ConstItem.MANH_GIAY);
        HOP_QUA_TET.add(10, ConstItem.MANH_NHAN);
        HOP_QUA_TET.add(10, ConstItem.MANH_GANG_TAY);
        HOP_QUA_TET.add(8, ConstItem.PHUONG_HOANG_LUA);
        HOP_QUA_TET.add(7, ConstItem.NOEL_2022_GOKU);
        HOP_QUA_TET.add(7, ConstItem.NOEL_2022_CADIC);
        HOP_QUA_TET.add(7, ConstItem.NOEL_2022_POCOLO);
        HOP_QUA_TET.add(20, ConstItem.CUONG_NO_2);
        HOP_QUA_TET.add(20, ConstItem.BO_HUYET_2);
        HOP_QUA_TET.add(20, ConstItem.BO_KHI_2);
    }

    private void initMap() {
        int[][] tileTyleTop = readTileIndexTileType(ConstMap.TILE_TOP);
        for (MapTemplate mapTemp : MAP_TEMPLATES) {
            int[][] tileMap = readTileMap(mapTemp.id);
            int[] tileTop = tileTyleTop[mapTemp.tileId - 1];
            nro.models.map.Map map = null;
            if (mapTemp.id == 126) {
                map = new SantaCity(mapTemp.id,
                        mapTemp.name, mapTemp.planetId, mapTemp.tileId, mapTemp.bgId,
                        mapTemp.bgType, mapTemp.type, tileMap, tileTop,
                        mapTemp.zones, mapTemp.isMapOffline(),
                        mapTemp.maxPlayerPerZone, mapTemp.wayPoints, mapTemp.effectMaps);
                SantaCity santaCity = (SantaCity) map;
                santaCity.timer(22, 0, 0, 3600000);
            } else {
                map = new nro.models.map.Map(mapTemp.id,
                        mapTemp.name, mapTemp.planetId, mapTemp.tileId, mapTemp.bgId,
                        mapTemp.bgType, mapTemp.type, tileMap, tileTop,
                        mapTemp.zones, mapTemp.isMapOffline(),
                        mapTemp.maxPlayerPerZone, mapTemp.wayPoints, mapTemp.effectMaps);
            }
            if (map != null) {
                MAPS.add(map);
                map.initMob(mapTemp.mobTemp, mapTemp.mobLevel, mapTemp.mobHp, mapTemp.mobX, mapTemp.mobY);
                map.initNpc(mapTemp.npcId, mapTemp.npcX, mapTemp.npcY, mapTemp.npcAvatar);
                new Thread(map, "Update map " + map.mapName).start();
            }
        }
        Referee r = new Referee();
        r.initReferee();
        Log.success("Init map thành công!");
    }

    public void loadDatabase(int type) {
        long st = System.currentTimeMillis();
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGame();) {
            switch (type) {
                case 0 -> //load part
                    PartManager.getInstance().load();
                case 1 -> //load pet
                    PetFollowManager.gI().load();
                case 2 -> //load map template
                {
                    ps = con.prepareStatement("select count(id) from map_template", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    rs = ps.executeQuery();
                    if (rs.first()) {
                        int countRow = rs.getShort(1);
                        MAP_TEMPLATES = new MapTemplate[countRow];
                        ps = con.prepareStatement("select * from map_template");
                        rs = ps.executeQuery();
                        short i = 0;
                        while (rs.next()) {
                            MapTemplate mapTemplate = new MapTemplate();
                            int mapId = rs.getInt("id");
                            String mapName = rs.getString("name");
                            mapTemplate.id = mapId;
                            mapTemplate.name = mapName;
                            //load data
                            dataArray = (JSONArray) jv.parse(rs.getString("data"));
                            mapTemplate.type = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            mapTemplate.planetId = Byte.parseByte(String.valueOf(dataArray.get(1)));
                            mapTemplate.bgType = Byte.parseByte(String.valueOf(dataArray.get(2)));
                            mapTemplate.tileId = Byte.parseByte(String.valueOf(dataArray.get(3)));
                            mapTemplate.bgId = Byte.parseByte(String.valueOf(dataArray.get(4)));
                            dataArray.clear();
                            mapTemplate.zones = rs.getByte("zones");
                            mapTemplate.maxPlayerPerZone = rs.getByte("max_player");
                            //load waypoints
                            dataArray = (JSONArray) jv.parse(rs.getString("waypoints")
                                    .replaceAll("\\[\"\\[", "[[")
                                    .replaceAll("\\]\"\\]", "]]")
                                    .replaceAll("\",\"", ",")
                            );
                            for (int j = 0; j < dataArray.size(); j++) {
                                WayPoint wp = new WayPoint();
                                JSONArray dtwp = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                                wp.name = String.valueOf(dtwp.get(0));
                                wp.minX = Short.parseShort(String.valueOf(dtwp.get(1)));
                                wp.minY = Short.parseShort(String.valueOf(dtwp.get(2)));
                                wp.maxX = Short.parseShort(String.valueOf(dtwp.get(3)));
                                wp.maxY = Short.parseShort(String.valueOf(dtwp.get(4)));
                                wp.isEnter = Byte.parseByte(String.valueOf(dtwp.get(5))) == 1;
                                wp.isOffline = Byte.parseByte(String.valueOf(dtwp.get(6))) == 1;
                                wp.goMap = Short.parseShort(String.valueOf(dtwp.get(7)));
                                wp.goX = Short.parseShort(String.valueOf(dtwp.get(8)));
                                wp.goY = Short.parseShort(String.valueOf(dtwp.get(9)));
                                mapTemplate.wayPoints.add(wp);
                                dtwp.clear();
                            }
                            dataArray.clear();
                            //load mobs
                            dataArray = (JSONArray) jv.parse(rs.getString("mobs").replaceAll("\\\"", ""));
                            mapTemplate.mobTemp = new byte[dataArray.size()];
                            mapTemplate.mobLevel = new byte[dataArray.size()];
                            mapTemplate.mobHp = new long[dataArray.size()];
                            mapTemplate.mobX = new short[dataArray.size()];
                            mapTemplate.mobY = new short[dataArray.size()];
                            for (int j = 0; j < dataArray.size(); j++) {
                                JSONArray dtm = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                                mapTemplate.mobTemp[j] = Byte.parseByte(String.valueOf(dtm.get(0)));
                                mapTemplate.mobLevel[j] = Byte.parseByte(String.valueOf(dtm.get(1)));
                                mapTemplate.mobHp[j] = Long.parseLong(String.valueOf(dtm.get(2)));
                                mapTemplate.mobX[j] = Short.parseShort(String.valueOf(dtm.get(3)));
                                mapTemplate.mobY[j] = Short.parseShort(String.valueOf(dtm.get(4)));
                                dtm.clear();
                            }
                            dataArray.clear();
                            //load npc
                            dataArray = (JSONArray) jv.parse(rs.getString("npcs").replaceAll("\\\"", ""));
                            mapTemplate.npcId = new byte[dataArray.size()];
                            mapTemplate.npcX = new short[dataArray.size()];
                            mapTemplate.npcY = new short[dataArray.size()];
                            mapTemplate.npcAvatar = new short[dataArray.size()];
                            for (int j = 0; j < dataArray.size(); j++) {
                                JSONArray dtn = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                                mapTemplate.npcId[j] = Byte.parseByte(String.valueOf(dtn.get(0)));
                                mapTemplate.npcX[j] = Short.parseShort(String.valueOf(dtn.get(1)));
                                mapTemplate.npcY[j] = Short.parseShort(String.valueOf(dtn.get(2)));
                                mapTemplate.npcAvatar[j] = Short.parseShort(String.valueOf(dtn.get(3)));
                                dtn.clear();
                            }
                            dataArray.clear();

                            dataArray = (JSONArray) jv.parse(rs.getString("effect"));
                            for (int j = 0; j < dataArray.size(); j++) {
                                EffectMap em = new EffectMap();
                                dataObject = (JSONObject) jv.parse(dataArray.get(j).toString());
                                em.setKey(String.valueOf(dataObject.get("key")));
                                em.setValue(String.valueOf(dataObject.get("value")));
                                mapTemplate.effectMaps.add(em);
                            }
                            if (Manager.EVENT_SEVER == 3) {
                                EffectMap em = new EffectMap();
                                em.setKey("beff");
                                em.setValue("11");
                                mapTemplate.effectMaps.add(em);
                            }
                            dataArray.clear();
                            dataObject.clear();
                            MAP_TEMPLATES[i++] = mapTemplate;
                        }
                        Log.success("Load map template thành công (" + MAP_TEMPLATES.length + ")");
                    }
                }
                case 3 -> {
                    //load skill
                    ps = con.prepareStatement("select * from skill_template order by nclass_id, slot");
                    rs = ps.executeQuery();
                    byte nClassId = -1;
                    NClass nClass = null;
                    while (rs.next()) {
                        byte id = rs.getByte("nclass_id");
                        if (id != nClassId) {
                            nClassId = id;
                            nClass = new NClass();
                            nClass.name = id == ConstPlayer.TRAI_DAT ? "Trái Đất" : id == ConstPlayer.NAMEC ? "Namếc" : "Xayda";
                            nClass.classId = nClassId;
                            NCLASS.add(nClass);
                        }
                        SkillTemplate skillTemplate = new SkillTemplate();
                        skillTemplate.classId = nClassId;
                        skillTemplate.id = rs.getByte("id");
                        skillTemplate.name = rs.getString("name");
                        skillTemplate.maxPoint = rs.getByte("max_point");
                        skillTemplate.manaUseType = rs.getByte("mana_use_type");
                        skillTemplate.type = rs.getByte("type");
                        skillTemplate.iconId = rs.getShort("icon_id");
                        skillTemplate.damInfo = rs.getString("dam_info");
                        skillTemplate.description = rs.getString("desc");
                        nClass.skillTemplatess.add(skillTemplate);

                        dataArray = (JSONArray) JSONValue.parse(
                                rs.getString("skills"));
                        for (int j = 0; j < dataArray.size(); j++) {
                            JSONObject dts = (JSONObject) jv.parse(String.valueOf(dataArray.get(j)));
                            Skill skill = new Skill();
                            skill.template = skillTemplate;
                            skill.skillId = Short.parseShort(String.valueOf(dts.get("id")));
                            skill.point = Byte.parseByte(String.valueOf(dts.get("point")));
                            skill.powRequire = Long.parseLong(String.valueOf(dts.get("power_require")));
                            skill.manaUse = Integer.parseInt(String.valueOf(dts.get("mana_use")));
                            skill.coolDown = Integer.parseInt(String.valueOf(dts.get("cool_down")));
                            skill.dx = Integer.parseInt(String.valueOf(dts.get("dx")));
                            skill.dy = Integer.parseInt(String.valueOf(dts.get("dy")));
                            skill.maxFight = Integer.parseInt(String.valueOf(dts.get("max_fight")));
                            skill.damage = Short.parseShort(String.valueOf(dts.get("damage")));
                            skill.price = Short.parseShort(String.valueOf(dts.get("price")));
                            skill.moreInfo = String.valueOf(dts.get("info"));
                            skillTemplate.skillss.add(skill);
                        }
                    }
                    rs.close();
                    ps.close();
                    Log.success("Load skill thành công (" + NCLASS.size() + ")");
                }
                case 4 -> {
                    //load head avatar
                    ps = con.prepareStatement("select * from head_avatar");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        HeadAvatar headAvatar = new HeadAvatar(rs.getInt("head_id"), rs.getInt("avatar_id"));
                        HEAD_AVATARS.add(headAvatar);
                    }
                    rs.close();
                    ps.close();
                    Log.success("Load head avatar thành công (" + HEAD_AVATARS.size() + ")");

                    //load cải trang
                    ps = con.prepareStatement("select * from cai_trang");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        CaiTrang caiTrang = new CaiTrang(rs.getInt("id_temp"),
                                rs.getInt("head"), rs.getInt("body"), rs.getInt("leg"), rs.getInt("bag"));
                        CAI_TRANGS.add(caiTrang);
                    }
                    rs.close();
                    ps.close();
                    Log.success("Load cải trang thành công (" + CAI_TRANGS.size() + ")");
                }
                case 5 -> {
                    //load intrinsic
                    ps = con.prepareStatement("select * from intrinsic");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        Intrinsic intrinsic = new Intrinsic();
                        intrinsic.id = rs.getByte("id");
                        intrinsic.name = rs.getString("name");
                        intrinsic.paramFrom1 = rs.getShort("param_from_1");
                        intrinsic.paramTo1 = rs.getShort("param_to_1");
                        intrinsic.paramFrom2 = rs.getShort("param_from_2");
                        intrinsic.paramTo2 = rs.getShort("param_to_2");
                        intrinsic.icon = rs.getShort("icon");
                        intrinsic.gender = rs.getByte("gender");
                        switch (intrinsic.gender) {
                            case ConstPlayer.TRAI_DAT:
                                INTRINSIC_TD.add(intrinsic);
                                break;
                            case ConstPlayer.NAMEC:
                                INTRINSIC_NM.add(intrinsic);
                                break;
                            case ConstPlayer.XAYDA:
                                INTRINSIC_XD.add(intrinsic);
                                break;
                            default:
                                INTRINSIC_TD.add(intrinsic);
                                INTRINSIC_NM.add(intrinsic);
                                INTRINSIC_XD.add(intrinsic);
                        }
                        INTRINSICS.add(intrinsic);
                    }
                    rs.close();
                    ps.close();
                    Log.success("Load intrinsic thành công (" + INTRINSICS.size() + ")");
                }
                case 6 -> {
                    //load task
                    ps = con.prepareStatement("SELECT id, task_main_template.name, detail, "
                            + "task_sub_template.name AS 'sub_name', max_count, notify, npc_id, map "
                            + "FROM task_main_template JOIN task_sub_template ON task_main_template.id = "
                            + "task_sub_template.task_main_id");
                    rs = ps.executeQuery();
                    int taskId = -1;
                    TaskMain task = null;
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        if (id != taskId) {
                            taskId = id;
                            task = new TaskMain();
                            task.id = taskId;
                            task.name = rs.getString("name");
                            task.detail = rs.getString("detail");
                            TASKS.add(task);
                        }
                        SubTaskMain subTask = new SubTaskMain();
                        subTask.name = rs.getString("sub_name");
                        subTask.maxCount = rs.getShort("max_count");
                        subTask.notify = rs.getString("notify");
                        subTask.npcId = rs.getByte("npc_id");
                        subTask.mapId = rs.getShort("map");
                        task.subTasks.add(subTask);
                    }
                    rs.close();
                    ps.close();
                    Log.success("Load task thành công (" + TASKS.size() + ")");
                }
                case 7 -> {
                    //load side task
                    ps = con.prepareStatement("select * from side_task_template");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        SideTaskTemplate sideTask = new SideTaskTemplate();
                        sideTask.id = rs.getInt("id");
                        sideTask.name = rs.getString("name");
                        String[] mc1 = rs.getString("max_count_lv1").split("-");
                        String[] mc2 = rs.getString("max_count_lv2").split("-");
                        String[] mc3 = rs.getString("max_count_lv3").split("-");
                        String[] mc4 = rs.getString("max_count_lv4").split("-");
                        String[] mc5 = rs.getString("max_count_lv5").split("-");
                        sideTask.count[0][0] = Integer.parseInt(mc1[0]);
                        sideTask.count[0][1] = Integer.parseInt(mc1[1]);
                        sideTask.count[1][0] = Integer.parseInt(mc2[0]);
                        sideTask.count[1][1] = Integer.parseInt(mc2[1]);
                        sideTask.count[2][0] = Integer.parseInt(mc3[0]);
                        sideTask.count[2][1] = Integer.parseInt(mc3[1]);
                        sideTask.count[3][0] = Integer.parseInt(mc4[0]);
                        sideTask.count[3][1] = Integer.parseInt(mc4[1]);
                        sideTask.count[4][0] = Integer.parseInt(mc5[0]);
                        sideTask.count[4][1] = Integer.parseInt(mc5[1]);
                        SIDE_TASKS_TEMPLATE.add(sideTask);
                    }
                    rs.close();
                    ps.close();
                    Log.success("Load side task thành công (" + SIDE_TASKS_TEMPLATE.size() + ")");
                }
                case 8 -> {
                    //load item template
                    ps = con.prepareStatement("select * from item_template");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        ItemTemplate itemTemp = new ItemTemplate();
                        itemTemp.id = rs.getShort("id");
                        itemTemp.type = rs.getByte("type");
                        itemTemp.gender = rs.getByte("gender");
                        itemTemp.name = rs.getString("name");
                        itemTemp.description = rs.getString("description");
                        itemTemp.iconID = rs.getShort("icon_id");
                        itemTemp.part = rs.getShort("part");
                        itemTemp.isUpToUp = rs.getBoolean("is_up_to_up");
                        itemTemp.strRequire = rs.getInt("power_require");
                        ITEM_TEMPLATES.add(itemTemp);
                    }
                    rs.close();
                    ps.close();
                    Log.success("Load item template thành công (" + ITEM_TEMPLATES.size() + ")");

                    //load item option template
                    ITEM_OPTION_TEMPLATES.clear();
                    ps = con.prepareStatement("select id, name from item_option_template");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        ItemOptionTemplate optionTemp = new ItemOptionTemplate();
                        optionTemp.id = rs.getInt("id");
                        optionTemp.name = rs.getString("name");
                        ITEM_OPTION_TEMPLATES.add(optionTemp);
                    }
                    rs.close();
                    ps.close();
                    Log.success("Load map item option template thành công (" + ITEM_OPTION_TEMPLATES.size() + ")");
                }
                case 9 -> {

                    //load flag bag
                    ps = con.prepareStatement("select * from flag_bag");
                    rs = ps.executeQuery();
                    FLAGS_BAGS.clear();
                    while (rs.next()) {
                        FlagBag flagBag = new FlagBag();
                        flagBag.id = rs.getByte("id");
                        flagBag.name = rs.getString("name");
                        flagBag.gold = rs.getInt("gold");
                        flagBag.gem = rs.getInt("gem");
                        flagBag.iconId = rs.getShort("icon_id");
                        String[] iconData = rs.getString("icon_data").split(",");
                        org.json.JSONArray jar = new org.json.JSONArray(rs.getString("des"));

                        for (int i = 0; i < jar.length(); i++) {
                            org.json.JSONObject itemObj = jar.getJSONObject(i);
                            flagBag.itemoptions.add(new ItemOption(itemObj.getInt("id"), itemObj.getInt("param")));
                        }
                        flagBag.iconEffect = new short[iconData.length];
                        for (int j = 0; j < iconData.length; j++) {
                            flagBag.iconEffect[j] = Short.parseShort(iconData[j].trim());
                        }
                        FLAGS_BAGS.add(flagBag);
                    }
                    rs.close();
                    ps.close();
                    Log.success("Load flag bag thành công (" + FLAGS_BAGS.size() + ")");
                }
                case 10 -> {
                    //load shop
                    SHOPS = ShopDAO.getShops(con);
                    Log.success("Load shop thành công (" + SHOPS.size() + ")");
                }
                case 11 -> {
                    //load reward lucky round
                    File folder = new File("AbsReSource/Data_File/data_lucky_round_reward");
//            for (File fileEntry : folder.listFiles()) {
//                if (!fileEntry.isDirectory()) {
//                    String line = Files.readAllLines(fileEntry.toPath()).get(0);
//                    JSONArray jdata = (JSONArray) JSONValue.parse(line);
//                    double sum = 0;
//                    for (int i = 0; i < jdata.size(); i++) {
//                        JSONObject obj = (JSONObject) jdata.get(i);
//                        int id = ((Long) obj.get("id")).intValue();
//                        double percent = ((Double) obj.get("percent"));
//                        JSONArray jOptions = (JSONArray) obj.get("options");
//                        ItemLuckyRound item = new ItemLuckyRound();
//                        item.temp = ItemService.gI().getTemplate(id);
//                        item.percent = percent;
//                        sum += percent;
//                        for (int j = 0; j < jOptions.size(); j++) {
//                            JSONObject jOption = (JSONObject) jOptions.get(j);
//                            int oID = ((Long) jOption.get("id")).intValue();
//                            String strParam = (String) jOption.get("param");
//                            ItemOptionLuckyRound io = new ItemOptionLuckyRound();
//                            ItemOption itemOption = new ItemOption(oID, 0);
//                            io.itemOption = itemOption;
//                            String[] param = strParam.split("-");
//                            io.param1 = Integer.parseInt(param[0]);
//                            if (param.length == 2) {
//                                io.param2 = Integer.parseInt(param[1]);
//                            }
//                            item.itemOptions.add(io);
//                        }
//                        LUCKY_ROUND_REWARDS.add(percent, item);
//                    }
//                    LUCKY_ROUND_REWARDS.add(((double) 100) - sum, null);
//                    Log.success("Load reward lucky round thành công! " + sum);
//                }
//            }

                    // load máy gắp thú thường
                    folder = new File("AbsReSource/Data_File/data_lucky_round_reward");
                    for (File fileEntry : folder.listFiles()) {
                        if (!fileEntry.isDirectory()) {
                            BufferedReader br = new BufferedReader(new FileReader(fileEntry));
                            String line = null;
                            while ((line = br.readLine()) != null) {
                                line = line.replaceAll("[{}\\[\\]]", "");
                                String[] arrSub = line.split("\\|");
                                String[] data1 = arrSub[0].split(":");
                                ItemLuckyRound item = new ItemLuckyRound();
                                item.temp = ItemService.gI().getTemplate(Integer.parseInt(data1[0]));
                                item.ratio = Integer.parseInt(data1[1]);
                                item.typeRatio = Integer.parseInt(data1[2]);
                                if (arrSub.length > 1) {
                                    String[] data2 = arrSub[1].split(";");
                                    for (String str : data2) {
                                        String[] data = str.split(":");
                                        ItemOptionLuckyRound io = new ItemOptionLuckyRound();
                                        ItemOption itemOption = new ItemOption(Integer.parseInt(data[0]), 0);
                                        io.itemOption = itemOption;
                                        String[] param = data[1].split("-");
                                        io.param1 = Integer.parseInt(param[0]);
                                        if (param.length == 2) {
                                            io.param2 = Integer.parseInt(param[1]);
                                        }
                                        item.itemOptions.add(io);
                                    }
                                }
                                MAY_GAP_THU_THUONG.add(item.ratio, item);

                            }
                            Log.success("Load reward máy gắp thú thường round thành công! ");
                        }
                    }
                    //load reward mob
                    folder = new File("resources/data/nro/data_mob_reward");
//                    for (File fileEntry : folder.listFiles()) {
//                        if (!fileEntry.isDirectory()) {
//                            BufferedReader br = new BufferedReader(new FileReader(fileEntry));
//                            String line = null;
//                            while ((line = br.readLine()) != null) {
//                                line = line.replaceAll("[{}\\[\\]]", "");
//                                String[] arrSub = line.split("\\|");
//                                int tempId = Integer.parseInt(arrSub[0]);
//                                boolean haveMobReward = false;
//                                MobReward mobReward = null;
//                                for (MobReward m : MOB_REWARDS) {
//                                    if (m.tempId == tempId) {
//                                        mobReward = m;
//                                        haveMobReward = true;
//                                        break;
//                                    }
//                                }
//                                if (!haveMobReward) {
//                                    mobReward = new MobReward();
//                                    mobReward.tempId = tempId;
//                                    MOB_REWARDS.add(mobReward);
//                                }
//                                for (int i = 1; i < arrSub.length; i++) {
//                                    String[] dataItem = arrSub[i].split(",");
//                                    String[] mapsId = dataItem[0].split(";");
//
//                                    String[] itemId = dataItem[1].split(";");
//                                    for (int j = 0; j < itemId.length; j++) {
//                                        ItemReward itemReward = new ItemReward();
//                                        itemReward.mapId = new int[mapsId.length];
//                                        for (int k = 0; k < mapsId.length; k++) {
//                                            itemReward.mapId[k] = Integer.parseInt(mapsId[k]);
//                                        }
//                                        itemReward.tempId = Integer.parseInt(itemId[j]);
//                                        itemReward.ratio = Integer.parseInt(dataItem[2]);
//                                        itemReward.typeRatio = Integer.parseInt(dataItem[3]);
//                                        itemReward.forAllGender = Integer.parseInt(dataItem[4]) == 1;
//                                        if (itemReward.tempId == 76
//                                                || itemReward.tempId == 188
//                                                || itemReward.tempId == 189
//                                                || itemReward.tempId == 190) {
//                                            mobReward.goldRewards.add(itemReward);
//                                        } else if (itemReward.tempId == 380) {
//                                            mobReward.capsuleKyBi.add(itemReward);
//                                        } else if (itemReward.tempId >= 663 && itemReward.tempId <= 667) {
//                                            mobReward.foods.add(itemReward);
//                                        } else if (itemReward.tempId == 590) {
//                                            mobReward.biKieps.add(itemReward);
//                                        } else {
//                                            mobReward.itemRewards.add(itemReward);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
                    Log.success("Load reward lucky round thành công (" + MOB_REWARDS.size() + ")");
                }
                case 12 -> {
                    //load mob template
                    MOB_TEMPLATES.clear();
                    ps = con.prepareStatement("select * from mob_template");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        MobTemplate mobTemp = new MobTemplate();
                        mobTemp.id = rs.getByte("id");
                        mobTemp.type = rs.getByte("type");
                        mobTemp.name = rs.getString("name");
                        mobTemp.hp = rs.getLong("hp");
                        mobTemp.rangeMove = rs.getByte("range_move");
                        mobTemp.speed = rs.getByte("speed");
                        mobTemp.dartType = rs.getByte("dart_type");
                        mobTemp.percentDame = rs.getByte("percent_dame");
                        mobTemp.percentTiemNang = rs.getByte("percent_tiem_nang");
                        MOB_TEMPLATES.add(mobTemp);
                    }
                    rs.close();
                    ps.close();
                    Log.success("Load mob template thành công (" + MOB_TEMPLATES.size() + ")");
                }
                case 13 -> {
                    //load npc template
                    NPC_TEMPLATES.clear();
                    ps = con.prepareStatement("select * from npc_template");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        NpcTemplate npcTemp = new NpcTemplate();
                        npcTemp.id = rs.getByte("id");
                        npcTemp.name = rs.getString("name");
                        npcTemp.head = rs.getShort("head");
                        npcTemp.body = rs.getShort("body");
                        npcTemp.leg = rs.getShort("leg");
                        NPC_TEMPLATES.add(npcTemp);
                    }
                    rs.close();
                    ps.close();
                    Log.success("Load npc template thành công (" + NPC_TEMPLATES.size() + ")");
                    initMap();
                }
                case 14 -> {
                    //load clan
                    ps = con.prepareStatement("select * from clan_sv" + SERVER);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        Clan clan = new Clan();
                        clan.id = rs.getInt("id");
                        clan.name = rs.getString("name");
                        clan.slogan = rs.getString("slogan");
                        clan.imgId = rs.getByte("img_id");
                        clan.powerPoint = rs.getLong("power_point");
                        clan.maxMember = rs.getByte("max_member");
                        clan.clanPoint = rs.getInt("clan_point");
                        clan.level = rs.getInt("level");
                        clan.createTime = (int) (rs.getTimestamp("create_time").getTime() / 1000);
                        dataArray = (JSONArray) JSONValue.parse(rs.getString("members"));
                        for (int i = 0; i < dataArray.size(); i++) {
                            dataObject = (JSONObject) JSONValue.parse(String.valueOf(dataArray.get(i)));
                            ClanMember cm = new ClanMember();
                            cm.clan = clan;
                            cm.id = Integer.parseInt(String.valueOf(dataObject.get("id")));
                            cm.name = String.valueOf(dataObject.get("name"));
                            cm.head = Short.parseShort(String.valueOf(dataObject.get("head")));
                            cm.body = Short.parseShort(String.valueOf(dataObject.get("body")));
                            cm.leg = Short.parseShort(String.valueOf(dataObject.get("leg")));
                            cm.role = Byte.parseByte(String.valueOf(dataObject.get("role")));
                            cm.donate = Integer.parseInt(String.valueOf(dataObject.get("donate")));
                            cm.receiveDonate = Integer.parseInt(String.valueOf(dataObject.get("receive_donate")));
                            cm.memberPoint = Integer.parseInt(String.valueOf(dataObject.get("member_point")));
                            cm.clanPoint = Integer.parseInt(String.valueOf(dataObject.get("clan_point")));
                            cm.joinTime = Integer.parseInt(String.valueOf(dataObject.get("join_time")));
                            cm.timeAskPea = Long.parseLong(String.valueOf(dataObject.get("ask_pea_time")));
                            try {
                                cm.powerPoint = Long.parseLong(String.valueOf(dataObject.get("power")));
                            } catch (Exception e) {
                            }
                            clan.addClanMember(cm);
                        }
                        CLANS.add(clan);
                        dataArray.clear();
                        dataObject.clear();
                    }
                    rs.close();
                    ps.close();

                    ps = con.prepareStatement("select id from clan_sv" + SERVER + " order by id desc limit 1");
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        Clan.NEXT_ID = rs.getInt("id") + 1;
                    }

                    rs.close();
                    ps.close();

                    Log.success("Load clan thành công (" + CLANS.size() + "), clan next id: " + Clan.NEXT_ID);
                }
                case 15 -> {
                    CardManager.cardTemplates.clear();
                    CardManager.getInstance().load();
                }
                case 16 ->
                    PowerLimitManager.getInstance().load();
                case 17 ->
                    CaptionManager.getInstance().load();
                case 18 ->
                    AttributeTemplateManager.getInstance().load();
                case 19 ->
                    loadAttributeServer();
                case 20 ->
                    loadEventCount();
                case 21 ->
                    EffectEventManager.gI().load();
                case 22 ->
                    NotiManager.getInstance().load();
                case 23 ->
                    ConsignManager.getInstance().load();
                case 24 ->
                    AchiveManager.getInstance().load();
                case 25 ->
                    MiniPetManager.gI().load();
                case 26 -> {
                    ps = con.prepareStatement("select * from abs_moc_thuongde");
                    rs = ps.executeQuery();
                    this.itemsReward = new ArrayList<>();
                    this.mocPointItemsRewardsList = new ArrayList<>();
                    while (rs.next()) {
                       mocPointItemsRewardsList.add(rs.getInt("moc"));
                        String[] itemMoc = rs.getString("Items").split(";");
                        for (String itemInfo : itemMoc) {
                            if (itemInfo == null || itemInfo.equals("") || itemInfo.equals("\"\"")) {
                                continue;
                            }
                            String[] subItemInfo = itemInfo.replaceAll("[{}\\[\\]]", "").split("\\|");
                            String[] baseInfo = subItemInfo[0].split(":");
                            int itemId = Integer.parseInt(baseInfo[0]);
                            int quantity = Integer.parseInt(baseInfo[1]);
                            Item item = ItemService.gI().createNewItem((short) itemId, quantity);
                            if (subItemInfo.length == 2) {
                                String[] options = subItemInfo[1].split(",");
                                for (String opt : options) {
                                    if (opt == null || opt.equals("")) {
                                        continue;
                                    }
                                    String[] optInfo = opt.split(":");
                                    int tempIdOption = Integer.parseInt(optInfo[0]);
                                    int param = Integer.parseInt(optInfo[1]);
                                    item.itemOptions.add(new ItemOption(tempIdOption, param));
                                }
                            }
                            this.itemsReward.add(item);
                        }
                    }
                    System.err.println("Load Mốc Thượng đế thành công : " + this.itemsReward.size());
                }
                default -> {
                }
            }
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception e) {
            Log.error(Manager.class, e, "Lỗi load database");
            System.exit(0);
        }
    }

    public static MapTemplate getMapTemplate(int mapID) {
        for (MapTemplate map : MAP_TEMPLATES) {
            if (map.id == mapID) {
                return map;
            }
        }
        return null;
    }

    public static void loadEventCount() {
        try {
            PreparedStatement ps = DBService.gI().getConnectionForGame().prepareStatement("select * from event where server =" + SERVER);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                EVENT_COUNT_QUY_LAO_KAME = rs.getInt("kame");
                EVENT_COUNT_THAN_HUY_DIET = rs.getInt("bill");
                EVENT_COUNT_THAN_MEO = rs.getInt("karin");
                EVENT_COUNT_THUONG_DE = rs.getInt("thuongde");
                EVENT_COUNT_THAN_VU_TRU = rs.getInt("thanvutru");
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void updateEventCount() {
        try {
            PreparedStatement ps = DBService.gI().getConnectionForGame().prepareStatement("UPDATE event SET kame = ?, bill = ?, karin = ?, thuongde = ?, thanvutru = ? WHERE `server` = ?");
            ps.setInt(1, EVENT_COUNT_QUY_LAO_KAME);
            ps.setInt(3, EVENT_COUNT_THAN_HUY_DIET);
            ps.setInt(2, EVENT_COUNT_THAN_MEO);
            ps.setInt(4, EVENT_COUNT_THUONG_DE);
            ps.setInt(5, EVENT_COUNT_THAN_VU_TRU);
            ps.setInt(6, SERVER);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadAttributeServer() {
        try {
            AttributeManager am = new AttributeManager();
            PreparedStatement ps = DBService.gI().getConnectionForGame().prepareStatement("SELECT * FROM `attribute_server`");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int templateID = rs.getInt("attribute_template_id");
                int value = rs.getInt("value");
                int time = rs.getInt("time");
                Attribute at = Attribute.builder()
                        .id(id)
                        .templateID(templateID)
                        .value(value)
                        .time(time)
                        .build();
                am.add(at);
            }
            rs.close();
            ps.close();
            ServerManager.gI().setAttributeManager(am);
        } catch (SQLException ex) {
            Logger.getLogger(Manager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateAttributeServer() {
        try {
            AttributeManager am = ServerManager.gI().getAttributeManager();
            List<Attribute> attributes = am.getAttributes();
            PreparedStatement ps = DBService.gI().getConnectionForAutoSave().prepareStatement("UPDATE `attribute_server` SET `attribute_template_id` = ?, `value` = ?, `time` = ? WHERE `id` = ?;");
            synchronized (attributes) {
                for (Attribute at : attributes) {
                    try {
                        if (at.isChanged()) {
                            ps.setInt(1, at.getTemplate().getId());
                            ps.setInt(2, at.getValue());
                            ps.setInt(3, at.getTime());
                            ps.setInt(4, at.getId());
                            ps.addBatch();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            ps.executeBatch();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(Manager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadProperties() throws IOException {
        DBService.gI().setDB();
    }

    /**
     * @param tileTypeFocus tile type: top, bot, left, right...
     * @return [tileMapId][tileType]
     */
    private int[][] readTileIndexTileType(int tileTypeFocus) {
        int[][] tileIndexTileType = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("AbsReSource/tile_set_info"));
            int numTileMap = dis.readByte();
            tileIndexTileType = new int[numTileMap][];
            for (int i = 0; i < numTileMap; i++) {
                int numTileOfMap = dis.readByte();
                for (int j = 0; j < numTileOfMap; j++) {
                    int tileType = dis.readInt();
                    int numIndex = dis.readByte();
                    if (tileType == tileTypeFocus) {
                        tileIndexTileType[i] = new int[numIndex];
                    }
                    for (int k = 0; k < numIndex; k++) {
                        int typeIndex = dis.readByte();
                        if (tileType == tileTypeFocus) {
                            tileIndexTileType[i][k] = typeIndex;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.error(MapService.class, e);
        }
        return tileIndexTileType;
    }

    /**
     * @param mapId mapId
     * @return tile map for paint
     */
    private int[][] readTileMap(int mapId) {
        int[][] tileMap = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("AbsReSource/Map/Tile/" + mapId));
            int w = dis.readByte();
            int h = dis.readByte();
            tileMap = new int[h][w];
            for (int i = 0; i < tileMap.length; i++) {
                for (int j = 0; j < tileMap[i].length; j++) {
                    tileMap[i][j] = dis.readByte();
                }
            }
            dis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tileMap;
    }

    //service*******************************************************************
    public static Clan getClanById(int id) throws Exception {
        for (Clan clan : CLANS) {
            if (clan.id == id) {
                return clan;
            }
        }
        throw new Exception("Không tìm thấy clan id: " + id);
    }

    public static void addClan(Clan clan) {
        CLANS.add(clan);
    }

    public static int getNumClan() {
        return CLANS.size();

    }

    public static CaiTrang getCaiTrangByItemId(int itemId) {
        for (CaiTrang caiTrang : CAI_TRANGS) {
            if (caiTrang.tempId == itemId) {
                return caiTrang;
            }
        }
        return null;
    }

    public static MobTemplate getMobTemplateByTemp(int mobTempId) {
        for (MobTemplate mobTemp : MOB_TEMPLATES) {
            if (mobTemp.id == mobTempId) {
                return mobTemp;
            }
        }
        return null;
    }

    public static void reloadShop() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnection();) {
            // load shop
            SHOPS = ShopDAO.getShops(con);
            System.out.println("Thông báo tải dữ liệu shop thành công (" + SHOPS.size() + ")\n");
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
//            System.exit(0);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
