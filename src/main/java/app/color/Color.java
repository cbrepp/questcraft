package app.color;

import java.io.Serializable;

/**
 *
 * @author repp
 */
public class Color implements RGBColor, Serializable {
    
    public static final Double DEFAULT_OPACITY = 1.0;
    public static final Double MAX_LUMINANCE = 1.0;
    public static final Double MIN_LUMINANCE = 0.0;
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color BLUE = new Color (0, 0, 255);
    public static final Color CHARCOAL = new Color(74, 74, 74);
    public static final Color DARK_GRAY = new Color(169, 169, 169);
    public static final Color DARK_MAGENTA = new Color(139, 0, 139); // Premium/Muted (Deep Magenta)
    public static final Color DARKEST_MAGENTA = new Color(84, 0, 84);
    public static final Color GREEN = new Color (0, 255, 0);
    public static final Color HYPERLINK_BLUE = new Color(0, 0, 238); // Default blue used by modern browsers
    public static final Color HYPERLINK_LIGHT_BLUE = new Color(0, 102, 255); // Contrast Ratio on Black: 4.7:1 (meets WCAG AA for normal text)
    public static final Color MODERN_MAGENTA = new Color(253, 61, 181); // Modern UI (Balanced/Soft)
    public static final Color NEON_MAGENTA = new Color(255, 0, 255); // High-Contrast (Vibrant/Neon)
    public static final Color RED = new Color (255, 0, 0);
    public static final Color SHADOW = new Color(54, 54, 54);
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color YELLOW = new Color(255, 222, 33);
    
    private final Integer red;
    private final Integer green;
    private final Integer blue;
    private final Double opacity;
    
    public Color(Integer red, Integer green, Integer blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.opacity = DEFAULT_OPACITY;
    }
    
    public Color(Integer red, Integer green, Integer blue, Double opacity) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.opacity = opacity;
    }
    
    @Override
    public Integer getRed() {
        return this.red;
    }
    
    @Override
    public Integer getGreen() {
        return this.green;
    }
    
    @Override
    public Integer getBlue() {
        return this.blue;
    }
    
    @Override
    public Double getLuminance() {
        // Legacy ITU-R BT.601 standard:
        // Double luminance = (0.299 * this.getRed()) + (0.587 * this.getGreen()) + (0.114 * this.getBlue());
        
        // *** ITU-R BT.709 standard ***
        
        // Normalize 0-255 to 0.0-1.0
        Double rs = this.getRed() / 255.0;
        Double gs = this.getGreen() / 255.0;
        Double bs = this.getBlue() / 255.0;

        // Linearize the channels
        rs = linearize(rs);
        gs = linearize(gs);
        bs = linearize(bs);

        // Apply coefficients for sRGB
        Double luminance = 0.2126 * rs + 0.7152 * gs + 0.0722 * bs;
        
        return luminance;
    }
    
    @Override
    public Double getOpacity() {
        return this.opacity;
    }
    
    @Override
    public Boolean isClosed() {
        return true;
    }
    
    @Override
    public String toString() {
        return "(" + this.getRed() + ", " + this.getGreen() + ", " + this.getBlue() + ", " + this.getOpacity() + ")";
    }
    
    private static Double linearize(Double colorChannel) {
        if (colorChannel <= 0.04045) {
            return colorChannel / 12.92;
        }
        
        return Math.pow((colorChannel + 0.055) / 1.055, 2.4);
    }
        
}
