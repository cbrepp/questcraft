package app.control;

import app.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author repp
 */
public abstract class Group extends BaseControl {
    
    public int borderWidth = 1; // Default (1) is thin borders
    public List<BaseControl> list = new ArrayList();

    public Group(String name, Layout layout) {
        this.name = name;
        this.layout = layout;
    }
    
}
