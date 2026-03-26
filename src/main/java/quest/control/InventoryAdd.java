
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class InventoryAdd extends BaseQuestControl {
    
    public final Object name;
    public final Boolean thereCanBeOnlyOne;
    
    public InventoryAdd(Object name) {
        this.name = name;
        this.thereCanBeOnlyOne = false;
    }

    public InventoryAdd(Object name, Boolean thereCanBeOnlyOne) {
        this.name = name;
        this.thereCanBeOnlyOne = thereCanBeOnlyOne;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        
        String nameString = this.name.toString();
        
        if ((this.thereCanBeOnlyOne) && (Quest.quest.inventory.containsKey(nameString))) {
            logger.log(Level.INFO, "Item {0} is already in inventory", nameString);
            return;
        }
        
        logger.log(Level.INFO, "Adding item {0} to inventory", nameString);
        Quest.quest.addInventoryItem(nameString);
    }
    
}
