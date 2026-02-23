
package quest.control_deprecated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import quest.model.Story;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class RandomControl extends QuestControl {
    
    public static String NAME = "random";
    
    public RandomControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("RandomControl: onExecute: tag=" + tag);
        String values = getTagToken(tag, 1, true);
        ArrayList<String> valueList = new ArrayList<>(Arrays.asList(values.split("\\+")));
        Random random = new Random();
        int selectedItemIndex = random.nextInt(valueList.size());
        String selectedItem = valueList.get(selectedItemIndex);
        System.out.println("RandomControl: onExecute: selectedItemIndex=" + selectedItemIndex + ", selectedItem=" + selectedItem);
        Story story = this.quest.getSubpage(selectedItem, false);
        if (story == null) {
            System.out.println("RandomControl: onExecute: Subpage is NOT defined: " + selectedItem);
            return "";
        }
        System.out.println("RandomControl: onExecute: Displaying subpage: " + selectedItem);
        this.quest.displayStory(story, true);
        return "";
    }
    
}
