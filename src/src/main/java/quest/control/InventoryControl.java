
package quest.control;

import app.EventListener;
import app.Icon;
import app.Utility;
import java.util.HashSet;
import java.util.Set;
import quest.model.InventoryItem;
import quest.model.Story;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class InventoryControl extends QuestControl implements EventListener {
    
    public static String INVENTORY_LABEL = "Inventory";
    public static String NAME = "inventory";
    
    public InventoryControl(Quest quest) {
        super(quest);
        this.unspoolStoryText = true; // So the inventory can be displayed in-line, unspool any accumulated story text before execution
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("InventoryControl: onExecute: tag=" + tag);
        
        String inventoryItemName = getTagToken(tag, 1, true);
        
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
        
        int realColumn = startingColumn + this.quest.textColumn - 1;
        System.out.println("InventoryControl: onExecute: textColumn=" + this.quest.textColumn + ", realColumn=" + realColumn);
        
        Set<String> keySet;
        if (inventoryItemName.equals("")) {
            System.out.println("InventoryControl: onExecute: Displaying full inventory");
            keySet = this.quest.inventory.keySet();
        } else {
            System.out.println("InventoryControl: onExecute: Displaying only " + inventoryItemName);
            keySet = new HashSet();
            keySet.add(inventoryItemName);
        }
        
        int totalLength = 0;
        for (String key : keySet) {
            System.out.println("InventoryControl: onExecute: realRow=" + realRow + ", realColumn=" + realColumn + ", key=" + key);
            InventoryItem item = this.quest.inventory.get(key);
            String linkText;
            int linkTextLength;
            if (inventoryItemName.equals("")) {
                linkText = "<a>" + item.unicodeSurrogatePair + "</a>" + "x" + item.quantity + " ";
                linkTextLength = 2 + 1 + String.valueOf(item.quantity).length() + 1; // Add emoticon plus "x" plus digits in quantity plus a space
            } else {
                if (this.quest.inventory.containsKey(inventoryItemName)) {
                    // In possession, so add a link
                    linkText = item.unicodeSurrogatePair + " <a>" + inventoryItemName + "</a>";
                } else {
                    // NOT in possession, so no link, just the emoji and name
                    item = this.quest.book.inventory.get(key);
                    linkText = item.unicodeSurrogatePair + " " + inventoryItemName;
                }
                linkTextLength = 2 + 1 + inventoryItemName.length(); // Add emoticon plus space plus name length
            }
            if ((realColumn + linkTextLength) > endingColumn) {
                System.out.println("InventoryControl: onExecute: Wrapping to the next line");
                realRow++;
                realColumn = startingColumn;
            }
            this.quest.appController.displayLink(this.quest.name, key, linkText, realRow, realColumn, linkTextLength, this);
            realColumn = realColumn + linkTextLength;
            totalLength += linkTextLength;
        }
        
        String placeholderSpaces = String.valueOf(' ').repeat(totalLength);
        
        return placeholderSpaces;
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("InventoryControl: onEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        InventoryItem item = this.quest.book.inventory.get(eventName);
        
        // If the item has a sound file, play it now
        if (item.soundFileName != null) {
            this.quest.appController.playSound(item.soundFileName, Boolean.FALSE);
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
