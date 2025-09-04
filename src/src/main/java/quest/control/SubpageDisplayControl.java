
package quest.control;

import quest.model.Story;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class SubpageDisplayControl extends QuestControl {
    
    public static String NAME = "subpage-display";
    
    public SubpageDisplayControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("SubpageDisplayControl: onExecute: tag=" + tag);

        String subpage = getTagToken(tag, 1, true);
        
        Story story = this.quest.getSubpage(subpage, false);
        if (story == null) {
            System.out.println("SubpageDisplayControl: onExecute: Subpage is NOT defined: " + subpage);
            return "";
        }
        
        System.out.println("SubpageDisplayControl: onExecute: Displaying subpage: " + subpage);
        this.quest.displayPage(story.contents, true);
        
        return "";
    }
    
}
