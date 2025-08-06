
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PlayerSymbolControl extends QuestControl {
    
    public static String NAME = "player-symbol";
    
    public PlayerSymbolControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("PlayerSymbolControl: onExecute: tag=" + tag);
        String symbol = this.quest.playerSymbol;
        return symbol;
    }
    
}
