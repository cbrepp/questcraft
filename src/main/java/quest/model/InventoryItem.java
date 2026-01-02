package quest.model;

import java.io.Serializable;
import java.util.List;

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
    public List<String> emojis; // List of Unicode surrogate pairs
    public int xp;
    
    public InventoryItem(String description, String soundFileName, List<String> emojis) {
        this.description = description;
        this.isNew = false;
        this.quantity = 0;
        this.soundFileName = soundFileName;
        this.emojis = emojis;
        this.xp = 0;
    }

}
