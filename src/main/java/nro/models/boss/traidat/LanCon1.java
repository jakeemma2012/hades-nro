package nro.models.boss.traidat;

import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.services.PlayerService;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

/**
 *
 */
public class LanCon1 extends Boss {

    public LanCon1() {
        super(BossFactory.LANCON1, BossData.LANCON);
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
