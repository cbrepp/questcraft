
package quest.control;

import app.AnimationView;
import app.Color;
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
    
    public static String BACKGROUND_NAME = "background";
    public static String MONSTER_MISSILE_CHUNGUS_NAME = "chungus monster missile";
    public static String MONSTER_MISSILE_MINI_NAME = "mini monster missile";
    public static String MONSTER_MISSILE_NAME = "monster missile";
    public static String MONSTER_NAME = "monster";
    public static String PLAYER_MISSILE_CHUNGUS_NAME = "chungus player missile";
    public static String PLAYER_MISSILE_MINI_NAME = "player missile";
    public static String PLAYER_NAME = "player";
    
    public String name;
    public Map<String, Coordinates> dimensionsMap;
    public Boolean isMonsterVisible;
    public String missileLeftImageFileName;
    public String missileRightImageFileName;
    public String missileSoundFileName;
    public SpriteModel monster;
    public int monsterDestinationX;
    public int monsterDirection;
    public int monsterHalfwayPoint;
    public String monsterImageFileName;
    public Boolean monsterHalfwayReached;
    public String monsterHitImageFileName;
    public int monsterHP;
    public String monsterMissileLeftImageFileName;
    public String monsterMissileRightImageFileName;
    public List<SpriteModel> monsterMissilesAttached;
    public List<SpriteModel> monsterMissilesLaunched;
    public Boolean monsterMissilesSpawned;
    public String monsterMissileSoundFileName;
    public String monsterSoundFileName;
    public SpriteModel player;
    public String playerImageFileName;
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
        
        Coordinates backgroundDimensions = this.dimensionsMap.get(BACKGROUND_NAME);
        
        String isPaused = this.quest.variables.get("animation-paused");
        if ((isPaused == null) || (!isPaused.toLowerCase().equals("true"))) {
            // TODO - Add difficulty variances
            // TODO - If Mylee has been tamed, randomly add her and she can launch her own attack

            // For each monster missile that is launched, increase its y coordinate
            ListIterator<SpriteModel> iterator = this.monsterMissilesLaunched.listIterator();
            while (iterator.hasNext()) {
                SpriteModel sprite = iterator.next();
                int speed = 1;
                if (sprite.name.equals(MONSTER_MISSILE_MINI_NAME)) {
                    speed = 5;
                } else if (sprite.name.equals(MONSTER_MISSILE_NAME)) {
                    speed = 4;
                } else if (sprite.name.equals(MONSTER_MISSILE_CHUNGUS_NAME)) {
                    speed = 2;
                }
                sprite.y += speed;
                if ((sprite.y + speed) >= (backgroundDimensions.y)) {
                    iterator.remove();  // Missile will go past the bottom boundary, remove
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
            int monsterSpeed = 2;
            this.monster.x += this.monsterDirection * monsterSpeed;

            // Move each attached monster missile
            iterator = this.monsterMissilesAttached.listIterator();
            while (iterator.hasNext()) {
                SpriteModel sprite = iterator.next();
                sprite.x += this.monsterDirection * monsterSpeed;
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
                List<String> monsterMissilePotentialCollisions = new ArrayList();
                monsterMissilePotentialCollisions.add(PLAYER_MISSILE_CHUNGUS_NAME);
                monsterMissilePotentialCollisions.add(PLAYER_MISSILE_MINI_NAME);
                monsterMissilePotentialCollisions.add(PLAYER_NAME);
                Coordinates monsterDimensions = this.dimensionsMap.get(MONSTER_NAME);
                Coordinates monsterMissileLeftChungusDimensions = this.dimensionsMap.get(MONSTER_MISSILE_CHUNGUS_NAME);
                Coordinates monsterMissileLeftDimensions = this.dimensionsMap.get(MONSTER_MISSILE_CHUNGUS_NAME);
                Coordinates monsterMissileLeftMiniDimensions = this.dimensionsMap.get(MONSTER_MISSILE_MINI_NAME);
                // Right edge of the left-side missiles is staggered at 25%, 50%, and 100% of the distance from the x-axis center of the monster
                this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_CHUNGUS_NAME, this.monsterMissileLeftImageFileName, 0.2, this.monster.x - monsterMissileLeftChungusDimensions.x, 1, monsterMissilePotentialCollisions, 0.1));
                this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_NAME, this.monsterMissileLeftImageFileName, 0.15, this.monster.x + (int) (0.5 * monsterDimensions.x * 0.5) -  monsterMissileLeftDimensions.x, 1, monsterMissilePotentialCollisions, 0.1));
                this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_MINI_NAME, this.monsterMissileLeftImageFileName, 0.1, this.monster.x + (int) (0.75 * monsterDimensions.x * 0.5) -  monsterMissileLeftMiniDimensions.x, 1, monsterMissilePotentialCollisions, 0.1));
                // Left edge of the right-side missiles is staggered at 25%, 50%, and 100% of the distance from the x-axis center of the monster
                this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_MINI_NAME, this.monsterMissileRightImageFileName, 0.1, this.monster.x + monsterDimensions.x - (int) (0.75 * monsterDimensions.x * 0.5), 1, monsterMissilePotentialCollisions, 0.1));
                this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_NAME, this.monsterMissileRightImageFileName, 0.15, this.monster.x + monsterDimensions.x - (int) (0.5 * monsterDimensions.x * 0.5), 1, monsterMissilePotentialCollisions, 0.1));
                this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_CHUNGUS_NAME, this.monsterMissileRightImageFileName, 0.2, this.monster.x + monsterDimensions.x, 1, monsterMissilePotentialCollisions, 0.1));
                this.monsterMissilesSpawned = true;
            }

            // Handle if the monster is now at its destination. (Change direction and calculate new destination.)
            if (((this.monsterDirection == 1) && (this.monster.x >= this.monsterDestinationX)) || ((this.monsterDirection == -1) && (this.monster.x <= this.monsterDestinationX))) {
                this.monsterHalfwayReached = false;
                System.out.println("MonsterShooterControl: onAnimate: PIVOT!!!!");
                this.monsterDirection *= -1;
                if (this.monsterDirection == 1) {
                    // Moving right
                    this.monsterDestinationX = this.monster.x + (int) (Math.random() * (backgroundDimensions.x - this.monster.x));
                } else {
                    // Moving left
                    this.monsterDestinationX = this.monster.x - (int) (Math.random() * (this.monster.x));
                }
                this.monsterHalfwayPoint = (Math.floorDiv(Math.abs(this.monster.x - this.monsterDestinationX), 2) * this.monsterDirection) + this.monster.x;
            }

            // Move the player based on input
            Coordinates playerDimensions = this.dimensionsMap.get(PLAYER_NAME);
            int origPlayerX = player.x;
            if (this.quest.variables.get("animation-left").toLowerCase().equals("true")) {
                // Count consecutive times the player held down the same key and magnify distance accordingly
                this.playerMovedRightCount = 0;
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
            }
            if (this.quest.variables.get("animation-right").toLowerCase().equals("true")) {
                this.playerMovedLeftCount = 0;
                this.playerMovedRightCount++;
                if (this.playerMovedRightCount > 15) {
                    this.playerMovedRightCount = 15;
                }
                System.out.println("MonsterShooterControl: onAnimate: PLAYER GOES RIGHT!");
                player.x += this.playerMovedRightCount;
                if (player.x > (backgroundDimensions.x - playerDimensions.x)) {
                    player.x = (backgroundDimensions.x - playerDimensions.x);
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

            // *** Detect collisions ***

            // TODO - Check for collisions between the monster's launched missiles and the player's launched missiles, attached missiles, and the player
            iterator = this.monsterMissilesLaunched.listIterator();
            while (iterator.hasNext()) {
                SpriteModel sprite = iterator.next();
                
                
            }
            
            // TODO - Check for collisions between the player's launched missiles and the monster's attached missiles and the monster
            
            // TODO - For each collision, track for a duration of time in a list and display an impact image
            // TODO - Each non-paused call back into this method iterates the duration
            // TODO - Once a few seconds has passed (longer wait time for bigger/Chungus collisions), stop tracking the collision
            
            // TODO - For each collision with the monster or player, also change the image to the red image
        }
        

        
        // Return all sprites
        this.player.collisionSprites.clear();
        sprites.add(this.player);
        this.monster.collisionSprites.clear();
        sprites.add(this.monster);
        for (SpriteModel sprite : this.playerMissilesAttached) {
            sprite.collisionSprites.clear();
            sprites.add(sprite);
        }
        for (SpriteModel sprite : this.monsterMissilesAttached) {
            sprite.collisionSprites.clear();
            sprites.add(sprite);
        }
        for (SpriteModel sprite : this.playerMissilesLaunched) {
            sprite.collisionSprites.clear();
            sprites.add(sprite);
        }
        for (SpriteModel sprite : this.monsterMissilesLaunched) {
            sprite.collisionSprites.clear();
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
        this.missileLeftImageFileName = getTagToken(tag, 5, false);
        this.missileRightImageFileName = getTagToken(tag, 6, false);
        this.missileSoundFileName = getTagToken(tag, 7, false);
        this.monsterImageFileName = getTagToken(tag, 8, false);
        this.monsterMissileLeftImageFileName = getTagToken(tag, 9, false);
        this.monsterMissileRightImageFileName = getTagToken(tag, 10, false);
        this.monsterMissileSoundFileName = getTagToken(tag, 11, false);
        this.monsterSoundFileName = getTagToken(tag, 12, false);
        this.isMonsterVisible = Boolean.valueOf(getTagToken(tag, 13, false).toLowerCase());

        // Calculate and cache the dimensions of each image
        Coordinates backgroundDimensions = this.quest.appController.getDimensions(backgroundImageFileName);
        this.dimensionsMap.put(BACKGROUND_NAME, backgroundDimensions);
        this.dimensionsMap.put(PLAYER_NAME, this.getScaledDimensions(this.playerImageFileName, backgroundDimensions.y, 0.2));
        this.dimensionsMap.put(PLAYER_MISSILE_CHUNGUS_NAME, this.getScaledDimensions(this.missileLeftImageFileName, backgroundDimensions.y, 0.2));
        this.dimensionsMap.put(PLAYER_MISSILE_MINI_NAME, this.getScaledDimensions(this.missileRightImageFileName, backgroundDimensions.y, 0.1));
        this.dimensionsMap.put(MONSTER_NAME, this.getScaledDimensions(this.monsterImageFileName, backgroundDimensions.y, 0.2));
        this.dimensionsMap.put(MONSTER_MISSILE_MINI_NAME, this.getScaledDimensions(this.monsterMissileLeftImageFileName, backgroundDimensions.y, 0.1));
        this.dimensionsMap.put(MONSTER_MISSILE_NAME, this.getScaledDimensions(this.monsterMissileLeftImageFileName, backgroundDimensions.y, 0.15));
        this.dimensionsMap.put(MONSTER_MISSILE_CHUNGUS_NAME, this.getScaledDimensions(this.monsterMissileLeftImageFileName, backgroundDimensions.y, 0.2));
        
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
                int halfImageWidth = (backgroundDimensions.x / 2);
                column = startingColumn + halfColumns - halfImageWidth + 1;
            }
            case "RIGHT" -> {
                int imageWidth = backgroundDimensions.x;
                column = endingColumn - imageWidth + 1;
            }
            default -> column = startingColumn;
        }
        
        // Build collision lists
        List<String> monsterMissilePotentialCollisions = new ArrayList();
        monsterMissilePotentialCollisions.add(PLAYER_MISSILE_CHUNGUS_NAME);
        monsterMissilePotentialCollisions.add(PLAYER_MISSILE_MINI_NAME);
        monsterMissilePotentialCollisions.add(PLAYER_NAME);
        List<String> playerMissilePotentialCollisions = new ArrayList();
        playerMissilePotentialCollisions.add(MONSTER_MISSILE_CHUNGUS_NAME);
        playerMissilePotentialCollisions.add(MONSTER_MISSILE_MINI_NAME);
        playerMissilePotentialCollisions.add(MONSTER_MISSILE_NAME);
        playerMissilePotentialCollisions.add(MONSTER_NAME);
        
        // Build a list of needed sprite images so the controller can cache them
        List<SpriteModel> images = new ArrayList();

        images.add(new SpriteModel(this.quest.appController, PLAYER_NAME, playerImageFileName, 0.2, 0, 0, null, 0.0));
        images.add(new SpriteModel(this.quest.appController, PLAYER_MISSILE_CHUNGUS_NAME, this.missileLeftImageFileName, 0.2, 0, 0, playerMissilePotentialCollisions, 0.1));
        images.add(new SpriteModel(this.quest.appController, PLAYER_MISSILE_MINI_NAME, this.missileLeftImageFileName, 0.1, 0, 0, playerMissilePotentialCollisions, 0.1));
        images.add(new SpriteModel(this.quest.appController, PLAYER_MISSILE_CHUNGUS_NAME, this.missileRightImageFileName, 0.2, 0, 0, playerMissilePotentialCollisions, 0.1));
        images.add(new SpriteModel(this.quest.appController, PLAYER_MISSILE_MINI_NAME, this.missileRightImageFileName, 0.1, 0, 0, playerMissilePotentialCollisions, 0.1));
        images.add(new SpriteModel(this.quest.appController, MONSTER_NAME, monsterImageFileName, 0.2, 0, 0, null, 0.0));
        images.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_MINI_NAME, monsterMissileLeftImageFileName, 0.1, 0, 0, monsterMissilePotentialCollisions, 0.1));
        images.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_NAME, monsterMissileLeftImageFileName, 0.15, 0, 0, monsterMissilePotentialCollisions, 0.1));
        images.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_CHUNGUS_NAME, monsterMissileLeftImageFileName, 0.2, 0, 0, monsterMissilePotentialCollisions, 0.1));
        images.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_MINI_NAME, monsterMissileRightImageFileName, 0.1, 0, 0, monsterMissilePotentialCollisions, 0.1));
        images.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_NAME, monsterMissileRightImageFileName, 0.15, 0, 0, monsterMissilePotentialCollisions, 0.1));
        images.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_CHUNGUS_NAME, monsterMissileRightImageFileName, 0.2, 0, 0, monsterMissilePotentialCollisions, 0.1));

        // Init the animation with monster at top center (moving right/1) and the player at bottom center (with positions relative to the background image)
        int centerX = Math.floorDiv(backgroundDimensions.x, 2);
        Coordinates monsterDimensions = this.dimensionsMap.get(MONSTER_NAME);
        int monsterCenterX = Math.floorDiv(monsterDimensions.x, 2);
        int monsterPositionX = centerX - monsterCenterX;
        this.monster = new SpriteModel(this.quest.appController, MONSTER_NAME, monsterImageFileName, 0.2, monsterPositionX, 1, null, 0.0);
        this.monsterDirection = 1;
        this.monsterDestinationX = this.monster.x + (int) (Math.random() * (backgroundDimensions.x - this.monster.x));
        this.monsterHalfwayPoint = (Math.floorDiv(Math.abs(monsterPositionX - this.monsterDestinationX), 2) * this.monsterDirection) + monsterPositionX;
        this.monsterHalfwayReached = false;
        this.monsterHP = 100;
        Coordinates playerDimensions = this.dimensionsMap.get(PLAYER_NAME);
        int halfPlayerX = Math.floorDiv(playerDimensions.x, 2);
        SpriteModel player = new SpriteModel(this.quest.appController, PLAYER_NAME, playerImageFileName, 0.2, centerX - halfPlayerX, backgroundDimensions.y - playerDimensions.y, null, 0.0) {
            @Override
            public void onCollision(SpriteModel collidingSprite) {
                if (collidingSprite.name.equals(MONSTER_MISSILE_CHUNGUS_NAME) || collidingSprite.name.equals(MONSTER_MISSILE_NAME) || collidingSprite.name.equals(MONSTER_MISSILE_MINI_NAME)) {
                    this.glowColor = new Color(255, 255, 0);
                }
            }
        };
        this.player = player;
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
        
        this.quest.appController.addAnimation(Questcraft.QUEST, name, row, column, backgroundImageFileName, images, 0.1, this);
        
        return "";
    }
    
    public Coordinates getScaledDimensions(String imageFileName, int relativeHeight, Double scale) {
        Coordinates dimensions = this.quest.appController.getDimensions(imageFileName);
        double originalY = dimensions.y;
        double y = ((double) relativeHeight * scale);
        double x = ((double) dimensions.x * (y / originalY));
        dimensions.y = (int) Math.floor(y);
        dimensions.x = (int) Math.floor(x);
        return dimensions;
    }
    
}
