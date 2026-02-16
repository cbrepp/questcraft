
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class ActGotoControl extends QuestControl {
    
    public static String NAME = "goto-act";
    
    public ActGotoControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        logger.log(Level.INFO, "Entered: tag={0}", tag);
        String actName = getTagToken(tag, 1, true);
        this.quest.startAct(actName);
        this.quest.display();
        return "";
    }
    
}
