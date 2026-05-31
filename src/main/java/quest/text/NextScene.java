
package quest.text;

import static app.controller.BaseController.logger;
import java.io.Serializable;
import java.util.logging.Level;
import quest.model.Act;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class NextScene implements Serializable {
    
    public NextScene() {
    }
    
    @Override
    public String toString() {
        logger.log(Level.INFO, "Entered");
        String nextSceneId = Quest.quest.getNextScene(true);
        Act act = Quest.quest.book.acts.get(Quest.quest.currentAct);
        logger.log(Level.INFO, "Act {0}'s next scene is {1}", new Object[]{Quest.quest.currentAct, nextSceneId});
        String nextSceneName;
        if (!nextSceneId.equals(Quest.EDGE_OF_THE_WORLD)) {
            quest.model.Scene nextScene = act.scenes.get(nextSceneId);
            String nextSceneSymbol = nextScene.symbol;
            nextSceneName = nextSceneSymbol + " " + nextSceneId;
        } else {
            nextSceneName = nextSceneId;
        }
        return nextSceneName;
    }
    
}
