package quest.view;

import app.ApplicationController;
import app.Color;
import app.FontStyle;
import app.Utility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import quest.model.Act;
import quest.model.Book;
import quest.model.Page;
import quest.control.*;
import quest.model.InventoryItem;
import quest.model.Scene;
import quest.model.Story;

public class Quest extends app.ApplicationView {
    
    public final static String DIRECTION_EAST = "EAST";
    public final static String DIRECTION_NORTH = "NORTH";
    public final static String DIRECTION_SOUTH = "SOUTH";
    public final static String DIRECTION_WEST = "WEST";
    public final static String EDGE_OF_THE_WORLD = "EDGE OF THE WORLD";
    public static int FIRST_PAGE = 1;
    public final static int LEFT_PAGE = 1;
    public final static String LOADING_COMPLETE = "loading-complete";
    public final static String MAP = "Map";
    public final static String NEW_ACT = "new-act";
    public final static String NEW_INVENTORY_ITEM = "new-inventory-item";
    public final static String NEW_SCENE = "new-scene";
    public final static String NEXT_PAGE = "next-page";
    public final static String PLAYER_DIRECTION = "player-direction";
    public final static String PREVIOUS_PAGE = "previous-page";
    public final static int RIGHT_PAGE = 2;
    public static int SECOND_PAGE = 2;
    public final static String VARIABLE_EVENT_PREFIX = "VARIABLE";
    
    public ApplicationController appController;
    public Book book;
    public int buttonRow;
    public String currentAct;
    public int currentDisplayPage;
    public String currentPage;
    public String currentScene;
    public Map<String, InventoryItem> inventory;
    public Inventory inventoryView; // TODO - Make the inventoryView a listener of inventory change events
    public int leftPageStartingColumn;
    public int leftPageEndingColumn;
    public Boolean magicText;
    public SceneMap map;
    public Map<String, List<String>> observedScenes;
    private String playerDirection;
    public String playerSymbol;
    public Integer playerX;
    public Integer playerY;
    public Map<String, QuestControl> questControls;
    public int rightPageStartingColumn;
    public int rightPageEndingColumn;
    public int startingRow;
    public Color textColor;
    public int textRow;
    public int textStyle;
    public int titleRow;
    public Map<String, String> variables;

