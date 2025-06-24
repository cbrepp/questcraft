
package quest.control;

import quest.model.Act;
import quest.model.Scene;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class NextSceneControl extends QuestControl {
    
    public static String NAME = "next-scene";
    
    public NextSceneControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("NextSceneControl: onExecute: tag=" + tag);
        String nextSceneName = this.quest.getNextScene();
        return nextSceneName;
    }
    
}
