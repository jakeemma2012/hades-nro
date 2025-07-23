package nro.models.boss.bosstuonglai;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.player.Player;

import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;

public class SuperZamax extends Boss {

    public SuperZamax() {
        super(BossFactory.ZAMASU2, BossData.SUPER_ZAMAX);
    }

    @Override
    public void rewards(Player pl) {
        getRewardBlack(this,pl,100,100);
    }

    @Override
    public void idle() {
    }

    @Override
    public void checkPlayerDie(Player pl) {
    }

    @Override
    public void joinMap() {
    }

    @Override
    public void initTalk() {
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void leaveMap() {
        Boss blackGoku = BossManager.gI().getBossById((int)this.parent.id);

        if (blackGoku != null) {
            blackGoku.setJustRest();
        }

        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
