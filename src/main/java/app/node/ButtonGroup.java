package app.node;

import app.*;
import app.color.RGBColor;
import static app.controller.BaseController.logger;
import app.node.effect.BaseEffect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author repp
 */
public class ButtonGroup extends BaseNode implements BaseCompositeNode {
    
    public RGBColor backgroundColor; // Default (null) is system default
    public RGBColor buttonBackgroundColor; // Default (null) is system default
    public Map<Object, List<BaseEffect>> effectsButtons;
    public EventListener eventListener;
    public Boolean isEnabled = true;
    public Map<Object, Boolean> isEnabledButtons;
    public Boolean isMultiUse = true;
    public Map<Object, KeyboardKey> keyBindingButtons;
    public Double pixelSize; // Default is app controller's default pixel size
    public Integer spacerPixels = 10; // Default is 10 pixels separating the buttons horizontally and vertically
    public List<Object> text; // toString() will be invoked on each object to derive text
    public RGBColor textColor; // Default (null) is either black or white depending on which color would best offset the background
    public String textFont; // Default is the app controller's default font
    
    public ButtonGroup (String name) {
        super(name);
        this.effectsButtons = new HashMap();
        this.isEnabledButtons = new HashMap();
        this.keyBindingButtons = new HashMap();
        this.text = new ArrayList();
    }
    
    public ButtonGroup (String name, List<Object> text) {
        this(name);
        this.text = text;
    }
    
    @Override
    public RGBColor getColor() {
        return this.backgroundColor;
    }
    
    @Override
    public List<BaseNode> getChildren() {
        List<BaseNode> children = new ArrayList();
        
        // Build a map of effects for specific buttons
        Map<String, List<BaseEffect>> effectsOverrides = new HashMap();
        for (Map.Entry<Object, List<BaseEffect>> entry : this.effectsButtons.entrySet()) {
            Object object = entry.getKey();
            String buttonText = object.toString();
            List<BaseEffect> effects = entry.getValue();
            effectsOverrides.put(buttonText, effects);
        }
        
        // Build a map of isEnabled for specific buttons
        Map<String, Boolean> isEnabledOverrides = new HashMap();
        for (Map.Entry<Object, Boolean> entry : this.isEnabledButtons.entrySet()) {
            Object object = entry.getKey();
            String buttonText = object.toString();
            Boolean isEnabled = entry.getValue();
            isEnabledOverrides.put(buttonText, isEnabled);
        }
        
        // Build a map of keyBinding for specific buttons
        Map<String, KeyboardKey> keyBindings = new HashMap();
        for (Map.Entry<Object, KeyboardKey> entry : this.keyBindingButtons.entrySet()) {
            Object object = entry.getKey();
            String buttonText = object.toString();
            KeyboardKey keyBinding = entry.getValue();
            keyBindings.put(buttonText, keyBinding);
        }
        
        List<String> addedChildren = new ArrayList();
        for (Object object : this.text) {
            String buttonText = object.toString();
            String buttonName = this.getButtonName(buttonText);
            logger.log(Level.WARNING, "Adding button {0}", buttonName);
            if (addedChildren.contains(buttonName)) {
                logger.log(Level.WARNING, "Duplicate button {0} found, skipping", buttonText);
                continue;
            }
            addedChildren.add(buttonName);
            Button button = new Button(buttonName);
            button.backgroundColor = this.buttonBackgroundColor;
            button.eventListener = this.eventListener;
            button.eventName = this.name;
            if (isEnabledOverrides.containsKey(buttonText)) {
                button.isEnabled = isEnabledOverrides.get(buttonText);
                logger.log(Level.WARNING, "Button {0} has custom isEnabled of {1}", new Object[]{buttonText, button.isEnabled});
            } else {
                button.isEnabled = this.isEnabled;
            }            
            button.isMultiUse = this.isMultiUse;
            if (keyBindings.containsKey(buttonText)) {
                button.keyBinding = keyBindings.get(buttonText);
                logger.log(Level.WARNING, "Button {0} has key binding of {1}", new Object[]{buttonText, button.keyBinding});
            }
            button.pixelSize = this.pixelSize;
            button.text = buttonText;
            button.textColor = this.textColor;
            button.textFont = this.textFont;
            if (effectsOverrides.containsKey(buttonText)) {
                button.effects = effectsOverrides.get(buttonText);
                logger.log(Level.WARNING, "Button {0} has custom effects", buttonText);
            }
            children.add(button);
        }
        return children;
    }

    public String getButtonName(String buttonText) {
        String buttonName = this.name + "_" + buttonText;
        return buttonName;
    }
    
}
