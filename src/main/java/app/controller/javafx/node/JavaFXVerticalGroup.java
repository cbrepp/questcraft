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
import app.node.VerticalGroup;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author repp
 */
public class JavaFXVerticalGroup extends BaseJavaFXNode implements BaseCompositeNode {
    
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

        CornerRadii radii = CornerRadii.EMPTY;
        if (node.cornerRadii > 0) {
            radii = new CornerRadii(node.cornerRadii);
        }        
        controllerNode.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, radii, new BorderWidths(node.borderWidth))));
        
        controllerNode.setFillWidth(node.expandChild); // Allow children to stay at their preferred widths and be centered 
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
            controllerNode.setAlignment(Pos.CENTER);
        }

        this.scaleNode(controllerNode);
    }
    
    @Override
    public Map<? extends BaseNode, Layout> getChildren() {
        VerticalGroup vg = (VerticalGroup) this.node;
        return vg.nodes;
    }
    
    @Override
    public void onChildAdded(BaseDecoratedNode childDecoratedNode) {
        logger.log(Level.INFO, "Entered: childDecoratedNode={0}", childDecoratedNode);
        
        if (this.controllerNode == null) {
            logger.log(Level.WARNING, "Child added before parent configured!");
            return;
        }
        
        VerticalGroup node = (VerticalGroup) this.node;
        if (node.expandChild) {
            //VBox controllerNode = (VBox) this.controllerNode;
            Node childNode = (Node) childDecoratedNode.controllerNode;
            VBox.setVgrow(childNode, Priority.ALWAYS);
        }
    }
    
}
