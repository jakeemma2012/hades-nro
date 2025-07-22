
package nro.models.boss.baconsoi;

import java.util.Random;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 *
 * @author Administrator
 */
public class Bergamo extends FutureBoss {

    public Bergamo() {
        super(BossFactory.BERGAMO, BossData.BERAMO);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player plKill) {
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
        if (!generalRewards(plKill, 13, 25)) {
            baseRewards(plKill, 15, 25, 6);
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
        this.textTalkMidle = new String[] { "Xem bản lĩnh của ngươi như nào đã", "Các ngươi tới số mới gặp phải ta" };
        this.textTalkAfter = new String[] { "Ác quỷ biến hình, hêy aaa......." };
    }

    @Override
    public void leaveMap() {
        Boss fd3 = BossFactory.createBoss(BossFactory.LAVENDER);
        fd3.zone = this.zone;
        fd3.location.x = this.location.x;
        fd3.location.y = this.location.y;
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void joinMap() {
        if (this.zone != null) {
            ChangeMapService.gI().changeMap(this, zone, this.location.x, this.location.y);
            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        }
    }
}
