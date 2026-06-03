package app;

import app.color.RGBColor;
import java.io.Serializable;

/**
 *
 * @author repp
 */
public class TextDecoration implements Serializable {
    
    public Double pixelSize; // Default is app controller's default pixel size
    public RGBColor color; // Default (null) is either black or white depending on which color would best offset the background
    public EventListener eventListener;
    public String font; // Default (null) is the app controller's default font
    public FontStyle style; // Default (null) is normal
    
    @Override
    public String toString() {
        return "pixelSize=" + this.pixelSize + ",  color=" + this.color + ", eventListener=" + this.eventListener + ", font=" + this.font + ", style=" + this.style;
    }
    
}
