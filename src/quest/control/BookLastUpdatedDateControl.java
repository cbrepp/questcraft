
package quest.control;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
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
        System.out.println("BookLastUpdatedDateControl: onExecute: tag=" + tag);
        String bookLastUpdatedDate = this.quest.book.updateDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.getDefault()));
        return bookLastUpdatedDate;
    }
    
}
