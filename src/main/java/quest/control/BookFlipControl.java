
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class BookFlipControl extends QuestControl {
    
    public static String NAME = "flip-book";
    
    public BookFlipControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        logger.log(Level.INFO, "Entered: tag={0}", tag);
        this.quest.flipBook();
        this.quest.display();
        return "";
    }
    
}
