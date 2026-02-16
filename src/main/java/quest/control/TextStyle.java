
package quest.control;

import app.FontStyle;
import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class TextStyle extends BaseQuestControl {
    
    public FontStyle style;
    
    public TextStyle(FontStyle style) {
        this.style = style;
    }
    
    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        Quest.quest.defaultTextStyle = this.style;
    }
    
}
