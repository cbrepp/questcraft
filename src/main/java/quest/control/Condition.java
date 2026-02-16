
package quest.control;

import static app.controller.BaseController.logger;
import java.io.Serializable;
import java.util.logging.Level;

/**
 *
 * @author repp
 */
public class Condition implements Serializable {
    
    public enum Operator {AND, EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUAL_TO, LESS_THAN, LESS_THAN_OR_EQUAL_TO, OR};
    
    // The components of the condition are only set by a constructor
    public final Operator operator;
    public final Boolean result;
    public final Object value1;
    public final Object value2;
    
    public Condition(Object value1, Operator operator, Object value2, Boolean result) {
        this.value1 = value1;
        this.operator = operator;
        this.result = result;
        this.value2 = value2;
    }

    public Boolean evaluate() {
        String value1 = this.getValue1();
        String value2 = this.getValue2(); // TODO - For performance, evaluate value2 only when necessary
        
        Boolean isTrue;
        if (null == this.operator) {
            logger.log(Level.SEVERE, "Condition is null");
            return false;
        } else switch (this.operator) {
            case AND -> isTrue = Boolean.parseBoolean(value1) && Boolean.parseBoolean(value2);
            case EQUALS -> isTrue = value1.equals(value2);
            case GREATER_THAN -> isTrue = Double.parseDouble(value1) > Double.parseDouble(value2);
            case GREATER_THAN_OR_EQUAL_TO -> isTrue = Double.parseDouble(value1) >= Double.parseDouble(value2);
            case LESS_THAN -> isTrue = Double.parseDouble(value1) < Double.parseDouble(value2);
            case LESS_THAN_OR_EQUAL_TO -> isTrue = Double.parseDouble(value1) <= Double.parseDouble(value2);
            case OR -> isTrue = Boolean.parseBoolean(value1) || Boolean.parseBoolean(value2);
            default -> {
                logger.log(Level.SEVERE, "Unsupported condition {0}", this.operator);
                return false;
            }
        }
        
        logger.log(Level.INFO, "{0} {1} {2} = {3}. Required is {4}.", new Object[]{value1, this.operator, value2, isTrue, this.result});

        return (this.result == isTrue);
    }
    
    public String getValue1() {
        String valueString = this.value1.toString();
        return valueString;
    }
    
    public String getValue2() {
        String valueString = this.value2.toString();
        return valueString;
    }
    
    @Override
    public String toString() {
        logger.log(Level.INFO, "Entered");
        
        Boolean isTrue = this.evaluate();
        
        String value = String.valueOf(isTrue);
        
        return value;
    }
    
}
