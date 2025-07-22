
package nro.models.boss.Yardart;

import nro.models.boss.BossData;
import nro.models.map.Zone;

/**
 *
 * 
 */
public class TanBinh extends BossYardart {

    public TanBinh(Zone zoneDefault, byte bossId, int xMoveMin, int xMoveMax) throws Exception {
        super(zoneDefault, bossId, BossData.PUT_NGU, xMoveMin, xMoveMax);
    }
}
