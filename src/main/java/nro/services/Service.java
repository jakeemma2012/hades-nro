package nro.services;

import nro.consts.Cmd;
import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.consts.ConstPlayer;
import nro.data.DataGame;
import nro.jdbc.daos.AccountDAO;
import nro.manager.TopManager;
import nro.models.Part;
import nro.models.PartManager;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.map.dungeon.zones.ZDungeon;
import nro.models.mob.Mob;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.power.Caption;
import nro.power.CaptionManager;
import nro.server.Client;
import nro.server.Manager;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.services.func.Input;
import nro.utils.FileIO;
import nro.utils.Log;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import nro.art.ServerLog;
import nro.jdbc.DBService;
import nro.jdbc.daos.PlayerDAO;
import nro.manager.PetFollowManager;
import nro.manager.TopToTask;
import nro.models.TopPlayer;
import nro.models.boss.Boss;
import nro.models.boss.BossManager;
import nro.models.boss.list_boss.WhisTop;
import nro.models.npc.Npc;
import nro.models.npc.specialnpc.BillEgg;
import nro.models.npc.specialnpc.EggLinhThu;
import nro.models.npc.specialnpc.MabuEgg;
import nro.services.func.ChangeMapService;
import nro.services.func.CombineServiceNew;

import static nro.manager.TopPlayerManager.GetTopNap;
import static nro.manager.TopPlayerManager.GetTopPower;
import nro.models.boss.BossFactory;
import nro.models.clan.ClanMember;
import nro.models.player.PetFollow;
import nro.models.shop.ItemShop;
import nro.models.shop.Shop;
import nro.server.Maintenance;
import nro.services.func.GiftCodeLoader;
import nro.services.func.TransactionService;
import nro.utils.SkillUtil;

public class Service {

    private static Service instance;

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public int x;
    public int y;

