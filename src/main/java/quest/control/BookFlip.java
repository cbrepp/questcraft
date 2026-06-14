
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class BookFlip extends BaseQuestControl {
    
    public BookFlip() {
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        Quest.quest.flipBook();
        Quest.quest.display(false);
    }
    
}
