
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
        
        String nextSceneName = this.quest.getNextScene(true);
        
        if (nextSceneName.equals(Quest.EDGE_OF_THE_WORLD)) {
            System.err.println("MoveAheadControl: onExecute: Can move past the edge of the world!");
        } else {
            this.quest.startScene(nextSceneName, false, false);
            this.quest.display();
        }
            
        return "";
    }
    
}
