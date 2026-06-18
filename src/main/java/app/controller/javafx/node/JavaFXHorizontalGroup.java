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
import app.node.HorizontalGroup;
import app.node.ScrollingDocument;
import java.util.Map;
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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 *
 * @author repp
 */
public class JavaFXHorizontalGroup extends BaseJavaFXNode implements BaseCompositeNode {
    
    public JavaFXHorizontalGroup(app.node.HorizontalGroup node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new HBox(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.HorizontalGroup node = (app.node.HorizontalGroup) this.node;
        HBox controllerNode = (HBox) this.controllerNode;
        
        RGBColor offsetColor = new DecoratedOffsetColor(new OffsetColor(), this.parent);
        
        controllerNode.getChildren().clear();
        
        controllerNode.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
        controllerNode.setAlignment(Pos.CENTER);
        controllerNode.setFillHeight(false); // Allow children to stay at their preferred heights and be centered 
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

        Map<? extends BaseNode, Layout> childNodes = ((app.node.Group) node).nodes;
        for (BaseNode childNode : childNodes.keySet()) {
            Layout layout = childNodes.get(childNode);
            this.controller.addNode(viewName, node.name, childNode, null);
        }
        
        this.scaleNode(controllerNode);
    }
    
    @Override
    public Map<? extends BaseNode, Layout> getChildren() {
        HorizontalGroup hg = (HorizontalGroup) this.node;
        return hg.nodes;
    }
    
}
