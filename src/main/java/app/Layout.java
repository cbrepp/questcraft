package app;

/**
 * (horizontal alignment, vertical alignment)
 * (LEFT, TOP)
 * (LEFT, CENTER)
 * (LEFT, BOTTOM)
 * (CENTER, TOP)
 * (CENTER, CENTER)
 * (CENTER, BOTTOM)
 * (RIGHT, TOP)
 * (RIGHT, CENTER)
 * (RIGHT, BOTTOM)
 * 
 * @author repp
 */
public class Layout {
    
    public RelativeCoordinates position;
    public HorizontalAlignment horizontalAlignment;
    public VerticalAlignment verticalAlignment;
    
    public Layout(RelativeCoordinates position, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
        this.position = position;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
    }
    
    public Layout(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
    }
}
