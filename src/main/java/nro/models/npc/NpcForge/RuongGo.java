
package nro.models.npc.NpcForge;

import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;

/**
 *
 * 
 */
public class RuongGo extends Npc {

    public RuongGo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            InventoryService.gI().sendItemBox(player);
            InventoryService.gI().openBox(player);
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {

        }
    }

}
