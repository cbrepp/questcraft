
package quest.control;

import app.Alignment;
import java.util.ArrayList;
import java.util.Arrays;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class GetValidatedInputControl extends QuestControl {
    
    public static String NAME = "get-validated-input";
    
    public GetValidatedInputControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("GetValidatedInputControl: onExecute: tag=" + tag);
        String variable = getTagToken(tag, 1, false);
        int alignment = Alignment.LEFT;
        String values = "";
        ArrayList<String> modifierList = new ArrayList<>(Arrays.asList(variable.toLowerCase().split("=")));
        if (modifierList.size() == 2) {
            if (modifierList.get(0).equals("align")) {
                String alignmentName = modifierList.get(1);
                System.out.println("GetInputControl: onExecute: alignment modifier, value=" + alignment);
                variable = getTagToken(tag, 2, false);
                values = getTagToken(tag, 3, true);
                switch (alignmentName) {
                    case "left" -> alignment = Alignment.LEFT;
                    case "center" -> alignment = Alignment.CENTER;
                    case "right" -> alignment = Alignment.RIGHT;
                    default -> {
                        System.err.println("GetValidatedInputControl: onExecute: Unsupported alignment!");
                        alignment = Alignment.LEFT;
                    }
                }
            }
        } else {
            values = getTagToken(tag, 2, true);
        }
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
        this.quest.appController.displayValidatedInputField(this.quest.name, eventName, valueList, realRow, startColumn, endColumn, alignment, this.quest, false);
        this.quest.textRow = this.quest.textRow + 2;
        this.quest.textColumn = 1;
        return "";
    }
    
}
