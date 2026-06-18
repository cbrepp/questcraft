package app.node;

import app.Layout;
import app.color.RGBColor;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author repp
 */
public abstract class Group extends BaseNode {
    
    public RGBColor backgroundColor; // Default (null) is transparent
    public int borderWidth = 1; // Default (1) is thin borders
    public Map<BaseNode, Layout> nodes = new LinkedHashMap();

    public Group (String name) {
        super(name);
    }
    
}
