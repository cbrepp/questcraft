
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SceneGoto extends BaseQuestControl {
    
    public Object sceneName;
    
    public SceneGoto(Object sceneName) {
        this.sceneName = sceneName;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        String sceneNameString = sceneName.toString();
        Quest.quest.startScene(sceneNameString, false, false);
        Quest.quest.display();
    }
    
}
