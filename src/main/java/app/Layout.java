package app;

/**
 *
 * @author repp
 */
public class Layout {
    
    public Coordinates position;
    public Alignment horizontalAlignment;
    public Alignment verticalAlignment;
    
    public Layout(Coordinates position, Alignment horizontalAlignment, Alignment verticalAlignment) {
        this.position = position;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
    }
    
    public Layout(Alignment horizontalAlignment, Alignment verticalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
    }
}
