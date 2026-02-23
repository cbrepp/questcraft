package quest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import quest.Condition;
import quest.control_deprecated.BaseQuestControl;

/**
 *
 * @author repp
 */
public class Story implements Serializable {

    public Condition condition;
    public List<String> contents;
    public List<BaseQuestControl> controls;
    public Boolean isSpell;
    public int mpCost;
    
    public Story() {
        this.contents = new ArrayList();
        this.controls = new ArrayList();
        this.isSpell = false;
        this.mpCost = 0;
    }
    
}
