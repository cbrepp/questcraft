
package quest.control;

import quest.Book;

/**
 *
 * @author repp
 */
public class SetFocusOnSecondPageControl extends QuestControl {
    
    public static String NAME = "second-page";
    
    public SetFocusOnSecondPageControl(Book book) {
        super(book);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SetFocusOnSecondPageControl: onExecute: Changing display to second page");
        this.book.currentDisplayPage = Book.SECOND_PAGE;
        this.book.textRow = 1;
        return "";
    }
    
}
