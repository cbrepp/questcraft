package app.node;

import app.*;
import static app.controller.BaseController.NODE_PUBLISHED_EVENT;
import static app.controller.BaseController.logger;
import app.node.effect.BaseEffect;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author repp
 */
public abstract class BaseNode implements EventListener {
    
    private RelativeBounds bounds; // Boundaries of the node (once published)
    public List<BaseEffect> effects = new ArrayList(); // Default (null/empty) is no effects
    public String name;
    public Double scaleX; // Default (null) is to not scale.  0.0 = hidden, 0.5 = half parent's width, 1.0 = parent's width.
    public Double scaleY; // Default (null) is to not scale.  0.0 = hidden, 0.5 = half parent's height, 1.0 = parent's height.
    
    public BaseNode(String name) {
        this.name = name;
    }
    
    public RelativeBounds getBounds() {
        return this.bounds;
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        logger.log(Level.INFO, "Entered: eventName={0}, eventValue={1}", new Object[]{eventName, eventValue});
        if (eventName.equals(NODE_PUBLISHED_EVENT)) {
            RelativeBounds bounds = (RelativeBounds) eventValue;
            this.bounds = bounds;
        }
    }
    
}
