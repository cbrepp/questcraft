package app.controller.javafx.node;

import app.TextDecoration;
import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.color.RGBColor;
import app.controller.BaseController;
import static app.controller.BaseController.DEFAULT_PIXEL_SIZE;
import static app.controller.BaseController.logger;
import app.controller.JavaFXApplication;
import static app.controller.JavaFXApplication.DEFAULT_FONT;
import static app.controller.JavaFXApplication.getFxColor;
import app.node.BaseDecoratedNode;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.TextFlow;

/**
 *
 * @author repp
 */
public class JavaFXLabel extends BaseJavaFXNode {
    
    public JavaFXLabel(app.node.Label node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new TextFlow(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.Label node = (app.node.Label) this.node;
        TextFlow controllerNode = (TextFlow) this.controllerNode;
        
        RGBColor offsetColor = new DecoratedOffsetColor(new OffsetColor(), this.parent);

        TextDecoration defaultTextDecoration = ((JavaFXApplication) this.controller).defaultTextDecorations.get(this.viewName);

        FontSmoothingType fst;
        if (node.antiAliasMethod == null) {
            fst = null;
        } else fst = switch (node.antiAliasMethod) {
            case ANIMATION -> FontSmoothingType.GRAY;
            case TEXT -> FontSmoothingType.LCD;
            default -> null;
        };
        
        for (app.Text text : node.texts) {
            if (text.decoration == null) {
                text.decoration = new TextDecoration();
            }
            
            if (text.decoration.font == null) {
                if ((defaultTextDecoration != null) && (defaultTextDecoration.font != null)) {
                    text.decoration.font = defaultTextDecoration.font;
                } else {
                    text.decoration.font = DEFAULT_FONT;
                }
            }

            if (text.decoration.color == null) {
                if ((defaultTextDecoration != null) && (defaultTextDecoration.color != null)) {
                    text.decoration.color = defaultTextDecoration.color;
                } else {
                    text.decoration.color = offsetColor;
                }
            }
            if (text.decoration.color instanceof OffsetColor primitiveOffsetColor) {
                text.decoration.color = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            
            if (text.decoration.pixelSize == null) {
                if ((defaultTextDecoration != null) && (defaultTextDecoration.pixelSize != null)) {
                    text.decoration.pixelSize = defaultTextDecoration.pixelSize;
                } else {
                    text.decoration.pixelSize = DEFAULT_PIXEL_SIZE;
                }
            }

            if (text.decoration.style == null) {
                if ((defaultTextDecoration != null) && (defaultTextDecoration.style != null)) {
                    text.decoration.style = defaultTextDecoration.style;
                } else {
                    text.decoration.style = app.FontStyle.NORMAL;
                }
            }

            logger.log(Level.INFO, "offsetColor={0}, text.decoration.color={1}", new Object[]{offsetColor.getClass(), text.decoration.color.getClass()});
            TextFlow fxTempLabel = JavaFXApplication.stringToTextFlow(text.text.toString(), offsetColor, text.decoration, fst);
            controllerNode.getChildren().addAll(fxTempLabel.getChildren());
        }
        
        // TODO - Only set the following values if they're changing
        if (node.backgroundColor != null) {
            if (node.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                node.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            Color fxBackgroundColor = getFxColor(node.backgroundColor);
            controllerNode.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            controllerNode.setBackground(Background.EMPTY); // Transparent        
        }
        
        if (node.borderWidth != null) {
            Color fxBorderColor;
            if (node.borderColor != null) {
                if (node.borderColor instanceof OffsetColor primitiveOffsetColor) {
                    node.borderColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
                }
                fxBorderColor = getFxColor(node.borderColor);
            } else {
                fxBorderColor = getFxColor(offsetColor);
            }
            controllerNode.setBorder(new Border(new BorderStroke(fxBorderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
        }
        
        this.scaleNode(controllerNode);
    }
    
}
