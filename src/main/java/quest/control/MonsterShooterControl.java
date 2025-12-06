
package quest.control;

import app.AnimationView;
import app.Color;
import app.EventListener;
import app.model.Coordinates;
import app.model.SpriteModel;
import java.util.ArrayList;
import java.util.Arrays;
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
public class MonsterShooterControl extends QuestControl implements AnimationView, EventListener {
    
    public static String NAME = "monster-shooter";
    
    public static Double ANIMATION_COMPLETE_DELAY = 3.0;
    public static String ANIMATION_CONTROLS_NAME = "animation controls";
    public static Double ANIMATION_DELAY = 0.1;
    public static String BACKGROUND_NAME = "background";
    public static Double COLLISION_EXPIRATION = 2.0;
    public static Color COLOR_RED = new Color(255, 0, 0);
    public static String CONTINUE_BUTTON_NAME = "continue control";
    public static String DIFFICULTY_EASY = "easy";
    public static String DIFFICULTY_NORMAL = "normal";
    public static String DIFFICULTY_HARD = "hard";
    public static String DIFFICULTY_MAGICAL = "magical";
    public static String LABEL_MONSTER_HP = "monster hp";
    public static String LABEL_PLAYER_HP = "player hp";
    public static String LABEL_PLAYER_MP = "player mp";
    public static String MONSTER_MISSILE_CHUNGUS_NAME = "chungus monster missile";
    public static String MONSTER_MISSILE_MINI_NAME = "mini monster missile";
    public static String MONSTER_MISSILE_NAME = "monster missile";
    public static String MONSTER_NAME = "monster";
    public static String PLAYER_MISSILE_CHUNGUS_NAME = "chungus player missile";
    public static String PLAYER_MISSILE_MINI_NAME = "player missile";
    public static Double PLAYER_MISSILE_SPAWN_DELAY = 2.0;
    public static Double PLAYER_MISSILE_CHUNGUS_SPAWN_DELAY = 7.0;
    public static String PLAYER_NAME = "player";
    
    public String name;
    public Boolean animationComplete;
    public Double animationCompleteDelay;
    public Map<SpriteModel, Double> collisionTimerMap;
    public int column;
    public String difficulty;
    public Map<String, Coordinates> dimensionsMap;
    public Boolean isMonsterVisible;
    public SpriteModel lastPlayerLeftMissile;
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
    public int monsterSpeed;
    public int monsterSpeedDirection;
    public SpriteModel player;
    public String playerImageFileName;
    public Double playerMissileSpawnTimer;
    public Double playerMissileChungusSpawnTimer;
    public List<SpriteModel> playerMissilesAttached;
    public List<SpriteModel> playerMissilesLaunched;
    public int playerMovedLeftCount;
    public int playerMovedRightCount;
    
    public MonsterShooterControl(Quest quest) {
        super(quest);
        this.collisionTimerMap = new HashMap();
        this.playerMissileSpawnTimer = PLAYER_MISSILE_SPAWN_DELAY;
        this.playerMissileChungusSpawnTimer = PLAYER_MISSILE_CHUNGUS_SPAWN_DELAY;
        this.monsterSpeed = 0;
        this.monsterSpeedDirection = 1;
        this.animationCompleteDelay = ANIMATION_COMPLETE_DELAY;
        this.animationComplete = false;
    }
    
