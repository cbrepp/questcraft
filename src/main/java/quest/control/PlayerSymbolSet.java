
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PlayerSymbolSet extends BaseQuestControl {
    
    public Object symbol;
    
    public PlayerSymbolSet(Object symbol) {
        this.symbol = symbol;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        String symbolString = this.symbol.toString();
        Quest.quest.playerSymbol = symbolString;
    }
    
}
