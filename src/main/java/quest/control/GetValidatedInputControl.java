
package quest.control;

import app.HorizontalAlignment;
import app.VerticalAlignment;
import app.Layout;
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
        HorizontalAlignment alignment = HorizontalAlignment.LEFT;
        String values = "";
        ArrayList<String> modifierList = new ArrayList<>(Arrays.asList(variable.toLowerCase().split("=")));
        if (modifierList.size() == 2) {
            if (modifierList.get(0).equals("align")) {
                String alignmentName = modifierList.get(1);
                System.out.println("GetInputControl: onExecute: alignment modifier, value=" + alignment);
                variable = getTagToken(tag, 2, false);
                values = getTagToken(tag, 3, true);
                switch (alignmentName) {
                    case "left" -> alignment = HorizontalAlignment.LEFT;
                    case "center" -> alignment = HorizontalAlignment.CENTER;
                    case "right" -> alignment = HorizontalAlignment.RIGHT;
                    default -> {
                        System.err.println("GetValidatedInputControl: onExecute: Unsupported alignment!");
                        alignment = HorizontalAlignment.LEFT;
                    }
                }
            }
        } else {
            values = getTagToken(tag, 2, true);
        }
        Boolean allowMultipleClicks = false;
        if (variable.charAt(0) == '*') {
            // TODO - This is ugly magic
            allowMultipleClicks = true;
            variable = variable.substring(1);   // Trim off the first character because it was a special code indicating that the variable's buttons can be clicked more than once
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
        this.quest.appController.displayValidatedInputField(this.quest.name, eventName, valueList, realRow, startColumn, endColumn, new Layout(alignment, VerticalAlignment.CENTER), this.quest, allowMultipleClicks);
        this.quest.textRow = this.quest.textRow + 2;
        this.quest.textColumn = 1;
        return "";
    }
    
}
