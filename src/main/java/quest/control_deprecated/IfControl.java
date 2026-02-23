
package quest.control_deprecated;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class IfControl extends QuestControl {
    
    public static String NAME = "if";
    
    public IfControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("IfControl: onExecute: tag=" + tag);
        String expression = getTagToken(tag, 1, true);
        String value = this.evaluateExpression(expression);
        return value;
    }
    
}
