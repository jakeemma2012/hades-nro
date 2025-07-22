package nro.models.boss.robotsatthu;

import java.util.Calendar;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

public class Android14 extends Boss {

    public Android14() {
        super(BossFactory.ANDROID_14, BossData.ANDROID_14);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player plKill) {
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    public void idle() {
    }

    @Override
    public void leaveMap() {
        Boss Adr13 = BossManager.gI().getBossById(BossFactory.ANDROID_13);
        if (Adr13 != null) {
            Adr13.changeToAttack();
        }
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void checkPlayerDie(Player pl) {
    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        long dame = 0;
        if (this.isDie()) {
            return dame;
        } else {
            final Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(11);
            if (hour >= 15 && hour < 17) {// 0 đến 8h sáng
                if (plAtt != null && plAtt.playerTask.taskMain.id != 22) {
                    if (plAtt.playerTask.taskMain.index != 1) {
                        if (damage >= 0) {
                            damage = 0;
                            Service.getInstance().sendThongBao(plAtt,
                                    "Bây giờ là giờ nhiệm vụ, không phải nhiệm vụ hiện tại của bạn, boss miễn nhiễm sát thương");
                        }
                    }
                }
            }
            dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (this.isDie()) {
                notifyPlayeKill(plAtt);
                die();
            }
            return dame;
        }
    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[] { "Các người là ai !!!!", "Hãy gọi Đại ca ra đây !", "Chuẩn bị !!!!",
                "Hừm!" };
        this.textTalkMidle = new String[] { "Kame Kame Haaaaa!!", "Mi khá đấy nhưng so với ta chỉ là hạng tôm tép",
                "Tất cả nhào vô hết đi", "Cứ chưởng tiếp đi. haha", "Các ngươi yếu thế này sao hạ được ta đây. haha",
                "Khi công pháo!!", "Cho mi biết sự lợi hại của ta" };
        this.textTalkAfter = new String[] { "Các ngươi được lắm", "Hãy đợi đấy thời gian tới ta sẽ quay lại.." };
    }

}
