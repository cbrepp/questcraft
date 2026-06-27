package app.dialog;

import app.*;

/**
 *
 * @author repp
 */
public class InternalFileSelection extends BaseDialog {
    
    public String emoji; // default (null) is the question mark emoji
    public EventListener eventListener;
    public String eventName;
    public String path; // ie, "/assets"

    public InternalFileSelection () {
        super();
    }
    
    public InternalFileSelection (String title) {
        super(title);
    }
    
}
