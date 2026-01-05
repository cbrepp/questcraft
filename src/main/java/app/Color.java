package app;

import java.io.Serializable;

/**
 *
 * @author repp
 */
public class Color implements Serializable {
    
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color DARK_GRAY = new Color(169, 169, 169);
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color YELLOW = new Color(255, 222, 33);
    
    public int red;
    public int green;
    public int blue;
    
    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    
    public app.Color getOffset() {
        app.Color offsetColor;
        double luminance = (0.299 * this.red) + (0.587 * this.green) + (0.114 * this.blue);
        if (luminance < 128) {
            offsetColor = new app.Color(255, 255, 255);
        } else {
            offsetColor = new app.Color(0, 0, 0);
        }
        return offsetColor;
    }
    
    @Override
    public String toString() {
        return "(" + this.red + ", " + this.green + ", " + this.blue + ")";
    }
}
