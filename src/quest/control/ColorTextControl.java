
package quest.control;

import app.Color;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class ColorTextControl extends QuestControl {
    
    public static String NAME = "color";
    
    public ColorTextControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("ColorTextControl: onExecute: tag=" + tag);
        int red = Integer.parseInt(getTagArgument(tag, 1));
        int green = Integer.parseInt(getTagArgument(tag, 2));
        int blue = Integer.parseInt(getTagArgument(tag, 3));
        this.quest.textColor = new Color(red, green, blue);
        return "";
    }
    
}
