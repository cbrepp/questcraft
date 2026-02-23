
package quest.control_deprecated;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class InventoryHasControl extends QuestControl {
    
    public static String NAME = "inventory-has";
    
    public InventoryHasControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("InventoryHasControl: onExecute: tag=" + tag);
        String inventoryItemName = getTagToken(tag, 1, true);
        Boolean has = this.quest.inventory.containsKey(inventoryItemName);
        String hasString = has.toString();
        return hasString;
    }
    
}
