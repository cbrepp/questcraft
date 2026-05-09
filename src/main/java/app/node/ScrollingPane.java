package app.node;

import app.color.RGBColor;

/**
 *
 * @author repp
 */
public class ScrollingPane extends BaseNode {
    
    public RGBColor backgroundColor; // Default (null) is transparent
    public int borderWidth = 1; // Default (1) is thin borders

    public ScrollingPane (String name) {
        super(name);
    }
    
    @Override
    public RGBColor getColor() {
        return this.backgroundColor;
    }
    
}
