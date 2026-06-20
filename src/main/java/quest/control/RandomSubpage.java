
package quest.control;

import static app.controller.BaseController.logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.model.Story;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class RandomSubpage extends BaseQuestControl {
    
    public final List<Object> values;
    
    public RandomSubpage(List<Object> values) {
        this.values = values;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        
        List<String> valueStrings = new ArrayList();
        for (Object value : this.values) {
            valueStrings.add(value.toString());
        }
        
        Random random = new Random();
        int selectedItemIndex = random.nextInt(valueStrings.size());
        String selectedItem = valueStrings.get(selectedItemIndex);
        logger.log(Level.INFO, "Randomly selected value {1} ({2}) from {3} possible value(s)", new Object[]{selectedItemIndex, selectedItem, valueStrings.size()});
        
        Story story = Quest.quest.getSubpage(selectedItem, false);
        if (story == null) {
            logger.log(Level.WARNING, "Randomly selected subpage is NOT defined");
            return;
        }
        
        Quest.quest.displayStory(story, true);
    }
    
}
