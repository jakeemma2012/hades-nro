package nro.models.boss.SieuDoanhDoan;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;
import nro.services.Service;
import nro.utils.Util;

public class Khi6 extends Boss {

    public Khi6() {
        super(BossFactory.KHI_6, BossData.Khi_6);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void idle() {
    }

    @Override

    public void rewards(Player plKill) {
        if (Util.isTrue(50, 100)) {
            plKill.pointBossVip += 1;
            Service.getInstance().chat(plKill,
                    "HAHAHA Ta đã tiêu diệt được " + this.name + ", nhận được 1 điểm Boss VIP ");

        } else {
            plKill.pointBossThuong += 1;
            Service.getInstance().chat(plKill,
                    "HAHAHA Ta đã tiêu diệt được " + this.name + ", nhận được 1 điểm Boss Thường ");

        }
    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (damage >= 3_000_000) {
            damage = 3_000_000;
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[] {};
        this.textTalkMidle = new String[] { "Kame Kame Haaaaa!!", "Mi khá đấy nhưng so với ta chỉ là hạng tôm tép",
                "Tất cả nhào vô hết đi", "Cứ chưởng tiếp đi. haha", "Các ngươi yếu thế này sao hạ được ta đây. haha",
                "Khi công pháo!!", "Cho mi biết sự lợi hại của ta" };
        this.textTalkAfter = new String[] { "Các ngươi được lắm", "Hãy đợi đấy thời gian tới ta sẽ quay lại.." };
    }
}
