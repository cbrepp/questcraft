
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class VariableSet extends BaseQuestControl {
    
    public Object variable;
    public Object value;
    
    public VariableSet(Object variable, Object value) {
        this.variable = variable;
        this.value = value;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        
        String variableString = this.variable.toString();
        String valueString = this.value.toString();
        
        logger.log(Level.INFO, "Setting {0} to '{1}'", new Object[]{variableString, valueString});
        Quest.quest.variables.put(variableString, valueString);
    }
    
}
