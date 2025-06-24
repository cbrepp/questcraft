
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
        String inventoryItemName = getTagToken(tag, 1, true);
        this.quest.addInventoryItem(inventoryItemName);
        return "";
    }
    
}
