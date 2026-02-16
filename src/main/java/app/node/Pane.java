package app.node;

import app.*;

/**
 *
 * @author repp
 */
public class Pane extends BaseNode {
    
    public Color backgroundColor; // Default (null) is transparent
    public Integer borderWidth = 1; // Default (1) is thin borders

    public Pane (String name) {
        super(name);
    }
    
}
