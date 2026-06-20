package app.node;

import app.EventListener;
import app.color.RGBColor;

/**
 *
 * @author repp
 */
public class Video extends BaseNode {
    
    public EventListener eventListener;
    public Boolean enableSkip = false;
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