    public Quest(String name) {
        super(name);
        
        this.backgroundColor = new Color(0, 0, 0);
        this.playerSymbol = "\uD83E\uDDB0";
        this.magicText = false;
        this.inventory = new LinkedHashMap<>();
        this.variables = new HashMap<>();
        this.observedScenes = new HashMap();
        this.questControls = new HashMap<>();
        this.questControls.put(BookAuthorControl.NAME, new BookAuthorControl(this));
        this.questControls.put(BookLastUpdatedDateControl.NAME, new BookLastUpdatedDateControl(this));
        this.questControls.put(BookTitleControl.NAME, new BookTitleControl(this));
        this.questControls.put(BreakControl.NAME, new BreakControl(this));
        this.questControls.put(DoubleQuoteControl.NAME, new DoubleQuoteControl(this));
        this.questControls.put(SetFocusOnFirstPageControl.NAME, new SetFocusOnFirstPageControl(this));
        this.questControls.put(SetFocusOnSecondPageControl.NAME, new SetFocusOnSecondPageControl(this));
        this.questControls.put(SetTextColorControl.NAME, new SetTextColorControl(this));
        this.questControls.put(GetInputControl.NAME, new GetInputControl(this));
        this.questControls.put(VariableControl.NAME, new VariableControl(this));
        this.questControls.put(ActGotoControl.NAME, new ActGotoControl(this));
        this.questControls.put(SceneGotoControl.NAME, new SceneGotoControl(this));
        this.questControls.put(VariableSetControl.NAME, new VariableSetControl(this));
        this.questControls.put(ImageControl.NAME, new ImageControl(this));
        this.questControls.put(PageRefreshControl.NAME, new PageRefreshControl(this));
        this.questControls.put(SubpageDisplayControl.NAME, new SubpageDisplayControl(this));
        this.questControls.put(SoundPlayControl.NAME, new SoundPlayControl(this));
        this.questControls.put(SoundStopControl.NAME, new SoundStopControl(this));
        this.questControls.put(SetMagicTextControl.NAME, new SetMagicTextControl(this));
        this.questControls.put(PageGotoControl.NAME, new PageGotoControl(this));
        this.questControls.put(BoldTextControl.NAME, new BoldTextControl(this));
        this.questControls.put(BoldTextOffControl.NAME, new BoldTextOffControl(this));
        this.questControls.put(ColorTextControl.NAME, new ColorTextControl(this));
        this.questControls.put(ColorTextOffControl.NAME, new ColorTextOffControl(this));
        this.questControls.put(SceneControl.NAME, new SceneControl(this));
        this.questControls.put(NextSceneControl.NAME, new NextSceneControl(this));
        this.questControls.put(UnderlineTextControl.NAME, new UnderlineTextControl(this));
        this.questControls.put(UnderlineTextOffControl.NAME, new UnderlineTextOffControl(this));
        this.questControls.put(PlayerDirectionControl.NAME, new PlayerDirectionControl(this));
        this.questControls.put(SetPlayerDirectionControl.NAME, new SetPlayerDirectionControl(this));
        this.questControls.put(SetCursorButtonRowControl.NAME, new SetCursorButtonRowControl(this));
        this.questControls.put(TurnLeftControl.NAME, new TurnLeftControl(this));
        this.questControls.put(TurnRightControl.NAME, new TurnRightControl(this));
        this.questControls.put(InventoryControl.NAME, new InventoryControl(this));
        this.questControls.put(InventoryAddControl.NAME, new InventoryAddControl(this));
        this.questControls.put(BookFlipControl.NAME, new BookFlipControl(this));
        this.questControls.put(AddViewControl.NAME, new AddViewControl(this));
        this.questControls.put(PlayerSymbolSetControl.NAME, new PlayerSymbolSetControl(this));
        this.questControls.put(ObservedSceneAddControl.NAME, new ObservedSceneAddControl(this));
        this.questControls.put(TabSelectControl.NAME, new TabSelectControl(this));
        this.questControls.put(MoveAheadControl.NAME, new MoveAheadControl(this));
        this.textColor = new Color(0, 0, 0);
        this.textStyle = FontStyle.NORMAL;
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("Quest: onEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        switch(eventName) {
            case LOADING_COMPLETE -> {
                this.startAct(this.book.firstActName);
                this.display();
            }
            case NEXT_PAGE -> {
                Act act = book.acts.get(this.currentAct);
                Scene scene = act.scenes.get(this.currentScene);
                Page page = scene.pages.get(this.currentPage);
                if (page.nextPageName != null) {
                    // Advance to the next page
                    this.currentPage = page.nextPageName;
                } else {
                    if (scene.nextSceneName != null) {
                        // Advance to the next scene
                        this.startScene(scene.nextSceneName, false, false);
                    } else {
                        // Advance to the next act
                        this.startAct(act.nextActName);
                    }
                }
                this.display();
            }
            case PREVIOUS_PAGE -> {
                Act act = book.acts.get(this.currentAct);
                Scene scene = act.scenes.get(this.currentScene);
                Page page = scene.pages.get(this.currentPage);
                this.currentPage = page.previousPageName;
                this.display();
            }
            default -> {
                String[] eventNameParts = eventName.split(":");
                if (eventNameParts[0].equals(VARIABLE_EVENT_PREFIX)) {
                    String key = eventNameParts[1];
                    this.variables.put(key, (String)eventValue);
                    String subpageName = "INPUT " + key + "=" + eventValue;
                    Story subpage = getSubpage(subpageName, false);
                    if (subpage != null) {
                        this.displayPage(subpage.contents, true);
                    }
                } else {
                    System.err.println("Quest: onEvent: Unsupported event");
                }
            }
        }
    }
    