    @Override
    public List<SpriteModel> onAnimate() {
        if (this.animationComplete) {
            return null;
        }
        
        List<SpriteModel> sprites = new ArrayList();
        
        Coordinates backgroundDimensions = this.dimensionsMap.get(BACKGROUND_NAME);
        
        // TODO - Play hooting.mp3 each time the monster spawns new lightning bolts
        // TODO - Play zap.wav each time a lightning bolt hits something
        // TODO - Play impact.wav each time the monster is hit
        
        String isPaused = this.quest.variables.get("animation-paused");
        if ((isPaused == null) || (!isPaused.toLowerCase().equals("true"))) {
            if (this.difficulty.equals(DIFFICULTY_MAGICAL)) {
                // TODO - If Mylee has been tamed, randomly add her and she can launch her own attack
            }
            
            // Handle each missile that collided with something
            List<SpriteModel> missileList = new ArrayList<>(this.playerMissilesAttached.size() + this.playerMissilesLaunched.size() + this.monsterMissilesAttached.size() + this.monsterMissilesLaunched.size());
            missileList.addAll(this.playerMissilesAttached);
            missileList.addAll(this.playerMissilesLaunched);
            missileList.addAll(this.monsterMissilesAttached);
            missileList.addAll(this.monsterMissilesLaunched);
            for (SpriteModel sprite : missileList) {
                if ((!this.collisionTimerMap.containsKey(sprite)) && ((sprite.collisionSprites == null) || (sprite.collisionSprites.isEmpty()))) {
                    continue;
                }
                
                // Reset the collision collection so the application controller can detect new collisions
                sprite.collisionSprites.clear();
                
                if (!this.collisionTimerMap.containsKey(sprite)) {
                    // Start the expiration countdown
                    this.collisionTimerMap.put(sprite, COLLISION_EXPIRATION);
                } else {
                    // Decrement the expiration countdown
                    Double collisionExpiration = this.collisionTimerMap.get(sprite);
                    collisionExpiration -= ANIMATION_DELAY;
                    if (collisionExpiration <= 0.0) {
                        // The missile has expired.  Remove it.
                        this.collisionTimerMap.remove(sprite);
                        if (this.playerMissilesAttached.contains(sprite)) {
                            this.playerMissilesAttached.remove(sprite);
                        }
                        if (this.playerMissilesLaunched.contains(sprite)) {
                            this.playerMissilesLaunched.remove(sprite);
                        }
                        if (this.monsterMissilesAttached.contains(sprite)) {
                            this.monsterMissilesAttached.remove(sprite);
                        }
                        if (this.monsterMissilesLaunched.contains(sprite)) {
                            this.monsterMissilesLaunched.remove(sprite);
                        }
                    } else {
                        // Update the expiration countdown
                        this.collisionTimerMap.put(sprite, collisionExpiration);
                    }
                }
            }

            // For each monster missile that is launched, increase its y coordinate unless it has collided with something
            ListIterator<SpriteModel> iterator = this.monsterMissilesLaunched.listIterator();
            while (iterator.hasNext()) {                
                SpriteModel sprite = iterator.next();
                
                if (this.collisionTimerMap.containsKey(sprite)) {
                    // Missiles stop falling once they have collided with something
                    continue;
                }

                int speed;
                if (sprite.name.equals(MONSTER_MISSILE_MINI_NAME)) {
                    speed = 5;
                } else if (sprite.name.equals(MONSTER_MISSILE_NAME)) {
                    speed = 4;
                } else if (sprite.name.equals(MONSTER_MISSILE_CHUNGUS_NAME)) {
                    speed = 3;
                } else {
                    speed = 1;
                }
                sprite.y += speed;
                if ((sprite.y + speed) >= (backgroundDimensions.y)) {
                    iterator.remove();  // Missile will go past the bottom boundary, remove
                }
            }
            
            // Launch the player's missiles based on inpute
            if (this.quest.variables.get("animation-up").toLowerCase().equals("true")) {
                // Move the missile from being attached to being launched
                iterator = this.playerMissilesAttached.listIterator();
                while (iterator.hasNext()) {
                    SpriteModel sprite = iterator.next();
                    if (this.collisionTimerMap.containsKey(sprite)) {
                        // Missiles stop moving once they have collided with something
                        continue;
                    }
                    this.playerMissilesLaunched.add(sprite);
                    iterator.remove();
                }
                this.quest.variables.put("animation-up", "false");
            }

            // For each player missile that is launched, decrease its y coordinate
            iterator = this.playerMissilesLaunched.listIterator();
            while (iterator.hasNext()) {
                SpriteModel sprite = iterator.next();
                
                if (this.collisionTimerMap.containsKey(sprite)) {
                    // Missiles stop rising once they have collided with something
                    continue;
                }
                
                int speed;
                if (sprite.name.equals(PLAYER_MISSILE_MINI_NAME)) {
                    speed = 5;
                } else if (sprite.name.equals(PLAYER_MISSILE_CHUNGUS_NAME)) {
                    speed = 3;
                } else {
                    speed = 1;
                }
                sprite.y -= speed;
                if (sprite.y < 1) {
                    iterator.remove();  // Missile hit the top boundary, remove
                }
            }

            // Move the monster at an oscillating speed and height to simulate flapping wings
            this.monsterSpeed += (1 * this.monsterSpeedDirection);
            if (this.monsterSpeed > 7) {
                // Slow down the monster's speed
                this.monsterSpeed = 7;
                this.monsterSpeedDirection = -1;
            } else if (this.monsterSpeed < 1) {
                // Speed up the monster's speed
                this.monsterSpeed = 1;
                this.monsterSpeedDirection = 1;
            }
            this.monster.x += this.monsterDirection * this.monsterSpeed;
            //this.monster.y += this.monsterDirection * (this.monsterSpeed / 10);

            // Move each attached monster missile
            iterator = this.monsterMissilesAttached.listIterator();
            while (iterator.hasNext()) {
                SpriteModel sprite = iterator.next();
                if (this.collisionTimerMap.containsKey(sprite)) {
                    // Missiles stop moving once they have collided with something
                    continue;
                }
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
                        if (this.collisionTimerMap.containsKey(sprite)) {
                            // Missiles stop moving once they have collided with something
                            continue;
                        }
                        this.monsterMissilesLaunched.add(sprite);
                        iterator.remove();
                    }
                    this.monsterMissilesSpawned = false;
                }
            }

