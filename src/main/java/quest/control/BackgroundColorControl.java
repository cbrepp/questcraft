
package quest.control;

import app.Color;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class BackgroundColorControl extends QuestControl {
    
    public static String NAME = "background-color";
    
    public BackgroundColorControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("BackgroundColorControl: onExecute: tag=" + tag);
        String colorTag = getTagToken(tag, 1, false);
        String[] colorParts = colorTag.split("\\+");
        int red = Integer.parseInt(colorParts[0]);
        int green = Integer.parseInt(colorParts[1]);
        int blue = Integer.parseInt(colorParts[2]);
        Color color = new Color(red, green, blue);
        String viewName = getTagToken(tag, 2, true);
        this.quest.appController.setBackgroundColor(viewName, color);
        
        return "";
    }
    
}
