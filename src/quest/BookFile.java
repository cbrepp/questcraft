package quest;

import java.io.Serializable;
import java.util.List;
import java.time.LocalDate;

/**
 *
 * @author repp
 */
public class BookFile implements Serializable {

    public String animationFileName;
    public String author;
    public LocalDate updateDate;
    public String musicFileName;
    public String title;
    public List<String> titlePage;
    
}
