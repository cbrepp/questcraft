
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class InventoryAddControl extends QuestControl {
    
    public static String NAME = "inventory-add";
    
    public InventoryAddControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("InventoryAddControl: onExecute: tag=" + tag);
        Boolean skipIfAdded = Boolean.valueOf(getTagToken(tag, 1, false));
        String inventoryItemName = getTagToken(tag, 2, true);
        if ((skipIfAdded) && (this.quest.inventory.containsKey(inventoryItemName))) {
            System.out.println("InventoryAddControl: onExecute: Item is already in inventory");
            return "";
        }
        System.out.println("InventoryAddControl: onExecute: Adding item to inventory");
        this.quest.addInventoryItem(inventoryItemName);
        return "";
    }
    
}
