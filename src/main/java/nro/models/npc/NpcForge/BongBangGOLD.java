package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.models.item.Item;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.ItemTimeService;
import nro.services.Service;
import nro.services.func.ShopService;

public class BongBangGOLD extends Npc {

    public BongBangGOLD(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (mapId == 109) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "|8|Ồ, nơi lạnh lẽo cũng có ngươi lui tới sao "
                        + "\n|6|Tại nơi khắc nghiệt như thế này chắc ngươi cũng khó khăn lắm nhỉ"
                        + "\nNgoài ra còn 1 số sản phẩm ,ngươi muốn xem qua chứ !???",
                        "Sức mạnh\nX5", "Shop", "Đóng");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (mapId == 109) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            this.createOtherMenu(player, 1, "|8|Ta đã ở đây từ rất lâu rồi \nvà ta đã khám phá ra 1 số nguồn sức mạnh tại nơi đây\n"
                                    + "|6|Ngươi muốn thử 1 chút chứ !?\n"
                                    + "Ngươi có thể trả cho ta 1000 Hồng ngọc\nhoặc 1000 Thỏi vàng và ta sẽ trao ngươi sức mạnh\n"
                                    + "X5 sức mạnh tiềm năng Đệ tử của ngươi trong vòng 10 phút\n",
                                    "1000\nHồng Ngọc", "1000\n Thỏi vàng", "Đóng");
                            break;
                        case 1:
                            ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_BONG_BANG_GOLD, 0, -1);
                            break;
                        default:
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == 1) {
                    switch (select) {
                        case 0:
                            buyBua(player, 0);
                            break;
                        case 1:
                            buyBua(player,1);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    void buyBua(Player player, int type) {
        switch (type) {
            case 0:
                if (!player.itemTime.isBuyBuaCOLD) {
                    if (player.inventory.ruby >= 1000) {
                        player.inventory.ruby -= 1000;
                        Service.getInstance().sendMoney(player);
                        player.itemTime.isBuyBuaCOLD = true;
                        player.itemTime.lastTimeBuyBuaCOLD = System.currentTimeMillis();
                        ItemTimeService.gI().sendAllItemTime(player);
                        this.npcChat(player, "|2|Cậu sỡ hữu sức mạnh này trong vòng 10 phút thôi đấy nhé !");
                    } else {
                        this.npcChat(player, "|7|Không đủ 1000 Hồng ngọc để mua loại bùa này !");
                    }
                } else {
                    this.npcChat(player, "|8|Cậu đang sử dụng loại bùa này rồi !");
                }
                break;
            case 1:
                if (!player.itemTime.isBuyBuaCOLD) {
                    Item tv = InventoryService.gI().findItemBag(player, 457);
                    if (tv != null && tv.quantity >= 1000) {
                        InventoryService.gI().subQuantityItemsBag(player, tv, 1000);
                        InventoryService.gI().sendItemBags(player);
                        player.itemTime.isBuyBuaCOLD = true;
                        player.itemTime.lastTimeBuyBuaCOLD = System.currentTimeMillis();
                           ItemTimeService.gI().sendAllItemTime(player);
                        this.npcChat(player, "|2|Cậu sỡ hữu sức mạnh này trong vòng 10 phút thôi đấy nhé !");
                    } else {
                        this.npcChat(player, "|7|Không đủ 1000 Thỏi vàng để mua loại bùa này !");
                    }
                } else {
                    this.npcChat(player, "|8|Cậu đang sử dụng loại bùa này rồi !");
                }
                break;
            default:
                break;
        }
    }
}
