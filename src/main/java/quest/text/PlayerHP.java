
package quest.text;

import static app.controller.BaseController.logger;
import java.io.Serializable;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PlayerHP implements Serializable {
    
    public PlayerHP() {
    }
    
    @Override
    public String toString() {
        logger.log(Level.INFO, "Entered");
        int amount = Quest.quest.getPlayerHP();
        return Integer.toString(amount);
    }
    
}
