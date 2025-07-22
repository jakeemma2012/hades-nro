
package nro.models.item;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kitak
 */
public class ItemLuckyRound {

    public ItemTemplate temp;
    public int ratio;
    public int typeRatio;
    public double percent;
    public List<ItemOptionLuckyRound> itemOptions;

    public ItemLuckyRound() {
        this.itemOptions = new ArrayList<>();
    }
}
