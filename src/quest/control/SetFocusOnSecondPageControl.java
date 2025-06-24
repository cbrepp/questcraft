
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SetFocusOnSecondPageControl extends QuestControl {
    
    public static String NAME = "second-page";
    
    public SetFocusOnSecondPageControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SetFocusOnSecondPageControl: onExecute: tag=" + tag);
        this.quest.currentDisplayPage = Quest.SECOND_PAGE;
        this.quest.textRow = 1;
        return "";
    }
    
}
