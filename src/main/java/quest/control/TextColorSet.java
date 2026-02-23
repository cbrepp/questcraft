
package quest.control;

import app.Color;
import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class TextColorSet extends BaseQuestControl {
    
    public Color color;
    
    public TextColorSet(Color color) {
        this.color = color;
    }
    
    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        Quest.quest.appController.setDefaultFontColor(this.color);
    }
    
}
