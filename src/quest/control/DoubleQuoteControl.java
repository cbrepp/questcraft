
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class DoubleQuoteControl extends QuestControl {
    
    public static String NAME = "quote";
    
    public DoubleQuoteControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("DoubleQuoteControl: onExecute: tag=" + tag);
        String doubleQuote = "\"";
        return doubleQuote;
    }
    
}
