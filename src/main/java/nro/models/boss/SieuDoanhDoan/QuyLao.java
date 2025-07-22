package nro.models.boss.SieuDoanhDoan;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;
import nro.services.Service;

public class QuyLao extends Boss {

    public QuyLao() {
        super(BossFactory.QUY_LAO, BossData.QUY_LAO);
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
    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (damage >= 30_000_000) {
            damage = 30_000_000;
        }
        return super.injured(plAtt, damage, piercing, isMobAttack); // Generated from
                                                                    // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
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
