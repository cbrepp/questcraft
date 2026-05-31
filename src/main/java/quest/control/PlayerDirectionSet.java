
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PlayerDirectionSet extends BaseQuestControl {
    
    public final Object direction;
    
    public PlayerDirectionSet(Object direction) {
        this.direction = direction;
    }
    
    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        // TODO - Should cast direction to enum to validate
        Quest.quest.setPlayerDirection(this.direction.toString());
    }
    
}
