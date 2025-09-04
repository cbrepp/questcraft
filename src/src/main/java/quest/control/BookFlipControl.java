
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class BookFlipControl extends QuestControl {
    
    public static String NAME = "flip-book";
    
    public BookFlipControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("BookFlipControl: onExecute: tag=" + tag);
        this.quest.flipBook();
        this.quest.display();
        return "";
    }
    
}
