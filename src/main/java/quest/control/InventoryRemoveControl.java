
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class InventoryRemoveControl extends QuestControl {
    
    public static String NAME = "inventory-remove";
    
    public InventoryRemoveControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("InventoryRemoveControl: onExecute: tag=" + tag);
        String inventoryItemName = getTagToken(tag, 1, true);
        this.quest.removeInventoryItem(inventoryItemName);
        return "";
    }
    
}
