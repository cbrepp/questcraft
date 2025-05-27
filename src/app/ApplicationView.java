package app;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author repp
 */
public class ApplicationView {
    
    public Boolean addTextArea;
    public Color backgroundColor;
    public String backgroundImage;
    public String className;
    public String iconFileName;
    public Boolean isSplash;
    public String name;
    public int timeoutSeconds;
    public String version;
    public LinkedHashMap<String, List<ApplicationView>> eventListenerMap;
    
    public ApplicationView(String name) {
        this.addTextArea = true;
        this.backgroundColor = null;
        this.backgroundImage = null;
        this.eventListenerMap = new LinkedHashMap<>();
        this.isSplash = false;
        this.name = name;
        this.timeoutSeconds = 0;
    }
    
    public void addListener(String eventName, ApplicationView listener) {
        System.out.println("ApplicationView: addListener: eventName=" + eventName + ", listener=" + listener);
        List<ApplicationView> eventListeners = this.eventListenerMap.get(eventName);
        if (eventListeners == null) {
            eventListeners = new ArrayList<>();
            this.eventListenerMap.put(eventName, eventListeners);
        }
        eventListeners.add(listener);
    }
    
    public void onDisplay(ApplicationController appController) {
        System.out.println("ApplicationView: onDisplay: Unimplemented");
    }
    
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("ApplicationView: onEvent: Unimplemented: eventName=" + eventName + ", eventValue=" + eventValue);
    }
    
    public void onLoad(ApplicationController appController) {
        System.out.println("ApplicationView: onLoad: Unimplemented");
    }
    
    public void publishEvent(String eventName, Object eventValue) {
        System.out.println("ApplicationView: publishEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        List<ApplicationView> eventListeners = this.eventListenerMap.get(eventName);
        if (eventListeners == null) {
            System.out.println("ApplicationView: publishEvent: No listeners!");
            return;
        }
        eventListeners.forEach(
            eventListener -> {
                System.out.println("ApplicationView: Publishing event for " + eventListener.name);
                eventListener.onEvent(eventName, eventValue);
            }
        );
    }
    
    public void removeListener(String eventName, ApplicationView listener) {
        System.out.println("ApplicationView: removeListener: eventName=" + eventName + ", listener=" + listener);
        List<ApplicationView> eventListeners = this.eventListenerMap.get(eventName);
        if (eventListeners == null) {
            return;
        }
        eventListeners.remove(listener);
    }
    
}
