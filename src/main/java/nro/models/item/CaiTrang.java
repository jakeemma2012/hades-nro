
package nro.models.item;

/**
 *
 * 
 */
public class CaiTrang {

    public int tempId;
    /**
     * head, body, leg, bag
     */
    public int[] id;

    public CaiTrang(int tempId, int... id) {
        this.tempId = tempId;
        this.id = id;
    }

    public int[] getID() {
        return id;
    }
}
