package quest.model;

import app.Color;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author repp
 */
public class Scene extends BookPart implements Serializable {
    
    public Color color;
    public String firstPageName;
    public Boolean hidePageHeaders = false;
    public String nextSceneName;
    public Map<String, Page> pages;
    public String soundFileName;
    public String symbol;
    public Integer x;
    public Integer y;
    
    public Scene() {
        this.pages = new LinkedHashMap<>();
    }
    
}
