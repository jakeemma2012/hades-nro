package nro.models.phuban.MabuBoss;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.player.Player;
import nro.utils.Util;

/**
 *
 */
public class MabuRom extends Boss {

    public MabuRom() {
        super(BossFactory.MABU_ROM, BossData.MabuROM);
    }

    @Override
    public void rewards(Player pl) {
         if(Util.isTrue(50, 100)) {
            this.dropItemReward(568, (int)pl.id, 1);
        }
    }

    @Override
    public void idle() {

    }

    @Override
    public void leaveMap() {
        Boss mabuMap = BossManager.gI().getBossById(BossFactory.MABU_PHUBAN);
        if (mabuMap != null) {
            mabuMap.setJustRest();
        }
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{
            "Trong bóng tối, những bí ẩn dần lộ diện.",
            "Sự im lặng của bóng đêm bao trùm tất cả.",
            "Ánh sáng yếu ớt không thể xua tan bóng tối."
        };
        this.textTalkMidle = new String[]{
            "Bóng tối che khuất mọi thứ, chỉ còn lại sự u ám.",
            "Dưới màn đêm, mọi thứ trở nên mờ ảo.",
            "Bóng tối như làn sóng, vây quanh chúng ta."
        };
        this.textTalkAfter = new String[]{
            "Bóng tối đã qua, nhưng sự im lặng vẫn còn đó.",
            "Khi ánh sáng trở lại, bóng tối chỉ là một kỷ niệm.",
            "Trong bóng tối, chúng ta đã tìm thấy ánh sáng bên trong mình."
        };
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }
}
