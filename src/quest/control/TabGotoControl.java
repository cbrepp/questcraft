
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class TabGotoControl extends QuestControl {
    
    public static String NAME = "goto-tab";
    
    public TabGotoControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("TabGotoControl: onExecute: tag=" + tag);
        this.quest.appController.displayView(quest);
        return "";
    }
    
}
