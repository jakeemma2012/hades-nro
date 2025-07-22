package nro.models.boss.broly;

import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.EffectSkillService;
import nro.services.PetService;
import nro.services.Service;
import nro.services.SkillService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

public class SuperBroly extends Boss {

    public SuperBroly() {
        super(BossFactory.SUPER_BROLY, BossData.SUPER_BROLY);
    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        long dame = 0;
        if (this.isDie()) {
            return dame;
        } else {
            if (damage > 500000) {
                damage = 500000;
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

    @Override
    public void rewards(Player pl) {
        if(pl.pet == null){
            PetService.gI().createNormalPet(pl, pl.gender,(byte)0);
        } else {
            Service.getInstance().sendThongBao(pl, "Đã có Đệ tử rồi còn ăn ... THAM THẾ !!!");
        }
    }

    public SuperBroly(byte id, BossData data) {
        super(id, data);
        this.nPoint.defg = (short) (this.nPoint.hpg / 1000);
        if (this.nPoint.defg < 0) {
            this.nPoint.defg = (short) -this.nPoint.defg;
        }
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
        this.textTalkAfter = new String[]{"Các ngươi chờ đấy, ta sẽ quay lại sau"};
    }

}
