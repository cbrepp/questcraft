package app.controller.javafx.node;

import app.HorizontalAlignment;
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
import java.util.Map;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
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

        CornerRadii radii = CornerRadii.EMPTY;
        if (node.cornerRadii > 0) {
            radii = new CornerRadii(node.cornerRadii);
        }        
        controllerNode.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, radii, new BorderWidths(node.borderWidth))));
        
        controllerNode.setFillHeight(node.expandChild); // Allow children to stay at their preferred heights and be centered 
        controllerNode.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        if (node.backgroundColor == null) {
            controllerNode.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, radii, Insets.EMPTY)));
        } else {
            if (node.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                node.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            Color fxBackgroundColor = getFxColor(node.backgroundColor);
            controllerNode.setBackground(new Background(new BackgroundFill(fxBackgroundColor, radii, Insets.EMPTY)));
        }
        
        if (node.expandChild) {
            controllerNode.setAlignment(Pos.TOP_LEFT);
        } else {
            if (null == node.alignment) {
                controllerNode.setAlignment(Pos.CENTER);
            } else switch (node.alignment) {
                case LEFT -> controllerNode.setAlignment(Pos.TOP_LEFT);
                case CENTER -> controllerNode.setAlignment(Pos.CENTER);
                case RIGHT -> controllerNode.setAlignment(Pos.CENTER_RIGHT);
                default -> controllerNode.setAlignment(Pos.CENTER);
            }
        }
        
        this.scaleNode(controllerNode);
    }
    
    @Override
    public Map<? extends BaseNode, Layout> getChildren() {
        HorizontalGroup hg = (HorizontalGroup) this.node;
        return hg.nodes;
    }
    
    @Override
    public void onChildAdded(BaseDecoratedNode childDecoratedNode) {
        logger.log(Level.INFO, "Entered: childDecoratedNode={0}", childDecoratedNode);
        
        if (this.controllerNode == null) {
            logger.log(Level.WARNING, "Child added before parent configured!");
            return;
        }
        
        HorizontalGroup node = (HorizontalGroup) this.node;
        if (node.expandChild) {
            //HBox controllerNode = (HBox) this.controllerNode;
            Node childNode = (Node) childDecoratedNode.controllerNode;
            HBox.setHgrow(childNode, Priority.ALWAYS);
        }
    }
    
}
