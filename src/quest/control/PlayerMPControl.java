
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PlayerMPControl extends QuestControl {
    
    public static String NAME = "mp";
    
    public PlayerMPControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("PlayerMPControl: onExecute: tag=" + tag);
        int amount = this.quest.getPlayerMP();
        return Integer.toString(amount);
    }
    
}
