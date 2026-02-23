package quest.view;

import quest.control_deprecated.BaseQuestControl;
import app.controller.BaseController;
import app.Color;
import app.Font;
import app.FontStyle;
import app.HorizontalAlignment;
import app.Layout;
import app.RelativeCoordinates;
import app.VerticalAlignment;
import static app.controller.BaseController.DEFAULT_PIXEL_SIZE;
import static app.controller.BaseController.logger;
import app.node.BaseNode;
import app.node.Button;
import app.node.Image;
import app.node.Label;
import app.node.Pane;
import app.node.Rectangle;
import app.node.ScrollingDocument;
import app.node.effect.Glow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import quest.model.Act;
import quest.model.Book;
import quest.model.InventoryItem;
import quest.model.Page;
import quest.model.Scene;
import quest.model.Story;

public class Quest extends app.view.BaseView {
    
    public enum Area {STORY, ILLUSTRATION};
    
    public static Quest quest; // TODO - Make this a session-specific value
    
    public final static Double DEFAULT_FONT_SIZE = 14.0;
    public final static String DIRECTION_EAST = "EAST";
    public final static String DIRECTION_NORTH = "NORTH";
    public final static String DIRECTION_SOUTH = "SOUTH";
    public final static String DIRECTION_WEST = "WEST";
    public final static String EDGE_OF_THE_WORLD = "EDGE OF THE WORLD";
    public static int FIRST_PAGE = 1;
    public final static String GAME_OVER = "game-over";
    public final static String HP_CHANGE = "hp-change";
    public final static String HP_CHANGE_REFRESH = "hp-change-refresh";
    public final static int LEFT_PAGE = 1;
    public final static Double LEFT_PAGE_STARTING_X = 0.09;
    public final static Double PAGE_STARTING_Y = 0.025;
    public final static Double PAGE_ENDING_Y = 0.93;
    public final static Double RIGHT_PAGE_ENDING_X = 0.922;
    public final static String LINK_EVENT_PREFIX = "LINK";
    public final static String LOADING_COMPLETE = "loading-complete";
    public final static String LOADING_OVERLAY = "loading-overlay";
    public final static String MAP = "Map";
    public final static String MP_CHANGE = "mp-change";
    public final static String MP_CHANGE_REFRESH = "mp-change-refresh";
    public final static String NEW_ACT = "new-act";
    public final static String NEW_INVENTORY_ITEM = "new-inventory-item";
    public final static String NEW_SCENE = "new-scene";
    public final static String NEXT_PAGE = "next-page";
    public final static String PLAYER_DIRECTION = "player-direction";
    public final static String PREVIOUS_PAGE = "previous-page";
    public final static int RIGHT_PAGE = 2;
    public static int SECOND_PAGE = 2;
    public final static String SPELL_BOOK = "Spell Book";
    public final static String TIMER_EVENT_PREFIX = "TIMER";
    public final static String VARIABLE_EVENT_PREFIX = "VARIABLE";
    public final static String XP_CHANGE = "xp-change";
    public final static String XP_CHANGE_REFRESH = "xp-change-refresh";
    
    public BaseController appController;
    public Book book;
    public String currentAct;
    public int currentDisplayPage;
    public String currentPage;
    public String currentScene;
    public Pane illustrationContainer;
    public Boolean isGameOver;
    public Map<String, InventoryItem> inventory;
    public Inventory inventoryView; // TODO - Make the inventoryView a listener of inventory change events
    public String lastEnemyThatAttacked;
    public SceneMap map;
    public int nodeIndex = 0;
    public Map<String, List<String>> observedScenes;
    private String playerDirection;
    private int playerHP;
    private int playerMP;
    private int playerXP;
    public String playerSymbol;
    public Integer playerX;
    public Integer playerY;
    public Random random = new Random();
    public SpellBook spellBook;
    public ScrollingDocument storyDocument;
    public Map<String, String> variables;

