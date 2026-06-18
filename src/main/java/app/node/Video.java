package app.node;

import app.EventListener;
import app.HorizontalAlignment;
import app.Layout;
import app.RelativeCoordinates;
import app.VerticalAlignment;
import app.color.Color;
import app.color.RGBColor;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author repp
 */
public class Video extends BaseNode {
    
    public EventListener eventListener;
    public Object eventName; // Default (null) is the video's name
    public String file;
    public Boolean loop = false;
    
    public Video (String name) {
        super(name);
    }
    
    public Video (String name, String file) {
        super(name);
        this.file = file;
    }
    
    @Override
    public RGBColor getColor() {
        return null;
    }
    
}