    @Override
    public void onLoad(ApplicationController appController) {
        System.out.println("Quest: onLoad");
        this.appController = appController;
        
        // Loading screen
        int halfColumns = (appController.getTextColumns() / 2);
        int halfGifWidth = (appController.getColumns(this.book.animationFileName) / 2);
        int gifColumn = halfColumns - halfGifWidth;
        int nextRow = appController.displayGif(this.name, this.book.animationFileName, 3, gifColumn);
        int halfTextWidth = ("Loading...".length() / 2);
        int loadingTextColumn = halfColumns - halfTextWidth;
        appController.displayText(this.name, "Loading...", nextRow, loadingTextColumn, new Color(255, 255, 255));
        Act firstAct = book.acts.get(this.book.firstActName);
        Scene firstScene = firstAct.scenes.get(firstAct.firstSceneName);
        app.Utility.playSound(firstScene.soundFileName, true);
        appController.setTimer(LOADING_COMPLETE, 3, this);
        
        // Calculate book margins
        int parentColumns = this.appController.getTextColumns();
        int parentRows = this.appController.getTextRows();
        this.titleRow = 2;
        this.startingRow = 4;
        this.leftPageStartingColumn = (int) (parentColumns * 0.1) + 1;
        this.leftPageEndingColumn = (int) (parentColumns * 0.49) + 1;
        this.rightPageStartingColumn = (int) (parentColumns * 0.52) + 1;
        this.rightPageEndingColumn = (int) (parentColumns * 0.92) + 1;
        int buttonRows = appController.getButtonRows();
        this.buttonRow = parentRows - buttonRows;
    }
    
    public void addInventoryItem(String inventoryItemName) {
        InventoryItem item;
        if (this.inventory.containsKey(inventoryItemName)) {
            item = this.inventory.get(inventoryItemName);
            item.quantity++;
            System.out.println("InventoryAddControl: onExecute: Item increased by 1: " + item.quantity);
        } else {
            item = this.book.inventory.get(inventoryItemName);
            item.quantity = 1;
            this.inventory.put(inventoryItemName, item);
            System.out.println("InventoryAddControl: onExecute: Item added to inventory");
        }

        // Play the item's sound if applicable
        if (item.soundFileName != null) {
            System.out.println("InventoryAddControl: onExecute: Playing inventory item's sound: " + item.soundFileName);
            Utility.playSound(item.soundFileName, false);
        }
        
        // Handle any custom add event logic for the item
        if (item.onAdd != null) {
            this.displayPage(item.onAdd.contents, true);
        }
        
        this.publishEvent(NEW_INVENTORY_ITEM, inventoryItemName);
    }
    
