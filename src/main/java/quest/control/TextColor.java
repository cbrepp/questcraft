
package quest.control;

import app.Color;
import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class TextColor extends BaseQuestControl {
    
    public Color color;
    
    public TextColor(Color color) {
        this.color = color;
    }
    
    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        Quest.quest.defaultTextColor = this.color;
    }
    
}
