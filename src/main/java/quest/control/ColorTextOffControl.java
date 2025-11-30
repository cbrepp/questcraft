
package quest.control;

import app.Color;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class ColorTextOffControl extends QuestControl {
    
    public static String NAME = "/color";
    
    public ColorTextOffControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("ColorTextOffControl: onExecute: tag=" + tag);
        this.quest.textColor = new Color(0, 0, 0);
        return "";
    }
    
}
