
package nro.models.npc.NpcForge;

import nro.consts.ConstMap;
import nro.consts.ConstNpc;
import nro.models.map.war.BlackBallWar;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.NpcService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Log;

public class RongOmega extends Npc {

    public RongOmega(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            BlackBallWar.gI().setTime();
            if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                try {
                    long now = System.currentTimeMillis();
                    if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW,
                                "Đường đến với ngọc rồng sao đen đã mở, "
                                        + "ngươi có muốn tham gia không?",
                                "Hướng dẫn\nthêm", "Tham gia", "Giải đất\n Ngọc rồng", "Từ chối");
                    } else {
                        String[] optionRewards = new String[7];
                        int index = 0;
                        for (int i = 0; i < 7; i++) {
                            if (player.rewardBlackBall.timeOutOfDateReward[i] > System
                                    .currentTimeMillis()) {
                                optionRewards[index] = "Nhận thưởng\n" + (i + 1) + " sao";
                                index++;
                            }
                        }
                        if (index != 0) {
                            String[] options = new String[index + 1];
                            for (int i = 0; i < index; i++) {
                                options[i] = optionRewards[i];
                            }
                            options[options.length - 1] = "Từ chối";
                            this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW,
                                    "Ngươi có một vài phần thưởng ngọc " + "rồng sao đen đây!",
                                    options);
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                    "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Giải đấu\n ngọc rồng", "Từ chối");
                        }
                    }
                } catch (Exception ex) {
                    Log.error("Lỗi mở menu rồng Omega");
                }
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (player.iDMark.getIndexMenu()) {
                case ConstNpc.MENU_REWARD_BDW:
                    if (player.clan != null) {
                        if (player.clan.isLeader(player)) {
                            player.rewardBlackBall.getRewardSelect((byte) select);
                        } else {
                            Service.getInstance().sendThongBao(player, "Chỉ bang chủ mới có thể thực hiện !");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Bạn chưa có bang hội !");
                    }
                    break;
                case ConstNpc.MENU_OPEN_BDW:
                    if (select == 0) {
                        showMenuTutorial(player);
                    } else if (select == 1) {
                        player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                        ChangeMapService.gI().openChangeMapTab(player);
                    }
                    break;
                case ConstNpc.MENU_NOT_OPEN_BDW:
                    if (select == 0) {
                        showMenuTutorial(player);
                    }
                    break;
            }
        }
    }

    void showMenuTutorial(Player player) {
        StringBuilder st = new StringBuilder();
        st.append("Mỗi ngày từ 20 đến 21h Ngọc Rồng Sao Đen sẽ xảy ra 1 cuộc đại chiến\n")
                .append("Người nào tìm thấy và giữ được Ngọc Rồng trong 5 phút\nsẽ mang phần thưởng về cho bang hội\n")
                .append("Lưu ý : Mỗi bang có thể chiếm hữu nhiều viên khác nhau\nnhưng nếu cùng loại cũng chỉ nhận được 1 lần phần thưởng đó\n")
                .append("Phần thưởng được cấp theo như hướng dẫn sau :\n")
                .append("1 Sao : tăng 20% Sức đánh\n")
                .append("2 Sao : tăng 25% Hp\n")
                .append("3 Sao : tăng 25% Ki\n")
                .append("4 Sao : tăng 20% Tiềm năng và sức mạnh\n")
                .append("5 Sao : 10% SD HP KI\n")
                .append("6 Sao : 10% STCM\n")
                .append("7 Sao : tăng 100K sức mạnh đệ tử\n")
                .append("Chúc các ngươi may mắn và sống sót trở về nhé !");
        NpcService.gI().createMenuConMeo(player, this.tempId, this.avartar, st.toString(), "OK !");
    }
}
