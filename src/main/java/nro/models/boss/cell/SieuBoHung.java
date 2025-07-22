package nro.models.boss.cell;

import nro.consts.ConstRatio;
import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;


public class SieuBoHung extends FutureBoss {

    public static boolean tuSat;

    public SieuBoHung() {
        super(BossFactory.SIEU_BO_HUNG, BossData.SIEU_BO_HUNG);
    }

    @Override
      public void rewards(Player plKill) {
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
     generalRewardsOmega(plKill, 12, 101,10);
        if (Util.isTrue(50, 100)) {
            plKill.pointBossVip += 1;
            Service.getInstance().chat(plKill, "HAHAHA Ta đã tiêu diệt được " + this.name + ", nhận được 1 điểm Boss thường ");
        } else {
            plKill.pointBossThuong += 1;
            Service.getInstance().chat(plKill, "HAHAHA Ta đã tiêu diệt được " + this.name + ", nhận được 1 điểm Boss thường VIP");

        }
    }
    
    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void attack() {
        try {
            Player pl = getPlayerAttack();
            if (pl != null) {
                this.playerSkill.skillSelect = this.getSkillAttack();
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                        goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
                    }
                    SkillService.gI().useSkill(this, pl, null);
                    checkPlayerDie(pl);
                } else {
                    goToPlayer(pl, false);
                }
            }
        } catch (Exception ex) {
            Log.error(SieuBoHung.class, ex);
        }
    }

    @Override
    public void leaveMap() {
        BossManager.gI().getBossById(BossFactory.XEN_BO_HUNG).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void joinMap() {
        int x = Util.nextInt(50, this.zone.map.mapWidth - 50);
        ChangeMapService.gI().changeMap(this, this.zone, x, this.zone.map.yPhysicInTop(x, 0));
        ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
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
        this.textTalkMidle = new String[]{"Kame Kame Haaaaa!!", "Mi khá đấy nhưng so với ta chỉ là hạng tôm tép",
            "Tất cả nhào vô hết đi", "Cứ chưởng tiếp đi. haha", "Các ngươi yếu thế này sao hạ được ta đây. haha",
            "Khi công pháo!!", "Cho mi biết sự lợi hại của ta"};
        this.textTalkAfter = new String[]{"Các ngươi được lắm", "Hãy đợi đấy thời gian tới ta sẽ quay lại.."};
    }

}
