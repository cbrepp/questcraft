package app.controller;

// JLayer - https://github.com/wkpark/JLayer/blob/master/LICENSE.txt

/* TODO:
 * Add json to the properties class for printing "Wayne Chung Enterprises" and "presents" on given row, column positions
 * Finish research on overlay and use that for the splash screen so "Wayne Chung Enterprises presents" is dynamically added
 * Close splash screen when audio is done playing
 * Figure out relative path for image and sound files, java.util.Scanner could help troubleshoot
 * Clean up audio... use mp3 if that's available and remove ogg file and library
 * Figure out text dimensions for the book image
 * Finish styledtext support by mapping each character that needs stylerange and reapplying
 * Figure out maximizing window and handling book image
 * Clean up code!
 * Continue with porting game to java, making all in-game data part of the Book object
 * Figure out license references
 */

import app.view.BaseView;
import app.Coordinates;
import app.EventListener;
import app.Layout;
import app.dialog.BaseDialog;
import app.node.BaseNode;
import java.util.List;
import java.util.logging.Logger;
import app.view.Animation;

/**
 * 
 * @author repp
 */
public abstract class BaseController {

    public static final Double DEFAULT_PIXEL_SIZE = 14.0;
    public static final String EMOJI_SHEET = "/assets/images/sheet_google_64.png";
    public static final String EMOJI_SHEET_JSON = "/assets/json/emoji.json";
    public static final Double EMOJI_SHEET_SIZE = 64.0;
    public static final String NAME_PROPERTY = "NAME";
    public static final String NODE_PUBLISHED_EVENT = "node published";
    public static final String NODE_TRANSITIONED_EVENT = "node transitioned";
    public static final String PROPERTIES_FILE = "assets/app.properties";
    public static String NAME;
    public static BaseController appController;
    public static final Logger logger = Logger.getLogger(BaseController.class.getName());
    
    public abstract void setDelegate(Object delegate);
    public abstract void addDesigner(String viewName);
    public abstract void selectTab(String viewName);
    public abstract void refreshTabLabel(String viewName);
    public abstract void removeTab(String viewName);
    public abstract void addView(BaseView view);
    public abstract void addView(BaseView view, Boolean isParent, int index, Boolean isRefresh);
    public abstract Integer getTabIndex(String viewName);
    public abstract void close();
    public abstract void open(BaseView splashView, BaseView mainView);
    public abstract void displayView(BaseView view);
    public abstract void displayView(String viewName);
    //public abstract void displayOverlay(String viewName, String name, app.Color color, Integer startRow, Integer startColumn, Integer endRow, Integer endColumn, Integer transparency, Boolean invert);
    public abstract void clearScreen(String name);
    public abstract void removeNode(String viewName, String nodeName);
    //public abstract void displayMessageBox(String title, String text, Icon level, List<String> emojis);
    //public abstract void displayText(String viewName, String text, Integer row, Integer column);
    //public abstract void displayText(String viewName, String text, Integer row, Integer column, Color color);
    //public abstract void displayText(String viewName, String text, Integer row, Integer column, Color color, int style);
    public abstract void displayGrid(String viewName, app.node.Grid grid, Layout layout);
    public abstract void changeNode(String viewName, BaseNode node, Layout layout);
    public abstract void addNode(String viewName, String parentName, BaseNode node, Layout layout);
    public abstract void newDialog(BaseDialog dialog);
    //public abstract void displayLink(String viewName, String name, String linkText, int row, int column, int length, EventListener listener);
    //public abstract void displayButton(String viewName, String name, String text, Integer row, Integer column, Integer endRow, Integer endColumn, Boolean isMonospace, String fontName, Boolean glow, EventListener listener);
    //public abstract void displayOpenFileButton(String viewName, String name, String text, Integer row, Integer column, Integer endRow, Integer endColumn, Boolean isMonospace, String fontName, Boolean glow, EventListener listener);
    //public abstract int displayImage(String viewName, String name, String fileName, int row, int column, Boolean fillParent);
    //public abstract void displayFloatingText(String viewName, String name, String text, Integer startRow, Integer startColumn, Integer endRow, Integer endColumn, app.Color fontColor, Integer fontSize, Integer fontStyle, String fontName);
    //public abstract void displayInputField(String viewName, String name, String label, int length, int row, int column, String initValue, Boolean addButton, Boolean isMonospace, Boolean isUpperCase, Boolean isMultiUse, EventListener listener);
    public abstract void displayValidatedInputField(String viewName, String name, List<String> values, int row, int startColumn, int endColumn, Layout layout, EventListener listener, Boolean allowRepeatClicks);
    public abstract int displayGif(String viewName, String fileName, int row, int column);
    public abstract void setTimer(String name, double seconds, EventListener listener);
    public abstract void removeTimer(String name);
    public abstract Coordinates getDimensions(String imageFileName);
    public abstract int getTextColumns();
    public abstract int getTextRows();
    public abstract int getColumns(String fileName);
    public abstract int getRows(String fileName);
    public abstract int getButtonColumns(String buttonText);
    public abstract int getButtonRows();
    public abstract void addAnimation(String viewName, String name, int row, int startColumn, String backgroundImageFileName, List<String> imageFiles, double animationDelay, Animation listener);
    public abstract void playSound(String fileName, Boolean isLoop);
    public abstract void stopSound(String fileName, Boolean removeAudioPlayer);
    public abstract void stopAllSounds();
    public abstract void pauseAllSounds();
    public abstract void unpauseAllSounds();
    public abstract void sendToFront(String viewName, String name);
    public abstract void sendToBack(String viewName, String name);
    public abstract void refreshView(String viewName);
    public abstract void loadEmojiData();

}
