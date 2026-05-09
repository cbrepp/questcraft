package quest.control;

import app.TextDecoration;
import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class DefaultTextDecorationSet extends BaseQuestControl {
    
    public final TextDecoration textDecoration;
    
    public DefaultTextDecorationSet(TextDecoration textDecoration) {
        this.textDecoration = textDecoration;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        Quest.quest.appController.setDefaultTextDecoration(Quest.quest.name, this.textDecoration);
    }
    
}
