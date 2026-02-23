
package quest.control_deprecated;

import app.Color;
import app.EventListener;
import app.FontStyle;
import app.Layout;
import app.RelativeCoordinates;
import app.VerticalAlignment;
import static app.controller.BaseController.logger;
import app.node.Group;
import app.node.InputField;
import java.util.logging.Level;
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
        logger.log(Level.INFO, "Entered: tag={0}", tag);
        String variable = getTagToken(tag, 1, false);
        int length = Integer.parseInt(getTagToken(tag, 2, false));
        Boolean clearValue = Boolean.valueOf(getTagToken(tag, 3, false));
        Boolean addButton = Boolean.valueOf(getTagToken(tag, 4, false));
        Boolean isUpperCase = Boolean.valueOf(getTagToken(tag, 5, false));
        Boolean isMultiUse = Boolean.valueOf(getTagToken(tag, 6, false));
        String prompt = getTagToken(tag, 7, true);

        String eventName = Quest.VARIABLE_EVENT_PREFIX + ":" + variable;
        String location;
        if (this.quest.currentDisplayPage == Quest.FIRST_PAGE) {
            location = Quest.Area.STORY.name();
        } else {
            location = Quest.Area.ILLUSTRATION.name();
        }
        
        String value = null;
        if (!clearValue) {
            value = this.quest.variables.get(variable);
        }
        
        InputField control = new InputField(eventName);
        control.buttonText = "Submit";
        control.eventListener = this.quest;
        //control.group; // TODO - The group's collection is meaningless... need to decouple its styling from the collection
        control.initialValue = value;
        control.isMultiUse = isMultiUse;
        control.isUpperCase = isUpperCase; // Default (false) is don't force upper case
        control.label = prompt;
        control.length = length; // Default (null) is system default
        //control.pixelSize; // Default is app controller's default pixel size
        //control.textColor; // Default (null) is either black or white depending on which color would best offset the background
        //control.textFont; // Default is the app controller's default font   
        //control.textStyle; // Default (null) is normal
        this.quest.appController.addNode(this.quest.name, location, control, null); // TODO - Need to add a layout for the illustration document
        
        //this.quest.appController.displayInputField(this.quest.name, eventName, prompt, length, realRow, realColumn, value, addButton, true, isUpperCase, isMultiUse, this.quest);
        return "";
    }
    
}
