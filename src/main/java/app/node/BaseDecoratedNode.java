package app.node;

import app.controller.BaseController;

/**
 *
 * @author repp
 */
public abstract class BaseDecoratedNode {

    public final BaseNode node;
    public final BaseDecoratedNode parent;
    public final BaseController controller;
    public final Object controllerNode;
    public final String viewName;
    
    public BaseDecoratedNode(BaseNode node, Object controllerNode, BaseDecoratedNode parent, String viewName, BaseController controller) {
        this.node = node;
        this.controller = controller;
        this.controllerNode = controllerNode;
        this.parent = parent;
        this.viewName = viewName;
    }
    
    public abstract void configure();
    
}
