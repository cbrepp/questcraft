package app.node;

import app.*;
import app.color.RGBColor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author repp
 */
public class Spinner extends BaseNode {
    
    public RGBColor backgroundColor; // Default (null) is system default
    public Integer borderWidth; // Default (null) is no borders
    public Object defaultValue;
    public EventListener eventListener;
    public Object eventName; // Default (null) is the button's name
    public Boolean isEnabled = true;
    public Double pixelSize; // Default is app controller's default pixel size
    public List<Object> values = new ArrayList();
    public RGBColor textColor; // Default (null) is either black or white depending on which color would best offset the background
    public String textFont; // Default is the app controller's default font
    public Boolean wrapAround; // Default (null) is false

    public Spinner (String name) {
        super(name);
    }

    public Spinner (String name, List<Object> values) {
        super(name);
        this.values = values;
    }
    
    @Override
    public RGBColor getColor() {
        return this.backgroundColor;
    }
    
}
