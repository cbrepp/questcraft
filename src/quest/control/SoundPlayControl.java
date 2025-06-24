
package quest.control;

import app.Utility;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SoundPlayControl extends QuestControl {
    
    public static String NAME = "play-sound";
    
    public SoundPlayControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SoundPlayControl: onExecute: tag=" + tag);
        String soundFileName = getTagToken(tag, 1, true);
        Utility.playSound(soundFileName, false);
        return "";
    }
    
}
