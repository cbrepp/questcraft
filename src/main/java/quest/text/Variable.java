
package quest.text;

import static app.controller.BaseController.logger;
import java.io.Serializable;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class Variable implements Serializable {
    
    public final Object name;
    
    public Variable(Object name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        logger.log(Level.INFO, "Entered");
        
        String nameString = this.name.toString();
        
        String value;
        if (Quest.quest.variables.containsKey(nameString)) {
            value = Quest.quest.variables.get(nameString);
            logger.log(Level.INFO, "Variable {0}='{1}'", new Object[]{nameString, value});
        } else {
            logger.log(Level.WARNING, "Variable {0} not found", nameString);
            value = "";
        }
        
        return value;
    }
    
}
