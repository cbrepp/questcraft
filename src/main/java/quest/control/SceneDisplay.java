
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SceneDisplay extends BaseQuestControl {
    
    public final Object sceneName;
    
    public SceneDisplay(Object sceneName) {
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
