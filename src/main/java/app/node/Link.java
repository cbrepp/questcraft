package app.node;

import app.*;
import app.color.RGBColor;

/**
 *
 * @author repp
 */
public class Link extends BaseNode {
    
    public RGBColor backgroundColor; // Default (null) is transparent
    public EventListener eventListener;
    public Boolean isEnabled = true;
    public Double pixelSize; // Default is app controller's default pixel size
    public Object text;
    public RGBColor textColor; // Default (null) is either black or white depending on which color would best offset the background
    public String textFont; // Default is the app controller's default font

    public Link (String name) {
        super(name);
    }
    
    @Override
    public RGBColor getColor() {
        return this.backgroundColor;
    }
    
}
