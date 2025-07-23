package nro.models.boss.NgucTu;

import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 * 
 */
public class SuperCumber extends FutureBoss {

    public SuperCumber() {
        super(BossFactory.CUMBER2, BossData.CUMBER2);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player plKill) {
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
        ItemMap itemMap = new ItemMap(zone, Util.nextInt(2014, 2018), 1, x, y, plKill.id);
        RewardService.gI().initBaseOptionTinhThach(itemMap);
        Service.getInstance().dropItemMap(zone, itemMap);
    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (this.isDie()) {
            return 0;
        } else {
            if (plAtt != null) {
                if (Util.isTrue(25, 70)) {
                    damage = 1;
                    Service.getInstance().chat(this, "Xí hụt..");
                }
            }
            long dame = super.injured(plAtt, damage, piercing, isMobAttack);
            return dame;
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
        // this.topDame.clear();
        // Service.getInstance().sendTextTime(this, (byte) 10, "", (short) 0);
        // Service.getInstance().sendTextTime(this, (byte) 11, "", (short) 0);
        // Service.getInstance().sendTextTime(this, (byte) 12, "", (short) 0);
        BossManager.gI().getBossById(BossFactory.CUMBER).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
