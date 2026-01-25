package app.node;

import app.*;

/**
 *
 * @author repp
 */
public class Label extends BaseNode {
    
    public Color backgroundColor; // Default (null) is transparent
    public Boolean enableScroll; // Wrap the text in a scroll pane if needed
    public Double pixelSize; // Default is app controller's default pixel size
    public String text;
    public Color textColor; // Default (null) is either black or white depending on which color would best offset the background
    public String textFont; // Default (null) is the app controller's default font
    public FontStyle textStyle; // Default (null) is normal

    public Label (String name) {
        super(name);
    }
    
}
