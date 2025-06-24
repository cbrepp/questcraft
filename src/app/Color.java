package app;

import java.io.Serializable;

/**
 *
 * @author repp
 */
public class Color implements Serializable {
    
    public int red;
    public int green;
    public int blue;
    
    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
}
