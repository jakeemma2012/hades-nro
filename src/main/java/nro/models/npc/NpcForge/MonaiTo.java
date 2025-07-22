package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.func.ChangeMapService;
import nro.services.func.ShopService;
import nro.services.Service;

public class MonaiTo extends Npc {

    long lastTimeClick = System.currentTimeMillis();

    public MonaiTo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 7:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào bạn, tôi sẽ đưa bạn tới hành tinh Cereal", "Đồng ý", "Từ chối");
                    break;
                case 177:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Cậu muốn quay về sao?!", "Đồng ý", "Từ chối");
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                if (mapId == 7) {
                    if (select == 0) {
                        ChangeMapService.gI().changeMap(player, 177, -1, 163, 528);
                    }
                } else if (mapId == 177) {
                    if (select == 0) {
                        ChangeMapService.gI().changeMap(player, 7, -1, 550, 432);
                    }
                }
            }
        }
    }

}
