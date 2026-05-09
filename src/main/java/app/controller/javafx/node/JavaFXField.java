package app.controller.javafx.node;

import app.TextDecoration;
import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.color.RGBColor;
import app.controller.BaseController;
import static app.controller.BaseController.DEFAULT_PIXEL_SIZE;
import static app.controller.BaseController.logger;
import static app.controller.JavaFXApplication.DEFAULT_FONT;
import static app.controller.JavaFXApplication.getFxColor;
import static app.controller.JavaFXApplication.stringToText;
import app.node.BaseDecoratedNode;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

/**
 *
 * @author repp
 */
public class JavaFXField extends BaseJavaFXNode {
    
    public JavaFXField(app.node.Field node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new TextField(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.Field node = (app.node.Field) this.node;
        TextField controllerNode = (TextField) this.controllerNode;
        
        RGBColor offsetColor = new DecoratedOffsetColor(new OffsetColor(), this.parent);

        controllerNode.setDisable(!node.isEnabled);
        
        if (node.label != null) {
            controllerNode.setPromptText(node.label);
        }
        
        if (node.initialValue != null) {
            controllerNode.setText(node.initialValue);
        }
        
        if (node.displayLength != null) {
            controllerNode.setPrefColumnCount(node.displayLength);
        }
        
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (node.isUpperCase) {
                // Apply the uppercase conversion to the new text
                change.setText(change.getText().toUpperCase());
            }

            // Enforce the character limit
            if ((node.length != null) && (change.getControlNewText().length() > node.length)) {
                return null; // Reject the change
            }
            
            if (node.eventListener != null) {
                // Raise an event for each entered character
                String currentText = change.getControlNewText();
                logger.log(Level.INFO, "Text entered: name={0}, text={1}", new Object[]{node.name, currentText});
                node.eventListener.onEvent(node.eventName, currentText);
            }
            
            return change; // Accept the change
        });
        controllerNode.setTextFormatter(textFormatter);
        
        String fontName;
        if (node.textFont == null) {
            fontName = DEFAULT_FONT;
        } else {
            fontName = node.textFont;
        }
        
        app.color.RGBColor textColor;
        if (node.textColor == null) {
            textColor = offsetColor;
        } else {
            textColor = node.textColor;
        }
        if (textColor instanceof OffsetColor primitiveOffsetColor) {
            textColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
        }
        
        double pixelSize;
        if (node.pixelSize == null) {
            pixelSize = DEFAULT_PIXEL_SIZE;
        } else {
            pixelSize = node.pixelSize;
        }
        
        TextDecoration decoration = new TextDecoration();
        decoration.font = fontName;
        decoration.color = textColor;
        decoration.pixelSize = pixelSize;
        decoration.style = node.textStyle;
        Text fieldText = stringToText("temp", offsetColor, decoration, FontSmoothingType.LCD); // Allow stringToText to parse the font style
        controllerNode.setFont(fieldText.getFont());
        controllerNode.setStyle("-fx-text-fill: rgb(" + textColor.getRed() + ", " + textColor.getGreen() + ", " + textColor.getBlue() + ");");                
         
        if (node.backgroundColor != null) {
            if (node.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                node.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            Color fxBackgroundColor = getFxColor(node.backgroundColor);
            controllerNode.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            controllerNode.setBackground(Background.EMPTY); // Transparent        
        }
        
        if ((node.borderWidth != null) && (node.borderWidth > 0)) {
            controllerNode.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
        }
        
        this.scaleNode(controllerNode);
    }
    
}
