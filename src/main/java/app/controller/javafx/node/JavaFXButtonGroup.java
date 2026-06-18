package app.controller.javafx.node;

import app.KeyboardKey;
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
import app.node.ButtonGroup;
import app.node.effect.BaseEffect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

/**
 *
 * @author repp
 */
public class JavaFXButtonGroup extends BaseJavaFXNode implements BaseCompositeNode {
    
    public JavaFXButtonGroup(app.node.ButtonGroup node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new FlowPane(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.ButtonGroup node = (app.node.ButtonGroup) this.node;
        FlowPane controllerNode = (FlowPane) this.controllerNode;
        
        controllerNode.setRowValignment(VPos.TOP);
        
        if (node.innerSpacerPixels != null) {
            controllerNode.setHgap(node.innerSpacerPixels);
            controllerNode.setVgap(node.innerSpacerPixels);
        }

        if (node.outerSpacerPixels != null) {
            controllerNode.setPadding(new Insets(node.outerSpacerPixels));
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
        
        this.scaleNode(controllerNode);
    }
    
    @Override
    public Map<? extends BaseNode, Layout> getChildren() {
        Map<BaseNode, Layout> children = new LinkedHashMap();
        
        ButtonGroup bg = (ButtonGroup) this.node;
        
        // Build a map of effects for specific buttons
        Map<String, List<BaseEffect>> effectsOverrides = new HashMap();
        for (Map.Entry<Object, List<BaseEffect>> entry : bg.effectsButtons.entrySet()) {
            Object object = entry.getKey();
            String buttonText = object.toString();
            List<BaseEffect> effects = entry.getValue();
            effectsOverrides.put(buttonText, effects);
        }
        
        // Build a map of isEnabled for specific buttons
        Map<String, Boolean> isEnabledOverrides = new HashMap();
        for (Map.Entry<Object, Boolean> entry : bg.isEnabledButtons.entrySet()) {
            Object object = entry.getKey();
            String buttonText = object.toString();
            Boolean isEnabled = entry.getValue();
            isEnabledOverrides.put(buttonText, isEnabled);
        }
        
        // Build a map of keyBinding for specific buttons
        Map<String, KeyboardKey> keyBindings = new HashMap();
        for (Map.Entry<Object, KeyboardKey> entry : bg.keyBindingButtons.entrySet()) {
            Object object = entry.getKey();
            String buttonText = object.toString();
            KeyboardKey keyBinding = entry.getValue();
            keyBindings.put(buttonText, keyBinding);
        }
        
        List<String> addedChildren = new ArrayList();
        for (Object object : bg.text) {
            String buttonText = object.toString();
            String buttonName = this.getButtonName(bg.name, buttonText);
            logger.log(Level.WARNING, "Adding button {0}", buttonName);
            if (addedChildren.contains(buttonName)) {
                logger.log(Level.WARNING, "Duplicate button {0} found, skipping", buttonText);
                continue;
            }
            addedChildren.add(buttonName);
            Button button = new Button(buttonName);
            button.backgroundColor = bg.buttonBackgroundColor;
            button.eventListener = bg.eventListener;
            button.eventName = bg.name;
            if (isEnabledOverrides.containsKey(buttonText)) {
                button.isEnabled = isEnabledOverrides.get(buttonText);
                logger.log(Level.WARNING, "Button {0} has custom isEnabled of {1}", new Object[]{buttonText, button.isEnabled});
            } else {
                button.isEnabled = bg.isEnabled;
            }            
            button.isMultiUse = bg.isMultiUse;
            if (keyBindings.containsKey(buttonText)) {
                button.keyBinding = keyBindings.get(buttonText);
                logger.log(Level.WARNING, "Button {0} has key binding of {1}", new Object[]{buttonText, button.keyBinding});
            }
            button.pixelSize = bg.pixelSize;
            button.text = buttonText;
            button.textColor = bg.textColor;
            button.textFont = bg.textFont;
            if (effectsOverrides.containsKey(buttonText)) {
                button.effects = effectsOverrides.get(buttonText);
                logger.log(Level.WARNING, "Button {0} has custom effects", buttonText);
            }
            children.put(button, null);
        }
        return children;
    }

    public String getButtonName(String groupName, String buttonText) {
        String buttonName = groupName + "_" + buttonText;
        return buttonName;
    }
    
}
