package quest;

import app.ApplicationController;
import app.Color;

/**
 *
 * @author repp
 */
public class Cheats extends app.ApplicationView {
    
    public static final String ENTERED_CHEAT = "entered-cheat";
    public ApplicationController appController;
    
    public Cheats(String name) {
        super(name);
        this.backgroundColor = new Color(255, 255, 255);
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("Cheats: onEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        switch(eventName) {
            case ENTERED_CHEAT -> {
                if (!eventValue.equals("")) {
                    this.appController.displayMessageBox("Cheat code not recognized!  Try again.");
                }
            }
            default -> System.err.println("Cheats: onEvent: Unsupported event");
        }
    }
    
    @Override
    public void onLoad(ApplicationController appController) {
        System.out.println("Cheats: onLoad");
        
        this.appController = appController;
        this.appController.displayInputField(this.name, ENTERED_CHEAT, "Enter code:", 10, 3, 5, this);
    }

}
