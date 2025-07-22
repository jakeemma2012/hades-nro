
package nro.models;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Kitak
 */
@Getter
@Setter
public class Part {

    private short id;

    private byte type;

    private PartImage[] pi;

    public short getIcon(int index) {
        return pi[index].getIcon();
    }
}
