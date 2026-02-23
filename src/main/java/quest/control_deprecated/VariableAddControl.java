
package quest.control_deprecated;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class VariableAddControl extends QuestControl {
    
    public static String NAME = "variable-add";
    
    public VariableAddControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("VariableAddControl: onExecute: tag=" + tag);
        
        String variable = getTagToken(tag, 1, false);
        int delta = Integer.parseInt(getTagToken(tag, 2, true));
        
        // If the variable has not yet been defined, error
        if (!this.quest.variables.containsKey(variable)) {
            System.err.println("VariableSetControl: onExecute: Variable " + variable + " is not defined!");
            return "";
        }

        int currentValue = Integer.parseInt(this.quest.variables.get(variable));
        int sum = currentValue + delta;
        System.out.println("VariableAddControl: onExecute: " + currentValue + " + " + delta + " = " + sum);

        this.quest.variables.put(variable, Integer.toString(sum));
        
        return "";
    }
    
}
