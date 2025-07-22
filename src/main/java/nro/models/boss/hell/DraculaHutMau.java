package nro.models.boss.hell;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.item.Item;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Util;

public class DraculaHutMau extends Boss {

    public DraculaHutMau() {
        super(BossFactory.DRACULA_HUT_MAU, BossData.DRACULA_HUT_MAU);
    }

    protected DraculaHutMau(byte id, BossData bossData) {
        super(id, bossData);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player plKill) {
        if(!generalRewards(plKill, 13, 25)) {
            baseRewards(plKill, 5, 15, 9);
        }
    }

    @Override
    public void initTalk() {
        this.textTalkAfter = new String[]{"Các ngươi chờ đấy, ta sẽ quay lại sau"};
    }

    @Override
    public void idle() {
        if (this.countIdle >= this.maxIdle) {
            this.maxIdle = Util.nextInt(0, 3);
            this.countIdle = 0;
            this.changeAttack();
        } else {
            this.countIdle++;
        }
    }

    @Override
    public void checkPlayerDie(Player pl) {
        if (pl.isDie()) {
            Service.getInstance().chat(this, "Chừa nha " + plAttack.name + " động vào ta chỉ có chết.");
            this.plAttack = null;
        }
    }
}
