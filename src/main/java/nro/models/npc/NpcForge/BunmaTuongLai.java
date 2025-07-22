package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.CombineServiceNew;
import nro.services.func.ShopService;

public class BunmaTuongLai extends Npc {

    public BunmaTuongLai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 102:
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?",
                                "Cửa hàng", "Đóng");
                    }
                    break;
                case 191:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, cậu muốn tôi giúp gì?",
                            "Ấp trứng\n linh thú", "Chúc phúc\nlinh thú", "Nở trứng\nlinh thú", "Cửa hàng", "Đóng");
                    break;
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (this.mapId) {
                case 102:
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0, player.gender);
                        }
                    }
                    break;
                case 191:
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.AP_TRUNG_LINH_THU,
                                        this);
                                break;
                            case 1:
                                this.createOtherMenu(player, 99,
                                        "Tôi sẽ giúp cậu chúc phúc trứng linh thú giúp giảm thời gian nở!\n"
                                                + "1) 1 hồn linh thú + 50tr vàng (giảm 1 giờ ấp)\n"
                                                + "2) 10 hồn linh thú + 750tr vàng (giảm 12 giờ ấp)\n"
                                                + "3) 30 hồn linh thú + 4tỉ vàng (giảm 2 ngày ấp)",
                                        "Lựa chọn 1", "Lựa chọn 2", "Lựa chọn 3", "Từ chối");
                                break;
                            case 2:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NO_TRUNG_LINH_THU,
                                        this);
                                break;
                            case 3:
                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_BUNMA_LINH_THU, 1, -1);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 99) {
                        switch (select) {
                            case 0:
                            case 1:
                            case 2:
                                this.chucPhucbyIndex(player, select);
                                break;
                            default:
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combineNew.typeCombine) {
                            case CombineServiceNew.NO_TRUNG_LINH_THU:
                            case CombineServiceNew.AP_TRUNG_LINH_THU:
                                if (select == 0) {
                                    CombineServiceNew.gI().startCombine(player);
                                }
                                break;

                            default:
                                break;
                        }
                    }
                    break;
            }
        }
    }

    short[] quantity = { 1, 10, 30 };
    long[] price = { 50_000_000, 750_000_000, 4_000_000_000L };

    void chucPhucbyIndex(Player player, int select) {
        if (player.egglinhthu != null) {
            if (InventoryService.gI().getQuantity(player, 2029) >= quantity[select]) {
                if (player.inventory.getGold() >= price[select]) {
                    player.inventory.gold -= price[select];
                    InventoryService.gI().subQuantityItemsBag(player, 2029, quantity[select]);
                    switch (select) {
                        case 0:
                            player.egglinhthu.subTimeDone(0, 1, 0, 0);
                            break;
                        case 1:
                            player.egglinhthu.subTimeDone(0, 12, 0, 0);
                            break;
                        case 2:
                            player.egglinhthu.subTimeDone(2, 0, 0, 0);
                            break;
                    }
                    Service.getInstance().sendThongBao(player, "Chúc phúc thành công!");
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Không đủ vàng!");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không đủ hồn linh thú!");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Không có trứng linh thú để thực hiện chúc phúc!");
        }
    }
}
