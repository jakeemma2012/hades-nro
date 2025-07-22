
package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.services.func.ShopService;

public class GokuSSJ extends Npc {

    public GokuSSJ(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 133) {
                Item biKiep = InventoryService.gI().findItem(player.inventory.itemsBag, 590);
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + (biKiep != null ? biKiep.quantity : 0)
                        + " bí kiếp.\n"
                        + "Hãy kiếm đủ 999 bí kiếp tôi sẽ dạy bạn cách thông thạo Siêu thần của hành tinh ngươi !",
                        "Học\n Siêu thần", "Mua\n kỹ năng ", "Đóng");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 133) {
                switch (select) {
                    case 0:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item biKiep = InventoryService.gI().findItemBag(player, 590);
                            if (biKiep != null && biKiep.quantity >= 999) {
                                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                    Item sieuthan1 = ItemService.gI()
                                            .createNewItem((short) getIdSieuThan(player.gender));
                                    InventoryService.gI().subQuantityItemsBag(player, biKiep, 999);
                                    InventoryService.gI().addItemBag(player, sieuthan1, 1);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn vừa nhận được Sách kỹ năng Siêu thần 1");
                                }
                            } else {
                                this.npcChat(player, "|7|Hãy luyện tập để trở nên mạnh mẽ hơn !");
                            }
                        } else {
                            this.npcChat(player, "|8|Hãy để trống 1 ô hành trang !");
                        }
                        break;
                    case 1:
                        ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_GOKU_SSJ, 0, -1);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    int getIdSieuThan(byte gender) {
        switch (gender) {
            case 0:
                return 1319;
            case 1:
                return 1324;
            case 2:
                return 1329;
            default:
                return 0;
        }
    }
}
