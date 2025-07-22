package nro.models.npc.NpcForge;

import java.text.DecimalFormat;
import java.util.ArrayList;
import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.jdbc.daos.PlayerDAO;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.PetService;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.Input;
import nro.utils.Log;
import nro.utils.TimeUtil;
import nro.utils.Util;

public class Trinova extends Npc {

    public Trinova(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    DecimalFormat fm = new DecimalFormat("##,###");

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "|2|Xin chào !? Con muốn làm gì nào ?",
                        "Nhận\n Ngọc xanh",
                        "Nhận\n Đệ tử",
                        "Nhập\nGift-Code",
                        "Kích hoạt\n tài khoản",
                        "Đổi Vàng \n Bằng Coin",
                        "Nhận quà\nPage",
                        "Đóng");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0:
                        if (player.inventory.gem < 1_000_000) {
                            player.inventory.gem += 1_000_000;
                            Service.getInstance().sendMoney(player);
                            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 1000K Ngọc Xanh");
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy sử dụng trước khi nhận tiếp ");
                        }
                        break;
                    case 1:
                        if (player.pet != null) {
                            Service.getInstance().sendThongBao(player, "Bạn đã có Đệ tử rồi !");
                        } else {
                            PetService.gI().createNormalPet(player, player.gender, (byte) 0);
                        }
                        break;
                    case 2:
                        Input.gI().createFormGiftCode(player);
                        break;
                    case 3:
                        activeUser(player);
                        break;
                    case 4:
                        this.createOtherMenu(player, 1, "Con đang có : " + player.getSession().vnd
                                + "\n Con có thể quy chiếu theo bảng sau : \n"
                                + "10K = 20TV + 1 hộp Goten\n"
                                + "20K = 40TV + x2 Item cấp 2\n"
                                + "50K = 105TV + x1 Phiếu X10 tỉ lệ pha lê\n"
                                + "100K = 210TV + x5 Hộp mảnh Goku SSJ, X10 thẻ x10 tỉ lệ SPL,x1 Xu Bông Tai\n"
                                + "500K = 1350TV\n"
                                + "1000K = 3000TV\n"
                                + "Con muốn đổi mốc nào ?",
                                "10K", "20K", "50K", "100K", "500K", "1000K", "Đóng");
                        break;
                    case 5:
                        NhanQuaPage(player);
                        break;

                }
            } else if (player.iDMark.getIndexMenu() == 1) {
                switch (select) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        ChangeGold(player, select);
                        break;
                    default:
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 3) {
                switch (select) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        DoiMocDiem(player, select);
                        break;
                    default:
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 4) {
                switch (select) {
                    case 0:
                    case 1:
                        DoiCode(player, select);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    void NhanGold(Player player) {
        if (player.getSession().actived) {
            int gold = player.getSession().goldBar;
            if (gold > 0) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    PlayerDAO.subGoldBar(player, gold);
                    Item tv = ItemService.gI().createNewItem((short) 457, gold);
                    InventoryService.gI().addItemBag(player, tv, 999999);
                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + gold + " thỏi vàng");
                    Log.errorSaveSQL(player.name + " nhận thỏi vàng amount: " + gold);
                    InventoryService.gI().sendItemBags(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Hiện không có số dư nào !");
            }

        } else {
            Service.getInstance().sendThongBao(player, "Chức năng chỉ dành cho tài khoản đã kích hoạt !");
        }
    }

    void activeUser(Player player) {
        if (!player.getSession().actived) {
            if (player.getSession().vnd >= MONEY_ACTIVE_USER) {
                if (InventoryService.gI().getCountEmptyBag(player) > 3) {
                    Service.getInstance().sendThongBao(player, "Kích hoạt thành công");
                    PlayerDAO.subCoin(player, MONEY_ACTIVE_USER, "MTV");
                    PlayerDAO.ActiveUser(player);
                    short idList[] = {1390, 1465};
                    for (short id : idList) {
                        Item vp = ItemService.gI().createNewItem(id);
                        InventoryService.gI().addItemBag(player, vp, 99999);
                        InventoryService.gI().sendItemBags(player);
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Hành trang phải trống 3 ô để kích hoạt ");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không đủ Coin");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Tài khoản của bạn đã được kích hoạt");
        }
    }

    int MONEY_ACTIVE_USER = 10000;
    int MONEY_CHANGE_GOLD[] = {10000, 20000, 50000, 100000, 500000, 1000000};
    int MONEY_DOI_MOC[] = {100, 500, 1000, 3000};
    int GOLD_RECEIVE[] = {20, 40, 105, 210, 1350, 3000};

    void ChangeGold(Player player, int select) {
        int plMoney = player.getSession().vnd;
        int money = MONEY_CHANGE_GOLD[select];
        if (plMoney >= money) {
            if (InventoryService.gI().getCountEmptyBag(player) > 2) {
                Item tv = ItemService.gI().createNewItem((short) 457, GOLD_RECEIVE[select]);
                InventoryService.gI().addItemBag(player, tv, 9999);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Đổi " + money + " Coin ra " + GOLD_RECEIVE[select] + " thỏi vàng thành công");
                switch (select) {
                    case 0: {
                        Item vp = ItemService.gI().createNewItem((short)1492, 1);
                        InventoryService.gI().addItemBag(player, vp, 9999);
                    }
                    break;
                    case 1: {
                        Item vp = ItemService.gI().createNewItem((short) Util.nextInt(1150, 1153), 2);
                        InventoryService.gI().addItemBag(player, vp, 9999);
                    }
                    break;
                    case 2: {
                        Item vp = ItemService.gI().createNewItem((short) 1465, 1);
                        InventoryService.gI().addItemBag(player, vp, 9999);
                    }
                    break;
                    case 3: {
                        Item vp = ItemService.gI().createNewItem((short) 1440, 5);
                        InventoryService.gI().addItemBag(player, vp, 9999);

                        Item the = ItemService.gI().createNewItem((short) 1465, 10);
                        InventoryService.gI().addItemBag(player, the, 9999);
                        
                        Item xu = ItemService.gI().createNewItem((short)1497,1);
                        InventoryService.gI().addItemBag(player, xu, 9999);
                    }
                    break;
                    default:
                        break;
                }
                PlayerDAO.subCoin(player, money, "GOLD : " + Util.fm.format(money));
                player.TotalPointNap += (money / 1000);
                player.ChangePointNap += (money / 10000);
                InventoryService.gI().sendItemBags(player);
            } else {
                Service.getInstance().sendThongBao(player, "Hành trang đã đầy");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Nạp thêm " + Util.numberToMoney(money - plMoney) + " coin nhé");
        }
    }

    void DoiMocDiem(Player player, int select) {
        if (player.TotalPointNap >= MONEY_DOI_MOC[select]) {
            switch (select) {
                case 0:
                    if (!player.m1) {
                        if (InventoryService.gI().getCountEmptyBag(player) > 6) {
                            ItemService.gI().addItemDoiMocNap(player, select);
                            Service.getInstance().sendThongBao(player, "Bạn vừa đổi thành công mốc nạp 1 , nhớ kiểm tra hành trang nhé !");
                            player.m1 = true;
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy để 6 ô hành trang trống nhé !");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Bạn đã nhận mốc 1 rồi !");
                    }
                    break;
                case 1:
                    if (!player.m2) {
                        if (InventoryService.gI().getCountEmptyBag(player) > 6) {
                            ItemService.gI().addItemDoiMocNap(player, select);
                            Service.getInstance().sendThongBao(player, "Bạn vừa đổi thành công mốc nạp 2, nhớ kiểm tra hành trang nhé !");
                            player.m2 = true;
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy để 6 ô hành trang trống nhé !");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Bạn đã nhận mốc 2 rồi !");
                    }
                    break;
                case 2:
                    if (!player.m3) {
                        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
                            ItemService.gI().addItemDoiMocNap(player, select);
                            Service.getInstance().sendThongBao(player, "Bạn vừa đổi thành công mốc nạp 3, nhớ kiểm tra hành trang nhé !");
                            player.m3 = true;
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy để 5 ô hành trang trống nhé !");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Bạn đã nhận mốc 3 rồi !");
                    }
                    break;
                case 3:
                    if (!player.m4) {
                        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
                            ItemService.gI().addItemDoiMocNap(player, select);
                            Service.getInstance().sendThongBao(player, "Bạn vừa đổi thành công mốc nạp 4, nhớ kiểm tra hành trang nhé !");
                            player.m4 = true;
                        } else {
                            Service.getInstance().sendThongBao(player, "Hãy để 5 ô hành trang trống nhé !");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Bạn đã nhận mốc 4 rồi !");
                    }
                    break;
                default:
                    break;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Không đủ điểm mốc nạp , không thể đổi !");
        }
    }

    void DoiCode(Player player, int select) {
        int pointNeed = 5 + 45 * select;
        if (player.ChangePointNap >= pointNeed) {
            if (InventoryService.gI().getCountEmptyBag(player) > 10) {
                short idList[];
                if (select == 0) {
                    idList = new short[]{16, 1354, 1150, 1151, 1152, 1153, 1304, 1305, 1355, 1993};
                } else {
                    idList = new short[]{16, 1354, 1150, 1151, 1152, 1153, 1304, 1305, 1355, 1353, 1993};
                }
                for (short id : idList) {
                    Item vp = ItemService.gI().createNewItem(id);
                    switch (vp.template.id) {
                        case 16:
                            vp.quantity = 1 + 9 * select;
                            break;
                        case 1354:
                            vp.quantity = 50 + 450 * select;
                            break;
                        case 1150:
                        case 1151:
                        case 1152:
                        case 1153:
                        case 1304:
                        case 1305:
                            vp.quantity = 5 + 45 * select;
                            break;
                        case 1355:
                            vp.quantity = 10 + 89 * select;
                            break;
                        case 1993:
                            vp.quantity = 10 + 100 * select;
                            break;
                        default:
                            break;
                    }
                    InventoryService.gI().addItemBag(player, vp, 9999);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Đổi thành công " + pointNeed + " điểm Code");
                player.ChangePointNap -= pointNeed;
            } else {
                Service.getInstance().sendThongBao(player, "Hãy để trống 10 ô hành trang nhé !");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Không đủ " + pointNeed + " điểm tích lũy Code để đổi !");
        }
    }

    void DiemdanhDaily(Player player) {
        if (player.getSession().actived) {
            if (Util.getNumDateFromJoinTimeToToday(player.lastTimeDiemDanh) > 0) {
                if (InventoryService.gI().getCountEmptyBag(player) > 3) {
                    ArrayList<Item> listit = new ArrayList<>();
                    switch (player.DaysDiemDanh) {
                        case 0:
                        case 5:
                        case 10:
                        case 18: {
                            Item vp = ItemService.gI().createNewItem((short) 189);
                            player.inventory.gold += 5_000_000_000L;
                            listit.add(vp);
                        }
                        break;
                        case 1: {
                            Item vp = ItemService.gI().createNewItem((short) 987, 5);
                            listit.add(vp);
                        }
                        break;
                        case 2: {
                            Item vp = ItemService.gI().createNewItem((short) 1981, 100);
                            listit.add(vp);
                        }
                        break;
                        case 3: {
                            Item vp = ItemService.gI().createNewItem((short) 933, 100);
                            Item vp1 = ItemService.gI().createNewItem((short) 934, 100);
                            listit.add(vp);
                            listit.add(vp1);
                        }
                        break;
                        case 4: {
                            Item vp = ItemService.gI().createNewItem((short) 1304, 10);
                            Item vp1 = ItemService.gI().createNewItem((short) 1305, 10);
                            listit.add(vp);
                            listit.add(vp1);
                        }
                        break;
                        case 6: {
                            short idlist[] = {1304, 1305, 1150, 1151, 1152, 1153, 611};
                            short idlistquan[] = {10, 10, 5, 5, 5, 5, 1};
                            int i = 0;
                            for (short id : idlist) {
                                Item it = ItemService.gI().createNewItem(id, idlistquan[i]);
                                i++;
                                listit.add(it);
                            }
                        }
                        case 7: {
                            short idlist[] = {1150, 1151, 1152, 1153};
                            for (short id : idlist) {
                                Item it = ItemService.gI().createNewItem(id, 5);
                                listit.add(it);
                            }
                        }
                        break;
                        case 8:
                        case 14: {
                            Item vp = ItemService.gI().createNewItem((short) 987, 5);
                            listit.add(vp);
                        }
                        break;
                        case 9: {
                            Item vp = ItemService.gI().createNewItem((short) 1404);
                            listit.add(vp);
//                          for (int i = 0; i < 5; i++) {
//                                Item vp = ItemService.gI().createNewItem((short) ConstItem.LIST_ITEM_CLOTHES[player.gender][i][0]);
//                                RewardService.gI().initBaseOptionClothes(vp);
//                                vp.itemOptions.add(new ItemOption(107, 7));
//                                listit.add(vp);
//                            }
                            break;
                        }
                        case 11: {
                            Item vp = ItemService.gI().createNewItem((short) 1981, 200);
                            listit.add(vp);
                        }
                        break;
                        case 12: {
                            Item vp = ItemService.gI().createNewItem((short) 933, 200);
                            Item vp1 = ItemService.gI().createNewItem((short) 934, 200);
                            listit.add(vp);
                            listit.add(vp1);
                        }
                        break;
                        case 13: {
                            int type = 0;
                            if (Util.isTrue(1, 10)) {
                                type = 0;
                            } else {
                                type = Util.getOne(1, 3);
                            }
                            Item dhd = ItemService.
                                    gI().createNewItem((short) ConstItem.doSKHVip[type][player.gender][13]);
                            RewardService.gI().initBaseOptionClothes(dhd);
                            listit.add(dhd);
                        }
                        break;
                        case 15: {
                            Item vp = ItemService.gI().createNewItem((short) Util.getOne(1366, 1369));
                            vp.itemOptions.add(new ItemOption(50, 7));
                            vp.itemOptions.add(new ItemOption(77, 7));
                            vp.itemOptions.add(new ItemOption(103, 7));
                            if (Util.isTrue(99, 100)) {
                                vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                            }
                            listit.add(vp);
                        }
                        break;
                        case 16: {
                            Item vp = ItemService.gI().createNewItem((short) Util.getOne(1274, 1275));
                            vp.itemOptions.add(new ItemOption(50, 32));
                            vp.itemOptions.add(new ItemOption(77, 32));
                            vp.itemOptions.add(new ItemOption(103, 32));
                            vp.itemOptions.add(new ItemOption(5, 10));
                            if (Util.isTrue(99, 100)) {
                                vp.itemOptions.add(new ItemOption(93, Util.getOne(7, 15)));
                            }
                            listit.add(vp);
                        }
                        break;
                        case 17: {
                            Item vp = ItemService.gI().createNewItem((short) Util.getOne(1111, 1001));
                            vp.itemOptions.add(new ItemOption(50, 5));
                            vp.itemOptions.add(new ItemOption(77, 5));
                            vp.itemOptions.add(new ItemOption(103, 5));
                            if (Util.isTrue(99, 100)) {
                                vp.itemOptions.add(new ItemOption(93, Util.getOne(7, 15)));
                            }
                            listit.add(vp);
                        }
                        break;
                        case 19: {

                        }
                        break;
                        default:
                            break;
                    }
                    if (!listit.isEmpty()) {
                        for (Item it : listit) {
                            it.itemOptions.add(new ItemOption(30, 1));
                            InventoryService.gI().addItemBag(player, it, 9999);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendMoney(player);
                        }
                        player.DaysDiemDanh += 1;
                        player.lastTimeDiemDanh = System.currentTimeMillis();
                        Service.getInstance().sendThongBao(player, "Bạn vừa nhận thưởng ngày thứ " + player.DaysDiemDanh);
                    } else {
                        this.npcChat(player, "|7|Có lỗi xảy ra !");
                    }
                } else {
                    this.npcChat(player, "|7|Yêu cầu bỏ trống 2 ô hành trang đã nhé !");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Hãy đợi " + TimeUtil.getTimeLeft(player.lastTimeDiemDanh, (8_640_000_000L / 1000L)) + " nữa nhé ");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Chức năng chỉ dành cho kích hoạt thành viên !");
        }
    }

    void kichHoatThanhVienVip(Player player) {
        if (player.DaysTheTuan == -1) {
            if (player.getSession().vnd >= 100_000) {
                if (InventoryService.gI().getCountEmptyBag(player) > 11) {
                    short idlist[] = {457, 1398, 987, 1361};
                    for (short id : idlist) {
                        Item vp = ItemService.gI().createNewItem(id);
                        switch (vp.template.id) {
                            case 457:
                                vp.quantity = 100;
                                break;
                            case 1398:
                                vp.itemOptions.add(new ItemOption(93, 1));
                                vp.itemOptions.add(new ItemOption(30, 0));
                                break;
                            case 987:
                                vp.quantity = 10;
                                break;
                            case 1361:
                                vp.quantity = 2;
                                break;
                            default:
                                break;
                        }
                        InventoryService.gI().addItemBag(player, vp, 9899);
                    }

                    for (int i = 0; i < 5; i++) {
                        Item vp = ItemService.gI().createNewItem((short) ConstItem.LIST_ITEM_CLOTHES[player.gender][i][0]);
                        RewardService.gI().initBaseOptionClothes(vp);
                        vp.itemOptions.add(new ItemOption(107, 8));
                        vp.itemOptions.add(new ItemOption(93, 3));
                        InventoryService.gI().addItemBag(player, vp, 1);
                    }

                    int id = (player.gender == 0 ? 1323 : (player.gender == 1 ? 1328 : 1333));
                    InventoryService.gI().addItemBag(player, ItemService.gI().createNewItem((short) id), 999);

                    InventoryService.gI().sendItemBags(player);
                    PlayerDAO.subCoin(player, 100_000, "VIP");
                    player.DaysTheTuan = 0;
                    player.lastTimeRewardsTheTuan = System.currentTimeMillis();
                    Service.getInstance().sendThongBao(player, "Bạn kích hoạt thành viên VIP thành công , vui lòng kiểm tra hành trang !");
                } else {
                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy để trống 11 ô hành trang để kích hoạt thành viên VIP nhé !", "Đóng");
                }

            } else {
                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Số dư không đủ !", "Đóng");
            }
        } else {
            this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn đã kích hoạt thành viên V.I.P rồi !", "Đóng");
        }
    }

    
    void NhanQuaPage(Player player){
        if(player.getSession().actived) {
            switch (player.hasNhanquaPage) {
                case 1:
                    if(InventoryService.gI().getCountEmptyBag(player) > 0)  {
                        player.inventory.gold += 50_000_000_000L;
                        Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 50 tỉ vàng ");
                        Service.getInstance().sendMoney(player);
                        player.hasNhanquaPage = 2;
                    } else {
                        Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
                    }   break;
                case 2:
                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn đã nhận phần quà này rồi mà !", "Đóng");
                    break;
                default:
                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn chưa chia sẻ Fanpage !", "Đóng");
                    break;
            }
        }  else {
            Service.getInstance().sendThongBao(player, "Chức năng dành cho mở thành viên !");
        }
    }
}
