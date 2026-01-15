package app;

/**
 *
 * @author repp
 */
public class RelativeBounds {
    
    public RelativeCoordinates coordinates; // 0.0 = left/top edge of parent, 0.5 = center of parent, 1.0 = right/bottom edge of parent
    public double height; // Percentage of parent's height
    public double width; // 0.0 = left edge of parent, 0.5 = horizontal center of parent, 1.0 = right edge of parent
    
    public RelativeBounds(RelativeCoordinates coordinates, double width, double height) {
        this.coordinates = coordinates;
        this.width = width;
        this.height = height;
    }
    
}
