package nro.models.boss.SieuDoanhDoan;

import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.mob.Mob;
import nro.models.phuban.MabuBoss.PhubanMabu;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.func.ChangeMapService;

public class SieuOmega extends FutureBoss {

    public SieuOmega() {
        super(BossFactory.OMEGA_PLUS, BossData.OMEGA_PLUS);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player plKill) {
         generalRewardsOmega(plKill, 12, 101,10);
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {
        
    }

    @Override
    public void joinMap() {
        super.joinMap();
        for (Mob m : this.zone.mobs) {
            m.point.hp -= 100_000;
            m.setDie();
        }
        
        PhubanMabu.gI().isAlive = true;
        
        for (Player p : this.zone.getPlayers()) {
            ChangeMapService.gI().changeMapBySpaceShip(p, p.gender + 21, -1, -1);
            Service.getInstance().sendThongBao(p, "Chiến tranh sắp đổ bộ, phi thuyền sẽ chở bạn về nhà !");
        }
    }

    @Override
    public void initTalk() {
    }

    @Override
    public void leaveMap() {
        PhubanMabu.gI().isAlive = false;
        super.leaveMap();
    }

}
