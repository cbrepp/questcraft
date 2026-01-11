
package quest.control;

import app.HorizontalAlignment;
import app.Layout;
import app.RelativeCoordinates;
import app.VerticalAlignment;
import app.node.Link;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class LinkControl extends QuestControl {
    
    public static String NAME = "link";
    
    public LinkControl(Quest quest) {
        super(quest);
        this.unspoolStoryText = true; // So the link can be displayed in-line, unspool any accumulated story text before execution
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("LinkControl: onExecute: tag=" + tag);
        
        String linkText = getTagToken(tag, 1, true);
        
        int row = this.quest.titleRow + 1 + this.quest.textRow;
        int column;
        if (this.quest.currentDisplayPage == Quest.RIGHT_PAGE) {
            column = this.quest.rightPageStartingColumn + this.quest.textColumn - 1;
        } else {
            column = this.quest.leftPageStartingColumn + this.quest.textColumn - 1;
        }
        
        int textLength = linkText.length();
        
        // TODO - Need to pass in relative coordinates
        Link linkControl = new Link(linkText, new Layout(new RelativeCoordinates(0.25, 0.25), HorizontalAlignment.LEFT, VerticalAlignment.TOP));
        linkControl.text = linkText;
        linkControl.eventListener = this.quest;
        linkControl.eventName = Quest.LINK_EVENT_PREFIX + ":" + linkText;
        this.quest.appController.addNode(this.quest.name, linkControl, this.quest.name);
        
        //this.quest.appController.displayLink(this.quest.name, Quest.LINK_EVENT_PREFIX + ":" + linkText, "<a>" + linkText + "</a>", row, column, textLength, this.quest);
        this.quest.textColumn = 1;
        this.quest.textRow = this.quest.textRow + 1;
        
        String placeholderSpaces = String.valueOf(' ').repeat(textLength);
        
        return placeholderSpaces;
    }
    
}
