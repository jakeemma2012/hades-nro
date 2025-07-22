package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.consts.ConstTask;
import nro.models.npc.ImageMenu;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.ItemTimeService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.TimeUtil;

public class BlackGuku extends Npc {

    public BlackGuku(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                switch (this.mapId) {
                    case 19:
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chào cậu!\n Tôi tới từ tương lai, nơi đã phát triển hơn rất nhiều công nghệ\n"
                                        + "Và tôi ở đây để có thể giúp bạn !\n"
                                        + "Hãy cùng đồng hành nhé !",
                                "Bóng tối\n Porata", "Bư\n Bóng tối", "Siêu\ndoanh đoàn", "Làng máu", "Từ chối");
                        break;

                    case 215:
                    case 223:
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chào cậu! sợ chạy về à",
                                "Chạy\nđi ngay", "Từ chối");
                        break;
                    case 222:
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chào cậu! sợ chạy về à",
                                "Chạy\nđi ngay", "Từ chối");
                        break;
                    case 221:

                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Chào cậu! sợ chạy về à",
                                "Chạy\nđi ngay", "Phù hộ\n X5", "Từ chối");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (this.mapId) {
                case 19:
                    switch (select) {
                        case 0:
                            if (player.getSession().actived) {
                                if (TaskService.gI().getMainIdTask(player) > 19) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 222, -1, 109);
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Hãy hoàn thành nhiệm vụ Kuku trước nhé !");
                                }
                            } else {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Chức năng chỉ dành cho thành viên !", "Đóng");
                            }
                            break;
                        case 1:
                            if (player.getSession().actived) {
                                if (TimeUtil.getCurrHour() % 2 == 0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 215, -1, 552);

                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Khu vực chỉ cho phép hoạt động giờ chẵn !");
                                }
                            } else {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Chức năng chỉ dành cho thành viên !", "Đóng");
                            }
                            break;
                        case 2:
                            if (player.getSession().actived) {
                                if (TaskService.gI().getIdTask(player) > ConstTask.TASK_21_4) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 223, -1, 305);
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Cần phải hoàn thành nhiệm vụ Fide trước nhé !");
                                }
                            } else {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Chức năng chỉ dành cho thành viên !", "Đóng");
                            }
                            break;
                        case 3:
                            if (player.getSession().actived) {
                                if (TaskService.gI().getMainIdTask(player) > 19) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 221, -1, 105);
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Hãy hoàn thành nhiệm vụ KuKu trước nhé !");
                                }
                            } else {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Chức năng chỉ dành cho thành viên !", "Đóng");
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case 215:
                case 223:
                    switch (select) {
                        case 0:
                            ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 956);
                            break;
                    }
                    break;
                case 222:
                    switch (select) {
                        case 0:
                            ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 956);
                            break;
                    }
                    break;
                case 221:
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 956);
                                break;
                            case 1:
                                ImageMenu x5 = new ImageMenu("Đồng ý", 31824, 20, 5);
                                ImageMenu tuchoi = new ImageMenu("Từ chối");
                                this.createOtherMenu(player, 1, "|2|Sức mạnh Thiên sứ đã được khai mở"
                                        + "\n|6|Tôi có khả năng truyền đạo sức mạnh thiên sứ\n"
                                        + "cậu có muốn sỡ hữu sức mạnh Thiên sứ\ntrong 30 phút với giá 10 tỉ vàng không?",
                                        "Đồng ý", "Từ chối");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        if (select == 0) {
                            if (player.inventory.gold >= 10_000_000_000L) {

                                player.itemTime.setActiveTime(999, 30, true);

                                player.inventory.gold -= 10_000_000_000L;
                                Service.getInstance().sendMoney(player);

                                Service.getInstance().sendThongBao(player,
                                        "Bạn sẽ nhận được sức mạnh Thiên sứ trong 30 phút ");
                                ItemTimeService.gI().sendAllItemTime(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Không đủ 10 tỉ vàng");
                            }
                        }
                    }
                    break;
            }
        }
    }

    void buyBua(Player player) {

    }
}
