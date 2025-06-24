
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PlayerSymbolSetControl extends QuestControl {
    
    public static String NAME = "set-player-symbol";
    
    public PlayerSymbolSetControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("PlayerSymbolSetControl: onExecute: tag=" + tag);
        String symbol = getTagToken(tag, 1, true);
        this.quest.playerSymbol = symbol;
        return "";
    }
    
}
