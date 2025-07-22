package nro.models.boss.cereal;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.services.func.ChangeMapService;

public class Elec extends FutureBoss {

    public Elec() {
        super(BossFactory.ELEC, BossData.ELEC);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        getRewardGranola(this, pl, 100, 100, 1);
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
    }

    @Override
    public void joinMap() {

        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[0]);
        }
        if (this.zone != null) {
            BossFactory.createBoss(BossFactory.GAS).zone = this.zone;
            BossFactory.createBoss(BossFactory.OLI).zone = this.zone;
            BossFactory.createBoss(BossFactory.MACKIE).zone = this.zone;

            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP);

            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);

            System.out.println(
                    "Boss: " + this.name + " xuất hiện mapId: " + this.zone.map.mapId + " zone: " + this.zone.zoneId);
        }
    }

    @Override
    public void leaveMap() {
        Boss Gas = this.zone.getBossInMap(BossFactory.GAS);
        if (Gas != null && !Gas.isDie()) {
            Gas.changeToAttack();
        }

        BossManager.gI().removeBoss(this);
        super.leaveMap();
    }

}
