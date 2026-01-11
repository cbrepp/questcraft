package app.node;

import app.*;

/**
 *
 * @author repp
 */
public class Button extends BaseNode {
    
    public Color backgroundColor; // Default (null) is transparent
    public EventListener eventListener;
    public String eventName;
    public Boolean isEnabled = true;
    public Boolean isMultiUse = true;
    public Double pixelSize; // Default is app controller's default pixel size
    public String text;
    public Color textColor; // Default (null) is either black or white depending on which color would best offset the background
    public String textFont; // Default is the app controller's default font

    public Button (String name) {
        super(name);
    }
    
    public Button(String name, Layout layout) {
        super(name, layout);
    }
    
}
