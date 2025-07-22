package nro.models.npc.NpcForge;

import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.consts.ConstTask;
import nro.lib.RandomCollection;
import nro.models.item.Item;
import nro.models.npc.ImageMenu;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.RewardService;
import nro.services.TaskService;
import nro.services.func.ShopService;
import nro.services.Service;
import nro.utils.Util;

public class LyTieuNuong extends Npc {

    long lastTimeClick = System.currentTimeMillis();

    public LyTieuNuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                    "Cậu cần trang bị gì cho Đệ tử cứ đến chỗ tôi nhé", "Cửa\nhàng");
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                if (select == 0) {
                    if (player.pet != null) {
                        ShopService.gI().openShopNormal(player, 7 + player.pet.gender, ConstNpc.SHOP_APPULE_0, 0, -1);
                    } else {
                        Service.getInstance().sendThongBao(player, "Bạn chưa có Đệ tử");
                    }
                }
            }
        }
    }

}
