package nro.models.boss.cold;

import nro.models.boss.*;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 
 */
public class Cooler2 extends Boss {

    public Cooler2() {
        super(BossFactory.COOLER2, BossData.COOLER2);
    }

    @Override
    public void rewards(Player plKill) {
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
        getRewardsBossNormalOrKichAn(this, plKill, 50, 100);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (this.isDie()) {
            return 0;
        } else {
            if (!piercing && Util.isTrue(25, 100)) {
                this.chat("Xí hụt lêu lêu");
                return 0;
            }
            if (this.isDie()) {
                rewards(plAtt);
            }
            return super.injured(plAtt, damage, piercing, isMobAttack);
        }
    }

    @Override
    public void idle() {
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {

        textTalkAfter = new String[] { "Ta đã giấu hết ngọc rồng rồi, các ngươi tìm vô ích hahaha" };
    }

    @Override
    public void leaveMap() {
        BossManager.gI().getBossById(BossFactory.COOLER).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
