
package quest.node;

import app.EventListener;
import static app.controller.BaseController.logger;
import app.node.Spinner;
import java.util.List;
import java.util.logging.Level;
import quest.model.Story;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class ValidatedVariableSpinner extends Spinner implements EventListener {

    public ValidatedVariableSpinner(String name) {
        super(name);
    }
    
    public ValidatedVariableSpinner (String name, List<Object> values) {
        super(name, values);
        this.eventListener = this;
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        logger.log(Level.INFO, "eventName={0}, eventValue={1}", new Object[]{eventName, eventValue});

        if (!eventName.equals(this.name)) {
            return;
        }
        
        Quest.quest.variables.put(eventName, (String)eventValue);
        String subpageName = "INPUT " + eventName + "=" + eventValue;
        Story subpage = Quest.quest.getSubpage(subpageName, false);
        if (subpage != null) {
            Quest.quest.displayStory(subpage, true);
        } else {
            subpageName = "INPUT " + eventName;
            subpage = Quest.quest.getSubpage(subpageName, false);
            if (subpage != null) {
                Quest.quest.displayStory(subpage, true);
            }
        }
    }
    
}
