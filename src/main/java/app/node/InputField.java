package app.node;

import app.*;
import app.color.RGBColor;
import app.node.effect.BaseEffect;
import java.util.List;

/**
 *
 * @author repp
 */
public class InputField extends BaseNode {
    
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
    
}
