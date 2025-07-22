package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.lib.RandomCollection;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.npc.Npc;
import nro.models.player.Inventory;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.NpcService;
import nro.services.PetService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.services.func.ShopService;
import nro.utils.Util;

public class XeNuocMia extends Npc {

    public XeNuocMia(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            createOtherMenu(player, ConstNpc.BASE_MENU, "Sự kiện hè đang được diễn ra tưng bừng tại " + Manager.DOMAIN
                    + "\nTa sẽ mở bán 1 số vật phẩm và map đặc biệt duy nhất chỉ trong mùa hè\n"
                    + "Ngươi có thể tới đó và kiếm đủ loại sinh vật quý để đổi được những vật phẩm từ ta\n"
                    + "Thượng lộ nằm ngang !!! HAHAHAHA !!",
                    "Đỉnh núi\n Sự kiện", "Cửa hàng", "Đổi rương\nHải sản bạc", "Đổi rương\n Hải sản vàng",  "Đổi\nTrứng Vip","Top\n Sk","Đóng");
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0:
                       if(player.getSession().actived){
                           ChangeMapService.gI().changeMapBySpaceShip(player, 212, -1, -1); 
                       } else {
                           Service.getInstance().sendThongBao(player, "Chỉ dành cho mở thành viên !");
                       }
                        break;
                    case 1:
                        ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_NUOC_MIA, 0, -1);
                        break;
                    case 2:
                        createOtherMenu(player, 1, "Ngươi muốn đổi theo cách nào ?\n"
                                + "+ Thường : cần x220 vỏ ốc , vỏ sò , con của và sao biển \n"
                                + "+ Cao cấp : cần x20 vỏ ốc , vò sò , con của , sao biển và x2 Trái dừa\n"
                                + "Mỗi loại đều có tỉ lệ ra những vật phẩm độc nhất \n"
                                + "Chúc ngươi may mắn !", "Thường", "Cao cấp", "Đóng");
                        break;
                    case 3:
                        createOtherMenu(player, 2, "Ngươi muốn đổi theo cách nào?\n"
                                + "+ Thường : cần 300 vỏ ốc, vỏ sò , con cua và sao biển \n"
                                + "+ Cao cấp : cần x30 vỏ ốc , vỏ sò , con cua ,sao biển và 3 Trái dừa\n"
                                + "Mỗi loại đều có tỉ lệ ra những vật phẩm độc nhất \n"
                                + "CHúc ngươi may mắn !",
                                "Thường", "Cao cấp", "Đóng");
                        break;
                    case 4:
                        doiCayMia(player);
                        break;
                    case 5:
                        Service.getInstance().showTopMia(player);
                        break;
                    default:
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 1) {
                switch (select) {
                    case 0:
                        doiruongBac(player, 0);
                        break;
                    case 1:
                        doiruongBac(player, 1);
                        break;
                    default:
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 2) {
                switch (select) {
                    case 0:
                        doiruongVang(player, 0);
                        break;
                    case 1:
                        doiruongVang(player, 1);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    void doiruongVang(Player player, int type) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            Item oc = InventoryService.gI().findItemBag(player, 695);
            Item so = InventoryService.gI().findItemBag(player, 696);
            Item cua = InventoryService.gI().findItemBag(player, 697);
            Item saobien = InventoryService.gI().findItemBag(player, 698);
            Item traidua = InventoryService.gI().findItemBag(player, 694);
            if (oc != null && so != null && cua != null && saobien != null) {
                switch (type) {
                    case 0:
                        type = 300;
                        if (oc.quantity >= type && so.quantity >= type && cua.quantity >= type && saobien.quantity >= type) {
                            InventoryService.gI().subQuantityItemsBag(player, oc, type);
                            InventoryService.gI().subQuantityItemsBag(player, so, type);
                            InventoryService.gI().subQuantityItemsBag(player, cua, type);
                            InventoryService.gI().subQuantityItemsBag(player, saobien, type);
                            doiquaVang(player);
                        } else {
                            Service.getInstance().sendThongBao(player, "Không đủ vật phẩm sự kiện để đổi !");
                        }
                        break;
                    case 1:
                        type = 30;
                        if (oc.quantity >= type && so.quantity >= type && cua.quantity >= type && saobien.quantity >= type && traidua != null && traidua.quantity >= 3) {
                            InventoryService.gI().subQuantityItemsBag(player, oc, type);
                            InventoryService.gI().subQuantityItemsBag(player, so, type);
                            InventoryService.gI().subQuantityItemsBag(player, cua, type);
                            InventoryService.gI().subQuantityItemsBag(player, saobien, type);
                            InventoryService.gI().subQuantityItemsBag(player, traidua, 3);
                            doiquaVang(player);
                        } else {
                            Service.getInstance().sendThongBao(player, "Không đủ vật phẩm sự kiện để đổi !");
                        }
                        break;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Chưa đủ vật phẩm sự kiện để đổi !");
            }

        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    void doiruongBac(Player player, int type) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            Item oc = InventoryService.gI().findItemBag(player, 695);
            Item so = InventoryService.gI().findItemBag(player, 696);
            Item cua = InventoryService.gI().findItemBag(player, 697);
            Item saobien = InventoryService.gI().findItemBag(player, 698);
            Item traidua = InventoryService.gI().findItemBag(player, 694);
            if (oc != null && so != null && cua != null && saobien != null) {
                switch (type) {
                    case 0:
                        type = 220;
                        if (oc.quantity >= type && so.quantity >= type && cua.quantity >= type && saobien.quantity >= type) {
                            InventoryService.gI().subQuantityItemsBag(player, oc, type);
                            InventoryService.gI().subQuantityItemsBag(player, so, type);
                            InventoryService.gI().subQuantityItemsBag(player, cua, type);
                            InventoryService.gI().subQuantityItemsBag(player, saobien, type);
                            Doiqua(player);
                        } else {
                            Service.getInstance().sendThongBao(player, "Không đủ vật phẩm sự kiện để đổi !");
                        }
                        break;
                    case 1:
                        type = 20;
                        if (oc.quantity >= type && so.quantity >= type && cua.quantity >= type
                                && saobien.quantity >= type && traidua != null && traidua.quantity >= 2) {
                            InventoryService.gI().subQuantityItemsBag(player, oc, type);
                            InventoryService.gI().subQuantityItemsBag(player, so, type);
                            InventoryService.gI().subQuantityItemsBag(player, cua, type);
                            InventoryService.gI().subQuantityItemsBag(player, saobien, type);
                            InventoryService.gI().subQuantityItemsBag(player, traidua, 2);
                            Doiqua(player);
                        } else {
                            Service.getInstance().sendThongBao(player, "Không đủ vật phẩm sự kiện để đổi !");
                        }
                        break;
                    default:
                        break;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Chưa đủ vật phẩm sự kiện để đổi !");
            }

        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    void Doiqua(Player player) {
        short idList[] = {1396, 1397, 1252, 1150, 1151, 1152, 1153};
        Item it = ItemService.gI().createNewItem(idList[Util.nextInt(idList.length)]);
        switch (it.template.id) {
            case 1396:
            case 1397:
                it.itemOptions.add(new ItemOption(50, Util.nextInt(8, 16)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(8, 16)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(8, 16)));
                it.itemOptions.add(new ItemOption(5, it.template.id == 1396 ? 10 : 5));
                if (Util.isTrue(98, 100)) {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                }
                break;
            case 1252:
                it.itemOptions.add(new ItemOption(50, Util.nextInt(3, 10)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(3, 10)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(3, 10)));
                if (Util.isTrue(98, 100)) {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                }
                break;
            default:
                break;
        }
        player.pointSk += 2;
        Service.getInstance().sendThongBao(player, "Bạn vừa đổi thành công và nhận được thêm 2 điểm sự kiện , vui lòng kiểm tra hành trang nhé !");
        InventoryService.gI().addItemBag(player, it, 9999);
        InventoryService.gI().sendItemBags(player);
    }

    void doiquaVang(Player player) {
        short idList[] = {1104, 1105, 1021, 865, 987};
        Item it = ItemService.gI().createNewItem(idList[Util.nextInt(idList.length)]);
        switch (it.template.id) {
            case 1104:
                it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 37)));
                it.itemOptions.add(new ItemOption(77, 33));
                it.itemOptions.add(new ItemOption(103, 33));
                if (Util.isTrue(98, 100)) {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                }
                break;
            case 1105:
                it.itemOptions.add(new ItemOption(50, 33));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                if (Util.isTrue(98, 100)) {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                }
                break;
            case 1021:
                it.itemOptions.add(new ItemOption(50, 3));
                it.itemOptions.add(new ItemOption(77, 3));
                it.itemOptions.add(new ItemOption(103, 3));
                it.itemOptions.add(new ItemOption(97, Util.nextInt(10, 30)));
                if (Util.isTrue(98, 100)) {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                }
                break;
            case 865:
                it.itemOptions.add(new ItemOption(50, Util.nextInt(8,18)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(8,18)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(8,18)));
                it.itemOptions.add(new ItemOption(5, 10));
                if (Util.isTrue(98, 100)) {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                }
                break;
            case 987:
                it.quantity = Util.nextInt(1, 3);
                break;
            default:
                break;
        }
        player.pointSk += 5;
        Service.getInstance().sendThongBao(player, "Bạn vừa đổi thành công và nhận được thêm 5 điểm sự kiện , vui lòng kiểm tra hành trang nhé !");
        InventoryService.gI().addItemBag(player, it, 9999);
        InventoryService.gI().sendItemBags(player);
    }

    void doiCayMia(Player player) {
//        Service.getInstance().sendThongBao(player, "Sự kiện đang được tiếp tục UPDATE !");
        Item caymia = InventoryService.gI().findItemBag(player, 1395);
        if (caymia != null && caymia.quantity >= 5000) {
            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                Item trung = ItemService.gI().createNewItem((short) 1398);
                InventoryService.gI().subQuantityItemsBag(player, caymia, 5000);
                InventoryService.gI().addItemBag(player, trung, 999);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được quả trứng VIP");
                player.pointSk += 1;
            } else {
                Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Cần 5000 cây mía để có thể đổi !");
        }
    }
}
