
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.model.Story;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SpellRegister extends BaseQuestControl {
    
    public final String name;
    public final Story story;
    
    public SpellRegister(String name, Story story) {
        this.name = name;
        this.story = story;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        Quest.quest.registerSpell(name, story);
    }
    
}
