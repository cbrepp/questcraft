package quest.view;

import app.controller.BaseController;
import app.color.Color;
import app.EventListener;
import app.HorizontalAlignment;
import app.Icon;
import app.Layout;
import app.TextDecoration;
import app.VerticalAlignment;
import app.dialog.Alert;
import app.node.Grid;
import app.node.Group;
import app.node.Label;
import app.node.Link;
import app.node.VerticalGroup;
import quest.model.InventoryItem;

/**
 *
 * @author repp
 */
public class Inventory extends app.view.BaseView implements EventListener {

    public final static String EMOJI = "\uD83C\uDF71";  // "bento box" Unicode emoji
    public final static String NEW_EMOJI = "\uD83D\uDD25"; // "fire" Unicode emoji... "NEW button" Unicode emoji is \uD83C\uDD95 but a dull gray
    
    public Boolean lastRenderContainedNewItems;
    public Quest quest;
    public BaseController appController;
    
    public Inventory(String name) {
        super(name);
        this.addTextArea = false;   // The text area would interfere with this view's grid layout, so prevent it here
        this.backgroundColor = Color.WHITE;
        this.emojis.add(EMOJI);
    }
    
    @Override
    public void onLoad(BaseController appController) {
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
                this.quest.displayStory(item.onSelect, true);
            } else {
                Alert alert = new Alert(this.name, item.description);
                alert.icon = Icon.INFORMATION;
                alert.emojis = String.join(" ", item.emojis);
                alert.header = eventName;
                this.appController.newDialog(alert);
                //this.quest.appController.displayMessageBox(title, item.description, Icon.INFORMATION, item.emojis);
            }
        }
    }
    
    @Override
    public void onSelected(BaseController appController) {
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
    public void onUnselected(BaseController appController) {
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
        
        TextDecoration emojiDecoration = new TextDecoration();
        emojiDecoration.pixelSize = BaseController.EMOJI_SHEET_SIZE;

        TextDecoration textDecoration = new TextDecoration();
        textDecoration.pixelSize = Quest.DEFAULT_FONT_SIZE;
        
        // As a special spell, the user can "ACTIVATE INVENTORY" to enable the links for inventory items they don't yet have.
        // TODO - The spell doesn't re-render the Inventory view
        Boolean activateInventory = false;
        if (this.quest.variables.containsKey("activate-inventory")) {
            activateInventory = Boolean.valueOf(this.quest.variables.get("activate-inventory").toLowerCase());
        }
        
        Grid gridControl = new Grid("inventory grid");
        gridControl.borderPadding = 5;
        gridControl.columns = 0;
        gridControl.listener = this;
        gridControl.padding = 5;
        gridControl.showBorders = false;
        for (String key : this.quest.book.inventory.keySet()) {
            VerticalGroup itemGroup = new VerticalGroup(key + " group");
            itemGroup.cornerRadii = 10; // Rounded cell corners
            
            InventoryItem bookItem = this.quest.book.inventory.get(key);

            Label emojiControl = new Label(key + " emojis", String.join(" ", bookItem.emojis), emojiDecoration);
            itemGroup.nodes.put(emojiControl, null);

            Link linkControl = new Link(key);
            linkControl.text = key;
            linkControl.pixelSize = Quest.DEFAULT_FONT_SIZE;
            linkControl.eventListener = this;
            itemGroup.nodes.put(linkControl, null);

            InventoryItem questItem = this.quest.inventory.get(key);
            if (questItem != null) {
                System.out.println("Inventory: render: Item in quest inventory: " + key + ", is new?=" + questItem.isNew);
                itemGroup.borderWidth = 1;

                if (questItem.isNew) {
                    // Show new items in the quest inventory as yellow, then show as transparent for subsequent inventory views
                    itemGroup.backgroundColor = Color.YELLOW;
                }

                linkControl.isEnabled = true;

                Label countControl = new Label(key + " count", "x" + questItem.quantity, textDecoration);
                itemGroup.nodes.put(countControl, null);
            } else {
                System.out.println("Inventory: render: Item NOT in quest inventory: " + key);
                itemGroup.borderWidth = 0;
                itemGroup.backgroundColor = Color.DARK_GRAY;
                linkControl.isEnabled = activateInventory;
            }
            
            System.out.println("Inventory: render: Adding " + key);
            gridControl.cells.add(itemGroup);
        }
        
        this.appController.addNode(this.name, this.name, gridControl, new Layout(HorizontalAlignment.CENTER, VerticalAlignment.CENTER));
    }

}
