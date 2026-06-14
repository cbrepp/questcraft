package app.controller.javafx.node;

import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.color.RGBColor;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.controller.JavaFXApplication;
import app.node.BaseDecoratedNode;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author repp
 */
public class JavaFXPopupWindow extends BaseJavaFXNode {
    
    public JavaFXPopupWindow(app.node.PopupWindow node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new StackPane(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.PopupWindow node = (app.node.PopupWindow) this.node;
        StackPane controllerNode = (StackPane) this.controllerNode;
        
        Color fxBackgroundColor;
        if (node.backgroundColor == null) {
            fxBackgroundColor = Color.TRANSPARENT;
        } else {
            RGBColor backgroundColor = node.backgroundColor;
            if (backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            fxBackgroundColor = JavaFXApplication.getFxColor(backgroundColor);
        }
        
        CornerRadii cornerRadii;
        if (node.cornerRadii == 0) {
            cornerRadii = CornerRadii.EMPTY;
        } else {
            cornerRadii = new CornerRadii(node.cornerRadii);
        }
        
        BackgroundFill overlayFill = new BackgroundFill(
            fxBackgroundColor,
            cornerRadii,
            Insets.EMPTY             // No margins/padding
        );
        controllerNode.setBackground(new Background(overlayFill));
        
        // TODO - Get the root pane from the constructor's parent param, should be passed in
        // TODO - The JavaFXApplication.parentDecoratedNode should be the only parent type node and should be registered like the others
        // TODO - Should probably be the root pane
        Stage primaryStage = (Stage) this.parent.controllerNode;
        Scene primaryScene = primaryStage.getScene();
        StackPane rootPane = (StackPane) primaryScene.getRoot();
        rootPane.getChildren().add(controllerNode);
        // Need to add named Stage, Scene, StackPane, and TabPane? with names like system/stage, etc.
        
        this.scaleNode(controllerNode);
    }
    
}
