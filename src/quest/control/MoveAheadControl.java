
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class MoveAheadControl extends QuestControl {
    
    public static String NAME = "move-ahead";
    
    public MoveAheadControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("MoveAheadControl: onExecute: tag=" + tag);
        
        String nextSceneName = this.quest.getNextScene();
        
        if (nextSceneName.equals(Quest.EDGE_OF_THE_WORLD)) {
            // TODO - How to handle the edge of the world?
        } else {
            this.quest.startScene(nextSceneName, false, false);
            this.quest.display();
        }
            
        return "";
    }
    
}
