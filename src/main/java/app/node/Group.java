package app.node;

import app.color.RGBColor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author repp
 */
public abstract class Group extends BaseNode {
    
    public RGBColor backgroundColor; // Default (null) is transparent
    public int borderWidth = 1; // Default (1) is thin borders
    public List<BaseNode> nodes = new ArrayList();

    public Group (String name) {
        super(name);
    }
    
}
