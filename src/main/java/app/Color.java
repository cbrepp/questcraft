package app;

import java.io.Serializable;

/**
 *
 * @author repp
 */
public class Color implements Serializable {
    
    public static final Color BLACK = new Color(0, 0, 0, 1.0);
    public static final Color BLUE = new Color (0, 0, 255, 1.0);
    public static final Color CHARCOAL = new Color(74, 74, 74, 1.0);
    public static final Color DARK_GRAY = new Color(169, 169, 169, 1.0);
    public static final Color DARK_MAGENTA = new Color(139, 0, 139, 1.0);
    public static final Color DARKEST_MAGENTA = new Color(84, 0, 84, 1.0);
    public static final Color GREEN = new Color (0, 255, 0, 1.0);
    public static final Color HYPERLINK_BLUE = new app.Color(0, 0, 238, 1.0); // Default blue used by modern browsers
    public static final Color HYPERLINK_LIGHT_BLUE = new app.Color(0, 102, 255, 1.0); // Contrast Ratio on Black: 4.7:1 (meets WCAG AA for normal text)
    public static final Color RED = new Color (255, 0, 0, 1.0);
    public static final Color SHADOW = new Color(54, 54, 54, 1.0);
    public static final Color WHITE = new Color(255, 255, 255, 1.0);
    public static final Color YELLOW = new Color(255, 222, 33, 1.0);
    
    public final int red;
    public final int green;
    public final int blue;
    public final double opacity;
    
    public Color(int red, int green, int blue, double opacity) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.opacity = opacity;
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
