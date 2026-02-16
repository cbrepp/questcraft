package app.node;

import app.*;

/**
 *
 * @author repp
 */
public class Label extends BaseNode {
    
    public Color backgroundColor; // Default (null) is transparent
    public Integer borderWidth; // Default (null) is no borders
    public Double pixelSize; // Default is app controller's default pixel size
    public Object text; // toString() is invoked on the Object to get the text, allowing for dynamic text
    public Color textColor; // Default (null) is either black or white depending on which color would best offset the background
    public String textFont; // Default (null) is the app controller's default font
    public FontStyle textStyle; // Default (null) is normal

    public Label (String name) {
        super(name);
    }
    
    public Label (String name, Object text) {
        super(name);
        this.text = text;
    }
    
}
