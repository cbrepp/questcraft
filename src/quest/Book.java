package quest;

import app.ApplicationController;
import app.Color;
import app.FontStyle;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import quest.control.BreakControl;
import quest.control.DoubleQuoteControl;
import quest.control.QuestControl;
import quest.control.SetFocusOnFirstPageControl;
import quest.control.SetFocusOnSecondPageControl;
import quest.control.SetTextColorControl;

public class Book extends app.ApplicationView {
    
    public final static int FIRST_PAGE = 1;
    public final static String INTRODUCTION = "INTRODUCTION";
    public final static String NEXT_PAGE = "next-page";
    public final static String PREVIOUS_PAGE = "previous-page";
    public final static int SECOND_PAGE = 2;
    public final static String TIMER_EVENT = "LOADING_COMPLETE";
    public final static String TITLE_PAGE = "title-page";
    
    public ApplicationController appController;
    public BookFile bookFile;
    public int buttonRow;
    public String currentChapter;
    public int currentDisplayPage;
    public int currentPage;
    public int leftPageStartingColumn;
    public int leftPageEndingColumn;
    public Map<String, QuestControl> questControls;
    public int rightPageStartingColumn;
    public int rightPageEndingColumn;
    public int startingRow;
    public Color textColor;
    public int textRow;
    public int textStyle;
    public int titleRow;
    public Map<String, String> variables;
    
    /*
    public String author;
    public String basicStoryChoices;
    public String chapter;
    public String controlChoices;
    public Integer controlChoicesColumn;
    public Integer controlChoicesRow;
    public String credits;
    public String deathHitSound;
    public String difficulty;
    public Integer doorX;
    public Integer doorY;
    public Integer experiencePoints;
    public Integer firstRow;
    public String heavyHitSound;
    public String highScores;
    public Integer hitPoints;
    public String hitSound;
    public String inventory;
    public String inventorySound;
    public Boolean isOver;
    public Boolean isRightHanded;
    public Integer lastImageX;
    public Integer lastImageY;
    public Integer lastLevel;
    public Integer lastRow;
    public Integer level;
    public Integer levelSize;
    public Integer magicPoints;
    public Integer maxPageLength;
    public String openingGIF;
    public String openingMusic;
    public Integer pageNumber;
    public String pageTurnSound;
    public String player;
    public Integer playerDirection;
    public Integer playerLastX;
    public Integer playerLastY;
    public String playerMode;
    public Integer playerX;
    public Integer playerY;
    public String pointsSound;
    public Integer score;
    public String secretChoices;
    public String story;
    public String storyChoices;
    public Integer storyChoicesColumn;
    public Integer storyChoicesRow;
    public String storyFile;
    public String storySound;
    public String title;
    public String twin;
    public String version;
    public String victorySound;
    */

    public Book(String name) {
        super(name);
        
        this.backgroundColor = new Color(0, 0, 0);
        this.currentChapter = TITLE_PAGE;
        this.currentDisplayPage = 1;
        this.currentPage = 0;
        this.variables = new LinkedHashMap<>();
        this.questControls = new LinkedHashMap<>();
        this.questControls.put(BreakControl.NAME, new BreakControl(this));
        this.questControls.put(SetTextColorControl.NAME, new SetTextColorControl(this));
        this.questControls.put(SetFocusOnFirstPageControl.NAME, new SetFocusOnFirstPageControl(this));
        this.questControls.put(DoubleQuoteControl.NAME, new DoubleQuoteControl(this));
        this.questControls.put(SetFocusOnSecondPageControl.NAME, new SetFocusOnSecondPageControl(this));
        
        /*
        this.isOver = false;
        this.isRightHanded = true; // Are the game controls on the right page? (1/0)
        this.firstRow = 3; // book.png allows for row 3 as the first row for text
        this.lastRow = 46; // book.png allows for row 46 as the last row for text
        this.leftPageStartingColumn = 15; // book.png allows for column 15 as the first column for starting text on the left page
        this.rightPageStartingColumn = 83; // book.png allows for column 83 as the first column for starting text on the right page
        this.maxPageLength = 66; // book.png allows for 66 characters of text on a single line of a single page
        this.controlChoicesColumn = 0;
        this.controlChoicesRow = 0;
        this.storyChoices = ""; // current choices for the player
        this.storyChoicesColumn = 0;
        this.storyChoicesRow = 0;
        this.player = ""; // GREYSON or ZARA
        this.controlChoices = "CREDITS,FLIP BOOK,HIGH SCORES,EXIT GAME";
        this.basicStoryChoices = "LOOK,LISTEN";
        this.secretChoices = "MOVE AHEAD,MOVE BACKWARDS,TURN LEFT,TURN RIGHT,ALL THE THINGS";
        this.score = 0;
        this.inventory = "";
        this.hitPoints = 100;
        this.magicPoints = 0;
        this.experiencePoints = 0;
        */
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("Book: handleEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        switch(eventName) {
            case NEXT_PAGE -> {
                // TODO - Add story mode property for chapters and check that instead of specific chapter names
                Chapter chapter = bookFile.chapters.get(this.currentChapter);
                if (this.currentPage < (chapter.pages.size() - 1)) {
                    this.currentPage++;
                } else if (this.currentChapter.equals(TITLE_PAGE)) {
                    this.currentChapter = INTRODUCTION;
                    app.Utility.stopAllSounds();
                    app.Utility.playSound(bookFile.chapters.get(INTRODUCTION).soundFileName, true);
                }
                this.display();
            }
            case PREVIOUS_PAGE -> this.appController.displayMessageBox("Not yet implemented!!!");
            case TIMER_EVENT -> {
                this.display();
            }
            default -> System.err.println("Book: handleEvent: Unsupported event");
        }
    }
    
