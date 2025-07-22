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

public class Buibui_Bongtoi extends Boss {

    public Buibui_Bongtoi() {
        super(BossFactory.BUIBUI_BONGTOI, BossData.BUIBUI_BONG_TOI);
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
        textTalkMidle = new String[] { "Ác quỷ là taaa" };
        textTalkAfter = new String[] { "Sức nặng lên người !!! Ta chạy đây !!!" };
    }

}
