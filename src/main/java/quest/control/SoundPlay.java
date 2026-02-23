
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SoundPlay extends BaseQuestControl {
    
    public final Boolean loop; // Default (null) is false
    public final Object soundFile;
    
    public SoundPlay(Object soundFile, Boolean loop) {
        this.loop = loop;
        this.soundFile = soundFile;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        String soundFileString = soundFile.toString();
        Quest.quest.appController.playSound(soundFileString, loop);
    }
    
}
