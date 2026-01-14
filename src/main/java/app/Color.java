package app;

import java.io.Serializable;

/**
 *
 * @author repp
 */
public class Color implements Serializable {
    
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color CHARCOAL = new Color(74, 74, 74);
    public static final Color DARK_GRAY = new Color(169, 169, 169);
    public static final Color DARK_MAGENTA = new Color(139, 0, 139);
    public static final Color HYPERLINK_BLUE = new app.Color(0, 0, 238); // Default blue used by modern browsers
    public static final Color HYPERLINK_LIGHT_BLUE = new app.Color(0, 102, 255); // Contrast Ratio on Black: 4.7:1 (meets WCAG AA for normal text)
    public static final Color SHADOW = new Color(54, 54, 54);
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
            offsetColor = WHITE;
        } else {
            offsetColor = BLACK;
        }
        return offsetColor;
    }
    
    @Override
    public String toString() {
        return "(" + this.red + ", " + this.green + ", " + this.blue + ")";
    }
}