    public void display() {
        System.out.println("Quest: display: act=" + this.currentAct + ", page=" + this.currentPage);
        
        // Dispose all controls on the book's composite
        this.appController.clearScreen(this.name);
        
        // Display the book image
        this.backgroundImage = "/assets/images/book.png";
        this.appController.setBackgroundImage(this.name, this.backgroundImage);
        
        Act act = this.book.acts.get(this.currentAct);
        Scene scene = act.scenes.get(this.currentScene);
        
        // Play the page turn sound
        app.Utility.playSound("/assets/sounds/turn-page.mp3", false);
        
        // Display the book title and act
        if (!scene.hidePageHeaders) {
            Color black = new Color(0, 0, 0);
            if (FIRST_PAGE == LEFT_PAGE) {
                this.appController.displayText(this.name, this.book.title, this.titleRow, this.leftPageStartingColumn, black, FontStyle.BOLD);
                this.appController.displayText(this.name, this.currentAct, this.titleRow, this.rightPageEndingColumn - this.currentAct.length() + 1, black, FontStyle.BOLD);
            } else {
                this.appController.displayText(this.name, this.currentAct, this.titleRow, this.leftPageStartingColumn, black, FontStyle.BOLD);
                this.appController.displayText(this.name, this.book.title, this.titleRow, this.rightPageEndingColumn - this.book.title.length() + 1, black, FontStyle.BOLD);
            }

        }

        // Display the current page's contents
        Page page = scene.pages.get(this.currentPage);
        List<String> pageContents = page.story.contents;
        displayPage(pageContents, false);
        
        // Previous page button
        if ((!page.hidePreviousButton) && (page.previousPageName != null)) {
            String buttonText = "< Previous";
            int buttonColumn = this.leftPageStartingColumn;
            this.appController.displayButton(this.name, PREVIOUS_PAGE, buttonText, this.buttonRow, buttonColumn, this);
        }
        
        // Next page button
        if ((!page.hideNextButton) && ((act.nextActName != null) || (scene.nextSceneName != null) || (page.nextPageName != null))) {
            String buttonText = "Next >";
            int buttonColumns = appController.getButtonColumns(buttonText);
            int buttonColumn = this.rightPageEndingColumn - buttonColumns + 1;
            this.appController.displayButton(this.name, NEXT_PAGE, buttonText, this.buttonRow, buttonColumn, this);
        }
    }
    
    public void displayTextOnPage(String text, Integer row, Integer column, Color color, int style) {
        int startingColumn;
        int endingColumn;
        if (this.currentDisplayPage == RIGHT_PAGE) {
            startingColumn = this.rightPageStartingColumn;
            endingColumn = this.rightPageEndingColumn;
        } else {
            startingColumn = this.leftPageStartingColumn;
            endingColumn = this.leftPageEndingColumn;
        }
        
        int realRow = this.titleRow + 1 + row;
        int realColumn = startingColumn + column - 1;
        int rowWidth = endingColumn - startingColumn;
        String remainingText = text;
        while (remainingText.length() > 0) {
            String lineText;
            if (remainingText.length() <= (rowWidth + 1)) {
                lineText = remainingText;
                remainingText = "";
            } else {
                lineText = remainingText.substring(0, rowWidth + 1);
                int lastSpaceIndex = lineText.lastIndexOf(' ');
                if (lastSpaceIndex != -1) {
                    lineText = lineText.substring(0, lastSpaceIndex);
                    remainingText = remainingText.substring(lineText.length(), remainingText.length());
                } else {
                    lineText = lineText + "-";
                    remainingText = remainingText.substring(rowWidth, remainingText.length());
                }
            }
            if ((!this.magicText) || (color.red != 0) || (color.green != 0) || (color.blue != 0)) {
                this.appController.displayText(this.name, lineText, realRow, realColumn, color, style);
            } else {
                // When magic words are turned on and the font color is black, color each word individually
                int magicColumn = realColumn;
                Random random = new Random();
                String[] magicWords = lineText.split(" ");
                for (String magicWord : magicWords) {
                    // Randomly pick a number from the bottom 3/4 of the 256 RGB scale.
                    // (Bottom half means darker and easier to read on the light book pages.)
                    int red = random.nextInt(192);
                    int green = random.nextInt(192);
                    int blue = random.nextInt(192);
                    Color randomColor = new Color(red, green, blue);
                    this.appController.displayText(this.name, magicWord + " ", realRow, magicColumn, randomColor, style);
                    magicColumn += magicWord.length() + 1;
                }
                this.appController.displayText(this.name, lineText, realRow, realColumn, color, style);
            }
            remainingText = remainingText.trim();
            realRow++;
            realColumn = startingColumn;
            this.textRow = this.textRow + 1;
        }
    }
    
