package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.consts.ConstTask;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

public class NpcPhuocTv extends Npc {
    
    public NpcPhuocTv(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }
    
    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                    "Chào bạn ? Tôi đang nắm giữ sức mạnh Toàn Vũ trụ\n"
                    + "Tôi sẽ ban cho cậu những phần quà tương xứng khi đạt đủ mốc sức mạnh !",
                    "50\nTỉ", "100\nTỉ", "120\n Tỉ", "10\n Tỉ", "Nhận \n phiếu Max nội tại", "Đóng");
        }
    }
    
    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (select) {
                case 0:
                case 1:
                case 2:
                case 3:
                    ReceivedMocSucManh(player, select);
                    break;
                case 4:
                    NhanPhieuNoiTai(player);
                    break;
                default:
                    break;
            }
        }
    }
    
    void NhanPhieuNoiTai(Player player) {
        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_30_2) {
            if (!player.receiveNoiTai) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    Item phiu = ItemService.gI().createNewItem((short)1507);
                    InventoryService.gI().addItemBag(player, phiu, 1);
                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được phiếu đổi Max nội tại !");
                    InventoryService.gI().sendItemBags(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Bạn đã nhận phiếu rồi !");
            }
            
        } else {
            Service.getInstance().sendThongBao(player, "Hãy luyện tập đến nhiệm vụ cuối cùng để có cơ hội nhận nhé !");
        }
    }
    long[] moc = {50_000_000_000L, 100_000_000_000L, 120_000_000_000L, 10_000_000_000L};
    short[] idvp = {1472, 1473, 1474, 1353};
    
    void ReceivedMocSucManh(Player player, int select) {
        if (player.getSession().actived) {
            if (player.nPoint.power >= moc[select]) {
                if (!player.receiveMocSM[select]) {
                    if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                        Item vp = ItemService.gI().createNewItem(idvp[select]);
                        if (select != 3) {
                            vp.itemOptions.add(new ItemOption(50, 5 + (5 * select)));
                            vp.itemOptions.add(new ItemOption(77, 5 + (5 * select)));
                            vp.itemOptions.add(new ItemOption(103, 5 + (5 * select)));
                            vp.itemOptions.add(new ItemOption(197, 1));
                        } else {
                            vp.itemOptions.add(new ItemOption(30, 1));
                        }
                        InventoryService.gI().addItemBag(player, vp, 1);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Bạn vừa nhận quà mốc " + Util.numberToMoney(moc[select]));
                        player.receiveMocSM[select] = true;
                    } else {
                        Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Bạn đã nhận quà mốc " + Util.numberToMoney(moc[select]) + " rồi mà !");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Hãy luyện tập thêm " + Util.numberToMoney(moc[select] - player.nPoint.power) + " để nhận mốc " + Util.numberToMoney(moc[select]));
            }
        } else {
            Service.getInstance().sendThongBao(player, "Bạn chưa kích hoạt thành viên cho Server !");
        }
    }
}
