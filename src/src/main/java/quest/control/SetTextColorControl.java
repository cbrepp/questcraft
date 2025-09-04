
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SetTextColorControl extends QuestControl {
    
    public static String NAME = "color";
    
    public SetTextColorControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SetTextColorControl: onExecute: tag=" + tag);
        int red = Integer.parseInt(getTagArgument(tag, 1));
        int green = Integer.parseInt(getTagArgument(tag, 2));
        int blue = Integer.parseInt(getTagArgument(tag, 3));
        this.quest.textColor = new app.Color(red, green, blue);
        return "";
    }
    
}
