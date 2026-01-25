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
import app.Utility;
import app.dialog.BaseDialog;
import app.node.BaseNode;
import app.node.Grid;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import app.view.Animation;

/**
 * Available font families:
 * - Lato
 * - Minecraft
 * - Roboto
 * - Roboto Black
 * - Roboto Light
 * - Roboto Medium
 * - Roboto Mono
 * - Roboto Mono Medium
 * - Roboto Mono Thin
 * - Roboto Mono Light
 * - Roboto Thin
 * - 
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
    public abstract void displayOverlay(String viewName, String name, app.Color color, Integer startRow, Integer startColumn, Integer endRow, Integer endColumn, Integer transparency, Boolean invert);
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
    
    public static void main(String[] args) {
        // Configure the JUL logger:
        //   %1$tF %1$tT: Date and time
        //   %4$s: Log level (e.g., INFO)
        //   %2$s: Class and method name
        //   %5$s%n: The log message and a newline
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tT %4$s %2$s: %5$s%n");
        
        //logger.log(Level.INFO, "ApplicationController: main: args=" + Arrays.toString(args));
        logger.log(Level.INFO, "Entered: args={0}", Arrays.toString(args));
        
        // Instance the application properties
        Properties props = loadProperties();
        if (props == null) {
            logger.log(Level.SEVERE, "Unable to load the properties file");
            return;
        }
        
        // Instance the application controller
        String guiProperty = props.getProperty("app.gui");
        BaseController.appController = resolveController(args, guiProperty);
        if (BaseController.appController == null) {
            logger.log(Level.SEVERE, "Unable to construct the application controller");
            return;
        }
        
        // Instance the application view
        logger.log(Level.INFO, "Instancing the application view for app class: {0}", props.getProperty("app.class"));
        String configAppClass = props.getProperty("app.class");
        String configSplashClass = props.getProperty("app.splash");
        
        Class<?> clazz;
        String appName = getAppName(configAppClass);

        // Initialize the splash view
        BaseView splashView = null;
        if (configSplashClass != null) {
            logger.log(Level.INFO, "Intializing the splash view");
            splashView = (BaseView) Utility.instance(configSplashClass, appName);
            clazz = splashView.getClass();
            if (!BaseView.class.isAssignableFrom(clazz)) {
                logger.log(Level.SEVERE, "Splash view is not an application view: {0}", clazz.getName());
                return;
            }
        }

        // Initialize the main application view
        logger.log(Level.INFO, "Intializing the main view");
        BaseView appView = (BaseView) Utility.instance(configAppClass, appName);
        clazz = appView.getClass();
        if (!BaseView.class.isAssignableFrom(clazz)) {
            logger.log(Level.SEVERE, "Main view is not an application view: {0}", clazz.getName());
            return;
        }
        appView.className = configAppClass;
        appView.iconFileName = props.getProperty("app.icon");
        
        // Display the application
        logger.log(Level.INFO, "Displaying the application");
        BaseController.appController.open(splashView, appView);
        
        // Dispose the application
        logger.log(Level.INFO, "Closing");
        BaseController.appController.close();
        logger.log(Level.INFO, "System exit");
    }
    
    public static String getAppName(String appClassName) {
        logger.log(Level.INFO, "Entered: appClassName={0}", appClassName);
        
        String appName = "";
        
        Class<?> appClass;
        try {
            appClass = Class.forName(appClassName);
            String className = appClass.getSimpleName();
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "A critical error occurred", e);
        }

        return appName;
    }
    
    // Order of primacy for determining which GUI is used:
    // 1) Passed in argument
    // 2) Immediate class (if not generic)
    // 3) app.properties file
    public static BaseController resolveController(String[] args, String guiProperty) {
        logger.log(Level.INFO, "Entered: args={0}, guiProperty={1}", new Object[]{Arrays.toString(args), guiProperty});
        
        BaseController controller = null;
        
        // Evaluate the passed in GUI parameter
        String gui = BaseController.getGUIFromArgs(args);
        
        // Evaluate the properties file
        if ((gui == null) || (gui.equals(""))) {
            gui = guiProperty;
            if ((gui != null) && (!gui.equals(""))) {
                logger.log(Level.INFO, "Using controller provided by properties file");
            }
        }

        // Resolve the controller class
        if ((gui != null) && (!gui.equals(""))) {
            logger.log(Level.INFO, "Instancing controller for controller: {0}", gui);
            Class<?> controllerClass = null;
            try {
                controllerClass = Class.forName(gui);
            } catch (ClassNotFoundException e) {
                logger.log(Level.SEVERE, "A critical error occurred", e);
            }
            if (controllerClass == null) {
                logger.log(Level.SEVERE, "Unsupported controller: {0}", gui);
            } else {
                controller = (BaseController) Utility.instance(controllerClass.getName());
            }
        } else {
            logger.log(Level.SEVERE, "Could not resolve controller");
        }
        
        return controller;
    }
    
    // Load the properties file
    public static Properties loadProperties() {
        logger.log(Level.INFO, "Entered");
        
        Properties props = new Properties();

        try (InputStream input = BaseController.class.getClassLoader().getResourceAsStream(BaseController.PROPERTIES_FILE)) {
            if (input == null) {
                logger.log(Level.SEVERE, "Unable to find properties file: {0}", BaseController.PROPERTIES_FILE);
                return null;
            }
            props.load(input);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "A critical error occurred", e);
            return null;
        } 
        
        return props;
    }
    
    public static String getGUIFromArgs(String[] args) {
        logger.log(Level.INFO, "Entered: args={0}", Arrays.toString(args));
        
        String gui = null;
        if (args.length > 0) {
            gui = args[0];
            logger.log(Level.INFO, "Read argument: {0}", gui);
        }
        return gui;
    }
    
    public static String getGUIFromThisClass() {
        logger.log(Level.INFO, "Entered");
        
        String gui = null;
        
        // TODO - This will only return ApplicationController
        String thisClassName = new Throwable().getStackTrace()[0].getClassName();
        
        try {
            Class<?> thisClass = Class.forName(thisClassName);
            Field field;
            try {
                field = thisClass.getField(BaseController.NAME_PROPERTY);
            } catch (NoSuchFieldException | SecurityException e) {
                logger.log(Level.SEVERE, "A critical error occurred", e);
                return null;
            }
            try { 
                gui = (String) field.get(null);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.log(Level.SEVERE, "A critical error occurred", e);
            }
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "A critical error occurred", e);
        }
        
        return gui;
    }
    
}
