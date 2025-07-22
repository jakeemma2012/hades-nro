package nro.models.boss.baconsoi;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;
import nro.services.TaskService;

public class Basil extends FutureBoss {

    public Basil() {
        super(BossFactory.BASIL, BossData.BASIL);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player plKill) {
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
        if(!generalRewards(plKill, 12, 25)) {
            baseRewards(plKill, 10, 20, 6);
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
        this.textTalkMidle = new String[]{"Xem bản lĩnh của ngươi như nào đã", "Các ngươi tới số mới gặp phải ta"};
        this.textTalkAfter = new String[]{"Ác quỷ biến hình, hêy aaa......."};
    }

    @Override
    public void leaveMap() {
        Boss fd2 = BossFactory.createBoss(BossFactory.BERGAMO);
        fd2.zone = this.zone;
        fd2.location.x = this.location.x;
        fd2.location.y = this.location.y;
        super.leaveMap();
        this.setJustRestToFuture();
    }

}
