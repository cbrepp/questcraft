
package quest.text;

import static app.controller.BaseController.logger;
import java.io.Serializable;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class InventoryItemName implements Serializable {
    
    public String name;
    
    public InventoryItemName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        logger.log(Level.INFO, "Entered");
        
        String nameText;
        
        quest.model.InventoryItem bookItem = Quest.quest.book.inventory.get(this.name);
        if (bookItem == null) {
            logger.log(Level.WARNING, "Inventory item does not exist!  name={0}", this.name);
            nameText = "";
        } else {
            String emojis = String.join(" ", bookItem.emojis);
            nameText = emojis + " " + this.name;
        }

        return nameText;
    }
    
}
