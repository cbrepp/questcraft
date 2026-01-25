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
    
    public HorizontalAlignment horizontalAlignment;
    public RelativeCoordinates position;
    public VerticalAlignment verticalAlignment;
    
    public Layout(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
    }
    
    public Layout(RelativeCoordinates position, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
        this.position = position;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
    }
    
    public Layout copy() {
        HorizontalAlignment horizontalAlignment;
        if (this.horizontalAlignment == null) {
            horizontalAlignment = null;
        } else {
            horizontalAlignment = this.horizontalAlignment;
        }
        
        RelativeCoordinates position;
        if (this.position == null) {
            position = null;
        } else {
            position = new RelativeCoordinates(this.position.x, this.position.y);
        }
        
        VerticalAlignment verticalAlignment;
        if (this.verticalAlignment == null) {
            verticalAlignment = null;
        } else {
            verticalAlignment = this.verticalAlignment;
        }
        
        Layout copy = new Layout(position, horizontalAlignment, verticalAlignment);
        
        return copy;
    }
}
