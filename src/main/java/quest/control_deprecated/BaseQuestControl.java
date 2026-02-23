
package quest.control_deprecated;

import quest.Condition;
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
