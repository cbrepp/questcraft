package app.node;

import app.EventListener;
import app.Layout;
import app.color.RGBColor;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author repp
 */
public class Dialog extends BaseNode {
    
    public Map<? extends BaseNode, Layout> children = new HashMap();
    public EventListener eventListener;
    public Object eventName; // Default (null) is the button's name
    public String headerText;
    public String title;
    
    public Dialog (String name) {
        super(name);
    }
    
    public Dialog (String name, String title, String headerText, Map<? extends BaseNode, Layout> children) {
        super(name);
        this.title = title;
        this.headerText = headerText;
        this.children = children;
    }

    @Override
    public RGBColor getColor() {
        return null;
    }
    
    @Override
    public boolean isParent() {
        return true;
    }
        
}
