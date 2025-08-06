
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PlayerHPChangeControl extends QuestControl {
    
    public static String NAME = "hp-change";
    
    public PlayerHPChangeControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("PlayerHPChangeControl: onExecute: tag=" + tag);
        int amount = Integer.parseInt(getTagToken(tag, 1, false));
        Boolean refreshPage = Boolean.valueOf(getTagToken(tag, 2, true));
        this.quest.setPlayerHP(amount, refreshPage);
        if (refreshPage) {
            this.quest.display();
        }
        return "";
    }
    
}
