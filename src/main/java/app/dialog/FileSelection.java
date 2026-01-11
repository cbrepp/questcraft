package app.dialog;

import app.*;
import java.util.List;

/**
 *
 * @author repp
 */
public class FileSelection extends BaseDialog {
    
    public EventListener eventListener;
    public String eventName;
    public List<String> extensionFilters; // ie, "*.txt"
    public String initialFolder;

    public FileSelection () {
        super();
    }
    
    public FileSelection (String title) {
        super(title);
    }
    
}
