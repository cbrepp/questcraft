
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PlayerDirectionControl extends QuestControl {
    
    public static String NAME = "player-direction";
    
    public PlayerDirectionControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("PlayerDirectionControl: onExecute: tag=" + tag);
        return this.quest.getPlayerDirection();
    }
    
}
