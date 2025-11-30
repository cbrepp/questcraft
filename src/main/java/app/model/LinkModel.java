package app.model;

import app.*;

/**
 *
 * @author repp
 */
public class LinkModel extends BaseModel {

    public LinkModel(String text, Color backgroundColor) {
        super(text, backgroundColor);
    }
    
    public LinkModel(String text, Color backgroundColor, Boolean isEnabled) {
        this(text, backgroundColor);
        this.isEnabled = isEnabled;
    }
    
}
