
package quest.control_deprecated;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SendToBackControl extends QuestControl {
    
    public static String NAME = "send-to-back";
    
    public SendToBackControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SendToBackControl: onExecute: tag=" + tag);
        String controlName = getTagToken(tag, 1, true);
        this.quest.appController.sendToBack(this.quest.name, controlName);
        return "";
    }
    
}
