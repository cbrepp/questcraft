
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class MoveBackControl extends QuestControl {
    
    public static String NAME = "move-back";
    
    public MoveBackControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("MoveBackControl: onExecute: tag=" + tag);
        
        String nextSceneName = this.quest.getNextScene(false);
        
        if (nextSceneName.equals(Quest.EDGE_OF_THE_WORLD)) {
            System.err.println("MoveBackControl: onExecute: Can move past the edge of the world!");
        } else {
            this.quest.startScene(nextSceneName, false, false);
            this.quest.display();
        }
            
        return "";
    }
    
}
