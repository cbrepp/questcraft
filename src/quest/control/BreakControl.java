
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class BreakControl extends QuestControl {
    
    public static String NAME = "br";
    
    public BreakControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("BreakControl: onExecute: tag=" + tag);
        this.quest.textRow = this.quest.textRow + 1;
        return "";
    }
    
}
