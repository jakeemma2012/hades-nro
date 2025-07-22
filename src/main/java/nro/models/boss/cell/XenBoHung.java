package nro.models.boss.cell;

import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.PlayerService;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

public class XenBoHung extends FutureBoss {

    public XenBoHung() {
        super(BossFactory.XEN_BO_HUNG, BossData.XEN_BO_HUNG);
    }

    @Override
    public void rewards(Player plKill) {

        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    protected boolean useSpecialSkill() {
        this.playerSkill.skillSelect = this.getSkillSpecial();
        if (SkillService.gI().canUseSkillWithCooldown(this)) {
            SkillService.gI().useSkill(this, null, null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void attack() {
        if (this.isDie()) {
            die();
            return;
        }
        try {
            Player pl = getPlayerAttack();
            if (pl != null) {
                if (!useSpecialSkill()) {
                    this.playerSkill.skillSelect = this.getSkillAttack();
                    if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                            goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50),
                                    false);
                        }
                        SkillService.gI().useSkill(this, pl, null);
                        checkPlayerDie(pl);
                    } else {
                        goToPlayer(pl, false);
                    }
                }
            }
        } catch (Exception ex) {
            Log.error(XenBoHung.class, ex);
        }
    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (this.isDie() || this.playerSkill.prepareTuSat) {
            return 0;
        } else {
            long dame = super.injuredNotCheckDie(plAtt, damage, piercing);
            if (this.isDie()) {
                rewards(plAtt);
            }
            return dame;
        }
    }

    @Override
    public void joinMap() {
        if (BossManager.gI().getBossById(BossFactory.SIEU_BO_HUNG) == null) {
            BossFactory.createBoss(BossFactory.XEN_CON);
            super.joinMap();
        }
    }

    @Override
    public void idle() {
        if (BossManager.gI().getBossById(BossFactory.XEN_CON) == null) {
            PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_ALL);
            changeAttack();
            // 准确消息？
        }
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[] { "Ta cho các ngươi 5 giây để chuẩn bị", "Cuộc chơi bắt đầu.." };
        this.textTalkMidle = new String[] { "Kame Kame Haaaaa!!", "Mi khá đấy nhưng so với ta chỉ là hạng tôm tép",
                "Tất cả nhào vô hết đi", "Cứ chưởng tiếp đi. haha", "Các ngươi yếu thế này sao hạ được ta đây. haha",
                "Khi công pháo!!", "Cho mi biết sự lợi hại của ta" };
        this.textTalkAfter = new String[] {};
    }

    @Override
    public void leaveMap() {
        Boss sieuBoHung = BossFactory.createBoss(BossFactory.SIEU_BO_HUNG);
        sieuBoHung.zone = this.zone;
        super.leaveMap();
        this.changeToIdle();
    }
}
