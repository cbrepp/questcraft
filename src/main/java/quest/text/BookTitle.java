
package quest.text;

import static app.controller.BaseController.logger;
import java.io.Serializable;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class BookTitle implements Serializable {
    
    public BookTitle() {
    }
    
    @Override
    public String toString() {
        logger.log(Level.INFO, "Entered");
        String author = Quest.quest.book.title;
        return author;
    }
    
}
