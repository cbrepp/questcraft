
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class VariableSetControl extends QuestControl {
    
    public static String NAME = "variable-set";
    
    public VariableSetControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("VariableSetControl: onExecute: tag=" + tag);
        
        String variable = getTagToken(tag, 1, false);
        String value = getTagToken(tag, 2, true);
        
        // If the variable is named the same as a quest control, error
        if (this.quest.questControls.get(variable) != null) {
            System.err.println("VariableSetControl: A variable can NOT be named the same as a control tag!");
            return "";
        }
        
        // Translate the variable if it's the name of a quest control
        if (this.quest.questControls.containsKey(value)) {
            // Special handling for getting the value from a control
            String questControlTag = '<' + value + '>';
            QuestControl control = this.quest.questControls.get(value);
            System.out.println("Quest: displayPage: Executing tag " + value);
            value = control.onExecute(questControlTag);
        }
        
        this.quest.variables.put(variable, value);
        
        return "";
    }
    
}
