
package quest.control_deprecated;

import quest.model.Act;
import quest.model.Scene;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class NextSceneControl extends QuestControl {
    
    public static String NAME = "next-scene";
    
    public NextSceneControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("NextSceneControl: onExecute: tag=" + tag);
        String nextSceneId = this.quest.getNextScene(true);
        Act act = this.quest.book.acts.get(this.quest.currentAct);
        System.out.println("NextSceneControl: onExecute: act " + this.quest.currentAct + "'s next scene is " + nextSceneId);
        String nextSceneName;
        if (!nextSceneId.equals(Quest.EDGE_OF_THE_WORLD)) {
            Scene nextScene = act.scenes.get(nextSceneId);
            String nextSceneSymbol = nextScene.symbol;
            nextSceneName = nextSceneSymbol + " " + nextSceneId;
        } else {
            nextSceneName = nextSceneId;
        }
        return nextSceneName;
    }
    
}
