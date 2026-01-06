package app.control;

import app.*;

/**
 *
 * @author repp
 */
public class ButtonControl extends BaseControl {
    
    public EventListener eventListener;
    public String eventName;

    public ButtonControl(String name, Layout layout) {
        this.name = name;
        this.layout = layout;
    }
    
}
