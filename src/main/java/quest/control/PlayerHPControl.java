
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PlayerHPControl extends QuestControl {
    
    public static String NAME = "hp";
    
    public PlayerHPControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("PlayerHPControl: onExecute: tag=" + tag);
        int amount = this.quest.getPlayerHP();
        return Integer.toString(amount);
    }
    
}
