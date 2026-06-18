package app.controller.javafx.node;

import app.Layout;
import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import static app.controller.JavaFXApplication.getFxColor;
import app.node.BaseCompositeNode;
import app.node.BaseDecoratedNode;
import app.node.BaseNode;
import app.node.Button;
import app.node.Field;
import app.node.InputField;
import java.util.LinkedHashMap;
import java.util.Map;
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
public class JavaFXInputField extends BaseJavaFXNode implements BaseCompositeNode {
    
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
    
    @Override
    public Map<? extends BaseNode, Layout> getChildren() {
        Map<BaseNode, Layout> children = new LinkedHashMap();
        
        InputField inputField = (InputField) this.node;
        
        String fieldName = inputField.name + "_field";
        logger.log(Level.WARNING, "Adding field {0}", fieldName);
        Field field = new Field(fieldName);
        if (inputField.fieldBackgroundColor != null) {
            field.backgroundColor = inputField.fieldBackgroundColor;
        }
        field.borderWidth = inputField.fieldBorderWidth;
        if (inputField.fieldDisplayLength != null) {
            field.displayLength = inputField.fieldDisplayLength;
        }
        field.effects = inputField.fieldEffects;
        if (inputField.initialValue != null) {
            field.initialValue = inputField.initialValue.toString();
        }
        field.isEnabled = inputField.isEnabled;
        field.isUpperCase = inputField.isUpperCase;
        if (inputField.label != null) {
            field.label = inputField.label.toString();
        }
        field.length = inputField.length;
        field.pixelSize = inputField.pixelSize;
        field.textColor = inputField.textColor;
        field.textFont = inputField.textFont;
        children.put(field, null);
        
        String buttonName;
        if (inputField.buttonText == null) {
            buttonName = inputField.name + "_button";
        } else {
            buttonName = inputField.name + "_" + inputField.buttonText.toString();
        }
        
        logger.log(Level.WARNING, "Adding button {0}", buttonName);
        Button button = new Button(buttonName);
        if (inputField.buttonBackgroundColor != null) {
            button.backgroundColor = inputField.buttonBackgroundColor;
        }
        if (inputField.buttonBorderWidth != null) {
            button.borderWidth = inputField.buttonBorderWidth;
        }
        button.effects = inputField.buttonEffects;
        button.eventListener = inputField.internalEventListener;
        
        // Button defers to parent's node's event listener
        button.eventName = inputField.name;
        button.isEnabled = inputField.isEnabled;
        button.isMultiUse = inputField.isMultiUse;
        button.keyBinding = inputField.keyBinding;
        button.pixelSize = inputField.pixelSize;
        if (inputField.buttonText != null) {
            button.text = inputField.buttonText.toString();
        }
        button.textColor = inputField.textColor;
        button.textFont = inputField.textFont;
        children.put(button, null);

        return children;
    }
    
}
