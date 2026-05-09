package app.node;

import app.color.RGBColor;

/**
 *
 * @author repp
 */
public class Pane extends BaseNode {
    
    public RGBColor backgroundColor; // Default (null) is transparent
    public Integer borderWidth = 1; // Default (1) is thin borders

    public Pane (String name) {
        super(name);
    }
    
    @Override
    public RGBColor getColor() {
        return this.backgroundColor;
    }
    
}
