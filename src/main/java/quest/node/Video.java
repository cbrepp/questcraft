
package quest.node;

import app.EventListener;
import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.model.Story;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class Video extends app.node.Video implements EventListener {

    public final Object onCompleteSubpage;
    
    public Video (String name, String videoFile, Object onCompleteSubpage) {
        super(name, videoFile);
        this.eventListener = this;
        this.onCompleteSubpage = onCompleteSubpage;
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        logger.log(Level.INFO, "eventName={0}, eventValue={1}", new Object[]{eventName, eventValue});
        
        if (!eventName.equals(this.name)) {
            return;
        }

        String subpageString = this.onCompleteSubpage.toString();
        
        Story story = Quest.quest.getSubpage(subpageString, false);
        if (story == null) {
            logger.log(Level.WARNING, "Subpage {0} is NOT defined", subpageString);
            return;
        }
        
        logger.log(Level.WARNING, "Displaying subpage {0}", subpageString);
        Quest.quest.displayStory(story, true);
    }
    
}
