
package quest.control;

import app.FontStyle;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class BoldTextOffControl extends QuestControl {
    
    public static String NAME = "/b";
    
    public BoldTextOffControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("BoldTextOffControl: onExecute: tag=" + tag);
        this.quest.textStyle = FontStyle.NORMAL;
        return "";
    }
    
}
