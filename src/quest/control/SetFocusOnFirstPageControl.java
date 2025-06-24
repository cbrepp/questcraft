
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SetFocusOnFirstPageControl extends QuestControl {
    
    public static String NAME = "first-page";
    
    public SetFocusOnFirstPageControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SetFocusOnFirstPageControl: onExecute: tag=" + tag);
        this.quest.currentDisplayPage = Quest.FIRST_PAGE;
        this.quest.textRow = 1;
        return "";
    }
    
}
