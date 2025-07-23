package nro.models.boss.NgucTu;

import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

public class Cumber extends FutureBoss {

    public Cumber() {
        super(BossFactory.CUMBER, BossData.CUMBER);
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
            long dame = super.injured(plAtt, damage, piercing, isMobAttack);
            // if (this.isDie()) {
            // rewards(plAtt);
            // }
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
        textTalkMidle = new String[]{"Ta chính là đệ nhất vũ trụ cao thủ"};
    }

    @Override
    public void leaveMap() {
        // this.topDame.clear();
        // Service.getInstance().sendTextTime(this, (byte) 10, "", (short) 0);
        // Service.getInstance().sendTextTime(this, (byte) 11, "", (short) 0);
        // Service.getInstance().sendTextTime(this, (byte) 12, "", (short) 0);
        Boss cumber2 = BossFactory.createBoss(BossFactory.CUMBER2);
        cumber2.zone = this.zone;
        this.setJustRestToFuture();
        super.leaveMap();
    }
}
