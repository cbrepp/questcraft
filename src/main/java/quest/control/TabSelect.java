
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class TabSelect extends BaseQuestControl {
    
    public final Object viewName;
    
    public TabSelect(Object viewName) {
        this.viewName = viewName;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        String viewNameString = viewName.toString();
        Quest.quest.appController.selectTab(viewNameString);
    }
}
