
package quest.control;

import quest.Book;

/**
 *
 * @author repp
 */
public class BreakControl extends QuestControl {
    
    public static String NAME = "br";
    
    public BreakControl(Book book) {
        super(book);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("BreakControl: onExecute: Breaking");
        this.book.textRow = this.book.textRow + 1;
        return "";
    }
    
}
