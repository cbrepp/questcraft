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
    public Boolean isSpell;
    public int mpCost;
    
    public Story() {
        this.contents = new ArrayList();
        this.isSpell = false;
        this.mpCost = 0;
    }
    
}
