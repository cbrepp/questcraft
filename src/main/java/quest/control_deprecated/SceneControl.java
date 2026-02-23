
package quest.control_deprecated;

import quest.model.Act;
import quest.model.Scene;
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
        Act act = this.quest.book.acts.get(this.quest.currentAct);
        Scene scene = act.scenes.get(this.quest.currentScene);
        String sceneSymbol = scene.symbol;
        String sceneName = sceneSymbol + " " + this.quest.currentScene;
        return sceneName;
    }
    
}
