
package quest.control;

import app.EventListener;
import app.Icon;
import app.Utility;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import quest.model.InventoryItem;
import quest.model.Story;
import quest.view.Quest;
import static quest.view.Quest.SECOND_PAGE;

/**
 *
 * @author repp
 */
public class InventoryControl extends QuestControl implements EventListener {
    
    public static String INVENTORY_LABEL = "Inventory";
    public static String NAME = "inventory";
    
    public InventoryControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("InventoryControl: onExecute: tag=" + tag);
        
        int realRow = this.quest.titleRow + this.quest.textRow + 1;
        int startingColumn;
        int endingColumn;
        if (this.quest.currentDisplayPage == Quest.RIGHT_PAGE) {
            startingColumn = this.quest.rightPageStartingColumn;
            endingColumn = this.quest.rightPageEndingColumn;
        } else {
            startingColumn = this.quest.leftPageStartingColumn;
            endingColumn = this.quest.leftPageEndingColumn;
        }
        int realColumn = startingColumn;
        
        for (String key : this.quest.inventory.keySet()) {
            if ((realColumn + 5) > endingColumn) {
                realRow++;
            }
            System.out.println("InventoryControl: onExecute: realRow=" + realRow + ", realColumn=" + realColumn + ", key=" + key);
            InventoryItem item = this.quest.inventory.get(key);
            String linkText =  "<a>" + item.unicodeSurrogatePair + "</a>x" + item.quantity + " ";
            int linkTextLength = item.unicodeSurrogatePair.length() + 1 + String.valueOf(item.quantity).length() + 1; // Add emoticon plus "x" plus digits in quantity plus a space
            this.quest.appController.displayLink(this.quest.name, key, linkText, realRow, realColumn, linkTextLength, this);
            realColumn = realColumn + linkTextLength;
        }
        
        this.quest.textRow = this.quest.textRow + 1;    // Prevent additional story text from overwriting
        
        return "";
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("InventoryControl: onEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        InventoryItem item = this.quest.inventory.get(eventName);
        
        // If the item has a sound file, play it now
        if (item.soundFileName != null) {
            Utility.playSound(item.soundFileName, Boolean.FALSE);
        }
        
        if (item.onSelect == null) {
            // Default to displaying a message box that describes the item
            String title = item.unicodeSurrogatePair + " " + eventName;
            this.quest.appController.displayMessageBox(title, item.description, Icon.INFORMATION);
        } else {
            Story itemStory = item.onSelect;
            System.out.println("InventoryControl: onEvent: Executing story");
            this.quest.displayPage(itemStory.contents, true);
        }
    }
    
}
