package nro.models.boss.traidat;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;

/**
 *
 */
public class LanCon extends Boss {

    public LanCon() {
        super(BossFactory.LANCON, BossData.LANCON);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
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
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        return super.injured(plAtt, 1, piercing, isMobAttack);
    }

    @Override
    public void rewards(Player pl) {
        this.dropItemReward(1489, (int) pl.id, 1);
    }
}
