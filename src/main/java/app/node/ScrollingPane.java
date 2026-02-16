package app.node;

import app.*;

/**
 *
 * @author repp
 */
public class ScrollingPane extends BaseNode {
    
    public Color backgroundColor; // Default (null) is transparent
    public int borderWidth = 1; // Default (1) is thin borders

    public ScrollingPane (String name) {
        super(name);
    }
    
}
