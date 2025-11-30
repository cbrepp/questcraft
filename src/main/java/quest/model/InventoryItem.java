package quest.model;

import java.io.Serializable;

/**
 *
 * @author repp
 */
public class InventoryItem implements Serializable {
    
    public String description;
    public transient Boolean isNew;
    public Story onAdd;
    public Story onSelect;
    public int quantity;
    public String soundFileName;
    public String unicodeSurrogatePair;
    public int xp;
    
    public InventoryItem(String description, String soundFileName, String unicodeSurrogatePair) {
        this.description = description;
        this.isNew = false;
        this.quantity = 0;
        this.soundFileName = soundFileName;
        this.unicodeSurrogatePair = unicodeSurrogatePair;
        this.xp = 0;
    }

}
