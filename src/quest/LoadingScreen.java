package quest;

import app.ApplicationController;
import app.ApplicationView;
import org.eclipse.swt.SWT;

/**
 *
 * @author repp
 */
public class LoadingScreen extends app.ApplicationView {
    
    public final static String TIMER_EVENT = "LOADING_COMPLETE";
    
    public ApplicationController appController;
    public ApplicationView view;
    public String gifFileName;
    public String soundFileName;
    
    public LoadingScreen() {
        super();
        this.backgroundColor = SWT.COLOR_BLACK;
    }
    
    public LoadingScreen(ApplicationView view, String gifFileName, String soundFileName) {
        this();
        this.view = view;
        this.gifFileName = gifFileName;
        this.soundFileName = soundFileName;
    }
    
    @Override
    public void onDisplay(ApplicationController appController, ApplicationView parentView) {
        System.out.println("LoadingScreen: onDisplay: appController=" + appController + ", parentView=" + parentView);
        this.appController = appController;
        int nextRow = appController.displayGif(this.gifFileName, 3, 12);
        appController.displayText("Loading...", nextRow, 12, SWT.COLOR_WHITE);
        app.Utility.playSound(this.soundFileName, true);
        appController.setTimer(TIMER_EVENT, 3, this);
    }
    
    @Override
    public void handleEvent(String eventName, String eventValue) {
        System.out.println("LoadingScreen: handleEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        switch(eventName) {
            case TIMER_EVENT -> {
                this.appController.displayView(this.view);
            }
            default -> System.err.println("Library: handleEvent: Unsupported event");
        }
    }

}
