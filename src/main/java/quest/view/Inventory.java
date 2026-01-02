package quest.view;

import app.ApplicationController;
import app.Color;
import app.EventListener;
import app.Icon;
import app.model.BaseModel;
import app.model.LabelModel;
import app.model.LinkModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import quest.model.InventoryItem;

/**
 *
 * @author repp
 */
public class Inventory extends app.ApplicationView implements EventListener {

    public final static String EMOJI = "\uD83C\uDF71";  // "bento box" Unicode emoji
    public final static String NEW_EMOJI = "\uD83D\uDD25"; // "fire" Unicode emoji... "NEW button" Unicode emoji is \uD83C\uDD95 but a dull gray
    
    public Boolean lastRenderContainedNewItems;
    public Quest quest;
    public ApplicationController appController;
    
    public Inventory(String name) {
        super(name);
        this.addTextArea = false;   // The text area would interfere with this view's grid layout, so prevent it here
        this.backgroundColor = new Color(255, 255, 255);
        this.emojis.add(EMOJI);
    }
    
    @Override
    public void onLoad(ApplicationController appController) {
        System.out.println("Inventory: onLoad");
        
        this.appController = appController;
        this.render();
        this.quest.addListener(Quest.NEW_INVENTORY_ITEM, this);
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("Inventory: onEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        if (eventName.equals(Quest.NEW_INVENTORY_ITEM)) {
            this.appController.clearScreen(this.name);
            this.render();
            if (!this.emojis.contains(NEW_EMOJI)) {
                this.emojis.addFirst(NEW_EMOJI);
                this.appController.refreshTabLabel(this.name);
            }
        } else {
            InventoryItem item = this.quest.book.inventory.get(eventName);
            String title = eventName;
            if (item.soundFileName != null) {
                this.appController.playSound(item.soundFileName, Boolean.FALSE);
            }
            if (item.onSelect != null) {
                System.out.println("Inventory: onEvent: Executing story");
                this.quest.displayPage(item.onSelect.contents, true);
            } else {
                this.quest.appController.displayMessageBox(title, item.description, Icon.INFORMATION, item.emojis);
            }
        }
    }
    
    @Override
    public void onSelected(ApplicationController appController) {
        System.out.println("Inventory: onSelected");
        
        this.emojis.removeIf(emoji -> emoji.equals(NEW_EMOJI));
        appController.refreshTabLabel(this.name);    // Remove emoji that indicates a new item has been added

        for (String key : this.quest.inventory.keySet()) {
            InventoryItem questItem = this.quest.inventory.get(key);
            if (questItem.isNew) {
                // Now that we've seen an item, it's no longer new
                questItem.isNew = false;
                this.lastRenderContainedNewItems = true;
            }
        }
    }
    
    @Override
    public void onUnselected(ApplicationController appController) {
        System.out.println("Inventory: onUnselected");
        
        if (this.lastRenderContainedNewItems) {
            System.out.println("Inventory: onUnselected: Re-rendering to remove highlighted items");
            this.appController.clearScreen(this.name);
            this.render();
        }
    }
    
    public void render() {
        System.out.println("Inventory: render");
        this.lastRenderContainedNewItems = false;
        Map<String, ArrayList<BaseModel>> gridCells = new LinkedHashMap<>();
        for (String key : this.quest.book.inventory.keySet()) {
            ArrayList<BaseModel> controlList = new ArrayList();
            InventoryItem bookItem = this.quest.book.inventory.get(key);
            String linkText =  key;
            String labelText = null;
            Color backgroundColor = null;
            InventoryItem questItem = this.quest.inventory.get(key);
            if (questItem != null) {
                System.out.println("Inventory: render: Item in quest inventory: " + key + ", is new?=" + questItem.isNew);
                linkText = "<a>" + linkText + "</a>";
                labelText = "x" + questItem.quantity;
                if (questItem.isNew) {
                    backgroundColor = new Color(255, 222, 33); // Yellow
                } else  {
                    backgroundColor = new Color(255, 255, 255); // White
                }
            } else {
                System.out.println("Inventory: render: Item NOT in quest inventory: " + key);
                backgroundColor = new Color(169, 169, 169); // Dark Gray
            }
            String itemEmojis = String.join(" ", bookItem.emojis);
            LabelModel emojiControl = new LabelModel(itemEmojis, backgroundColor);
            emojiControl.backgroundColor = backgroundColor;
            emojiControl.pixelSize = 64.0;
            controlList.add(emojiControl);
            LinkModel linkControl = new LinkModel(linkText, backgroundColor, questItem != null);
            controlList.add(linkControl);
            if (labelText != null) {
                LabelModel labelControl = new LabelModel(labelText, backgroundColor);
                controlList.add(labelControl);
            }
            System.out.println("Inventory: onLoad: Adding " + key);
            gridCells.put(key, controlList);
        }
        
        appController.displayGrid(this.name, gridCells, 0, true, this);   // Pass zero for columns to allow them to be calculated
    }

}
