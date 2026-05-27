package app.controller.javafx.node;

import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import static app.controller.JavaFXApplication.getFxColor;
import app.node.BaseDecoratedNode;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

/**
 *
 * @author repp
 */
public class JavaFXScrollingDocument extends BaseJavaFXNode {
    
    public JavaFXScrollingDocument(app.node.ScrollingDocument node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new ScrollPane(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.ScrollingDocument node = (app.node.ScrollingDocument) this.node;
        ScrollPane controllerNode = (ScrollPane) this.controllerNode;
        
        JavaFXDocument document = new JavaFXDocument((app.node.ScrollingDocument) this.node, this, this.viewName, this.controller);
        document.configure();
        ((TextFlow) document.controllerNode).setManaged(true);
        ((TextFlow) document.controllerNode).setSnapToPixel(true);
        ((TextFlow) document.controllerNode).setPadding(Insets.EMPTY);
        controllerNode.setContent(((TextFlow) document.controllerNode));
        controllerNode.setSnapToPixel(true);

        // Disable the view port's cache once it's ready
        final ScrollPane finalScrollPane = controllerNode;
        controllerNode.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsW, oldW, newW) -> {
                    if (newW != null) {
                        newW.setOnShown(e -> {
                            finalScrollPane.lookup(".viewport").setCache(false);
                        });
                    }
                });
            }
        });

        // Allow text to be scrolled if needed
        ((TextFlow) document.controllerNode).setCache(false);
        controllerNode.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        controllerNode.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        controllerNode.setFitToWidth(true);
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
        controllerNode.getStyleClass().add("edge-to-edge"); // Removes the border
        controllerNode.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-padding: 0;");
        controllerNode.setCache(false);
        controllerNode.setCacheHint(CacheHint.QUALITY);
        ((TextFlow) document.controllerNode).setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Fill the scroll pane.  The scroll pane will be scaled as needed.
        
        this.scaleNode(controllerNode);
    }
    
}
