package nro.models.boss.SieuDoanhDoan;

import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;
import nro.services.SkillService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

public class HongHoa extends Boss {

    public HongHoa() {
        super(BossFactory.HOAHONG, BossData.HONGHOA);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void idle() {
    }

    @Override

    public void rewards(Player plKill) {
        this.dropItemReward(1504, (int)plKill.id, 1);
    }

    @Override
    public synchronized void attack() {
         try {
            Player pl = getPlayerAttack();
            if (pl == null || pl.isDie() || pl.isMiniPet || pl.effectSkin.isVoHinh) {
                return;
            }
            this.playerSkill.skillSelect = this.getSkillAttack();
            if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                if (Util.isTrue(15, ConstRatio.PER100)) {
                    if (SkillUtil.isUseSkillChuong(this)) {
                        goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
                    } else {
                        goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 30)),
                                Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
                    }
                }
                checkPlayerDie(pl);
            } else {
                goToPlayer(pl, false);
            }
        } catch (Exception ex) {
            Log.error(Boss.class, ex);
        }
    }

    
    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        return super.injured(plAtt, 1, piercing, isMobAttack); 
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
    }
}
