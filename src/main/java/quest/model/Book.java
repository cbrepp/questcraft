package quest.model;

import java.io.Serializable;
import java.util.List;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author repp
 */
public class Book extends BookPart implements Serializable {

    // Data structure:
    //
    // book
    // - acts
    //   - scenes
    //     - pages
    //       - subpages
    //         - story
    //       - story
    //   - subpages
    //     - story
    // - subpages
    //   - story
    public Map<String, Act> acts;
    public String animationFileName;
    public String author;
    public String firstActName;
    public List<HighScore> highScores;
    public Map<String, InventoryItem> inventory;
    public Boolean preloadEmojisDuringAnimation;
    public String title;
    public LocalDate updateDate;
    public Map<String, String> variables;
    
    public Book() {
        this.acts = new HashMap();
        this.preloadEmojisDuringAnimation = false;
        this.variables = new HashMap<>();
    }
    
}
