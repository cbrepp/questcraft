package app.node;

import app.Text;
import app.TextDecoration;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author repp
 */
public class ScrollingLabel extends Label {

    public ScrollingLabel (String name, List<Text> texts) {
        super(name);
        this.texts = texts;
    }
    
    public ScrollingLabel (String name, Object string, TextDecoration decoration) {
        this(name, new ArrayList());
        Text text = new Text(string, decoration);
        this.texts.add(text);
    }
    
    public ScrollingLabel (String name, Object string) {
        this(name, string, null);
    }
    
    public ScrollingLabel (String name) {
        this(name, null);
    }
    
}
