package app;

import java.util.LinkedHashMap;

/**
 *
 * @author repp
 */
public abstract class ApplicationView {
    
    public Integer backgroundColor;
    public String backgroundImage;
    public String className;
    public String iconFileName;
    public String name;
    public String splashImageFileName;
    public int splashSeconds;
    public String splashSoundFileName;
    public String version;
    
    public ApplicationView() {
        this.backgroundColor = null;
        this.backgroundImage = null;
    }
    
    public abstract void onDisplay(ApplicationController appController, ApplicationView parentView);

    public LinkedHashMap<String, ApplicationView> getChildren() {
        return null;
    }
    
    public void handleEvent(String eventName, String eventValue) {}
    
}
