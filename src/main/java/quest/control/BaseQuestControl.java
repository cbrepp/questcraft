
package quest.control;

import java.io.Serializable;

/**
 *
 * @author repp
 */
public abstract class BaseQuestControl implements Serializable {
    
    public Condition condition;
    
    public void onExecute() {
    }
    
}
