
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SetPlayerDirectionControl extends QuestControl {
    
    public static String NAME = "set-player-direction";
    
    public SetPlayerDirectionControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SetPlayerDirectionControl: onExecute: tag=" + tag);
        String direction = getTagToken(tag, 1, true);
        this.quest.setPlayerDirection(direction);
        return "";
    }
    
}
