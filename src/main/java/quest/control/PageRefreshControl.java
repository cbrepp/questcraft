
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PageRefreshControl extends QuestControl {
    
    public static String NAME = "page-refresh";
    
    public PageRefreshControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("PageRefreshControl: onExecute: tag=" + tag);
        this.quest.display();
        return "";
    }
    
}
