
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
public class Scene implements Serializable {
    
    public Scene() {
    }
    
    @Override
    public String toString() {
        logger.log(Level.INFO, "Entered");
        Act act = Quest.quest.book.acts.get(Quest.quest.currentAct);
        quest.model.Scene scene = act.scenes.get(Quest.quest.currentScene);
        String sceneSymbol = scene.symbol;
        String sceneName = sceneSymbol + " " + Quest.quest.currentScene;
        return sceneName;
    }
    
}
