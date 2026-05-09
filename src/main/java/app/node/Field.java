package app.node;

import app.*;
import app.color.RGBColor;

/**
 *
 * @author repp
 */
public class Field extends BaseNode {
    
    public RGBColor backgroundColor; // Default (null) is transparent
    public Integer borderWidth = 1; // Default is a 1 pixel border
    public Integer displayLength;
    public EventListener eventListener;
    public String eventName;
    public String initialValue;
    public Boolean isEnabled = true;
    public Boolean isUpperCase = false; // Default (false) is don't force upper case
    public String label;
    public Integer length; // Default (null) is system default
    public Double pixelSize; // Default is app controller's default pixel size
    public RGBColor textColor; // Default (null) is either black or white depending on which color would best offset the background
    public String textFont; // Default is the app controller's default font
    public FontStyle textStyle; // Default (null) is normal

    public Field (String name) {
        super(name);
    }
    
    @Override
    public RGBColor getColor() {
        return this.backgroundColor;
    }
    
}
