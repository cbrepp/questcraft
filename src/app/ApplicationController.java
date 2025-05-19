package app;

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

import quest.SplashScreen;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author repp
 */
public abstract class ApplicationController {

    public static final String NAME_PROPERTY = "NAME";
    public static final String PROPERTIES_FILE = "assets/app.properties";
    public static String NAME;
    
    public abstract void addDesigner(String viewName);
    public abstract void addView(ApplicationView view);
    public abstract void close();
    public abstract void displayApplication(ApplicationView view);
    public abstract void displayView(ApplicationView view);
    public abstract void initialize(ApplicationView view);
    public abstract void clearScreen(String name);
    public abstract void displayMessageBox(String text);
    public abstract void displayText(String viewName, String text, Integer row, Integer column);
    public abstract void displayText(String viewName, String text, Integer row, Integer column, int color);
    public abstract void displayText(String viewName, String text, Integer row, Integer column, Color color);
    public abstract void displayButton(String viewName, String name, String text, int row, int column, ApplicationView listener);
    public abstract void displayOpenFileButton(String viewName, String name, String text, int row, int column, ApplicationView listener);
    public abstract int displayImage(String viewName, String fileName, int row, int column);
    public abstract int displayGif(String viewName, String fileName, int row, int column);
    public abstract void setTimer(String name, int seconds, ApplicationView listener);
    public abstract int getTextColumns();
    public abstract int getTextRows();
    public abstract int getColumns(String fileName);
    public abstract int getRows(String fileName);
    public abstract int getButtonColumns(String buttonText);
    public abstract int getButtonRows();
    public abstract void setBackgroundImage(String viewName, String imageFileName);
    
    public static void main(String[] args) {
        System.out.println("ApplicationController: main: args=" + Arrays.toString(args));
        
        // Instance the application properties
        Properties props = loadProperties();
        if (props == null) {
            System.err.println("ApplicationController: main: Unable to load the properties file");
            return;
        }
        
        // Instance the application controller
        String guiProperty = props.getProperty("app.gui");
        ApplicationController appController = resolveController(args, guiProperty);
        if (appController == null) {
            System.err.println("ApplicationController: main: Unable to construct application controller");
            return;
        }
        
        // Instance the application view
        System.out.println("ApplicationController: Instancing application view for " + props.getProperty("app.class"));
        String configAppClass = props.getProperty("app.class");
        String configAppVersion = props.getProperty("app.version");
        String configSplashClass = props.getProperty("app.splash");
        
        Class<?> clazz;
        String appName = getAppName(configAppClass, configAppVersion);
        
        // Initialize the splash view
        if (configSplashClass != null) {
            ApplicationView splashView = (ApplicationView) Utility.instance(configSplashClass, appName);
            clazz = splashView.getClass();
            if (!ApplicationView.class.isAssignableFrom(clazz)) {
                System.err.println("ApplicationController: main: " + clazz.getName() + " is not an application view");
                return;
            }
            appController.initialize(splashView);
            System.out.println("ApplicationController: main: Displaying splash view");
            appController.displayApplication(splashView);
        }

        // Initialize the main application view
        ApplicationView appView = (ApplicationView) Utility.instance(configAppClass, appName);
        clazz = appView.getClass();
        if (!ApplicationView.class.isAssignableFrom(clazz)) {
            System.err.println("ApplicationController: main: " + clazz.getName() + " is not an application view");
            return;
        }
        appView.className = configAppClass;
        appView.iconFileName = props.getProperty("app.icon");
        appView.name = 
        appView.version = configAppVersion;
        appController.initialize(appView);
        System.out.println("ApplicationController: main: Displaying main application view");
        appController.displayApplication(appView);
        System.out.println("ApplicationController: main: Closing");
        appController.close();
        System.out.println("ApplicationController: main: System exit");
    }
    
    public static String getAppName(String appClassName, String appVersion) {
        System.out.println("ApplicationController: getAppName: appClassName=" + appClassName + ", appVersion=" + appVersion);
        
        String appName = "";
        
        Class<?> appClass;
        try {
            appClass = Class.forName(appClassName);
            String className = appClass.getSimpleName();
            appName = className + " " + appVersion;
        } catch (ClassNotFoundException e) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("ApplicationController: getAppName: " + e.toString());
        }

        return appName;
    }
    
    // Order of primacy for determining which GUI is used:
    // 1) Passed in argument
    // 2) Immediate class (if not generic)
    // 3) app.properties file
    public static ApplicationController resolveController(String[] args, String guiProperty) {
        System.out.println("ApplicationController: resolveController: args=" + Arrays.toString(args) + ", guiProperty=" + guiProperty);
        
        ApplicationController controller = null;
        
        // Evaluate the passed in GUI parameter
        String gui = ApplicationController.getGUIFromArgs(args);
        
        // Evaluate the properties file
        if ((gui == null) || (gui.equals(""))) {
            gui = guiProperty;
            if ((gui != null) && (!gui.equals(""))) {
                System.out.println("ApplicationController: resolveController: Using GUI provided by properties file");
            }
        }

        // Resolve the controller class
        if ((gui != null) && (!gui.equals(""))) {
            System.out.println("ApplicationController: resolveController: Instancing controller for " + gui);
            Class<?> controllerClass = null;
            try {
                controllerClass = Class.forName(gui);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (controllerClass == null) {
                System.err.println("ApplicationController: resolveController: Unsupported GUI: " + gui);
            } else {
                controller = (ApplicationController) Utility.instance(controllerClass.getName());
            }
        } else {
            System.err.println("ApplicationController: resolveController: Could not resolve GUI");
        }
        
        return controller;
    }
    
    // Load the properties file
    public static Properties loadProperties() {
        System.out.println("ApplicationController: loadProperties");
        
        Properties props = new Properties();

        try (InputStream input = ApplicationController.class.getClassLoader().getResourceAsStream(ApplicationController.PROPERTIES_FILE)) {
            if (input == null) {
                System.err.println("ApplicationController: loadProperties: Unable to find " + ApplicationController.PROPERTIES_FILE);
                return null;
            }
            props.load(input);
        } catch (IOException e) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("ApplicationController: loadProperties: " + e.toString());
            return null;
        } 
        
        return props;
    }
    
    public static String getGUIFromArgs(String[] args) {
        System.out.println("ApplicationController: getGUIFromArgs: args=" + Arrays.toString(args));
        
        String gui = null;
        if (args.length > 0) {
            gui = args[0];
            System.out.println("ApplicationController: getGUIFromArgs: Read argument: " + gui);
        }
        return gui;
    }
    
    public static String getGUIFromThisClass() {
        System.out.println("ApplicationController: getGUIFromThisClass");
        
        String gui = null;
        
        // TODO - This will only return ApplicationController
        String thisClassName = new Throwable().getStackTrace()[0].getClassName();
        
        try {
            Class<?> thisClass = Class.forName(thisClassName);
            Field field;
            try {
                field = thisClass.getField(ApplicationController.NAME_PROPERTY);
            } catch (NoSuchFieldException | SecurityException e) {
                Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, e);
                System.err.println("ApplicationController: getGUIFromThisClass: " + e.toString());
                return null;
            }
            try { 
                gui = (String) field.get(null);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, e);
                System.err.println("ApplicationController: getGUIFromThisClass: " + e.toString());
            }
        } catch (ClassNotFoundException e) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("ApplicationController: getGUIFromThisClass: " + e.toString());
        }
        
        return gui;
    }
    
}
