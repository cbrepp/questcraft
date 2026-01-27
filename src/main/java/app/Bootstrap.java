package app;

import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.view.BaseView;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;

/**
 *
 * @author repp
 */
public class Bootstrap {
    
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
        String gui = getGUIFromArgs(args);
        
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
