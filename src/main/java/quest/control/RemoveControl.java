
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class RemoveControl extends QuestControl {
    
    public static String NAME = "remove";
    
    public RemoveControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("RemoveControl: onExecute: tag=" + tag);
        String nodeName = getTagToken(tag, 1, true);
        this.quest.appController.removeNode(this.quest.name, nodeName);
        return "";
    }
    
}
