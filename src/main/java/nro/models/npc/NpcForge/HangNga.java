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
import nro.services.func.ShopService;
import nro.utils.Util;

public class HangNga extends Npc {

    public HangNga(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (mapId == 0 || mapId == 7 || mapId == 14) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào !\n "
                        + "Ta đến đây vì nghe nói ngươi có bánh kem !\n"
                        + "Cậu có nguyên liệu đó không ?\n"
                        + "Hãy thu thập đủ X99 Bó Hoa đỏ ,xanh ,hồng và X1 Mảnh giấy ký tên\n"
                        + "với 200 triệu vàng tôi có thể làm cho cậu 1 chiếc !\n"
                        + "|2|Cậu có muốn thử không ?","Đổi\n Bánh kem","Top Điểm\n Bánh Kem","Cửa hàng","Hành tinh\n  Thực vật", "Đóng");
            }

            if (mapId == 160) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "|2|Cậu đang cố gắng tìm nguyên liệu sao ?", "Quay về","Đóng");
            }

        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 0:
                case 7:
                case 14:
                    switch (select) {
                        case 0:
                            DoibanhKem(player);
                            break;
                        case 1:
                            Service.getInstance().shopTopBanhKem(player);
                            break;
                        case 2:
                            ShopService.gI().openShopSpecial(player, this, ConstNpc.HANG_NGA_SHOP, 0, -1);
                            break;
                        case 3:
                            ChangeMapService.gI().changeMapBySpaceShip(player, 160, -1, 1161);
                            break;
                        default:
                            break;
                    }
                    break;
                case 160:
                    ChangeMapService.gI().changeMapBySpaceShip(player, 0 + (7 * player.gender), -1, -1);
                    break;
                default:
                    break;
            }
        }
    }

    void DoibanhKem(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (InventoryService.gI().getQuantity(player, 1501) >= 99
                    && InventoryService.gI().getQuantity(player, 1502) >= 99
                    && InventoryService.gI().getQuantity(player, 1503) >= 99
                    && InventoryService.gI().getQuantity(player, 1504) >= 1) {
                if(player.inventory.gold >= 200_000_000) {
                    Item banh = ItemService.gI().createNewItem((short)1505);
                    InventoryService.gI().addItemBag(player, banh, 9999);
                    
                    InventoryService.gI().subQuantityItemsBag(player, 1501, 99);
                    InventoryService.gI().subQuantityItemsBag(player, 1502, 99);
                    InventoryService.gI().subQuantityItemsBag(player, 1503, 99);
                    InventoryService.gI().subQuantityItemsBag(player, 1504, 1);
                    InventoryService.gI().sendItemBags(player);
                    player.inventory.gold -= 200_000_000;
                    Service.getInstance().sendMoney(player);
                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được X1 Bánh kem");
                } else {
                    Service.getInstance().sendThongBao(player, "Bạn còn thiếu " + Util.numberToMoney(200_000_000 - player.inventory.gold) + " nữa để đổi !");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Bạn không mang đủ X99 Hoa đỏ, xanh,vàng hoặc thiếu Mảnh giấy");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    void DoiHopQuaTang(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (InventoryService.gI().getQuantity(player, 1486) >= 99
                    && InventoryService.gI().getQuantity(player, 1488) >= 5) {

                Item vp = ItemService.gI().createNewItem((short) 1487);

                InventoryService.gI().subQuantityItemsBag(player, 1486, 99);
                InventoryService.gI().subQuantityItemsBag(player, 1488, 5);

                InventoryService.gI().addItemBag(player, vp, 1);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được : " + vp.getName());
                InventoryService.gI().sendItemBags(player);
            } else {
                Service.getInstance().sendThongBao(player, "Bạn không có đủ X99 Bánh trung thu hoặc X5 Hộp trà rồi !");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    void DoiBanhTrungThu(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (InventoryService.gI().getQuantity(player, 1486) >= 99 && player.inventory.gold >= 200_000_000) {
                player.pointTrungThu += 1;
                short[] idList = {(short) Util.nextInt(1150, 1153), (short) Util.nextInt(220, 224), 17, 16, 999, (short) Util.nextInt(1454, 1457), 1463, 1462};
                short[] rate = {10, 20, 20, 5, 15, 20, 5, 5};
                short idvp = Util.getRandomId(idList, rate);
                if (idvp != 999) {
                    Item vp = ItemService.gI().createNewItem(idvp);
                    InventoryService.gI().addItemBag(player, vp, 1);
                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được : " + vp.getName() + " và nhận được 1 điểm sự kiện");
                } else {
                    player.pointBossThuong += 1;
                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 1 điểm Boss Thường");
                }
                InventoryService.gI().subQuantityItemsBag(player, 1486, 99);
                player.inventory.gold -= 200_000_000;
                Service.getInstance().sendMoney(player);
                InventoryService.gI().sendItemBags(player);
            } else {
                Service.getInstance().sendThongBao(player, "Bạn không đủ X99 Bánh trung thu hoặc 200 triệu vàng để đổi !");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    void DoiDauLan(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (InventoryService.gI().getQuantity(player, 1489) >= 5
                    && player.inventory.gold >= 500_000_000) {
                player.pointTrungThu += 2;
                short[] idList = {(short) Util.nextInt(828, 832), (short) Util.nextInt(220, 224), 1491, 17};
                short[] rate = {20, 20, 10, 50};
                short idvp = Util.getRandomId(idList, rate);
                Item vp = ItemService.gI().createNewItem(idvp);
                switch (vp.getId()) {
                    case 828:
                    case 829:
                    case 830:
                    case 831:
                    case 832:
                        vp.itemOptions.add(new ItemOption(30, 0));
                        break;
                    case 1491:
                        vp.itemOptions.add(new ItemOption(93, 7));
                        break;
                    default:
                        break;
                }
                InventoryService.gI().subQuantityItemsBag(player, 1489, 5);
                InventoryService.gI().addItemBag(player, vp, 1);
                player.inventory.gold -= 500_000_000;
                Service.getInstance().sendMoney(player);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được : " + vp.getName() + " và nhận được 2 điểm sự kiện");
                InventoryService.gI().sendItemBags(player);
            } else {
                Service.getInstance().sendThongBao(player, "Bạn không đủ X5 Đầu lân và 500 triệu vàng để đổi !");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }
}
