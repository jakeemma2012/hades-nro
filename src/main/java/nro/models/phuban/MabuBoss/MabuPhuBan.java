package nro.models.phuban.MabuBoss;

import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.services.SkillService;
import nro.services.func.ChangeMapService;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 *
 */
public class MabuPhuBan extends Boss {

    public MabuPhuBan() {
        super(BossFactory.MABU_PHUBAN, BossData.MABUPHUBAN);
    }

    @Override
    public void rewards(Player pl) {
        if (Util.isTrue(50, 100)) {
            this.dropItemReward(568, (int) pl.id, 1);
        }
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if(damage >= 5_000_000){
            damage = 5_000_000;
        }
        return super.injured(plAtt, damage, piercing, isMobAttack); 
    }

    
    @Override
    public void leaveMap() {
        Boss mabuGay = BossManager.gI().getBossById(BossFactory.MABU_ROM);
        if (mabuGay != null) {
            mabuGay.changeToAttack();
        }
        super.leaveMap();
        this.setJustRestToFuture();
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
    public void joinMap() {
        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
        if (this.zone != null) {
            BossFactory.createBoss(BossFactory.MABU_ROM).zone = this.zone;
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP);
            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        }
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }
}
