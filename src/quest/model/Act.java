package quest.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author repp
 */
public class Act extends BookPart implements Serializable {
    
    public String firstSceneName;
    public String nextActName;
    public Map<String, Scene> scenes;
    
    public Act() {
        this.scenes = new LinkedHashMap<>();
    }
    
}
