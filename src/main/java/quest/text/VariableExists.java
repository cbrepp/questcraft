
package quest.text;

import static app.controller.BaseController.logger;
import java.io.Serializable;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class VariableExists implements Serializable {
    
    public final Object name;
    
    public VariableExists(Object name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        logger.log(Level.INFO, "Entered");
        
        String nameString = this.name.toString();
        
        String value;
        if (Quest.quest.variables.containsKey(nameString)) {
            value = "true";
            logger.log(Level.INFO, "Variable '{0}' found", nameString);
        } else {
            value = "false";
        }
        
        return value;
    }
    
}
