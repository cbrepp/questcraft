package quest.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author repp
 */
public abstract class BookPart implements Serializable {

    public Map<String, Story> subpages;
    
    public BookPart() {
        this.subpages = new HashMap();
    }
    
}
