
package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.models.item.Item;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.Service;
import nro.services.func.ShopService;

public class Santa extends Npc {

    public Santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 5:
                case 20:
                case 13:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                            "Cửa hàng", "Cửa hàng\nđặc biệt", "Xóa bỏ\n Vật phẩm \n ngày", "Đóng");
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 5 || this.mapId == 20 || this.mapId == 13) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0: // shop
                            this.openShopWithGender(player, ConstNpc.SHOP_SANTA_0, 0);
                            break;
                        case 1:
                            ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_SANTA_SPEC, 4, -1);
                            break;
                        case 2:
                            this.createOtherMenu(player, 1, "|7|Con chắc chắn muốn xóa hết vật phẩm ngày chứ ?",
                                    "Đồng ý", "Từ chối");
                            break;
                        default:
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == 1) {
                    if (select == 0) {
                        Xoahsd(player);
                    }
                }
            }
        }
    }

    void Xoahsd(Player player) {
        if (player != null) {
            if (player.inventory.itemsBag != null) {
                try {
                    for (Item it : player.inventory.itemsBag) {
                        if (it.haveOption(93)) {
                            InventoryService.gI().subQuantityItemsBag(player, it, it.quantity);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Đã thực hiện xóa hết vật phẩm ngày trong hành trang !");
            }
        }
    }

}
