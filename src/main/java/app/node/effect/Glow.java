package app.node.effect;

import app.color.RGBColor;

/**
 *
 * @author repp
 */
public class Glow extends BaseEffect {
    
    public RGBColor color; // Default (null) is the offset color
    
    public Glow(RGBColor color) {
        this.color = color;
    }
    
}
