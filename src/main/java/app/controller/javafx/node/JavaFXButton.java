package app.controller.javafx.node;

import app.FontStyle;
import static app.KeyboardKey.DOWN;
import static app.KeyboardKey.LEFT;
import static app.KeyboardKey.RIGHT;
import static app.KeyboardKey.UP;
import app.TextDecoration;
import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.color.RGBColor;
import app.controller.BaseController;
import static app.controller.BaseController.DEFAULT_PIXEL_SIZE;
import static app.controller.BaseController.logger;
import app.controller.JavaFXApplication;
import static app.controller.JavaFXApplication.DEFAULT_FONT;
import static app.controller.JavaFXApplication.DEFAULT_OFFSET_COLOR;
import static app.controller.JavaFXApplication.getFxColor;
import app.node.BaseDecoratedNode;
import java.util.logging.Level;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/**
 *
 * @author repp
 */
public class JavaFXButton extends BaseJavaFXNode {
    
    public JavaFXButton(app.node.Button node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new Button(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.Button node = (app.node.Button) this.node;
        Button controllerNode = (Button) this.controllerNode;
        
        RGBColor offsetColor = new DecoratedOffsetColor(new OffsetColor(), this.parent);
        
        TextDecoration defaultTextDecoration = ((JavaFXApplication) this.controller).defaultTextDecorations.get(this.viewName);
        
        controllerNode.setDisable(!node.isEnabled);
                
        String font;
        if (node.textFont == null) {
            font = DEFAULT_FONT;
        } else {
            font = node.textFont;
        }
        
        app.color.RGBColor textColor;
        if (node.textColor == null) {
            if (node.backgroundColor != null) {
                textColor = offsetColor;
            } else {
                textColor = DEFAULT_OFFSET_COLOR; // By default, light gray is the background color
            }
        } else {
            textColor = node.textColor;
        }
        if (textColor instanceof OffsetColor primitiveOffsetColor) {
            textColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
        }
        
        double pixelSize;
        if (node.pixelSize == null) {
            if ((defaultTextDecoration != null) && (defaultTextDecoration.pixelSize != null)) {
                pixelSize = defaultTextDecoration.pixelSize;
            } else {
                pixelSize = DEFAULT_PIXEL_SIZE;
            }
        } else {
            pixelSize = node.pixelSize;
        }
        
        // Configure the font style based on whether the button is enabled
        FontStyle fontStyle;
        if (node.isEnabled) {
            fontStyle = FontStyle.NORMAL;
        } else {
            fontStyle = FontStyle.ITALIC;
        }
        
        // Use a graphic instead of text to support formatted text
        controllerNode.setContentDisplay(ContentDisplay.CENTER);
        if (node.scaleY != null) {
            // TODO - An unfortunate work-around for the label/graphic not vertically aligning when the node is scaled vertically and there is no text
            controllerNode.setText("\u200B"); // Unicode zero-width space
        }
        controllerNode.setAlignment(Pos.CENTER);
        String buttonText = node.text.toString();
        TextDecoration decoration = new TextDecoration();
        decoration.font = font;
        decoration.color = textColor;
        decoration.pixelSize = pixelSize;
        decoration.style = fontStyle;
        TextFlow textFlow = JavaFXApplication.stringToTextFlow(buttonText, offsetColor, decoration, FontSmoothingType.LCD);
        textFlow.setTextAlignment(TextAlignment.CENTER);
        controllerNode.setGraphic(textFlow);
        
        if (node.backgroundColor != null) {
            if (node.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                node.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            Color fxBackgroundColor = getFxColor(node.backgroundColor);
            controllerNode.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        
        if ((node.borderWidth != null) && (node.borderWidth > 0)) {
            controllerNode.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
        }
        
        // TODO - Key bindings should be modifiable
        if ((node.keyBinding != null) && (!((JavaFXApplication)this.controller).keyBindings.containsKey(controllerNode))) {
            KeyCode keyBinding = null;
            if (null != node.keyBinding) switch (node.keyBinding) {
                case UP -> keyBinding = KeyCode.UP;
                case DOWN -> keyBinding = KeyCode.DOWN;
                case LEFT -> keyBinding = KeyCode.LEFT;
                case RIGHT -> keyBinding = KeyCode.RIGHT;
                default -> { logger.log(Level.WARNING, "Unsupported key binding {0}", node.keyBinding);
                }
            }
            
            if (keyBinding != null) {
                final KeyCode finalKeyBinding = keyBinding;
                final Button finalFxButton = controllerNode;
                EventHandler<KeyEvent> keyHandler = event -> {
                    if (event.getCode() == finalKeyBinding) {
                        finalFxButton.fire();
                        event.consume(); // Prevent key from triggering other events
                    }
                };
                
                ((JavaFXApplication)this.controller).primaryScene.addEventFilter(KeyEvent.KEY_PRESSED, keyHandler);
                ((JavaFXApplication)this.controller).keyBindings.put(controllerNode, keyHandler);
            }
        }
        
        String eventName;
        if (node.eventName == null) {
            eventName = node.name;
        } else {
            eventName = node.eventName.toString();
        }
        
        final Button fxButtonFinal = controllerNode;
        if (node.eventListener != null) {
            // setOnAction updates the only event handler and thus is safe to call many times without needing to remove the previous handler
            controllerNode.setOnAction(e -> {
                logger.log(Level.INFO, "Button selected: name={0}", node.name);
                if (!node.isMultiUse) {
                    fxButtonFinal.setDisable(true);
                }
                node.eventListener.onEvent(eventName, node.text);
            });
        }
        
        // Handle scaling
        
        double parentWidth;
        double parentHeight;
        if (this.parent.controllerNode instanceof Region region) {
            parentWidth = region.getPrefWidth();
            parentHeight = region.getPrefHeight();
        } else {
            Class<?> parentControlClass = this.parent.controllerNode.getClass();
            logger.log(Level.SEVERE, "Parent does not have a supported class: {0}", parentControlClass.getSimpleName());
            return;
        }
        
        if (node.scaleX != null) {
            controllerNode.setPrefWidth(parentWidth * node.scaleX);
        }

        if (node.scaleY == null) {
            // Fix the height of the button scaling beyond control.  This may be an issue caused by the scroll pane.
            // Calling bind replaces any previous binding.
            controllerNode.prefHeightProperty().bind(
                Bindings.createDoubleBinding(
                    () -> textFlow.prefHeight(fxButtonFinal.getWidth()) + 10, // +10 for button padding
                    controllerNode.widthProperty(), 
                    textFlow.widthProperty()
                )
            );
        } else {
            controllerNode.setPrefHeight(parentHeight * node.scaleY);
        }
        
        this.scaleNode(controllerNode);
    }
    
}
