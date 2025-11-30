
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PageGotoControl extends QuestControl {
    
    public static String NAME = "goto-page";
    
    public PageGotoControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("PageGotoControl: onExecute: tag=" + tag);
        String pageName = getTagToken(tag, 1, true);
        this.quest.currentPage = pageName;
        this.quest.display();
        return "";
    }
    
}
