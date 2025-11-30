
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PlayerMPChangeControl extends QuestControl {
    
    public static String NAME = "mp-change";
    
    public PlayerMPChangeControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("PlayerMPChangeControl: onExecute: tag=" + tag);
        int amount = Integer.parseInt(getTagToken(tag, 1, false));
        Boolean refreshPage = Boolean.valueOf(getTagToken(tag, 2, true));
        this.quest.setPlayerMP(amount, refreshPage);
        return "";
    }
    
}
