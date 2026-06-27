package app.node;

import app.*;
import app.color.RGBColor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author repp
 */
public class Grid extends BaseNode {
    
    public RGBColor backgroundColor; // Default (null) is transparent
    public int borderPadding = 0; // Default (0px) is no padding outside the border of each cell
    public int borderWidth = 1; // Default (1px) is thin borders
    public List<BaseNode> cells = new ArrayList();
    public int cornerRadii = 0; // Default (0px) is square corners
    public int columns = 0; // Default (0) is dynamic columns count that attempts to square the grid based on the number of cells
    public Map<Integer, HorizontalAlignment> columnHAlignments; // Default (null) lets the grid compute column alignment
    public Map<Integer, VerticalAlignment> columnVAlignments; // Default (null) lets the grid compute column alignment
    public List<Double> columnWidths; // Default (null) lets the grid compute column widths.  Column widths are fractional (0.1 == 10%).
    public Boolean expandCells = true;
    public EventListener listener;
    public int padding = 0; // Default (0px) is no padding inside the border of each cell
    public Boolean showBorders = true;
    
    public Grid (String name) {
        super(name);
    }
    
    @Override
    public RGBColor getColor() {
        return this.backgroundColor;
    }
    
}
