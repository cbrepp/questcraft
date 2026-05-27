package app.node;

import app.color.RGBColor;

/**
 *
 * @author repp
 */
public class Rectangle extends BaseNode {
    
    public RGBColor color; // Default (null) is either black or white depending on which color would best offset the background
    
    public Rectangle (String name) {
        super(name);
    }
    
    @Override
    public RGBColor getColor() {
        return this.color;
    }
    
}
