package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.lib.RandomCollection;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.npc.ImageMenu;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.utils.Util;

public class UronTru extends Npc {

    public UronTru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                    "|8|Này , cậu gì đó có muốn thử vận may vào hôm nay không nào ?"
                            + "\n|6|Tôi đã thu thập 1 số mẫu Labubu HOT HIT gần đây\n"
                            + "|2|Vào mỗi ngày sau khi bảo trì\nmỗi người sẽ có 1 lượt quay miễn phí!"
                            + "\n|7|Hãy nhớ thực hiện mỗi ngày nhé !\n"
                            + "Hôm nay cậu " + (player.hasUron < 1 ? "\n|7|Chưa" : "\n|2|Đã")
                            + " \n|7|thực hiện quay !",
                    "Quay ngay", "Đóng");
        }
    }

    ImageMenu tuchoi = new ImageMenu("Từ chối");

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0:
                        if (player.hasUron < 1) {
                            ImageMenu m1 = new ImageMenu(31851);
                            this.createOtherMenu(player, 1, "|2|Hãy lựa chọn 1 hộp Labubu dưới đây nhé !\n"
                                    + "|8|Cơ hội mỗi ngày chỉ có 1 !!!\n"
                                    + "|7|KẺ KHÔNG TRÂN TRỌNG LƯỢT QUAY KHÔNG XỨNG ĐÁNG ĐƯỢC QUAY ",
                                    "1", "2", "3", "4", "5", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Hôm nay cậu đã quay rồi mà ? THAM QUÁ ĐẤY !!!", "Đóng");
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 1) {
                switch (select) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        doVongQuay(player, select);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    void doVongQuay(Player player, int select) {
        if (player.hasUron < 1) {
            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                player.hasUron += 1;
                ImageMenu menus[] = new ImageMenu[5];
                Item items[] = new Item[5];

                RandomCollection<Integer> THUONG = new RandomCollection<>();

                THUONG.add(5, 1506);
                THUONG.add(10, 189);
                THUONG.add(15, 18);
                THUONG.add(10, 17);
                THUONG.add(40, Util.nextInt(381, 384));
                THUONG.add(10, 1304);
                THUONG.add(5, 457);
                THUONG.add(5, 1506);

                for (int i = 0; i < 5; i++) {
                    int rd = THUONG.next();
                    Item it = ItemService.gI().createNewItem((short) rd);
                    switch (it.getId()) {
                        case 1483: /// labubu
                            it.itemOptions.add(new ItemOption(101, 50));
                            break;
                        case 189: //// vàng
                            it.quantity = Util.nextInt(500000000, 1000000000);
                            break;
                        case 1476: // uron
                            it.itemOptions.add(new ItemOption(50, 20));
                            it.itemOptions.add(new ItemOption(77, 20));
                            it.itemOptions.add(new ItemOption(103, 20));
                            it.itemOptions.add(new ItemOption(101, 10));
                            if (Util.isTrue(95, 100)) {
                                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                            }
                            break;
                        case 1506:
                            it.itemOptions.add(new ItemOption(30, 0));
                            break;
                        default:
                            break;
                    }
                    it.itemOptions.add(new ItemOption(30, 0));
                    items[i] = it;
                    THUONG.remove(rd);
                    ImageMenu m = new ImageMenu(it.template.iconID);
                    menus[i] = m;
                }

                InventoryService.gI().addItemBag(player, items[select], 9999);
                InventoryService.gI().sendItemBags(player);

                menus[select] = new ImageMenu("Đã chọn", menus[select].iconID, 15, 5);

                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "|2|Quéo quèo\n"
                        + "Bạn đã chọn được : "
                        + (items[select].getId() == 189 ? (" " + Util.numberToMoney(items[select].quantity)) : "")
                        + items[select].template.name,
                        "1", "2", "3", "4", "5", "Từ chối");
            } else {
                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang đã đầy!", "Đóng");
            }
        } else {
            this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "|7|Hôm nay cậu đã quay rồi mà ? THAM QUÁ ĐẤY !!!",
                    "Đóng");
        }
    }
}
