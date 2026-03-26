
package quest.text;

import app.EventListener;
import app.FontStyle;
import app.Icon;
import app.TextDecoration;
import static app.controller.BaseController.logger;
import app.dialog.Alert;
import java.io.Serializable;
import java.util.logging.Level;
import quest.model.Story;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class InventoryItem extends app.Text implements EventListener, Serializable {
    
    public String name;
    
    public InventoryItem(Object name) {
        super(new InventoryItemName(name.toString()), new TextDecoration());
        this.name = name.toString();
        this.decoration.style = FontStyle.UNDERLINE_LINK;
        this.decoration.eventListener = this;
    }
    
    @Override
    public String toString() {
        String text = new InventoryItemName(this.name).toString();
        return text;
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        logger.log(Level.INFO, "eventName={0}, eventValue={1}", new Object[]{eventName, eventValue});
        
        quest.model.InventoryItem item = Quest.quest.book.inventory.get(this.name);
        
        // If the item has a sound file, play it now
        if (item.soundFileName != null) {
            Quest.quest.appController.playSound(item.soundFileName, Boolean.FALSE);
        }
        
        if (item.onSelect == null) {
            logger.log(Level.INFO, "Displaying alert");
            Alert alert = new Alert(this.name, item.description);
            alert.icon = Icon.INFORMATION;
            alert.emojis = String.join(" ", item.emojis);
            alert.header = this.name;
            Quest.quest.appController.newDialog(alert);
        } else {
            logger.log(Level.INFO, "Executing story");
            Story itemStory = item.onSelect;
            Quest.quest.displayStory(itemStory, true);
        }
    }
    
}
