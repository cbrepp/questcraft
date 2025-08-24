
package quest.control;

import app.AnimationView;
import app.model.Coordinates;
import app.model.SpriteModel;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import quest.view.Quest;
import quest.view.Questcraft;

/**
 *
 * @author repp
 */
public class MonsterShooterControl extends QuestControl implements AnimationView {
    
    public static String NAME = "monster-shooter";
    
    public String name;
    public Coordinates backgroundDimensions;
    public Boolean isMonsterVisible;
    public String missileSoundFileName;
    public SpriteModel monster;
    public int monsterDestinationX;
    public Coordinates monsterDimensions;
    public int monsterDirection;
    public int monsterHalfwayPoint;
    public String monsterImageFileName;
    public String monsterHitImageFileName;
    public Coordinates monsterMissileChungusDimensions;
    public Coordinates monsterMissileDimensions;
    public int monsterHP;
    public String monsterMissileLeftMiniImageFileName;
    public String monsterMissileLeftImageFileName;
    public String monsterMissileLeftChungusImageFileName;
    public String monsterMissileRightMiniImageFileName;
    public String monsterMissileRightImageFileName;
    public String monsterMissileRightChungusImageFileName;
    public Coordinates monsterMissileMiniDimensions;
    public List<SpriteModel> monsterMissilesAttached;
    public List<SpriteModel> monsterMissilesLaunched;
    public Boolean monsterMissilesSpawned;
    public String monsterMissileSoundFileName;
    public String monsterSoundFileName;
    public SpriteModel player;
    public Coordinates playerDimensions;
    public Coordinates playerMissileDimensions;
    public List<SpriteModel> playerMissilesAttached;
    public List<SpriteModel> playerMissilesLaunched;
    
