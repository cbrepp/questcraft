
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.model.Story;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class Subpage extends BaseQuestControl {
    
    public final Object subpage;
    
    public Subpage(Object subpage) {
        this.subpage = subpage;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        
        String subpageString = this.subpage.toString();
        
        Story story = Quest.quest.getSubpage(subpageString, false);
        if (story == null) {
            logger.log(Level.WARNING, "Subpage {0} is NOT defined", subpageString);
            return;
        }
        
        logger.log(Level.WARNING, "Displaying subpage {0}", subpageString);
        Quest.quest.displayPagev2(story.controls, true);
    }
    
}
