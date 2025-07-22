package nro.models.boss.nappa;

import java.util.Calendar;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 *
 
 *
 */
public class Kuku extends FutureBoss {

    public Kuku() {
        super(BossFactory.KUKU, BossData.KUKU);
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
        this.textTalkBefore = new String[]{};
        this.textTalkMidle = new String[]{};
        this.textTalkAfter = new String[]{};
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.TENNIS_SPACE_SHIP);
        super.leaveMap();
    }
    
    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        long dame = 0;
        if (this.isDie()) {
            return dame;
        } else {
            if (Util.isTrue(1, 5) && plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.TU_SAT:
                    case Skill.QUA_CAU_KENH_KHI:
                    case Skill.MAKANKOSAPPO:
                        break;
                    default:
                        return 0;
                }
            }
            final Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(11);
            if (hour >= 14 && hour < 17) {// 0 đến 8h  sáng
                if (plAtt != null && plAtt.playerTask.taskMain.id != 19) {
                    if (plAtt.playerTask.taskMain.index != 0) {
                        if (damage >= 0) {
                            damage = 0;
                            Service.getInstance().sendThongBao(plAtt, "Bây giờ là giờ nhiệm vụ, không phải nhiệm vụ hiện tại của bạn, boss miễn nhiễm sát thương");
                        }
                    }
                }
            }
            dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (this.isDie()) {
                rewards(plAtt);
                notifyPlayeKill(plAtt);
                die();
            }
            return dame;
        }
    }

}
