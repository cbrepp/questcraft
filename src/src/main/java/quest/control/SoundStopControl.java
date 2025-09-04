
package quest.control;

import app.Utility;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SoundStopControl extends QuestControl {
    
    public static String NAME = "stop-sound";
    
    public SoundStopControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SoundStopControl: onExecute: tag=" + tag);
        String soundFileName = getTagToken(tag, 1, true);
        if (!soundFileName.equals("")) {
            Utility.stopSound(soundFileName, true);
        } else {
            Utility.stopAllSounds();
        }
        return "";
    }
    
}
