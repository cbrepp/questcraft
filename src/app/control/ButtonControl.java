package app.control;

import app.*;

/**
 *
 * @author repp
 */
public class ButtonControl extends BaseControl {

    public ButtonControl(String text, Color backgroundColor) {
        super(text, backgroundColor);
    }
    
    public ButtonControl(String text, Color backgroundColor, Boolean isEnabled) {
        this(text, backgroundColor);
        this.isEnabled = isEnabled;
    }
    
}
