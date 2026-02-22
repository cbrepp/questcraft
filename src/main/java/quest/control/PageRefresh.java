
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PageRefresh extends BaseQuestControl {
    
    public PageRefresh() {
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        Quest.quest.display();
    }
    
}
