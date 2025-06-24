
package quest.control;

import app.FontStyle;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class BoldTextControl extends QuestControl {
    
    public static String NAME = "b";
    
    public BoldTextControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("BoldTextControl: onExecute: tag=" + tag);
        this.quest.textStyle = FontStyle.BOLD;
        return "";
    }
    
}
