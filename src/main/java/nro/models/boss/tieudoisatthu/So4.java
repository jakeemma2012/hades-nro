package nro.models.boss.tieudoisatthu;

import java.util.Calendar;
import nro.models.boss.*;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 *
 
 *
 */
public class So4 extends FutureBoss {

    public So4() {
        super(BossFactory.SO4, BossData.SO4);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
//        generalRewards(pl);
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{""};
        this.textTalkMidle = new String[]{"Oải rồi hả?", "Ê cố lên nhóc",
            "Chán", "Đại ca Fide có nhầm không nhỉ"};
    }

    @Override
    public void leaveMap() {
        Boss bossSO3 = BossManager.gI().getBossById(BossFactory.SO3);
        if (bossSO3 != null) {
            bossSO3.changeToAttack();
        }
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
