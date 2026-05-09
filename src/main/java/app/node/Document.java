package app.node;

import app.color.RGBColor;

/**
 * Lays out its children horizontally (left to right) and wraps them to a new row once they hit the width boundary
 * 
 * @author repp
 */
public class Document extends Group {
    
    public Document(String name) {
        super(name);
    }
    
    @Override
    public RGBColor getColor() {
        return null;
    }
    
}
