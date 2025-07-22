package nro.models.boss.BongToi;

import nro.consts.ConstItem;
import nro.consts.ConstRatio;
import nro.jdbc.daos.PlayerDAO;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

public class HidraBula_Bongtoi extends Boss {

    public HidraBula_Bongtoi() {
        super(BossFactory.HIDRABULA_BONGTOI, BossData.HIDRABULA_BONG_TOI);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player plKill) {
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        textTalkMidle = new String[] { "Đóng băng taasttt cả" };
        textTalkAfter = new String[] { "Phọt ! Đóng băng đi , ta di đây !!!" };
    }

}
