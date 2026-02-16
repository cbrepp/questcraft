
package quest.control;

import static app.controller.BaseController.logger;
import java.io.Serializable;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class BookAuthor implements Serializable {
    
    public BookAuthor() {
    }
    
    @Override
    public String toString() {
        logger.log(Level.INFO, "Entered");
        String author = Quest.quest.book.author;
        return author;
    }
    
}
