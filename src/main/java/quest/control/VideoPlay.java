
package quest.control;

import app.EventListener;
import app.HorizontalAlignment;
import app.Layout;
import app.RelativeCoordinates;
import app.VerticalAlignment;
import app.color.Color;
import static app.controller.BaseController.logger;
import app.node.Rectangle;
import app.node.Video;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.model.Story;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class VideoPlay extends BaseQuestControl implements EventListener {
    
    public final Object onCompleteSubpage;
    public final Object videoFile;
    
    private String videoFileString;
    
    public VideoPlay(Object videoFile, Object onCompleteSubpage) {
        this.videoFile = videoFile;
        this.onCompleteSubpage = onCompleteSubpage;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        
        Rectangle overlay = new Rectangle("overlay");
        overlay.color = new Color(Color.BLACK, 0.5);
        overlay.scaleX = 1.0;
        overlay.scaleY = 1.0;
        Quest.quest.appController.addNode(Quest.quest.name, Quest.quest.name, overlay, new Layout(new RelativeCoordinates(0.0, 0.0), HorizontalAlignment.CENTER, VerticalAlignment.CENTER));

        this.videoFileString = this.videoFile.toString();
        Video video = new Video(this.videoFileString);
        video.file = this.videoFileString;
        video.eventListener = this;
        Quest.quest.appController.addNode(Quest.quest.name, Quest.quest.name, video, new Layout(new RelativeCoordinates(0.0, 0.0), HorizontalAlignment.CENTER, VerticalAlignment.CENTER));
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        logger.log(Level.INFO, "eventName={0}, eventValue={1}", new Object[]{eventName, eventValue});
        
        if (!eventName.equals(this.videoFileString)) {
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
