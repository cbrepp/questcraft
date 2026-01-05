package app.control;

import app.*;

/**
 *
 * @author repp
 */
public class BaseControl {
    
    public Color backgroundColor; // Default (null) is transparent
    public Boolean isEnabled = true;
    public Layout layout;
    public String name;
    public Double pixelSize = 14.0;
    public String text;
    
    public BaseControl() {}
    
    public BaseControl(String name, Layout layout) {
        this.name = name;
        this.layout = layout;
    }
    
}
