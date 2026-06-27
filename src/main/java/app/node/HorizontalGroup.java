package app.node;

import app.HorizontalAlignment;
import app.color.RGBColor;

/**
 *
 * @author repp
 */
public class HorizontalGroup extends Group {

    public HorizontalAlignment alignment; // Default (null) is top-left
    public int cornerRadii = 0; // Default (0px) is square corners
    public Boolean expandChild = false; // Default (null) is to not automatically expand children to fill the width and height of the group
    
    public HorizontalGroup(String name) {
        super(name);
    }
    
    @Override
    public RGBColor getColor() {
        return null;
    }
    
}
