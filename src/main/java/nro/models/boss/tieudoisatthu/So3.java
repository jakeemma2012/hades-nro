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
public class So3 extends FutureBoss {

    public So3() {
        super(BossFactory.SO3, BossData.SO3);
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
        this.textTalkMidle = new String[]{"Oải rồi hả?", "Ê cố lên nhóc",
            "Chán", "Đại ca Fide có nhầm không nhỉ"};

    }

    @Override
    public void leaveMap() {
        Boss bossSO2 = BossManager.gI().getBossById(BossFactory.SO2);
        if (bossSO2 != null) {
            bossSO2.changeToAttack();
        }
        Boss bossSO1 = BossManager.gI().getBossById(BossFactory.SO1);
        if (bossSO1 != null) {
            bossSO1.changeToAttack();
        }
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
