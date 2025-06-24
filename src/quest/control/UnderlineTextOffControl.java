
package quest.control;

import app.FontStyle;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class UnderlineTextOffControl extends QuestControl {
    
    public static String NAME = "/u";
    
    public UnderlineTextOffControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("UnderlineTextOffControl: onExecute: tag=" + tag);
        this.quest.textStyle = FontStyle.NORMAL;
        return "";
    }
    
}
