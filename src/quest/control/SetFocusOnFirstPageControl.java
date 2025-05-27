
package quest.control;

import quest.Book;

/**
 *
 * @author repp
 */
public class SetFocusOnFirstPageControl extends QuestControl {
    
    public static String NAME = "first-page";
    
    public SetFocusOnFirstPageControl(Book book) {
        super(book);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SetFocusOnFirstPageControl: onExecute: Changing display to first page");
        this.book.currentDisplayPage = Book.FIRST_PAGE;
        this.book.textRow = 1;
        return "";
    }
    
}
