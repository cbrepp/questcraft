
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SetCursorButtonRowControl extends QuestControl {
    
    public static String NAME = "button-row";
    
    public SetCursorButtonRowControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SetCursorButtonRowControl: onExecute: tag=" + tag);
        this.quest.textRow = this.quest.buttonRow - this.quest.titleRow - 1;
        return "";
    }
    
}
