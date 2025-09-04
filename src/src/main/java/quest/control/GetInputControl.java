
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class GetInputControl extends QuestControl {
    
    public static String NAME = "get-input";
    
    public GetInputControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("GetInputControl: onExecute: tag=" + tag);
        String variable = getTagToken(tag, 1, false);
        int length = Integer.parseInt(getTagToken(tag, 2, false));
        Boolean clearValue = Boolean.valueOf(getTagToken(tag, 3, false));
        Boolean addButton = Boolean.valueOf(getTagToken(tag, 4, false));
        Boolean isUpperCase = Boolean.valueOf(getTagToken(tag, 5, false));
        Boolean isMultiUse = Boolean.valueOf(getTagToken(tag, 6, false));
        String prompt = getTagToken(tag, 7, true);

        String eventName = Quest.VARIABLE_EVENT_PREFIX + ":" + variable;
        int startColumn;
        if (this.quest.currentDisplayPage == Quest.RIGHT_PAGE) {
            startColumn = this.quest.rightPageStartingColumn;
        } else {
            startColumn = this.quest.leftPageStartingColumn;
        }
        int realColumn = startColumn + this.quest.textColumn - 1;
        int realRow = this.quest.titleRow + 1 + this.quest.textRow;
        
        String value = null;
        if (!clearValue) {
            value = this.quest.variables.get(variable);
        }
        
        this.quest.appController.displayInputField(this.quest.name, eventName, prompt, length, realRow, realColumn, value, addButton, true, isUpperCase, isMultiUse, this.quest);
        this.quest.textRow = this.quest.textRow + 2;
        this.quest.textColumn = 1;
        return "";
    }
    
}
