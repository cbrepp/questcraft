
package quest.control_deprecated;

import app.color.Color;
import app.HorizontalAlignment;
import app.Layout;
import app.RelativeCoordinates;
import app.VerticalAlignment;
import app.node.Rectangle;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class OverlayControl extends QuestControl {
    
    public static String NAME = "overlay";
    
    public OverlayControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("OverlayControl: onExecute: tag=" + tag);
        
        String controlName = getTagToken(tag, 1, false);
        String colorValue = getTagToken(tag, 2, false);
        Boolean invert = Boolean.valueOf(getTagToken(tag, 3, false).toLowerCase());   // Default is false
        String[] colorParts = colorValue.split("\\+");
        int red = Integer.parseInt(colorParts[0]);
        int green = Integer.parseInt(colorParts[1]);
        int blue = Integer.parseInt(colorParts[2]);
        Color color = new Color(red, green, blue, 1.0);
        
        // TODO - Support positional overlays
        /*
        String startRowValue = getTagToken(tag, 3, false);
        int startRow = Integer.parseInt(startRowValue);
        String startColumnValue = getTagToken(tag, 4, false);
        int startColumn = Integer.parseInt(startColumnValue);
        String endRowValue = getTagToken(tag, 5, false);
        int endRow = Integer.parseInt(endRowValue);
        String endColumnValue = getTagToken(tag, 6, false);
        int endColumn = Integer.parseInt(endColumnValue);
        String transparencyValue = getTagToken(tag, 7, false);
        int transparency = Integer.parseInt(transparencyValue);
        
        // Adjust row and column for the quest book
        startRow += this.quest.startingRow;
        endRow += this.quest.startingRow;
        if (this.quest.currentDisplayPage == Quest.RIGHT_PAGE) {
            startColumn += this.quest.rightPageStartingColumn;
            endColumn += this.quest.rightPageStartingColumn;
        } else {
            startColumn += this.quest.leftPageStartingColumn;
            endColumn += this.quest.leftPageStartingColumn;
        }
        */
        
        Rectangle overlay = new Rectangle(controlName);
        overlay.color = color;
        overlay.scaleX = 1.0;
        overlay.scaleY = 1.0;
        this.quest.appController.addNode(this.quest.name, this.quest.name, overlay, new Layout(new RelativeCoordinates(0.0, 0.0), HorizontalAlignment.CENTER, VerticalAlignment.CENTER));

        //this.quest.appController.displayOverlay(this.quest.name, controlName, color, null, null, null, null, null, invert);
        return "";
    }
    
}
