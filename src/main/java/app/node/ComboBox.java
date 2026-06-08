package app.node;

import app.Text;
import app.color.RGBColor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author repp
 */
public class ComboBox extends BaseNode {
    
    public Object defaultValue;
    public List<Text> values = new ArrayList();

    public ComboBox (String name) {
        super(name);
    }
    
    @Override
    public RGBColor getColor() {
        return null;
    }
    
}
