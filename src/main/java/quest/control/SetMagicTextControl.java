
package quest.control;

import app.Color;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SetMagicTextControl extends QuestControl {
    
    public static String NAME = "set-magic-text";
    
    public SetMagicTextControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SetMagicTextControl: onExecute: tag=" + tag);
        String value = getTagToken(tag, 1, false);
        Boolean isOn = (value.toLowerCase().equals("true"));
        if (isOn) {
            this.quest.defaultTextColor = Color.DARK_MAGENTA;
        } else {
            this.quest.defaultTextColor = null;
        }
        return "";
    }
    
}
