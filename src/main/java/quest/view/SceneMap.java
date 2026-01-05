package quest.view;

import app.Alignment;
import app.ApplicationController;
import app.Color;
import app.EventListener;
import app.Layout;
import app.control.BaseControl;
import app.control.GridControl;
import app.control.Group;
import app.control.LabelControl;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import app.control.ImageControl;
import app.control.LinkControl;
import app.control.VerticalGroup;
import java.util.List;
import quest.model.Act;
import quest.model.Scene;

/**
 *
 * @author repp
 */
public class SceneMap extends app.ApplicationView implements EventListener {

    public final static String COMPASS = "COMPASS";
    public final static String EMOJI = "\uD83D\uDDFA\uFE0F";
    public Quest quest;
    public ApplicationController appController;
    
    public SceneMap(String name) {
        super(name);
        this.addTextArea = false;   // The text area would interfere with this view's grid layout, so prevent it here
        this.backgroundColor = new Color(255, 255, 255);
        this.backgroundImage = "/assets/images/map.jpg";
        this.emojis.add(EMOJI); 
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
        GridControl gridControl = new GridControl(this.name, new Layout(Alignment.CENTER, Alignment.CENTER));
        gridControl.borderPadding = 0;
        gridControl.columns = mapWidth + 1;
        gridControl.listener = this;
        gridControl.padding = 20;
        gridControl.showBorders = false;
        int emptyCellCount = 0;
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x <= mapWidth; x++) {
                if (x == mapWidth) {
                    if (y == 0) {
                        // The last column is reserved for the compass
                        Group itemGroup = new VerticalGroup(COMPASS, new Layout(Alignment.CENTER, Alignment.CENTER));
                        ImageControl imageControl = new ImageControl(COMPASS + " image", new Layout(Alignment.CENTER, Alignment.CENTER));
                        imageControl.text = "/assets/images/compass-small.png";
                        itemGroup.list.add(imageControl);
                        LinkControl linkControl = new LinkControl(COMPASS + " link", new Layout(Alignment.CENTER, Alignment.CENTER));
                        linkControl.text = "Return to Quest";
                        itemGroup.list.add(linkControl);
                        gridControl.cells.add(itemGroup);
                    } else {
                        emptyCellCount++;   // TODO - This is ugly
                        Group itemGroup = new VerticalGroup("EMPTY SCENE " + emptyCellCount, new Layout(Alignment.CENTER, Alignment.CENTER));
                        gridControl.cells.add(itemGroup);
                    }
                    continue;
                }
                
                String sceneName = sortedScenes[x][y];
                Group itemGroup = new VerticalGroup(sceneName, new Layout(Alignment.CENTER, Alignment.CENTER));
                List<String> observedActScenes = this.quest.observedScenes.get(this.quest.currentAct);
                if (sceneName == null) {
                    emptyCellCount++;   // TODO - This is ugly
                    sceneName = "EMPTY SCENE " + emptyCellCount;
                    System.out.println("SceneMap: render: Nothing to add to " + x + ", " + y);
                } else {
                    Scene scene = act.scenes.get(sceneName);
                    itemGroup.backgroundColor = scene.color;
                    LabelControl labelControl;
                    if ((observedActScenes.isEmpty()) || (!observedActScenes.contains(sceneName))) {
                        labelControl = new LabelControl(sceneName + " label", new Layout(Alignment.CENTER, Alignment.CENTER));
                        labelControl.text = "?";
                        System.out.println("SceneMap: render: Adding unobserved scene to " + x + ", " + y);
                    } else {
                        labelControl = new LabelControl(sceneName + " label", new Layout(Alignment.CENTER, Alignment.CENTER));
                        labelControl.text = sceneName;
                        System.out.println("SceneMap: render: Adding " + sceneName + " to " + x + ", " + y);
                    }
                    itemGroup.list.add(labelControl);
                }
                if ((this.quest.playerX != null) && (this.quest.playerX == x) && (this.quest.playerY != null) && (this.quest.playerY == y)) {
                    String playerSymbol = this.quest.playerSymbol;
                    switch (this.quest.getPlayerDirection().toUpperCase()) {
                        case Quest.DIRECTION_EAST -> playerSymbol += " " + "\u27A1";
                        case Quest.DIRECTION_NORTH -> playerSymbol += " " + "\u2B06";
                        case Quest.DIRECTION_SOUTH -> playerSymbol += " " + "\u2B07";
                        case Quest.DIRECTION_WEST -> playerSymbol += " " + "\u2B05";
                    }
                    LabelControl labelControl = new LabelControl("player", new Layout(Alignment.CENTER, Alignment.CENTER));
                    labelControl.text = playerSymbol;
                    itemGroup.list.add(labelControl);
                    System.out.println("SceneMap: render: Added " + playerSymbol + " to " + x + ", " + y);
                }
                if (sceneName != null) {
                    Scene scene = act.scenes.get(sceneName);
                    if ((scene != null) && (scene.symbol != null) && (observedActScenes.contains(sceneName))) {
                        LabelControl labelControl2 = new LabelControl(sceneName + " symbol", new Layout(Alignment.CENTER, Alignment.CENTER));
                        labelControl2.text = scene.symbol;
                        itemGroup.list.add(labelControl2);
                        System.out.println("SceneMap: render: Added " + scene.symbol + " to " + x + ", " + y);
                    }
                }
                gridControl.cells.add(itemGroup);
            }
        }
        
        this.appController.displayGrid(this.name, gridControl);
    }

}
