
package quest.control;

import quest.view.Quest;
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
        String name = getTagToken(tag, 1, false);
        String alignment = getTagToken(tag, 2, false);
        String imageFiles = getTagToken(tag, 3, true);
        String[] imageFileTokens = imageFiles.split("\\+");
        for (String imageFile : imageFileTokens) {
            this.displayImage(name, imageFile, alignment);
        }
        return "";
    }
    
    public void displayImage(String name, String imageFile, String alignment) {
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
        Boolean fillParent = false;
        if (alignment.toUpperCase().equals("CENTER")) {
            int halfColumns = ((endingColumn - startingColumn) / 2);
            int halfImageWidth = (this.quest.appController.getColumns(imageFile) / 2);
            imageColumn = startingColumn + halfColumns - halfImageWidth + 1;
        } else if (alignment.toUpperCase().equals("RIGHT")) {
            int imageWidth = this.quest.appController.getColumns(imageFile);
            imageColumn = endingColumn - imageWidth + 1;
        } else if (alignment.toUpperCase().equals("FILL")) {
            fillParent = true;
            imageColumn = 0;
        } else {
            imageColumn = startingColumn;
        }
        // TODO - Support right align
        this.quest.appController.displayImage(Questcraft.QUEST, name, imageFile, row, imageColumn, fillParent);
    }
    
}
