
package quest.control;

import java.util.ArrayList;
import java.util.Arrays;
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
        String values = getTagToken(tag, 2, true);
        ArrayList<String> valueList = new ArrayList<>(Arrays.asList(values.split("\\+")));
        String eventName = Quest.VARIABLE_EVENT_PREFIX + ":" + variable;
        int startColumn;
        int endColumn;
        if (this.quest.currentDisplayPage == Quest.RIGHT_PAGE) {
            startColumn = this.quest.rightPageStartingColumn;
            endColumn = this.quest.rightPageEndingColumn;
        } else {
            startColumn = this.quest.leftPageStartingColumn;
            endColumn = this.quest.leftPageEndingColumn;
        }
        int realRow = this.quest.titleRow + 1 + this.quest.textRow;
        // TODO - Display max column so buttons will wrap onto the next line
        this.quest.appController.displayValidatedInputField(this.quest.name, eventName, valueList, realRow, startColumn, endColumn, this.quest);
        this.quest.textRow = this.quest.textRow + 2;
        return "";
    }
    
}
