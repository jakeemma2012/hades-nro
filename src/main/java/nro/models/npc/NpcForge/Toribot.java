package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.func.ShopService;

public class Toribot extends Npc {

    public Toribot(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào cậu, cậu muốn tớ giúp gì?",
                "Cửa hàng\n vật phẩm\n chế tạo\n đồ Thiên sứ", "Từ chối");
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (player.iDMark.getIndexMenu()) {
                case ConstNpc.BASE_MENU:
                    switch (select) {
                        case 0:
                            ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_TORIBOT, 0, -1);
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