    @Override
    public void onLoad(ApplicationController appController) {
        System.out.println("Book: onLoad");
        this.appController = appController;
        
        // Loading screen
        int halfColumns = (appController.getTextColumns() / 2);
        int halfGifWidth = (appController.getColumns(this.bookFile.animationFileName) / 2);
        int gifColumn = halfColumns - halfGifWidth;
        int nextRow = appController.displayGif(this.name, this.bookFile.animationFileName, 3, gifColumn);
        int halfTextWidth = ("Loading...".length() / 2);
        int loadingTextColumn = halfColumns - halfTextWidth;
        appController.displayText(this.name, "Loading...", nextRow, loadingTextColumn, new Color(255, 255, 255));
        app.Utility.playSound(bookFile.chapters.get(TITLE_PAGE).soundFileName, true);
        appController.setTimer(TIMER_EVENT, 3, this);
        
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
    
    public void display() {
        System.out.println("Book: display: chapter=" + this.currentChapter + ", page=" + this.currentPage);
        
        // Dispose all controls on the book's composite
        this.appController.clearScreen(this.name);
        
        // Display the book image
        this.backgroundImage = "/assets/images/book.png";
        this.appController.setBackgroundImage(this.name, this.backgroundImage);
        
        // Play the page turn sound
        app.Utility.playSound("/assets/sounds/turn-page.mp3", false);
        
        // Display the book title and chapter
        if (!this.currentChapter.equals(TITLE_PAGE)) {
            Color black = new Color(0, 0, 0);
            this.appController.displayText(this.name, bookFile.title, this.titleRow, this.leftPageStartingColumn, black, FontStyle.BOLD);
            this.appController.displayText(this.name, this.currentChapter, this.titleRow, this.rightPageEndingColumn - this.currentChapter.length() + 1, black, FontStyle.BOLD);
        }

        // Display the story page
        Chapter chapter = bookFile.chapters.get(this.currentChapter);
        List<String> page = chapter.pages.get(this.currentPage);
        displayPage(page);
        
        // Display the control page
        if (this.currentChapter.equals(TITLE_PAGE)) {
            // Display book information
            this.appController.displayText(this.name, bookFile.title, this.startingRow, this.rightPageStartingColumn);
            this.appController.displayText(this.name, "by " + bookFile.author, this.startingRow + 1, this.rightPageStartingColumn);
            this.appController.displayText(this.name, "Last Updated: " + bookFile.updateDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.getDefault())), this.startingRow + 3, this.rightPageStartingColumn);
        } else {
            // TODO - Display the map and basic book controls
        }
        
        // Previous page button
        if (this.currentPage > 0) {
            String buttonText = "< Previous";
            int buttonColumn = this.leftPageStartingColumn - 1;
            this.appController.displayButton(this.name, PREVIOUS_PAGE, buttonText, this.buttonRow, buttonColumn, this);
        }
        
        // Next page button
        if ((this.currentChapter.equals(TITLE_PAGE)) || (this.currentChapter.equals(INTRODUCTION)) || (this.currentPage < (chapter.pages.size() - 1))) {
            String buttonText = "Next >";
            int buttonColumns = appController.getButtonColumns(buttonText);
            int buttonColumn = this.rightPageEndingColumn - buttonColumns + 1;
            this.appController.displayButton(this.name, NEXT_PAGE, buttonText, this.buttonRow, buttonColumn, this);
        }
    }
    
    public void displayTextOnPage(String text, Integer row, Integer column, Color color, int style) {
        int startingColumn;
        int endingColumn;
        if (this.currentDisplayPage == SECOND_PAGE) {
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
            this.appController.displayText(this.name, lineText, realRow, realColumn, color, style);
            remainingText = remainingText.trim();
            realRow++;
            realColumn = startingColumn;
            this.textRow = this.textRow + 1;
        }
    }
    
    public void displayPage(List<String> page) {
        System.out.println("Book: displayPage");
        
        this.currentDisplayPage = FIRST_PAGE;
        this.textRow = 1;
        this.textColor = new Color(0, 0, 0);
        this.textStyle = FontStyle.NORMAL;
        
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
                            System.out.println("Book: displayPage: Unsupported tag: ***" + questControlName + "***");
                        } else {
                            System.out.println("Book: displayPage: Executing tag " + questControlName);
                            newText = control.onExecute(questControlTag);
                            i = i + questControlTag.length() - 1;
                        }
                    }
                }
                storyText = storyText + newText;
            }
            
            if (storyText.length() > 0) {
                this.displayTextOnPage(storyText, this.textRow, 1, this.textColor, this.textStyle);
            } else if (pageLine.length() == 0) {
                this.textRow = this.textRow + 1;
            }
        }
    }
    
}
