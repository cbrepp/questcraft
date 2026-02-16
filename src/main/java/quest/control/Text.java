
package quest.control;

import static app.controller.BaseController.logger;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author repp
 */
public class Text implements Serializable {
    
    public List<Object> collection;
    
    public Text() {
        this.collection = new ArrayList();
    }

    public Text(List<Object> collection) {
        this.collection = collection;
    }
    
    @Override
    public String toString() {
        logger.log(Level.INFO, "Entered");
        
        if ((this.collection == null) || (this.collection.isEmpty())) {
            logger.log(Level.INFO, "There are no objects in the collection");
            return "";
        }
        
        String value = "";        
        for (Object object : this.collection) {
            value += object.toString();
        }
        
        logger.log(Level.INFO, "Value of collection={0}", value);
        return value;
    }
    
}