    // TODO - Skip [[VARIABLE player=GREYSON]] sections... these should really be a sub-page data structure in Act mapped with the same page number and executed by getting input?
    public void displayPage(List<String> page, Boolean isSubpage) {
        System.out.println("Quest: displayPage");
        
        if (!isSubpage) {
            this.currentDisplayPage = FIRST_PAGE;
            this.textRow = 1;
            this.textColor = new Color(0, 0, 0);
            this.textStyle = FontStyle.NORMAL;
        }
        
        Color currentTextColor = this.textColor;
        int currentTextStyle = this.textStyle;
        
        for (String pageLine : page) {
            String storyText = "";
            for (int i = 0; i < pageLine.length(); i++) {
                char character = pageLine.charAt(i);
                String newText = "" + character;
                
                if (character == '<') {
                    String questControlName = null;
                    String questControlTag = QuestControl.getTag(pageLine, i);
                    if (questControlTag != null) {
                        questControlName = QuestControl.getTagName(questControlTag, 0);
                    }
                    if (questControlName != null) {
                        QuestControl control = this.questControls.get(questControlName);
                        if (control == null) {
                            // Unsupported tags are supported and should not throw an exception
                            System.out.println("Quest: displayPage: Unsupported tag: ***" + questControlName + "***");
                        } else {
                            System.out.println("Quest: displayPage: Executing tag " + questControlName);
                            newText = control.onExecute(questControlTag);
                            i = i + questControlTag.length() - 1;
                        }
                    }
                }
                storyText = storyText + newText;
                
                if ((currentTextStyle != this.textStyle) || (!currentTextColor.equals(this.textColor))) {
                    // Display the previous text styling for the text accumulated thus far
                    System.out.println("Quest: displayPage: Displaying current story text: " + storyText);
                    // TODO - The text column should NOT be 1
                    this.displayTextOnPage(storyText, this.textRow, 1, currentTextColor, currentTextStyle);
                    storyText = "";
                    currentTextColor = this.textColor;
                    currentTextStyle = this.textStyle;
                }
            }
            
            if (storyText.length() > 0) {
                this.displayTextOnPage(storyText, this.textRow, 1, this.textColor, this.textStyle);
            } else if (pageLine.length() == 0) {
                this.textRow = this.textRow + 1;
            }
        }
    }
    
    public void flipBook() {
        System.out.println("Quest: flipBook");
        if (FIRST_PAGE == LEFT_PAGE) {
            FIRST_PAGE = RIGHT_PAGE;
            SECOND_PAGE = LEFT_PAGE;
        } else {
            FIRST_PAGE = LEFT_PAGE;
            SECOND_PAGE = RIGHT_PAGE;
        }
    }
    
    public String getNextScene() {
        System.out.println("Quest: getNextScene");
        
        Act currentAct = this.book.acts.get(this.currentAct);
        Scene currentScene = currentAct.scenes.get(this.currentScene);
        
        if ((currentScene.x == null) || (currentScene.y == null)) {
            return Quest.EDGE_OF_THE_WORLD;
        }
        
        int x = currentScene.x;
        int y = currentScene.y;
        switch (this.getPlayerDirection().toUpperCase()) {
            case Quest.DIRECTION_EAST -> x += 1;
            case Quest.DIRECTION_NORTH -> y += -1;
            case Quest.DIRECTION_SOUTH -> y += 1;
            case Quest.DIRECTION_WEST -> x += -1;
        }
        
        String nextSceneName = Quest.EDGE_OF_THE_WORLD;
        for (String sceneName : currentAct.scenes.keySet()) {
            Scene scene = currentAct.scenes.get(sceneName);
            if ((scene.x == null) || (scene.y == null)) {
                continue;
            }
            if ((scene.x == x) && (scene.y == y)) {
                nextSceneName = sceneName;
                break;
            }
        }
        
        return nextSceneName;
    }
    
    public String getPlayerDirection() {
        return this.playerDirection;
    }
    
