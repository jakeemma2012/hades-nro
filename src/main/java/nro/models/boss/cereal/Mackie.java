package nro.models.boss.cereal;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;

public class Mackie extends FutureBoss {

    public Mackie() {
        super(BossFactory.MACKIE, BossData.MACKIE);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        getRewardGranola(this, pl, 100, 100, 1);
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
        Boss Oli = this.zone.getBossInMap(BossFactory.OLI);
        if (Oli != null && !Oli.isDie()) {
            Oli.changeToAttack();
        }
        BossManager.gI().removeBoss(this);
        super.leaveMap();
    }

}
