package quest;

import app.ApplicationController;
import app.ApplicationView;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import javax.sound.sampled.*;
import org.eclipse.swt.SWT;

public class Book extends app.ApplicationView {
    public final static String SUPPORTED_TAGS = "color";
    public final static String TIMER_EVENT = "LOADING_COMPLETE";
    public final static String TURN_PAGE = "turn-page";
    
    public ApplicationController appController;
    public ApplicationView parentView;
    public BookFile bookFile;
    
    public String author;
    public String basicStoryChoices;
    public String chapter;
    public String controlChoices;
    public Integer controlChoicesColumn;
    public Integer controlChoicesRow;
    public String credits;
    public TextCursor cursor = new TextCursor();
    public Clip deathHitSound;
    public String difficulty;
    public MapDirections directions = new MapDirections();
    public Integer doorX;
    public Integer doorY;
    public Integer experiencePoints;
    public Integer firstRow;
    public Clip heavyHitSound;
    public String highScores;
    public Integer hitPoints;
    public Clip hitSound;
    public String inventory;
    public Clip inventorySound;
    public Boolean isOver;
    public Boolean isRightHanded;
    public Integer lastImageX;
    public Integer lastImageY;
    public Integer lastLevel;
    public Integer lastRow;
    public Integer leftPageStartingColumn;
    public Integer leftPageEndingColumn;
    public Integer level;
    public Integer levelSize;
    public Integer magicPoints;
    public Integer maxPageLength;
    public String openingGIF;
    public String openingMusic;
    public Integer pageNumber;
    public Clip pageTurnSound;
    public String player;
    public Integer playerDirection;
    public Integer playerLastX;
    public Integer playerLastY;
    public String playerMode;
    public Integer playerX;
    public Integer playerY;
    public Clip pointsSound;
    public Integer rightPageStartingColumn;
    public Integer rightPageEndingColumn;
    public Integer score;
    public String secretChoices;
    public Integer startingRow;
    public String story;
    public String storyChoices;
    public Integer storyChoicesColumn;
    public Integer storyChoicesRow;
    public String storyFile;
    public Clip storySound;
    public String title;
    public Integer titleRow;
    public String twin;
    public String version;
    public Clip victorySound;
    
    public app.Color textColor;
    public int textRow;

    public Book() {
        super();
        
        this.chapter = "";
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
    }
    
    @Override
    public LinkedHashMap<String, ApplicationView> getChildren() {
        return null;
    }
    
    @Override
    public void onDisplay(ApplicationController appController, ApplicationView parentView) {
        System.out.println("Book: onDisplay");
        this.appController = appController;
        this.parentView = parentView;
        
        // Loading screen
        int nextRow = appController.displayGif(this.bookFile.animationFileName, 3, 12);
        appController.displayText("Loading...", nextRow, 12, SWT.COLOR_WHITE);
        app.Utility.playSound(this.bookFile.musicFileName, true);
        appController.setTimer(TIMER_EVENT, 3, this);
    }
    
    @Override
    public void onLoad(ApplicationController appController, ApplicationView parentView) {}
    
    @Override
    public void handleEvent(String eventName, String eventValue) {
        System.out.println("Book: handleEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        switch(eventName) {
            case TURN_PAGE -> this.appController.displayMessageBox("Not yet implemented!!!");
            case TIMER_EVENT -> this.displayTitlePage();
            default -> System.err.println("Book: handleEvent: Unsupported event");
        }
    }
    
