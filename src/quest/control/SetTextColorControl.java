
package quest.control;

import quest.Book;

/**
 *
 * @author repp
 */
public class SetTextColorControl extends QuestControl {
    
    public static String NAME = "color";
    
    public SetTextColorControl(Book book) {
        super(book);
    }
    
    @Override
    public String onExecute(String tag) {
        int red = Integer.parseInt(getTagArgument(tag, 1));
        int green = Integer.parseInt(getTagArgument(tag, 2));
        int blue = Integer.parseInt(getTagArgument(tag, 3));
        System.out.println("SetTextColorControl: onExecute: Text color=" + red + "+" + green + "+" + blue);
        this.book.textColor = new app.Color(red, green, blue);
        return "";
    }
    
}
