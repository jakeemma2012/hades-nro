package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.services.func.CombineServiceNew;
import nro.services.func.ShopService;
import nro.utils.Util;

public class BaHatMit extends Npc {

    public BaHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (this.mapId) {
                case 5:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ngươi tìm ta có việc gì?",
                            "Ép sao\ntrang bị",
                            "Pha lê\nhóa\ntrang bị",
                            "Trao đổi \n trang bị\n kích hoạt",
                            "Mở chỉ số kích hoạt ẩn ", "Ấn trang bị", "Thăng hoa trang bị\nkích hoạt");
                    break;
                case 121:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ngươi tìm ta có việc gì?",
                            "Về đảo\nrùa");
                    break;
                case 185:
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Đây là lĩnh vực của các vị thần cổ xửa , có khẳ năng  rơi những trang bị hiếm",
                            "Cửa hàng", "Nâng cấp\n Porata", "Đổi\n Chỉ số ", "Quay về");
                    break;
                case 222:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|2|Ngươi cũng đến đây vì sức mạnh bóng tối sao ?\n"
                                    + "|8|Có vẻ ta đã bảo quản không kỹ rồi !!!!",
                            "Shop\n vật phẩm", "Nâng cấp\n Bông tai Black", "Nâng cấp\n Bông tai Black \n2", "Đóng");
                    break;
                default:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ngươi tìm ta có việc gì?",
                            "Cửa hàng\nBùa",
                            "Nâng cấp\nVật phẩm",
                            "Nhập\nNgọc Rồng","Nâng cấp\n Bông tai\n Cấp 2", "Mở chỉ số\n Bông tai\n Cp 2");
                    break;
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (this.mapId) {
                case 5:
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI, this);
                                break;
                            case 1:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI,
                                        this);
                                break;
                            case 2:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.TRAO_DOI_DO_KICH_HOAT,
                                        this);
                                break;
                            case 3:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_DO_KICH_HOAT,
                                        this);
                                break;
                            case 4:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.AN_TRANG_BI, this);
                                break;
                            case 5:
                                // CombineServiceNew.gI().openTabCombine(player,
                                // CombineServiceNew.THANG_HOA_TRANG_BI);
                                this.npcChat(player, "Tính năng bảo trì !");
                                break;
                            default:
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combineNew.typeCombine) {
                            case CombineServiceNew.NANG_CAP_DO_THIEN_SU:
                                switch (select) {
                                    case 0:
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                        CombineServiceNew.gI().NANGCAPCHONDOTHIENSU(player, select);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                switch (select) {
                                    case 0:
                                        CombineServiceNew.gI().phaLeHoaTrangBiX(player, 1);
                                        break;
                                    case 1:
                                        CombineServiceNew.gI().phaLeHoaTrangBiX(player, 10);
                                        break;
                                    case 2:
                                        CombineServiceNew.gI().phaLeHoaTrangBiX(player, 20);
                                        break;
                                    case 3:
                                        CombineServiceNew.gI().phaLeHoaTrangBiX(player, 100);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case CombineServiceNew.XOA_DONG_ANH_SANG_NHANH:
                            case CombineServiceNew.XOA_DONG_ANH_SANG_THUONG:
                            case CombineServiceNew.NANG_CAP_DO_HUY_DIET:
                            case CombineServiceNew.NANG_CAP_HUY_DIET:
                            case CombineServiceNew.NANG_CAP_SKH:
                            case CombineServiceNew.EP_SAO_TRANG_BI:
                            case CombineServiceNew.GHEP_SACH:
                            case CombineServiceNew.AN_TRANG_BI:
                            case CombineServiceNew.THANG_HOA_TRANG_BI:
                            case CombineServiceNew.TRAO_DOI_DO_KICH_HOAT:
                            case CombineServiceNew.MO_CHI_SO_DO_KICH_HOAT:
                                if (select == 0) {
                                    CombineServiceNew.gI().startCombine(player);
                                }
                                break;
                        }
                    }
                    break;
                case 112:
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                break;
                        }
                    }
                    break;
                case 42:
                case 43:
                case 44:
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0: // shop bùa
                                createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                        "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                                + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                        "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng", "Bùa Đậu năng", "Đóng");
                                break;
                            case 1:
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.NANG_CAP_VAT_PHAM, this);
                                break;
                            case 2:
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.NHAP_NGOC_RONG, this);
                                break;
                            case 3:
                                CombineServiceNew.gI().openTabCombine(player,CombineServiceNew.NANG_CAP_BONG_TAI,this);
                                break;
                            case 4:
                                CombineServiceNew.gI().openTabCombine(player,CombineServiceNew.MO_CHI_SO_BONG_TAI,this);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                        switch (select) {
                            case 0:
                                ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_0, 0);
                                break;
                            case 1:
                                ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_1, 1);
                                break;
                            case 2:
                                ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_2, 2);
                                break;
                            case 3:
                                this.createOtherMenu(player, 100, "|2|Xin chào !\n"
                                        + "Ta có thể phù phép cho đệ con không tiêu hao thể lực trong 1 tháng\n"
                                        + "nhưng đồng thời con cũng mất 1 thỏi vàng để làm phí phù phép nhé !",
                                        "Đồng ý", "Đóng");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 100) {
                        if (select == 0) {
                            if (InventoryService.gI().getQuantity(player, 457) >= 1) {
                                InventoryService.gI().subQuantityItemsBag(player, 457, 1);
                                Service.getInstance().sendThongBao(player,
                                        "Bạn đã nhận được bùa Đậu năng trong 1 tháng , có thể kiểm tra thời gian tại cửa hàng bùa nhé !");
                                InventoryService.gI().sendItemBags(player);

                                if (player.charms.tdNoStamina < System.currentTimeMillis()) {
                                    player.charms.tdNoStamina = System.currentTimeMillis();
                                }
                                player.charms.tdNoStamina += (60 * 24 * 30) * 60 * 1000L;
                            } else {
                                Service.getInstance().sendThongBao(player, "Không có đủ 1 thỏi vàng !");
                            }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combineNew.typeCombine) {
                            case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                switch (select) {
                                    case 0:
                                    case 1:
                                        CombineServiceNew.gI().nangCapVatPham(player, select);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case CombineServiceNew.NANG_CAP_BONG_TAI:
                            case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                            case CombineServiceNew.LAM_PHEP_NHAP_DA:
                            case CombineServiceNew.NHAP_NGOC_RONG:
                            case CombineServiceNew.DAP_BONG_TAI_CAP_3:
                                if (select == 0) {
                                    CombineServiceNew.gI().startCombine(player);
                                }
                                break;
                        }
                    }
                    break;
                case 185:
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_BA_HAT_MIT_4, 4, -1);
                                break;
                            case 1:
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.NANG_CAP_BONG_TAI, this);
                                break;
                            case 2:
                                this.createOtherMenu(player, 1, "Con muốn nâng cấp theo phương pháp nào ?\n"
                                        + "Phương pháp thường sẽ tiêu tốn 1 lượng tài nguyên lớn hơn cao cấp đấy nhé !!!",
                                        "Nâng thường", "Nhập cao cấp", "Đóng");
                                break;
                            case 3:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, -1);
                                break;
                            default:
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        switch (select) {
                            case 0:
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.MO_CHI_SO_BONG_TAI, this);
                                break;
                            case 1:
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.DAP_BONG_TAI_CAP_3, this);
                                break;
                            default:
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combineNew.typeCombine) {
                            case CombineServiceNew.NANG_CAP_BONG_TAI:
                            case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                            case CombineServiceNew.DAP_BONG_TAI_CAP_3:
                                if (select == 0) {
                                    CombineServiceNew.gI().startCombine(player);
                                }
                                break;
                        }
                    }
                    break;
                case 222:
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ShopService.gI().openShopSpecial(player, this, ConstNpc.BA_HAT_MIT_BLACK, 5, -1);
                                break;
                            case 1:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI,
                                        this);
                                break;
                            case 2:
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.NANG_CAP_BONG_TAI_BONG_TOI_2, this);
                                break;
                            default:
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combineNew.typeCombine) {
                            case CombineServiceNew.NANG_CAP_BONG_TAI:
                            case CombineServiceNew.NANG_CAP_BONG_TAI_BONG_TOI_2:
                                if (select == 0) {
                                    CombineServiceNew.gI().startCombine(player);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    void TradeVatPham(Player player, int type) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            switch (type) {
                case 0:
                    Item tv = InventoryService.gI().findItemBag(player, 457);
                    Item daLt = InventoryService.gI().findItemBag(player, 1355);
                    if (tv != null && daLt != null && tv.quantity >= 100 && daLt.quantity >= 99) {
                        short idList[] = { 2017, 2026 };
                        Item lt = ItemService.gI().createNewItem(idList[Util.nextInt(idList.length)]);
                        lt.itemOptions.add(new ItemOption(50, Util.nextInt(5, 25)));
                        lt.itemOptions.add(new ItemOption(77, Util.nextInt(5, 25)));
                        lt.itemOptions.add(new ItemOption(103, Util.nextInt(5, 25)));
                        lt.itemOptions.add(new ItemOption(5, Util.nextInt(5, 15)));
                        InventoryService.gI().subQuantityItemsBag(player, tv, 100);
                        InventoryService.gI().subQuantityItemsBag(player, lt, 99);
                        InventoryService.gI().addItemBag(player, lt, 1);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Đổi thành công " + lt.template.name);
                    } else {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ thỏi vàng hoặc Đá linh thú ròi !",
                                "Đóng");
                    }
                    break;
                case 1:
                    Item daPet = InventoryService.gI().findItemBag(player, 1356);
                    if (player.inventory.ruby >= 100 && daPet != null && daPet.quantity >= 99) {
                        short idList[] = { 2017, 2026 };
                        Item pet = ItemService.gI().createNewItem(idList[Util.nextInt(idList.length)]);
                        pet.itemOptions.add(new ItemOption(50, Util.nextInt(10, 30)));
                        pet.itemOptions.add(new ItemOption(77, Util.nextInt(10, 30)));
                        pet.itemOptions.add(new ItemOption(103, Util.nextInt(10, 30)));
                        pet.itemOptions.add(new ItemOption(5, Util.nextInt(10, 25)));
                        InventoryService.gI().subQuantityItemsBag(player, daPet, 99);
                        player.inventory.subRuby(100);
                        InventoryService.gI().addItemBag(player, pet, 1);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Đổi thành công " + pet.template.name);
                    } else {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ hồng ngọc hoặc Đá Pet ròi !",
                                "Đóng");
                    }
                    break;
                default:
                    break;
            }
        } else {
            this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang đã đầy !", "Đóng");
        }
    }

}
