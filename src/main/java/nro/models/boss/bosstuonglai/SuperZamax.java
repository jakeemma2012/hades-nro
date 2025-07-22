package nro.models.boss.bosstuonglai;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.player.Player;

import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;

public class SuperZamax extends Boss {

    public SuperZamax() {
        super(BossFactory.ZAMASU2, BossData.SUPER_ZAMAX);
    }

    @Override
    public void rewards(Player pl) {
    }

    @Override
    public void idle() {
    }

    @Override
    public void checkPlayerDie(Player pl) {
    }

    @Override
    public void joinMap() {
    }

    @Override
    public void initTalk() {
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void leaveMap() {
        Boss blackGoku = null;

        switch (this.zone.map.mapId) {
            case 92:
                blackGoku = BossManager.gI().getBossById(BossFactory.BLACKGOKU);
                break;
            case 93:
                blackGoku = BossManager.gI().getBossById(BossFactory.BLACKGOKU_1);
                break;
            case 94:
                blackGoku = BossManager.gI().getBossById(BossFactory.BLACKGOKU_2);
                break;
        }

        if (blackGoku != null) {
            blackGoku.setJustRest();
        }

        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
