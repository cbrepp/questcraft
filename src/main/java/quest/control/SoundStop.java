
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SoundStop extends BaseQuestControl {
    
    public final Object soundFile;
    
    public SoundStop(Object soundFile) {
        this.soundFile = soundFile;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        
        String soundFileString = soundFile.toString();
        logger.log(Level.INFO, "Sound file is {0}", soundFileString);
        
        if (soundFileString.isEmpty()) {
            Quest.quest.appController.stopAllSounds();
        } else {
            Quest.quest.appController.stopSound(soundFileString, true);
        }
    }
    
}
