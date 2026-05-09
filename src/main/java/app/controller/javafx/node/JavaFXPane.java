package app.controller.javafx.node;

import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.color.RGBColor;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import static app.controller.JavaFXApplication.getFxColor;
import app.node.BaseDecoratedNode;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author repp
 */
public class JavaFXPane extends BaseJavaFXNode {
    
    public JavaFXPane(app.node.Pane node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new Pane(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.Pane node = (app.node.Pane) this.node;
        Pane controllerNode = (Pane) this.controllerNode;
        
        controllerNode.setSnapToPixel(true);
        controllerNode.setPadding(Insets.EMPTY);

        controllerNode.setCache(false);
        Color fxBackgroundColor;
        if (node.backgroundColor == null) {
            fxBackgroundColor = Color.TRANSPARENT;
        } else {
            if (node.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                node.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            fxBackgroundColor = getFxColor(node.backgroundColor);
        }
        controllerNode.setBackground(new Background(new BackgroundFill(
            fxBackgroundColor,
            CornerRadii.EMPTY, 
            Insets.EMPTY      // To prevent blurry text
        )));
        controllerNode.setPadding(Insets.EMPTY);
        controllerNode.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        controllerNode.setCache(false);
        if ((node.borderWidth != null) && (node.borderWidth > 0)) {
            RGBColor offsetColor = new DecoratedOffsetColor(new OffsetColor(), this.parent);
            controllerNode.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
        }
        
        this.scaleNode(controllerNode);
    }
    
}
