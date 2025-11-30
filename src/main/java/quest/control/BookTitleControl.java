
package quest.control;

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
        System.out.println("BookTitleControl: onExecute: tag=" + tag);
        String bookTitle = this.quest.book.title;
        return bookTitle;
    }
    
}
