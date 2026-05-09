package app.controller.javafx.node;

import app.FontStyle;
import app.TextDecoration;
import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.color.RGBColor;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.controller.JavaFXApplication;
import app.node.BaseDecoratedNode;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;

/**
 *
 * @author repp
 */
public class JavaFXLink extends BaseJavaFXNode {
    
    public JavaFXLink(app.node.Link node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new Hyperlink(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.Link node = (app.node.Link) this.node;
        Hyperlink controllerNode = (Hyperlink) this.controllerNode;
        
        RGBColor offsetColor = new DecoratedOffsetColor(new OffsetColor(), this.parent);
        
        controllerNode.setDisable(!node.isEnabled);
        
        controllerNode.setPadding(Insets.EMPTY);
        controllerNode.setBorder(Border.EMPTY);
        
        String font;
        if (node.textFont == null) {
            font = JavaFXApplication.DEFAULT_FONT;
        } else {
            font = node.textFont;
        }
        
        RGBColor textColor;
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
            pixelSize = BaseController.DEFAULT_PIXEL_SIZE;
        } else {
            pixelSize = node.pixelSize;
        }
        
        // Configure the font style based on whether the link is enabled
        FontStyle fontStyle;
        if (node.isEnabled) {
            fontStyle = FontStyle.UNDERLINE_LINK;
        } else {
            fontStyle = FontStyle.BOLD;
        }
        
        // Use a graphic instead of text to support formatted text
        String textString = node.text.toString();
        TextDecoration decoration = new TextDecoration();
        decoration.font = font;
        decoration.color = textColor;
        decoration.pixelSize = pixelSize;
        decoration.style = fontStyle;
        controllerNode.setGraphic(JavaFXApplication.stringToTextFlow(textString, offsetColor, decoration, FontSmoothingType.LCD));
        
        if (node.backgroundColor != null) {
            if (node.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                node.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            Color fxBackgroundColor = JavaFXApplication.getFxColor(node.backgroundColor);
            controllerNode.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            controllerNode.setBackground(Background.EMPTY); // Transparent        
        }
        
        if (node.eventListener != null) {
            controllerNode.setOnAction(e -> {
                logger.log(Level.INFO, "Link selected: name={0}", node.name);
                node.eventListener.onEvent(node.name, null);
            });
        }
        
        this.scaleNode(controllerNode);
    }
    
}
