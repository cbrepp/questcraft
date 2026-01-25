package app.node;

import app.*;

/**
 *
 * @author repp
 */
public class Field extends BaseNode {
    
    public Color backgroundColor; // Default (null) is transparent
    public EventListener eventListener;
    public String eventName;
    public String initialValue;
    public Boolean isEnabled = true;
    public Boolean isUpperCase = false; // Default (false) is don't force upper case
    public String label;
    public Integer length; // Default (null) is system default
    public Double pixelSize; // Default is app controller's default pixel size
    public Color textColor; // Default (null) is either black or white depending on which color would best offset the background
    public String textFont; // Default is the app controller's default font
    public FontStyle textStyle; // Default (null) is normal

    public Field (String name) {
        super(name);
    }
    
}
