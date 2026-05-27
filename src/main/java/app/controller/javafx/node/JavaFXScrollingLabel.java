package app.controller.javafx.node;

import app.AntiAliasMethod;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.node.BaseDecoratedNode;
import java.util.logging.Level;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

/**
 *
 * @author repp
 */
public class JavaFXScrollingLabel extends BaseJavaFXNode {
    
    public JavaFXScrollingLabel(app.node.Label node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new VBox(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.Label node = (app.node.Label) this.node;
        VBox controllerNode = (VBox) this.controllerNode;
        
        controllerNode.getChildren().clear();
        
        node.antiAliasMethod = AntiAliasMethod.ANIMATION; // Needed because scroll pane's dork with FontSmoothingType.LCD
        JavaFXLabel decoratedLabel = new JavaFXLabel(node, this, this.viewName, this.controller);
        decoratedLabel.configure();
        
        // Allow text to be scrolled if needed
        ((TextFlow) decoratedLabel.controllerNode).setCache(false);
        ScrollPane scrollPane = new ScrollPane(((TextFlow) decoratedLabel.controllerNode));
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setBackground(Background.EMPTY);
        controllerNode.getChildren().add(scrollPane);
        controllerNode.setBackground(Background.EMPTY);
        controllerNode.setSnapToPixel(true);
        scrollPane.getStyleClass().add("edge-to-edge"); // Removes the border
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setCache(false);
        ((TextFlow) decoratedLabel.controllerNode).setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Fill the scroll pane.  The scroll pane will be scaled as needed.
        
        this.scaleNode(controllerNode);
        this.scaleNode(scrollPane);
    }
    
}
