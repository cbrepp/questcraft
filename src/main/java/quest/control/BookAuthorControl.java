
package quest.control;

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
        System.out.println("BookAuthorControl: onExecute: tag=" + tag);
        String bookAuthor = this.quest.book.author;
        return bookAuthor;
    }
    
}
