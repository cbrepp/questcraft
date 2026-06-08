
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class NodeRemove extends BaseQuestControl {
    
    public final Object nodeName;
    
    public NodeRemove(Object nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        String nodeName = this.nodeName.toString();
        Quest.quest.appController.removeNode(Quest.quest.name, nodeName);
    }
    
}