    public void displayTitlePage() {
        System.out.println("Book: displayTitlePage");
        
        this.appController.clearScreen();
        this.backgroundImage = "/assets/images/book.png";
        this.backgroundColor = SWT.COLOR_BLACK;
        this.appController.setBackground(this.backgroundColor, this.backgroundImage);
        app.Utility.playSound("/assets/sounds/turn-page.mp3", false);
        displayScene(bookFile.titlePage);

        // TODO - Add wrapper method in this class for displayText() that includes a parameter for story page versus control page
        // TODO - Display book metadata on the control page
        
        // TODO - Make a base story element class that the different story elements can implement
        // TODO - The instance could be provided with the arguments and the book instance to do the needful
        // TODO - A collection of mapped story elements could be used to validate each tag as to whether it's a story element tag or not
        
        // Calculate book margins
        int parentColumns = this.appController.getTextColumns();
        int parentRows = this.appController.getTextRows();
        this.titleRow = 2;
        this.startingRow = 4;
        this.leftPageStartingColumn = (int) (parentColumns * 0.1) + 1;
        this.leftPageEndingColumn = (int) (parentColumns * 0.49) + 1;
        this.rightPageStartingColumn = (int) (parentColumns * 0.52) + 1;
        this.rightPageEndingColumn = (int) (parentColumns * 0.92) + 1;
        
        // Display book information
        this.appController.displayText(bookFile.title, this.startingRow, this.rightPageStartingColumn);
        this.appController.displayText("by " + bookFile.author, this.startingRow + 1, this.rightPageStartingColumn);
        this.appController.displayText("Last Updated: " + bookFile.updateDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.getDefault())), this.startingRow + 3, this.rightPageStartingColumn);

        // Turn Page button
        String buttonText = "Turn Page -->";
        int buttonColumns = appController.getButtonColumns(buttonText);
        int buttonRows = appController.getButtonRows();
        int buttonColumn = parentColumns - buttonColumns - 1 - 7;   // TODO - Factor in book margins
        int buttonRow = parentRows - buttonRows;   // TODO - Factor in book margins
        this.appController.displayButton(TURN_PAGE, buttonText, buttonRow, buttonColumn, this);
    }
    
    public void displayScene(List<String> scene) {
        System.out.println("Book: displayScene");
        
        this.textRow = 4;
        this.textColor = new app.Color(0, 0, 0);
        
        for (String storyElement : scene) {
            String storyText = "";
            for (int i = 0; i < storyElement.length(); i++) {
                char character = storyElement.charAt(i);
                if (character == '<') {
                    String tag = getTag(storyElement, i);
                    if (tag != null) {
                        handleTag(tag);
                        i = i + tag.length() - 1;
                        continue;
                    }
                }
                storyText = storyText + character;
            }
            
            if (storyText.length() > 0) {
                this.appController.displayText(storyElement, this.textRow, 12, this.textColor);
                this.textRow = this.textRow + 1;
            } else if (storyElement.length() == 0) {
                this.textRow = this.textRow + 1;
            }
        }
    }
    
    public void handleTag(String tag) {
        System.out.println("Book: handleTag: tag=" + tag);
        String[] parts = tag.substring(1).toLowerCase().split(" ");
        if (parts[0].equals("color")) {
            int red = Integer.parseInt(getTagArgument(tag, 1));
            int green = Integer.parseInt(getTagArgument(tag, 2));
            int blue = Integer.parseInt(getTagArgument(tag, 3));
            System.out.println("Book: handleTag: Text color=" + red + "+" + green + "+" + blue);
            this.textColor = new app.Color(red, green, blue);
        } else {
            System.out.println("Book: handleTag: Unsupported tag: ***" + parts[0] + "***");
        }
    }
    
    public static String getTagArgument(String tag, int arg) {
        System.out.println("Book: getTagArgument: tag=" + tag + ", arg=" + arg);
        String[] tagParts = tag.split(" ");
        String[] argParts = tagParts[1].substring(0, tagParts[1].length() - 1).split("\\+");    // The plus character needs to be escaped
        if (argParts.length < arg) {
            System.out.println("Book: getTagArgument: No value");
            return null;
        }
        String value = argParts[arg - 1];
        System.out.println("Book: getTagArgument: value=" + value);
        return value;
    }
    
    public static String getTag(String text, int index) {
        String[] parts = text.substring(1).toLowerCase().split(" ");
        if ((parts.length > 0) && (("," + SUPPORTED_TAGS + ",").contains("," + parts[0] + ","))) {
            int lastChar = text.indexOf('>');
            if (lastChar > -1) {
                return text.substring(0, lastChar + 1).toLowerCase();
            }
        }
        return null;
    }
    
}
