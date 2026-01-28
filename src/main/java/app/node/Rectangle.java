package app.node;

import app.Color;

/**
 *
 * @author repp
 */
public class Rectangle extends BaseNode {
    
    public Double opacity; // Default (null) is 1.0 (100%) opacity
    public Color color; // Default (null) is either black or white depending on which color would best offset the background
    
    public Rectangle (String name) {
        super(name);
    }
    
}
