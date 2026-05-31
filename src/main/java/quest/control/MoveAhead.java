
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class MoveAhead extends BaseQuestControl {
    
    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        
        String nextSceneName = Quest.quest.getNextScene(true);
        
        if (nextSceneName.equals(Quest.EDGE_OF_THE_WORLD)) {
            logger.log(Level.WARNING, "Moved past the edge of the world!");
        } else {
            Quest.quest.startScene(nextSceneName, false, false);
            Quest.quest.display();
        }
    }
    
}
