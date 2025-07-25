package nro.services;

import nro.consts.ConstNpc;
import nro.models.intrinsic.Intrinsic;
import nro.models.player.Player;
import nro.server.Manager;
import nro.server.io.Message;
import nro.utils.Util;

import java.util.List;
import nro.models.item.Item;

/**
 *
 *
 *
 */
public class IntrinsicService {

    private static IntrinsicService i;
    private static final int[] COST_OPEN = {10, 20, 40, 80, 160, 320, 640, 1280};

    public static IntrinsicService gI() {
        if (i == null) {
            i = new IntrinsicService();
        }
        return i;
    }

    public List<Intrinsic> getIntrinsics(byte playerGender) {
        switch (playerGender) {
            case 0:
                return Manager.INTRINSIC_TD;
            case 1:
                return Manager.INTRINSIC_NM;
            default:
                return Manager.INTRINSIC_XD;
        }
    }

    public Intrinsic getIntrinsicById(int id) {
        for (Intrinsic intrinsic : Manager.INTRINSICS) {
            if (intrinsic.id == id) {
                return new Intrinsic(intrinsic);
            }
        }
        return null;
    }

    public void sattd(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.menutd, -1,
                "Chọn đi ku", "Set\nKamejoko", "Set\nKaioken", "Set\nThên xin hăng", "Từ chối");
    }

    public void satnm(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.menunm, -1,
                "Chọn đi ku", "Set\nLiên hoàn", "Set\nPicolo", "Set\nPikkoro Daimao", "Từ chối");
    }

    public void setxd(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.menuxd, -1,
                "Chọn đi ku", "Set\nKakarot", "Set\nCađíc", "Set\nNappa", "Từ chối");
    }

    public void sendInfoIntrinsic(Player player) {
        Message msg;
        try {
            msg = new Message(112);
            msg.writer().writeByte(0);
            msg.writer().writeShort(player.playerIntrinsic.intrinsic.icon);
            msg.writer().writeUTF(player.playerIntrinsic.intrinsic.getName());
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void showAllIntrinsic(Player player) {
        List<Intrinsic> listIntrinsic = getIntrinsics(player.gender);
        Message msg;
        try {
            msg = new Message(112);
            msg.writer().writeByte(1);
            msg.writer().writeByte(1); //count tab
            msg.writer().writeUTF("Nội tại");
            msg.writer().writeByte(listIntrinsic.size() - 1);
            for (int i = 1; i < listIntrinsic.size(); i++) {
                msg.writer().writeShort(listIntrinsic.get(i).icon);
                msg.writer().writeUTF(listIntrinsic.get(i).getDescription());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void showMenu(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.INTRINSIC, -1,
                "Nội tại là một kỹ năng bị động hỗ trợ đặc biệt\nBạn có muốn mở hoặc thay đổi nội tại không?",
                "Xem\ntất cả\nNội Tại", "Mở\nNội Tại", "Mở VIP", "Từ chối");
    }

    public void showConfirmOpen(Player player) {
        int index = player.playerIntrinsic.countOpen;
        if (index >= 0 && index < COST_OPEN.length) {
//            NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_OPEN_INTRINSIC, -1, "Bạn muốn đổi Nội Tại khác\nvới giá là "
//                    + COST_OPEN[index] + " Tr vàng ?", "Mở\nNội Tại", "Từ chối");
            NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP, -1,
                    "Bạn có muốn mở Nội Tại\nvới giá là 1 thỏi vàng không ?", "Mở\nNội Tại", "Từ chối");
        } else {
            Service.getInstance().sendThongBao(player, "Lỗi");
        }
    }

    public void showConfirmOpenVip(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP, -1,
                "Bạn có muốn mở Nội Tại\nvới giá là 100 Ngọc không ?", "Mở\n Nội Tại\n VIP", "Từ chối");
    }

    public void changeIntrinsic(Player player) {
        List<Intrinsic> listIntrinsic = getIntrinsics(player.gender);
        player.playerIntrinsic.intrinsic = new Intrinsic(listIntrinsic.get(Util.nextInt(1, listIntrinsic.size() - 1)));
        player.playerIntrinsic.intrinsic.param1 = (short) Util.nextInt(player.playerIntrinsic.intrinsic.paramFrom1, player.playerIntrinsic.intrinsic.paramTo1);
        player.playerIntrinsic.intrinsic.param2 = (short) Util.nextInt(player.playerIntrinsic.intrinsic.paramFrom2, player.playerIntrinsic.intrinsic.paramTo2);
        Service.getInstance().sendThongBao(player, "Bạn nhận được Nội tại:\n" + player.playerIntrinsic.intrinsic.getName().substring(0, player.playerIntrinsic.intrinsic.getName().indexOf(" [")));
        sendInfoIntrinsic(player);
    }
    
    public void changeIntrinsicMAX(Player player) {
        List<Intrinsic> listIntrinsic = getIntrinsics(player.gender);
        player.playerIntrinsic.intrinsic = new Intrinsic(listIntrinsic.get(Util.nextInt(1, listIntrinsic.size() - 1)));
        player.playerIntrinsic.intrinsic.param1 = (short) player.playerIntrinsic.intrinsic.paramTo1;
        player.playerIntrinsic.intrinsic.param2 = (short) Util.nextInt(player.playerIntrinsic.intrinsic.paramFrom2, player.playerIntrinsic.intrinsic.paramTo2);
        Service.getInstance().sendThongBao(player, "Bạn nhận được Nội tại:\n" + player.playerIntrinsic.intrinsic.getName().substring(0, player.playerIntrinsic.intrinsic.getName().indexOf(" [")));
        sendInfoIntrinsic(player);
    }

    public void open(Player player) {
        if (player.nPoint.power >= 10000000000L) {
            Item tv = InventoryService.gI().findItemBag(player, 457);
            if (tv != null && tv.quantity >= 1) {
                InventoryService.gI().subQuantityItemsBag(player, tv, 1);
                InventoryService.gI().sendItemBags(player);
                changeIntrinsic(player);
            } else {
                Service.getInstance().sendThongBao(player, "Không có thỏi vàng");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Yêu cầu sức mạnh tối thiểu 10 tỷ");
        }
    }

    public void openVip(Player player) {
        if (player.nPoint.power >= 10000000000L) {
            if(player.inventory.gem >= 100){
                changeIntrinsic(player);
                Service.getInstance().sendMoney(player);
                player.playerIntrinsic.countOpen = 0;
            } else {
                Service.getInstance().sendThongBao(player, "Còn thiếu " + (100 - player.inventory.gem) + " ngọc nữa ");
            }

        } else {
            Service.getInstance().sendThongBao(player, "Yêu cầu sức mạnh tối thiểu 10 tỷ");
        }
    }

}
