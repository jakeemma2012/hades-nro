package nro.models.boss.cereal;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;

public class Oli extends FutureBoss {

    public Oli() {
        super(BossFactory.OLI, BossData.OLI);
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
        Boss Granola = BossManager.gI().getBossById(BossFactory.GRANOLA);
        if (Granola != null) {
            Granola.setJustRest();
        }
        BossManager.gI().removeBoss(this);
        super.leaveMap();
    }

}
