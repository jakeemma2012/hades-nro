package nro.services.func;

import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.data.DataGame;
import nro.lib.RandomCollection;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.npc.Npc;
import nro.models.npc.NpcManager;
import nro.models.npc.specialnpc.EggLinhThu;
import nro.models.player.Player;
import nro.art.ServerLog;
import nro.server.ServerNotify;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.MapService;
import nro.services.Service;
import nro.utils.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nro.services.RewardService;

public class CombineServiceNew {

    private static final int TIME_COMBINE = 500;

    private static final byte MAX_STAR_ITEM = 8;

    private static final byte MAX_SAO_FLAG_BAG = 5;

    private static final byte MAX_LEVEL_ITEM = 8;

    public static final byte OPEN_TAB_COMBINE = 0;
    public static final byte REOPEN_TAB_COMBINE = 1;
    public static final byte COMBINE_SUCCESS = 2;
    public static final byte COMBINE_FAIL = 3;
    public static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;

    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int CHUYEN_HOA_TRANG_BI = 502;
    public static final int DOI_VE_HUY_DIET = 503;
    public static final int DAP_SET_KICH_HOAT = 504;
    public static final int DOI_MANH_KICH_HOAT = 505;
    public static final int NANG_CAP_VAT_PHAM = 506;
    public static final int NANG_CAP_BONG_TAI = 507;
    public static final int LAM_PHEP_NHAP_DA = 508;
    public static final int NHAP_NGOC_RONG = 509;
    public static final int CHE_TAO_DO_THIEN_SU = 510;
    public static final int DAP_SET_KICH_HOAT_CAO_CAP = 511;
    public static final int GIA_HAN_CAI_TRANG = 512;
    public static final int PHA_LE_HOA_TRANG_BI_X10 = 514;
    public static final int NANG_CAP_DO_TS = 515;
    public static final int TRADE_DO_THAN_LINH = 516;
    public static final int NANG_CAP_DO_KICH_HOAT = 517;
    public static final int UPGRADE_CAITRANG = 518;
    public static final int MO_CHI_SO_BONG_TAI = 519;
    public static final int UPGRADE_LINHTHU = 520;
    public static final int EP_NGOC_RONG_BANG = 521;
    public static final int UPGRADE_THAN_LINH = 522;
    public static final int UPGRADE_PET = 523;
    public static final int TRADE_PET = 524;
    public static final int PHA_LE_HOA_DISGUISE = 525;
    public static final int NANG_CAP_BONG_TAI_CAP3 = 526;
    public static final int DAP_BONG_TAI_CAP_3 = 527;
    public static final int PHA_LE_HOA_CAI_TRANG = 528;
    public static final int NANG_CAP_SKH = 529;
    public static final int NANG_CAP_CAI_TRANG = 530;
    public static final int NANG_CAP_LINH_THU = 531;
    public static final int NANG_CAP_VPDL = 532;
    public static final int XOA_VPDL = 533;
    public static final int NANG_CAP_HUY_DIET = 534;

    public static final int NANG_CAP_ANH_SANG = 535;
    public static final int XOA_DONG_ANH_SANG_THUONG = 536;
    public static final int XOA_DONG_ANH_SANG_NHANH = 537;
    public static final int NANG_CAP_DO_HUY_DIET = 538;
    public static final int NANG_CAP_DO_THIEN_SU = 539;

    public static final int GHEP_MANH_GOKU = 540;
    public static final int GHEP_MANH_CUMBER = 541;
    public static final int GHEP_MANH_PIKKON = 542;
    public static final int GHEP_MANH_MI = 543;
    public static final int GHEP_MANH_CU_CAI_TRANG = 544;
    public static final int GHEP_MANH_BONG_BANG = 545;

    public static final int GHEP_SACH = 546;

    public static final int NANG_CAP_BONG_TAI_BONG_TOI_2 = 547;

    public static final int NO_TRUNG_LINH_THU = 548;

    public static final int AP_TRUNG_LINH_THU = 549;

    public static final int KHAM_TINH_THACH = 550;

    public static final int PHA_LE_HOA_LINH_THU = 551;

    public static final int CHUYEN_HOA_MA_THUAT = 552;

    public static final int PHAN_RA_THIEN_SU = 553;

    public static final int TRAO_DOI_DO_KICH_HOAT = 554;

    public static final int MO_CHI_SO_DO_KICH_HOAT = 555;

    public static final int AN_TRANG_BI = 556;

    public static final int THANG_HOA_TRANG_BI = 557;

    private static final int GOLD_MOCS_BONG_TAI = 500_000_000;

    private static final int RUBY_MOCS_BONG_TAI = 10_000;

    private static final int GOLD_BONG_TAI2 = 500_000_000;

    private static final int RUBY_BONG_TAI2 = 20_000;

    private static final int RATIO_NANG_CAP = 44;

    private String optChose[][] = { { "Set Gohan", "Set Krillin", "Set Songoku", "Set Goten", "Set Bulma" },
            { "Set Piccolo", "Set Dende", "Set Pikkoro Daimao", "Set Ma vương", "Set Thượng đế" },
            { "Set Kakarot", "Set Cadic", "Set Nappa", "Set Vegeta", "Set Ca Lích" } };

