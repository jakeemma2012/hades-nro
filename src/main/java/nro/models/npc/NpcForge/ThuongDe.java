package nro.models.npc.NpcForge;

import java.util.List;

import nro.consts.ConstMap;
import nro.consts.ConstNpc;
import nro.models.item.CaiTrang;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.dungeon.SnakeRoad;
import nro.models.map.dungeon.zones.ZSnakeRoad;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.server.Manager;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.NpcService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.services.func.Input;
import nro.services.func.LuckyRoundService;
import nro.services.func.ShopService;

public class ThuongDe extends Npc {

    public ThuongDe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 45:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Con muốn thực hiện quay vòng quay sao ?\n"
                                    + "Con đang có tổng thực hiện " + player.pointVongQuayThuongDe + " lần quay",
                            "Quay số\nmay mắn", "Top\n vòng quay", "Mốc \n vòng quay","Đến Kaio");
                    break;
                case ConstMap.CON_DUONG_RAN_DOC:
                    if (player.zone instanceof ZSnakeRoad) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hãy lắm lấy tay ta mau",
                                "Về thần điện");
                        break;
                    }

                default:
                    break;
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 45) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            // if (player.getSession().actived) {
                            this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                    "Con muốn làm gì nào?", "Quay bằng\nthỏi vàng", "Quay nhanh\n100 lần",
                                    "Rương phụ\n("
                                            + (player.inventory.itemsBoxCrackBall.size()
                                                    - InventoryService.gI().getCountEmptyListItem(
                                                            player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                    "Xóa hết\ntrong rương", "Đóng");
                            // } else {
                            // this.npcChat(player, "|8|Chức năng chỉ dành cho mở thành viên !");
                            // }
                            break;

                        case 1:
                            Service.getInstance().showTopCa(player);
                            break;
                        case 2:
                            this.OpenMocRewards(player);
                            break;
                        case 3:
                            ChangeMapService.gI().changeMapBySpaceShip(player,48,-1,372);
                            break;
                        // case 1:
                        // ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                        // break;
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                    switch (select) {
                        case 0:
                            LuckyRoundService.gI().openCrackBallUI(player,
                                    LuckyRoundService.USING_GOLD);
                            break;
                        case 1:
                            Input.gI().createFormRollFast(player);
                            break;
                        case 2:
                            ShopService.gI().openBoxItemLuckyRound(player);
                            break;
                        case 3:
                            NpcService.gI().createMenuConMeo(player,
                                    ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                    "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                            + "sẽ không thể khôi phục!",
                                    "Đồng ý", "Hủy bỏ");
                            break;
                    }
                }
            } else if (player.zone instanceof ZSnakeRoad) {
                if (mapId == ConstMap.CON_DUONG_RAN_DOC) {
                    ZSnakeRoad zroad = (ZSnakeRoad) player.zone;
                    if (zroad.isKilledAll()) {
                        SnakeRoad road = (SnakeRoad) zroad.getDungeon();
                        ZSnakeRoad egr = (ZSnakeRoad) road.find(ConstMap.THAN_DIEN);
                        egr.enter(player, 360, 408);
                        Service.getInstance().sendThongBao(player, "Hãy xuống gặp thần mèo Karin");
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Hãy tiêu diệt hết quái vật ở đây!");
                    }
                }
            }
        }
    }

    public static void OpenMocRewards(Player player) {
        player.iDMark.setShopId(ConstNpc.MOC_REWARDS);
        Message msg;
        try {
            msg = new Message(-44);
            msg.writer().writeByte(4);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Phần\nthưởng");
            msg.writer().writeByte(Manager.gI().itemsReward.size());
            int i = 0;
            for (Item item : Manager.gI().itemsReward) {
                msg.writer().writeShort(item.template.id);
                msg.writer().writeUTF("\n|7|Quà mốc " + Manager.gI().mocPointItemsRewardsList.get(i) + " điểm");
                i++;
                List<ItemOption> itemOptions = item.getDisplayOptions();
                msg.writer().writeByte(itemOptions.size() + 1);
                for (ItemOption io : itemOptions) {
                    msg.writer().writeByte(io.optionTemplate.id);
                    msg.writer().writeShort(io.param);
                }
                // số lượng
                msg.writer().writeByte(31);
                msg.writer().writeShort(item.quantity);
                //
                msg.writer().writeByte(1);
                CaiTrang ct = Manager.getCaiTrangByItemId(item.template.id);
                msg.writer().writeByte(ct != null ? 1 : 0);
                if (ct != null) {
                    msg.writer().writeShort(ct.getID()[0]);
                    msg.writer().writeShort(ct.getID()[1]);
                    msg.writer().writeShort(ct.getID()[2]);
                    msg.writer().writeShort(ct.getID()[3]);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
