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
    
    public Grid getGrid(Boolean mini) {
        logger.log(Level.INFO, "Entered: mini={0}", mini);
        
        Act act = this.quest.book.acts.get(this.quest.currentAct);
        int minX = 0, maxX = 0, minY = 0, maxY = 0;
        
        // Parse each scene in the current act to get the dimensions of the map
        for (String sceneName : act.scenes.keySet()) {
            Scene scene = act.scenes.get(sceneName);
            
            if ((scene.x == null) || (scene.y == null)) {
                continue;
            }
            
            if (scene.x < minX) {
                minX = scene.x;
            }
            if (scene.x > maxX) {
                maxX = scene.x;
            }
            if (scene.y < minY) {
                minY = scene.x;
            }
            if (scene.y > maxY) {
                maxY = scene.y;
            }
        }
        
        int mapWidth = maxX - minX + 1;
        int mapHeight = maxY - minY + 1;

        // Sort each scene by its coordinates
        String[][] sortedScenes = new String[mapWidth][mapHeight];
        for (String sceneName : act.scenes.keySet()) {
            Scene scene = act.scenes.get(sceneName);
            if ((scene.x != null) && (scene.y != null)) {
                sortedScenes[scene.x - minX][scene.y - minY] = sceneName;
            }
        }
        
        TextDecoration normalSize = new TextDecoration();
        normalSize.style = FontStyle.BOLD;
        normalSize.pixelSize = Quest.DEFAULT_FONT_SIZE;
        TextDecoration twiceAsBig = new TextDecoration();
        twiceAsBig.pixelSize = Quest.DEFAULT_FONT_SIZE * 2;
        
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
                if ((!mini) && (x == mapWidth)) {
                    if (y == 0) {
                        // The last column is reserved for the compass
                        Group itemGroup = new VerticalGroup(COMPASS);
                        itemGroup.borderWidth = 0;
                        Image imageControl = new Image(COMPASS + " image");
                        imageControl.file = "/assets/images/compass-small.png";
                        itemGroup.nodes.add(imageControl);
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
                List<String> observedActScenes = this.quest.observedScenes.get(this.quest.currentAct);
                if (sceneName == null) {
                    emptyCellCount++;   // TODO - This is ugly
                    sceneName = "EMPTY SCENE " + emptyCellCount;
                    logger.log(Level.INFO, "Nothing to add to {0}, {1}", new Object[]{x, y});
                } else {
                    if ((this.quest.playerX != null) && (this.quest.playerX == x) && (this.quest.playerY != null) && (this.quest.playerY == y)) {
                        if (this.quest.getPlayerDirection().toUpperCase().equals(Quest.DIRECTION_NORTH)) {
                            logger.log(Level.INFO, "Adding NORTH direction label");
                            Label directionLabel = new Label("direction", "\u2B06", twiceAsBig);
                            itemGroup.nodes.add(directionLabel);
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
                        itemGroup.nodes.add(playerLabel);
                        
                        if (this.quest.getPlayerDirection().toUpperCase().equals(Quest.DIRECTION_SOUTH)) {
                            logger.log(Level.INFO, "Adding NORTH direction label");
                            Label directionLabel = new Label("direction", "\u2B07", twiceAsBig);
                            itemGroup.nodes.add(directionLabel);
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
                    itemGroup.nodes.add(labelControl);
                }
                if (sceneName != null) {
                    Scene scene = act.scenes.get(sceneName);
                    if ((scene != null) && (scene.symbol != null) && (observedActScenes.contains(sceneName))) {
                        logger.log(Level.INFO, "Adding scene symbol {0} to {1}, {2}", new Object[]{scene.symbol, x, y});
                        Label labelControl2 = new Label(sceneName + " symbol", scene.symbol, twiceAsBig);
                        itemGroup.nodes.add(labelControl2);
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
        this.appController.addNode(this.name, this.name, gridControl, new Layout(HorizontalAlignment.CENTER, VerticalAlignment.CENTER));
    }

}
