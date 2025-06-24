
package quest.control;

import quest.view.Quest;
import static quest.view.Quest.SECOND_PAGE;
import quest.view.Questcraft;

/**
 *
 * @author repp
 */
public class ImageControl extends QuestControl {
    
    public static String NAME = "image";
    
    public ImageControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("ImageControl: onExecute: tag=" + tag);
        String alignment = getTagToken(tag, 1, false);
        String imageFiles = getTagToken(tag, 2, true);
        String[] imageFileTokens = imageFiles.split("\\+");
        for (String imageFile : imageFileTokens) {
            this.displayImage(imageFile, alignment);
        }
        return "";
    }
    
    public void displayImage(String imageFile, String alignment) {
        int row = this.quest.titleRow + 1 + this.quest.textRow;
        int imageColumn;
        int startingColumn, endingColumn;
        if (this.quest.currentDisplayPage == Quest.RIGHT_PAGE) {
            startingColumn = this.quest.rightPageStartingColumn;
            endingColumn = this.quest.rightPageEndingColumn;
        } else {
            startingColumn = this.quest.leftPageStartingColumn;
            endingColumn = this.quest.leftPageEndingColumn;
        }
        if (alignment.toUpperCase().equals("CENTER")) {
            int halfColumns = ((endingColumn - startingColumn) / 2);
            int halfImageWidth = (this.quest.appController.getColumns(imageFile) / 2);
            imageColumn = startingColumn + halfColumns - halfImageWidth + 1;
        } else {
            imageColumn = startingColumn;
        }
        // TODO - Support right align
        this.quest.appController.displayImage(Questcraft.QUEST, imageFile, row, imageColumn);
    }
    
}
