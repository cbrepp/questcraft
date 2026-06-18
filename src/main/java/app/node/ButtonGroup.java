package app.node;

import app.*;
import app.color.RGBColor;
import app.node.effect.BaseEffect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author repp
 */
public class ButtonGroup extends BaseNode {
    
    public RGBColor backgroundColor; // Default (null) is system default
    public RGBColor buttonBackgroundColor; // Default (null) is system default
    public Map<Object, List<BaseEffect>> effectsButtons;
    public EventListener eventListener;
    public Boolean isEnabled = true;
    public Map<Object, Boolean> isEnabledButtons;
    public Boolean isMultiUse = true;
    public Map<Object, KeyboardKey> keyBindingButtons;
    public Double pixelSize; // Default is app controller's default pixel size
    public Integer innerSpacerPixels = 10; // Default is 10 pixels separating the buttons horizontally and vertically
    public Integer outerSpacerPixels = 10; // Default is 10 pixels separating the border around the buttons
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
    
}
