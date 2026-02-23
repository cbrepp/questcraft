
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class ActDisplay extends BaseQuestControl {
    
    public final Object actName;
    
    public ActDisplay(Object actName) {
        this.actName = actName;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        String actNameString = this.actName.toString();
        Quest.quest.startAct(actNameString);
        Quest.quest.display();
    }
    
}
