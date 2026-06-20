package app;

import java.io.Serializable;

/**
 *
 * @author repp
 */
public class RelativeCoordinates implements Serializable {
    
    public double x; // 0.0 = left edge of parent, 0.5 = horizontal center of parent, 1.0 = right edge of parent
    public double y; // 0.0 = top edge of parent, 0.5 = vertical center of parent, 1.0 = bottom edge of parent
    
    public RelativeCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
}
