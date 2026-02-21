
package quest.control;

import app.EventListener;
import static app.controller.BaseController.logger;
import app.node.ButtonGroup;
import java.util.List;
import java.util.logging.Level;
import quest.model.Story;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class ValidatedInput extends ButtonGroup implements EventListener {

    public ValidatedInput(String name) {
        super(name);
    }
    
    public ValidatedInput (String name, List<Object> text) {
        super(name, text);
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        logger.log(Level.INFO, "eventName={0}, eventValue={1}", new Object[]{eventName, eventValue});

        Quest.quest.variables.put(this.name, (String)eventName);
        String subpageName = "INPUT " + this.name + "=" + eventName;
        Story subpage = Quest.quest.getSubpage(subpageName, false);
        if (subpage != null) {
            Quest.quest.displayPagev2(subpage.controls, true);
        } else {
            subpageName = "INPUT " + this.name;
            subpage = Quest.quest.getSubpage(subpageName, false);
            if (subpage != null) {
                Quest.quest.displayPagev2(subpage.controls, true);
            }
        }
    }
    
}
