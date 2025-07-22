package nro.data;

import nro.consts.Cmd;
import nro.models.*;
import nro.models.item.HeadAvatar;
import nro.models.map.MapTemplate;
import nro.models.mob.MobTemplate;
import nro.models.npc.NpcTemplate;
import nro.models.skill.NClass;
import nro.models.skill.Skill;
import nro.models.skill.SkillTemplate;
import nro.power.Caption;
import nro.power.CaptionManager;
import nro.resources.Resources;
import nro.server.Manager;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.services.Service;
import nro.utils.FileIO;
import nro.utils.Log;
import nro.utils.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataGame {

    public static byte randomV = (byte) Util.nextInt(127);
    public static byte vsData = randomV;
    public static byte vsMap = randomV;
    public static byte vsSkill = randomV;
    public static byte vsItem = randomV;

    public static String LINK_IP_PORT = "Hades:192.168.1.59:14445:0";

    public static int[][] Arr_Head_2Fr = {
            { 1344, 1345 },
            { 1341, 1365, 1373 },
            { 1366, 1367 },
            { 1370, 1371, 1372 },
            { 1375, 1376, 1377, 1376 },
            { 1380, 1381, 1382 },
            { 1386, 1387, 1388 },
            { 1391, 1392, 1393 },
            { 1394, 1395, 1396 },
            { 1397, 1398, 1399 },
            { 1400, 1401, 1402 },
            { 1403, 1404, 1405 },
            { 1406, 1407, 1408 },
            { 1409, 1410, 1411 },
            { 1412, 1413, 1414 },
            { 1415, 1416, 1417 },
            { 1418, 1419, 1420 },
            { 1421, 1422, 1423 },
            { 1424, 1425, 1426 },
            { 1427, 1428, 1429 },
            { 1459, 1460, 1461 },
            { 1470, 1471, 1472 },
            { 1491, 1492 },
            { 1495, 1496, 1497 },
            { 1503, 1504, 1505 },
            { 1520, 1521 },
            { 1520, 1521 },
            { 1524, 1525 },
            { 1522, 1523 },
            { 1526, 1527 },
            { 1528, 1529 },
            { 1530, 1531 },
            { 1535, 1536, 1537 },
            { 1555, 1556 }, };

    public static int[] Map_NOT_Blend = { 215, 221, 223, 222 };

    private static final short[][] headHoaSieuThan = {
            { 1380, 1380, 1391, 1394, 1397, 1400 },
            { 1386, 1386, 1403, 1406, 1409, 1412 },
            { 1375, 1375, 1418, 1421, 1424, 1427 }
    };

    private static final String MOUNT_NUM = "733:1,"
            + "734:2,"
            + "735:3,"
            + "743:4,"
            + "744:5,"
            + "746:6,"
            + "795:7,"
            + "849:8,"
            + "897:9,"
            + "920:10,"
            + "1092:11,"
            + "1135:12,"
            + "1148:13,"
            + "1278:19,"
            + "1176:14,"
            + "1290:32,"
            + "1291:33,"
            + "1383:34,"
            + "1384:37,"
            + "1413:40,"
            + "1414:41,"
            + "1415:38,"
            + "1421:39";

    public static final Map MAP_MOUNT_NUM = new HashMap();

    private static final byte[] dart = FileIO.readFile("AbsReSource/Data_File/Update_Data/dart");
    private static final byte[] arrow = FileIO.readFile("AbsReSource/Data_File/Update_Data/arrow");
    private static final byte[] effect = FileIO.readFile("AbsReSource/Data_File/Update_Data/effect");
    private static final byte[] image = FileIO.readFile("AbsReSource/Data_File/Update_Data/image");
    private static final byte[] skill = FileIO.readFile("AbsReSource/Data_File/Update_Data/skill");

    static {
        String[] array = MOUNT_NUM.split(",");
        for (String str : array) {
            String[] data = str.split(":");
            short num = (short) (Short.parseShort(data[1]) + 30000);
            MAP_MOUNT_NUM.put(data[0], num);
        }
        Resources.getInstance().init();
    }

    public static void sendVersionGame(Session session) {
        Message msg;
        try {
            msg = Service.getInstance().messageNotMap((byte) 4);
            msg.writer().writeByte(vsData);
            msg.writer().writeByte(vsMap);
            msg.writer().writeByte(vsSkill);
            msg.writer().writeByte(vsItem);
            msg.writer().writeByte(0);
            List<Caption> captions = CaptionManager.getInstance().getCaptions();
            msg.writer().writeByte(captions.size());
            for (Caption caption : captions) {
                msg.writer().writeLong(caption.getPower());
            }
            msg.writer().writeInt(Arr_Head_2Fr.length);
            for (int i = 0; i < Arr_Head_2Fr.length; i++) {
                int sze = Arr_Head_2Fr[i].length;
                msg.writer().writeShort(sze);
                for (int j = 0; j < sze; j++) {
                    msg.writer().writeInt(Arr_Head_2Fr[i][j]);

                }
            }

            msg.writer().writeShort(Map_NOT_Blend.length);
            for (int i = 0; i < Map_NOT_Blend.length; i++) {
                msg.writer().writeShort(Map_NOT_Blend[i]);
            }

            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // vcData
    public static void updateData(Session session) {
        Message msg;
        try {
            msg = new Message(-87);
            msg.writer().writeByte(vsData);
            msg.writer().writeInt(dart.length);
            msg.writer().write(dart);
            msg.writer().writeInt(arrow.length);
            msg.writer().write(arrow);
            msg.writer().writeInt(effect.length);
            msg.writer().write(effect);
            msg.writer().writeInt(image.length);
            msg.writer().write(image);
            byte[] dataPart = PartManager.getInstance().getData();
            msg.writer().writeInt(dataPart.length);
            msg.writer().write(dataPart);
            msg.writer().writeInt(skill.length);
            msg.writer().write(skill);

            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    // vcMap
    public static void createMap(Session session) {
        Message msg;
        try {
            msg = Service.getInstance().messageNotMap((byte) 6);
            msg.writer().writeByte(vsMap);
            msg.writer().writeByte(Manager.MAP_TEMPLATES.length);
            for (MapTemplate temp : Manager.MAP_TEMPLATES) {
                msg.writer().writeUTF(temp.name);
            }
            msg.writer().writeByte(Manager.NPC_TEMPLATES.size());
            for (NpcTemplate temp : Manager.NPC_TEMPLATES) {
                msg.writer().writeUTF(temp.name);
                msg.writer().writeShort(temp.head);
                msg.writer().writeShort(temp.body);
                msg.writer().writeShort(temp.leg);
                msg.writer().writeByte(0);
            }
            msg.writer().writeByte(Manager.MOB_TEMPLATES.size());
            for (MobTemplate temp : Manager.MOB_TEMPLATES) {
                msg.writer().writeByte(temp.type);
                msg.writer().writeUTF(temp.name);
                msg.writer().writeLong(temp.hp);
                msg.writer().writeByte(temp.rangeMove);
                msg.writer().writeByte(temp.speed);
                msg.writer().writeByte(temp.dartType);
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(DataGame.class, e);
        }
    }

    // vcSkill
    public static void updateSkill(Session session) {
        Message msg;
        try {
            msg = new Message(-28);

            msg.writer().writeByte(7);
            msg.writer().writeByte(vsSkill);
            msg.writer().writeByte(0); // count skill option

            msg.writer().writeByte(Manager.NCLASS.size());
            for (NClass nClass : Manager.NCLASS) {
                msg.writer().writeUTF(nClass.name);
                msg.writer().writeByte(nClass.skillTemplatess.size());
                for (SkillTemplate skillTemp : nClass.skillTemplatess) {
                    msg.writer().writeByte(skillTemp.id);
                    msg.writer().writeUTF(skillTemp.name);
                    msg.writer().writeByte(skillTemp.maxPoint);
                    msg.writer().writeByte(skillTemp.manaUseType);
                    msg.writer().writeByte(skillTemp.type);
                    msg.writer().writeShort(skillTemp.iconId);
                    msg.writer().writeUTF(skillTemp.damInfo);
                    msg.writer().writeUTF(skillTemp.description);
                    if (skillTemp.id != 0) {
                        msg.writer().writeByte(skillTemp.skillss.size());
                        for (Skill skill : skillTemp.skillss) {
                            msg.writer().writeShort(skill.skillId);
                            msg.writer().writeByte(skill.point);
                            msg.writer().writeLong(skill.powRequire);
                            msg.writer().writeShort(skill.manaUse);
                            if (skill.skillId == 1) {
                                msg.writer().writeInt(5000);
                            } else {
                                msg.writer().writeInt(skill.coolDown);
                            }
                            msg.writer().writeShort(skill.dx);
                            msg.writer().writeShort(skill.dy);
                            msg.writer().writeByte(skill.maxFight);
                            msg.writer().writeShort(skill.damage);
                            msg.writer().writeShort(skill.price);
                            msg.writer().writeUTF(skill.moreInfo);
                        }
                    } else {
                        // Thêm 2 skill trống 105, 106
                        msg.writer().writeByte(skillTemp.skillss.size() + 2);
                        for (Skill skill : skillTemp.skillss) {
                            msg.writer().writeShort(skill.skillId);
                            msg.writer().writeByte(skill.point);
                            msg.writer().writeLong(skill.powRequire);
                            msg.writer().writeShort(skill.manaUse);
                            if (skill.skillId == 1) {
                                msg.writer().writeInt(5000);
                            } else {
                                msg.writer().writeInt(skill.coolDown);
                            }
                            msg.writer().writeShort(skill.dx);
                            msg.writer().writeShort(skill.dy);
                            msg.writer().writeByte(skill.maxFight);
                            msg.writer().writeShort(skill.damage);
                            msg.writer().writeShort(skill.price);
                            msg.writer().writeUTF(skill.moreInfo);
                        }
                        for (int i = 105; i <= 106; i++) {
                            msg.writer().writeShort(i);
                            msg.writer().writeByte(0);
                            msg.writer().writeLong(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeInt(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeByte(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeUTF("");
                        }
                    }
                }
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendDataImageVersion(Session session) {
        Message msg;
        try {
            msg = new Message(-111);
            msg.writer().write(
                    FileIO.readFile("AbsReSource/Data_File/Data_Img_Version/x" + session.zoomLevel + "/img_version"));
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(DataGame.class, e);
        }
    }

    public static void sendDataItemBG(Session session) {
        Message msg;
        try {
            byte[] item_bg = FileIO.readFile("AbsReSource/bg_data");
            msg = new Message(Cmd.ITEM_BACKGROUND);
            msg.writer().write(item_bg);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public static void sendTileSetInfo(Session session) {
        Message msg;
        try {
            msg = new Message(-82);
            msg.writer().write(FileIO.readFile("AbsReSource/tile_set_info"));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    // data vẽ map
    public static void sendMapTemp(Session session, int id) {
        Message msg;
        try {
            msg = Service.getInstance().messageNotMap(Cmd.REQUEST_MAPTEMPLATE);
            msg.writer().write(FileIO.readFile("AbsReSource/Map/Tile/" + id));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(DataGame.class, e);
        }
    }

    // head-avatar
    public static void sendHeadAvatar(Message msg) {
        try {
            msg.writer().writeShort(Manager.HEAD_AVATARS.size());
            for (HeadAvatar ha : Manager.HEAD_AVATARS) {
                msg.writer().writeShort(ha.headId);
                msg.writer().writeShort(ha.avatarId);
            }
        } catch (Exception e) {
        }
    }

    public static void sendLinkIP(Session session) {
        Message msg = null;
        try {
            msg = new Message(-29);
            msg.writer().writeByte(2);
            msg.writer().writeUTF(LINK_IP_PORT + ",0,0");
            msg.writer().writeByte(1);
            session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }
}
