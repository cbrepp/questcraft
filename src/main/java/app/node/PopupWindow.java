package app.node;

import app.color.RGBColor;

/**
 *
 * @author repp
 */
public class PopupWindow extends BaseNode {
    
    public RGBColor backgroundColor; // Default (null) is transparent
    public int cornerRadii = 0; // Default (zero) is no rounded corners
    
    public PopupWindow (String name) {
        super(name);
    }

    public PopupWindow (String name, RGBColor backgroundColor) {
        this(name);
        this.backgroundColor = backgroundColor;
    }

    @Override
    public RGBColor getColor() {
        return this.backgroundColor;
    }
    
    @Override
    public boolean isParent() {
        return true;
    }
        
}
