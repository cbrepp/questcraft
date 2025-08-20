
package quest.control;

import app.AnimationView;
import app.EventListener;
import app.model.SpriteModel;
import java.util.ArrayList;
import java.util.List;
import quest.view.Quest;
import quest.view.Questcraft;

/**
 *
 * @author repp
 */
public class MonsterShooterControl extends QuestControl implements AnimationView {
    
    public static String NAME = "monster-shooter";
    
    public String name;
    
    public MonsterShooterControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public List<SpriteModel> onAnimate() {
        List<SpriteModel> sprites = new ArrayList();
        // Return null to stop the animation
        return sprites;
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("MonsterShooterControl: onExecute: tag=" + tag);
        this.name = getTagToken(tag, 1, false);
        String alignment = getTagToken(tag, 2, false);
        String backgroundImageFileName = getTagToken(tag, 3, false);
        String playerImageFileName = getTagToken(tag, 4, false);
        String missileLeftImageFileName = getTagToken(tag, 5, false);
        String missileRightImageFileName = getTagToken(tag, 6, false);
        String monsterImageFile = getTagToken(tag, 7, false);
        String monsterSoundFile = getTagToken(tag, 8, false);
        Boolean isMonsterVisible = Boolean.valueOf(getTagToken(tag, 9, false).toLowerCase());
        
        int row = this.quest.titleRow + 1 + this.quest.textRow;
        int column;
        int startingColumn, endingColumn;
        if (this.quest.currentDisplayPage == Quest.RIGHT_PAGE) {
            startingColumn = this.quest.rightPageStartingColumn;
            endingColumn = this.quest.rightPageEndingColumn;
        } else {
            startingColumn = this.quest.leftPageStartingColumn;
            endingColumn = this.quest.leftPageEndingColumn;
        }
        switch (alignment.toUpperCase()) {
            case "CENTER" -> {
                int halfColumns = ((endingColumn - startingColumn) / 2);
                int halfImageWidth = (this.quest.appController.getColumns(backgroundImageFileName) / 2);
                column = startingColumn + halfColumns - halfImageWidth + 1;
            }
            case "RIGHT" -> {
                int imageWidth = this.quest.appController.getColumns(backgroundImageFileName);
                column = endingColumn - imageWidth + 1;
            }
            default -> column = startingColumn;
        }
        
        List<SpriteModel> sprites = new ArrayList();
        
        this.quest.appController.addAnimation(Questcraft.QUEST, name, row, column, backgroundImageFileName, sprites, 0.1, this);
        
        return "";
    }
    
}
