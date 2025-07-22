package nro.models.npc.NpcForge;

import nro.consts.ConstMap;
import nro.consts.ConstNpc;
import nro.models.map.dungeon.zones.ZSnakeRoad;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.TaskService;

public class ThanMeoKarin extends Npc {

    public ThanMeoKarin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (mapId == ConstMap.THAP_KARIN) {
                if (player.zone instanceof ZSnakeRoad) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Hãy cầm lấy hai hạt đậu cuối cùng ở đây\nCố giữ mình nhé "
                            + player.name,
                            "Cảm ơn\nsư phụ");
                } else if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào ? Con đang làm gì ở đây thế !", "OK");
                }
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (mapId == ConstMap.THAP_KARIN) {
                if (player.iDMark.isBaseMenu()) {
                    if (player.zone instanceof ZSnakeRoad) {
                        switch (select) {
                            case 0:
                                player.setInteractWithKarin(true);
                                Service.getInstance().sendThongBao(player,
                                        "Hãy mau bay xuống chân tháp Karin");
                                break;
                        }
                    } else {
                        switch (select) {
                            case 0:
//                                this.npcChat(player, "Log");
                                break;
                            case 1:
                                break;
                        }
                    }
                }
            }
        }
    }
}
