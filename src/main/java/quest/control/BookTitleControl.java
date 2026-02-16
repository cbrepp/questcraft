
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class BookTitleControl extends QuestControl {
    
    public static String NAME = "book-title";
    
    public BookTitleControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        logger.log(Level.INFO, "Entered: tag={0}", tag);
        String bookTitle = this.quest.book.title;
        return bookTitle;
    }
    
}
