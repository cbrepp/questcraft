package quest.view;

import app.ApplicationController;
import app.Color;
import app.FontStyle;
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
    public final static String GAME_OVER = "game-over";
    public final static String HP_CHANGE = "hp-change";
    public final static String HP_CHANGE_REFRESH = "hp-change-refresh";
    public final static int LEFT_PAGE = 1;
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
    
    public ApplicationController appController;
    public Book book;
    public int buttonRow;
    public String currentAct;
    public int currentDisplayPage;
    public String currentPage;
    public String currentScene;
    public Boolean isGameOver;
    public Map<String, InventoryItem> inventory;
    public Inventory inventoryView; // TODO - Make the inventoryView a listener of inventory change events
    public String lastEnemyThatAttacked;
    public int leftPageStartingColumn;
    public int leftPageEndingColumn;
    public Boolean magicText;
    public SceneMap map;
    public Map<String, List<String>> observedScenes;
    private String playerDirection;
    private int playerHP;
    private int playerMP;
    private int playerXP;
    public String playerSymbol;
    public Integer playerX;
    public Integer playerY;
    public Map<String, QuestControl> questControls;
    public Random random = new Random();
    public int rightPageStartingColumn;
    public int rightPageEndingColumn;
    public SpellBook spellBook;
    public int startingRow;
    public Color textColor;
    public int textColumn;
    public int textRow;
    public int textStyle;
    public int titleRow;
    public Map<String, String> variables;

    public Quest(String name) {
        super(name);
        
        this.backgroundImage = "/assets/images/book.png";
        this.backgroundColor = new Color(0, 0, 0);
        this.isGameOver = false;
        this.playerHP = 100;
        this.playerMP = 0;
        this.playerXP = 0;
        this.playerSymbol = "\uD83E\uDDB0";
        this.magicText = false;
        this.inventory = new LinkedHashMap<>();
        this.variables = new HashMap<>();
        this.observedScenes = new HashMap();
        this.questControls = new HashMap<>();
        this.questControls.put(ActGotoControl.NAME, new ActGotoControl(this));
        this.questControls.put(AddViewControl.NAME, new AddViewControl(this));
        this.questControls.put(AnimationInitControl.NAME, new AnimationInitControl(this));
        this.questControls.put(BoldTextControl.NAME, new BoldTextControl(this));
        this.questControls.put(BoldTextOffControl.NAME, new BoldTextOffControl(this));
        this.questControls.put(BookAuthorControl.NAME, new BookAuthorControl(this));
        this.questControls.put(BookFlipControl.NAME, new BookFlipControl(this));
        this.questControls.put(BookLastUpdatedDateControl.NAME, new BookLastUpdatedDateControl(this));
        this.questControls.put(BookTitleControl.NAME, new BookTitleControl(this));
        this.questControls.put(BreakControl.NAME, new BreakControl(this));
        this.questControls.put(ButtonRowControl.NAME, new ButtonRowControl(this));
        this.questControls.put(ColorTextControl.NAME, new ColorTextControl(this));
        this.questControls.put(ColorTextOffControl.NAME, new ColorTextOffControl(this));
        this.questControls.put(DoubleQuoteControl.NAME, new DoubleQuoteControl(this));
        this.questControls.put(GetInputControl.NAME, new GetInputControl(this));
        this.questControls.put(GetValidatedInputControl.NAME, new GetValidatedInputControl(this));
        this.questControls.put(GifControl.NAME, new GifControl(this));
        this.questControls.put(IfControl.NAME, new IfControl(this));
        this.questControls.put(ImageControl.NAME, new ImageControl(this));
        this.questControls.put(InventoryAddControl.NAME, new InventoryAddControl(this));
        this.questControls.put(InventoryControl.NAME, new InventoryControl(this));
        this.questControls.put(InventoryHasControl.NAME, new InventoryHasControl(this));
        this.questControls.put(ItalicsTextControl.NAME, new ItalicsTextControl(this));
        this.questControls.put(ItalicsTextOffControl.NAME, new ItalicsTextOffControl(this));
        this.questControls.put(LinkControl.NAME, new LinkControl(this));
        this.questControls.put(MaskControl.NAME, new MaskControl(this));
        this.questControls.put(MonsterShooterControl.NAME, new MonsterShooterControl(this));
        this.questControls.put(MoveAheadControl.NAME, new MoveAheadControl(this));
        this.questControls.put(MoveBackControl.NAME, new MoveBackControl(this));
        this.questControls.put(NextSceneControl.NAME, new NextSceneControl(this));
        this.questControls.put(ObservedSceneAddControl.NAME, new ObservedSceneAddControl(this));
        this.questControls.put(OverlayControl.NAME, new OverlayControl(this));
        this.questControls.put(PageGotoControl.NAME, new PageGotoControl(this));
        this.questControls.put(PageRefreshControl.NAME, new PageRefreshControl(this));
        this.questControls.put(PlayerDirectionControl.NAME, new PlayerDirectionControl(this));
        this.questControls.put(PlayerHPChangeControl.NAME, new PlayerHPChangeControl(this));
        this.questControls.put(PlayerHPControl.NAME, new PlayerHPControl(this));
        this.questControls.put(PlayerMPChangeControl.NAME, new PlayerMPChangeControl(this));
        this.questControls.put(PlayerMPControl.NAME, new PlayerMPControl(this));
        this.questControls.put(PlayerSymbolControl.NAME, new PlayerSymbolControl(this));
        this.questControls.put(PlayerSymbolSetControl.NAME, new PlayerSymbolSetControl(this));
        this.questControls.put(PlayerXPChangeControl.NAME, new PlayerXPChangeControl(this));
        this.questControls.put(PlayerXPControl.NAME, new PlayerXPControl(this));
        this.questControls.put(RandomControl.NAME, new RandomControl(this));
        this.questControls.put(RemoveControl.NAME, new RemoveControl(this));
        this.questControls.put(SceneGotoControl.NAME, new SceneGotoControl(this));
        this.questControls.put(SceneControl.NAME, new SceneControl(this));
        this.questControls.put(SetCursorButtonRowControl.NAME, new SetCursorButtonRowControl(this));
        this.questControls.put(SetFocusOnFirstPageControl.NAME, new SetFocusOnFirstPageControl(this));
        this.questControls.put(SetFocusOnSecondPageControl.NAME, new SetFocusOnSecondPageControl(this));
        this.questControls.put(SetMagicTextControl.NAME, new SetMagicTextControl(this));
        this.questControls.put(SetPlayerDirectionControl.NAME, new SetPlayerDirectionControl(this));
        this.questControls.put(SetTextColorControl.NAME, new SetTextColorControl(this));
        this.questControls.put(SoundPlayControl.NAME, new SoundPlayControl(this));
        this.questControls.put(SoundStopControl.NAME, new SoundStopControl(this));
        this.questControls.put(SubpageDisplayControl.NAME, new SubpageDisplayControl(this));
        this.questControls.put(TabSelectControl.NAME, new TabSelectControl(this));
        this.questControls.put(TimerStartControl.NAME, new TimerStartControl(this));
        this.questControls.put(TimerStopControl.NAME, new TimerStopControl(this));
        this.questControls.put(TurnLeftControl.NAME, new TurnLeftControl(this));
        this.questControls.put(TurnRightControl.NAME, new TurnRightControl(this));
        this.questControls.put(UnderlineTextControl.NAME, new UnderlineTextControl(this));
        this.questControls.put(UnderlineTextOffControl.NAME, new UnderlineTextOffControl(this));
        this.questControls.put(VariableControl.NAME, new VariableControl(this));
        this.questControls.put(VariableSetControl.NAME, new VariableSetControl(this));
        this.questControls.put(VariableAddControl.NAME, new VariableAddControl(this));
        this.textColor = new Color(0, 0, 0);
        this.textStyle = FontStyle.NORMAL;
        this.emoji = "\uD83D\uDCD6"; // "open book" Unicode emoji
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("Quest: onEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        switch(eventName) {
            case HP_CHANGE -> this.appController.clearControl(this.name, HP_CHANGE);
            case HP_CHANGE_REFRESH -> {
                this.appController.clearControl(this.name, HP_CHANGE_REFRESH);
                this.display();
            }
            case MP_CHANGE -> this.appController.clearControl(this.name, MP_CHANGE);
            case MP_CHANGE_REFRESH -> {
                this.appController.clearControl(this.name, MP_CHANGE_REFRESH);
                this.display();
            }
            case XP_CHANGE -> this.appController.clearControl(this.name, XP_CHANGE);
            case XP_CHANGE_REFRESH -> {
                this.appController.clearControl(this.name, XP_CHANGE_REFRESH);
                this.display();
            }
            case LOADING_COMPLETE -> {
                this.appController.clearControl(this.name, LOADING_OVERLAY);
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
                        this.displayPage(subpage.contents, true);
                    } else {
                        subpageName = "INPUT " + key;
                        subpage = getSubpage(subpageName, false);
                        if (subpage != null) {
                            this.displayPage(subpage.contents, true);
                        }
                    }
                } else if (eventNameParts[0].equals(TIMER_EVENT_PREFIX)) {
                    String key = eventNameParts[1];
                    String subpageName = "TIMER " + key;
                    Story subpage = getSubpage(subpageName, false);
                    if (subpage != null) {
                        this.displayPage(subpage.contents, true);
                    }
                } else if (eventNameParts[0].equals(LINK_EVENT_PREFIX)) {
                    String key = eventNameParts[1];
                    String subpageName = "LINK " + key;
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
        if (this.book.animationFileName != null) {
            this.appController.displayOverlay(this.name, LOADING_OVERLAY, new Color(0, 0, 0), null, null, null, null, null);
            int halfColumns = (appController.getTextColumns() / 2);
            int halfGifWidth = (appController.getColumns(this.book.animationFileName) / 2);
            int gifColumn = halfColumns - halfGifWidth;
            int halfRows = (appController.getTextRows() / 2);
            int gifHeight = appController.getRows(this.book.animationFileName);
            int gifRow = halfRows - gifHeight;
            int nextRow = appController.displayGif(this.name, this.book.animationFileName, gifRow, gifColumn);
            int halfTextWidth = ("Loading...".length() / 2);
            int loadingTextColumn = halfColumns - halfTextWidth;
            appController.displayText(this.name, "Loading...", nextRow, loadingTextColumn, new Color(255, 255, 255));
            Act firstAct = book.acts.get(this.book.firstActName);
            Scene firstScene = firstAct.scenes.get(firstAct.firstSceneName);
            this.appController.playSound(firstScene.soundFileName, true);
            appController.setTimer(LOADING_COMPLETE, 3, this);
        }
        
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
        
        // Start book (if not waiting for the animation to complete and the LOADING_COMPLETE event to be raised)
        if (this.book.animationFileName == null) {
            this.startAct(this.book.firstActName);
            this.display();
        }
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
        this.appController.playSound("/assets/sounds/turn-page.mp3", false);
        
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
        List<String> pageContents;
        Story pageStory;
        if ((page == null) || (page.story == null)) {
            pageContents = new ArrayList();
            pageContents.add("404 NOT FOUND");
            displayPage(pageContents, false);
            return;
        }
        pageStory = page.story;
        pageContents = pageStory.contents;
        displayPage(pageContents, false);
        
        // Next page button
        Boolean isNextPageDisplaying = false;
        if ((this.playerHP != 0) && (!page.hideNextButton) && ((act.nextActName != null) || (scene.nextSceneName != null) || (page.nextPageName != null))) {
            String buttonText = "Next >";
            int buttonColumns = appController.getButtonColumns(buttonText);
            int buttonColumn = this.rightPageEndingColumn - buttonColumns + 1;
            this.appController.displayButton(this.name, NEXT_PAGE, buttonText, this.buttonRow, buttonColumn, null, null, false, null, !page.noGlow, this);
            isNextPageDisplaying = true;
        }

        // Previous page button (return to previous page, scene, or act)
        if ((this.playerHP != 0) && (!page.hidePreviousButton) && ((page.previousPageName != null) || ((scene.firstPageName.equals(this.currentPage)) && (scene.previousSceneName != null))  || ((scene.firstPageName.equals(this.currentPage)) && (act.firstSceneName.equals(this.currentScene)) && (act.previousActName != null)))) {
            String buttonText = "< Previous";
            int buttonColumn = this.leftPageStartingColumn;
            Boolean glow = ((!isNextPageDisplaying) && (!page.noGlow));   // If there is no Next Page button, then attention should be called to going back
            this.appController.displayButton(this.name, PREVIOUS_PAGE, buttonText, this.buttonRow, buttonColumn, null, null, false, null, glow, this);
        }
        
        // Game Over button
        if ((!this.isGameOver) && (this.playerHP == 0)) {
            String buttonText = "Game Over >";
            int buttonColumns = appController.getButtonColumns(buttonText);
            int buttonColumn = this.rightPageEndingColumn - buttonColumns + 1;
            this.appController.displayButton(this.name, GAME_OVER, buttonText, this.buttonRow, buttonColumn, null, null, false, null, true, this);
        }
    }
    
    public void displayPage(List<String> page, Boolean isSubpage) {
        System.out.println("Quest: displayPage");
        
        if (!isSubpage) {
            this.currentDisplayPage = FIRST_PAGE;
            this.textColumn = 1;
            this.textRow = 1;
            this.textColor = new Color(0, 0, 0);
            this.textStyle = FontStyle.NORMAL;
            System.out.println("Quest: displayPage : Initialized textRow to 1");
        }
        
        Color currentTextColor = this.textColor;
        int currentTextStyle = this.textStyle;
        
        for (String pageLine : page) {
            String storyText = "";
            Boolean pageLineContainsText = false;
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
                            if ((control.unspoolStoryText) && (!storyText.equals(""))) {
                                System.out.println("Quest: displayPage: Unspooling story text...");
                                pageLineContainsText = true;
                                this.displayTextOnPage(storyText, this.textRow, this.textColumn, this.textColor, this.textStyle);
                                storyText = "";
                                newText = "";
                                this.textColumn = this.textColumn - 1;
                            }
                            System.out.println("Quest: displayPage: Executing tag " + questControlName);
                            // TODO - If displaying an in-line control (like 'inventory'), need to display all of the text accumulated thus far
                            newText = control.execute(questControlTag);
                            i = i + questControlTag.length() - 1;
                        }
                    }
                }
                storyText = storyText + newText;
                
                if ((currentTextStyle != this.textStyle) || (!currentTextColor.equals(this.textColor))) {
                    pageLineContainsText = true;
                    // Display the previous text styling for the text accumulated thus far
                    System.out.println("Quest: displayPage: Displaying current story text for previous style on textRow " + this.textRow + ": " + storyText);
                    this.displayTextOnPage(storyText, this.textRow, this.textColumn, currentTextColor, currentTextStyle);
                    storyText = "";
                    currentTextColor = this.textColor;
                    currentTextStyle = this.textStyle;
                    //this.textColumn = this.textColumn - 1;
                    System.out.println("Quest: displayPage: Updated text style!");
                }
            }
            
            if (storyText.length() > 0) {
                pageLineContainsText = true;
                this.displayTextOnPage(storyText, this.textRow, this.textColumn, this.textColor, this.textStyle);
            }
            
            if (pageLineContainsText) {
                // Page lines that have no text but rather a quest control like <br> or <second-page> should not increment the text row.
                // This requires that empty lines use <br>.
                this.textRow = this.textRow + 1;
                this.textColumn = 1;
                System.out.println("Quest: displayPage: Done with page line and it had text so advanced text row to " + this.textRow);
            }
        }
    }
    
    public void displayTextOnPage(String text, Integer row, Integer column, Color color, int style) {
        this.textColumn = column;
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
        System.out.println("Quest: displayTextOnPage: Start: realRow=" + realRow);
        int realColumn = startingColumn + this.textColumn - 1;
        int rowWidth = endingColumn - startingColumn;
        String remainingText = text;
        while (remainingText.length() > 0) {
            Boolean wrapText = true;
            String lineText;
            if ((remainingText.length() + this.textColumn - 1) <= (rowWidth + 1)) {
                lineText = remainingText;
                remainingText = "";
                wrapText = false;
            } else {
                System.out.println("remainingText=" + remainingText + ", rowWidth=" + rowWidth + ", this.textColumn=" + this.textColumn);
                int charsToGrab;
                if ((rowWidth + 1 - this.textColumn) > (remainingText.length())) {
                    charsToGrab = remainingText.length();
                } else {
                    charsToGrab = rowWidth + 1 - this.textColumn;
                    if (charsToGrab < 0) {
                        charsToGrab = remainingText.length();
                    }
                }
                System.out.println("charsToGrab = " + charsToGrab + ", length = " + remainingText.length());
                lineText = remainingText.substring(0, charsToGrab);
                int lastSpaceIndex = lineText.lastIndexOf(' ');
                if (lastSpaceIndex != -1) {
                    lineText = lineText.substring(0, lastSpaceIndex);
                    remainingText = remainingText.substring(lineText.length(), remainingText.length());
                } else {
                    lineText = lineText + "-";
                    //remainingText = remainingText.substring(rowWidth, remainingText.length());
                    remainingText = remainingText.substring(0, charsToGrab);
                }
            }
            if ((!this.magicText) || (color.red != 0) || (color.green != 0) || (color.blue != 0)) {
                System.out.println("Quest: displayTextOnPage: starting row=" + row + ", now on " + (this.textRow) + ", text=" + lineText + ", realColumn=" + realColumn + ", textColumn=" + this.textColumn);
                this.appController.displayText(this.name, lineText, realRow, realColumn, color, style);
            } else {
                // When magic words are turned on and the font color is black, color each word individually
                int magicColumn = realColumn;
                //Random random = new Random();
                String[] magicWords = lineText.split(" ");
                for (String magicWord : magicWords) {
                    // Randomly pick a number from the bottom 3/4 of the 256 RGB scale.
                    // (Bottom half means darker and easier to read on the light book pages.)
                    int red = this.random.nextInt(192);
                    int blue = this.random.nextInt(192);
                    Color randomColor = new Color(red, 0, blue);
                    this.appController.displayText(this.name, magicWord + " ", realRow, magicColumn, randomColor, style);
                    this.textColumn += magicWord.length() + 1;
                    magicColumn += magicWord.length() + 1;
                }
                System.out.println("Quest: displayTextOnPage: lineText=" + lineText + ", realRow=" + realRow + ", realColumn=" + realColumn);
                this.appController.displayText(this.name, lineText, realRow, realColumn, color, style);
            }
            remainingText = remainingText.trim();
            if (wrapText) {
                realRow++;
                realColumn = startingColumn;
                this.textColumn = 1;
                this.textRow = this.textRow + 1;
                System.out.println("Quest: displayTextOnPage: Wrapping text so incrementing row: realRow now " + realRow);
            } else {
                this.textColumn += lineText.length() + 1;
                realColumn += lineText.length();
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
        
        System.out.println("Quest: getSubpage: Did NOT find the subpage");
        
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
                this.appController.displayOverlay(this.name, overlayName, new Color(255, 0, 0), null, null, null, null, null);
                appController.setTimer(overlayName, 0.5, this);
            }
        } else if (delta > 0) {
            //this.appController.playSound("/assets/sounds/TODO", false);
            if (displayOverlay) {
                this.appController.displayOverlay(this.name, overlayName, new Color(0, 255, 0), null, null, null, null, null);
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
            this.appController.displayOverlay(this.name, overlayName, new Color(128, 0, 128), null, null, null, null, null);
            appController.setTimer(overlayName, 0.5, this);
        } else if (delta > 0) {
            this.appController.playSound("/assets/sounds/mp-up.wav", false);
            this.appController.displayOverlay(this.name, overlayName, new Color(128, 0, 128), null, null, null, null, null);
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
        if ((previousSoundFileName != null) && (!scene.soundFileName.equals(previousSoundFileName))) {
            System.out.println("Quest: startScene: Stopping sound file " + previousSoundFileName);
            this.appController.stopAllSounds();
        }
        
        // Start a new sound file if needed
        if ((!isFirstAct) && (scene.soundFileName != null) && (!scene.soundFileName.equals(previousSoundFileName))) {
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
