
package quest.control;

import app.Color;
import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class ColorTextControl extends QuestControl {
    
    public static String NAME = "color";
    
    public ColorTextControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        logger.log(Level.INFO, "Entered: tag={0}", tag);
        int red = Integer.parseInt(getTagArgument(tag, 1));
        int green = Integer.parseInt(getTagArgument(tag, 2));
        int blue = Integer.parseInt(getTagArgument(tag, 3));
        this.quest.defaultTextColor = new Color(red, green, blue);
        return "";
    }
    
}
