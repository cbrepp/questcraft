package app.model;

import app.*;

/**
 *
 * @author repp
 */
public class ButtonModel extends BaseModel {

    public ButtonModel(String text, Color backgroundColor) {
        super(text, backgroundColor);
    }
    
    public ButtonModel(String text, Color backgroundColor, Boolean isEnabled) {
        this(text, backgroundColor);
        this.isEnabled = isEnabled;
    }
    
}
