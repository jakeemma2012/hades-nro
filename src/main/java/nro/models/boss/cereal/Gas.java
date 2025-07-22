package nro.models.boss.cereal;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;

public class Gas extends FutureBoss {

    public Gas() {
        super(BossFactory.GAS, BossData.GAS);
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
        Boss Mackie = this.zone.getBossInMap(BossFactory.MACKIE);
        if (Mackie != null && !Mackie.isDie()) {
            Mackie.changeToAttack();
        }
        BossManager.gI().removeBoss(this);
        super.leaveMap();
    }

}
