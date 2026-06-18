package app.node;

import app.Layout;
import java.util.Map;

/**
 *
 * @author repp
 */
public interface BaseCompositeNode {
    
    public Map<? extends BaseNode, Layout> getChildren();
    
}
