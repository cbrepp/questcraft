package app.view;

import app.Color;
import app.EventListener;
import app.controller.BaseController;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author repp
 */
public class BaseView implements EventListener {
    
    public Boolean addTextArea;
    public Color backgroundColor;
    public String backgroundImage;
    public String className;
    public List<String> emojis;
    public String iconFileName;
    public Boolean isSplash;
    public String name;
    public int timeoutSeconds;
    public String version;
    public LinkedHashMap<String, List<BaseView>> eventListenerMap;
    
    public BaseView(String name) {
        this.addTextArea = true;
        this.emojis = new ArrayList();
        this.eventListenerMap = new LinkedHashMap<>();
        this.isSplash = false;
        this.name = name;
        this.timeoutSeconds = 0;
    }
    
    public void addListener(String eventName, BaseView listener) {
        System.out.println("ApplicationView: addListener: eventName=" + eventName + ", listener=" + listener);
        List<BaseView> eventListeners = this.eventListenerMap.get(eventName);
        if (eventListeners == null) {
            eventListeners = new ArrayList<>();
            this.eventListenerMap.put(eventName, eventListeners);
        }
        eventListeners.add(listener);
    }
    
    public void onDisplay(BaseController appController) {
        System.out.println("ApplicationView: onDisplay: Unimplemented");
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("ApplicationView: onEvent: Unimplemented: eventName=" + eventName + ", eventValue=" + eventValue);
    }
    
    public void onLoad(BaseController appController) {
        System.out.println("ApplicationView: onLoad: Unimplemented");
    }
    
    public void onSelected(BaseController appController) {
        System.out.println("ApplicationView: onSelected: Unimplemented for " + this.name);
    }
    
    public void onUnselected(BaseController appController) {
        System.out.println("ApplicationView: onUnselected: Unimplemented for " + this.name);
    }
    
    public void publishEvent(String eventName, Object eventValue) {
        System.out.println("ApplicationView: publishEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        List<BaseView> eventListeners = this.eventListenerMap.get(eventName);
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
    
    public void removeListener(String eventName, BaseView listener) {
        System.out.println("ApplicationView: removeListener: eventName=" + eventName + ", listener=" + listener);
        List<BaseView> eventListeners = this.eventListenerMap.get(eventName);
        if (eventListeners == null) {
            return;
        }
        eventListeners.remove(listener);
    }
    
}
