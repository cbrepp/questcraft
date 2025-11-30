
package quest.control;

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
            this.quest.appController.stopSound(soundFileName, true);
        } else {
            this.quest.appController.stopAllSounds();
        }
        return "";
    }
    
}
