
package quest.control;

import quest.view.Quest;
import quest.view.Questcraft;
import quest.view.SceneMap;

/**
 *
 * @author repp
 */
public class AddViewControl extends QuestControl {
    
    public static String NAME = "add-view";
    
    public AddViewControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("AddViewControl: onExecute: tag=" + tag);
        String viewName = getTagToken(tag, 1, true);
        if (viewName.equals(Quest.MAP)) {
            this.quest.map = new SceneMap(Quest.MAP);
            this.quest.map.quest = this.quest;
            Integer inventoryIndex = this.quest.appController.getTabIndex(Questcraft.INVENTORY);
            Integer mapIndex = inventoryIndex + 1;
            this.quest.appController.addView(this.quest.map, false, mapIndex);
        } else {
            System.err.println("AddViewControl: UNSUPPORTED VIEW!");
        }
        
        return "";
    }
    
}
