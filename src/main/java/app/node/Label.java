package app.node;

import app.*;
import app.color.RGBColor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author repp
 */
public class Label extends BaseNode {
    
    public AntiAliasMethod antiAliasMethod; // Default (null) is text
    public RGBColor backgroundColor; // Default (null) is transparent
    public RGBColor borderColor; // Default (null) is either black or white depending on which color would best offset the background
    public Integer borderWidth; // Default (null) is no borders
    public List<Text> texts; // toString() is invoked on each Text object's text property to get the actual string value, allowing for dynamic text

    public Label (String name, List<Text> texts) {
        super(name);
        this.texts = texts;
    }
    
    public Label (String name, Object string, TextDecoration decoration) {
        this(name, new ArrayList());
        Text text = new Text(string, decoration);
        this.texts.add(text);
    }
    
    public Label (String name, Object string) {
        this(name, string, null);
    }
    
    public Label (String name) {
        this(name, null);
    }
    
    @Override
    public RGBColor getColor() {
        return this.backgroundColor;
    }
    
}