    public Quest(String name) {
        super(name);
        
        this.backgroundImage = "/assets/images/book.png";
        this.backgroundColor = new Color(255, 255, 255);
        this.emojis.add("\uD83D\uDCD6"); // "open book" Unicode emoji
        this.inventory = new LinkedHashMap<>();
        this.isGameOver = false;
        this.observedScenes = new HashMap();
        this.playerHP = 100;
        this.playerMP = 0;
        this.playerXP = 0;
        this.playerSymbol = "\uD83E\uDDD1\u200D\uD83E\uDDB0";
        this.variables = new HashMap<>();
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("Quest: onEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        switch(eventName) {
            case HP_CHANGE -> this.appController.removeNode(this.name, HP_CHANGE);
            case HP_CHANGE_REFRESH -> {
                this.appController.removeNode(this.name, HP_CHANGE_REFRESH);
                this.display();
            }
            case MP_CHANGE -> this.appController.removeNode(this.name, MP_CHANGE);
            case MP_CHANGE_REFRESH -> {
                this.appController.removeNode(this.name, MP_CHANGE_REFRESH);
                this.display();
            }
            case XP_CHANGE -> this.appController.removeNode(this.name, XP_CHANGE);
            case XP_CHANGE_REFRESH -> {
                this.appController.removeNode(this.name, XP_CHANGE_REFRESH);
                this.display();
            }
            case LOADING_COMPLETE -> {
                this.appController.removeNode(this.name, LOADING_OVERLAY);
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
                if (page.previousPageName != null) {
                    this.currentPage = page.previousPageName;
                    this.display();
                } else if ((scene.firstPageName.equals(this.currentPage)) && (scene.previousSceneName != null)) {
                    this.startScene(scene.previousSceneName, false, false);
                    this.display();
                } else if ((scene.firstPageName.equals(this.currentPage)) && (act.firstSceneName.equals(this.currentScene)) && (act.previousActName != null)) {
                    this.startAct(act.previousActName);
                    this.display();
                }
            }
            case GAME_OVER -> {
                // TODO - Make this a configurable Story that can be added to any level of the Book
                this.isGameOver = true;
                Act act = book.acts.get(this.currentAct);
                Scene scene = act.scenes.get(this.currentScene);
                Page deathPage = new Page();
                deathPage.hideNextButton = true;
                deathPage.hidePreviousButton = true;
                deathPage.story.contents.add("Alas!  You were defeated by <b>" + this.lastEnemyThatAttacked + "</b>.  Here endeth your quest.");
                deathPage.story.contents.add("<br>");
                deathPage.story.contents.add("<get-validated-input action *Check High Scores>");
                deathPage.story.contents.add("<second-page>");
                deathPage.story.contents.add("<image center /assets/images/skull.png>");
                scene.pages.put("DEATH PAGE", deathPage);
                Story checkHighScoresSubpage = new Story();
                checkHighScoresSubpage.contents.add("<tab-select " + Questcraft.HIGH_SCORES + ">");
                deathPage.subpages.put("INPUT action=Check High Scores", checkHighScoresSubpage);
                this.currentPage = "DEATH PAGE";
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
                        this.displayStory(subpage, true);
                    } else {
                        subpageName = "INPUT " + key;
                        subpage = getSubpage(subpageName, false);
                        if (subpage != null) {
                            this.displayStory(subpage, true);
                        }
                    }
                } else if (eventNameParts[0].equals(TIMER_EVENT_PREFIX)) {
                    String key = eventNameParts[1];
                    String subpageName = "TIMER " + key;
                    Story subpage = getSubpage(subpageName, false);
                    if (subpage != null) {
                        this.displayStory(subpage, true);
                    }
                } else if (eventNameParts[0].equals(LINK_EVENT_PREFIX)) {
                    String key = eventNameParts[1];
                    String subpageName = "LINK " + key;
                    Story subpage = getSubpage(subpageName, false);
                    if (subpage != null) {
                        this.displayStory(subpage, true);
                    }
                } else {
                    System.err.println("Quest: onEvent: Unsupported event");
                }
            }
        }
    }
    
    @Override
    public void onLoad(BaseController appController) {
        logger.log(Level.INFO, "Entered: appController={0}", appController);
        
        this.appController = appController;
        
        // Loading screen
        if (this.book.animationFileName != null) {
            Rectangle overlay = new Rectangle("overlay");
            overlay.color = Color.BLACK;
            overlay.opacity = 0.5;
            overlay.scaleX = 1.0;
            overlay.scaleY = 1.0;
            this.appController.addNode(this.name, this.name, overlay, new Layout(new RelativeCoordinates(0.0, 0.0), HorizontalAlignment.CENTER, VerticalAlignment.CENTER));
            
            Image loadingImage = new Image("loading");
            loadingImage.file = this.book.animationFileName;
            this.appController.addNode(this.name, this.name, loadingImage, new Layout(new RelativeCoordinates(0.0, 0.25), HorizontalAlignment.CENTER, VerticalAlignment.TOP));
            
            Act firstAct = book.acts.get(this.book.firstActName);
            Scene firstScene = firstAct.scenes.get(firstAct.firstSceneName);
            if (!firstScene.soundFileName.equals("")) {
                this.appController.playSound(firstScene.soundFileName, true);
            }
            appController.setTimer(LOADING_COMPLETE, 3, this);
            if (this.book.preloadEmojisDuringAnimation) {
                this.appController.loadEmojiData();
            }
        }
        
        // Start book (if not waiting for the animation to complete and the LOADING_COMPLETE event to be raised)
        if (this.book.animationFileName == null) {
            this.startAct(this.book.firstActName);
            this.display();
        }
    }
    
    public void removeInventoryItem(String inventoryItemName) {
        System.out.println("removeInventoryItem: inventoryItemName=" + inventoryItemName);
        
        if (!this.inventory.containsKey(inventoryItemName)) {
            System.out.println("removeInventoryItem: Item wasn't in inventory");
            return;
        }
        
        InventoryItem item = this.inventory.get(inventoryItemName);
        item.quantity--;
        System.out.println("removeInventoryItem: Item decreased by 1: " + item.quantity);
        
        if (item.quantity == 0) {
            this.inventory.remove(inventoryItemName);
        }

        // Play the item's sound if applicable
        if (item.soundFileName != null) {
            System.out.println("removeInventoryItem: Playing inventory item's sound: " + item.soundFileName);
            this.appController.playSound(item.soundFileName, false);
        }
        
        this.publishEvent(NEW_INVENTORY_ITEM, inventoryItemName);
    }
    
    public void addInventoryItem(String inventoryItemName) {
        System.out.println("addInventoryItem: inventoryItemName=" + inventoryItemName);
        
        InventoryItem item;
        if (this.inventory.containsKey(inventoryItemName)) {
            item = this.inventory.get(inventoryItemName);
            item.isNew = true;
            item.quantity++;
            System.out.println("addInventoryItem: Item increased by 1: " + item.quantity);
        } else {
            item = this.book.inventory.get(inventoryItemName);
            item.isNew = true;
            item.quantity = 1;
            this.inventory.put(inventoryItemName, item);
            System.out.println("addInventoryItem: Item added to inventory");
        }

        // Play the item's sound if applicable
        if (item.soundFileName != null) {
            System.out.println("addInventoryItem: Playing inventory item's sound: " + item.soundFileName);
            this.appController.playSound(item.soundFileName, false);
        }
        
        // Give the player experience for aquiring the item
        this.playerXP += item.xp;
        
        // Handle any custom add event logic for the item
        if (item.onAdd != null) {
            this.displayStory(item.onAdd, true);
        }
        
        this.publishEvent(NEW_INVENTORY_ITEM, inventoryItemName);
    }
    
    public void display() {
        logger.log(Level.INFO, "Entered");
        
        // Dispose all controls on the book's composite
        this.appController.clearScreen(this.name);
        
        Act act = this.book.acts.get(this.currentAct);
        Scene scene = act.scenes.get(this.currentScene);
        
        // Play the page turn sound
        this.appController.playSound("/assets/sounds/turn-page.mp3", false);
        
        // Display the book title and act
        if (!scene.hidePageHeaders) {
            Double titleX;
            Double titleY = PAGE_STARTING_Y;
            HorizontalAlignment titleHorizontalAlignment;
            Double chapterX;
            Double chapterY = PAGE_STARTING_Y;
            HorizontalAlignment chapterHorizontalAlignment;
            if (FIRST_PAGE == LEFT_PAGE) {
                titleX = LEFT_PAGE_STARTING_X;
                titleHorizontalAlignment = HorizontalAlignment.LEFT;
                chapterX = RIGHT_PAGE_ENDING_X;
                chapterHorizontalAlignment = HorizontalAlignment.RIGHT;
            } else {
                titleX = RIGHT_PAGE_ENDING_X;
                titleHorizontalAlignment = HorizontalAlignment.RIGHT;
                chapterX = LEFT_PAGE_STARTING_X;
                chapterHorizontalAlignment = HorizontalAlignment.LEFT;
            }
            Label titleLabel = new Label("title");        
            titleLabel.text = this.book.title;
            titleLabel.pixelSize = DEFAULT_PIXEL_SIZE;
            titleLabel.textColor = Color.BLACK;
            titleLabel.textFont = Font.ROBOTO_MONO;
            titleLabel.textStyle = FontStyle.BOLD;
            this.appController.addNode(this.name, this.name, titleLabel, new Layout(new RelativeCoordinates(titleX, titleY), titleHorizontalAlignment, VerticalAlignment.TOP));

            Label chapterLabel = new Label("chapter");        
            chapterLabel.text = this.currentAct;
            chapterLabel.pixelSize = DEFAULT_PIXEL_SIZE;
            chapterLabel.textColor = Color.BLACK;
            chapterLabel.textFont = Font.ROBOTO_MONO;
            chapterLabel.textStyle = FontStyle.BOLD;
            this.appController.addNode(this.name, this.name, chapterLabel, new Layout(new RelativeCoordinates(chapterX, chapterY), chapterHorizontalAlignment, VerticalAlignment.TOP));
        }
        
        this.displayStoryContainer();
        this.displayIllustrationContainer();
        
        // So each story node can have a unique name, initialize an index
        this.nodeIndex = 0;

        // Display the current page's contents
        Page page = scene.pages.get(this.currentPage);
        List<BaseQuestControl> pageControls;
        Story pageStory;
        if ((page == null) || (page.story == null)) {
            this.displayStory(new Story(), false);
            return;
        }
        pageStory = page.story;
        displayStory(pageStory, false);
        
        // Next page button
        Boolean isNextPageDisplaying = false;
        if ((this.playerHP != 0) && (!page.hideNextButton) && ((act.nextActName != null) || (scene.nextSceneName != null) || (page.nextPageName != null))) {
            //this.appController.displayButton(this.name, NEXT_PAGE, buttonText, this.buttonRow, buttonColumn, null, null, false, null, !page.noGlow, this);
            Button nextButton = new Button(NEXT_PAGE);
            nextButton.eventListener = this;
            nextButton.pixelSize = DEFAULT_PIXEL_SIZE;
            nextButton.textColor = Color.BLACK;
            nextButton.text = "Next \uD83E\uDC62";
            //nextButton.textFont = Font.ROBOTO_BLACK;
            if (!page.noGlow) {
                nextButton.effects.add(new Glow(Color.DARK_MAGENTA));
            }
            this.appController.addNode(this.name, this.name, nextButton, new Layout(new RelativeCoordinates(RIGHT_PAGE_ENDING_X, PAGE_ENDING_Y), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM));
            
            isNextPageDisplaying = true;
        }

        // Previous page button (return to previous page, scene, or act)
        if ((this.playerHP != 0) && (!page.hidePreviousButton) && ((page.previousPageName != null) || ((scene.firstPageName.equals(this.currentPage)) && (scene.previousSceneName != null))  || ((scene.firstPageName.equals(this.currentPage)) && (act.firstSceneName.equals(this.currentScene)) && (act.previousActName != null)))) {
            //this.appController.displayButton(this.name, PREVIOUS_PAGE, buttonText, this.buttonRow, buttonColumn, null, null, false, null, glow, this);
            Button previousButton = new Button(PREVIOUS_PAGE);
            previousButton.eventListener = this;
            previousButton.pixelSize = DEFAULT_PIXEL_SIZE;
            previousButton.textColor = Color.BLACK;
            previousButton.text = "\uD83E\uDC60 Previous";
            //previousButton.textFont = Font.ROBOTO_BLACK;
            if ((!isNextPageDisplaying) && (!page.noGlow)) {
                previousButton.effects.add(new Glow(Color.DARK_MAGENTA));
            }
            this.appController.addNode(this.name, this.name, previousButton, new Layout(new RelativeCoordinates(LEFT_PAGE_STARTING_X, PAGE_ENDING_Y), HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM));
        }
        
        // Game Over button
        if ((!this.isGameOver) && (this.playerHP == 0)) {
            //this.appController.displayButton(this.name, GAME_OVER, buttonText, this.buttonRow, buttonColumn, null, null, false, null, true, this);
            Button gameOverButton = new Button(GAME_OVER);
            gameOverButton.eventListener = this;
            gameOverButton.pixelSize = DEFAULT_PIXEL_SIZE;
            gameOverButton.textColor = Color.BLACK;
            gameOverButton.text = "Game Over >";
            //gameOverButton.textFont = Font.ROBOTO_BLACK;
            gameOverButton.effects.add(new Glow(Color.DARK_MAGENTA));
            this.appController.addNode(this.name, this.name, gameOverButton, new Layout(new RelativeCoordinates(RIGHT_PAGE_ENDING_X, PAGE_ENDING_Y), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM));
        }
    }
    
    public void displayStory(Story story, Boolean isSubpage) {
        logger.log(Level.INFO, "Entered: page={0}, isSubpage={1}", new Object[]{story, isSubpage});
        
        if (story.condition != null) {
            if (!story.condition.evaluate()) {
                logger.log(Level.INFO, "Condition for story not met!");
                return;
            }
        }
        
        if (!isSubpage) {
            logger.log(Level.INFO, "Resetting page");
            this.currentDisplayPage = FIRST_PAGE;
            this.displayStoryContainer();
            this.displayIllustrationContainer();
        }
        
        for (BaseQuestControl control : story.controls) {
            if (control.condition != null) {
                logger.log(Level.INFO, "Evaluating condition for control {0}", control);
                if (!control.condition.evaluate()) {
                    logger.log(Level.INFO, "Skipping control {0}", control);
                    continue;
                }
            }
            logger.log(Level.INFO, "Executing control {0}", control);
            control.onExecute();
        }
    }
    
    public void displayIllustrationContainer() {
        if (this.illustrationContainer != null) {
            this.appController.removeNode(this.name, Area.ILLUSTRATION.name());
        }
        
        Double documentX;
        Double documentY = 0.09;
        HorizontalAlignment documentHorizontalAlignment;
        if (SECOND_PAGE == LEFT_PAGE) {
            documentX = LEFT_PAGE_STARTING_X;
            documentHorizontalAlignment = HorizontalAlignment.LEFT;
        } else {
            documentX = RIGHT_PAGE_ENDING_X;
            documentHorizontalAlignment = HorizontalAlignment.RIGHT;
        }
        this.illustrationContainer = new Pane(Area.ILLUSTRATION.name());
        this.illustrationContainer.borderWidth = 0;
        this.illustrationContainer.scaleX = 0.4; // Relative width of an individual page
        this.illustrationContainer.scaleY = 0.785; // Relative height of an individual page (less the title and a spacer and the button row)
        this.appController.addNode(this.name, this.name, this.illustrationContainer, new Layout(new RelativeCoordinates(documentX, documentY), documentHorizontalAlignment, VerticalAlignment.TOP));
    }
    
    public void displayIllustrationNode(BaseNode node, Layout layout) {
        logger.log(Level.INFO, "Entered: node={0}, layout={1}", new Object[]{node, layout});
        this.appController.addNode(this.name, this.illustrationContainer.name, node, layout);
    }
    
    public void displayStoryContainer() {
        if (this.storyDocument != null) {
            this.appController.removeNode(this.name, Area.STORY.name());
        }
        
        Double documentX;
        Double documentY = 0.09;
        HorizontalAlignment storyDocumentHorizontalAlignment;
        if (FIRST_PAGE == LEFT_PAGE) {
            documentX = LEFT_PAGE_STARTING_X;
            storyDocumentHorizontalAlignment = HorizontalAlignment.LEFT;
        } else {
            documentX = RIGHT_PAGE_ENDING_X;
            storyDocumentHorizontalAlignment = HorizontalAlignment.RIGHT;
        }
        this.storyDocument = new ScrollingDocument(Area.STORY.name());
        this.storyDocument.borderWidth = 0;
        this.storyDocument.scaleX = 0.4; // Relative width of an individual page
        this.storyDocument.scaleY = 0.785; // Relative height of an individual page (less the title and a spacer)
        this.appController.addNode(this.name, this.name, this.storyDocument, new Layout(new RelativeCoordinates(documentX, documentY), storyDocumentHorizontalAlignment, VerticalAlignment.TOP));
    }
    
    public void displayStoryNode(BaseNode node) {
        logger.log(Level.INFO, "Entered: node={0}", node);
        this.appController.addNode(this.name, this.storyDocument.name, node, null);
    }
    
    public String newNodeIndex() {
        String index = this.name + this.nodeIndex++;
        return index;
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
    
    public String getNextScene(Boolean movingForward) {
        System.out.println("Quest: getNextScene: movingForward=" + movingForward);
        
        Act currentAct = this.book.acts.get(this.currentAct);
        Scene currentScene = currentAct.scenes.get(this.currentScene);
        
        if ((currentScene.x == null) || (currentScene.y == null)) {
            System.out.println("Quest: getNextScene: Outside of a scene! x=" + currentScene.x + ", y=" + currentScene.y);
            return Quest.EDGE_OF_THE_WORLD;
        }
        
        int x = currentScene.x;
        int y = currentScene.y;
        System.out.println("Quest: getNextScene: Current x=" + x + ", current y=" + y);
        if (movingForward) {
            switch (this.getPlayerDirection().toUpperCase()) {
                case Quest.DIRECTION_EAST -> x += 1;
                case Quest.DIRECTION_NORTH -> y += -1;
                case Quest.DIRECTION_SOUTH -> y += 1;
                case Quest.DIRECTION_WEST -> x += -1;
            }
        } else {
            switch (this.getPlayerDirection().toUpperCase()) {
                case Quest.DIRECTION_EAST -> x += -1;
                case Quest.DIRECTION_NORTH -> y += 1;
                case Quest.DIRECTION_SOUTH -> y += -1;
                case Quest.DIRECTION_WEST -> x += 1;
            }
        }
        System.out.println("Quest: getNextScene: Next x=" + x + ", next y=" + y);
        
        String nextSceneName = Quest.EDGE_OF_THE_WORLD;
        for (String sceneName : currentAct.scenes.keySet()) {
            Scene scene = currentAct.scenes.get(sceneName);
            if ((scene.x == null) || (scene.y == null)) {
                continue;
            }
            if ((scene.x == x) && (scene.y == y)) {
                System.out.println("Quest: getNextScene: Found matching scene: " + sceneName);
                nextSceneName = sceneName;
                break;
            }
        }
        
        return nextSceneName;
    }
    
    public String getPlayerDirection() {
        return this.playerDirection;
    }
    
    public int getPlayerHP() {
        return this.playerHP;
    }
    
    public int getPlayerMP() {
        return this.playerMP;
    }
    
    public int getPlayerXP() {
        return this.playerXP;
    }
    
    public Story getSubpage(String name, Boolean spellOnly) {
        System.out.println("Quest: getSubpage: name=" + name + ", spellOnly=" + spellOnly);
        
        Act act = this.book.acts.get(this.currentAct);
        Scene scene = act.scenes.get(this.currentScene);
        Page page = scene.pages.get(this.currentPage);
        
        Story story;
        if (page.subpages.containsKey(name)) {
            story = page.subpages.get(name);
            if ((!spellOnly) || (story.isSpell)) {
                System.out.println("Quest: getSubpage: Found subpage at page level");
                return story;
            }
        }
        if (scene.subpages.containsKey(name)) {
            story = scene.subpages.get(name);
            if ((!spellOnly) || (story.isSpell)) {
                System.out.println("Quest: getSubpage: Found subpage at scene level");
                return story;
            }
        }
        if (act.subpages.containsKey(name)) {
            story = act.subpages.get(name);
            if ((!spellOnly) || (story.isSpell)) {
                System.out.println("Quest: getSubpage: Found subpage at act level");
                return story;
            }
        }
        if (this.book.subpages.containsKey(name)) {
            story = this.book.subpages.get(name);
            if ((!spellOnly) || (story.isSpell)) {
                System.out.println("Quest: getSubpage: Found subpage at book level");
                return story;
            }
        }
        
        System.out.println("Quest: getSubpage: Did NOT find the subpage: page=" + this.currentPage + ", scene=" + this.currentScene + ", act=" + this.currentAct);
        
        return null;
    }
    
    public void setPlayerDirection(String direction) {
        this.playerDirection = direction;

        // Update the collection of observed scenes
        this.updateObservedScenes();

        this.publishEvent(PLAYER_DIRECTION, this.playerDirection);        
    }
    
    // TODO - Make this configurable as a Story element on any level of the Book
    public void setPlayerHP(int delta, Boolean refreshPage, String lastEnemyThatAttacked, Boolean displayOverlay) {
        System.out.println("Quest: setPlayerHP: delta=" + delta + ", refreshPage=" + refreshPage);
        
        this.playerHP = this.playerHP + delta;
        
        String overlayName;
        if (refreshPage) {
            overlayName = HP_CHANGE_REFRESH;
        } else {
            overlayName = HP_CHANGE;
        }
        
        if (this.playerHP <= 0) {
            this.playerHP = 0;
            this.appController.stopAllSounds();
            this.appController.playSound("/assets/sounds/impact.wav false>", false);
            this.appController.playSound("/assets/sounds/death.mp3", true);
        }
        
        if (delta < 0) {
            this.lastEnemyThatAttacked = lastEnemyThatAttacked;
            if (delta >= -10) {
                this.appController.playSound("/assets/sounds/hit.mp3", false);
            } else {
                this.appController.playSound("/assets/sounds/hit-harder.mp3", false);
            }
            if (displayOverlay) {
                Rectangle overlay = new Rectangle(overlayName);
                overlay.color = Color.RED;
                overlay.opacity = 0.5;
                overlay.scaleX = 1.0;
                overlay.scaleY = 1.0;
                this.appController.addNode(this.name, this.name, overlay, new Layout(new RelativeCoordinates(0.0, 0.0), HorizontalAlignment.CENTER, VerticalAlignment.CENTER));
                appController.setTimer(overlayName, 0.5, this);
            }
        } else if (delta > 0) {
            //this.appController.playSound("/assets/sounds/TODO", false);
            if (displayOverlay) {
                Rectangle overlay = new Rectangle(overlayName);
                overlay.color = Color.GREEN;
                overlay.opacity = 0.5;
                overlay.scaleX = 1.0;
                overlay.scaleY = 1.0;
                this.appController.addNode(this.name, this.name, overlay, new Layout(new RelativeCoordinates(0.0, 0.0), HorizontalAlignment.CENTER, VerticalAlignment.CENTER));
                appController.setTimer(overlayName, 0.5, this);
            }
        }        
    }
    
    // TODO - Make this configurable as a Story element on any level of the Book
    public void setPlayerMP(int delta, Boolean refreshPage) {
        System.out.println("Quest: setPlayerMP: delta=" + delta + ", refreshPage=" + refreshPage);
        
        this.playerMP = this.playerMP + delta;

        if (this.playerMP < 0) {
            this.playerMP = 0;
        } else if (this.playerMP > 100) {
            this.playerMP = 100;
        }
        
        String overlayName;
        if (refreshPage) {
            overlayName = MP_CHANGE_REFRESH;
        } else {
            overlayName = MP_CHANGE;
        }
        
        if (delta < 0) {
            this.appController.playSound("/assets/sounds/spell-cast.wav", false);
            Rectangle overlay = new Rectangle(overlayName);
            overlay.color = Color.DARK_MAGENTA;
            overlay.opacity = 0.5;
            overlay.scaleX = 1.0;
            overlay.scaleY = 1.0;
            this.appController.addNode(this.name, this.name, overlay, new Layout(new RelativeCoordinates(0.0, 0.0), HorizontalAlignment.CENTER, VerticalAlignment.CENTER));
            appController.setTimer(overlayName, 0.5, this);
        } else if (delta > 0) {
            this.appController.playSound("/assets/sounds/mp-up.wav", false);
            Rectangle overlay = new Rectangle(overlayName);
            overlay.color = Color.DARK_MAGENTA;
            overlay.opacity = 0.5;
            overlay.scaleX = 1.0;
            overlay.scaleY = 1.0;
            this.appController.addNode(this.name, this.name, overlay, new Layout(new RelativeCoordinates(0.0, 0.0), HorizontalAlignment.CENTER, VerticalAlignment.CENTER));
            appController.setTimer(overlayName, 0.5, this);
        }        
    }
    
    // TODO - Make this configurable as a Story element on any level of the Book
    public void setPlayerXP(int delta, Boolean refreshPage) {
        System.out.println("Quest: setPlayerXP: delta=" + delta + ", refreshPage=" + refreshPage);
        
        this.playerXP = this.playerXP + delta;
        
        String overlayName;
        if (refreshPage) {
            overlayName = XP_CHANGE_REFRESH;
        } else {
            overlayName = XP_CHANGE;
        }
        
        if (delta < 0) {
            //this.appController.displayOverlay(this.name, overlayName, new Color(128, 0, 128), null, null, null, null, null);
            //appController.setTimer(overlayName, 0.5, this);
        } else if (delta > 0) {
            //this.appController.displayOverlay(this.name, overlayName, new Color(128, 0, 128), null, null, null, null, null);
            //appController.setTimer(overlayName, 0.5, this);
        }  

        if (this.playerXP < 0) {
            this.playerXP = 0;
        }       
    }
    
    public void startAct(String actName) {
        System.out.println("Quest: startAct: act=" + actName);
        
        Boolean isFirstAct = (this.currentAct == null);
        if (!isFirstAct) {
            System.out.println("Quest: startAct: Stopping any sounds from previous act");
            this.appController.stopAllSounds();
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
        if ((scene.stopOtherSounds == true) && (previousSoundFileName != null) && (!previousSoundFileName.equals("")) && (!scene.soundFileName.equals(previousSoundFileName))) {
            System.out.println("Quest: startScene: Stopping sound file " + previousSoundFileName);
            this.appController.stopAllSounds();
        }
        
        // Start a new sound file if needed
        if ((!isFirstAct) && (scene.soundFileName != null) && (!scene.soundFileName.equals("")) && (!scene.soundFileName.equals(previousSoundFileName))) {
            System.out.println("Quest: startScene: Playing sound file " + scene.soundFileName);
            this.appController.playSound(scene.soundFileName, true);
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
        String nextSceneName = this.getNextScene(true);
        if (!this.observedScenes.get(this.currentAct).contains(nextSceneName)) {
            this.observedScenes.get(this.currentAct).add(nextSceneName);
        }
    }
    
}
