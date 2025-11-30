
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class ButtonRowControl extends QuestControl {
    
    public static String NAME = "button-row";
    
    public ButtonRowControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("ButtonRowControl: onExecute: tag=" + tag);
        int buttonRow = this.quest.buttonRow - this.quest.titleRow - 1; // TODO - This seems dumb
        this.quest.textRow = buttonRow;
        this.quest.textColumn = 1;
        return "";
    }
    
}
