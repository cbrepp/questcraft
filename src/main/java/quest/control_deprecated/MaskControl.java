
package quest.control_deprecated;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class MaskControl extends QuestControl {
    
    public static String NAME = "mask";
    
    public MaskControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("MaskControl: onExecute: tag=" + tag);
        String maskCharacter = getTagToken(tag, 1, false);
        String variable = getTagToken(tag, 2, true);
        
        String value;
        if (this.quest.variables.containsKey(variable)) {
            value = this.quest.variables.get(variable);
            System.out.println("MaskControl: onExecute: variable '" + variable + "' = '" + value + "'");
        } else {
            System.err.println("MaskControl: onExecute: variable '" + variable + "' not found!");
            value = variable;
        }
        
        String maskedValue = maskCharacter.repeat(value.length());
        
        return maskedValue;
    }
    
}
