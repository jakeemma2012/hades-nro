
package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.consts.ConstTask;
import nro.models.map.war.BlackBallWar;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.TaskService;
import nro.utils.Util;

/**
 *
 * 
 */
public class BoMong extends Npc {

    public BoMong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin chào? Cậu muốn tôi giúp gì sao?", "Đóng");
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 47 || this.mapId == 84) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            // this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                            // "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                            // "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                            // "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                            // "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                            // "Từ chối");
                            // }

                            // if (player.playerTask.sideTask.template != null) {
                            // String npcSay = "Nhiệm vụ hiện tại: "
                            // + player.playerTask.sideTask.getName() + " ("
                            // + player.playerTask.sideTask.getLevel() + ")"
                            // + "\nHiện tại đã hoàn thành: "
                            // + player.playerTask.sideTask.count + "/"
                            // + player.playerTask.sideTask.maxCount + " ("
                            // + player.playerTask.sideTask.getPercentProcess()
                            // + "%)\nSố nhiệm vụ còn lại trong ngày: "
                            // + player.playerTask.sideTask.leftTask + "/"
                            // + ConstTask.MAX_SIDE_TASK;
                            // this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                            // npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                            // } else {
                            // this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                            // "Tôi có vài nhiệm vụ theo cấp bậc, "
                            // + "sức cậu có thể làm được cái nào?",
                            // "Dễ", "Bình thường", "Khó", "Siêu khó", "Từ chối");
                            // }
                            break;
                        // case 1:
                        // this.npcChat(player, "Tính năng sớm truy cập !");
                        // break;
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                    switch (select) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                            TaskService.gI().changeSideTask(player, (byte) select);
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                    switch (select) {
                        case 0:
                            TaskService.gI().paySideTask(player);
                            break;
                        case 1:
                            TaskService.gI().removeSideTask(player);
                            break;
                    }
                }
            }
        }
    }
}
