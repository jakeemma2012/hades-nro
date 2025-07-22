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

public class Yacon_Bongtoi extends Boss {

    public Yacon_Bongtoi() {
        super(BossFactory.YACON_BONGTOI, BossData.YACON_BONG_TOI);
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
        textTalkMidle = new String[] { "Các người tìm thấy ta ?" };
        textTalkAfter = new String[] { "Ẩn thân chi thuật ! Hãy đợi đấy !!!" };
    }

}
