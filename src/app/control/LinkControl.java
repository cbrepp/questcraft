package app.control;

import app.*;

/**
 *
 * @author repp
 */
public class LinkControl extends BaseControl {

    public LinkControl(String text, Color backgroundColor) {
        super(text, backgroundColor);
    }
    
    public LinkControl(String text, Color backgroundColor, Boolean isEnabled) {
        this(text, backgroundColor);
        this.isEnabled = isEnabled;
    }
    
}
