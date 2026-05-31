
package quest.text;

import static app.controller.BaseController.logger;
import java.io.Serializable;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class InventoryHas implements Serializable {
    
    public final Object inventoryItem;
    
    public InventoryHas(Object inventoryItem) {
        this.inventoryItem = inventoryItem;
    }
    
    @Override
    public String toString() {
        logger.log(Level.INFO, "Entered");
        
        String nameString = this.inventoryItem.toString();        
        Boolean has = Quest.quest.inventory.containsKey(nameString);
        String hasString = has.toString();
        return hasString;
    }
    
}
