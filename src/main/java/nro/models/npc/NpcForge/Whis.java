package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.func.ChangeMapService;
import nro.services.func.CombineServiceNew;

public class Whis extends Npc {

    public Whis(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (this.mapId) {
                // case 48:
                // this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chưa tới giờ thi đấu, xem
                // hướng dẫn để biết thêm chi tiết",
                // "Hướng\ndẫn\nthêm", "Từ chối");
                // break;
                // case 154:
                // int level = TopWhis.GetLevel(player.id);
                // this.createOtherMenu(player, ConstNpc.MENU_WHIS_200,
                // "Ngươi muốn gì nào",
                // new String[]{"Nói chuyện",
                // "Hành tinh\nBill",
                // "Top 100", "[LV:" + level + "]"});
                // break;
                // case 200:
                // this.createOtherMenu(player, ConstNpc.MENU_WHIS,
                // "Ngươi muốn gì nào",
                // "Nói chuyện",
                // "Học\n Tuyệt kĩ");
                // break;

                case 154:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, cậu cần tôi hỗ trợ gì nào?",
                            "Chế tạo\nTrang bị\n Thiên sứ", "Chuyển hóa\n đá ma thuật", "Phân rã\n đồ Thiên sứ",
                            "Đóng");
                    break;
                case 48:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta sẽ dịch chuyển ngươi tới Hành tinh mới mang sức mạnh to lớn!",
                            "Đồng ý", "Từ chối");
                    break;
                case 190:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ngươi muốn quay trở về hành tinh Kaio?",
                            "Đồng ý", "Từ chối");
                    break;
                default:
                    super.openBaseMenu(player);
                    break;
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (this.mapId) {
                case 48:
                    if (player.nPoint.power >= 40_000_00_000L) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, 190, -1, 141);
                    } else {
                        this.npcChat(player, "Sức mạnh của ngươi không thể sống tại Hành tinh mới !");
                    }
                    break;
                case 190:
                    if (select == 0) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 353);
                    }
                    break;
                case 154:
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            switch (select) {
                                case 0:
                                    if (player.setClothes.SetHuyDiet == 5) {
                                        CombineServiceNew.gI().openTabCombine(player,
                                                CombineServiceNew.NANG_CAP_DO_THIEN_SU, this);
                                    } else {
                                        this.npcChat(player, "Ngươi chưa hoàn thiện Set trang bị Hủy Diệt !");
                                    }
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.CHUYEN_HOA_MA_THUAT, this);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHAN_RA_THIEN_SU, this);
                                    break;
                            }
                            break;
                        case ConstNpc.MENU_START_COMBINE:
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_DO_THIEN_SU:
                                case CombineServiceNew.CHUYEN_HOA_MA_THUAT:
                                case CombineServiceNew.PHAN_RA_THIEN_SU:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                        break;
                                    }
                            }
                            break;
                    }
                    break;
            }
        }
    }
}
// case ConstNpc.MENU_WHIS_200:
// switch (select) {
// case 0:
// this.createOtherMenu(player, ConstNpc.BASE_MENU,
// "Ta sẽ giúp ngươi chế tạo trang bị Thiên Sứ!",
// "OK", "Đóng");
// break;
// case 1:
// ChangeMapService.gI().changeMapBySpaceShip(player, 200, -1, 336);
// break;
// case 2:
// TopWhisService.ShowTop(player);
// break;
// case 3:
// int level = TopWhis.GetLevel(player.id);
// int whisId = TopWhis.GetMaxPlayerId();
// int coin = 1000;
// if (player.inventory.ruby < coin) {
// this.npcChat(player, "Mày chưa đủ xền");
// return;
// }
// player.inventory.ruby -= coin;
// Service.getInstance().sendMoney(player);
// TopWhis.SwitchToWhisBoss(player, whisId, level);
// break;
// }
// break;