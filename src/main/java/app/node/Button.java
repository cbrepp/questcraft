package app.node;

import app.*;

/**
 *
 * @author repp
 */
public class Button extends BaseNode {
    
    public Color backgroundColor; // Default (null) is system default
    public Integer borderWidth; // Default (null) is no borders
    public EventListener eventListener;
    public Object eventName; // Default (null) is the button's name
    public Boolean isEnabled = true;
    public Boolean isMultiUse = true;
    public KeyboardKey keyBinding; // Default (null) is no keyboard binding
    public Double pixelSize; // Default is app controller's default pixel size
    public Object text;
    public Color textColor; // Default (null) is either black or white depending on which color would best offset the background
    public String textFont; // Default is the app controller's default font

    public Button (String name) {
        super(name);
    }
    
}
