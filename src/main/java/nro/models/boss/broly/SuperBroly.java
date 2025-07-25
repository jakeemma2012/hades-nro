package nro.models.boss.broly;

import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.EffectSkillService;
import nro.services.PetService;
import nro.services.PlayerService;
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
        int mstChuong = this.nPoint.mstChuong;
        if (!this.isDie()) {
            if (mstChuong > 0 && SkillUtil.isUseSkillChuong(plAtt)) {
                PlayerService.gI().hoiPhuc(this, 0, damage * mstChuong / 100);
                damage = 0;
            }
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);

            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (!piercing) {
                if ((plAtt.playerSkill.skillSelect.template.id == Skill.ANTOMIC || plAtt.playerSkill.skillSelect.template.id == Skill.KAMEJOKO || plAtt.playerSkill.skillSelect.template.id == Skill.MASENKO)) {
                    Service.getInstance().chat(plAtt, "Trời ơi, chưởng hoàn toàn vô hiệu lực với hắn..");
                    damage = 0;
                }
                if (damage >= this.nPoint.hpMax / 100) {
                    damage = this.nPoint.hpMax / 100;
                }
            }

            if (plAtt != null && plAtt.getSession() != null && plAtt.isAdmin()) {
                damage = nPoint.hpMax;
            }

            this.nPoint.subHP(damage);

            if (isDie()) {
                setDie(plAtt);
                die();
            }
            return damage;
        } else {
            return 0;
        }
    }

    @Override
    public void leaveMap() {
        BossFactory.createBoss(BossFactory.BROLY);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
    @Override
    public void rewards(Player pl) {
        if(pl.pet == null){
            PetService.gI().createNormalPet(pl, pl.gender,(byte)0);
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
