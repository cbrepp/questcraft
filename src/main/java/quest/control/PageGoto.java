
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class PageGoto extends BaseQuestControl {
    
    public final Object page;
    
    public PageGoto(Object page) {
        this.page = page;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        String pageName = this.page.toString();
        logger.log(Level.WARNING, "Displaying page {0}", pageName);
        Quest.quest.currentPage = pageName;
        Quest.quest.display(false);
    }
    
}
