
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class InventoryRemove extends BaseQuestControl {
    
    public final Object inventoryItem;
    
    public InventoryRemove(Object inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        String inventoryItemString = this.inventoryItem.toString();
        Quest.quest.removeInventoryItem(inventoryItemString);
    }
    
}
