package app.control;

import app.*;

/**
 *
 * @author repp
 */
public class LinkControl extends BaseControl {
    
    public EventListener eventListener;
    public String eventName;

    public LinkControl(String name, Layout layout) {
        this.name = name;
        this.layout = layout;
    }
    
}