    public Story getSubpage(String name, Boolean cheatOnly) {
        System.out.println("Quest: getSubpage: name=" + name + ", cheatOnly=" + cheatOnly);
        
        Act act = this.book.acts.get(this.currentAct);
        Scene scene = act.scenes.get(this.currentScene);
        Page page = scene.pages.get(this.currentPage);
        
        Story story;
        if (page.subpages.containsKey(name)) {
            story = page.subpages.get(name);
            if ((!cheatOnly) || (story.isCheat)) {
                System.out.println("Quest: getSubpage: Found subpage at page level");
                return story;
            }
        }
        if (scene.subpages.containsKey(name)) {
            story = scene.subpages.get(name);
            if ((!cheatOnly) || (story.isCheat)) {
                System.out.println("Quest: getSubpage: Found subpage at scene level");
                return story;
            }
        }
        if (act.subpages.containsKey(name)) {
            story = act.subpages.get(name);
            if ((!cheatOnly) || (story.isCheat)) {
                System.out.println("Quest: getSubpage: Found subpage at act level");
                return story;
            }
        }
        if (this.book.subpages.containsKey(name)) {
            story = this.book.subpages.get(name);
            if ((!cheatOnly) || (story.isCheat)) {
                System.out.println("Quest: getSubpage: Found subpage at book level");
                return story;
            }
        }
        
        System.out.println("Quest: getSubpage: Did NOT find the subpage");
        
        return null;
    }
    
    public void setPlayerDirection(String direction) {
        this.playerDirection = direction;

        // Update the collection of observed scenes
        this.updateObservedScenes();

        this.publishEvent(PLAYER_DIRECTION, this.playerDirection);        
    }
    
    public void startAct(String actName) {
        System.out.println("Quest: startAct: act=" + actName);
        
        Boolean isFirstAct = (this.currentAct == null);
        if (!isFirstAct) {
            System.out.println("Quest: startAct: Stopping any sounds from previous act");
            app.Utility.stopAllSounds();
        }
        this.currentAct = actName;
        
        this.publishEvent(NEW_ACT, actName);
        
        Act act = book.acts.get(this.currentAct);
        this.startScene(act.firstSceneName, true, isFirstAct);
    }
    
    public void startScene(String sceneName, Boolean isNewAct, Boolean isFirstAct) {
        System.out.println("Quest: startScene: scene=" + sceneName);
        
        Act act = book.acts.get(this.currentAct);
        Scene scene = act.scenes.get(sceneName);
        
        // Stop the current sound file if needed
        String previousSoundFileName = null;
        if (isNewAct == false) {
            Scene previousScene = act.scenes.get(this.currentScene);
            previousSoundFileName = previousScene.soundFileName;
        }
        if ((previousSoundFileName != null) && (!scene.soundFileName.equals(previousSoundFileName))) {
            System.out.println("Quest: startScene: Stopping sound file " + previousSoundFileName);
            app.Utility.stopAllSounds();
        }
        
        // Start a new sound file if needed
        if ((!isFirstAct) && (scene.soundFileName != null) && (!scene.soundFileName.equals(previousSoundFileName))) {
            System.out.println("Quest: startScene: Playing sound file " + scene.soundFileName);
            app.Utility.playSound(scene.soundFileName, true);
        }
        
        // Track where we are in the book
        this.currentScene = sceneName;
        this.currentDisplayPage = FIRST_PAGE;
        this.currentPage = scene.firstPageName;  
        
        // Move the player to the scene's coordinates (if any)
        this.playerX = scene.x;
        this.playerY = scene.y;
        
        // Update the collection of observed scenes
        this.updateObservedScenes();
        
        this.publishEvent(NEW_SCENE, sceneName);
    }
    
    public void updateObservedScenes() {
        if (!this.observedScenes.containsKey(this.currentAct)) {
            this.observedScenes.put(this.currentAct, new ArrayList<>());
        }
        if (!this.observedScenes.get(this.currentAct).contains(this.currentScene)) {
            this.observedScenes.get(this.currentAct).add(this.currentScene);
        }
        String nextSceneName = this.getNextScene();
        if (!this.observedScenes.get(this.currentAct).contains(nextSceneName)) {
            this.observedScenes.get(this.currentAct).add(nextSceneName);
        }
    }
    
}
