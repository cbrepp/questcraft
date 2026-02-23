
package quest.control;

import app.Layout;
import static app.controller.BaseController.logger;
import app.node.BaseNode;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class Illustrate extends BaseQuestControl {
    
    public final Layout layout;
    public final BaseNode node;
    
    public Illustrate(BaseNode node, Layout layout) {
        this.node = node;
        this.layout = layout;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        
        if (this.node == null) {
            logger.log(Level.WARNING, "No node");
            return;
        }
        
        Quest.quest.displayIllustrationNode(node, layout);
    }
    
}