    public MonsterShooterControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public List<SpriteModel> onAnimate() {
        List<SpriteModel> sprites = new ArrayList();
        
        // TODO - Add difficulty variances
        
        // For each monster missile that is launched, increase its y coordinate
        ListIterator<SpriteModel> iterator = this.monsterMissilesLaunched.listIterator();
        while (iterator.hasNext()) {
            SpriteModel sprite = iterator.next();
            sprite.y++;
            if (sprite.y > (this.backgroundDimensions.y - sprite.dimensions.y)) {
                iterator.remove();  // Missile hit the bottom boundary, remove
            }
        }
        
        // For each player missile that is launched, decrease its y coordinate
        iterator = this.playerMissilesLaunched.listIterator();
        while (iterator.hasNext()) {
            SpriteModel sprite = iterator.next();
            sprite.y--;
            if (sprite.y < 1) {
                iterator.remove();  // Missile hit the top boundary, remove
            }
        }
        
        // Move the monster
        this.monster.x += this.monsterDirection;
        
        // Move each attached monster missile
        iterator = this.playerMissilesAttached.listIterator();
        while (iterator.hasNext()) {
            SpriteModel sprite = iterator.next();
            sprite.x += this.monsterDirection;
        }
        
        // Handle if the monster is now halfway to its destination. (Toggle between spawning and releasing missiles.)
        Boolean spawnMissiles = false;
        if (((this.monsterDirection == 1) && (this.monster.x >= this.monsterHalfwayPoint)) || ((this.monsterDirection == -1) && (this.monster.x <= this.monsterHalfwayPoint))) {
            if (!this.monsterMissilesSpawned) {
                System.out.println("MonsterShooterControl: onAnimate: SPAWNING MISSILES!");
                spawnMissiles = true;
            } else {
                System.out.println("MonsterShooterControl: onAnimate: DROPPING MISSILES!");
                // Move the missile from being attached to being launched
                iterator = this.monsterMissilesAttached.listIterator();
                while (iterator.hasNext()) {
                    SpriteModel sprite = iterator.next();
                    this.monsterMissilesLaunched.add(sprite);
                    iterator.remove();
                }
            }
        }
        
        // Spawn new missiles
        if (spawnMissiles) {
            this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, monsterMissileLeftChungusImageFileName, 1.0, 1, this.monster.x));
            // TODO - Add normal and mini missiles to the left
            this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, monsterMissileRightChungusImageFileName, 1.0, 1, this.monster.x + this.monster.dimensions.x));
            // TODO - Add normal and mini missiles to the right
            this.monsterMissilesSpawned = true;
        }
        
        // Handle if the monster is now at its destination. (Change direction and calculate new destination.)
        if (((this.monsterDirection == 1) && (this.monster.x >= this.monsterDestinationX)) || ((this.monsterDirection == -1) && (this.monster.x <= this.monsterDestinationX))) {
            System.out.println("MonsterShooterControl: onAnimate: PIVOT!!!!");
            this.monsterDirection *= -1;
            if (this.monsterDirection == 1) {
                // Moving right
                this.monsterDestinationX = this.monster.x + (int) (Math.random() * (this.backgroundDimensions.x - this.monster.x));
            } else {
                // Moving left
                this.monsterDestinationX = this.monster.x - (int) (Math.random() * (this.monster.x));
            }
            this.monsterHalfwayPoint = (Math.floorDiv(Math.abs(this.monster.x - this.monsterDestinationX), 2) * this.monsterDirection) + this.monster.x;
        }
        
        // Move the player based on input
        int origPlayerX = player.x;
        if (this.quest.variables.get("animation-left").toLowerCase().equals("true")) {
            if (player.x > 10) {
                System.out.println("MonsterShooterControl: onAnimate: PLAYER GOES LEFT!");
                // TODO - Count consecutive times the player held down the same key and magnify distance accordingly
                player.x -= 10;
            }
            this.quest.variables.put("animation-left", "false");
        }
        if (this.quest.variables.get("animation-right").toLowerCase().equals("true")) {
            if (player.x < this.backgroundDimensions.x - 10) {
                System.out.println("MonsterShooterControl: onAnimate: PLAYER GOES RIGHT!");
                player.x += 10;
            }
            this.quest.variables.put("animation-right", "false");
        }
        
        // Move each attached player missile
        if (origPlayerX != player.x) {
            int playerDeltaX = player.x - origPlayerX;
            for (SpriteModel sprite : this.playerMissilesAttached) {
                sprite.x += playerDeltaX;
            }
        }
        
        // TODO - Detect collisions between missiles
        // TODO - Detect collisions between monster missiles and player
        // TODO - Detect collisions between player missiles and monster
        
        // Return all sprites
        sprites.add(this.player);
        sprites.add(this.monster);
        for (SpriteModel sprite : this.playerMissilesAttached) {
            sprites.add(sprite);
        }
        for (SpriteModel sprite : this.monsterMissilesAttached) {
            sprites.add(sprite);
        }
        for (SpriteModel sprite : this.playerMissilesLaunched) {
            sprites.add(sprite);
        }
        for (SpriteModel sprite : this.monsterMissilesLaunched) {
            sprites.add(sprite);
        }
        
        // Return null to stop the animation
        System.out.println("MonsterShooterControl: onAnimate: Returning " + sprites.size() + " sprites");
        return sprites;
    }
    
    /*
        Game variables:
        animation-on - "true" when the animation is in progress
        animation-started - "true" when the animation has begun
        animation-left - "true" moves the player to the left
        animation-right - "true" moves the player to the right
    */
    @Override
    public String onExecute(String tag) {
        System.out.println("MonsterShooterControl: onExecute: tag=" + tag);
        
        //
        this.name = getTagToken(tag, 1, false);
        String alignment = getTagToken(tag, 2, false);
        String backgroundImageFileName = getTagToken(tag, 3, false);
        String playerImageFileName = getTagToken(tag, 4, false);
        String playerHitImageFileName = getTagToken(tag, 5, false);
        String missileLeftImageFileName = getTagToken(tag, 6, false);
        String missileRightImageFileName = getTagToken(tag, 7, false);
        this.missileSoundFileName = getTagToken(tag, 8, false);
        this.monsterImageFileName = getTagToken(tag, 9, false);
        this.monsterHitImageFileName = getTagToken(tag, 10, false);
        // TODO - Only need one image and we can flip it and shrink it dynamically
        this.monsterMissileLeftMiniImageFileName = getTagToken(tag, 11, false);
        this.monsterMissileLeftImageFileName = getTagToken(tag, 12, false);
        this.monsterMissileLeftChungusImageFileName = getTagToken(tag, 13, false);
        this.monsterMissileRightMiniImageFileName = getTagToken(tag, 14, false);
        this.monsterMissileRightImageFileName = getTagToken(tag, 15, false);
        this.monsterMissileRightChungusImageFileName = getTagToken(tag, 16, false);
        this.monsterMissileSoundFileName = getTagToken(tag, 18, false);
        this.monsterSoundFileName = getTagToken(tag, 19, false);
        this.isMonsterVisible = Boolean.valueOf(getTagToken(tag, 19, false).toLowerCase());
        
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
        
        // Build a list of needed sprite images so the controller can cache them
        List<SpriteModel> images = new ArrayList();
        images.add(new SpriteModel(this.quest.appController, playerImageFileName, 1.0, 0, 0));
        images.add(new SpriteModel(this.quest.appController, playerHitImageFileName, 1.0, 0, 0));
        images.add(new SpriteModel(this.quest.appController, missileLeftImageFileName, 1.0, 0, 0));
        images.add(new SpriteModel(this.quest.appController, missileRightImageFileName, 1.0, 0, 0));
        images.add(new SpriteModel(this.quest.appController, monsterImageFileName, 1.0, 0, 0));
        images.add(new SpriteModel(this.quest.appController, monsterHitImageFileName, 1.0, 0, 0));
        images.add(new SpriteModel(this.quest.appController, monsterMissileLeftMiniImageFileName, 1.0, 0, 0));
        images.add(new SpriteModel(this.quest.appController, monsterMissileLeftImageFileName, 1.0, 0, 0));
        images.add(new SpriteModel(this.quest.appController, monsterMissileLeftChungusImageFileName, 1.0, 0, 0));
        images.add(new SpriteModel(this.quest.appController, monsterMissileRightMiniImageFileName, 1.0, 0, 0));
        images.add(new SpriteModel(this.quest.appController, monsterMissileRightImageFileName, 1.0, 0, 0));
        images.add(new SpriteModel(this.quest.appController, monsterMissileRightChungusImageFileName, 1.0, 0, 0));
        
        // Calculate the dimensions of each image
        this.backgroundDimensions = this.quest.appController.getDimensions(backgroundImageFileName);
        this.monsterDimensions = this.quest.appController.getDimensions(monsterImageFileName);
        this.monsterMissileChungusDimensions = this.quest.appController.getDimensions(monsterMissileLeftChungusImageFileName);
        this.monsterMissileDimensions = this.quest.appController.getDimensions(monsterMissileLeftImageFileName);
        this.monsterMissileMiniDimensions = this.quest.appController.getDimensions(monsterMissileLeftMiniImageFileName);
        this.playerDimensions = this.quest.appController.getDimensions(playerImageFileName);

        // Init the animation with night owl at top center (moving right/1) and the player at bottom center (with positions relative to the background image)
        int centerX = Math.floorDiv(this.backgroundDimensions.x, 2);
        int halfMonsterX = Math.floorDiv(this.monsterDimensions.x, 2);
        int monsterPositionX = centerX - halfMonsterX;
        this.monster = new SpriteModel(this.quest.appController, monsterImageFileName, 1.0, monsterPositionX, 1);
        this.monsterDirection = 1;
        this.monsterDestinationX = this.monster.x + (int) (Math.random() * (this.backgroundDimensions.x - this.monster.x));
        this.monsterHalfwayPoint = (Math.floorDiv(Math.abs(monsterPositionX - this.monsterDestinationX), 2) * this.monsterDirection) + monsterPositionX;
        this.monsterHP = 100;
        int halfPlayerX = Math.floorDiv(this.playerDimensions.x, 2);
        this.player = new SpriteModel(this.quest.appController, playerImageFileName, 1.0, centerX - halfPlayerX, this.backgroundDimensions.y - this.playerDimensions.y);
        
        // Init missiles collections
        this.monsterMissilesAttached = new ArrayList();
        this.monsterMissilesLaunched = new ArrayList();
        this.monsterMissilesSpawned = false;
        this.playerMissilesAttached = new ArrayList();
        this.playerMissilesLaunched = new ArrayList();
        
        // TODO - Add floating text underneath the background image for the player's HP
        
        // Set the animation variables
        this.quest.variables.put("animation-started", "true");
        this.quest.variables.put("animation-on", "true");
        
        this.quest.appController.addAnimation(Questcraft.QUEST, name, row, column, backgroundImageFileName, images, 0.1, this);
        
        return "";
    }
    
}
