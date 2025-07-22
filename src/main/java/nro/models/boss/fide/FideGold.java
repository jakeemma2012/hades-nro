package nro.models.boss.fide;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.ItemService;
import nro.services.Service;

public class FideGold extends Boss {

    public FideGold() {
        super(BossFactory.FIDEGOLD, BossData.FIDEGOLD);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        for (int i = 0; i < 5; i++) {
            ItemMap itemMap = null;
            int x = this.location.x + (14 * i);
            if (x < 0 || x >= this.zone.map.mapWidth) {
                break;
            }
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
            itemMap = new ItemMap(this.zone, 380, 1, x, y, -1);
            Service.getInstance().dropItemMap(zone, itemMap);
        }

        for (int i = 0; i < 5; i++) {
            ItemMap itemMap = null;
            int x = this.location.x + (14 * i);
            if (x < 0 || x >= this.zone.map.mapWidth) {
                break;
            }
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
            itemMap = new ItemMap(this.zone, 457, 1, x, y, -1);
            Service.getInstance().dropItemMap(zone, itemMap);
        }

    }

    @Override
    public void idle() {

    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        return super.injured(plAtt, 1, piercing, isMobAttack);
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkMidle = new String[]{"Oải rồi hả?", "Ê cố lên nhóc",
            "Chán", "Ta có nhầm không nhỉ"};

    }

}
