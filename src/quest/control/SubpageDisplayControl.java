
package quest.control;

import quest.model.Story;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SubpageDisplayControl extends QuestControl {
    
    public static String NAME = "subpage-display";
    
    public SubpageDisplayControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SubpageDisplayControl: onExecute: tag=" + tag);
        
        String firstParam = getTagToken(tag, 1, false);
        String[] equalsTokens = firstParam.split("=");
        String variable;
        if (equalsTokens.length > 0) {
            variable = equalsTokens[0];
        } else {
            variable = "";
        }
        String targetValue;
        if (equalsTokens.length == 2) {
            targetValue = equalsTokens[1];
        } else {
            targetValue = "";
        }
        String subpage = getTagToken(tag, 2, true);
        String value;
        
        if (!this.quest.variables.containsKey(variable)) {
            System.out.println("SubpageDisplayControl: onExecute: Variable is NOT defined");
            value = "";
        } else {
            value = this.quest.variables.get(variable);
            System.out.println("SubpageDisplayControl: onExecute: Variable is defined: " + value);
        }
        
        if (!value.equals(targetValue)) {
            return "";
        }
        
        Story story = this.quest.getSubpage(subpage, false);
        if (story == null) {
            System.out.println("SubpageDisplayControl: onExecute: Subpage is NOT defined: " + subpage);
            return "";
        }
        
        System.out.println("SubpageDisplayControl: onExecute: Displaying subpage: " + subpage);
        this.quest.displayPage(story.contents, true);
        
        return "";
    }
    
}
