
package quest.control;

import app.FontStyle;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class UnderlineTextControl extends QuestControl {
    
    public static String NAME = "u";
    
    public UnderlineTextControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("UnderlineTextControl: onExecute: tag=" + tag);
        this.quest.textStyle = FontStyle.UNDERLINE_SINGLE;
        return "";
    }
    
}
