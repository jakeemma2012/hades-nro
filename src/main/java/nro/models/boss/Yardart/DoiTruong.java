
package nro.models.boss.Yardart;

import nro.models.boss.BossData;
import nro.models.map.Zone;

/**
 *
 * @author Administrator
 */
public class DoiTruong extends BossYardart {

    public DoiTruong(Zone zoneDefault, byte bossId, int xMoveMin, int xMoveMax) throws Exception {
        super(zoneDefault, bossId, BossData.DOI_TRUONG, xMoveMin, xMoveMax);
    }
}
