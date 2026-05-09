package app.node;

import app.color.RGBColor;

/**
 *
 * @author repp
 */
public class Image extends BaseNode {
    
    public String file;
    
    public Image (String name) {
        super(name);
    }
    
    public Image (String name, String file) {
        super(name);
        this.file = file;
    }
    
    @Override
    public RGBColor getColor() {
        return null;
    }
    
}
