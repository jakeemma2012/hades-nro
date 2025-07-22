
package nro.models;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Kitak
 */
@Getter
@Setter
public class PartImage {

    private short icon;

    private byte dx;

    private byte dy;

    public PartImage(short icon, byte dx, byte dy) {
        this.icon = icon;
        this.dx = dx;
        this.dy = dy;
    }
}
