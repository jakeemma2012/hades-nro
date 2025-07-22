package nro.models.boss.cold;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;
import nro.services.TaskService;
import nro.utils.Util;

public class Cooler extends Boss {

    public Cooler() {
        super(BossFactory.COOLER, BossData.COOLER);
    }

    @Override
    public void rewards(Player plKill) {
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
        getRewardsBossNormalOrKichAn(this, plKill, 100, 100);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (this.isDie()) {
            return 0;
        } else {
            if (!piercing && Util.isTrue(25, 100)) {
                this.chat("Xí hụt lêu lêu");
                return 0;
            }
            if (this.isDie()) {
                rewards(plAtt);
            }
            return super.injured(plAtt, damage, piercing, isMobAttack);
        }
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        textTalkMidle = new String[] { "Ta chính là đệ nhất vũ trụ cao thủ" };
        textTalkAfter = new String[] { "Ác quỷ biến hình aaa..." };
    }

    @Override
    public void leaveMap() {
        Boss cooler2 = BossFactory.createBoss(BossFactory.COOLER2);
        cooler2.zone = this.zone;
        this.setJustRestToFuture();
        super.leaveMap();
    }

}
