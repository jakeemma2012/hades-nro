
package nro.models.boss.baconsoi;

import java.util.Random;
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
public class Lavender extends FutureBoss {

    public Lavender() {
        super(BossFactory.LAVENDER, BossData.LAVENDER);
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

    }

    @Override
    public void leaveMap() {
        BossFactory.createBoss(BossFactory.BASIL).setJustRest();
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
