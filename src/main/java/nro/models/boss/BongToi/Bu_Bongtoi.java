package nro.models.boss.BongToi;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;
import nro.utils.Util;

public class Bu_Bongtoi extends Boss {

    public Bu_Bongtoi() {
        super(BossFactory.MABU_BONGTOI, BossData.BUBONG_TOI);
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
        textTalkMidle = new String[] { "Bubuuuuuu" };
        textTalkAfter = new String[] { "Các ngươi hãy đợi biến thành SUCULU điiii !" };
    }

}
