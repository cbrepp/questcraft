package app;

import java.io.Serializable;

/**
 *
 * @author repp
 */
public class Text implements Serializable {
    
    public Object text; // toString() is invoked on the Object to get the text, allowing for dynamic text
    public TextDecoration decoration; // Default (null) is to use the view's defaults
    
    public Text(Object text, TextDecoration decoration) {
        this.text = text;
        this.decoration = decoration;
    }

    public Text(Object text) {
        this(text, null);
    }
    
}
