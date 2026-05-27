package app.node;

import app.*;
import app.color.RGBColor;
import static app.controller.BaseController.logger;
import app.node.effect.BaseEffect;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author repp
 */
public class InputField extends BaseNode implements BaseCompositeNode {
    
    public RGBColor backgroundColor; // Default (null) is system default
    public List<BaseEffect> buttonEffects;
    public RGBColor buttonBackgroundColor; // Default (null) is typically light gray
    public Integer buttonBorderWidth; // Default (null) is no borders
    public Object buttonText;
    public EventListener eventListener;
    public RGBColor fieldBackgroundColor; // Default (null) is transparent
    public Integer fieldBorderWidth = 1; // Default is a 1 pixel border
    public Integer fieldDisplayLength;
    public List<BaseEffect> fieldEffects;
    public Group group; // TODO - The group's collection is meaningless... need to decouple its styling from the collection
    public Object initialValue;
    public EventListener internalEventListener; // TODO - This shouldn't be public everywhere
    public Boolean isEnabled = true;
    public Boolean isMultiUse = true;
    public Boolean isUpperCase = false; // Default (false) is don't force upper case
    public KeyboardKey keyBinding;
    public Object label;
    public Integer length; // Default (null) is system default
    public Double pixelSize; // Default is app controller's default pixel size
    public Integer spacerPixels = 10; // Default is 10 pixels separating the buttons horizontally and vertically
    public RGBColor textColor; // Default (null) is either black or white depending on which color would best offset the background
    public String textFont; // Default is the app controller's default font   

    public InputField (String name) {
        super(name);
    }
    
    @Override
    public RGBColor getColor() {
        return this.backgroundColor;
    }
    
    @Override
    public List<? extends BaseNode> getChildren() {
        List<BaseNode> children = new ArrayList();
        
        String fieldName = this.name + "_field";
        logger.log(Level.WARNING, "Adding field {0}", fieldName);
        Field field = new Field(fieldName);
        if (this.fieldBackgroundColor != null) {
            field.backgroundColor = this.fieldBackgroundColor;
        }
        field.borderWidth = fieldBorderWidth;
        if (this.fieldDisplayLength != null) {
            field.displayLength = this.fieldDisplayLength;
        }
        field.effects = this.fieldEffects;
        if (this.initialValue != null) {
            field.initialValue = this.initialValue.toString();
        }
        field.isEnabled = this.isEnabled;
        field.isUpperCase = this.isUpperCase;
        if (this.label != null) {
            field.label = this.label.toString();
        }
        field.length = this.length;
        field.pixelSize = this.pixelSize;
        field.textColor = this.textColor;
        field.textFont = this.textFont;
        children.add(field);
        
        String buttonName = this.getButtonName();
        logger.log(Level.WARNING, "Adding button {0}", buttonName);
        Button button = new Button(buttonName);
        if (this.buttonBackgroundColor != null) {
            button.backgroundColor = this.buttonBackgroundColor;
        }
        if (this.buttonBorderWidth != null) {
            button.borderWidth = this.buttonBorderWidth;
        }
        button.effects = this.buttonEffects;
        button.eventListener = this.internalEventListener;
        
        // Button defers to parent's node's event listener
        button.eventName = this.name;
        button.isEnabled = this.isEnabled;
        button.isMultiUse = this.isMultiUse;
        button.keyBinding = this.keyBinding;
        button.pixelSize = this.pixelSize;
        if (this.buttonText != null) {
            button.text = this.buttonText.toString();
        }
        button.textColor = this.textColor;
        button.textFont = this.textFont;
        children.add(button);

        return children;
    }
    
    public String getButtonName() {
        String buttonName;
        
        if (this.buttonText == null) {
            buttonName = this.name + "_button";
        } else {
            buttonName = this.name + "_" + this.buttonText.toString();
        }
        
        return buttonName;
    }
    
}
