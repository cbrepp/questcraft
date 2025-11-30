
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PlayerXPControl extends QuestControl {
    
    public static String NAME = "xp";
    
    public PlayerXPControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("PlayerXPControl: onExecute: tag=" + tag);
        int amount = this.quest.getPlayerXP();
        return Integer.toString(amount);
    }
    
}
