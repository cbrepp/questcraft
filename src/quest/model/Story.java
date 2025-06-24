package quest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author repp
 */
public class Story implements Serializable {

    public List<String> contents;
    public Boolean isCheat;
    
    public Story() {
        this.contents = new ArrayList();
        this.isCheat = false;
    }
    
}
