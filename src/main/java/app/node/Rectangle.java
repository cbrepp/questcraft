package app.node;

import app.color.RGBColor;

/**
 *
 * @author repp
 */
public class Rectangle extends BaseNode {
    
    public Double opacity; // Default (null) is 1.0 (100%) opacity
    public RGBColor color; // Default (null) is either black or white depending on which color would best offset the background
    
    public Rectangle (String name) {
        super(name);
    }
    
    @Override
    public RGBColor getColor() {
        return this.color;
    }
    
}
