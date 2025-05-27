
package quest.control;

import quest.Book;

/**
 *
 * @author repp
 */
public class DoubleQuoteControl extends QuestControl {
    
    public static String NAME = "quote";
    
    public DoubleQuoteControl(Book book) {
        super(book);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("DoubleQuoteControl: onExecute: Translating to double quote");
        String doubleQuote = "\"";
        return doubleQuote;
    }
    
}
