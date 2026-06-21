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
import static app.controller.JavaFXApplication.DEFAULT_OFFSET_COLOR;
import static app.controller.JavaFXApplication.getFxColor;
import app.node.BaseDecoratedNode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.ListSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author repp
 */
public class JavaFXSpinner extends BaseJavaFXNode {
    
    public JavaFXSpinner(app.node.Spinner node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new Spinner(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.Spinner node = (app.node.Spinner) this.node;
        Spinner controllerNode = (Spinner) this.controllerNode;
        
        RGBColor offsetColor = new DecoratedOffsetColor(new OffsetColor(), this.parent);
        
        TextDecoration defaultTextDecoration = ((JavaFXApplication) this.controller).defaultTextDecorations.get(this.viewName);

        String defaultValue = null;
        if (node.defaultValue != null) {
            defaultValue = node.defaultValue.toString();
        }
        
        Boolean foundDefaultValue = false;
        List<String> values = new ArrayList();
        if (node.values != null) {
            for (Object object : node.values) {
                String stringValue = object.toString();
                values.add(stringValue);
                if ((defaultValue != null) && (defaultValue.equals(stringValue))) {
                    foundDefaultValue = true;
                }
            }
            ObservableList<String> spinnerValues = FXCollections.observableArrayList(values);
            ListSpinnerValueFactory<String> factory = new ListSpinnerValueFactory<>(spinnerValues);
            factory.setItems(spinnerValues);
            if (foundDefaultValue) {
                factory.setValue(defaultValue);
            } else {
                if (defaultValue != null) {
                    logger.log(Level.WARNING, "Default value is not member of value list! value={0}", defaultValue);
                }
            }
            controllerNode.setValueFactory(factory);
        }
        
        controllerNode.setEditable(false);
        controllerNode.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        if (node.wrapAround != null) {
            controllerNode.getValueFactory().setWrapAround(node.wrapAround);
        }
        
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
        
        if (node.backgroundColor != null) {
            if (node.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                node.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            Color fxBackgroundColor = getFxColor(node.backgroundColor);
            controllerNode.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            controllerNode.setBackground(Background.EMPTY);
        }
        
        TextField spinnerEditor = controllerNode.getEditor();
        spinnerEditor.setStyle(
            "-fx-alignment: center; " +
            "-fx-background-color: transparent; " +
            "-fx-text-fill: rgba(" + textColor.getRed() + ", " + textColor.getGreen() + ", " + textColor.getBlue() + ", " + textColor.getOpacity() + ");"
        );
        spinnerEditor.setFont(Font.font(font, FontWeight.BOLD, pixelSize));
        
        
        if ((node.borderWidth != null) && (node.borderWidth > 0)) {
            controllerNode.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
        }
        
        String eventName;
        if (node.eventName == null) {
            eventName = node.name;
        } else {
            eventName = node.eventName.toString();
        }
        
        if (defaultValue != null) {
            String finalDefaultValue = defaultValue;
            Platform.runLater(() -> {
                int currentIndex = values.indexOf(finalDefaultValue);
                if (currentIndex == 0) {
                    // If we are at the very first item, fade the left arrow
                    Node decrementBtn = controllerNode.lookup(".decrement-arrow-button");
                    if (decrementBtn != null) {
                        decrementBtn.setOpacity(0.3); // Looks faded/disabled
                    } else {
                        logger.log(Level.WARNING, "Decrement button does not exist after layout pass delay");
                    }
                } else if (currentIndex == values.size() - 1) {
                    // If we are at the very last item, fade the right arrow
                    Node incrementBtn = controllerNode.lookup(".increment-arrow-button");
                    if (incrementBtn != null) {
                        incrementBtn.setOpacity(0.3); // Looks faded/disabled
                    } else {
                        logger.log(Level.WARNING, "Increment button does not exist after layout pass delay");
                    }
                }
            });
        }
        
        // setOnAction updates the only event handler and thus is safe to call many times without needing to remove the previous handler
        controllerNode.valueProperty().addListener((obs, oldValue, newValue) -> {
            logger.log(Level.INFO, "Value selected: name={0}, oldValue={1}, newValue={2}", new Object[]{node.name, oldValue, newValue});
            if (node.eventListener != null) {
                node.eventListener.onEvent(eventName, newValue);
            }

            // Because the spinner control by default does not make the buttons appear disabled on either end, implement by adjusting opacity of the buttons
            if ((node.wrapAround == null) || (node.wrapAround == false)) {
                int currentIndex = values.indexOf(newValue);
                // Look up the internal arrow button nodes
                Node incrementBtn = controllerNode.lookup(".increment-arrow-button");
                Node decrementBtn = controllerNode.lookup(".decrement-arrow-button");

                if (incrementBtn != null && decrementBtn != null) {
                    // Reset both to full visibility first
                    incrementBtn.setOpacity(1.0);
                    decrementBtn.setOpacity(1.0);

                    if (currentIndex == 0) {
                        // If we are at the very first item, fade the left arrow
                        decrementBtn.setOpacity(0.3); // Looks faded/disabled
                    } else if (currentIndex == values.size() - 1) {
                        // If we are at the very last item, fade the right arrow
                        incrementBtn.setOpacity(0.3); // Looks faded/disabled
                    }
                }
            }
        });
        
        // Handle scaling
        this.scaleNode(controllerNode);
    }
    
}