            // Spawn new monster missiles
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
                if ((this.difficulty.equals(DIFFICULTY_HARD)) || (this.difficulty.equals(DIFFICULTY_MAGICAL))) {
                    this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_CHUNGUS_NAME, this.monsterMissileLeftImageFileName, 0.2, this.monster.x - monsterMissileLeftChungusDimensions.x, 1, monsterMissilePotentialCollisions, 0.1, null));
                }
                if ((this.difficulty.equals(DIFFICULTY_NORMAL)) || (this.difficulty.equals(DIFFICULTY_HARD)) || (this.difficulty.equals(DIFFICULTY_MAGICAL))) {
                    this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_NAME, this.monsterMissileLeftImageFileName, 0.15, this.monster.x + (int) (0.5 * monsterDimensions.x * 0.5) -  monsterMissileLeftDimensions.x, 1, monsterMissilePotentialCollisions, 0.1, null));
                }
                if ((this.difficulty.equals(DIFFICULTY_EASY)) || (this.difficulty.equals(DIFFICULTY_NORMAL)) || (this.difficulty.equals(DIFFICULTY_HARD)) || (this.difficulty.equals(DIFFICULTY_MAGICAL))) {
                    this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_MINI_NAME, this.monsterMissileLeftImageFileName, 0.1, this.monster.x + (int) (0.75 * monsterDimensions.x * 0.5) -  monsterMissileLeftMiniDimensions.x, 1, monsterMissilePotentialCollisions, 0.1, null));
                }
                // Left edge of the right-side missiles is staggered at 25%, 50%, and 100% of the distance from the x-axis center of the monster
                if ((this.difficulty.equals(DIFFICULTY_EASY)) || (this.difficulty.equals(DIFFICULTY_NORMAL)) || (this.difficulty.equals(DIFFICULTY_HARD)) || (this.difficulty.equals(DIFFICULTY_MAGICAL))) {
                    this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_MINI_NAME, this.monsterMissileRightImageFileName, 0.1, this.monster.x + monsterDimensions.x - (int) (0.75 * monsterDimensions.x * 0.5), 1, monsterMissilePotentialCollisions, 0.1, null));
                }
                if ((this.difficulty.equals(DIFFICULTY_NORMAL)) || (this.difficulty.equals(DIFFICULTY_HARD)) || (this.difficulty.equals(DIFFICULTY_MAGICAL))) {
                    this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_NAME, this.monsterMissileRightImageFileName, 0.15, this.monster.x + monsterDimensions.x - (int) (0.5 * monsterDimensions.x * 0.5), 1, monsterMissilePotentialCollisions, 0.1, null));
                }
                if ((this.difficulty.equals(DIFFICULTY_HARD)) || (this.difficulty.equals(DIFFICULTY_MAGICAL))) {
                    this.monsterMissilesAttached.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_CHUNGUS_NAME, this.monsterMissileRightImageFileName, 0.2, this.monster.x + monsterDimensions.x, 1, monsterMissilePotentialCollisions, 0.1, null));
                }
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
                    if (this.collisionTimerMap.containsKey(sprite)) {
                        // Missiles stop moving once they have collided with something
                        continue;
                    }
                    sprite.x += playerDeltaX;
                }
            }
            
            // Spawn new player missiles
            if (this.playerMissileSpawnTimer > 0.0) {
                this.playerMissileSpawnTimer -= ANIMATION_DELAY;
            }
            if (this.playerMissileSpawnTimer <= 0.0) {
                if ((this.playerMissilesAttached == null) || (this.playerMissilesAttached.size() < 2)) {
                    if (this.playerMissileChungusSpawnTimer > 0.0) {
                        this.playerMissileChungusSpawnTimer--;
                    }
                    List<String> playerMissilePotentialCollisions = new ArrayList();
                    playerMissilePotentialCollisions.add(MONSTER_MISSILE_CHUNGUS_NAME);
                    playerMissilePotentialCollisions.add(MONSTER_MISSILE_NAME);
                    playerMissilePotentialCollisions.add(MONSTER_MISSILE_MINI_NAME);
                    playerMissilePotentialCollisions.add(MONSTER_NAME);
                    if (this.playerMissileChungusSpawnTimer <= 0.0) {
                        // Spawn the chungus missile
                        if ((this.playerMissilesAttached.isEmpty()) || (this.lastPlayerLeftMissile == null) || (!this.playerMissilesAttached.get(0).equals(this.lastPlayerLeftMissile))) {
                            // Spawn the missile on the left
                            Coordinates playerMissileLeftChungusDimensions = this.dimensionsMap.get(PLAYER_MISSILE_CHUNGUS_NAME);
                            SpriteModel leftMissile = new SpriteModel(this.quest.appController, PLAYER_MISSILE_CHUNGUS_NAME, this.missileLeftImageFileName, 0.2, this.player.x - playerMissileLeftChungusDimensions.x, this.player.y, playerMissilePotentialCollisions, 0.1, COLOR_RED);
                            this.playerMissilesAttached.add(leftMissile);
                            this.lastPlayerLeftMissile = leftMissile;
                        } else {
                            // Spawn the missile on the right
                            this.playerMissilesAttached.add(new SpriteModel(this.quest.appController, PLAYER_MISSILE_CHUNGUS_NAME, this.missileRightImageFileName, 0.2, this.player.x + playerDimensions.x, this.player.y, playerMissilePotentialCollisions, 0.1, COLOR_RED));
                        }
                        this.playerMissileChungusSpawnTimer = PLAYER_MISSILE_CHUNGUS_SPAWN_DELAY;
                    } else {
                        // Spawn the mini missile
                        if ((this.playerMissilesAttached.isEmpty()) || (this.lastPlayerLeftMissile == null) || (!this.playerMissilesAttached.get(0).equals(this.lastPlayerLeftMissile))) {
                            // Spawn the missile on the left
                            Coordinates playerMissileLeftMiniDimensions = this.dimensionsMap.get(PLAYER_MISSILE_MINI_NAME);
                            SpriteModel leftMissile = new SpriteModel(this.quest.appController, PLAYER_MISSILE_MINI_NAME, this.missileLeftImageFileName, 0.1, this.player.x - playerMissileLeftMiniDimensions.x, this.player.y, playerMissilePotentialCollisions, 0.1, COLOR_RED);
                            this.playerMissilesAttached.add(leftMissile);
                            this.lastPlayerLeftMissile = leftMissile;
                        } else {
                            // Spawn the missile on the right
                            this.playerMissilesAttached.add(new SpriteModel(this.quest.appController, PLAYER_MISSILE_MINI_NAME, this.missileRightImageFileName, 0.1, this.player.x + playerDimensions.x, this.player.y, playerMissilePotentialCollisions, 0.1, COLOR_RED));
                        }
                    }
                }
                this.playerMissileSpawnTimer = PLAYER_MISSILE_SPAWN_DELAY;
            }

            // Handle collisions with the player
            if (!this.player.collisionSprites.isEmpty()) {
                for (SpriteModel collidingSprite : this.player.collisionSprites) {
                    if (collidingSprite.name.equals(MONSTER_MISSILE_CHUNGUS_NAME)) {
                        this.quest.setPlayerHP(-3, false, "Night Owl", false);
                    } else if (collidingSprite.name.equals(MONSTER_MISSILE_NAME)) {
                        this.quest.setPlayerHP(-2, false, "Night Owl", false);
                    } else if (collidingSprite.name.equals(MONSTER_MISSILE_MINI_NAME)) {
                        this.quest.setPlayerHP(-1, false, "Night Owl", false);
                    }
                    this.quest.appController.updateFloatingText(Questcraft.QUEST, LABEL_PLAYER_HP, "HP: " + String.valueOf(this.quest.getPlayerHP()));
                }
                this.player.collisionSprites.clear();
                this.player.glowColor = null;
            }
            if (!this.monster.collisionSprites.isEmpty()) {
                for (SpriteModel collidingSprite : this.monster.collisionSprites) {
                    if (collidingSprite.name.equals(PLAYER_MISSILE_CHUNGUS_NAME)) {
                        this.monsterHP -= 3;
                    } else if (collidingSprite.name.equals(PLAYER_MISSILE_MINI_NAME)) {
                        this.monsterHP -= 1;
                    }
                    this.quest.appController.updateFloatingText(Questcraft.QUEST, LABEL_PLAYER_MP, "BOSS HP: " + String.valueOf(this.monsterHP));
                }
                this.monster.collisionSprites.clear();
            }
        }
        
        if (this.quest.variables.get("animation-complete").equals("true")) {
            if ((this.animationCompleteDelay <= 0.0) && (this.animationCompleteDelay > -1.0)) {
                // Display the Continue button that will refresh the pages to let the quest resume
                int buttonRow = this.quest.buttonRow;
                this.quest.appController.displayButton(this.quest.name, CONTINUE_BUTTON_NAME, "Continue", buttonRow, this.column, null, null, false, null, true, this);
                this.animationCompleteDelay = -1.0;
                this.quest.appController.stopAllSounds();
            } else if (this.animationCompleteDelay > -1.0) {
                // Wait a few iterations of the animation (to prevent the user from accidentally pressing it) before displaying the Continue button
                this.animationCompleteDelay -= ANIMATION_DELAY;
            }
        } else if ((this.quest.getPlayerHP() <= 0) || (this.monsterHP <= 0)) {
            this.quest.variables.put("animation-paused", "true");
            this.quest.variables.put("animation-complete", "true");
            this.quest.appController.clearControl(this.quest.name, ANIMATION_CONTROLS_NAME);
        }
        
        // Return all sprites
        sprites.add(this.player);
        sprites.add(this.monster);
        for (SpriteModel sprite : this.playerMissilesAttached) {
            sprites.add(sprite);
        }
        for (SpriteModel sprite : this.playerMissilesLaunched) {
            sprites.add(sprite);
        }
        for (SpriteModel sprite : this.monsterMissilesAttached) {
            sprites.add(sprite);
        }
        for (SpriteModel sprite : this.monsterMissilesLaunched) {
            sprites.add(sprite);
        }
        
        // Return null to stop the animation
        //System.out.println("MonsterShooterControl: onAnimate: Returning " + sprites.size() + " sprites");
        return sprites;
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("MonsterShooterControl: onEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        if (eventName.equals(CONTINUE_BUTTON_NAME)) {
            this.animationComplete = true;
            this.quest.appController.clearControl(this.quest.name, CONTINUE_BUTTON_NAME);
            this.quest.display(); // Refresh the pages
        } else {
            if (eventValue.equals(" Move Left")) {
                this.quest.variables.put("animation-left", "true");
            } else if (eventValue.equals(" Move Right")) {
                this.quest.variables.put("animation-right", "true");
            } else if (eventValue.equals(" Launch")) {
                this.quest.variables.put("animation-up", "true");
            } else if (eventValue.equals("P")) {
                if (this.quest.variables.get("animation-paused").equals("true")) {
                    this.quest.variables.put("animation-paused", "false");
                } else {
                    this.quest.variables.put("animation-paused", "true");
                }
                this.quest.appController.playSound("/assets/sounds/pause.mp3", false);
            } else {
                System.out.println("MonsterShooterControl: onEvent: Unsupported event value!");
            }
        }
    }
    
    /*
        Game variables:
        animation-complete - "true" when the animation has reached its conclusion
        animation-on - "true" when the animation is in progress
        animation-started - "true" when the animation has begun
        animation-left - "true" moves the player to the left
        animation-right - "true" moves the player to the right
        animation-up - "true" launches the player's attached missiles
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
        String difficultyVariable = getTagToken(tag, 14, false);
        
        if (this.quest.variables.containsKey(difficultyVariable)) {
            this.difficulty = this.quest.variables.get(difficultyVariable).toLowerCase();
        } else {
            this.difficulty = "easy";
        }
        System.out.println("MonsterShooterControl: onExecute: difficulty=" + this.difficulty);

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
                this.column = startingColumn + halfColumns - halfImageWidth + 1;
            }
            case "RIGHT" -> {
                int imageWidth = backgroundDimensions.x;
                this.column = endingColumn - imageWidth + 1;
            }
            default -> this.column = startingColumn;
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

        images.add(new SpriteModel(this.quest.appController, PLAYER_NAME, playerImageFileName, 0.2, 0, 0, null, 0.0, null));
        images.add(new SpriteModel(this.quest.appController, PLAYER_MISSILE_CHUNGUS_NAME, this.missileLeftImageFileName, 0.2, 0, 0, playerMissilePotentialCollisions, 0.1, COLOR_RED));
        images.add(new SpriteModel(this.quest.appController, PLAYER_MISSILE_MINI_NAME, this.missileLeftImageFileName, 0.1, 0, 0, playerMissilePotentialCollisions, 0.1, COLOR_RED));
        images.add(new SpriteModel(this.quest.appController, PLAYER_MISSILE_CHUNGUS_NAME, this.missileRightImageFileName, 0.2, 0, 0, playerMissilePotentialCollisions, 0.1, COLOR_RED));
        images.add(new SpriteModel(this.quest.appController, PLAYER_MISSILE_MINI_NAME, this.missileRightImageFileName, 0.1, 0, 0, playerMissilePotentialCollisions, 0.1, COLOR_RED));
        images.add(new SpriteModel(this.quest.appController, MONSTER_NAME, monsterImageFileName, 0.2, 0, 0, null, 0.0, null));
        images.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_MINI_NAME, monsterMissileLeftImageFileName, 0.1, 0, 0, monsterMissilePotentialCollisions, 0.1, null));
        images.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_NAME, monsterMissileLeftImageFileName, 0.15, 0, 0, monsterMissilePotentialCollisions, 0.1, null));
        images.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_CHUNGUS_NAME, monsterMissileLeftImageFileName, 0.2, 0, 0, monsterMissilePotentialCollisions, 0.1, null));
        images.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_MINI_NAME, monsterMissileRightImageFileName, 0.1, 0, 0, monsterMissilePotentialCollisions, 0.1, null));
        images.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_NAME, monsterMissileRightImageFileName, 0.15, 0, 0, monsterMissilePotentialCollisions, 0.1, null));
        images.add(new SpriteModel(this.quest.appController, MONSTER_MISSILE_CHUNGUS_NAME, monsterMissileRightImageFileName, 0.2, 0, 0, monsterMissilePotentialCollisions, 0.1, null));

        // Init the animation with monster at top center (moving right/1) and the player at bottom center (with positions relative to the background image)
        int centerX = Math.floorDiv(backgroundDimensions.x, 2);
        Coordinates monsterDimensions = this.dimensionsMap.get(MONSTER_NAME);
        int monsterCenterX = Math.floorDiv(monsterDimensions.x, 2);
        int monsterPositionX = centerX - monsterCenterX;
        this.monster = new SpriteModel(this.quest.appController, MONSTER_NAME, monsterImageFileName, 0.2, monsterPositionX, 1, null, 0.0, null);
        this.monsterDirection = 1;
        this.monsterDestinationX = this.monster.x + (int) (Math.random() * (backgroundDimensions.x - this.monster.x));
        this.monsterHalfwayPoint = (Math.floorDiv(Math.abs(monsterPositionX - this.monsterDestinationX), 2) * this.monsterDirection) + monsterPositionX;
        this.monsterHalfwayReached = false;
        this.monsterHP = 100;
        Coordinates playerDimensions = this.dimensionsMap.get(PLAYER_NAME);
        int halfPlayerX = Math.floorDiv(playerDimensions.x, 2);
        SpriteModel player = new SpriteModel(this.quest.appController, PLAYER_NAME, playerImageFileName, 0.2, centerX - halfPlayerX, backgroundDimensions.y - playerDimensions.y, null, 0.0, null) {
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
        
        // Initialize the animation variables
        this.quest.variables.put("animation-complete", "false");
        this.quest.variables.put("animation-left", "false");
        this.quest.variables.put("animation-on", "true");
        this.quest.variables.put("animation-right", "false");
        this.quest.variables.put("animation-started", "true");
        this.quest.variables.put("animation-up", "false");
        
        // Add floating text above the animation to indicate the player and monster's stats
        this.quest.appController.displayFloatingText(Questcraft.QUEST, LABEL_PLAYER_HP, "HP: " + String.valueOf(this.quest.getPlayerHP()), row, this.column, null, null, null, 12, null, "RobotoMono-Medium");
        this.quest.appController.displayFloatingText(Questcraft.QUEST, LABEL_PLAYER_MP, "MP: " + String.valueOf(this.quest.getPlayerMP()), row, this.column + 17, null, null, null, 12, null, "RobotoMono-Medium");
        this.quest.appController.displayFloatingText(Questcraft.QUEST, LABEL_PLAYER_MP, "Monster HP: " + String.valueOf(this.monsterHP), row, this.column + 34, null, null, null, 12, null, "RobotoMono-Medium");

        // Initialize the animation
        this.quest.appController.addAnimation(Questcraft.QUEST, name, row + 3, this.column, backgroundImageFileName, images, ANIMATION_DELAY, this);
        
        // Display the buttons used to control the animation
        List<String> valueList = new ArrayList<>(Arrays.asList("&left; Move Left+&up; Launch+&right; Move Right+P".split("\\+")));
        int buttonRow = this.quest.buttonRow;
        int endColumn;
        if (this.quest.currentDisplayPage == Quest.RIGHT_PAGE) {
            endColumn = this.quest.rightPageEndingColumn + 2;
        } else {
            endColumn = this.quest.leftPageEndingColumn + 2;
        }
        this.quest.appController.displayValidatedInputField(this.quest.name, ANIMATION_CONTROLS_NAME, valueList, buttonRow, this.column, endColumn, 0, this, true);
        
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
