
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PlayerXPChangeControl extends QuestControl {
    
    public static String NAME = "xp-change";
    
    public PlayerXPChangeControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("PlayerXPChangeControl: onExecute: tag=" + tag);
        int amount = Integer.parseInt(getTagToken(tag, 1, false));
        Boolean refreshPage = Boolean.valueOf(getTagToken(tag, 2, true));
        this.quest.setPlayerXP(amount, refreshPage);
        if (refreshPage) {
            this.quest.display();
        }
        return "";
    }
    
}
