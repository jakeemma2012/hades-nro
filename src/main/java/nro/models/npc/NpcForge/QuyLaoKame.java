package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.map.phoban.BanDoKhoBau;
import nro.models.npc.Npc;
import static nro.models.npc.NpcFactory.PLAYERID_OBJECT;
import nro.models.player.Player;
import nro.services.BanDoKhoBauService;
import nro.services.NpcService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.services.func.Input;
import nro.utils.Util;

import nro.models.item.Item;
import nro.services.InventoryService;

public class QuyLaoKame extends Npc {

    public QuyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Chào con, con muốn ta giúp gì nào !?",
                        "Nói chuyện",
                        "Từ chối");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0:
                        this.createOtherMenu(player, ConstNpc.NOI_CHUYEN,
                                "Chào con, ta rất vui khi gặp con\nCon muốn làm gì nào?",
                                "Nhiệm vụ", "Giản tán\nBang hội", "Kho báu\ndưới biển");
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.NOI_CHUYEN) {
                switch (select) {
                    case 0:
                        NpcService.gI().createTutorial(player, 564, "Nhiệm vụ tiếp theo của bạn là: "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                        break;
                    case 1:
                        Clan clan = player.clan;
                        if (clan != null) {
                            ClanMember cm = clan.getClanMember((int) player.id);
                            if (cm != null) {
                                if (clan.members.size() > 1) {
                                    Service.getInstance().sendThongBao(player, "Bang phải còn một người");
                                    break;
                                }
                                if (!clan.isLeader(player)) {
                                    Service.getInstance().sendThongBao(player, "Phải là bảng chủ");
                                    break;
                                }
                                NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                        "OK!", "Từ chối!");
                            }
                            break;
                        }
                        Service.getInstance().sendThongBao(player, "Chưa có bang hội!!!");
                        break;
                    case 2:
//                        Service.getInstance().sendThongBao(player, "Chức năng đang trong quá trình hoàn thiện ");
                        if (player.clan != null) {
                            if (player.clan.banDoKhoBau != null) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                        "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                        + player.clan.banDoKhoBau.level
                                        + "\nCon có muốn đi theo không?",
                                        "Đồng ý", "Từ chối");
                            } else {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                        "Đây là bản đồ kho báu hải tặc tí hon\nCác con cứ yên tâm lên đường\n"
                                        + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                        "Chọn\ncấp độ", "Từ chối");
                            }
                        } else {
                            NpcService.gI().createTutorial(player, 564, "Con phải có bang hội ta mới có thể cho con đi");
                        }
                        break;

                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                switch (select) {
                    case 0:
                        if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                            ChangeMapService.gI().goToDBKB(player);
                        } else {
                            this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                    + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                        }
                        break;

                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                switch (select) {
                    case 0:
                        if (player.isAdmin()
                                || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                            Input.gI().createFormChooseLevelBDKB(player);
                        } else {
                            this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                    + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                        }
                        break;
                }

            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                switch (select) {
                    case 0:
                        BanDoKhoBauService.gI().openBanDoKhoBau(player,
                                Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                        break;
                }

            }
        }
    }

    void BuyDanhHieu(Player pl) {
        Item xu = InventoryService.gI().findItemBag(pl, 1993);
        if (xu != null && xu.quantity >= 5) {
            if (!pl.itemTime.Received50Ti) {
                InventoryService.gI().subQuantityItemsBag(pl, xu, 5);
                InventoryService.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Mua thành công danh hiệu Vương thần");
                pl.itemTime.Received50Ti = true;
                pl.itemTime.isUseDanhHieu1 = true;
            } else {
                Service.getInstance().sendThongBao(pl, "Bạn đã mua rồi mà !");
            }
        }
    }

}
