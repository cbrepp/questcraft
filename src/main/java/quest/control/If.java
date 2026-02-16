
package quest.control;

import static app.controller.BaseController.logger;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author repp
 */
public class If implements Serializable {
    
    public enum ResultType {COLLECTION, SINGULAR};
    
    public static final String DEFAULT_VALUE = "";
    
    // The components of the if are only set by a constructor
    public final Condition condition;
    public final Object result;
    public final List<Object> resultCollection;
    public final ResultType resultType;
    
    public If(Object result, Condition condition) {
        this.resultType = ResultType.SINGULAR;
        this.result = result;
        this.resultCollection = null; // This will never be used
        this.condition = condition;
    }
    
    public If(List<Object> resultCollection, Condition condition) {
        this.resultType = ResultType.COLLECTION;
        this.result = null; // This will never be used
        this.resultCollection = resultCollection;
        this.condition = condition;
    }
    
    public String getResult() {
        String value = DEFAULT_VALUE;
        
        if (((this.resultType == ResultType.SINGULAR) && (this.result == null)) || ((this.resultType == ResultType.COLLECTION) && (this.resultCollection == null))) {
            logger.log(Level.INFO, "Result is null");
            return value;
        }
        
        if (this.resultType == ResultType.SINGULAR) {
            value = this.result.toString();
        } else {
            for (Object object : this.resultCollection) {
                value += object.toString();
            }
        }
        
        logger.log(Level.INFO, "Result is {0}", value);
        
        return value;
    }
    
    @Override
    public String toString() {
        logger.log(Level.INFO, "Entered");
        
        Boolean isTrue = this.condition.evaluate();
        
        String value;
        if (isTrue) {
            value = this.getResult();
        } else {
            value = DEFAULT_VALUE;
        }
        
        return value;
    }
    
}
