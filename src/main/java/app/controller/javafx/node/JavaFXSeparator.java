package app.controller.javafx.node;

import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.node.BaseDecoratedNode;
import java.util.logging.Level;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;

/**
 *
 * @author repp
 */
public class JavaFXSeparator extends BaseJavaFXNode {
    
    public JavaFXSeparator(app.node.Separator node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new Separator(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.Separator node = (app.node.Separator) this.node;
        Separator controllerNode = (Separator) this.controllerNode;
        
        if (node.orientation == app.node.Separator.Orientation.HORIZONTAL) {
            controllerNode.setOrientation(Orientation.HORIZONTAL);
        } else {
            controllerNode.setOrientation(Orientation.VERTICAL);
        }
        
        if (this.parent.controllerNode instanceof Pane fxParentPane) {
            if (((app.node.Separator) node).orientation == app.node.Separator.Orientation.HORIZONTAL) {
                controllerNode.prefWidthProperty().bind(fxParentPane.widthProperty());
            } else {
                controllerNode.prefHeightProperty().bind(fxParentPane.heightProperty());
            }
        } else {
            this.scaleNode(controllerNode);
        }
    }
    
}
