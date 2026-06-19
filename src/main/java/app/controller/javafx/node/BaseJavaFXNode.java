package app.controller.javafx.node;

import app.Coordinates;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.node.BaseDecoratedNode;
import app.node.BaseNode;
import java.util.logging.Level;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaView;
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
    
    public Coordinates getParentDimensions(Node controllerNode) {
        Object fxParentNode = this.parent.controllerNode;
        double parentWidth;
        double parentHeight;
        if ((fxParentNode != null) && (fxParentNode instanceof Region parentRegion)) {
            parentWidth = parentRegion.getPrefWidth();
            parentHeight = parentRegion.getPrefHeight();
            logger.log(Level.SEVERE, "Parent region preferred dimensions: {0}x{1}", new Object[]{parentWidth, parentHeight});
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
            return null;
        }
        Coordinates parentDimensions = new Coordinates((int) Math.round(parentWidth), (int) Math.round(parentHeight));
        return parentDimensions;
    }
    
    public void scaleNode(Node controllerNode) {
        if ((controllerNode != null) && (this.parent != null)) {
            Coordinates parentDimensions = this.getParentDimensions(controllerNode);
            if (parentDimensions == null) {
                return;
            }

            if (controllerNode instanceof Region region) {
                if (node.scaleX != null) {
                    logger.log(Level.INFO, "Scaling Region to parent width {0} by {1}", new Object[]{parentDimensions.x, node.scaleX});
                    double prefWidth = Math.round(parentDimensions.x * node.scaleX);
                    region.setPrefWidth(prefWidth);
                    region.setMaxWidth(prefWidth);
                } else {
                    region.setMaxWidth(parentDimensions.x);
                }
                if (node.scaleY != null) {
                    logger.log(Level.INFO, "Scaling Region to parent height {0} by {1}", new Object[]{parentDimensions.y, node.scaleY});
                    double prefHeight = Math.round(parentDimensions.y * node.scaleY);
                    region.setPrefHeight(prefHeight);
                    region.setMaxHeight(prefHeight);
                } else {
                    region.setMaxHeight(parentDimensions.y);
                }
            } else if (controllerNode instanceof Rectangle rectangle) {
                if (node.scaleX != null) {
                    logger.log(Level.INFO, "Scaling Rectangle to parent width {0} by {1}", new Object[]{parentDimensions.x, node.scaleX});
                    double prefWidth = Math.round(parentDimensions.x * node.scaleX);
                    rectangle.setWidth(prefWidth);
                } else {
                    rectangle.setWidth(parentDimensions.x);
                }
                if (node.scaleY != null) {
                    logger.log(Level.INFO, "Scaling Rectangle to parent height {0} by {1}", new Object[]{parentDimensions.y, node.scaleY});
                    double prefHeight = Math.round(parentDimensions.y * node.scaleY);
                    rectangle.setHeight(prefHeight);
                } else {
                    rectangle.setHeight(parentDimensions.y);
                }
            } else if (controllerNode instanceof MediaView video) {
                if (node.scaleX != null) {
                    logger.log(Level.INFO, "Scaling MediaView to parent width {0} by {1}", new Object[]{parentDimensions.x, node.scaleX});
                    double prefWidth = Math.round(parentDimensions.x * node.scaleX);
                    video.setFitWidth(prefWidth);
                }
                if (node.scaleY != null) {
                    logger.log(Level.INFO, "Scaling MediaView to parent height {0} by {1}", new Object[]{parentDimensions.y, node.scaleY});
                    double prefHeight = Math.round(parentDimensions.y * node.scaleY);
                    video.setFitHeight(prefHeight);
                }
            } else {
                logger.log(Level.WARNING, "Scaling is not supported for node type {0}", controllerNode.getClass().getName());
            }
        }
    }

}
