package quest;

import app.ApplicationController;
import app.Color;
import app.FontStyle;

/**
 *
 * @author repp
 */
public class SplashScreen extends app.ApplicationView {
    
    public ApplicationController appController;
    
    public SplashScreen(String name) {
        super(name);
        this.isSplash = true;
        this.backgroundImage = "/assets/images/wayne-chung.jpg";
    }
    
    @Override
    public void onLoad(ApplicationController appController) {
        System.out.println("SplashScreen: onLoad");
        this.appController = appController;
        Color black = new Color(0, 0, 0);
        appController.displayText(this.name, "Wayne Chung Enterprises", 7, 2, black, FontStyle.BOLD);
        appController.displayText(this.name, "presents", 9, 9, black, FontStyle.BOLD);
        app.Utility.playSound("/assets/sounds/wayne-chung.wav", false);
    }
    
    @Override
    public void onDisplay(ApplicationController appController) {
        System.out.println("SplashScreen: onDisplay");
        appController.setTimer("timeout", 4, this);
    }

    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("SplashScreen: onEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        switch(eventName) {
            case "timeout" -> {
                System.out.println("SplashScreen: onEvent: Closing window after " + eventValue + " seconds");
                this.appController.close();
            }
            default -> System.err.println("SplashScreen: onEvent: Unsupported event");
        }
    }
    
}
