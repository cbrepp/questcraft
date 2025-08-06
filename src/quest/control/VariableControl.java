
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class VariableControl extends QuestControl {
    
    public static String NAME = "variable";
    
    public VariableControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("VariableControl: onExecute: tag=" + tag);
        
        String variable = getTagToken(tag, 1, false);
        
        String value;
        if (this.quest.variables.containsKey(variable)) {
            value = this.quest.variables.get(variable);
            System.out.println("VariableControl: onExecute: variable '" + variable + "' = '" + value + "'");
        } else {
            System.err.println("VariableControl: onExecute: variable '" + variable + "' not found!");
            value = "";
        }
        
        return value;
    }
    
}
