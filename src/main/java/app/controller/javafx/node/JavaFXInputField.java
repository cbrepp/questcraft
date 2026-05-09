package app.controller.javafx.node;

import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import static app.controller.JavaFXApplication.getFxColor;
import app.node.BaseDecoratedNode;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

/**
 *
 * @author repp
 */
public class JavaFXInputField extends BaseJavaFXNode {
    
    public JavaFXInputField(app.node.InputField node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new FlowPane(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.InputField node = (app.node.InputField) this.node;
        FlowPane controllerNode = (FlowPane) this.controllerNode;
        
        if (node.spacerPixels != null) {
            controllerNode.setHgap(node.spacerPixels);
            controllerNode.setVgap(node.spacerPixels);
            controllerNode.setPadding(new Insets(node.spacerPixels));
        }
        
        if (node.backgroundColor != null) {
            if (node.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                node.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            Color fxBackgroundColor = getFxColor(node.backgroundColor);
            BackgroundFill backgroundFill = new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY);
            Background background = new Background(backgroundFill);
            controllerNode.setBackground(background);
        }
        
        // TODO - This is an interesting way to subscribe to the button click and raise the main event, passing the entered text
        final FlowPane finalFxInputField = controllerNode;
        node.internalEventListener = (String eventName, Object eventValue) -> {
            String enteredText = "";
            for (Node child : finalFxInputField.getChildren()) {
                if (child instanceof TextField field) {
                    enteredText = field.getText();
                }
            }
            node.eventListener.onEvent(node.name, enteredText);
            
            // Clear the entered text
            for (Node child : finalFxInputField.getChildren()) {
                if (child instanceof TextField field) {
                    field.clear();
                }
            }
        };
        
        this.scaleNode(controllerNode);
    }
    
}
