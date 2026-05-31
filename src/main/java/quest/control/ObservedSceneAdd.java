
package quest.control;

import static app.controller.BaseController.logger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;
import static quest.view.Quest.NEW_SCENE;

/**
 *
 * @author repp
 */
public class ObservedSceneAdd extends BaseQuestControl {
    
    public final Object sceneName;
    
    public ObservedSceneAdd(Object sceneName) {
        this.sceneName = sceneName;
    }
    
    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        
        List<String> observedActScenes = Quest.quest.observedScenes.get(Quest.quest.currentAct);
        if (observedActScenes == null) {
            observedActScenes = new ArrayList();
            Quest.quest.observedScenes.put(Quest.quest.currentAct, observedActScenes);
        }
        
        String calculatedSceneName = this.sceneName.toString();
        if (!observedActScenes.contains(calculatedSceneName)) {
            observedActScenes.add(calculatedSceneName);
        }
        
        Quest.quest.publishEvent(NEW_SCENE, calculatedSceneName);
    }
    
}
