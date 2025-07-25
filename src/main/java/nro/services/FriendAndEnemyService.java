package nro.services;

import nro.consts.Cmd;
import nro.consts.ConstNpc;
import nro.models.Part;
import nro.models.PartManager;
import nro.models.map.war.NamekBallWar;
import nro.models.player.Enemy;
import nro.models.player.Friend;
import nro.models.player.Player;
import nro.server.Client;
import nro.server.io.Message;
import nro.services.func.ChangeMapService;
import nro.services.func.PVPServcice;
import nro.utils.Log;
import nro.utils.Util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 
 */
public class FriendAndEnemyService {

    private static final byte OPEN_LIST = 0;

    private static final byte MAKE_FRIEND = 1;
    private static final byte REMOVE_FRIEND = 2;

    private static final byte REVENGE = 1;
    private static final byte REMOVE_ENEMY = 2;

    private static FriendAndEnemyService i;

    public static FriendAndEnemyService gI() {
        if (i == null) {
            i = new FriendAndEnemyService();
        }
        return i;
    }

    public void controllerFriend(Player player, Message msg) {
        try {
            byte action = msg.reader().readByte();
            switch (action) {
                case OPEN_LIST:
                    openListFriend(player);
                    break;
                case MAKE_FRIEND:
                    makeFriend(player, msg.reader().readInt());
                    break;
                case REMOVE_FRIEND:
                    removeFriend(player, msg.reader().readInt());
                    break;
            }
        } catch (IOException ex) {

        }
    }

