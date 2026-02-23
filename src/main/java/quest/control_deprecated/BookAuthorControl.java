
package quest.control_deprecated;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class BookAuthorControl extends QuestControl {
    
    public static String NAME = "book-author";
    
    public BookAuthorControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        logger.log(Level.INFO, "Entered: tag={0}", tag);
        String bookAuthor = this.quest.book.author;
        return bookAuthor;
    }
    
}
