
package quest.control;

import java.util.ArrayList;
import java.util.List;
import quest.view.Quest;
import static quest.view.Quest.NEW_SCENE;

/**
 *
 * @author repp
 */
public class ObservedSceneAddControl extends QuestControl {
    
    public static String NAME = "observed-scene-add";
    
    public ObservedSceneAddControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("ObservedSceneAddControl: onExecute: tag=" + tag);
        
        // Add or update the quantity for the item
        String sceneName = getTagToken(tag, 1, true);
        List<String> observedActScenes = this.quest.observedScenes.get(this.quest.currentAct);
        if (observedActScenes == null) {
            observedActScenes = new ArrayList();
            this.quest.observedScenes.put(this.quest.currentAct, observedActScenes);
        }
        if (!observedActScenes.contains(sceneName)) {
            observedActScenes.add(sceneName);
        }
        this.quest.publishEvent(NEW_SCENE, sceneName);
        
        return "";
    }
    
}
