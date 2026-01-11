package app.node;

import app.*;
import app.node.effect.BaseEffect;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author repp
 */
public abstract class BaseNode {
    
    public List<BaseEffect> effects = new ArrayList(); // Default (null/empty) is no effects
    public Layout layout; // Default (null) is to let the parent node handle the layout
    public String name;
    
    public BaseNode(String name) {
        this.name = name;
    }
    
    public BaseNode(String name, Layout layout) {
        this.name = name;
        this.layout = layout;
    }
    
}
