
package quest.control;

import app.AnimationView;
import app.model.Coordinates;
import app.model.SpriteModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
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
    public Map<String, Coordinates> dimensionsMap;
    public Boolean isMonsterVisible;
    public String missileSoundFileName;
    public SpriteModel monster;
    public int monsterDestinationX;
    public int monsterDirection;
    public int monsterHalfwayPoint;
    public String monsterImageFileName;
    public Boolean monsterHalfwayReached;
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
    public String playerImageFileName;
    public Coordinates playerMissileDimensions;
    public List<SpriteModel> playerMissilesAttached;
    public List<SpriteModel> playerMissilesLaunched;
    public int playerMovedLeftCount;
    public int playerMovedRightCount;
    
    public MonsterShooterControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public List<SpriteModel> onAnimate() {
        List<SpriteModel> sprites = new ArrayList();
        
        String isPaused = this.quest.variables.get("animation-paused");
        if ((isPaused == null) || (!isPaused.toLowerCase().equals("true"))) {
            // TODO - Add difficulty variances

            // For each monster missile that is launched, increase its y coordinate
            ListIterator<SpriteModel> iterator = this.monsterMissilesLaunched.listIterator();
            while (iterator.hasNext()) {
                SpriteModel sprite = iterator.next();
                int speed = 1;
                if ((sprite.imageFile.equals(this.monsterMissileLeftMiniImageFileName)) || (sprite.imageFile.equals(this.monsterMissileRightMiniImageFileName))) {
                    speed = 5;
                } else if ((sprite.imageFile.equals(this.monsterMissileLeftImageFileName)) || (sprite.imageFile.equals(this.monsterMissileRightImageFileName))) {
                    speed = 3;
                } else if ((sprite.imageFile.equals(this.monsterMissileLeftChungusImageFileName)) || (sprite.imageFile.equals(this.monsterMissileRightChungusImageFileName))) {
                    speed = 1;
                }
                sprite.y += speed;
                if (sprite.y > (this.backgroundDimensions.y)) {
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
            iterator = this.monsterMissilesAttached.listIterator();
            while (iterator.hasNext()) {
                SpriteModel sprite = iterator.next();
                sprite.x += this.monsterDirection;
            }

            // Handle if the monster is now halfway to its destination. (Toggle between spawning and releasing missiles.)
            Boolean spawnMissiles = false;
            if ((!this.monsterHalfwayReached) && (((this.monsterDirection == 1) && (this.monster.x >= this.monsterHalfwayPoint)) || ((this.monsterDirection == -1) && (this.monster.x <= this.monsterHalfwayPoint)))) {
                this.monsterHalfwayReached = true;
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
                    this.monsterMissilesSpawned = false;
                }
            }

            // Spawn new missiles
            if (spawnMissiles) {
                Coordinates monsterDimensions = this.dimensionsMap.get(this.monsterImageFileName);
                Coordinates monsterMissileLeftChungusDimensions = this.dimensionsMap.get(this.monsterMissileLeftChungusImageFileName);
                Coordinates monsterMissileLeftDimensions = this.dimensionsMap.get(this.monsterMissileLeftImageFileName);
                Coordinates monsterMissileLeftMiniDimensions = this.dimensionsMap.get(this.monsterMissileLeftMiniImageFileName);
                // Right edge of the left-side missiles is staggered at 25%, 50%, and 100% of the distance from the x-axis center of the monster
                this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, this.monsterMissileLeftChungusImageFileName, 1.0, this.monster.x -  monsterMissileLeftChungusDimensions.x, 1));
                this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, this.monsterMissileLeftImageFileName, 1.0, this.monster.x + (int) (0.5 * monsterDimensions.x * 0.5) -  monsterMissileLeftDimensions.x, 1));
                this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, this.monsterMissileLeftMiniImageFileName, 1.0, this.monster.x + (int) (0.75 * monsterDimensions.x * 0.5) -  monsterMissileLeftMiniDimensions.x, 1));
                // Left edge of the right-side missiles is staggered at 25%, 50%, and 100% of the distance from the x-axis center of the monster
                this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, this.monsterMissileRightMiniImageFileName, 1.0, this.monster.x + monsterDimensions.x - (int) (0.75 * monsterDimensions.x * 0.5), 1));
                this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, this.monsterMissileRightImageFileName, 1.0, this.monster.x + monsterDimensions.x - (int) (0.5 * monsterDimensions.x * 0.5), 1));
                this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, this.monsterMissileRightChungusImageFileName, 1.0, this.monster.x + monsterDimensions.x, 1));
                this.monsterMissilesSpawned = true;
            }

            // Handle if the monster is now at its destination. (Change direction and calculate new destination.)
            if (((this.monsterDirection == 1) && (this.monster.x >= this.monsterDestinationX)) || ((this.monsterDirection == -1) && (this.monster.x <= this.monsterDestinationX))) {
                this.monsterHalfwayReached = false;
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
            Coordinates playerDimensions = this.dimensionsMap.get(this.playerImageFileName);
            int origPlayerX = player.x;
            if (this.quest.variables.get("animation-left").toLowerCase().equals("true")) {
                // Count consecutive times the player held down the same key and magnify distance accordingly
                this.playerMovedLeftCount++;
                if (this.playerMovedLeftCount > 15) {
                    this.playerMovedLeftCount = 15;
                }
                System.out.println("MonsterShooterControl: onAnimate: PLAYER GOES LEFT!");
                player.x -= this.playerMovedLeftCount;
                if (player.x < 1) {
                    player.x = 1;
                }
                this.quest.variables.put("animation-left", "false");
            } else {
                this.playerMovedLeftCount = 0;
            }
            if (this.quest.variables.get("animation-right").toLowerCase().equals("true")) {
                this.playerMovedRightCount++;
                if (this.playerMovedRightCount > 15) {
                    this.playerMovedRightCount = 15;
                }
                System.out.println("MonsterShooterControl: onAnimate: PLAYER GOES RIGHT!");
                player.x += this.playerMovedRightCount;
                if (player.x > (this.backgroundDimensions.x - playerDimensions.x)) {
                    player.x = (this.backgroundDimensions.x - playerDimensions.x);
                }
                this.quest.variables.put("animation-right", "false");
            } else {
                this.playerMovedRightCount = 0;
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
        }
        
        // *** Detect collisions ***
        
        // TODO - Combine monster's launched and attached missiles into a single list
        // TODO - Combine player's launched and attached missiles into a single list
        // TODO - Check for collisions between the monster's missiles and the player's missiles and the player
        // TODO - Check for collisions between the player's missiles and the monster
        // TODO - For each collision, track for a duration of time in a list and display an impact image
        // TODO - For each collision with the monster or player, also change the image to the red image
        
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
        //System.out.println("MonsterShooterControl: onAnimate: Returning " + sprites.size() + " sprites");
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
        
        this.dimensionsMap = new HashMap();
        this.name = getTagToken(tag, 1, false);
        String alignment = getTagToken(tag, 2, false);
        String backgroundImageFileName = getTagToken(tag, 3, false);
        this.playerImageFileName = getTagToken(tag, 4, false);
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

        // Calculate and cache the dimensions of each image
        this.dimensionsMap.put(backgroundImageFileName, this.quest.appController.getDimensions(backgroundImageFileName));
        this.dimensionsMap.put(this.playerImageFileName, this.quest.appController.getDimensions(this.playerImageFileName));
        this.dimensionsMap.put(playerHitImageFileName, this.quest.appController.getDimensions(playerHitImageFileName));
        this.dimensionsMap.put(missileLeftImageFileName, this.quest.appController.getDimensions(missileLeftImageFileName));
        this.dimensionsMap.put(missileRightImageFileName, this.quest.appController.getDimensions(missileRightImageFileName));
        this.dimensionsMap.put(this.monsterImageFileName, this.quest.appController.getDimensions(this.monsterImageFileName));
        this.dimensionsMap.put(this.monsterHitImageFileName, this.quest.appController.getDimensions(this.monsterHitImageFileName));
        this.dimensionsMap.put(this.monsterMissileLeftMiniImageFileName, this.quest.appController.getDimensions(this.monsterMissileLeftMiniImageFileName));
        this.dimensionsMap.put(this.monsterMissileLeftImageFileName, this.quest.appController.getDimensions(this.monsterMissileLeftImageFileName));
        this.dimensionsMap.put(this.monsterMissileLeftChungusImageFileName, this.quest.appController.getDimensions(this.monsterMissileLeftChungusImageFileName));
        this.dimensionsMap.put(this.monsterMissileRightMiniImageFileName, this.quest.appController.getDimensions(this.monsterMissileRightMiniImageFileName));
        this.dimensionsMap.put(this.monsterMissileRightImageFileName, this.quest.appController.getDimensions(this.monsterMissileRightImageFileName));
        this.dimensionsMap.put(this.monsterMissileRightChungusImageFileName, this.quest.appController.getDimensions(this.monsterMissileRightChungusImageFileName));
        
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
        this.monsterMissileChungusDimensions = this.quest.appController.getDimensions(monsterMissileLeftChungusImageFileName);
        this.monsterMissileDimensions = this.quest.appController.getDimensions(monsterMissileLeftImageFileName);
        this.monsterMissileMiniDimensions = this.quest.appController.getDimensions(monsterMissileLeftMiniImageFileName);
        this.playerDimensions = this.quest.appController.getDimensions(playerImageFileName);

        // Init the animation with night owl at top center (moving right/1) and the player at bottom center (with positions relative to the background image)
        int centerX = Math.floorDiv(this.backgroundDimensions.x, 2);
        Coordinates monsterDimensions = this.dimensionsMap.get(this.monsterImageFileName);
        int monsterCenterX = Math.floorDiv(monsterDimensions.x, 2);
        int monsterPositionX = centerX - monsterCenterX;
        this.monster = new SpriteModel(this.quest.appController, monsterImageFileName, 1.0, monsterPositionX, 1);
        this.monsterDirection = 1;
        this.monsterDestinationX = this.monster.x + (int) (Math.random() * (this.backgroundDimensions.x - this.monster.x));
        this.monsterHalfwayPoint = (Math.floorDiv(Math.abs(monsterPositionX - this.monsterDestinationX), 2) * this.monsterDirection) + monsterPositionX;
        this.monsterHalfwayReached = false;
        this.monsterHP = 100;
        int halfPlayerX = Math.floorDiv(this.playerDimensions.x, 2);
        this.player = new SpriteModel(this.quest.appController, playerImageFileName, 1.0, centerX - halfPlayerX, this.backgroundDimensions.y - this.playerDimensions.y);
        this.playerMovedLeftCount = 0;
        this.playerMovedRightCount = 0;
        
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
        
        this.quest.appController.addAnimation(Questcraft.QUEST, name, row, column, backgroundImageFileName, images, 0.05, this);
        
        return "";
    }
    
}