    public void RemoveEffPlayer(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(1);
            me.writer().writeInt((int) player.id);
            me.writer().writeShort(id);
            if (player.isPl()) {
                player.getSession().sendMessage(me);
            }
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeTitle(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            if (player.isPl()) {
                player.getSession().sendMessage(me);
            }
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addEffectChar(Player pl, int id, int layer, int loop, int loopcount, int stand, int xC, int yC) {
        if (!pl.idEffChar.contains(id)) {
            pl.idEffChar.add(id);
        } else {
            return;
        }
        try {
            Message msg = new Message(-128);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(id);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(loop);
            msg.writer().writeShort(loopcount);
            msg.writer().writeByte(stand);
            msg.writer().writeByte(xC);
            msg.writer().writeByte(yC);
            sendMessAllPlayerInMap(pl.zone, msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToRegisterScr(Session session) {
        Message message;
        try {
            message = new Message(42);
            message.writer().writeByte(0);
            session.sendMessage(message);
            message.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTextTime(Player pl, byte id, String name, short time) {
        Message msg;
        try {
            msg = new Message(Cmd.MESSAGE_TIME);
            msg.writer().writeByte(id);
            msg.writer().writeUTF(name);
            msg.writer().writeShort(time);
            // sendMessAllPlayerInMap(pl.zone, msg);
            pl.getSession().sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendMessAllPlayer(Message msg) {
        msg.transformData();
        PlayerService.gI().sendMessageAllPlayer(msg);
    }

    public void sendMessAllPlayerIgnoreMe(Player player, Message msg) {
        msg.transformData();
        PlayerService.gI().sendMessageIgnore(player, msg);
    }

    public void sendPetFollow(Player player, short smallId) {
        Message msg;
        try {
            if (player != null) {
                msg = new Message(31);
                PetFollow pw = PetFollowManager.gI().findByID(smallId);
                msg.writer().writeInt((int) player.id);
                if (smallId == 0) {
                    msg.writer().writeByte(0);// type 0
                } else {
                    msg.writer().writeByte(1);// type 1
                    msg.writer().writeShort(pw.getIconID());
                    msg.writer().writeByte(1);
                    msg.writer().writeByte(pw.getNFrame());
                    for (int i = 0; i < pw.getNFrame(); i++) {
                        msg.writer().writeByte(i);
                    }
                    msg.writer().writeShort(pw.getWidth());
                    msg.writer().writeShort(pw.getHeight());
                }
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LinkService(Player player, int iconId, String text, String p2, String caption) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(1);
            msg.writer().writeUTF(p2); // link sex
            msg.writer().writeUTF(caption);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPetFollowToMe(Player me, Player pl) {
        Item linhThu = pl.inventory.itemsBody.get(10);
        if (!linhThu.isNotNullItem()) {
            return;
        }
        short smallId = (short) (linhThu.template.iconID - 1);
        Message msg;
        try {
            msg = new Message(31);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(smallId);
            msg.writer().writeByte(1);
            int[] fr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
            msg.writer().writeByte(fr.length);
            for (int i = 0; i < fr.length; i++) {
                msg.writer().writeByte(fr[i]);
            }
            msg.writer().writeShort(smallId == 15067 ? 65 : 75);
            msg.writer().writeShort(smallId == 15067 ? 65 : 75);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Mabu14hAttack(Boss mabu, Player plAttack, int x, int y, byte skillId) {
        mabu.isUseSpeacialSkill = true;
        mabu.lastTimeUseSpeacialSkill = System.currentTimeMillis();
        try {
            Message msg = new Message(51);
            msg.writer().writeInt((int) mabu.id);
            msg.writer().writeByte(skillId);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            if (skillId == 1) {
                msg.writer().writeByte(1);
                long dame = plAttack.injured(mabu,
                        (long) (mabu.nPoint.getDameAttack(false, false) * (skillId == 1 ? 1.5 : 1)),
                        false, false);
                msg.writer().writeInt((int) plAttack.id);
                msg.writer().writeLong(dame);
            } else if (skillId == 0) {
                List<Player> listAttack = mabu.getListPlayerAttack(70);
                msg.writer().writeByte(listAttack.size());
                for (int i = 0; i < listAttack.size(); i++) {
                    Player pl = listAttack.get(i);
                    long dame = pl.injured(mabu, mabu.nPoint.getDameAttack(false, false), false, false);
                    msg.writer().writeInt((int) pl.id);
                    msg.writer().writeLong(dame);
                }
                listAttack.clear();
            }
            sendMessAllPlayerInMap(mabu.zone, msg);
            mabu.isUseSpeacialSkill = false;
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendMabuEat(Player plHold, short... point) {
        Message msg;
        try {
            msg = new Message(52);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) plHold.id);
            msg.writer().writeShort(point[0]);
            msg.writer().writeShort(point[1]);
            sendMessAllPlayerInMap(plHold.zone, msg);
            plHold.location.x = point[0];
            plHold.location.y = point[1];
            MapService.gI().sendPlayerMove(plHold);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void removeMabuEat(Player plHold) {
        PlayerService.gI().changeAndSendTypePK(plHold, ConstPlayer.NON_PK);
        plHold.effectSkill.isHoldMabu = false;
        plHold.effectSkill.isTaskHoldMabu = -1;
        Message msg;
        try {
            msg = new Message(52);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) plHold.id);
            sendMessAllPlayerInMap(plHold.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void eatPlayer(Boss mabu, Player plHold) {
        mabu.isUseSpeacialSkill = true;
        mabu.lastTimeUseSpeacialSkill = System.currentTimeMillis();
        plHold.effectSkill.isTaskHoldMabu = 1;
        plHold.effectSkill.lastTimeHoldMabu = System.currentTimeMillis();
        try {
            Message msg = new Message(52);
            msg.writer().writeByte(2);
            msg.writer().writeInt((int) mabu.id);
            msg.writer().writeInt((int) plHold.id);
            sendMessAllPlayerInMap(mabu.zone, msg);
            mabu.isUseSpeacialSkill = false;
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendPopUpMultiLine(Player pl, int tempID, int avt, String text) {
        Message msg = null;
        try {
            msg = new Message(-218);
            msg.writer().writeShort(tempID);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(avt);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendMessAllPlayerInMap(Zone zone, Message msg) {
        msg.transformData();
        if (zone != null) {
            List<Player> players = zone.getPlayers();
            synchronized (players) {
                for (Player pl : players) {
                    if (pl != null) {
                        pl.sendMessage(msg);
                    }
                }
            }
            msg.cleanup();
        }
    }

    public void sendMessAllPlayerInMap(Player player, Message msg) {
        msg.transformData();
        if (player.zone != null) {
            if (player.zone.map.isMapOffline) {
                if (player.isPet) {
                    ((Pet) player).master.sendMessage(msg);
                } else {
                    if (player instanceof WhisTop) {
                        List<Player> players = player.zone.getPlayers();
                        synchronized (players) {
                            for (Player pl : players) {
                                try {
                                    if (pl != null && pl.id == (long) Util.GetPropertyByName(player, "player_id")) {
                                        pl.sendMessage(msg);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        player.sendMessage(msg);
                    }
                }
            } else {
                List<Player> players = player.zone.getPlayers();
                synchronized (players) {
                    for (Player pl : players) {
                        if (pl != null) {
                            pl.sendMessage(msg);
                        }
                    }
                }

                msg.cleanup();
            }
        }
    }

    public void sendMessAnotherNotMeInMap(Player player, Message msg) {
        if (player.zone != null) {
            List<Player> players = player.zone.getPlayers();
            synchronized (players) {
                for (Player pl : players) {
                    if (pl != null && !pl.equals(player)) {
                        pl.sendMessage(msg);
                    }
                }
            }

            msg.cleanup();
        }
    }

    public void sendMessToPlayer(Player player, Message msg, long id) {
        if (player.zone != null) {
            List<Player> players = player.zone.getPlayers();
            synchronized (players) {
                for (Player pl : players) {
                    if (pl != null && pl.id == id) {
                        pl.sendMessage(msg);
                    }
                }
            }
            msg.cleanup();
        }
    }

    public void Send_Info_NV(Player pl) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 14);// Cập nhật máu
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeLong(pl.nPoint.hp);
            msg.writer().writeByte(0);// Hiệu ứng Ăn Đậu
            msg.writer().writeLong(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendInfoPlayerEatPea(Player pl) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 14);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeLong(pl.nPoint.hp);
            msg.writer().writeByte(1);
            msg.writer().writeLong(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void loginDe(Session session, short second) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(second);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void resetPoint(Player player, int x, int y) {
        Message msg;
        try {
            player.location.x = x;
            player.location.y = y;
            msg = new Message(46);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void clearMap(Player player) {
        Message msg;
        try {
            msg = new Message(-22);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendEffectCombine(Player player, byte type) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(type);

            msg.writer().writeShort(player.combineNew.npcCombine.tempId);
            msg.writer().writeShort(player.combineNew.npcCombine.cx);
            msg.writer().writeShort(player.combineNew.npcCombine.cy);

            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    // int test = 0;
    public void chat(Player player, String text) {
        if (player.getSession() != null && player.isAdmin()) {
            text = text.toLowerCase();
            if (text.equals("tele")) {
                this.sendThongBao(player, "Thực thi lệnh thành công");
                List<Player> playersMap = Client.gI().getPlayers();
                for (Player pl : playersMap) {
                    if (pl != null && !player.equals(pl)) {
                        if (pl.zone != null) {
                            ChangeMapService.gI().changeMap(pl, player.zone, player.location.x, player.location.y);
                        }
                        Service.getInstance().sendThongBao(pl, "|2|Bạn đã được ADMIN gọi đến đây");
                    }
                }
                return;
            }
            if (text.equals("killpl")) {
                this.sendThongBao(player, "Xiên toàn server thành công");
                List<Player> playersMap = Client.gI().getPlayers();
                for (Player pl : playersMap) {
                    if (pl != null && !player.equals(pl)) {
                        pl.isDie();
                        pl.setDie(player);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.getInstance().Send_Info_NV(pl);
                        Service.getInstance().sendThongBao(pl, "|2|ADMIN ĐÃ TÀN SÁT CẢ SERVER");
                    }
                }
                return;
            }
            if (text.equals("logskill")) {
                Service.getInstance().sendThongBao(player, player.playerSkill.skillSelect.coolDown + "");
                return;
            }
            if (text.equals("hsk")) {
                Service.getInstance().releaseCooldownSkill(player);
                if (!player.isDie()) {
                    player.nPoint.setHp(player.nPoint.hpMax);
                    player.nPoint.setMp(player.nPoint.mpMax);
                }
                PlayerService.gI().sendInfoHpMpMoney(player);
                Service.getInstance().sendThongBao(player, "LỬA PHI PHAI !");
                return;
            }
            if (text.startsWith("xf")) {
                for (Skill skill : player.playerSkill.skills) {
                    skill.lastTimeUseThisSkill = skill.lastTimeUseThisSkill - skill.coolDown;
                }
                if (!player.isDie()) {
                    player.nPoint.setHp(player.nPoint.hpMax);
                    player.nPoint.setMp(player.nPoint.mpMax);
                }
                sendTimeSkill(player);
                PlayerService.gI().sendInfoHpMpMoney(player);
                sendThongBao(player, "LỬA PHI PHAI !");
                return;
            }
            if (text.startsWith("jk")) {
                String[] parts = text.split(" ");
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                this.x = x;
                this.y = y;
                System.out.println("X : " + x + " Y : " + y);
                return;
            } else if (text.startsWith("setlv")) {
                if (InventoryService.gI().getCountEmptyBag(player) > 5) {
                    String[] parts = text.split(" ");
                    int gender = Integer.parseInt(parts[1]);
                    int lv = Integer.parseInt(parts[2]);
                    int kh = -1;
                    try {
                        kh = Integer.parseInt(parts[3]);
                    } catch (Exception e) {

                    }
                    for (int i = 0; i < 5; i++) {
                        Item vp = ItemService.gI().createNewItem(ConstItem.doSKHVip[i][gender][lv]);
                        RewardService.gI().initBaseOptionClothes(vp);
                        if(kh != -1){
                            RewardService.gI().initExactlyActivationOption(gender, kh,vp.itemOptions);
                        }
                        InventoryService.gI().addItemBag(player, vp, 1);
                        InventoryService.gI().sendItemBags(player);
                    }
                    Service.getInstance().sendThongBao(player, "Init set LV " + lv + " thành công !");
                } else {
                    sendThongBao(player, "Full rương !");
                }
                return;
            } else if (text.startsWith("jakef")) {
                String[] parts = text.split(" ");
                int id = Integer.parseInt(parts[1]);
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[3]);
                int lp = 0;
                try {
                    lp = Integer.parseInt(parts[4]);
                } catch (Exception e) {
                }
                if (lp > 0) {
                    y = -y;
                }
                Service.getInstance().addEffectChar(player, id, 0, -1, 50, -1, x, y);
                Service.getInstance().sendThongBao(player, "EFF ID : " + id + " X : " + x + "Y : " + y);
                return;
            }
            if (text.startsWith("jake")) {
                String[] parts = text.split(" ");
                if (parts.length < 3) {
                    Service.getInstance().sendThongBao(player, "Vui lòng nhập số lượng!");
                    return;
                }
                int itemId = Integer.parseInt(parts[1]);
                long quantity = Long.parseLong(parts[2]);
                if (quantity > 2000000000) {
                    Service.getInstance().sendThongBao(player, "Không thể lấy số lượng vượt quá 2 tỷ!");
                    return;
                }
                Item item = ItemService.gI().createNewItem((short) itemId);
                item.quantity = (int) quantity;
                ItemShop itemShop = new Shop().getItemShop(itemId);
                if (itemShop != null && !itemShop.options.isEmpty()) {
                    item.itemOptions.addAll(itemShop.options);
                }
                InventoryService.gI().addItemBag(player, item, (int) quantity);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player,
                        "Đã lấy vật phẩm: " + item.template.name + " Số lượng: " + quantity);

            }
            if (text.startsWith("jaki")) {
                short id = Short.parseShort(text.replace("jaki ", ""));
                Item item = ItemService.gI().createNewItem(id);
                RewardService.gI().initBaseOptionClothes(item);
                InventoryService.gI().addItemBag(player, item, 0);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn nhận được " + item.template.name);
                return;
            }

            if (text.equals("a")) {
                BossManager.gI().showListBoss(player);
                return;
            }
            if (text.equals("jk")) {
                System.err.println("TIME : " + (System.currentTimeMillis()));
                Input.gI().createFormSenditem1(player);
                return;
            }
            if (text.equals("client")) {
                Client.gI().show(player);
                return;
            }
            if (text.equals("loadshop")) {
                Manager.reloadShop();
                this.sendThongBao(player, "Load Shop Success");
                return;
            }
            if (text.equals("god")) {
                List<Integer> it = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    Item item = InventoryService.gI().findGodClothesIgnoreIndex(player, it);
                    if (player.inventory.itemsBag.indexOf(item) != -1) {
                        it.add(player.inventory.itemsBag.indexOf(item));
                    }
                }
                for (Integer t : it) {
                    System.err.println("ID : " + t + " NAME :" + player.inventory.itemsBag.get(t).template.name);
                    ;
                }
                System.err.println("SIZE : " + it.size());
                return;
            }
            if (text.equals("clan")) {
                if (player.clan != null) {
                    for (ClanMember cl : player.clan.members) {
                        Player p = Client.gI().getPlayer(cl.id);
                        if (p != null) {
                            addSMTN(p, (byte) 2, 100_000, false);
                        } else {
                            PlayerDAO.addPower(cl.id, 1500);
                        }
                    }
                }
            } else if (text.startsWith("xoa")) {
                String[] txt = text.split(" ");
                int type = Integer.parseInt(txt[1]);
                switch (type) {
                    case 0:
                        for (int i = 0; i < player.inventory.itemsBody.size(); i++) {
                            InventoryService.gI().removeItemBody(player, i);
                        }
                        break;
                    case 1:
                        for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
                            InventoryService.gI().removeItemBag(player, i);
                        }
                        break;
                    case 2:
                        for (int i = 0; i < player.inventory.itemsBox.size(); i++) {
                            InventoryService.gI().removeItemBox(player, i);
                        }
                        break;
                    case 3:
                        for (int i = 0; i < player.inventory.itemsBody.size(); i++) {
                            InventoryService.gI().removeItemBody(player, i);
                        }
                        for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
                            InventoryService.gI().removeItemBag(player, i);
                        }
                        for (int i = 0; i < player.inventory.itemsBox.size(); i++) {
                            InventoryService.gI().removeItemBox(player, i);
                        }
                        break;
                    default:
                        break;
                }
                InventoryService.gI().sendItemBags(player);
                InventoryService.gI().sendItemBody(player);
                InventoryService.gI().sendItemBox(player);
                sendThongBao(player, "Clear inven !");
                return;
            } else if (text.equals("admin")) {
                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_ADMIN, -1,
                        "Quản trị admin " + Manager.DOMAIN
                                + "\n|1|Online: " + Client.gI().getPlayers().size() + "\n"
                                + "|4|Thread: " + Thread.activeCount() + "\n",
                        "Ngọc rồng", "Log check", "Bảo trì", "Tìm kiếm\nngười chơi", "Call\nBoss", "Đệ tử !", "Đóng");
                return;
            } else if (text.equals("toado")) {
                NpcService.gI().createMenuConMeo(player, ConstNpc.COORDINATES, 11525, "Tọa độ: " + player.location.x
                        + " - " + player.location.y + "\nMap: " + player.zone.map.mapId + " - " + player.zone.zoneId);
                return;
            } else if (text.equals("huybtri")) {
                Service.getInstance().sendThongBao(player, "Hủy bảo trì !");
                Maintenance.gI().stopMaintenance();
                return;
            } else if (text.startsWith("upp")) {
                try {
                    long power = Long.parseLong(text.replaceAll("upp", ""));
                    addSMTN(player.pet, (byte) 2, power, false);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (text.startsWith("up")) {
                try {
                    long power = Long.parseLong(text.replaceAll("up", ""));
                    addSMTN(player, (byte) 2, power, false);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (text.startsWith("ngu")) {
                try {
                    int power = Integer.parseInt(text.replaceAll("ngu", ""));
                    Boss b = BossManager.gI().getBossById(-power);
                    if (b != null) {
                        b.leaveMap();
                    }
                    BossManager.gI().removeBoss(b);
                    Service.getInstance().sendThongBao(player, "Kill thành công : " + b.name);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (text.startsWith("khon")) {
                try {
                    int power = Integer.parseInt(text.replaceAll("khon", ""));
                    BossFactory.createBoss(-power);
                    Service.getInstance().sendThongBao(player, "Creat Boss Id : " + (-power));
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (text.startsWith("m ")) {
                try {
                    int mapId = Integer.parseInt(text.replace("m ", ""));
                    Zone zone = MapService.gI().getZoneJoinByMapIdAndZoneId(player, mapId, 0);
                    if (zone != null) {
                        player.location.x = 500;
                        player.location.y = zone.map.yPhysicInTop(500, 100);
                        MapService.gI().goToMap(player, zone);
                        Service.getInstance().clearMap(player);
                        zone.mapInfo(player);
                        player.zone.loadAnotherToMe(player);
                        player.zone.load_Me_To_Another(player);
                    }
                    return;
                } catch (Exception e) {
                    Service.getInstance().sendThongBao(player, "Lỗi !");
                }
            } else if (text.startsWith("sao ")) {
                try {
                    int mapId = Integer.parseInt(text.replace("sao ", ""));
                    for (Item it : player.inventory.itemsBody) {
                        if (it.isNotNullItem()) {
                            it.itemOptions.add(new ItemOption(107, mapId));
                        }
                    }
                    InventoryService.gI().sendItemBody(player);
                    Service.getInstance().sendThongBao(player, "SET " + mapId + " SAO");
                } catch (Exception e) {
                }
                return;
            } else if (text.startsWith("level ")) {
                try {
                    int mapId = Integer.parseInt(text.replace("level ", ""));
                    for (Item it : player.inventory.itemsBody) {
                        if (it.isNotNullItem()) {
                            it.itemOptions.add(new ItemOption(72, mapId));
                        }
                    }
                    InventoryService.gI().sendItemBody(player);
                    Service.getInstance().sendThongBao(player, "SET LEVEL :" + mapId);
                } catch (Exception e) {
                }
                return;
            }
        }
        if (text.equals("banvang")) {
            setSellingGold(player);
        }
        if (text.startsWith("ten con la ")) {
            PetService.gI().changeNamePet(player, text.replaceAll("ten con la ", ""));
        }
        if (player.pet != null) {
            if (text.equals("di theo") || text.equals("follow")) {
                player.pet.changeStatus(Pet.FOLLOW);
            } else if (text.equals("bao ve") || text.equals("protect")) {
                player.pet.changeStatus(Pet.PROTECT);
            } else if (text.equals("tan cong") || text.equals("attack")) {
                player.pet.changeStatus(Pet.ATTACK);
            } else if (text.equals("ve nha") || text.equals("go home")) {
                player.pet.changeStatus(Pet.GOHOME);
            } else if (text.equals("bien hinh")) {
                player.pet.transform();
            }
        }

        if (text.length() > 100) {
            text = text.substring(0, 100);
        }
        chatMap(player, text);
    }

    public void chatMap(Player player, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeUTF(text);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void regisAccount(Session session, Message _msg) {
        try {
            PreparedStatement ps = null;
            int key = -1;
            int sl = 0;
            String day = _msg.reader().readUTF();
            String month = _msg.reader().readUTF();
            String year = _msg.reader().readUTF();
            String address = _msg.reader().readUTF();
            String cmnd = _msg.reader().readUTF();
            String dayCmnd = _msg.reader().readUTF();
            String noiCapCmnd = _msg.reader().readUTF();
            String user = _msg.reader().readUTF();
            String pass = _msg.reader().readUTF();
            if (!(user.length() >= 4 && user.length() <= 18)) {
                sendThongBaoOK(session, "Tài khoản phải có độ dài 4-18 ký tự");
                return;
            }
            if (!(pass.length() >= 6 && pass.length() <= 18)) {
                sendThongBaoOK(session, "Mật khẩu phải có độ dài 6-18 ký tự");
                return;
            }
            try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
                ps = con.prepareStatement("SELECT COUNT(1) AS sl FROM account WHERE ip_address = ?");
                ps.setString(1, session.ipAddress);
                ResultSet rset = ps.executeQuery();
                rset.next();
                sl = rset.getInt("sl");
                if (sl > 5) {
                    sendThongBaoOK(session, "Số lượng account tối đa có thể đăng ký cho 1 Ip là 5");
                } else {
                    ps = con.prepareStatement("select * from account where username = ?");
                    ps.setString(1, user);
                    if (ps.executeQuery().next()) {
                        sendThongBaoOK(session, "Tạo thất bại do tài khoản đã tồn tại");
                    } else {
                        ps = con.prepareStatement("insert into account(username,password) values (?,?)",
                                Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, user);
                        ps.setString(2, pass);
                        ps.executeUpdate();
                        ResultSet rs = ps.getGeneratedKeys();
                        rs.next();
                        key = rs.getInt(1);
                        sendThongBaoOK(session, "Tạo tài khoản thành công!");
                    }
                }
            } catch (Exception e) {
                Log.error(AccountDAO.class, e);
            } finally {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            sendThongBaoOK(session, "Tạo tài khoản thất bại");
        }
    }

    public void chatJustForMe(Player me, Player plChat, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeUTF(text);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void point(Player player) {
        player.nPoint.calPoint();
        Send_Info_NV(player);
        if (!player.isPet && !player.isBoss) {
            Message msg;
            try {
                msg = new Message(-42);
                msg.writer().writeLong(player.nPoint.hpg);
                msg.writer().writeLong(player.nPoint.mpg);
                msg.writer().writeLong(player.nPoint.dameg);
                msg.writer().writeLong(player.nPoint.hpMax);// hp full
                msg.writer().writeLong(player.nPoint.mpMax);// mp full
                msg.writer().writeLong(player.nPoint.hp);// hp
                msg.writer().writeLong(player.nPoint.mp);// mp
                msg.writer().writeByte(player.nPoint.speed);// speed
                msg.writer().writeByte(20);
                msg.writer().writeByte(20);
                msg.writer().writeByte(1);
                msg.writer().writeLong(player.nPoint.dame);// dam base
                msg.writer().writeInt(player.nPoint.def);// def full
                msg.writer().writeByte(player.nPoint.crit);// crit full
                msg.writer().writeLong(player.nPoint.tiemNang);
                msg.writer().writeShort(100);
                msg.writer().writeShort(player.nPoint.defg);
                msg.writer().writeByte(player.nPoint.critg);
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Log.error(Service.class, e);
            }
        }
    }

    public void player(Player pl) {
        if (pl == null) {
            return;
        }
        Message msg;
        try {
            msg = messageSubCommand((byte) 0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.playerTask.taskMain.id);
            msg.writer().writeByte(pl.gender);
            msg.writer().writeShort(pl.head);
            msg.writer().writeUTF(pl.name /* + "[" + pl.id + "]" */);
            msg.writer().writeByte(0); // cPK
            msg.writer().writeByte(pl.typePk);
            msg.writer().writeLong(pl.nPoint.power);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(pl.gender);
            // --------skill---------

            ArrayList<Skill> skills = (ArrayList<Skill>) pl.playerSkill.skills;

            msg.writer().writeByte(pl.playerSkill.getSizeSkill());

            for (Skill skill : skills) {
                if (skill.skillId != -1) {
                    msg.writer().writeShort(skill.skillId);
                }
            }

            // ---vang---luong--luongKhoa
            long gold = pl.inventory.getGoldDisplay();
            if (pl.isVersionAbove(214)) {
                msg.writer().writeLong(gold);
            } else {
                msg.writer().writeInt((int) gold);
            }
            msg.writer().writeInt(pl.inventory.ruby);
            msg.writer().writeInt(pl.inventory.gem);

            // --------itemBody---------
            ArrayList<Item> itemsBody = (ArrayList<Item>) pl.inventory.itemsBody;
            msg.writer().writeByte(itemsBody.size());
            for (Item item : itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            // --------itemBag---------
            ArrayList<Item> itemsBag = (ArrayList<Item>) pl.inventory.itemsBag;
            msg.writer().writeByte(itemsBag.size());
            for (int i = 0; i < itemsBag.size(); i++) {
                Item item = itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            // --------itemBox---------
            ArrayList<Item> itemsBox = (ArrayList<Item>) pl.inventory.itemsBox;
            msg.writer().writeByte(itemsBox.size());
            for (int i = 0; i < itemsBox.size(); i++) {
                Item item = itemsBox.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            // -----------------
            DataGame.sendHeadAvatar(msg);
            // -----------------
            msg.writer().writeShort(514); // char info id - con chim thông báo
            msg.writer().writeShort(515); // char info id
            msg.writer().writeShort(537); // char info id
            msg.writer().writeByte(pl.fusion.typeFusion != ConstPlayer.NON_FUSION ? 1 : 0); // nhập thể
            msg.writer().writeInt(333); // deltatime
            msg.writer().writeByte(pl.isNewMember ? 1 : 0); // is new member

            msg.writer().writeShort(pl.getAura()); // idauraeff
            msg.writer().writeByte(pl.getEffFront());
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public Message messageNotLogin(byte command) throws IOException {
        Message ms = new Message(-29);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageNotMap(byte command) throws IOException {
        Message ms = new Message(-28);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageSubCommand(byte command) throws IOException {
        Message ms = new Message(-30);
        ms.writer().writeByte(command);
        return ms;
    }

    public void addSMTN(Player player, byte type, long param, boolean isOri) {
        if (player.isPet) {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }
            player.nPoint.powerUp(param);
            player.nPoint.tiemNangUp(param);
            Player master = ((Pet) player).master;

            param = master.nPoint.calSubTNSM(param);
            if (master.nPoint.power < master.nPoint.getPowerLimit()) {
                master.nPoint.powerUp(param);
            }
            master.nPoint.tiemNangUp(param);
            addSMTN(master, type, param, true);
        } else {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }
            switch (type) {
                case 1:
                    player.nPoint.tiemNangUp(param);
                    break;
                case 2:
                    player.nPoint.powerUp(param);
                    player.nPoint.tiemNangUp(param);
                    break;
                default:
                    player.nPoint.powerUp(param);
                    break;
            }
            PlayerService.gI().sendTNSM(player, type, param);
            if (isOri) {
                if (player.clan != null) {
                    player.clan.addSMTNClan(player, param);
                }
            }
        }
    }

    public String get_HanhTinh(int hanhtinh) {
        switch (hanhtinh) {
            case 0:
                return "Trái Đất";
            case 1:
                return "Xayda";
            case 2:
                return "Namec";
            default:
                return "";
        }
    }

    public String getCurrStrLevel(Player pl) {
        long sucmanh = pl.nPoint.power;
        if (sucmanh < 3000) {
            return "Tân thủ";
        } else if (sucmanh < 15000) {
            return "Tập sự sơ cấp";
        } else if (sucmanh < 40000) {
            return "Tập sự trung cấp";
        } else if (sucmanh < 90000) {
            return "Tập sự cao cấp";
        } else if (sucmanh < 170000) {
            return "Tân binh";
        } else if (sucmanh < 340000) {
            return "Chiến binh";
        } else if (sucmanh < 700000) {
            return "Chiến binh cao cấp";
        } else if (sucmanh < 1500000) {
            return "Vệ binh";
        } else if (sucmanh < 15000000) {
            return "Vệ binh hoàng gia";
        } else if (sucmanh < 150000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 1500000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 5000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 10000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 4";
        } else if (sucmanh < 40000000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 50010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 60010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 70010000000L) {
            return "Giới Vương Thần cấp 11";
        } else if (sucmanh < 80010000000L) {
            return "Giới Vương Thần cấp 2";
        } else if (sucmanh < 100010000000L) {
            return "Giới Vương Thần cấp 3";
        } else if (sucmanh < 11100010000000L) {
            return "Thần Huỷ Diệt cấp 1";
        }
        return "Thần Huỷ Diệt cấp 2";
    }

    public void hsChar(Player pl, long hp, long mp) {
        Message msg;
        try {
            pl.setJustRevivaled();
            pl.nPoint.setHp(hp);
            pl.nPoint.setMp(mp);
            if (!pl.isPet) {
                msg = new Message(-16);
                pl.sendMessage(msg);
                msg.cleanup();
                PlayerService.gI().sendInfoHpMpMoney(pl);
            }
            msg = messageSubCommand((byte) 15);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeLong(hp);
            msg.writer().writeLong(mp);
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
            Send_Info_NV(pl);
            PlayerService.gI().sendInfoHpMp(pl);
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void charDie(Player pl) {
        Message msg;
        try {
            if (!pl.isPet) {
                msg = new Message(-17);
                msg.writer().writeByte((int) pl.id);
                msg.writer().writeShort(pl.location.x);
                msg.writer().writeShort(pl.location.y);
                pl.sendMessage(msg);
                msg.cleanup();
            } else {
                ((Pet) pl).lastTimeDie = System.currentTimeMillis();
            }

            msg = new Message(-8);
            msg.writer().writeShort((int) pl.id);
            msg.writer().writeByte(0); // cpk
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
            // Send_Info_NV(pl);
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void attackMob(Player pl, int mobId) {
        if (pl != null && pl.zone != null) {
            for (Mob mob : pl.zone.mobs) {
                if (mob.id == mobId) {
                    SkillService.gI().useSkill(pl, null, mob);
                    break;
                }
            }
        }
    }

    public void sendHideEffNpc(Player player) {
        Message msg;
        try {
            msg = new Message(-122);
            msg.writer().writeShort(50);
            msg.writer().writeByte(0);
            msg.writer().writeShort(-1);
            msg.writer().writeByte(0);
            msg.writer().writeInt(0);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEffectHideNPC(Player pl, byte npcID, byte status) {
        Message msg;
        try {
            msg = new Message(-73);
            msg.writer().writeByte(npcID);
            msg.writer().writeByte(status); // 0 = hide
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEffectHideNPCPlayer(Player pl, byte npcID, byte status) {
        Message msg;
        try {
            msg = new Message(-73);
            msg.writer().writeByte(npcID);
            msg.writer().writeByte(status); // 0 = hide
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Send_Caitrang(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                short head = player.getHead();
                short body = player.getBody();
                short leg = player.getLeg();

                msg.writer().writeShort(head);// set head
                msg.writer().writeShort(body);// setbody
                msg.writer().writeShort(leg);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Log.error(Service.class, e);
            }
        }
    }

    public void setNotMonkey(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendFlagBag(Player pl) {
        Message msg;
        try {
            msg = new Message(-64);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.getFlagBag());
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendThongBaoOK(Player pl, String text) {
        if (pl.isPet) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendThongBaoOK(Session session, String text) {
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendThongBaoAllPlayer(String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendBigMessage(Player player, int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendThongBaoFromAdmin(Player player, String text) {
        sendBigMessage(player, 1139, text);
    }

    public void sendBigMessAllPlayer(int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendThongBao(Player pl, String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendMoney(Player pl) {
        Message msg;
        try {
            msg = new Message(6);
            long gold = pl.inventory.getGoldDisplay();
            if (pl.isVersionAbove(214)) {
                msg.writer().writeLong(gold);
            } else {
                msg.writer().writeInt((int) gold);
            }
            msg.writer().writeInt(pl.inventory.gem);
            msg.writer().writeInt(pl.inventory.ruby);
            ServerLog.logSubRuby(pl.name, pl.inventory.ruby);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendToAntherMePickItem(Player player, int itemMapId) {
        Message msg;
        try {
            msg = new Message(-19);
            msg.writer().writeShort(itemMapId);
            msg.writer().writeInt((int) player.id);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public boolean isItemMoney(int type) {
        return type == 9 || type == 10 || type == 34;
    }

    public void useSkillNotFocus(Player player, Message m) throws IOException {
        byte status = m.reader().readByte();
        if (status == 20) {
            byte SkillID = m.reader().readByte();
            short xPlayer = m.reader().readShort();
            short yPlayer = m.reader().readShort();
            byte dir = m.reader().readByte();
            short x = m.reader().readShort();
            short y = m.reader().readShort();
            SkillService.gI().selectSkill(player, SkillID);
            // if (player.skillSpecial.checkDoBenSachBeforUseSKill(player) == false) {
            // Service.getInstance().sendThongBao(player, "Phục hồi sách hoặc tháo sách ra
            // để dùng skill");
            // return;
            // }
            if (player.nPoint.mp < (player.nPoint.mpMax * 80 / 100)
                    || !SkillService.gI().canUseSkillWithCooldown(player)) {
                return;
            }
            player.skillSpecial.setSkillSpecial(dir, xPlayer, yPlayer);
            SkillService.gI().PrePareSkillNotFocus(player, dir);
        } else {
            SkillService.gI().useSkill(player, null, null);
        }
    }

    public void chatGlobal(Player pl, String text) {
        if (pl.inventory.getGem() >= 5) {
            if (pl.isAdmin() || Util.canDoWithTime(pl.lastTimeChatGlobal, 180000)) {
                if (pl.isAdmin() || pl.nPoint.power > 2000000000) {
                    pl.inventory.subGem(5);
                    sendMoney(pl);
                    pl.lastTimeChatGlobal = System.currentTimeMillis();
                    Message msg;
                    try {
                        msg = new Message(92);
                        msg.writer().writeUTF(pl.name);
                        msg.writer().writeUTF("|5|" + text);
                        msg.writer().writeInt((int) pl.id);
                        msg.writer().writeShort(pl.getHead());
                        msg.writer().writeShort(pl.getBody());
                        msg.writer().writeShort(pl.getFlagBag()); // bag
                        msg.writer().writeShort(pl.getLeg());
                        msg.writer().writeByte(0);
                        sendMessAllPlayer(msg);
                        msg.cleanup();
                    } catch (Exception e) {
                    }
                } else {
                    sendThongBao(pl, "Sức mạnh phải ít nhất 2tỷ mới có thể chat thế giới");
                }
            } else {
                sendThongBao(pl, "Không thể chat thế giới lúc này, vui lòng đợi "
                        + TimeUtil.getTimeLeft(pl.lastTimeChatGlobal, 120));
            }
        } else {
            sendThongBao(pl, "Không đủ ngọc chat thế giới");
        }
    }

    private int tiLeXanhDo = 3;

    public int xanhToDo(int n) {
        return n * tiLeXanhDo;
    }

    public int doToXanh(int n) {
        return (int) n / tiLeXanhDo;
    }

    public static final int[] flagTempId = { 363, 364, 365, 366, 367, 368, 369, 370, 371, 519, 520, 747 };
    public static final int[] flagIconId = { 2761, 2330, 2323, 2327, 2326, 2324, 2329, 2328, 2331, 4386, 4385, 2325 };

    public void openFlagUI(Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(0);
            msg.writer().writeByte(flagTempId.length);
            for (int i = 0; i < flagTempId.length; i++) {
                msg.writer().writeShort(flagTempId[i]);
                msg.writer().writeByte(1);
                switch (flagTempId[i]) {
                    case 363:
                        msg.writer().writeByte(73);
                        msg.writer().writeShort(0);
                        break;
                    case 371:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(10);
                        break;
                    default:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(5);
                        break;
                }
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changeFlag(Player pl, int index) {

        Message msg;
        try {
            pl.cFlag = (byte) index;
            msg = new Message(-103);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(index);
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(index);
            msg.writer().writeShort(flagIconId[index]);
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            if (pl.pet != null) {
                pl.pet.cFlag = (byte) index;
                msg = new Message(-103);
                msg.writer().writeByte(1);
                msg.writer().writeInt((int) pl.pet.id);
                msg.writer().writeByte(index);
                Service.getInstance().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();

                msg = new Message(-103);
                msg.writer().writeByte(2);
                msg.writer().writeByte(index);
                msg.writer().writeShort(flagIconId[index]);
                Service.getInstance().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();
            }
            pl.lastTimeChangeFlag = System.currentTimeMillis();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendFlagPlayerToMe(Player me, Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(pl.cFlag);
            msg.writer().writeShort(flagIconId[pl.cFlag]);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseFlag(Player pl, int index) {
        if (Util.canDoWithTime(pl.lastTimeChangeFlag, 60000)) {
            if (!MapService.gI().isMapBlackBallWar(pl.zone.map.mapId)
                    && !MapService.gI().isMapMabuWar(pl.zone.map.mapId) && !pl.isHoldBlackBall) {
                if (index < 0 || index >= 12) {
                    Service.getInstance().sendThongBao(pl, "Không thể thao tác");
                    return;
                }
                changeFlag(pl, index);
            } else {
                sendThongBao(pl, "Không thể đổi cờ ở khu vực này");
            }
        } else {
            sendThongBao(pl, "Không thể đổi cờ lúc này! Vui lòng đợi " + TimeUtil.getTimeLeft(pl.lastTimeChangeFlag, 60)
                    + " nữa!");
        }
    }

    public void attackPlayer(Player pl, int idPlAnPem) {
        SkillService.gI().useSkill(pl, pl.zone.getPlayerInMap(idPlAnPem), null);
    }

    public void openZoneUI(Player pl) {
        if (pl.zone == null || pl.zone.map.isMapOffline) {
            sendThongBaoOK(pl, "Không thể đổi khu vực trong map này");
            return;
        }
        int mapid = pl.zone.map.mapId;
        if (!pl.isAdmin() && (MapService.gI().isMapDoanhTrai(mapid) || MapService.gI().isMapBanDoKhoBau(mapid)
                || MapService.gI().isMapVS(mapid) || mapid == 126 || pl.zone instanceof ZDungeon)) {
            sendThongBaoOK(pl, "Không thể đổi khu vực trong map này");
            return;
        }
        Message msg;
        try {
            msg = new Message(29);
            msg.writer().writeByte(pl.zone.map.zones.size());
            for (Zone zone : pl.zone.map.zones) {
                msg.writer().writeByte(zone.zoneId);
                int numPlayers = zone.getNumOfPlayers();
                msg.writer().writeByte((numPlayers < 5 ? 0 : (numPlayers < 8 ? 1 : 2)));
                msg.writer().writeByte(numPlayers);
                msg.writer().writeByte(zone.maxPlayer);
                msg.writer().writeByte(0);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void releaseCooldownSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                skill.coolDown = 0;
                msg.writer().writeShort(skill.skillId);
                int leftTime = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (leftTime < 0) {
                    leftTime = 0;
                }
                msg.writer().writeInt(leftTime);
            }
            pl.sendMessage(msg);
            pl.nPoint.setMp(pl.nPoint.mpMax);
            PlayerService.gI().sendInfoHpMpMoney(pl);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void sendTimeSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                msg.writer().writeShort(skill.skillId);
                int timeLeft = Math.max(0,
                        skill.coolDown - (int) (System.currentTimeMillis() - skill.lastTimeUseThisSkill));
                if (timeLeft < 0) {
                    timeLeft = 0;
                }
                msg.writer().writeInt(timeLeft);

            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropItemMap(Zone zone, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt((int) item.playerId);
            if (item.playerId == -2) {
                msg.writer().writeShort(item.range);
            }
            sendMessAllPlayerInMap(zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void dropItemMapForMe(Player player, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt((int) item.playerId);
            if (item.playerId == -2) {
                msg.writer().writeShort(item.range);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void showInfoPet(Player pl) {
        TransactionService.gI().cancelTrade(pl);
        if (pl != null && pl.pet != null) {
            Message msg;
            try {
                msg = new Message(-107);
                msg.writer().writeByte(2);
                msg.writer().writeShort(pl.pet.getAvatar());
                msg.writer().writeByte(pl.pet.inventory.itemsBody.size());

                for (Item item : pl.pet.inventory.itemsBody) {
                    if (!item.isNotNullItem()) {
                        msg.writer().writeShort(-1);
                    } else {
                        msg.writer().writeShort(item.template.id);
                        msg.writer().writeInt(item.quantity);
                        msg.writer().writeUTF(item.getInfo());
                        msg.writer().writeUTF(item.getContent());

                        List<ItemOption> itemOptions = item.getDisplayOptions();
                        int countOption = itemOptions.size();
                        msg.writer().writeByte(countOption);
                        for (ItemOption iop : itemOptions) {
                            msg.writer().writeByte(iop.optionTemplate.id);
                            msg.writer().writeShort(iop.param);
                        }
                    }
                }
                msg.writer().writeLong(pl.pet.nPoint.hp); // hp
                msg.writer().writeLong(pl.pet.nPoint.hpMax); // hpfull
                msg.writer().writeLong(pl.pet.nPoint.mp); // mp
                msg.writer().writeLong(pl.pet.nPoint.mpMax); // mpfull
                msg.writer().writeLong(pl.pet.nPoint.dame); // damefull
                msg.writer().writeUTF(pl.pet.name); // name
                msg.writer().writeUTF(getCurrStrLevel(pl.pet)); // curr level
                msg.writer().writeLong(pl.pet.nPoint.power); // power
                msg.writer().writeLong(pl.pet.nPoint.tiemNang); // tiềm năng
                msg.writer().writeByte(pl.pet.getStatus()); // status
                msg.writer().writeShort(pl.pet.nPoint.stamina); // stamina
                msg.writer().writeShort(pl.pet.nPoint.maxStamina); // stamina full
                msg.writer().writeByte(pl.pet.nPoint.crit); // crit
                msg.writer().writeShort(pl.pet.nPoint.def); // def
                msg.writer().writeByte(4); // counnt pet skill
                for (int i = 0; i < pl.pet.playerSkill.skills.size(); i++) {
                    if (pl.pet.playerSkill.skills.get(i).skillId != -1) {
                        msg.writer().writeShort(pl.pet.playerSkill.skills.get(i).skillId);
                    } else {
                        switch (i) {
                            case 1:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 150tr để mở");
                                break;
                            case 2:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 1tỷ5 để mở");
                                break;
                            case 3:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 20tỷ để mở");
                                break;
                            default:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh tối thượng");
                                break;
                        }
                    }
                }

                pl.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendPointPet(Player pl) {
        if (pl != null && pl.pet != null) {
            Message msg;
            try {
                msg = new Message(-109);
                msg.writer().writeLong(pl.pet.nPoint.hpg); // hp
                msg.writer().writeLong(pl.pet.nPoint.mpg); // mp
                msg.writer().writeLong(pl.pet.nPoint.dameg); // damefull
                msg.writer().writeShort(pl.pet.nPoint.def); // def
                msg.writer().writeByte(pl.pet.nPoint.critg); // crit
                pl.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendSpeedPlayer(Player pl, int speed) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 8);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.nPoint.speed);
            pl.sendMessage(msg);
            // Service.getInstance().sendMessAllPlayerInMap(pl.map, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void setPos(Player player, int x, int y) {
        player.location.x = x;
        player.location.y = y;
        Message msg;
        try {
            msg = new Message(123);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeByte(1);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void getPlayerMenu(Player player, int playerId) {
        Message msg;
        try {
            msg = new Message(-79);
            Player pl = player.zone.getPlayerInMap(playerId);
            if (pl != null) {
                msg.writer().writeInt(playerId);
                msg.writer().writeLong(pl.nPoint.power);
                msg.writer().writeUTF(Service.getInstance().getCurrStrLevel(pl));
                player.sendMessage(msg);
            }
            msg.cleanup();
            if (player.isAdmin()) {
                SubMenuService.gI().showMenuForAdmin(player);
            }
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void subMenuPlayer(Player player) {
        Message msg;
        try {
            msg = messageSubCommand((byte) 63);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("String 1");
            msg.writer().writeUTF("String 2");
            msg.writer().writeShort(550);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void hideWaitDialog(Player pl) {
        Message msg;
        try {
            msg = new Message(-99);
            msg.writer().writeByte(-1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chatPrivate(Player plChat, Player plReceive, String text) {
        Message msg;
        try {
            msg = new Message(92);
            msg.writer().writeUTF(plChat.name);
            msg.writer().writeUTF("|5|" + text);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeShort(plChat.getHead());
            Part part = PartManager.getInstance().find(plChat.head);
            msg.writer().writeShort(part.getIcon(0));
            msg.writer().writeShort(plChat.getBody());
            msg.writer().writeShort(plChat.getFlagBag()); // bag
            msg.writer().writeShort(plChat.getLeg());
            msg.writer().writeByte(1);
            plChat.sendMessage(msg);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changePassword(Player player, String oldPass, String newPass, String rePass) {
        if (player.getSession().pp.equals(oldPass)) {
            if (newPass.length() >= 6) {
                if (newPass.equals(rePass)) {
                    player.getSession().pp = newPass;
                    AccountDAO.updateAccount(player.getSession());
                    Service.getInstance().sendThongBao(player, "Đổi mật khẩu thành công!");
                } else {
                    Service.getInstance().sendThongBao(player, "Mật khẩu nhập lại không đúng!");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Mật khẩu ít nhất 6 ký tự!");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Mật khẩu cũ không đúng!");
        }
    }

    public void switchToCreateChar(Session session) {
        Message msg;
        try {
            msg = new Message(2);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendCaption(Session session, byte gender) {
        Message msg;
        try {
            List<Caption> captions = CaptionManager.getInstance().getCaptions();
            msg = new Message(-41);
            msg.writer().writeByte(captions.size());
            for (Caption caption : captions) {
                msg.writer().writeUTF(caption.getCaption(gender));
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendHavePet(Player player) {
        Message msg;
        try {
            msg = new Message(-107);
            msg.writer().writeByte(player.pet == null ? 0 : 1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendWaitToLogin(Session session, int secondsWait) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(secondsWait);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendMessage(Session session, int cmd, String path) {
        Message msg;
        try {
            msg = new Message(cmd);
            msg.writer().write(FileIO.readFile(path));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendTopRank(Player pl) {
        Message msg;
        try {
            msg = new Message(Cmd.THELUC);
            msg.writer().writeInt(1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createItemMap(Player player, int tempId) {
        ItemMap itemMap = new ItemMap(player.zone, tempId, 1, player.location.x, player.location.y, player.id);
        dropItemMap(player.zone, itemMap);
    }

    public void sendNangDong(Player player) {
        Message msg;
        try {
            msg = new Message(-97);
            msg.writer().writeInt(100);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendPowerInfo(Player pl, String info, short point) {
        Message m = null;
        try {
            m = new Message(-115);
            m.writer().writeUTF(info);
            m.writer().writeShort(point);
            m.writer().writeShort(20);
            m.writer().writeShort(10);
            m.writer().flush();
            if (pl != null && pl.getSession() != null) {
                pl.sendMessage(m);
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void setMabuHold(Player pl, byte type) {
        Message m = null;
        try {
            m = new Message(52);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void sendPercentMabuEgg(Player player, byte percent) {
        try {
            Message msg = new Message(-117);
            msg.writer().writeByte(percent);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendPlayerInfo(Player player) {
        try {
            Message msg = messageSubCommand((byte) 7);
            msg.writer().writeInt((int) player.id);
            if (player.clan != null) {
                msg.writer().writeInt(player.clan.id);
            } else {
                msg.writer().writeInt(-1);
            }
            int level = CaptionManager.getInstance().getLevel(player);
            level = player.isInvisible ? 0 : level;
            msg.writer().writeByte(level);
            msg.writer().writeBoolean(player.isInvisible);
            msg.writer().writeByte(player.typePk);
            msg.writer().writeByte(player.gender);
            msg.writer().writeByte(player.gender);
            msg.writer().writeShort(player.getHead());
            msg.writer().writeUTF(player.name);
            msg.writer().writeLong(player.nPoint.hp);
            msg.writer().writeLong(player.nPoint.hpMax);
            msg.writer().writeShort(player.getBody());
            msg.writer().writeShort(player.getLeg());
            msg.writer().writeByte(player.getFlagBag());
            msg.writer().writeByte(-1);
            msg.writer().writeShort(player.location.x);
            msg.writer().writeShort(player.location.y);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(0);

            // msg.writer().writeShort(0);
            // msg.writer().writeByte(0);
            // msg.writer().writeShort(0);
            sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCurrLevel(Player pl) {

    }

    public int getWidthHeightImgPetFollow(int id) {
        if (id == 15067) {
            return 65;
        }
        return 75;
    }

    public void showTopOmega(Player player) {
        List<Player> list = TopManager.getInstance().getListTopOmega();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Omega :");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF("Dame : " + Util.fm.format(pl.dameOmega));
                msg.writer().writeUTF("Dame : " + Util.fm.format(pl.dameOmega));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopThoiVang(Player player) {
        List<Player> list = TopManager.getInstance().getListTopThoiVang();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Thỏi vàng :");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF("Thỏi : " + pl.pointThoiVang);
                msg.writer().writeUTF("Thỏi : " + pl.pointThoiVang);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shopTopBanhKem(Player player) {
        List<Player> list = TopManager.getInstance().getListTOpBanhLKem();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Điểm bánh :");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF("Điểm : " + pl.pointBanhKem);
                msg.writer().writeUTF("Điểm : " + pl.pointBanhKem);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopThan(Player player) {
        List<Player> list = TopManager.getInstance().getListTopDothan();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Thần :");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF("Món : " + pl.count);
                msg.writer().writeUTF("Món : " + pl.count);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopVP(Player player, int id) {
        List<Player> list = TopManager.getInstance().getListTopRnadoom();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top " + id);
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF("Món : " + pl.countRd);
                msg.writer().writeUTF("Món : " + pl.countRd);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopMia(Player player) {
        List<Player> list = TopManager.getInstance().getListTopMia();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Top Cây mía :");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF("Điểm : " + pl.pointSk);
                msg.writer().writeUTF("Điểm : " + pl.pointSk);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopTrungThu(Player player) {
        List<Player> list = TopManager.getInstance().getListTopTrungThu();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Top phá cỗ :");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(Util.numberToMoney(pl.pointTrungThu) + " điểm");
                msg.writer().writeUTF(Util.numberToMoney(pl.pointTrungThu) + " điểm");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopCa(Player player) {
        List<Player> list = TopManager.getInstance().getListTopCa();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top vòng quay :");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF("Điểm : " + Util.numberToMoney(pl.pointVongQuayThuongDe));
                msg.writer().writeUTF("Điểm : " + Util.numberToMoney(pl.pointVongQuayThuongDe));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopPower(Player player) {
        List<Player> list = TopManager.getInstance().getList();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Sức Mạnh");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(Client.gI().getPlayer(pl.id) != null ? "Online" : "");
                msg.writer().writeUTF("Sức mạnh: " + Util.numberToMoney(pl.nPoint.power));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopTask(Player player) {
        List<Player> list = TopToTask.getInstance().load();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Nhiệm Vụ");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(Client.gI().getPlayer(pl.id) != null ? "Online" : "");
                // msg.writer().writeUTF("Sức mạnh: " + Util.numberToMoney(pl.nPoint.power));
                msg.writer().writeUTF(" ặ ặ");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSellingGold(Player player) {
        player.isSellingGold = !player.isSellingGold;
        Service.getInstance().sendThongBao(player, "Bán vàng [" + (player.isSellingGold ? "ON" : "OFF") + "]");

    }

    public static void ShowTopNap(Player player) {
        List<TopPlayer> list = GetTopNap();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Bảng xếp hạng Nạp");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                TopPlayer top = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(Math.toIntExact(top.id));
                msg.writer().writeShort(player.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(player.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(player.getBody());
                msg.writer().writeShort(player.getLeg());
                msg.writer().writeUTF(top.name);

                msg.writer().writeUTF(Util.formatCurrency(top.amount) + " VNĐ");

                msg.writer().writeUTF("");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ShowTopPower(Player player) {
        List<TopPlayer> list = GetTopPower();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Bảng xếp hạng Sức mạnh");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                TopPlayer top = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(Math.toIntExact(top.id));
                msg.writer().writeShort(player.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(player.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(player.getBody());
                msg.writer().writeShort(player.getLeg());
                msg.writer().writeUTF(top.name);

                msg.writer().writeUTF(Util.formatCurrency(top.amount));

                msg.writer().writeUTF("");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFusionEffect(Player player, int type) {
        Message msg = null;
        try {
            msg = new Message(125);
            msg.writer().writeByte(type);
            msg.writer().writeInt((int) player.id);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
