package quest.view;

import app.ApplicationController;
import app.Color;
import app.EventListener;
import app.control.BaseControl;
import app.control.LabelControl;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import app.control.ImageControl;
import app.control.LinkControl;
import java.util.List;
import quest.model.Act;
import quest.model.Scene;

/**
 *
 * @author repp
 */
public class SceneMap extends app.ApplicationView implements EventListener {

    public final static String COMPASS = "COMPASS";
    public Quest quest;
    public ApplicationController appController;
    
    public SceneMap(String name) {
        super(name);
        this.addTextArea = false;   // The text area would interfere with this view's grid layout, so prevent it here
        this.backgroundColor = new Color(255, 255, 255);
        this.backgroundImage = "/assets/images/map.jpg";
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("SceneMap: onEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        switch (eventName) {
            case Quest.NEW_SCENE -> this.render();
            case Quest.PLAYER_DIRECTION -> this.render();
            case COMPASS -> this.appController.selectTab(Questcraft.QUEST);
            default -> System.err.println("SceneMap: onEvent: UNSUPPORTED EVENT!");
        }
    }
    
    @Override
    public void onLoad(ApplicationController appController) {
        System.out.println("SceneMap: onLoad");
        
        this.appController = appController;
        this.render();
        this.quest.addListener(Quest.NEW_SCENE, this);
        this.quest.addListener(Quest.PLAYER_DIRECTION, this);
    }
    
    public void render() {
        System.out.println("SceneMap: render");
        
        this.appController.clearScreen(this.name);
        
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
        System.out.println("SceneMap: render: Calculated " + mapWidth + "x" + mapHeight + " grid with X coordinates " + minX + "-" + maxY + " and Y coordinates " + minY + "-" + maxY);

        // Sort each scene by its coordinates
        String[][] sortedScenes = new String[mapWidth][mapHeight];
        for (String sceneName : act.scenes.keySet()) {
            Scene scene = act.scenes.get(sceneName);
            if ((scene.x != null) && (scene.y != null)) {
                sortedScenes[scene.x - minX][scene.y - minY] = sceneName;
            }
        }
        
        // Populate the grid cells using the sorted cells
        java.util.Map<String, ArrayList<BaseControl>> gridCells = new LinkedHashMap<>();
        int emptyCellCount = 0;
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x <= mapWidth; x++) {
                if (x == mapWidth) {
                    if (y == 0) {
                        // The last column is reserved for the compass
                        ArrayList<BaseControl> controlList = new ArrayList();
                        ImageControl imageControl = new ImageControl("/assets/images/compass-small.png", null);
                        controlList.add(imageControl);
                        LinkControl linkControl = new LinkControl("<a>Return to Quest</a>", null);
                        controlList.add(linkControl);
                        gridCells.put(COMPASS, controlList);
                    } else {
                        ArrayList<BaseControl> controlList = new ArrayList();
                        emptyCellCount++;   // TODO - This is ugly
                        String compassCell = "EMPTY SCENE " + emptyCellCount;
                        gridCells.put(compassCell, controlList);
                    }
                    continue;
                }
                
                String sceneName = sortedScenes[x][y];
                ArrayList<BaseControl> controlList = new ArrayList();
                Color backgroundColor = null;
                List<String> observedActScenes = this.quest.observedScenes.get(this.quest.currentAct);
                if (sceneName == null) {
                    emptyCellCount++;   // TODO - This is ugly
                    sceneName = "EMPTY SCENE " + emptyCellCount;
                    System.out.println("SceneMap: render: Nothing to add to " + x + ", " + y);
                } else {
                    Scene scene = act.scenes.get(sceneName);
                    LabelControl labelControl;
                    if ((observedActScenes.isEmpty()) || (!observedActScenes.contains(sceneName))) {
                        labelControl = new LabelControl("?", backgroundColor);
                        System.out.println("SceneMap: render: Adding unobserved scene to " + x + ", " + y);
                    } else {
                        backgroundColor = scene.color;
                        labelControl = new LabelControl(sceneName, backgroundColor);
                        System.out.println("SceneMap: render: Adding " + sceneName + " to " + x + ", " + y);
                    }
                    controlList.add(labelControl);
                }
                if ((this.quest.playerX != null) && (this.quest.playerX == x) && (this.quest.playerY != null) && (this.quest.playerY == y)) {
                    String playerSymbol = this.quest.playerSymbol;
                    switch (this.quest.getPlayerDirection().toUpperCase()) {
                        case Quest.DIRECTION_EAST -> playerSymbol += " " + "\u27A1";
                        case Quest.DIRECTION_NORTH -> playerSymbol += " " + "\u2B06";
                        case Quest.DIRECTION_SOUTH -> playerSymbol += " " + "\u2B07";
                        case Quest.DIRECTION_WEST -> playerSymbol += " " + "\u2B05";
                    }
                    LabelControl labelControl = new LabelControl(playerSymbol, backgroundColor);
                    controlList.add(labelControl);
                    System.out.println("SceneMap: render: Added " + playerSymbol + " to " + x + ", " + y);
                }
                if (sceneName != null) {
                    Scene scene = act.scenes.get(sceneName);
                    if ((scene != null) && (scene.symbol != null) && (observedActScenes.contains(sceneName))) {
                        LabelControl labelControl2 = new LabelControl(scene.symbol, backgroundColor);
                        controlList.add(labelControl2);
                        System.out.println("SceneMap: render: Added " + scene.symbol + " to " + x + ", " + y);
                    }
                }
                gridCells.put(sceneName, controlList);
            }
        }
        
        this.appController.displayGrid(this.name, gridCells, mapWidth + 1, false, this);
    }

}