    public void controllerEnemy(Player player, Message msg) {
        try {
            byte action = msg.reader().readByte();
            switch (action) {
                case OPEN_LIST:
                    openListEnemy(player);
                    break;
                case REVENGE:
                    int id = msg.reader().readInt();
                    boolean flag = false;
                    for (Enemy e : player.enemies) {
                        if (e.id == id) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        Player enemy = Client.gI().getPlayer(id);
                        if (enemy != null) {
                            PVPServcice.gI().openSelectRevenge(player, enemy);
                        } else {
                            Service.getInstance().sendThongBao(player, "Đang offline");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case REMOVE_ENEMY:
                    removeEnemy(player, msg.reader().readInt());
                    break;
            }
        } catch (IOException ex) {

        }
    }

    private void reloadFriend(Player player) {
        for (Friend f : player.friends) {
            Player pl = null;
            if ((pl = Client.gI().getPlayerByUser(f.id)) != null || (pl = Client.gI().getPlayer(f.name)) != null) {
                try {
                    f.power = pl.nPoint.power;
                    f.head = pl.getHead();
                    f.body = pl.getBody();
                    f.leg = pl.getLeg();
                    f.bag = (byte) pl.getFlagBag();
                } catch (Exception e) {
                }
                f.online = true;
            } else {
                f.online = false;
            }
        }
    }

    private void reloadEnemy(Player player) {
        for (Enemy e : player.enemies) {
            Player pl = null;
            if ((pl = Client.gI().getPlayerByUser(e.id)) != null || (pl = Client.gI().getPlayer(e.name)) != null) {
                try {
                    e.power = pl.nPoint.power;
                    e.head = pl.getHead();
                    e.body = pl.getBody();
                    e.leg = pl.getLeg();
                    e.bag = (byte) pl.getFlagBag();
                } catch (Exception ex) {
                }
                e.online = true;
            } else {
                e.online = false;
            }
        }
    }

    private void openListFriend(Player player) {
        reloadFriend(player);
        Message msg;
        try {
            msg = new Message(Cmd.FRIEND);
            msg.writer().writeByte(OPEN_LIST);
            msg.writer().writeByte(player.friends.size());
            for (Friend f : player.friends) {
                msg.writer().writeInt(f.id);
                msg.writer().writeShort(f.head);
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(f.head);
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(f.body);
                msg.writer().writeShort(f.leg);
                msg.writer().writeByte(f.bag);
                msg.writer().writeUTF(f.name);
                msg.writer().writeBoolean(Client.gI().getPlayer((int) f.id) != null);
                msg.writer().writeUTF(Util.numberToMoney(f.power));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(FriendAndEnemyService.class, e);
        }
    }

    private void openListEnemy(Player player) {
        reloadEnemy(player);
        Message msg;
        try {
            msg = new Message(-99);
            msg.writer().writeByte(OPEN_LIST);
            msg.writer().writeByte(player.enemies.size());
            for (Enemy e : player.enemies) {
                msg.writer().writeInt(e.id);
                msg.writer().writeShort(e.head);
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(e.head);
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(e.body);
                msg.writer().writeShort(e.leg);
                msg.writer().writeShort(e.bag);
                msg.writer().writeUTF(e.name);
                msg.writer().writeUTF(Util.numberToMoney(e.power));
                msg.writer().writeBoolean(Client.gI().getPlayer((int) e.id) != null);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(FriendAndEnemyService.class, e);
        }
    }

    private void makeFriend(Player player, int playerId) {// debug
        boolean madeFriend = false;
        if (player.friends.size() >= 10) {
            Service.getInstance().sendThongBao(player, "Kết bạn được 10 đứa nữa thôi địt mẹ mày");
        } else {
            for (Friend friend : player.friends) {
                if (friend.id == playerId) {
                    Service.getInstance().sendThongBao(player, "Đã có trong danh sách bạn bè");
                    madeFriend = true;
                    break;
                }
            }
            if (!madeFriend) {
                Player pl = Client.gI().getPlayer(playerId);
                if (pl != null) {
                    String npcSay;
                    if (player.friends.size() >= 5) {
                        npcSay = "Bạn có muốn kết bạn với " + pl.name + " với phí là 5 ngọc ?";
                    } else {
                        npcSay = "Bạn có muốn kết bạn với " + pl.name + " ?";
                    }
                    NpcService.gI().createMenuConMeo(player, ConstNpc.MAKE_FRIEND, -1, npcSay, new String[]{"Đồng ý", "Từ chối"}, playerId);
                }
            }
        }

    }

    private void removeFriend(Player player, int playerId) {
        for (int i = 0; i < player.friends.size(); i++) {
            if (player.friends.get(i).id == playerId) {
                Service.getInstance().sendThongBao(player, "Đã xóa thành công "
                        + player.friends.get(i).name + " khỏi danh sách bạn");
                Message msg;
                try {
                    msg = new Message(Cmd.FRIEND);
                    msg.writer().writeByte(REMOVE_FRIEND);
                    msg.writer().writeInt((int) player.friends.get(i).id);
                    player.sendMessage(msg);
                    msg.cleanup();
                } catch (Exception e) {
                }
                player.friends.remove(i);
                break;
            }
        }
    }

    public void removeEnemy(Player player, int playerId) {
        for (int i = 0; i < player.enemies.size(); i++) {
            if (player.enemies.get(i).id == playerId) {
                player.enemies.remove(i);
                break;
            }
        }
        openListEnemy(player);
    }

    public void chatPrivate(Player player, Message msg) {
        if (Util.canDoWithTime(player.lastTimeChatPrivate, 5000)) {
            player.lastTimeChatPrivate = System.currentTimeMillis();
            try {
                int playerId = msg.reader().readInt();
                String text = msg.reader().readUTF();
                Player pl = Client.gI().getPlayer(playerId);
                if (pl != null) {
                    Service.getInstance().chatPrivate(player, pl, text);
                }
            } catch (Exception e) {
            }
        }
    }

    public void acceptMakeFriend(Player player, int playerId) {
        Player pl = Client.gI().getPlayer(playerId);
        if (pl != null) {
            Friend friend = new Friend();
            friend.id = (int) pl.id;
            friend.name = pl.name;
            friend.power = pl.nPoint.power;
            friend.head = pl.getHead();
            friend.body = pl.getBody();
            friend.leg = pl.getLeg();
            friend.bag = (byte) pl.getFlagBag();
            player.friends.add(friend);
            Service.getInstance().sendThongBao(player, "Kết bạn thành công");
            Service.getInstance().chatPrivate(player, pl, player.name + " vừa mới kết bạn với " + pl.name);
//            TaskService.gI().checkDoneTaskMakeFriend(player, pl);
        } else {
            Service.getInstance().sendThongBao(player, "Không tìm thấy hoặc đang Offline, vui lòng thử lại sau");
        }
    }

    public void goToPlayerWithYardrat(Player player, Message msg) {
        try {
            Player pl = Client.gI().getPlayer(msg.reader().readInt());
            if (player.isHoldNamecBall) {
                NamekBallWar.gI().dropBall(player);
                return;
            }
            if (pl != null) {
                if (player.isAdmin() || player.nPoint.teleport) {
                    int mapid = pl.zone.map.mapId;
                    List<Integer> clone = Arrays.asList(114, 115, 116, 117, 118,
                            119, 120, 121, 122, 123,
                            53, 54, 55, 56, 57,
                            58, 59, 60, 61, 62,
                            160, 161, 162, 163, 164,
                            124, 125, 126, 127, 128, 155, 206, 207, 208, 209, 210, 211);
                    if (clone.contains(mapid)) {
                        Service.getInstance().sendThongBao(player, "Tele cái máu nhoàn");
                        return;
                    }
                    if (!pl.itemTime.isActive(385) || player.isAdmin()) {
                        if (player.isAdmin() || !pl.zone.isFullPlayer()) {
                            ChangeMapService.gI().changeMapYardrat(player, pl.zone, pl.location.x + Util.nextInt(-5, 5), pl.location.y);
                        } else {
                            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Yêu cầu trang bị có khả năng dịch chuyển tức thời");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addEnemy(Player player, Player enemy) {
        boolean hadEnemy = false;
        for (Enemy ene : player.enemies) {
            if (ene.id == ene.id) {
                hadEnemy = true;
            }
        }
        if (!hadEnemy) {
            Enemy e = new Enemy();
            e.id = (int) enemy.id;
            e.name = enemy.name;
            e.power = enemy.nPoint.power;
            e.head = enemy.getHead();
            e.body = enemy.getBody();
            e.leg = enemy.getLeg();
            e.bag = (byte) enemy.getFlagBag();
            player.enemies.add(e);
        }
    }
}
