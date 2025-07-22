package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.models.npc.Npc;
import nro.models.phuban.MabuBoss.PhubanMabu;
import nro.models.player.Player;
import nro.services.Service;

public class Hit extends Npc {

    public Hit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        switch (this.mapId) {
            case 27:
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào ! Omega " + (PhubanMabu.gI().isAlive ?  " đang xuất hiện"  : " chưa xuất hiện") + "\n"
                        + "|7|Hãy cẩn thận với sức mạnh của hắn nhé !" , 
                        "Top Dame","Đóng");
                break;
            default:
                break;
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (this.mapId) {
                case 27:
                    if (select == 0) {
                        Service.getInstance().showTopOmega(player);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
