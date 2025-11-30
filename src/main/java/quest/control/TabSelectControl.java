
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class TabSelectControl extends QuestControl {
    
    public static String NAME = "tab-select";
    
    public TabSelectControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("TabSelectControl: onExecute: tag=" + tag);
        String viewName = getTagToken(tag, 1, true);
        this.quest.appController.selectTab(viewName);
        return "";
    }
    
}
