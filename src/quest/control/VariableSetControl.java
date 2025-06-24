
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
        this.quest.variables.put(variable, value);
        return "";
    }
    
}
