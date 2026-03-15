
package quest.text;

import static app.controller.BaseController.logger;
import java.io.Serializable;
import java.util.logging.Level;

/**
 *
 * @author repp
 */
public class LineSeparator implements Serializable {
    
    public LineSeparator() {
    }
    
    @Override
    public String toString() {
        logger.log(Level.INFO, "Entered");
        return System.lineSeparator();
    }
    
}
