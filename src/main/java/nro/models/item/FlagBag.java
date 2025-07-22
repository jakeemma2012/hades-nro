
package nro.models.item;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kitak
 */
public class FlagBag {

    public int id;
    public short iconId;
    public short[] iconEffect;
    public String name;
    public int gold;
    public int gem;
    public String description;
    public List<ItemOption> itemoptions = new ArrayList<>();

}