    public void showInfoCombine(Player player, int[] index) {
        player.combineNew.clearItemCombine();
        if (index.length > 0) {
            for (int i = 0; i < index.length; i++) {
                player.combineNew.itemsCombine.add(player.inventory.itemsBag.get(index[i]));
            }
        }
        switch (player.combineNew.typeCombine) {
            case MO_CHI_SO_DO_KICH_HOAT:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item vp = null;
                    Item dmt = null;
                    for (Item it : player.combineNew.itemsCombine) {
                        if (it.getType() >= 0 && it.getType() <= 4) {
                            vp = it;
                        }
                        if (it.getId() == 2030) {
                            dmt = it;
                        }
                    }
                    if (vp != null && dmt != null && dmt.quantity >= 2) {
                        ItemOption opt = vp.itemOptions.stream().filter(io -> io.optionTemplate.id == 234).findFirst()
                                .orElse(null);
                        if (opt != null) {
                            String npcSay = "|0|Cậu có muổn mở chỉ số kích hoạt";
                            npcSay += "\n|2|" + vp.getName();
                            npcSay += "\n|1|Yêu cầu cần:";
                            npcSay += "\n|2|2 Đá ma thuật\n 200 Tr vàng";
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Mở chỉ số\n kích hoạt", "Từ chối");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                    "Hãy bỏ vào 1 trang bị có chỉ số kích hoạt ẩn và 2 Đá ma thuật để tiếp tục !",
                                    "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                "Hãy bỏ vào 1 trang bị có chỉ số kích hoạt ẩn và 2 Đá ma thuật để tiếp tục !", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            "Hãy bỏ vào 1 trang bị có chỉ số kích hoạt ẩn và 2 Đá ma thuật để tiếp tục !", "Đóng");
                }
                break;
            case AN_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item trangBi = null;
                    Item nr1s = null;
                    Item dmt = null;
                    for (Item it : player.combineNew.itemsCombine) {
                        if (it.getType() >= 0 && it.getType() <= 4) {
                            trangBi = it;
                        }
                        if (it.getId() == 14) {
                            nr1s = it;
                        }
                        if (it.getId() == 2030) {
                            dmt = it;
                        }
                    }
                    if (trangBi != null && nr1s != null && dmt != null && dmt.quantity >= 2) {
                        ItemOption optAn = trangBi.itemOptions.stream()
                                .filter(io -> io.optionTemplate.id == 34 || io.optionTemplate.id == 35
                                        || io.optionTemplate.id == 36)
                                .findFirst().orElse(null);
                        if (optAn == null) {
                            String npcSay = "";
                            npcSay += "|2|" + trangBi.getName() + "\n|1|";
                            for (ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|2|Ấn trang bị cần\n";
                            npcSay += "|1| 200 Tr vàng";
                            npcSay += "\n|2|Ngươi có muốn ấn không?";
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Ấn trang bị",
                                    "Không");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Trang bị chỉ có thể Ấn 1 lần duy nhất !",
                                    "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy bỏ vào 1 trang bị cần ấn, X2 Đá ma thuật và X1 Ngọc rồng 1 sao để thực hiện !",
                                "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy bỏ vào 1 trang bị cần ấn, X2 Đá ma thuật và X1 Ngọc rồng 1 sao để thực hiện !",
                            "Đóng");
                }
                break;
            case TRAO_DOI_DO_KICH_HOAT:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item dts = player.combineNew.itemsCombine.get(0);
                    if (dts != null && dts.isDTS()) {
                        ItemOption csThuong = dts.getOption(41);
                        if (csThuong != null && csThuong.param >= 2) {

                            String npcSay = "|0|Ta sẽ trao đổi với cậu\n|2|";
                            npcSay += dts.template.name + "\n|1|";
                            for (ItemOption io : dts.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|2| và 200 Tr vàng\n";
                            npcSay += "|7|Lấy 1 trang bị kích hoạt ẩn tương ứng";
                            npcSay += "\n|0|Cậu có đồng ý không?";
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Đồng ý\n trao đổi",
                                    "Không");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Hãy bỏ vào 1 trang bị Thiên sứ có 2 dòng thưởng trở lên để thực hiện !", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy bỏ vào 1 trang bị Thiên sứ có 2 dòng thưởng trở lên để thực hiện !", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy bỏ vào 1 trang bị Thiên sứ có 2 dòng thưởng trở lên để thực hiện !", "Đóng");
                }
                break;
            case NANG_CAP_DO_THIEN_SU:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() <= 4) {
                    Item manh = null;
                    Item congThuc = null;
                    Item Dnc = null;
                    Item DMM = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isManhTS()) {
                            manh = item;
                        }
                        if (item.isDamayman()) {
                            DMM = item;
                        }

                        if (item.isdanangcapDoTs()) {
                            Dnc = item;
                        }

                        if (item.isCongthucNomal()) {
                            congThuc = item;
                        }
                    }

                    if (manh != null && congThuc != null && manh.quantity >= 999) {
                        String npcSay = "|1|Chế tạo " + getNameThieSu(manh.getId()) + " Thiên sứ "
                                + getGenderThienSu(congThuc.getId())
                                + "\nMạnh hơn tang bị Hủy diệt từ 20% đến 35%\n"
                                + "|2| Mảnh ghép " + manh.quantity + "/999 (Thất bại -99 mảnh ghép)\n";
                        if (Dnc != null) {
                            npcSay += Dnc.getName() + " (thêm " + ((Dnc.getId() - 1073) * 10) + "% tỉ lệ thành công)\n";
                        }
                        if (DMM != null) {
                            npcSay += DMM.getName() + " (thêm " + ((DMM.getId() - 1078) * 10)
                                    + "% tỉ lệ tối đa các chỉ số)\n";
                        }
                        player.combineNew.ratioCombine = 25 + (Dnc != null ? ((Dnc.getId() - 1073) * 10) : 0);
                        npcSay += "Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n";
                        npcSay += "|1|Phí nâng cấp: 200 Tr vàng";

                        this.whis.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Đồng ý", "Từ chối");
                    } else {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy bỏ vào ít nhất x1 Công thức và x999 Mảnh thiên sứ bất kì để thực hiện !", "Đóng");
                    }
                } else {
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy bỏ vào 2 - 4 trang bị để thực hiện nâng cấp !", "Đóng");
                }
                break;
            case CHUYEN_HOA_MA_THUAT:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item dts = player.combineNew.itemsCombine.get(0);
                    if (dts != null && dts.isDTL()) {
                        String npcSay = "|0| Cậu có muốn chuyển hóa\n|2|";
                        npcSay += dts.getName() + "\n|1|";
                        for (ItemOption io : dts.itemOptions) {
                            if (io.optionTemplate.id != 102) {
                                npcSay += io.getOptionString() + "\n";
                            }
                        }
                        npcSay += "\n|0| thành";
                        npcSay += "\n|2|" + getDaChuyenHoa(dts.getType()) + " Đá ma thuật\n";
                        this.whis.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Đồng ý", "Từ chối");

                    } else {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy bỏ vào 1 trang bị Thần Linh để thực hiện chuyển hóa !", "Đóng");
                    }
                } else {
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy bỏ vào 1 trang bị Thần Linh để thực hiện chuyển hóa !", "Đóng");
                }
                break;
            case PHAN_RA_THIEN_SU:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item dts = player.combineNew.itemsCombine.get(0);
                    if (dts != null && dts.isDTS()) {
                        this.whis.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                "|2|" + dts.getName()
                                        + "\n|1| Phân rã được x500 mảnh " + getNameType(dts.getType())
                                        + "\n Ngươi có đồng ý thực hiện phân rã không?",
                                "Phân rã", "Từ chối");
                    } else {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy bỏ vào 1 trang bị Thiên sứ để thực hiện phân rã !", "Đóng");
                    }
                } else {
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy bỏ vào 1 trang bị Thiên sứ để thực hiện phân rã !", "Đóng");
                }
                break;
            case KHAM_TINH_THACH:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item lthu = null;
                    Item tthach = null;
                    for (Item it : player.combineNew.itemsCombine) {
                        if (it.getType() == 72) {
                            lthu = it;
                        } else if (it.getId() >= 2014 && it.getId() <= 2018) {
                            tthach = it;
                        }
                    }
                    if (lthu != null && tthach != null) {
                        String npcSay = lthu.template.name + "\n|2|";
                        for (ItemOption io : lthu.itemOptions) {
                            if (io.optionTemplate.id != 102) {
                                npcSay += io.getOptionString() + "\n";
                            }
                        }
                        npcSay += "\n|1|Đồng ý nâng cấp?\n";

                        this.drief.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay,
                                "Đồng ý", "Từ chối");

                    } else {
                        this.drief.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy bảo vào 1 Linh thú và Tinh thạch cần thiết !", "Đóng");
                    }
                } else {
                    this.drief.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy bảo vào 1 Linh thú và Tinh thạch cần thiết !", "Đóng");
                }
                break;
            case PHA_LE_HOA_LINH_THU:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (item != null && item.getType() == 72) {
                        ItemOption optionStar = null;
                        for (ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                optionStar = io;
                                break;
                            }
                        }
                        if (optionStar == null || optionStar.param < 5) {
                            player.combineNew.ratioCombine = getRatioPhaLeHoa(
                                    optionStar != null ? optionStar.param : 0);
                            this.drief.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                    "|1| Cậu đang có : \n|2|" + item.template.name + "\n"
                                            + (optionStar != null ? optionStar.param : 0) + " sao pha lê\n"
                                            + "|1|Ta sẽ giúp cậu pha lê hóa Linh thú lên cấp sao cao hơn\n"
                                            + "|8|Cậu có muốn không?\n"
                                            + "|7| Tỉ lệ thành công : " + player.combineNew.ratioCombine + "%\n"
                                            + "|2| Chi phí : " + player.combineNew.ratioCombine * 1000 + " vàng",
                                    "Đồng ý", "Từ chối");
                        } else {
                            this.drief.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                    "Trứng linh thú này chỉ có thể pha lê hóa lên 5 sao", "Đóng");
                        }
                    } else {
                        this.drief.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ bỏ vào linh thú !", "Đóng");
                    }
                } else {
                    this.drief.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Chỉ bỏ vào linh thú !", "Đóng");
                }
                break;
            case AP_TRUNG_LINH_THU:
                if (player.egglinhthu == null) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item trung = null;
                        Item hon = null;
                        Item tv = InventoryService.gI().findItemBag(player, 457);

                        for (Item it : player.combineNew.itemsCombine) {
                            if (it.getId() == 2027 || it.getId() == 2028) {
                                trung = it;
                            }
                            if (it.getId() == 2029) {
                                hon = it;
                            }
                        }
                        if (trung != null && hon != null) {
                            if (trung != null && hon != null && hon.quantity >= 200) {
                                if (tv != null && tv.quantity >= 20) {
                                    this.bulmatl.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                            "|1|Cậu đang có\n"
                                                    + "|2|" + trung.template.name + "\n"
                                                    + hon.quantity + " Hồn linh thú"
                                                    + "\n|1| Tôi sẽ giúp cậu ấp quả trứng này\n"
                                                    + "|7|trong vòng 30 ngày với chi phí 20 thỏi vàng?\n"
                                                    + "|1|Cậu có đồng ý không?",
                                            "Đồng ý", "Từ chối");
                                } else {
                                    this.bulmatl.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Số lượng thỏi vàng không đủ!", "Đóng");
                                }

                            } else {
                                this.bulmatl.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Số lượng nguyên liệu không đủ để thực hiện!", "Đóng");
                            }
                        } else {
                            this.bulmatl.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Chỉ bỏ vào Trứng linh thú và Hồn lình thú !", "Đóng");
                        }
                    } else {
                        this.bulmatl.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ bỏ vào Trứng linh thú và Hồn lình thú !", "Đóng");
                    }
                } else {
                    this.bulmatl.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cậu đang có trứng linh thú chưa nở!", "Đóng");
                }
                break;
            case NO_TRUNG_LINH_THU:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item hon = null;
                    Item dns = null;
                    Item tv = InventoryService.gI().findItemBag(player, 457);
                    for (Item it : player.combineNew.itemsCombine) {
                        if (it.getId() == 2029) {
                            hon = it;
                        }
                        if (it.getId() == 674) {
                            dns = it;
                        }
                    }
                    if (hon != null && hon.quantity >= 200 && dns != null && dns.quantity >= 5) {
                        if (tv != null && tv.quantity >= 15) {
                            if (player.egglinhthu != null && player.egglinhthu.getSecondDone() <= 0) {
                                this.bulmatl.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        "|1|Cậu có muốn cho trứng nở hay không? Yêu cầu:\n"
                                                + "|2|200 hồn linh thú\n"
                                                + "5 đá ngũ sắc\n"
                                                + "15 thỏi vàng",
                                        "Nở trứng", "Từ chối");
                            } else {
                                this.bulmatl.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Trứng chưa đến thời gian nở!", "Đóng");
                            }
                        } else {
                            this.bulmatl.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ thỏi vàng!",
                                    "Đóng");
                        }
                    } else {
                        this.bulmatl.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ nguyên liệu!",
                                "Đóng");
                    }
                } else {
                    this.bulmatl.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ nguyên liệu!", "Đóng");
                }
                break;
            // case NANG_CAP_DO_THIEN_SU:
            // if (player.combineNew.itemsCombine.size() == 6) {
            // Item ts1 = player.combineNew.itemsCombine.get(0);
            // Item ts2 = player.combineNew.itemsCombine.get(1);
            // ;
            // Item ts3 = player.combineNew.itemsCombine.get(2);
            // ;
            // Item ts4 = player.combineNew.itemsCombine.get(3);
            // Item ts5 = player.combineNew.itemsCombine.get(4);
            // Item dats = player.combineNew.itemsCombine.get(5);
            // if (ts1.isNotNullItem() && ts1.isDHD()
            // && ts2.isNotNullItem() && ts2.isDHD()
            // && ts3.isNotNullItem() && ts3.isDHD()
            // && ts4.isNotNullItem() && ts4.isDHD()
            // && ts5.isNotNullItem() && ts5.isDHD()
            // && dats != null && dats.getId() == 1419) {
            // String npcSay = "|7|Nâng cấp trang bị Thiên Sứ\n"
            // + "Con có muốn dùng 6 vật phẩm để triệu hồi trang bị cấp THIÊN SỨ không ?";
            // this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
            // npcSay, "Áo\nThiên sứ", "Quần\nThiên sứ", "Găng\nThiên sứ",
            // "Giày\nThiên sứ", "Nhẫn\nThiên sứ", "Từ chối");
            // } else {
            // this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
            // "Hãy bỏ vào 5 trang bị cấp HỦY DIỆT và X1 Đá Thiên sứ để thực hiện", "Đóng");
            // }
            // } else {
            // this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
            // "!Hãy bỏ vào 5 trang bị cấp HỦY DIỆT và X1 Đá Thiên sứ để thực hiện",
            // "Đóng");
            // }
            // break;

            case NANG_CAP_DO_HUY_DIET:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item tl1 = player.combineNew.itemsCombine.get(0);
                    Item tl2 = player.combineNew.itemsCombine.get(1);
                    Item tl3 = player.combineNew.itemsCombine.get(2);
                    if (tl1 != null && tl2 != null && tl3 != null
                            && tl1.isDTL() && tl2.isDTL() && tl3.isDTL()) {
                        String npcSay = "|2|Nâng cấp trang bị Hủy Diệt\n"
                                + "|6|Con muốn dùng 3 vật phẩm để triệu hồi trang bị cấp HỦY DIỆT không?\n"
                                + "|7|Vật phẩm HỦY DIỆT sẽ dựa trên vật phẩm THẦN LINH đầu tiên !";
                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay, "Đồng ý", "Từ chối");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "|7|3 vật phẩm phải là vật phẩm Thần Linh", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "|7|Hãy bỏ vào 3 trang bị thần linh",
                            "Đóng");
                }
                break;
            case XOA_DONG_ANH_SANG_NHANH:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item vp = null;
                    Item da = null;
                    for (Item it : player.combineNew.itemsCombine) {
                        if (it.template.type == 11
                                || it.template.type == 21
                                || it.template.type == 5) {
                            vp = it;
                        }
                        if (it.template.id == 1981) {
                            da = it;
                        }
                    }
                    if (player.inventory.gold < 10_000_000_000L) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ 10 tỉ vàng để thực hiện",
                                "Đóng");
                        return;
                    }
                    if (vp != null && da != null && da.quantity >= 20) {
                        ItemOption lv = null;
                        for (ItemOption io : vp.itemOptions) {
                            if (io.optionTemplate.id == 212) {
                                lv = io;
                                break;
                            }
                        }
                        if (lv != null) {
                            String npcSay = "|7|Xóa chỉ số Ánh Sao\n"
                                    + "|2|Bạn thật sự muốn xóa hết toàn bộ Chỉ Số Ánh sao chứ ?\n"
                                    + "Chi phí xóa : 10 tỉ vàng";
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                    npcSay, "Xóa ngay", "Từ chối");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Phải là trang bị đã Ánh Sáng mới có thể xóa chỉ số !", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy cho vào 1 trang bị đã Ánh Sáng và X20 Đá pha lê Ánh sáng !", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy cho vào 1 trang bị đã Ánh Sáng và X20 Đá pha lê Ánh sáng !", "Đóng");
                }
                break;
            case XOA_DONG_ANH_SANG_THUONG:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item vp = null;
                    Item da = null;
                    for (Item it : player.combineNew.itemsCombine) {
                        if (it.template.type == 11
                                || it.template.type == 21
                                || it.template.type == 5) {
                            vp = it;
                        }
                        if (it.template.id == 1981) {
                            da = it;
                        }
                    }
                    if (vp != null && da != null && da.quantity >= 200) {
                        ItemOption lv = null;
                        for (ItemOption io : vp.itemOptions) {
                            if (io.optionTemplate.id == 212) {
                                lv = io;
                                break;
                            }
                        }
                        if (lv != null) {
                            String npcSay = "|7|Xóa chỉ số Ánh Sao\n"
                                    + "|2|Bạn thật sự muốn xóa hết toàn bộ Chỉ Số Ánh sao chứ ?";
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                    npcSay, "Xóa ngay", "Từ chối");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Phải là trang bị đã Ánh Sáng mới có thể xóa chỉ số !", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy cho vào 1 trang bị đã Ánh Sáng và X200 Đá pha lê Ánh sáng !", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy cho vào 1 trang bị đã Ánh Sáng và X200 Đá pha lê Ánh sáng !", "Đóng");
                }
                break;
            case NANG_CAP_ANH_SANG:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item vp = null;
                    Item da = null;
                    for (Item it : player.combineNew.itemsCombine) {
                        if (it.template.type == 11
                                || it.template.type == 21
                                || it.template.type == 5) {
                            vp = it;
                        }
                        if (it.template.id == 1981) {
                            da = it;
                        }
                    }
                    if (da != null && vp != null) {
                        int level = 0;
                        for (ItemOption io : vp.itemOptions) {
                            if (io.optionTemplate.id == 212) {
                                level = io.param;
                                break;
                            }
                        }
                        int sl = getCountDaNangCapDo(level);
                        if (level < 8) {
                            if (da.quantity >= sl) {
                                if (player.inventory.gold >= 200_00_000) {
                                    String npcSay = "|7|Trang bị nâng cấp Ánh Sáng \"" + vp.template.name
                                            + "\"\n"
                                            + "|0|Sau khi nâng cấp được thêm"
                                            + "\n ngẫu nhiên chỉ số Ánh sao tối HP, KI hoặc SD\n"
                                            + "Tỷ lệ thành công: " + getRatioBongToiTrangBi(level) + "%\n"
                                            + "|2|Cần " + sl + " Đá pha lê Ánh sáng";
                                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                            "1\n lần", "10\n lần", "20\n lần", "100\n lần", "Đóng");
                                } else {
                                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu "
                                            + Util.numberToMoney(200_000_000 - player.inventory.gold) + " vàng nữa",
                                            "Đóng");
                                }
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Phải có " + sl + " Đá pha lê Ánh sáng nhé !", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Trang bị đạt đạt cấp Ánh sao tối đa !", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy bỏ vào 1 trang bị có khả năng Ánh sáng và số lượng Đá pha lê Ánh sáng cần thiết để Ánh Sáng trang bị nhé !",
                                "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy bỏ vào 1 trang bị có khả năng Ánh sáng và số lượng Đá pha lê Ánh sáng cần thiết để Ánh Sáng trang bị nhé !",
                            "Đóng");
                }
                break;
            case DAP_BONG_TAI_CAP_3:
                if (player.combineNew.itemsCombine.size() == 4) {
                    Item bt = null;
                    Item mh = null;
                    Item mv = null;
                    Item giay = null;
                    for (Item it : player.combineNew.itemsCombine) {
                        if (it.TempIdIs(933)) {
                            mv = it;
                        }
                        if (it.TempIdIs(934)) {
                            mh = it;
                        }
                        if (it.TempIdIs(921)) {
                            bt = it;
                        }
                        if (it.TempIdIs(1032)) {
                            giay = it;
                        }
                    }
                    if (mv != null && mv.quantity >= 50
                            && mh != null && mh.quantity >= 10
                            && giay != null && bt != null) {
                        String npcSay = "Con muốn làm phép Mở lại chỉ số ẩn trong bông tai sao ?\n"
                                + "Sau khi nâng cấp thành công , bông tai sẽ được xóa mọi chỉ số cũ\nvà có được chỉ số mới ngẫu nhiên (Có khả năng lặp )\n"
                                + "Tỉ lệ thành công : 80%";
                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Làm phép", "Từ chối");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy cho vào X1 Bông Tai Porata, X50 Mảnh vỡ bông tai , X10 Mảnh hồn bông tai và X1 Giấy tạo tác",
                                "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy cho vào X1 Bông Tai Porata, X50 Mảnh vỡ bông tai , X10 Mảnh hồn bông tai và X1 Giấy tạo tác",
                            "Đóng");
                }
                break;
            case NANG_CAP_HUY_DIET:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item dtl1 = (player.combineNew.itemsCombine.get(0).isDTL() ? player.combineNew.itemsCombine.get(0)
                            : null);
                    Item dtl2 = (player.combineNew.itemsCombine.get(1).isDTL() ? player.combineNew.itemsCombine.get(1)
                            : null);
                    Item dtl3 = (player.combineNew.itemsCombine.get(2).isDTL() ? player.combineNew.itemsCombine.get(2)
                            : null);
                    if (dtl1 != null && dtl2 != null && dtl3 != null) {
                        if (player.inventory.gold >= 500_000_000) {
                            String npcSay = "|2|Nâng cấp trang bị Cấp Hủy diệt cần 3 trang bị Thần Linh hiến tế\n"
                                    + "Con muốn nâng cấp trang bị Hủy Diệt chứ ?\n"
                                    + "Sau khi nâng cấp thành công , con sẽ nhận được 1 trang bị Hủy Diệt !";
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                    npcSay, "Nâng cấp", "Từ chối");
                        } else {
                            baHatMit.createOtherMenu(
                                    player, ConstNpc.IGNORE_MENU, "Còn thiếu "
                                            + Util.numberToMoney(500_000_000 - player.inventory.gold) + " vàng nữa",
                                    "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy bỏ vào 3 trang bị thần linh , ta sẽ làm phép lên trang bị Hủy diệt theo món thần linh đầu tiên của ngươi!",
                                "Đóng");
                    }

                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy bỏ vào 3 trang bị thần linh , ta sẽ làm phép lên trang bị Hủy diệt theo món thần linh đầu tiên của ngươi!",
                            "Đóng");
                }
                break;
            case NANG_CAP_SKH:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item dtl = player.combineNew.itemsCombine.get(0);
                    if (dtl.isDHD()) {
                        if (player.inventory.gold >= 500_000_000) {
                            String npcSay = "|2|Hiến tế trang bị Hủy diệt\n"
                                    + "Con sẽ nhận được 1 trang bị Kích hoạt cùng loại ";
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                    npcSay, "Nâng cấp", "Đóng");
                        } else {
                            baHatMit.createOtherMenu(
                                    player, ConstNpc.IGNORE_MENU, "Còn thiếu "
                                            + Util.numberToMoney(500_000_000 - player.inventory.gold) + " vàng nữa",
                                    "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy bỏ vào 1 trang bị cấp Hủy Diệt", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy bỏ vào 1 trang bị cấp Hủy Diệt",
                            "Đóng");
                }
                break;
            case EP_SAO_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item trangBi = null;
                    Item daPhaLe = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (isTrangBiEpSaoPhale(item)) {
                            trangBi = item;
                        } else if (isDaPhaLe(item)) {
                            daPhaLe = item;
                        }
                    }
                    int star = 0; // sao pha lê đã ép
                    int starEmpty = 0; // lỗ sao pha lê
                    if (trangBi != null && daPhaLe != null) {
                        for (ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 102) {
                                star = io.param;
                            } else if (io.optionTemplate.id == 107) {
                                starEmpty = io.param;
                            }
                        }
                        if (star < starEmpty) {
                            player.combineNew.gemCombine = getGemEpSao(star);
                            String npcSay = trangBi.template.name + "\n|2|";
                            for (ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (daPhaLe.template.type == 30) {
                                for (ItemOption io : daPhaLe.itemOptions) {
                                    npcSay += "|7|" + io.getOptionString() + "\n";
                                }
                            } else {
                                npcSay += "|7|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name
                                        .replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                            }
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " ngọc";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                }
                break;
            case PHA_LE_HOA_TRANG_BI:
                if (player.combineNew.itemsCombine.size() <= 2) {
                    if (player.inventory.gold < 0) {
                        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bán vàng đi con", "Đóng");
                        break;
                    }
                    Item item = null;
                    Item thex10 = null;
                    if (player.combineNew.itemsCombine.size() == 2) {
                        for (Item it : player.combineNew.itemsCombine) {
                            if (isTrangBiPhaLeHoa(it)) {
                                item = it;
                            }
                            if (it.getId() == 1465) {
                                thex10 = it;
                            }
                        }
                    } else {
                        item = player.combineNew.itemsCombine.get(0);
                    }
                    int size = player.combineNew.itemsCombine.size();
                    if (size == 1 && isTrangBiPhaLeHoa(item)
                            || size == 2 && isTrangBiPhaLeHoa(item) && thex10 != null) {
                        int star = 0;
                        for (ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.combineNew.gemCombine = getGemPhaLeHoa(star);
                            player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                            float ratiohienthi = getRatioPhaLeHoa(star);
                            if (thex10 != null) {
                                ratiohienthi *= 10;
                                player.combineNew.ratioCombine *= 10;
                            }
                            String npcSay = item.template.name + "\n|2|";
                            for (ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            // npcSay += "|7|Tỉ lệ thành công: " + ratiohienthi + "%" + "\n";

                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                if (player.combineNew.gemCombine <= player.inventory.gem) {
                                    npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                    baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                            "1\n lần", "10\n lần", "20\n lần", "100\n lần", "Đóng");
                                } else {
                                    npcSay += "Còn thiếu "
                                            + Util.numberToMoney(player.combineNew.gemCombine - player.inventory.gem)
                                            + " ngọc";
                                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                                }
                            } else {
                                npcSay += "Còn thiếu "
                                        + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                                        + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ",
                                "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa",
                            "Đóng");
                }
                break;
            case NHAP_NGOC_RONG:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem()) {
                            if ((item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                                String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n" + "1 viên "
                                        + ItemService.gI().getTemplate((short) (item.template.id - 1)).name + "\n"
                                        + "|7|Cần 7 " + item.template.name;
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép",
                                        "Từ chối");
                            } // else if ((item.template.id == 14 && item.quantity >= 7)) {
                              // String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n" +
                              // "1 viên "
                              // + ItemService.gI().getTemplate((short) (925)).name + "\n" + "\n|7|Cần 7 "
                              // + item.template.name + "\n|7|Cần 500tr Vàng";
                              // this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                              // "Làm phép",
                              // "Từ chối");
                              // } else if (item.template.id == 926 && item.quantity >= 7) {
                              // String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n" +
                              // "1 viên "
                              // + ItemService.gI().getTemplate((short) (925)).name + "\n" + "\n|7|Cần 7 "
                              // + item.template.name + "\n|7|Cần 500tr Vàng";
                              // this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                              // "Làm phép",
                              // "Từ chối");
                              // }
                            else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Cần 7 viên ngọc rồng cùng loại và dưới 2 sao ", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 7 viên ngọc rồng cùng loại và dưới 2 sao ", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 7 viên ngọc rồng cùng loại và dưới 2 sao ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống",
                            "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 2 || player.combineNew.itemsCombine.size() == 3) {
                    Item mv = null;
                    Item mh = null;
                    Item bua = null;
                    for (Item it : player.combineNew.itemsCombine) {
                        switch (it.getId()) {
                            case 933:
                                mv = it;
                                break;
                            case 934:
                                mh = it;
                                break;
                            case 1446:
                                bua = it;
                                break;
                            default:
                                break;
                        }
                    }

                    if (mv != null && mh != null && mv.quantity >= 999 && mh.quantity >= 20) {
                        StringBuilder st = new StringBuilder();
                        if (bua != null) {
                            st.append("|8|Con đã tìm thấy rồi sao !\n")
                                    .append("|2|Sức mạnh Bông tai Bóng tối\n")
                                    .append("Con có muốn đổi những vật phẩm này lấy Bông tai Bóng tối và nhận thêm x2 chỉ số ngẫu nhiên không ?");
                        } else {
                            st.append("|8|Con đã tìm thấy rồi sao !\n")
                                    .append("|2|Sức mạnh Bông tai Bóng tối\n")
                                    .append("Con có muốn đổi những vật phẩm này lấy Bông tai Bóng tối và nhận được chỉ số ngẫu nhiên không ?");
                        }
                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, st.toString(),
                                "Đồng ý", "Từ chối");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Không đủ X999 mảnh vỡ, X20 mảnh hồn và 1 Bông tai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy bỏ vào ít nhất X999 mảnh vỡ, X20 mảnh hồn và 1 Bông tai chứ ", "Đóng");
                }

                break;
            case MO_CHI_SO_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item prt = null;
                    Item mv = null;
                    Item mh = null;
                    for (Item it : player.combineNew.itemsCombine) {
                        if (it.template.id == 934) {
                            mh = it;
                        }
                        if (it.template.id == 933) {
                            mv = it;
                        }
                        if (it.template.id == 921) {
                            prt = it;
                        }
                    }
                    if (mv != null && mv.quantity >= 1000
                            && mh != null && mh.quantity >= 500
                            && prt != null) {
                        String npcSay = "Con muốn làm phép Mở lại chỉ số ẩn trong bông tai sao ?\n"
                                + "Sau khi nâng cấp thành công , bông tai sẽ được xóa mọi chỉ số cũ\nvà có được chỉ số mới ngẫu nhiên (Có khả năng lặp )\n"
                                + "Tỉ lệ thành công : 80%";
                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Làm phép", "Từ chối");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy đưa vào ô làm phép 1 bông tai Porata cấp 2 ,X1000 Mảnh vỡ bông tai và X500 Mảnh hồn bông tai Porata để ta làm phép nhé !",
                                "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy đưa vào ô làm phép 1 bông tai Porata cấp 2,X1000 Mảnh vỡ bông tai và X500 Mảnh hồn bông tai Porata để ta làm phép nhé !",
                            "Đóng");
                }
                break;
            case NANG_CAP_VAT_PHAM:
                if (player.combineNew.itemsCombine.size() == 2) {
                    if (isCoupleItemNangCap(player.combineNew.itemsCombine.get(0),
                            player.combineNew.itemsCombine.get(1))) {
                        Item trangBi = null;
                        Item daNangCap = null;
                        Item buaBaoVe = InventoryService.gI().findBuaBaoVeNangCap(player);
                        if (player.combineNew.itemsCombine.get(0).template.type < 5) {
                            trangBi = player.combineNew.itemsCombine.get(0);
                            daNangCap = player.combineNew.itemsCombine.get(1);
                        } else {
                            trangBi = player.combineNew.itemsCombine.get(1);
                            daNangCap = player.combineNew.itemsCombine.get(0);
                        }

                        int level = 0;
                        for (ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM) {
                            long goldCombine = 5_000_000_000L;
                            player.combineNew.ratioCombine = getTileNangCapDo(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                            String npcSay = "|2|Hiện tại " + trangBi.template.name + " (+" + level + ")\n|0|";
                            for (ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            for (ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id == 47 || io.optionTemplate.id == 6 || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7 || io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 22 || io.optionTemplate.id == 23) {
                                    option = io.optionTemplate.name;
                                    param = io.param + (io.param * 10 / 100);
                                    break;
                                }
                            }
                            npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param)) + "\n|7|Tỉ lệ thành công: "
                                    + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > daNangCap.quantity ? "|7|" : "|1|") + "Cần "
                                    + player.combineNew.countDaNangCap + " " + daNangCap.template.name + "\n"
                                    + (goldCombine > player.inventory.gold ? "|7|" : "|1|") + "Cần "
                                    + Util.numberToMoney(goldCombine) + " vàng";
                            if (level == 2 || level == 4 || level == 6) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > daNangCap.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                                        "Còn thiếu\n" + (player.combineNew.countDaNangCap - daNangCap.quantity) + " "
                                                + daNangCap.template.name);
                            } else if (goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                                        "Còn thiếu\n"
                                                + Util.numberToMoney(
                                                        (goldCombine - player.inventory.gold))
                                                + " vàng");
                            } else {
                                if (buaBaoVe != null) {
                                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                            "Nâng cấp", "Nâng cấp\n bảo vệ\n (Còn " + buaBaoVe.quantity + ")");
                                } else {
                                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                            "Nâng cấp");
                                }
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                }
                break;

        }
    }

    public void startCombine(Player player) {
        if (Util.canDoWithTime(player.combineNew.lastTimeCombine, TIME_COMBINE)) {
            switch (player.combineNew.typeCombine) {
                case EP_SAO_TRANG_BI:
                    epSaoTrangBi(player);
                    break;
                case PHA_LE_HOA_TRANG_BI:
                    phaLeHoaTrangBi(player);
                    break;
                case NHAP_NGOC_RONG:
                    nhapNgocRong(player);
                    break;
                case NANG_CAP_BONG_TAI:
                    nangCapBongTai(player);
                    break;
                case MO_CHI_SO_BONG_TAI:
                    moChiSoBongTai(player);
                    break;
                case DAP_BONG_TAI_CAP_3:
                    dapBongTaiCap3(player);
                    break;
                case NANG_CAP_SKH:
                    NangcapSKH(player);
                    break;
                case NO_TRUNG_LINH_THU:
                    noTrungLinhThu(player);
                    break;
                case AP_TRUNG_LINH_THU:
                    apTrungLinhThu(player);
                    break;
                case PHA_LE_HOA_LINH_THU:
                    phaLeHoaLinhThu(player);
                    break;
                case KHAM_TINH_THACH:
                    khamTinhThach(player);
                    break;
                case PHAN_RA_THIEN_SU:
                    phanRaThienSu(player);
                    break;
                case CHUYEN_HOA_MA_THUAT:
                    chuyenHoaMaThuat(player);
                    break;
                case NANG_CAP_DO_THIEN_SU:
                    nangCapDoThienSu(player);
                    break;
                case TRAO_DOI_DO_KICH_HOAT:
                    traodidokichhoat(player);
                    break;
                case AN_TRANG_BI:
                    anTrangBi(player);
                    break;
                case MO_CHI_SO_DO_KICH_HOAT:
                    moChiSoDoKichHoat(player);
                    break;
            }
            player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
            player.combineNew.clearParamCombine();
            player.combineNew.lastTimeCombine = System.currentTimeMillis();
        } else {
            Service.getInstance().sendThongBao(player, "Hãy đợi 5 giây để thực hiện lại!");
        }
    }

    void moChiSoDoKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item vp = null;
            Item dmt = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.getType() >= 0 && it.getType() <= 4) {
                    vp = it;
                }
                if (it.getId() == 2030) {
                    dmt = it;
                }
            }
            if (vp != null && dmt != null && dmt.quantity >= 2) {
                ItemOption opt = vp.itemOptions.stream().filter(io -> io.optionTemplate.id == 234).findFirst()
                        .orElse(null);
                if (opt != null) {
                    vp.itemOptions.remove(vp.getOption(234));
                    vp.itemOptions.remove(vp.getOption(30));
                    RewardService.gI().initActivationOption(vp.template.gender, vp.getType(), vp.itemOptions);
                    InventoryService.gI().subQuantityItemsBag(player, dmt, 2);
                    InventoryService.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                    Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                }
            }
        }
    }

    void anTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item trangBi = null;
            Item nr1s = null;
            Item dmt = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.getType() >= 0 && it.getType() <= 4) {
                    trangBi = it;
                }
                if (it.getId() == 14) {
                    nr1s = it;
                }
                if (it.getId() == 2030) {
                    dmt = it;
                }
            }
            if (trangBi != null && nr1s != null && dmt != null && dmt.quantity >= 2) {
                ItemOption optAn = trangBi.itemOptions.stream()
                        .filter(io -> io.optionTemplate.id == 34 || io.optionTemplate.id == 35
                                || io.optionTemplate.id == 36)
                        .findFirst().orElse(null);
                if (optAn == null) {
                    trangBi.itemOptions.add(new ItemOption(Util.nextInt(34, 36), 1));
                    InventoryService.gI().subQuantityItemsBag(player, dmt, 2);
                    InventoryService.gI().subQuantityItemsBag(player, nr1s, 1);
                    InventoryService.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                    Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                }
            }
        }
    }

    void traodidokichhoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item dts = player.combineNew.itemsCombine.get(0);
            if (dts != null && dts.isDTS()) {
                ItemOption csThuong = dts.getOption(41);
                if (csThuong != null && csThuong.param >= 2) {
                    Item vp = ItemService.gI().createNewItem(
                            (short) ConstItem.LIST_ITEM_CLOTHES[dts.template.gender][dts.getType()][Util.nextInt(3)]);
                    RewardService.gI().initBaseOptionClothes(vp);
                    vp.itemOptions.add(new ItemOption(234, 1));
                    vp.itemOptions.add(new ItemOption(30, 1));
                    sendEffectOpenItem(player, dts.template.iconID, vp.template.iconID);
                    InventoryService.gI().addItemBag(player, vp, 1);
                    InventoryService.gI().subQuantityItemsBag(player, dts, 1);
                    InventoryService.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void nangCapDoThienSu(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() <= 4) {
            Item manh = null;
            Item congThuc = null;
            Item Dnc = null;
            Item DMM = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isManhTS()) {
                    manh = item;
                }
                if (item.isDamayman()) {
                    DMM = item;
                }

                if (item.isdanangcapDoTs()) {
                    Dnc = item;
                }

                if (item.isCongthucNomal()) {
                    congThuc = item;
                }
            }

            if (manh != null && congThuc != null && manh.quantity >= 999) {
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    Item dts = ItemService.gI().createNewItem(
                            (short) ConstItem.doSKHVip[getTypeThienSu(
                                    manh.getId())][congThuc.template.gender][14],
                            1);
                    RewardService.gI().initBaseOptionClothes(dts);

                    int csThuong = 0;

                    if (DMM != null) {
                        if (Util.isTrue(((DMM.getId() - 1078) * 10), 100)) {
                            csThuong = Util.getOne(2, 3);
                        } else {
                            csThuong = 1;
                        }
                    }
                    int[] opt = { 50, 5, 77, 103, 80, 94, 108, 94, 95, 97, 18 };
                    dts.itemOptions.add(new ItemOption(41, csThuong));
                    for (int i = 0; i < csThuong; i++) {
                        if (Util.isTrue(25, 100)) {
                            dts.itemOptions.add(new ItemOption(opt[Util.nextInt(opt.length)], Util.getOne(1, 5)));
                        } else {
                            dts.itemOptions.add(new ItemOption(opt[Util.nextInt(opt.length)], Util.getOne(1, 4)));
                        }
                    }

                    InventoryService.gI().addItemBag(player, dts, 1);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                } else {
                    Service.getInstance().sendEffectCombine(player, COMBINE_FAIL);
                }

                InventoryService.gI().subQuantityItemsBag(player, manh, 999);
                InventoryService.gI().subQuantityItemsBag(player, congThuc, 1);
                if (Dnc != null) {
                    InventoryService.gI().subQuantityItemsBag(player, Dnc, 1);
                }
                if (DMM != null) {
                    InventoryService.gI().subQuantityItemsBag(player, DMM, 1);
                }

                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        }
    }

    void chuyenHoaMaThuat(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item dts = player.combineNew.itemsCombine.get(0);
            if (dts != null && dts.isDTL()) {
                Item dmt = ItemService.gI().createNewItem((short) 2030, getDaChuyenHoa(dts.getType()));
                InventoryService.gI().subQuantityItemsBag(player, dts, 1);
                InventoryService.gI().addItemBag(player, dmt, 9999);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
                Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
            }
        }
    }

    void phanRaThienSu(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item dts = player.combineNew.itemsCombine.get(0);
            if (dts != null && dts.isDTS()) {
                short[] listId_manh = { 1066, 1067, 1070, 1068, 1069 };
                Item manh = ItemService.gI().createNewItem(listId_manh[dts.getType()], 500);
                InventoryService.gI().subQuantityItemsBag(player, dts, 1);
                InventoryService.gI().addItemBag(player, manh, 9999);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
                Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
            }
        }
    }

    void khamTinhThach(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item lthu = null;
            Item tthach = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.getType() == 72) {
                    lthu = it;
                } else if (it.getId() >= 2014 && it.getId() <= 2018) {
                    tthach = it;
                }
            }
            if (lthu != null && tthach != null) {
                int star = 0;
                int starEmpty = 0;
                ItemOption optionStar = null;
                for (ItemOption io : lthu.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                if (star < starEmpty) {
                    int optionId = getOptionTinhThach(tthach);
                    int param = this.getParamDaTinhThach(tthach);
                    ItemOption option = null;
                    for (ItemOption io : lthu.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        lthu.itemOptions.add(new ItemOption(optionId, param));
                    }
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        lthu.itemOptions.add(new ItemOption(102, 1));
                    }

                    Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                    InventoryService.gI().subQuantityItemsBag(player, tthach, 1);
                    InventoryService.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    void phaLeHoaLinhThu(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item item = player.combineNew.itemsCombine.get(0);
            if (item != null && item.getType() == 72) {
                ItemOption optionStar = null;
                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        optionStar = io;
                        break;
                    }
                }
                if (optionStar == null || optionStar.param < 5) {
                    player.combineNew.ratioCombine = getRatioPhaLeHoa(
                            optionStar != null ? optionStar.param : 0);

                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        if (optionStar == null) {
                            item.itemOptions.add(new ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                    } else {
                        Service.getInstance().sendEffectCombine(player, COMBINE_FAIL);
                    }

                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    void apTrungLinhThu(Player player) {
        if (player.egglinhthu == null) {
            if (player.combineNew.itemsCombine.size() == 2) {
                Item trung = null;
                Item hon = null;
                Item tv = InventoryService.gI().findItemBag(player, 457);
                for (Item it : player.combineNew.itemsCombine) {
                    if (it.getId() == 2027 || it.getId() == 2028) {
                        trung = it;
                    }
                    if (it.getId() == 2029) {
                        hon = it;
                    }
                }
                if (trung != null && hon != null) {
                    if (trung != null && hon != null && hon.quantity >= 200) {
                        if (tv != null && tv.quantity >= 20) {
                            switch (trung.getId()) {
                                case 2027:
                                    EggLinhThu.createEggLinhThu(player, 0);
                                    break;
                                case 2028:
                                    EggLinhThu.createEggLinhThu(player, 1);
                                    break;
                            }

                            InventoryService.gI().subQuantityItemsBag(player, hon, 200);
                            InventoryService.gI().subQuantityItemsBag(player, tv, 20);
                            InventoryService.gI().subQuantityItemsBag(player, trung, 1);
                            InventoryService.gI().sendItemBags(player);
                            reOpenItemCombine(player);
                            player.egglinhthu.sendEggLinhThu();
                            Service.getInstance().sendEffectHideNPC(player, (byte) 50, (byte) 1);
                            Service.getInstance().sendThongBao(player, "Ấp trứng thành công");
                        }
                    }
                }
            }
        }
    }

    void noTrungLinhThu(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item hon = null;
            Item dns = null;
            Item tv = InventoryService.gI().findItemBag(player, 457);
            for (Item it : player.combineNew.itemsCombine) {
                if (it.getId() == 2029) {
                    hon = it;
                }
                if (it.getId() == 674) {
                    dns = it;
                }
            }
            if (hon != null && hon.quantity >= 200 && dns != null && dns.quantity >= 5) {
                if (tv != null && tv.quantity >= 15) {
                    if (player.egglinhthu != null && player.egglinhthu.getSecondDone() <= 0) {
                        short[] idlthu;
                        if (player.egglinhthu.type == 0) {
                            idlthu = new short[] { 2021, 2024, 2019, 2020 };
                        } else {
                            idlthu = new short[] { 2025, 2023, 2022, 2026 };
                        }

                        Item linhthu = ItemService.gI().createNewItem(idlthu[Util.nextInt(idlthu.length)]);

                        player.egglinhthu.laychiso(player, linhthu, 0);

                        CombineServiceNew.gI().sendEffectOpenItem(player,
                                (short) (player.egglinhthu.type == 0 ? 15073 : 15074),
                                (short) linhthu.template.iconID);

                        InventoryService.gI().subQuantityItemsBag(player, tv, 15);
                        InventoryService.gI().subQuantityItemsBag(player, hon, 200);
                        InventoryService.gI().subQuantityItemsBag(player, dns, 5);
                        InventoryService.gI().addItemBag(player, linhthu, 1);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);
                        player.egglinhthu = null;
                        Service.getInstance().sendEffectHideNPC(player, (byte) 50, (byte) 0);
                    }
                }
            }
        }
    }

    void nangcapbongtaiBongtoiCap2(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item mbt = null;
            Item xu = null;
            Item bt = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.getId() == 933) {
                    mbt = it;
                }
                if (it.getId() == 1497) {
                    xu = it;
                }
                if (it.getId() == 921) {
                    bt = it;
                }
            }
            if (mbt != null && mbt.quantity >= 9999 && xu != null && bt != null) {
                if (!bt.haveOption(72)) {
                    if (player.inventory.gold >= 500_000_000) {
                        if (Util.isTrue(25, 100)) {
                            bt.template = ItemService.gI().getTemplate(1496);
                            ItemOption opt = bt.itemOptions.get(0);
                            bt.itemOptions.clear();
                            bt.itemOptions.add(new ItemOption(72, 2));
                            bt.itemOptions.add(new ItemOption(opt.optionTemplate.id, opt.param * 2));
                            Service.getInstance().sendThongBaoOK(player, "Bóng tối Hóa thành công");
                        } else {
                            Service.getInstance().sendThongBao(player, "Bóng tối Hóa thất bại");
                        }
                        InventoryService.gI().subQuantityItemsBag(player, mbt, 9999);
                        InventoryService.gI().subQuantityItemsBag(player, xu, 1);
                        player.inventory.gold -= 500_000_000;
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        reOpenItemCombine(player);
                    }
                }
            }
        }
    }

    void GhepThienThu(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item sach = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.getId() == 1471) {
                    sach = it;
                }
            }
            if (sach != null && sach.quantity >= 50) {
                if (player.inventory.gold >= 500_000_000) {
                    Item thienThu = ItemService.gI().createNewItem((short) Util.nextInt(1466, 1470));
                    switch (thienThu.getId()) {
                        case 1466:
                            thienThu.itemOptions.add(new ItemOption(47, Util.nextInt(10, 200)));
                            break;
                        case 1467:
                            thienThu.itemOptions.add(new ItemOption(23, Util.nextInt(1, 20)));
                            break;
                        case 1468:
                            thienThu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                            break;
                        case 1469:
                            thienThu.itemOptions.add(new ItemOption(22, Util.nextInt(1, 20)));
                            break;
                        case 1470:
                            thienThu.itemOptions.add(new ItemOption(0, Util.nextInt(100, 2000)));
                            break;
                        default:
                            break;
                    }
                    thienThu.itemOptions.add(new ItemOption(30, 0));

                    InventoryService.gI().addItemBag(player, thienThu, 1);
                    InventoryService.gI().subQuantityItemsBag(player, sach, 50);
                    player.inventory.gold -= 500_000_000;
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);

                }
            }
        }
    }

    void NangCaptragnbiHuydiet(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item tl1 = player.combineNew.itemsCombine.get(0);
            Item tl2 = player.combineNew.itemsCombine.get(1);
            Item tl3 = player.combineNew.itemsCombine.get(2);
            if (tl1 != null && tl2 != null && tl3 != null
                    && tl1.isDTL() && tl2.isDTL() && tl3.isDTL()) {
                int gender = tl1.template.gender;
                if (gender == 3) {
                    gender = player.gender;
                }
                Item dhd = ItemService.gI().createNewItem((short) ConstItem.doSKHVip[tl1.template.type][gender][13]);
                RewardService.gI().initBaseOptionClothes(dhd);

                InventoryService.gI().addItemBag(player, dhd, 1);

                InventoryService.gI().subQuantityItemsBag(player, tl1, 1);
                InventoryService.gI().subQuantityItemsBag(player, tl2, 1);
                InventoryService.gI().subQuantityItemsBag(player, tl3, 1);

                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        }
    }

    void XoaAnhSangNhanh(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item vp = null;
            Item da = null;
            if (player.inventory.gold < 10_000_000_000L) {
                return;
            }
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.type == 11
                        || it.template.type == 21
                        || it.template.type == 5) {
                    vp = it;
                }
                if (it.template.id == 1981) {
                    da = it;
                }
            }
            if (vp != null && da != null && da.quantity >= 20) {
                ItemOption lV = null;
                ItemOption opt207 = null;
                ItemOption opt208 = null;
                ItemOption opt209 = null;
                for (ItemOption io : vp.itemOptions) {
                    if (io.optionTemplate.id == 207) {
                        opt207 = io;
                    }
                    if (io.optionTemplate.id == 208) {
                        opt208 = io;
                    }
                    if (io.optionTemplate.id == 209) {
                        opt209 = io;
                    }
                    if (io.optionTemplate.id == 212) {
                        lV = io;
                    }
                }
                if (lV != null) {
                    if (opt207 != null) {
                        vp.itemOptions.remove(opt207);
                    }
                    if (opt208 != null) {
                        vp.itemOptions.remove(opt208);
                    }
                    if (opt209 != null) {
                        vp.itemOptions.remove(opt209);
                    }
                    vp.itemOptions.remove(lV);
                    Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                    player.inventory.gold -= 10_000_000_000L;
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().subQuantityItemsBag(player, da, 20);
                    InventoryService.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    void XoaAnhSangThuong(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item vp = null;
            Item da = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.type == 11
                        || it.template.type == 21
                        || it.template.type == 5) {
                    vp = it;
                }
                if (it.template.id == 1981) {
                    da = it;
                }
            }
            if (vp != null && da != null && da.quantity >= 200) {
                ItemOption lV = null;
                ItemOption opt207 = null;
                ItemOption opt208 = null;
                ItemOption opt209 = null;
                for (ItemOption io : vp.itemOptions) {
                    if (io.optionTemplate.id == 207) {
                        opt207 = io;
                    }
                    if (io.optionTemplate.id == 208) {
                        opt208 = io;
                    }
                    if (io.optionTemplate.id == 209) {
                        opt209 = io;
                    }
                    if (io.optionTemplate.id == 212) {
                        lV = io;
                    }
                }
                if (lV != null) {
                    if (opt207 != null) {
                        vp.itemOptions.remove(opt207);
                    }
                    if (opt208 != null) {
                        vp.itemOptions.remove(opt208);
                    }
                    if (opt209 != null) {
                        vp.itemOptions.remove(opt209);
                    }
                    vp.itemOptions.remove(lV);
                    Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                    InventoryService.gI().subQuantityItemsBag(player, da, 200);
                    InventoryService.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    boolean NangCapAnhSang(Player player) {
        boolean flag = false;
        if (player.combineNew.itemsCombine.size() == 2) {
            Item vp = null;
            Item da = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.type == 11
                        || it.template.type == 21
                        || it.template.type == 5) {
                    vp = it;
                }
                if (it.template.id == 1981) {
                    da = it;
                }
            }
            if (da != null && vp != null) {
                int level = 0;
                ItemOption lV = null;
                ItemOption opt207 = null;
                ItemOption opt208 = null;
                ItemOption opt209 = null;
                for (ItemOption io : vp.itemOptions) {
                    if (io.optionTemplate.id == 207) {
                        opt207 = io;
                    }
                    if (io.optionTemplate.id == 208) {
                        opt208 = io;
                    }
                    if (io.optionTemplate.id == 209) {
                        opt209 = io;
                    }
                    if (io.optionTemplate.id == 212) {
                        lV = io;
                        level = io.param;
                    }
                }

                int sl = getCountDaNangCapDo(level);
                if (level < 8) {
                    if (da.quantity >= sl) {
                        if (player.inventory.gold < 200_000_000) {
                            return false;
                        }
                        Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                        float ratio = getRatioBongToiTrangBi(level);
                        if (Util.isTrue(ratio, 100)) {
                            if (vp.haveOption(212)) {
                                lV.param += 1;
                            } else {
                                vp.itemOptions.add(new ItemOption(212, 1));
                            }
                            List<Integer> idOptionHacHoa = Arrays.asList(207, 208, 209);
                            int randomOption = idOptionHacHoa.get(Util.nextInt(0, 2));
                            int param = getParamAnhSang(level);
                            if (!vp.haveOption(randomOption)) {
                                vp.itemOptions.add(new ItemOption(randomOption, param));
                            } else {
                                for (ItemOption io : vp.itemOptions) {
                                    if (io.optionTemplate.id == randomOption) {
                                        io.param += param;
                                        break;
                                    }
                                }
                            }
                            flag = true;
                        } else {
                            Service.getInstance().sendEffectCombine(player, COMBINE_FAIL);
                        }
                        player.inventory.subGold(200_000_000);
                        Service.getInstance().sendMoney(player);
                        InventoryService.gI().subQuantityItemsBag(player, da, sl);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);
                    }
                }
            }
        }
        return flag;
    }

    void NangCapHuyDiet(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item dtl1 = (player.combineNew.itemsCombine.get(0).isDTL() ? player.combineNew.itemsCombine.get(0) : null);
            Item dtl2 = (player.combineNew.itemsCombine.get(1).isDTL() ? player.combineNew.itemsCombine.get(1) : null);
            Item dtl3 = (player.combineNew.itemsCombine.get(2).isDTL() ? player.combineNew.itemsCombine.get(2) : null);
            if (dtl1 != null && dtl2 != null && dtl3 != null) {
                if (player.inventory.gold < 500_00_000) {
                    return;
                }
                int gender = dtl1.template.gender;
                if (gender == 3) {
                    gender = player.gender;
                }
                Item dhd = ItemService.gI().createNewItem((short) ConstItem.doSKHVip[dtl1.template.type][gender][13]);
                RewardService.gI().initBaseOptionClothes(dhd);
                Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                InventoryService.gI().subQuantityItemsBag(player, dtl1, 1);
                InventoryService.gI().subQuantityItemsBag(player, dtl2, 1);
                InventoryService.gI().subQuantityItemsBag(player, dtl3, 1);
                InventoryService.gI().addItemBag(player, dhd, 1);
                player.inventory.subGold(500_000_000);
                Service.getInstance().sendMoney(player);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        }
    }

    void NangcapSKH(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item dtl = player.combineNew.itemsCombine.get(0);
            if (dtl.isDHD()) {
                if (player.inventory.gold < 500_000_000) {
                    return;
                }
                int gender = dtl.template.gender;
                if (gender == 3) {
                    gender = player.gender;
                }
                Item skh = ItemService.gI().createNewItem(
                        (short) ConstItem.LIST_ITEM_CLOTHES[gender][dtl.template.type][Util.nextInt(12)]);
                RewardService.gI().initBaseOptionClothes(skh.template.id, skh.template.type, skh.itemOptions);
                int skhId = ItemService.gI().randomSKHId((byte) gender);
                ItemService.gI().AddOptionSKH(skh, skhId);
                Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                player.inventory.gold -= 500_000_000;
                Service.getInstance().sendMoney(player);
                InventoryService.gI().subQuantityItemsBag(player, dtl, 1);
                InventoryService.gI().addItemBag(player, skh, 1);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        }

        if (player.combineNew.itemsCombine.size() == 3) {
            Item dtl = null;
            Item dhd = null;
            Item da = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.isDHD()) {
                    dhd = it;
                }
                if (it.isDTL()) {
                    dtl = it;
                }
                if (it.template.id == 457) {
                    da = it;
                }
            }
            if (dtl != null && dhd != null && da != null && da.quantity >= 50) {
                int gender = dtl.template.gender;
                if (gender == 3) {
                    gender = player.gender;
                }
                Item skh = ItemService.gI().createNewItem(
                        (short) ConstItem.LIST_ITEM_CLOTHES[gender][dtl.template.type][Util.nextInt(12)]);
                RewardService.gI().initBaseOptionClothes(skh.template.id, skh.template.type, skh.itemOptions);
                int skhId = ItemService.gI().randomSKHId((byte) gender);
                ItemService.gI().AddOptionSKH(skh, skhId);
                Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                InventoryService.gI().subQuantityItemsBag(player, dtl, 1);
                InventoryService.gI().subQuantityItemsBag(player, dhd, 1);
                InventoryService.gI().subQuantityItemsBag(player, da, 50);
                InventoryService.gI().addItemBag(player, skh, 1);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        }
    }

    public void NangcapSKHChoose(Player player, int select) {
        if (player.combineNew.itemsCombine.size() <= 3) {
            Item dtl = null;
            Item dhd = null;
            Item da = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.isDHD()) {
                    dhd = it;
                }
                if (it.isDTL()) {
                    dtl = it;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (it.template.id == 1980) {
                        da = it;
                    }
                }
            }
            if (player.combineNew.itemsCombine.size() == 3 && da == null) {
                return;
            }
            if (dtl != null && dhd != null) {
                int gender = dtl.template.gender;
                if (gender == 3) {
                    gender = player.gender;
                }
                Item skh = ItemService.gI().createNewItem(
                        (short) ConstItem.LIST_ITEM_CLOTHES[gender][dtl.template.type][Util.nextInt(12)]);
                RewardService.gI().initBaseOptionClothes(skh.template.id, skh.template.type, skh.itemOptions);
                int opt[][] = { { 127, 128, 129, 210, 211 }, { 130, 131, 132, 213, 214 }, { 133, 134, 135, 216, 217 } };
                int skhId = opt[gender][select];
                ItemService.gI().AddOptionSKH(skh, skhId);
                Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                InventoryService.gI().subQuantityItemsBag(player, dtl, 1);
                InventoryService.gI().subQuantityItemsBag(player, dhd, 1);
                InventoryService.gI().subQuantityItemsBag(player, da, 1);
                InventoryService.gI().addItemBag(player, skh, 1);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        }
    }

    public void phaLeHoaTrangBiX(Player player, int solan) {
        if (player.combineNew.itemsCombine.size() == 2) {
            if (InventoryService.gI().getQuantity(player, 1465) >= solan) {
                for (int i = 0; i < solan; i++) { // số lần pha lê hóa
                    if (phaLeHoaTrangBi(player)) {
                        Service.getInstance().sendThongBao(player,
                                "Chúc mừng bạn đã pha lê hóa thành công,tổng số lần nâng cấp là: " + i);
                        break;
                    }
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ số lượng vé để pha lê !", "Đóng");
            }
        } else if (player.combineNew.itemsCombine.size() == 1) {
            for (int i = 0; i < solan; i++) { // số lần pha lê hóa
                if (phaLeHoaTrangBi(player)) {
                    Service.getInstance().sendThongBao(player,
                            "Chúc mừng bạn đã pha lê hóa thành công,tổng số lần nâng cấp là: " + i);
                    break;
                }
            }
        }
    }

    public void AnhsangXLan(Player player, int solan) {
        for (int i = 0; i < solan; i++) { // số lần pha lê hóa
            if (NangCapAnhSang(player)) {
                Service.getInstance().sendThongBao(player,
                        "Chúc mừng bạn đã Ánh sáng thành công lần : " + i);
                break;
            }
        }
    }

    private void nangCapBongTai(Player player) {

        if (player.combineNew.itemsCombine.size() == 2 || player.combineNew.itemsCombine.size() == 3) {
            Item mv = null;
            Item mh = null;
            Item bua = null;
            for (Item it : player.combineNew.itemsCombine) {
                switch (it.getId()) {
                    case 933:
                        mv = it;
                        break;
                    case 934:
                        mh = it;
                        break;
                    case 1446:
                        bua = it;
                        break;
                    default:
                        break;
                }
            }

            if (mv != null && mh != null && mv.quantity >= 999 && mh.quantity >= 20) {
                int param = Util.nextInt(1, 15);
                if (bua != null) {
                    param *= 2;
                    InventoryService.gI().subQuantityItemsBag(player, bua, 1);
                }
                Item bt = ItemService.gI().createNewItem((short) 921);
                bt.itemOptions.clear();
                int rdUp = Util.nextInt(0, 2);
                switch (rdUp) {
                    case 0:
                        bt.itemOptions.add(new ItemOption(50, param));
                        break;
                    case 1:
                        bt.itemOptions.add(new ItemOption(77, param));
                        break;
                    case 2:
                        bt.itemOptions.add(new ItemOption(103, param));
                        break;
                }
                InventoryService.gI().addItemBag(player, bt, 1);
                InventoryService.gI().subQuantityItemsBag(player, mh, 20);
                InventoryService.gI().subQuantityItemsBag(player, mv, 999);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        }

        // if (player.combineNew.itemsCombine.size() == 3) {
        // Item prt = null;
        // Item mv = null;
        // Item mh = null;
        // for (Item it : player.combineNew.itemsCombine) {
        // if (it.template.id == 934) {
        // mh = it;
        // }
        // if (it.template.id == 933) {
        // mv = it;
        // }
        // if (it.template.id == 454) {
        // prt = it;
        // }
        // }
        // if (mv != null && mv.quantity >= 999
        // && mh != null && mv.quantity >= 20
        // && prt != null) {
        //
        // Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
        // prt.template = ItemService.gI().getTemplate(921);
        // prt.itemOptions.clear();
        // int rdUp = Util.nextInt(0, 2);
        // int param = Util.nextInt(1, 30);
        // switch (rdUp) {
        // case 0:
        // prt.itemOptions.add(new ItemOption(50, param));
        // break;
        // case 1:
        // prt.itemOptions.add(new ItemOption(77, param));
        // break;
        // case 2:
        // prt.itemOptions.add(new ItemOption(103, param));
        // break;
        // }
        // prt.itemOptions.add(new ItemOption(72, 2));
        // InventoryService.gI().subQuantityItemsBag(player, mh, 500);
        // InventoryService.gI().subQuantityItemsBag(player, mv, 1000);
        // InventoryService.gI().sendItemBags(player);
        // reOpenItemCombine(player);
        // }
        // }
    }

    short getLv(Item it) {
        switch (it.template.id) {
            case 454:
                return 1;
            case 921:
                return 2;
            case 1995:
                return 3;
        }
        return 0;
    }

    private short getidbtsaukhilencap(Item it) {
        switch (it.template.id) {
            case 454:
                return 921;
            case 921:
                return 1995;
            case 1995:
                return 1994;
        }
        return 0;
    }

    private void moChiSoBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item bongTai = null;
            Item mv = null;
            Item mh = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 934) {
                    mh = it;
                }
                if (it.template.id == 933) {
                    mv = it;
                }
                if (it.template.id == 921) {
                    bongTai = it;
                }
            }
            if (mv != null && mv.quantity >= 1000
                    && mh != null && mh.quantity >= 500
                    && bongTai != null) {
                if (Util.isTrue(80, 100)) {
                    bongTai.itemOptions.clear();
                    bongTai.itemOptions.add(new ItemOption(72, 2));

                    int rdUp = Util.nextInt(0, 5);
                    int param = Util.nextInt(1, 15);
                    switch (rdUp) {
                        case 0:
                            bongTai.itemOptions.add(new ItemOption(50, param));
                            break;
                        case 1:
                            bongTai.itemOptions.add(new ItemOption(77, param));
                            break;
                        case 2:
                            bongTai.itemOptions.add(new ItemOption(103, param));
                            break;
                        case 3:
                            bongTai.itemOptions.add(new ItemOption(14, param));
                            break;
                        case 4:
                            bongTai.itemOptions.add(new ItemOption(5, param));
                            break;
                        case 5:
                            bongTai.itemOptions.add(new ItemOption(47, param));
                            break;
                    }
                    Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                } else {
                    Service.getInstance().sendEffectCombine(player, COMBINE_FAIL);
                }
                InventoryService.gI().subQuantityItemsBag(player, mh, 500);
                InventoryService.gI().subQuantityItemsBag(player, mv, 1000);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        }

        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int ruby = player.combineNew.rubyCombine;
            if (player.inventory.ruby < ruby) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhHon = null;
            Item daXanhLam = null;
            for (Item item : player.combineNew.itemsCombine) {
                switch (item.template.id) {
                    case 921:
                        bongTai = item;
                        break;
                    case 934:
                        manhHon = item;
                        break;
                    case 935:
                        daXanhLam = item;
                        break;
                    default:
                        break;
                }
            }
            if (bongTai != null && daXanhLam != null && manhHon.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.ruby -= ruby;
                InventoryService.gI().subQuantityItemsBag(player, manhHon, 99);
                InventoryService.gI().subQuantityItemsBag(player, daXanhLam, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.itemOptions.clear();
                    if (bongTai.template.id == 921) {
                        bongTai.itemOptions.add(new ItemOption(72, 2));
                    }
                    int rdUp = Util.nextInt(0, 5);
                    int param = Util.nextInt(1, 15);
                    switch (rdUp) {
                        case 0:
                            bongTai.itemOptions.add(new ItemOption(50, param));
                            break;
                        case 1:
                            bongTai.itemOptions.add(new ItemOption(77, param));
                            break;
                        case 2:
                            bongTai.itemOptions.add(new ItemOption(103, param));
                            break;
                        case 3:
                            bongTai.itemOptions.add(new ItemOption(14, param));
                            break;
                        case 4:
                            bongTai.itemOptions.add(new ItemOption(5, param));
                            break;
                        case 5:
                            bongTai.itemOptions.add(new ItemOption(47, param));
                            break;
                    }
                    Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                } else {
                    Service.getInstance().sendEffectCombine(player, COMBINE_FAIL);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void dapBongTaiCap3(Player player) {
        if (player.combineNew.itemsCombine.size() == 4) {
            Item bt = null;
            Item mh = null;
            Item mv = null;
            Item giay = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.TempIdIs(933)) {
                    mv = it;
                }
                if (it.TempIdIs(934)) {
                    mh = it;
                }
                if (it.TempIdIs(921)) {
                    bt = it;
                }
                if (it.TempIdIs(1032)) {
                    giay = it;
                }
            }
            if (mv != null && mv.quantity >= 50
                    && mh != null && mh.quantity >= 10
                    && giay != null && bt != null) {
                if (Util.isTrue(80, 100)) {
                    bt.itemOptions.clear();
                    bt.itemOptions.add(new ItemOption(72, 2));

                    int rdUp = Util.nextInt(0, 5);
                    int param = Util.nextInt(1, 10);
                    switch (rdUp) {
                        case 0:
                            bt.itemOptions.add(new ItemOption(50, param));
                            break;
                        case 1:
                            bt.itemOptions.add(new ItemOption(77, param));
                            break;
                        case 2:
                            bt.itemOptions.add(new ItemOption(103, param));
                            break;
                        case 3:
                            bt.itemOptions.add(new ItemOption(14, param));
                            break;
                        case 4:
                            bt.itemOptions.add(new ItemOption(5, param));
                            break;
                        case 5:
                            bt.itemOptions.add(new ItemOption(47, param));
                            break;
                    }
                    Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                } else {
                    Service.getInstance().sendEffectCombine(player, COMBINE_FAIL);
                }
                InventoryService.gI().subQuantityItemsBag(player, mh, 10);
                InventoryService.gI().subQuantityItemsBag(player, mv, 50);
                InventoryService.gI().subQuantityItemsBag(player, giay, 1);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        }
    }

    private int getTine(Item tl) {// tile thanh cong
        int tile = 10;
        if (tl != null) {
            if (tl.template.id >= 555 && tl.template.id <= 567) {
                tile = 30;
            }
        }
        return tile;
    }

    private void epSaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item trangBi = null;
            Item daPhaLe = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiEpSaoPhale(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
            }
            int star = 0; // sao pha lê đã ép
            int starEmpty = 0; // lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                ItemOption optionStar = null;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                if (star < starEmpty) {
                    player.inventory.subGem(gem);
                    int optionId = getOptionDaPhaLe(daPhaLe);
                    int param = getParamDaPhaLe(daPhaLe);
                    ItemOption option = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new ItemOption(optionId, param));
                    }
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        trangBi.itemOptions.add(new ItemOption(102, 1));
                    }

                    if (trangBi.haveListOption(34, 36)) {
                        ItemOption optAn = trangBi.getOptionAn();
                        trangBi.itemOptions.removeIf(io -> io.optionTemplate.id == 34 || io.optionTemplate.id == 35
                                || io.optionTemplate.id == 36);
                        trangBi.itemOptions.add(optAn);

                    }

                    InventoryService.gI().subQuantityItemsBag(player, daPhaLe, 1);
                    Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private boolean phaLeHoaTrangBi(Player player) {
        boolean flag = false;

        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < gold) {
                return false;
            }
            if (player.inventory.gem < gem) {
                return false;
            }
            Item item = null;
            Item thex10 = null;
            if (player.combineNew.itemsCombine.size() == 2) {
                for (Item it : player.combineNew.itemsCombine) {
                    if (isTrangBiPhaLeHoa(it)) {
                        item = it;
                    }
                    if (it.getId() == 1465) {
                        thex10 = it;
                    }
                }
            } else {
                item = player.combineNew.itemsCombine.get(0);
            }
            int size = player.combineNew.itemsCombine.size();
            if (size == 1 && isTrangBiPhaLeHoa(item) || size == 2 && isTrangBiPhaLeHoa(item) && thex10 != null) {
                int star = 0;
                ItemOption optionStar = null;
                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.subGem(gem);
                    int ratio = 1;
                    if (optionStar != null && optionStar.param > 6) {
                        ratio = 1;
                    }
                    player.combineNew.ratioCombine = getRatioPhaLeHoaBip(star);
                    if (thex10 != null) {
                        InventoryService.gI().subQuantityItemsBag(player, thex10, 1);
                        player.combineNew.ratioCombine *= 10;
                    }
                    if (Util.isTrue(player.combineNew.ratioCombine, 600 * ratio)) {
                        if (optionStar == null) {
                            item.itemOptions.add(new ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                        if (optionStar != null && optionStar.param >= 7) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa " + "thành công "
                                    + item.template.name + " lên " + optionStar.param + " sao pha lê");
                            ServerLog.logCombine(player.name, item.template.name, optionStar.param);
                        }
                        flag = true;
                    } else {
                        Service.getInstance().sendEffectCombine(player, COMBINE_FAIL);
                    }
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
        return flag;
    }

    public boolean isItemPhaLeHoa(Item it) {
        return it.template.id == 2053;
    }

    public boolean isItemCaiTrang(Item it) {
        return it.template.type == 5;
    }

    private void nhapNgocRong(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem()) {
                    if ((item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                        Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
                        InventoryService.gI().addItemBag(player, nr, 0);
                        InventoryService.gI().subQuantityItemsBag(player, item, 7);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);
                        sendEffectCombineDB(player, item.template.iconID);
                        return;
                    }
                    // if (player.inventory.gold >= 500000000) {
                    // if (item.template.id == 14 && item.quantity >= 7) {
                    // Item nr = ItemService.gI().createNewItem((short) (1015));
                    // InventoryService.gI().addItemBag(player, nr, 0);
                    // sendEffectCombineDB(player, (short) 9650);
                    // } else if (item.template.id == 926 && item.quantity >= 7) {
                    // Item nr = ItemService.gI().createNewItem((short) (925));
                    // nr.itemOptions.add(new ItemOption(93, 70));
                    // InventoryService.gI().addItemBag(player, nr, 0);
                    // sendEffectCombineDB(player, item.template.iconID);
                    // }
                    // InventoryService.gI().subQuantityItemsBag(player, item, 7);
                    // player.inventory.gold -= 500000000;
                    // Service.getInstance().sendMoney(player);
                    // InventoryService.gI().sendItemBags(player);
                    // reOpenItemCombine(player);
                    // } else {
                    // Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " +
                    // Util.numberToMoney(500000000 - player.inventory.gold) + " vàng");
                    // }
                }
            }
        }
    }

    public void nangCapVatPham(Player player, int select) {
        if (player.combineNew.itemsCombine.size() == 2) {
            if (isCoupleItemNangCap(player.combineNew.itemsCombine.get(0), player.combineNew.itemsCombine.get(1))) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                long gold = 5_000_000_000L;
                if (player.inventory.gold < gold) {
                    return;
                }
                Item trangBi = null;
                Item daNangCap = null;
                Item veBaoVe = InventoryService.gI().findBuaBaoVeNangCap(player);
                if (player.combineNew.itemsCombine.get(0).template.type < 5) {
                    trangBi = player.combineNew.itemsCombine.get(0);
                    daNangCap = player.combineNew.itemsCombine.get(1);
                } else {
                    trangBi = player.combineNew.itemsCombine.get(1);
                    daNangCap = player.combineNew.itemsCombine.get(0);
                }
                if (daNangCap.quantity < countDaNangCap) {
                    return;
                }
                int level = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    ItemOption option = null;
                    ItemOption option2 = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == 47 || io.optionTemplate.id == 6 || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7 || io.optionTemplate.id == 14 || io.optionTemplate.id == 22
                                || io.optionTemplate.id == 23) {
                            option = io;
                        } else if (io.optionTemplate.id == 27 || io.optionTemplate.id == 28) {
                            option2 = io;
                        }
                    }
                    float ratioCombine;
                    ratioCombine = player.combineNew.ratioCombine;
                    if (Util.isTrue(ratioCombine, 100)) {
                        option.param += (option.param * 10 / 100);
                        if (option2 != null) {
                            option2.param += (option2.param * 10 / 100);
                        }
                        if (optionLevel == null) {
                            trangBi.itemOptions.add(new ItemOption(72, 1));
                        } else {
                            optionLevel.param++;
                        }
                        if (optionLevel != null && optionLevel.param >= 5) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp " + "thành công "
                                    + trangBi.template.name + " lên +" + optionLevel.param);
                        }
                        Service.getInstance().sendEffectCombine(player, COMBINE_SUCCESS);
                    } else {
                        if (veBaoVe != null && select == 1) {
                            InventoryService.gI().subQuantityItemsBag(player, veBaoVe, 1);
                        } else {
                            if (level == 2 || level == 4 || level == 6) {
                                option.param -= (option.param * 10 / 100);
                                if (option2 != null) {
                                    option2.param -= (option2.param * 10 / 100);
                                }
                                optionLevel.param--;
                            }
                        }
                        Service.getInstance().sendEffectCombine(player, COMBINE_FAIL);

                    }
                    InventoryService.gI().subQuantityItemsBag(player, daNangCap, player.combineNew.countDaNangCap);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);

                }
            }
        }
    }

    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player
     */
    private void reOpenItemCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combineNew.itemsCombine.size());
            for (Item it : player.combineNew.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    private void sendEffectCombineDB(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private int getCountDaBaoVe(int level) {
        return level + 1;
    }

    // --------------------------------------------------------------------------Ratio,
    // cost combine
    private int getThoiVangPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 10;
            case 1:
                return 20;
            case 2:
                return 50;
            case 3:
                return 100;
            case 4:
                return 200;
            case 5:
                return 400;
            case 6:
                return 800;
            case 7:
                return 1500;
            default:
                return 0;
        }
    }

    private int getGemEpSao(int star) {
        switch (star) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 25;
            case 5:
                return 50;
            case 6:
                return 100;
        }
        return 0;
    }

    private int getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 100;
            case 1:
                return 100;
            case 2:
                return 100;
            case 3:
                return 100;
            case 4:
                return 100;
            case 5:
                return 100;
            case 6:
                return 100;
            case 7:
                return 100;
        }
        return 0;
    }

    private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 3;
            case 1:
                return 7;
            case 2:
                return 11;
            case 3:
                return 17;
            case 4:
                return 23;
            case 5:
                return 35;
            case 6:
                return 50;
            case 7:
                return 100;
            case 8:
                return 100;
        }
        return 0;
    }

    private int lvbt(Item bongtai) {
        switch (bongtai.template.id) {
            case 454:
                return 1;
            case 921:
                return 2;
            case 1995:
                return 3;
        }
        return 0;

    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 10000;
            case 1:
                return 70000;
            case 2:
                return 300000;
            case 3:
                return 1500000;
            case 4:
                return 7000000;
            case 5:
                return 23000000;
            case 6:
                return 100000000;
        }
        return 0;
    }

    // --------------------------------------------------------------------------check
    public boolean isAngelClothes(int id) {
        if (id >= 1048 && id <= 1062) {
            return true;
        }
        return false;
    }

    public boolean isDestroyClothes(int id) {
        if (id >= 650 && id <= 662) {
            return true;
        }
        return false;
    }

    private int getGoldnangbt(int lvbt) {
        return GOLD_BONG_TAI2;
    }

    private int getRubyUpgradeBT(int lvbt) {
        return RUBY_BONG_TAI2;
    }

    private boolean checkbongtai(Item item) {
        if (item.template.id == 454 || item.template.id == 921 || item.template.id == 1995) {
            return true;
        }
        return false;
    }

    int SoluonThoiVang(Item it) {
        if (it.template.id == 454) {
            return 200;
        } else if (it.template.id == 921) {
            return 500;
        } else if (it.template.id == 1995) {
            return 1000;
        }
        return 0;
    }

    private boolean isCoupleItemNangCap(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type < 5) {
                trangBi = item1;
            } else if (item1.template.type == 14) {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type < 5) {
                trangBi = item2;
            } else if (item2.template.type == 14) {
                daNangCap = item2;
            }
        }
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDaPhaLe(Item item) {
        if (item != null && item.isNotNullItem()) {
            return item.template.type == 30 || (item.template.id >= 14 && item.template.id <= 20);
        }
        return false;
    }

    private boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            int type = item.template.type;
            if (type <= 5 || type == 32 || type == 11 || type == 23 || type == 24 || type == 72 || type == 21
                    || type == 28) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isTrangBiEpSaoPhale(Item item) {
        if (item != null && item.isNotNullItem()) {
            int type = item.template.type;
            if (type <= 5 || type == 32 || type == 11 || type == 23 || type == 24 || type == 72 || type == 21
                    || type == 28) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int getParamDaTinhThach(Item tinhThachItem) {
        switch (tinhThachItem.template.id) {
            case 2014:
                return 3;
            case 2015:
                return 3;
            case 2016:
                return 3;
            case 2017:
                return 2;
            case 2018:
                return 2;
        }
        return 0;
    }

    private int getOptionTinhThach(Item tinhThachItem) {
        switch (tinhThachItem.template.id) {
            case 2014:
                return 77;
            case 2015:
                return 103;
            case 2016:
                return 108;
            case 2017:
                return 5;
            case 2018:
                return 50;
        }
        return -1;
    }

    private int getParamDaPhaLe(Item daPhaLe) {
        switch (daPhaLe.template.id) {
            case 20:
                return 5; // +5%hp
            case 19:
                return 5; // +5%ki
            case 18:
                return 5; // +5%hp/30s
            case 17:
                return 5; // +5%ki/30s
            case 16:
                return 3; // +3%sđ
            case 15:
                return 2; // +2%giáp
            case 14:
                return 2; // +2%né đòn
            case 441:
                return 5;
            case 442:
                return 5;
            case 443:
                return 10;
            case 444:
                return 10;
            case 445:
                return 10;
            case 446:
                return 10;
            case 447:
                return 5;
            default:
                return -1;
        }
    }

    private int getOptionDaPhaLe(Item daPhaLe) {
        switch (daPhaLe.template.id) {
            case 20:
                return 77;
            case 19:
                return 103;
            case 18:
                return 80;
            case 17:
                return 81;
            case 16:
                return 50;
            case 15:
                return 94;
            case 14:
                return 108;
            case 441:
                return 95;
            case 442:
                return 96;
            case 443:
                return 97;
            case 444:
                return 98;
            case 445:
                return 99;
            case 446:
                return 100;
            case 447:
                return 101;
            default:
                return -1;
        }
    }

    /**
     * Trả về id item c0
     *
     * @param gender
     * @param type
     * @return
     */
    // --------------------------------------------------------------------------Text
    // tab combine
    public String getTextTopTabCombine(int type) {
        switch (type) {
            case MO_CHI_SO_DO_KICH_HOAT:
                return "Ta sẽ giúp cậu mở chỉ số\n kích hoạt ẩn của đồ";
            case AN_TRANG_BI:
                return "Ấn trang bị sẽ giúp\ntrang bị của ngươi gia tăng\n đáng kể chỉ số";
            case TRAO_DOI_DO_KICH_HOAT:
                return "Ta sẽ trao đổi với\n một món đồ Thiên sứ\n lấy một món trang bị kích hoạt";
            case CHUYEN_HOA_MA_THUAT:
                return "Ta sẽ chuyển hóa giúp cậu\n đồ thần linh thành đá ma thuật";
            case PHAN_RA_THIEN_SU:
                return "Ta sẽ thực hiện\n phân tách trang bị Thiên sứ";
            case KHAM_TINH_THACH:
                return "Ta sẽ giúp cậu\n khảm tinh thạch";
            case PHA_LE_HOA_LINH_THU:
                return "Ta sẽ giúp cậu\npha lê hóa trứng linh thú";
            case AP_TRUNG_LINH_THU:
                return "Tôi sẽ giúp cậu\nấp quả trứng linh thú";
            case NO_TRUNG_LINH_THU:
                return "Trứng của cậu đã tới lúc nở rồi\n Tôi sẽ giúp cậu";
            case NANG_CAP_BONG_TAI_BONG_TOI_2:
                return "Ta sẽ Bóng tối hóa\n Bông tai của ngươi";
            case GHEP_SACH:
                return "Ta sẽ kêu gọi Thiên\n trả lời ngươi";
            case GHEP_MANH_GOKU:
            case GHEP_MANH_CUMBER:
            case GHEP_MANH_CU_CAI_TRANG:
            case GHEP_MANH_MI:
            case GHEP_MANH_PIKKON:
            case GHEP_MANH_BONG_BANG:
                return "Ta sẽ hợp nhất\n các mảnh cải trang bị phân rã";
            case NANG_CAP_DO_THIEN_SU:
                return "Ta sẽ giúp cậu\n chế tạo trang bị\n Thiên sứ";
            case NANG_CAP_DO_HUY_DIET:
                return "Ta sẽ hiến tế\n trang bị cấp HỦY DIỆT";
            case XOA_DONG_ANH_SANG_NHANH:
            case XOA_DONG_ANH_SANG_THUONG:
                return "Ta sẽ loại bỏ Ánh Sáng\n khỏi trang bị của ngươi";
            case NANG_CAP_ANH_SANG:
                return "Ta sẽ dùng Ánh sáng\n chiếu vào trang bị của ngươi ";
            case NANG_CAP_HUY_DIET:
                return "Ta sẽ \n hiến tế trang bị Hủy Diệt";
            case DAP_BONG_TAI_CAP_3:
                return "Ta sẽ làm phép\n Bông tai của ngươi";
            case NANG_CAP_SKH:
                return "Ta sẽ \n hiến tế\n Trang bị kích hoạt";
            case EP_SAO_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case PHA_LE_HOA_TRANG_BI:
            case PHA_LE_HOA_TRANG_BI_X10:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case NHAP_NGOC_RONG:
                return "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao";
            case NANG_CAP_VAT_PHAM:
                return "Ta sẽ phù phép cho trang bị của ngươi trở lên mạnh mẽ";
            case NANG_CAP_BONG_TAI:
                return "Ta sẽ hoàn đổi\n Bông Tai Black cho ngươi ";
            case NANG_CAP_BONG_TAI_CAP3:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 3";
            case MO_CHI_SO_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 2 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            default:
                return "";
        }
    }

    public String getTextInfoTabCombine(int type) {
        switch (type) {
            case MO_CHI_SO_DO_KICH_HOAT:
                return "Hãy chọn 1 trang bị có chỉ số kích hoạt ẩn,\n 2 đá ma thuật\n Sau đó nhấn 'Mở'";
            case AN_TRANG_BI:
                return "Vào hành trang\n Chọn 1 trang bị chưa ấn,\n 1 viên ngọc rồng 1 sao\n và 2 đá ma thuật\n Sau đó nhấn 'Ấn trang bị'";
            case TRAO_DOI_DO_KICH_HOAT:
                return "Hãy chọn 1 trang bị thiên sứ tương ứng\n có 2 dòng chỉ số thưởng trở lên\n Sau đó nhấn 'Đổi'";
            case CHUYEN_HOA_MA_THUAT:
                return "Vào hành trang\n Chọn 1 món đồ thần linh bất kỳ\n Sau đó nhấn 'Chuyển hóa'";
            case PHAN_RA_THIEN_SU:
                return "Chọn 1 trang bị Thiên sứ\n Sau đó chọn 'Phân rã'";
            case KHAM_TINH_THACH:
                return "Chọn 1 linh thú\n và 1 loại Tinh thạch\n Sau đó chọn 'Khảm tinh thạch'";
            case PHA_LE_HOA_LINH_THU:
                return "Chọn 1 linh thú\n Tôi sẽ giúp cậu pha lê hóa chúng !";
            case AP_TRUNG_LINH_THU:
                return "Chọn 1 quả trứng linh thú\nvà 200 hồn linh thú\n Tôi sẽ giúp cậu ấp trứng !";
            case NO_TRUNG_LINH_THU:
                return "Chọn 200 hồn linh thú\nvà 5 đá ngũ sắc\n Sau đó chọn 'Nở trứng'";
            case NANG_CAP_BONG_TAI_BONG_TOI_2:
                return "Hãy bỏ vào\n"
                        + "X1 Bông Tai\n"
                        + "X9999 Mảnh vỡ\n"
                        + "X1 Xu Bông tai Cấp 2 \n"
                        + "Sau đó chọn 'Nâng cấp'";
            case GHEP_SACH:
                return "Hãy bỏ vào\n"
                        + "X50 mảnh Kinh\n"
                        + "Và có đủ 500 Triệu\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case GHEP_MANH_BONG_BANG:
                return "Hãy bỏ vào\n"
                        + "X1000 Mảnh Bông Băng\n"
                        + "X10 Thỏi vàng\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case GHEP_MANH_CU_CAI_TRANG:
                return "Hãy bỏ vào\n"
                        + "X1000 Mảnh Củ Cải Trắng\n"
                        + "X10 Thỏi vàng\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case GHEP_MANH_MI:
                return "Hãy bỏ vào\n"
                        + "X1000 Mảnh Mị Đen\n"
                        + "X10 Thỏi vàng\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case GHEP_MANH_PIKKON:
                return "Hãy bỏ vào\n"
                        + "X1000 Mảnh PikKon\n"
                        + "X10 Thỏi vàng\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case GHEP_MANH_CUMBER:
                return "Hãy bỏ vào\n"
                        + "X1000 Mảnh Cumber\n"
                        + "X10 Thỏi vàng\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case GHEP_MANH_GOKU:
                return "Hãy bỏ vào\n"
                        + "X1000 Mảnh Goku SSJ\n"
                        + "X10 Thỏi vàng\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_DO_THIEN_SU:
                return "(Yêu cầu cần đeo đủ set hủy diệt)\n"
                        + "Cần 1 công thức\n"
                        + "Mảnh trang bị tương ứng\n"
                        + "1 đá nâng cấp (tùy chọn)\n"
                        + "1 đá may mắn (tùy chọn)";
            case NANG_CAP_DO_HUY_DIET:
                return "Hãy bỏ vào :\n"
                        + "X3 Trang bị Thần Linh\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case XOA_DONG_ANH_SANG_NHANH:
                return "Vào hành trang"
                        + "\n Chọn :"
                        + "\n+ X1 Vật phẩm đã Ánh Sáng"
                        + "\n+ X20 Đá pha lê Ánh Sáng\n"
                        + " Chuẩn bị 10 tỉ vàng \n"
                        + "Sau đó chọn 'Nâng cấp'";
            case XOA_DONG_ANH_SANG_THUONG:
                return "Vào hành trang"
                        + "\n Chọn :"
                        + "\n + X1 Vật phẩm đã Ánh Sáng"
                        + "\n + X200 Đá pha lê Ánh Sáng"
                        + "\n Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_ANH_SANG:
                return "Vào hành trang \n"
                        + "Chọn :"
                        + "\n+ 1 trang bị có thể Ánh Sáng\n"
                        + "(Cải trang, Pet , Đeo lưng)"
                        + "\n+ Số lượng Đá pha lê Ánh sáng cần\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_HUY_DIET:
                return "Vào hành trang\n"
                        + "Chọn\n"
                        + "+ X3 trang bị Thần Linh\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_SKH:
                return "Vào hành trang\n "
                        + "Chọn :\n"
                        + "+ X1 Trang bị Hủy diệt\n"
                        + "Sau đó chọn 'Nâng Cấp' ";
            case DAP_BONG_TAI_CAP_3:
                return "Vào hành trang"
                        + "\nChọn :\n"
                        + "+ Bông tai Porata cấp 2"
                        + "\n+ X50 Mảnh vỡ bông tai"
                        + "\n+ X10 Mảnh hồn bông tai"
                        + "\n+ X1 Giấy tạo tác"
                        + "\nSau đó chọn 'Nâng cấp'";
            case EP_SAO_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) có ô đặt sao pha lê\nChọn loại sao pha lê\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nvà số lượng Ngôi Sao cần thiết \nSau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI_X10:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'\n Khi nâng cấp thành công hoặc đủ 5 lần thì sẽ dừng lại";
            case NHAP_NGOC_RONG:
                return "Vào hành trang\nChọn 7 viên ngọc cùng sao\nSau đó chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI:
                return "Hãy bỏ vào :\n"
                        + "X999 Mảnh vỡ bóng tối\n"
                        + "X20 Mảnh hồn\n"
                        + "(X1 Bùa X2 nếu có)\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI_CAP3:
                return "Vào hành trang"
                        + "\nChọn bông tai Porata cấp 2"
                        + "\nChọn X10 Aether Stone Nham"
                        + "\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI:
                return "Vào hành trang \n Chọn :\n X1 Bông tai Porata cấp 2\n"
                        + "+ X1000 Mảnh vỡ bông tai\n"
                        + "+ X500 Mảnh hồn bông tai\n"
                        + "Sau đó chọn 'Nâng cấp'";
            default:
                return "";
        }
    }

    private void sendEffectSuccessCombineDoTS(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(7);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Npc baHatMit;
    private final Npc bulmatl;
    private final Npc whis;
    private final Npc tosu;
    private final Npc drief;
    private static CombineServiceNew i;

    public CombineServiceNew() {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.whis = NpcManager.getNpc(ConstNpc.WHIS);
        this.bulmatl = NpcManager.getNpc(ConstNpc.BUNMA_TL);
        this.tosu = NpcManager.getNpc(ConstNpc.TO_SU_KAIO);
        this.drief = NpcManager.getNpc(ConstNpc.DR_DRIEF);
    }

    public static CombineServiceNew gI() {
        if (i == null) {
            i = new CombineServiceNew();
        }
        return i;
    }

    /**
     * Mở tab đập đồ
     *
     * @param player
     * @param type   kiểu đập đồ
     */
    public void openTabCombine(Player player, int type, Npc npc) {
        player.combineNew.setTypeCombine(type);
        player.combineNew.setNpc(npc);
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            msg.writer().writeShort(npc.tempId);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float getRationangbt(int lvbt) { // tile dap do chi hat mit
        switch (lvbt) {
            case 1:
                return 40f;

        }
        return 0;
    }

    int getQuantityFood(int Level) {
        switch (Level) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 4;
            case 3:
                return 8;
            case 4:
                return 16;
            case 5:
                return 32;
            case 6:
                return 64;
            case 7:
                return 128;
            default:
                return 9999;
        }
    }

    int getParamOptionLinhThu(int level) {
        switch (level) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 5;
            case 4:
                return 7;
            case 5:
                return 9;
            case 6:
                return 11;
            default:
                return -1;
        }
    }

    private float getRatioPhaLeHoaBip(int star) {
        switch (star) {
            case 0:
                return 50f;
            case 1:
                return 30f;
            case 2:
                return 20f;
            case 3:
                return 10f;
            case 4:
                return 5f;
            case 5:
                return 2f;
            case 6:
                return 0.9f;
            case 7:
                return 0.5f;
        }
        return 0;
    }

    int getParamAnhSang(int level) {
        switch (level) {
            case 0:
                return 1;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 5;
            case 5:
                return 7;
            case 6:
                return 9;
            case 7:
                return 12;
            default:
                return 0;
        }
    }

    int getRequireStoneAnhSang(int level) {
        switch (level) {
            case 0:
                return 80;
            case 1:
                return 50;
            case 2:
                return 40;
            case 3:
                return 30;
            case 4:
                return 10;
            case 5:
                return 6;
            case 6:
                return 2;
            case 7:
                return 1;
        }
        return 9999;
    }

    private float getRatioBongToiTrangBi(int star) {
        switch (star) {
            case 0:
                return 100f;
            case 1:
                return 50f;
            case 2:
                return 30f;
            case 3:
                return 20f;
            case 4:
                return 15f;
            case 5:
                return 8f;
            case 6:
                return 3f;
            case 7:
                return 1f;
        }
        return 0;
    }

    private float getRatioPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 80f;
            case 1:
                return 50f;
            case 2:
                return 40f;
            case 3:
                return 30f;
            case 4:
                return 10f;
            case 5:
                return 6f;
            case 6:
                return 2f;
            case 7:
                return 0.8f;
        }
        return 0;
    }

    private int getGoldPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 5000000;
            case 1:
                return 10000000;
            case 2:
                return 20000000;
            case 3:
                return 40000000;
            case 4:
                return 60000000;
            case 5:
                return 90000000;
            case 6:
                return 120000000;
            case 7:
                return 200000000;
        }
        return 0;
    }

    private int getGemPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 10;
            case 1:
                return 20;
            case 2:
                return 30;
            case 3:
                return 40;
            case 4:
                return 50;
            case 5:
                return 60;
            case 6:
                return 70;
            case 7:
                return 80;
            case 8:
                return 90;
        }
        return 0;
    }

    public void NANGCAPCHONDOTHIENSU(Player player, int select) {
        if (player.combineNew.itemsCombine.size() == 6) {
            Item ts1 = player.combineNew.itemsCombine.get(0);
            Item ts2 = player.combineNew.itemsCombine.get(1);
            ;
            Item ts3 = player.combineNew.itemsCombine.get(2);
            ;
            Item ts4 = player.combineNew.itemsCombine.get(3);
            Item ts5 = player.combineNew.itemsCombine.get(4);
            Item dats = player.combineNew.itemsCombine.get(5);
            if (ts1.isNotNullItem() && ts1.isDHD()
                    && ts2.isNotNullItem() && ts2.isDHD()
                    && ts3.isNotNullItem() && ts3.isDHD()
                    && ts4.isNotNullItem() && ts4.isDHD()
                    && ts5.isNotNullItem() && ts5.isDHD()
                    && dats != null && dats.getId() == 1419) {
                Item dts = ItemService.gI().createNewItem((short) ConstItem.doSKHVip[select][player.gender][14]);
                RewardService.gI().initBaseOptionClothes(dts);

                InventoryService.gI().subQuantityItemsBag(player, ts1, 1);
                InventoryService.gI().subQuantityItemsBag(player, ts2, 1);
                InventoryService.gI().subQuantityItemsBag(player, ts3, 1);
                InventoryService.gI().subQuantityItemsBag(player, ts4, 1);
                InventoryService.gI().subQuantityItemsBag(player, ts5, 1);
                InventoryService.gI().subQuantityItemsBag(player, dats, 1);

                InventoryService.gI().addItemBag(player, dts, 1);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        }
    }

    public void ghepManhCaiTrang(Player player, int IdManh) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item mgoku = null;
            Item tv = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.getId() == IdManh) {
                    mgoku = it;
                }
                if (it.getId() == 457) {
                    tv = it;
                }
            }
            if (mgoku != null && tv != null && mgoku.quantity >= 1000 && tv.quantity >= 10) {
                Item ct = null;
                switch (IdManh) {
                    case 1426: // goku
                        ct = ItemService.gI().createNewItem((short) 2055);
                        ct.itemOptions.add(new ItemOption(50, 5));
                        ct.itemOptions.add(new ItemOption(77, 5));
                        ct.itemOptions.add(new ItemOption(103, 5));
                        ct.itemOptions.add(new ItemOption(5, Util.nextInt(10, 70)));
                        break;
                    case 1427:
                        ct = ItemService.gI().createNewItem((short) 1275);
                        ct.itemOptions.add(new ItemOption(226, 50));
                        break;
                    // case 1428:
                    // ct = ItemService.gI().createNewItem((short) 1420);
                    // ct.itemOptions.add(new ItemOption(77, Util.nextInt(10, 100)));
                    // ct.itemOptions.add(new ItemOption(103, Util.nextInt(10, 100)));
                    // ct.itemOptions.add(new ItemOption(14, 100));
                    // break;
                    case 1429:
                        ct = ItemService.gI().createNewItem((short) 1432);
                        ct.itemOptions.add(new ItemOption(50, Util.nextInt(10, 25)));
                        ct.itemOptions.add(new ItemOption(77, Util.nextInt(10, 25)));
                        ct.itemOptions.add(new ItemOption(103, Util.nextInt(10, 25)));
                        ct.itemOptions.add(new ItemOption(14, 5));

                        break;
                    // case 1430:
                    // ct = ItemService.gI().createNewItem((short) 1364);
                    // ct.itemOptions.add(new ItemOption(225, Util.nextInt(10, 20)));
                    // break;
                    case 1431:
                        ct = ItemService.gI().createNewItem((short) 1287);
                        ct.itemOptions.add(new ItemOption(50, 20));
                        ct.itemOptions.add(new ItemOption(77, 20));
                        ct.itemOptions.add(new ItemOption(103, 20));
                        ct.itemOptions.add(new ItemOption(106, 1));
                        break;
                }
                InventoryService.gI().subQuantityItemsBag(player, mgoku, 1000);
                InventoryService.gI().subQuantityItemsBag(player, tv, 10);
                InventoryService.gI().addItemBag(player, ct, 1);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        }
    }

    public String getNameType(int type) {
        switch (type) {
            case 0:
                return "Áo";
            case 1:
                return "Quần";
            case 2:
                return "Găng";
            case 3:
                return "Giày";
            case 4:
                return "Nhẫn";
            default:
                return "";
        }
    }

    public int getDaChuyenHoa(int type) {
        switch (type) {
            case 0:
            case 1:
            case 3:
                return 1;
            case 2:
                return 2;
            case 4:
                return 3;
        }
        return 0;
    }

    public boolean isManhTrangBi(Item it) {
        switch (it.template.id) {
            case 1066:
            case 1067:
            case 1068:
            case 1069:
            case 1070:
                return true;
        }
        return false;
    }

    public boolean isCraftingRecipe(int id) {
        switch (id) {
            case 1071:
            case 1072:
            case 1073:
            case 1084:
            case 1085:
            case 1086:
                return true;
        }
        return false;
    }

    public int getRatioCraftingRecipe(int id) {
        switch (id) {
            case 1071:
                return 0;
            case 1072:
                return 0;
            case 1073:
                return 0;
            case 1084:
                return 10;
            case 1085:
                return 10;
            case 1086:
                return 10;
        }
        return 0;
    }

    public boolean isUpgradeStone(int id) {
        switch (id) {
            case 1074:
            case 1075:
            case 1076:
            case 1077:
            case 1078:
                return true;
        }
        return false;
    }

    public int getRatioUpgradeStone(int id) {
        switch (id) {
            case 1074:
                return 10;
            case 1075:
                return 20;
            case 1076:
                return 30;
            case 1077:
                return 40;
            case 1078:
                return 50;
        }
        return 0;
    }

    public boolean isLuckyStone(int id) {
        switch (id) {
            case 1079:
            case 1080:
            case 1081:
            case 1082:
            case 1083:
                return true;
        }
        return false;
    }

    public int getRatioLuckyStone(int id) {
        switch (id) {
            case 1079:
                return 10;
            case 1080:
                return 20;
            case 1081:
                return 30;
            case 1082:
                return 40;
            case 1083:
                return 50;
        }
        return 0;
    }

    public String getNameThieSu(int idItem) {
        switch (idItem) {
            case 1066:
                return "Áo";
            case 1067:
                return "Quần";
            case 1068:
                return "Giày";
            case 1069:
                return "Nhẫn";
            case 1070:
                return "Găng";
            default:
                return "";
        }
    }

    public int getTypeThienSu(int idManh) {
        switch (idManh) {
            case 1066:
                return 0;
            case 1067:
                return 1;
            case 1068:
                return 3;
            case 1069:
                return 4;
            case 1070:
                return 2;
            default:
                return -1;
        }
    }

    public String getGenderThienSu(int idItem) {
        switch (idItem) {
            case 1071:
                return "Trái Đất";
            case 1072:
                return "Namếc";
            case 1073:
                return "Xayda";
            default:
                return "";
        }
    }
}
