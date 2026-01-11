package app.node;

import app.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author repp
 */
public class Grid extends BaseNode {
    
    public Color backgroundColor; // Default (null) is transparent
    public int borderPadding = 0; // Default (0px) is no padding outside the border of each cell
    public int borderWidth = 1; // Default (1px) is thin borders
    public List<Group> cells = new ArrayList();
    public int cornerRadii = 0; // Default (0px) is square corners
    public int columns = 0; // Default (0) is dynamic columns count that attempts to square the grid based on the number of cells
    public EventListener listener;
    public int padding = 0; // Default (0px) is no padding inside the border of each cell
    public Boolean showBorders = true;

    public Grid (String name) {
        super(name);
    }
    
    public Grid(String name, Layout layout) {
        super(name, layout);
    }
    
}
