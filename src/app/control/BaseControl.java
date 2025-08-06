package app.control;

import app.*;

/**
 *
 * @author repp
 */
public class BaseControl {
    
    public Color backgroundColor;
    public Boolean isEnabled = true;
    public String text;
    
    public BaseControl(String text, Color backgroundColor) {
        this.text = text;
        this.backgroundColor = backgroundColor;
    }
    
}
