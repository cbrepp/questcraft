
package quest.control;

import app.FontStyle;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class ItalicsTextOffControl extends QuestControl {
    
    public static String NAME = "/i";
    
    public ItalicsTextOffControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("ItalicsTextOffControl: onExecute: tag=" + tag);
        this.quest.textStyle = FontStyle.NORMAL;
        return "";
    }
    
}
