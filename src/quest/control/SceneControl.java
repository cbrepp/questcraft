
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SceneControl extends QuestControl {
    
    public static String NAME = "scene";
    
    public SceneControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SceneControl: onExecute: tag=" + tag);
        String sceneName = this.quest.currentScene;
        return sceneName;
    }
    
}
