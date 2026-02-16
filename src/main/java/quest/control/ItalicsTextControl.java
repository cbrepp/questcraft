
package quest.control;

import app.FontStyle;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class ItalicsTextControl extends QuestControl {
    
    public static String NAME = "i";
    
    public ItalicsTextControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("ItalicsTextControl: onExecute: tag=" + tag);
        this.quest.defaultTextStyle = FontStyle.ITALIC;
        return "";
    }
    
}
