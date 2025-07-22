
package nro.models.boss.Yardart;

import nro.models.boss.BossData;
import nro.models.map.Zone;

/**
 *
 * 
 */
public class TapSu extends BossYardart {

    public TapSu(Zone zoneDefault, byte bossId, int xMoveMin, int xMoveMax) throws Exception {
        super(zoneDefault, bossId, BossData.BEO_DEP_TRAI, xMoveMin, xMoveMax);
    }
}
