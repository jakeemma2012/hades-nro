
package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.func.ShopService;

/**
 *
 * 
 */
public class Uron extends Npc {

    public Uron(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player pl) {
        if (canOpenNpc(pl)) {
            switch (mapId) {
                case 24:
                case 25:
                case 26:
                    ShopService.gI().openShopSpecial(pl, this, ConstNpc.SHOP_URON_0, 0, pl.gender);
                    break;
                case 0:
                case 7:
                case 14:
                    ShopService.gI().openShopSpecial(pl, this, ConstNpc.SHOP_URON_1, 1, pl.gender);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {

        }
    }

}
