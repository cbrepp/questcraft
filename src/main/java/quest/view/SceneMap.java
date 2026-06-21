package quest.view;

import quest.Questcraft;
import app.controller.BaseController;
import app.color.Color;
import app.EventListener;
import app.FontStyle;
import app.HorizontalAlignment;
import app.Layout;
import app.TextDecoration;
import app.VerticalAlignment;
import static app.controller.BaseController.logger;
import app.node.Grid;
import app.node.Group;
import app.node.Label;
import app.node.Image;
import app.node.VerticalGroup;
import java.util.List;
import java.util.logging.Level;
import quest.model.Act;
import quest.model.Scene;

/**
 *
 * @author repp
 */
public class SceneMap extends app.view.BaseView implements EventListener {

    public final static String COMPASS = "COMPASS";
    public final static String EMOJI = "\uD83D\uDDFA\uFE0F";
    public Quest quest;
    public BaseController appController;
    
    public SceneMap(String name) {
        super(name);
        this.addTextArea = false;   // The text area would interfere with this view's grid layout, so prevent it here
        this.backgroundColor = new Color(255, 255, 255, 1.0);
        this.backgroundImage = "/assets/images/map.jpg";
        this.emojis.add(EMOJI); 
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        logger.log(Level.INFO, "Entered: eventName={0}, eventValue={1}", new Object[]{eventName, eventValue});
        
        switch (eventName) {
            case Quest.NEW_SCENE -> this.render();
            case Quest.PLAYER_DIRECTION -> this.render();
            case COMPASS -> this.appController.selectTab(Questcraft.QUEST);
            default -> logger.log(Level.WARNING, "Unsupported event!");
        }
    }
    
    @Override
    public void onLoad(BaseController appController) {
        logger.log(Level.INFO, "Entered: appController={0}", appController);
        
        this.appController = appController;
        this.render();
        this.quest.addListener(Quest.NEW_SCENE, this);
        this.quest.addListener(Quest.PLAYER_DIRECTION, this);
    }
    
    public boolean isSceneAdjacentToPlayer(int x, int y) {
        if ((this.quest.playerX == null) || (this.quest.playerY == null)) {
            return false;
        }
                
        int xDiff = Math.abs(x - this.quest.playerX);
        int yDiff = Math.abs(y - this.quest.playerY);
        boolean isAdjacent = ((xDiff + yDiff) <= 1);
        
        return isAdjacent;
    }
    
