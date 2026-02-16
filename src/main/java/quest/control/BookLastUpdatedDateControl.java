
package quest.control;

import static app.controller.BaseController.logger;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class BookLastUpdatedDateControl extends QuestControl {
    
    public static String NAME = "book-last-updated-date";
    
    public BookLastUpdatedDateControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        logger.log(Level.INFO, "Entered: tag={0}", tag);
        String bookLastUpdatedDate = this.quest.book.updateDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.getDefault()));
        return bookLastUpdatedDate;
    }
    
}
