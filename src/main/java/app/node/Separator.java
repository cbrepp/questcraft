package app.node;

/**
 *
 * @author repp
 */
public class Separator extends BaseNode {
    
    public enum Orientation {HORIZONTAL, VERTICAL};
    
    public Orientation orientation;
    
    public Separator (String name, Orientation orientation) {
        super(name);
        this.orientation = orientation;
    }
    
}
