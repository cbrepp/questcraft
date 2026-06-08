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
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author repp
 */
public class JavaFXVerticalGroup extends BaseJavaFXNode {
    
    public JavaFXVerticalGroup(app.node.VerticalGroup node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new VBox(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.VerticalGroup node = (app.node.VerticalGroup) this.node;
        VBox controllerNode = (VBox) this.controllerNode;
        
        RGBColor offsetColor = new DecoratedOffsetColor(new OffsetColor(), this.parent);
        
        controllerNode.getChildren().clear();
        
        controllerNode.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
        controllerNode.setAlignment(Pos.CENTER);
        controllerNode.setFillWidth(false); // Allow children to stay at their preferred widths and be centered 
        controllerNode.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        if (node.backgroundColor == null) {
            controllerNode.setBackground(Background.EMPTY);
        } else {
            if (node.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                node.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            Color fxBackgroundColor = getFxColor(node.backgroundColor);
            controllerNode.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        this.scaleNode(controllerNode);
    }
    
}
