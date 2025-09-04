
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SceneGotoControl extends QuestControl {
    
    public static String NAME = "goto-scene";
    
    public SceneGotoControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SceneGotoControl: onExecute: tag=" + tag);
        String sceneName = getTagToken(tag, 1, true);
        this.quest.startScene(sceneName, false, false);
        this.quest.display();
        return "";
    }
    
}
