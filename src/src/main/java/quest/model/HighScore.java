package quest.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author repp
 */
public class HighScore implements Serializable {
    
    public int score;
    public String player;
    public LocalDate date;
    
    public HighScore(int score, String player, LocalDate date) {
        this.score = score;
        this.player = player;
        this.date = date;
    }

}
