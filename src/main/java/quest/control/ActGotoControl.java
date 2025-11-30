
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class ActGotoControl extends QuestControl {
    
    public static String NAME = "goto-act";
    
    public ActGotoControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("ActGotoControl: onExecute: tag=" + tag);
        String actName = getTagToken(tag, 1, true);
        this.quest.startAct(actName);
        this.quest.display();
        return "";
    }
    
}
