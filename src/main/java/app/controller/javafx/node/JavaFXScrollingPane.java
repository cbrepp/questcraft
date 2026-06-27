package app.controller.javafx.node;

import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import static app.controller.JavaFXApplication.getFxColor;
import app.node.BaseDecoratedNode;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author repp
 */
public class JavaFXScrollingPane extends BaseJavaFXNode {
    
    public JavaFXScrollingPane(app.node.ScrollingPane node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new ScrollPane(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.ScrollingPane node = (app.node.ScrollingPane) this.node;
        ScrollPane controllerNode = (ScrollPane) this.controllerNode;
        
        controllerNode.setContent(null);
        
        Pane fxPane;
        fxPane = new Pane();
        fxPane.setManaged(true);
        fxPane.setSnapToPixel(true);
        fxPane.setPadding(Insets.EMPTY);
        controllerNode.setContent(fxPane);
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
        fxPane.setCache(false);
        controllerNode.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        controllerNode.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        controllerNode.setFitToWidth(true);
        controllerNode.setFitToHeight(true);
        Color fxBackgroundColor;
        if (node.backgroundColor == null) {
            fxBackgroundColor = Color.TRANSPARENT;
        } else {
            if (node.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                node.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            fxBackgroundColor = getFxColor(node.backgroundColor);
        }
        fxPane.setBackground(new Background(new BackgroundFill(
            fxBackgroundColor,
            CornerRadii.EMPTY, 
            Insets.EMPTY      // To prevent blurry text
        )));
        fxPane.setPadding(Insets.EMPTY);
        controllerNode.getStyleClass().add("edge-to-edge"); // Removes the border
        controllerNode.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        controllerNode.setCache(false);
        fxPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Fill the scroll pane.  The scroll pane will be scaled as needed.
        
        this.scaleNode(controllerNode);
    }
    
}
