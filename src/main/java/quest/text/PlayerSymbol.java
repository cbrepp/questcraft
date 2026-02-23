
package quest.text;

import static app.controller.BaseController.logger;
import java.io.Serializable;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PlayerSymbol implements Serializable {
    
    public PlayerSymbol() {
    }
    
    @Override
    public String toString() {
        logger.log(Level.INFO, "Entered");
        String symbol = Quest.quest.playerSymbol;
        return symbol;
    }
    
}
