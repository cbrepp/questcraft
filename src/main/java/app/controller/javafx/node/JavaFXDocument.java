package app.controller.javafx.node;

import app.Layout;
import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.color.RGBColor;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import static app.controller.JavaFXApplication.getFxColor;
import app.node.BaseCompositeNode;
import app.node.BaseDecoratedNode;
import app.node.BaseNode;
import app.node.Document;
import java.util.Map;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

/**
 *
 * @author repp
 */
public class JavaFXDocument extends BaseJavaFXNode implements BaseCompositeNode {
    
    public JavaFXDocument(app.node.Document node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new TextFlow(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.Document node = (app.node.Document) this.node;
        TextFlow controllerNode = (TextFlow) this.controllerNode;
        
        RGBColor offsetColor = new DecoratedOffsetColor(new OffsetColor(), this.parent);

        // TODO - Only set the following values if they're changing
        if (node.backgroundColor != null) {
            if (node.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                node.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            Color fxBackgroundColor = getFxColor(node.backgroundColor);
            controllerNode.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            controllerNode.setBackground(Background.EMPTY); // Transparent        
        }
        
        if (node.borderWidth > 0) {
            controllerNode.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
        }
        
        this.scaleNode(controllerNode);
    }
    
    @Override
    public Map<? extends BaseNode, Layout> getChildren() {
        Document doc = (Document) this.node;
        return doc.nodes;
    }
    
}
