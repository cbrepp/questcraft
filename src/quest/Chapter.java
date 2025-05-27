package quest;

import app.Color;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Supported Tags:
 * <br>/<break>
 * <choices-add CHOICE1+CHOICE2+CHOICE3...>
 * <choices-remove CHOICE1+CHOICE2+CHOICE3...>
 * <color RED+BLUE+GREEN/STORY>
 * <end-page>
 * <get-input LABEL:CHOICE1+CHOICE2+CHOICE3...>
 * <get-story-variable VARIABLE>
 * <goto-chapter CHAPTER>
 * <hp-remove POINTS>
 * <if-game-variable PLAYER/TODO>
 * <if-inventory-contains ITEM>
 * <if-story-variable VARIABLE>
 * <image IMAGE1+IMAGE2+IMAGE3...>
 * <inventory-add ITEM1+ITEM2+ITEM3...>
 * <load-scene>
 * <monster-shooter>
 * <play-sound SOUND1+SOUND2+SOUND3...>
 * <player>
 * <press-any-key>
 * <quote>
 * <random COUNT>
 * <score-add POINTS>
 * <set-game-variable VARIABLE=VALUE>
 * <set-player-mode VALUE>
 * <set-story-variable VARIABLE=VALUE>
 * <stop-sound SOUND1+SOUND2+SOUND3...>
 * <story-prompt LABEL:CHOICE1+CHOICE2+CHOICE3...>
 * <story-sound SOUND>
 * <story-variable VARIABLE>
 * <turn-page>
 * <twin>
 * <unbreak>
 *
 * @author repp
 */
public class Chapter implements Serializable {
    
    public Color color;
    public String soundFileName;
    public String symbol;
    public Map<Integer, List<String>> pages;
    public Map<String, String> variables;
    
    public Chapter() {
        this.pages = new LinkedHashMap<>();
    }
    
}