    public Grid getGrid(Boolean mini) {
        logger.log(Level.INFO, "Entered: mini={0}", mini);
        
        Act act = this.quest.book.acts.get(this.quest.currentAct);
        Integer minX = null, maxX = null, minY = null, maxY = null;
        
        // Parse each scene in the current act to get the dimensions of the map
        for (String sceneName : act.scenes.keySet()) {
            Scene scene = act.scenes.get(sceneName);
            
            if ((scene.x == null) || (scene.y == null)) {
                continue;
            }
            
            // For the mini map, only show the scenes that are immediately adjacent to the player
            if ((mini) && (!this.isSceneAdjacentToPlayer(scene.x, scene.y))) {
                continue;
            }
            
            logger.log(Level.INFO, "Evaluating boundaries: Reviewing scene {0} at ({1}, {2})", new Object[]{sceneName, scene.x, scene.y});
            
            if ((minX == null) || (scene.x < minX)) {
                minX = scene.x;
                logger.log(Level.INFO, "Evaluating boundaries: Scene has minimum X");
            }
            if ((maxX == null) || (scene.x > maxX)) {
                maxX = scene.x;
                logger.log(Level.INFO, "Evaluating boundaries: Scene has maximum X");
            }
            if ((minY == null) || (scene.y < minY)) {
                minY = scene.y;
                logger.log(Level.INFO, "Evaluating boundaries: Scene has minimum Y");
            }
            if ((maxY == null) || (scene.y > maxY)) {
                maxY = scene.y;
                logger.log(Level.INFO, "Evaluating boundaries: Scene has maximum Y");
            }
        }
        
        int mapWidth = 0;
        int mapHeight = 0;
        if ((minX != null) && (minY != null)) {
            mapWidth = maxX - minX + 1;
            mapHeight = maxY - minY + 1;
        } else {
            logger.log(Level.INFO, "Map does not have dimensions!");
            return null;
        }

        // Sort each scene by its coordinates
        logger.log(Level.INFO, "Sorting {0}x{1} map", new Object[]{mapWidth, mapHeight});
        String[][] sortedScenes = new String[mapWidth][mapHeight];
        for (String sceneName : act.scenes.keySet()) {
            Scene scene = act.scenes.get(sceneName);
            if ((scene.x != null) && (scene.y != null)) {
                // For the mini map, only show the scenes that are immediately adjacent to the player
                if ((mini) && (!this.isSceneAdjacentToPlayer(scene.x, scene.y))) {
                    continue;
                }
                sortedScenes[scene.x - minX][scene.y - minY] = sceneName;
                logger.log(Level.INFO, "Sorted {0} into ({1}, {2})", new Object[]{sceneName, scene.x - minX, scene.y - minY});
            }
        }
        
        TextDecoration smallItalics = new TextDecoration();
        smallItalics.style = FontStyle.ITALIC;
        smallItalics.pixelSize = Quest.DEFAULT_FONT_SIZE - 2;
        TextDecoration normalSize = new TextDecoration();
        normalSize.style = FontStyle.BOLD;
        normalSize.pixelSize = Quest.DEFAULT_FONT_SIZE;
        TextDecoration twiceAsBig = new TextDecoration();
        twiceAsBig.pixelSize = Quest.DEFAULT_FONT_SIZE * 2;

        List<String> observedActScenes = this.quest.observedScenes.get(this.quest.currentAct);
        
        // Populate the grid cells using the sorted cells
        Grid gridControl = new Grid("scene map grid");
        gridControl.borderPadding = 0;
        gridControl.columns = mapWidth;
        if (!mini) {
            gridControl.columns++;
        }
        gridControl.listener = this;
        gridControl.padding = 20;
        gridControl.showBorders = false;
        int emptyCellCount = 0;
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < gridControl.columns; x++) {
                int sceneX = x + minX;
                int sceneY = y + minY;                
                
                if ((!mini) && (x == mapWidth)) {
                    if (y == 0) {
                        // The last column is reserved for the compass
                        Group itemGroup = new VerticalGroup(COMPASS);
                        itemGroup.borderWidth = 0;
                        Image imageControl = new Image(COMPASS + " image");
                        imageControl.file = "/assets/images/compass-small.png";
                        itemGroup.nodes.put(imageControl, null);
                        gridControl.cells.add(itemGroup);
                    } else if (y == 1) {
                        int observedSceneCount = 0;
                        for (String observedSceneName : observedActScenes) {
                            Scene observedScene = act.scenes.get(observedSceneName);
                            if ((observedScene != null) && (observedScene.x != null) && (observedScene.y != null)) {
                                observedSceneCount++;
                            }
                        }
                        String explorationInfoText;
                        if (observedSceneCount == 1) {
                            explorationInfoText = "\u2139\uFE0F Discovered 1 location";
                        } else {
                            explorationInfoText = "\u2139\uFE0F Discovered " + observedSceneCount + " locations";
                        }
                        Group itemGroup = new VerticalGroup("exploration-info");
                        itemGroup.borderWidth = 0;
                        Label explorationInfo = new Label("exploration-info-text", explorationInfoText, smallItalics);
                        itemGroup.nodes.put(explorationInfo, null);
                        gridControl.cells.add(itemGroup);
                    } else {
                        emptyCellCount++;   // TODO - This is ugly
                        Group itemGroup = new VerticalGroup("EMPTY SCENE " + emptyCellCount);
                        itemGroup.borderWidth = 0;
                        gridControl.cells.add(itemGroup);
                    }
                    continue;
                }
                
                String sceneName = sortedScenes[x][y];
                Group itemGroup = new VerticalGroup(sceneName);
                itemGroup.borderWidth = 0;
                itemGroup.name = "cell " + x + "" + y;
                if (sceneName == null) {
                    emptyCellCount++;   // TODO - This is ugly
                    sceneName = "EMPTY SCENE " + emptyCellCount;
                    logger.log(Level.INFO, "Nothing to add to {0}, {1}", new Object[]{x, y});
                } else {
                    if ((this.quest.playerX != null) && (this.quest.playerX == sceneX) && (this.quest.playerY != null) && (this.quest.playerY == sceneY)) {
                        if (this.quest.getPlayerDirection().toUpperCase().equals(Quest.DIRECTION_NORTH)) {
                            logger.log(Level.INFO, "Adding NORTH direction label");
                            Label directionLabel = new Label("direction", "\u2B06", twiceAsBig);
                            itemGroup.nodes.put(directionLabel, null);
                        }
                        
                        String playerSymbol = this.quest.playerSymbol;
                        logger.log(Level.INFO, "Adding player {0} to {1}, {2}", new Object[]{playerSymbol, x, y});
                        switch (this.quest.getPlayerDirection().toUpperCase()) {
                            case Quest.DIRECTION_EAST -> {
                                logger.log(Level.INFO, "Adding EAST direction label");
                                playerSymbol += " " + "\u27A1";
                            }
                            case Quest.DIRECTION_WEST -> {
                                logger.log(Level.INFO, "Adding WEST direction label");
                                playerSymbol = "\u2B05 " + playerSymbol;
                            }
                        }
                        Label playerLabel = new Label("player", playerSymbol, twiceAsBig);
                        itemGroup.nodes.put(playerLabel, null);
                        
                        if (this.quest.getPlayerDirection().toUpperCase().equals(Quest.DIRECTION_SOUTH)) {
                            logger.log(Level.INFO, "Adding NORTH direction label");
                            Label directionLabel = new Label("direction", "\u2B07", twiceAsBig);
                            itemGroup.nodes.put(directionLabel, null);
                        }
                    }

                    Scene scene = act.scenes.get(sceneName);
                    itemGroup.backgroundColor = new Color(scene.color, 0.5);
                    Label labelControl;
                    if ((observedActScenes.isEmpty()) || (!observedActScenes.contains(sceneName))) {
                        logger.log(Level.INFO, "Found unobserved scene");
                        labelControl = new Label(sceneName + " label", "?", normalSize);
                    } else {
                        labelControl = new Label(sceneName + " label", sceneName, normalSize);
                    }
                    logger.log(Level.INFO, "Adding scene name {0} to {1}, {2}", new Object[]{sceneName, x, y});
                    itemGroup.nodes.put(labelControl, null);
                }
                if (sceneName != null) {
                    Scene scene = act.scenes.get(sceneName);
                    if ((scene != null) && (scene.symbol != null) && (observedActScenes.contains(sceneName))) {
                        logger.log(Level.INFO, "Adding scene symbol {0} to {1}, {2}", new Object[]{scene.symbol, x, y});
                        Label labelControl2 = new Label(sceneName + " symbol", scene.symbol, twiceAsBig);
                        itemGroup.nodes.put(labelControl2, null);
                    }
                }
                gridControl.cells.add(itemGroup);
            }
        }
        
        return gridControl;
    }
    
    public void render() {
        logger.log(Level.INFO, "Entered");
        this.appController.clearScreen(this.name);
        Grid gridControl = this.getGrid(false);
        if (gridControl != null) {
            this.appController.addNode(this.name, this.name, gridControl, new Layout(HorizontalAlignment.CENTER, VerticalAlignment.CENTER));
        }
    }

}
