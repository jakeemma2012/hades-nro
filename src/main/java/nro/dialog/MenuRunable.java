package nro.dialog;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Setter
@Getter
public abstract class MenuRunable implements Runnable {

    private int indexSelected;
}
