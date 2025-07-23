package nro.models.boss.bosstuonglai;

import nro.models.boss.Boss;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.BossData;

public class Zamasu extends Boss {

    public Zamasu() {
        super(BossFactory.ZAMASU, BossData.ZAMASU);
    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[] {};
        this.textTalkMidle = new String[] {};
        this.textTalkAfter = new String[] {};
    }

    @Override
    public void idle() {
    }

    @Override
    public void joinMap() {
    }

    @Override
    public void rewards(Player player) {
        TaskService.gI().checkDoneTaskKillBoss(player, this);
        getRewardBlack(this,player,100,100);
    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (this.isDie()) {
            return 0;
        } else {
            if (plAtt != null) {
                if (Util.isTrue(20, 70)) {
                    damage = 1;
                    Service.getInstance().chat(this, "Xí hụt..");
                }
            }
            long dame = super.injured(plAtt, damage, piercing, isMobAttack);
            // if (this.isDie()) {
            // rewards(plAtt);
            // }
            return dame;
        }
    }

    @Override
    public boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
