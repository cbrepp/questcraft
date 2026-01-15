package app.node.effect;

import app.Color;

/**
 *
 * @author repp
 */
public class Glow extends BaseEffect {
    
    public Color color; // Default (null) is the offset color
    
    public Glow(Color color) {
        this.color = color;
    }
    
}
