
package quest.control_deprecated;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SendToFrontControl extends QuestControl {
    
    public static String NAME = "send-to-front";
    
    public SendToFrontControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SendToFrontControl: onExecute: tag=" + tag);
        String controlName = getTagToken(tag, 1, true);
        this.quest.appController.sendToFront(this.quest.name, controlName);
        return "";
    }
    
}
