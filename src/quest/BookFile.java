package quest;

import java.io.Serializable;
import java.util.List;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author repp
 */
public class BookFile implements Serializable {

    public String animationFileName;
    public String author;
    public LocalDate updateDate;
    public String title;
    public List<HighScore> highScores;
    public Map<String, Chapter> chapters;
    
    public BookFile() {
        this.chapters = new LinkedHashMap();
    }
    
}
