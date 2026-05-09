package app.controller.javafx.node;

import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.color.RGBColor;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.node.BaseDecoratedNode;
import java.util.logging.Level;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author repp
 */
public class JavaFXRectangle extends BaseJavaFXNode {
    
    public JavaFXRectangle(app.node.Rectangle node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new Rectangle(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.Rectangle node = (app.node.Rectangle) this.node;
        Rectangle controllerNode = (Rectangle) this.controllerNode;
        
        RGBColor offsetColor = new DecoratedOffsetColor(new OffsetColor(), this.parent);
    
        if (node.color == null) {
            controllerNode.setFill(Color.rgb(offsetColor.getRed(), offsetColor.getGreen(), offsetColor.getBlue(), node.opacity));
        } else {
            if (node.color instanceof OffsetColor primitiveOffsetColor) {
                node.color = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            controllerNode.setFill(Color.rgb(node.color.getRed(), node.color.getGreen(), node.color.getBlue(), node.opacity));
        }
        
        this.scaleNode(controllerNode);
    }
    
}
