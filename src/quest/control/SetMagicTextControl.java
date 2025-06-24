
package quest.control;

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
        this.quest.magicText = (value.toLowerCase().equals("true"));
        return "";
    }
    
}
