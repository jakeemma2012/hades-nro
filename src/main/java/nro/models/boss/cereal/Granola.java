package nro.models.boss.cereal;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;

public class Granola extends FutureBoss {

    public Granola() {
        super(BossFactory.GRANOLA, BossData.GRANOLA);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        getRewardGranola(this, pl, 100, 100, 0);
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
    }

    @Override
    public void leaveMap() {
        BossFactory.createBoss(BossFactory.ELEC);
        this.setJustRestToFuture();
        super.leaveMap();
    }

}
