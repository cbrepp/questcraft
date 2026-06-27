package app.node;

import app.color.RGBColor;

/**
 *
 * @author repp
 */
public class VerticalGroup extends Group {
    
    public int cornerRadii = 0; // Default (0px) is square corners
    public Boolean expandChild = false; // Default (null) is to not automatically expand children to fill the width and height of the group
    
    public VerticalGroup(String name) {
        super(name);
    }
    
    @Override
    public RGBColor getColor() {
        return null;
    }
    
}
