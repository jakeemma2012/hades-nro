
package nro.models.boss.Yardart;

import nro.models.boss.BossData;
import nro.models.map.Zone;

/**
 *
 * 
 */
public class ChienBinh extends BossYardart {

    public ChienBinh(Zone zoneDefault, byte bossId, int xMoveMin, int xMoveMax) throws Exception {
        super(zoneDefault, bossId, BossData.CHIEN_BINH, xMoveMin, xMoveMax);
    }
}
