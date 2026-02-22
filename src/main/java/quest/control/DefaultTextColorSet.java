
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class DefaultTextColorSet extends BaseQuestControl {
    
    public app.Color textColor;
    
    public DefaultTextColorSet(app.Color textColor) {
        this.textColor = textColor;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        Quest.quest.appController.setDefaultFontColor(this.textColor);
    }
    
}
