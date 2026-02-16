
package quest.control;

import static app.controller.BaseController.logger;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class BookLastUpdatedDate implements Serializable {
    
    public BookLastUpdatedDate() {
    }
    
    @Override
    public String toString() {
        logger.log(Level.INFO, "Entered");
        String bookLastUpdatedDate = Quest.quest.book.updateDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.getDefault()));
        return bookLastUpdatedDate;
    }
    
}
