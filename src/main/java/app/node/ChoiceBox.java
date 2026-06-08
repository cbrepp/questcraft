package app.node;

import app.color.RGBColor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author repp
 */
public class ChoiceBox extends BaseNode {
    
    public Object defaultValue;
    public List<String> values = new ArrayList();

    public ChoiceBox (String name) {
        super(name);
    }
    
    @Override
    public RGBColor getColor() {
        return null;
    }
    
}
