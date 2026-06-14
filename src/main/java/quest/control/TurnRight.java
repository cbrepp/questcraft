
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class TurnRight extends BaseQuestControl {
    
    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        switch (Quest.quest.getPlayerDirection().toUpperCase()) {
            case Quest.DIRECTION_EAST -> Quest.quest.setPlayerDirection(Quest.DIRECTION_SOUTH);
            case Quest.DIRECTION_NORTH -> Quest.quest.setPlayerDirection(Quest.DIRECTION_EAST);
            case Quest.DIRECTION_SOUTH -> Quest.quest.setPlayerDirection(Quest.DIRECTION_WEST);
            case Quest.DIRECTION_WEST -> Quest.quest.setPlayerDirection(Quest.DIRECTION_NORTH);
        }        
        Quest.quest.display(true);
    }
    
}
