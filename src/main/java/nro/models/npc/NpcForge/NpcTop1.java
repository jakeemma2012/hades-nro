package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.consts.ConstTask;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ShopService;

public class NpcTop1 extends Npc {

    public NpcTop1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                    "Chào bạn ? Bạn đang làm gì ở đây vậy ?", "Nhận quà", "Top\n Thỏi vàng", "Đóng");
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (select) {
                case 0:
                    ShopService.gI().openBoxItemReward(player);
                    break;
                case 1:
                    Service.getInstance().showTopThoiVang(player);
                    break;
            }
        }
    }
}
