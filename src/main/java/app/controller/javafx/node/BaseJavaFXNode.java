package app.controller.javafx.node;

import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.node.BaseDecoratedNode;
import app.node.BaseNode;
import java.util.logging.Level;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author repp
 */
public abstract class BaseJavaFXNode extends BaseDecoratedNode {
    
    public BaseJavaFXNode(BaseNode node, Object controllerNode, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, controllerNode, parent, viewName, controller);
    }
    
    public void scaleNode(Node controllerNode) {
        if ((controllerNode != null) && (this.parent != null)) {
            Object fxParentNode = this.parent.controllerNode;
            double parentWidth;
            double parentHeight;
            if ((fxParentNode != null) && (fxParentNode instanceof Region parentRegion)) {
                parentWidth = parentRegion.getPrefWidth();
                parentHeight = parentRegion.getPrefHeight();
            } else if ((fxParentNode != null) && (fxParentNode instanceof Stage parentStage)) {
                Scene scene = parentStage.getScene();
                Pane pane = (Pane) scene.getRoot();
                parentWidth = pane.getPrefWidth();
                parentHeight = pane.getPrefHeight();
            } else {
                if (fxParentNode == null) {
                    logger.log(Level.SEVERE, "Parent has not been defined");
                } else {
                    Class<?> parentControlClass = fxParentNode.getClass();
                    logger.log(Level.SEVERE, "Parent does not have a supported class: {0}", parentControlClass.getSimpleName());
                }
                return;
            }

            if (controllerNode instanceof Region region) {
                if (node.scaleX != null) {
                    double prefWidth = Math.round(parentWidth * node.scaleX);
                    region.setPrefWidth(prefWidth);
                    region.setMaxWidth(prefWidth);
                } else {
                    region.setMaxWidth(parentWidth);
                }
                if (node.scaleY != null) {
                    double prefHeight = Math.round(parentHeight * node.scaleY);
                    region.setPrefHeight(prefHeight);
                    region.setMaxHeight(prefHeight);
                } else {
                    region.setMaxHeight(parentHeight);
                }
            } else if (controllerNode instanceof Rectangle rectangle) {
                if (node.scaleX != null) {
                    double prefWidth = Math.round(parentWidth * node.scaleX);
                    rectangle.setWidth(prefWidth);
                } else {
                    rectangle.setWidth(parentWidth);
                }
                if (node.scaleY != null) {
                    double prefHeight = Math.round(parentHeight * node.scaleY);
                    rectangle.setHeight(prefHeight);
                } else {
                    rectangle.setHeight(parentHeight);
                }
            } else {
                logger.log(Level.WARNING, "Scaling is not supported for node type {0}", controllerNode.getClass().getName());
            }
        }
    }

}
