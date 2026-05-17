package app.view;

import app.EventListener;
import app.color.RGBColor;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.node.Pane;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author repp
 */
public class BaseView extends Pane implements EventListener {
    
    public Boolean addTextArea;
    public String backgroundImage;
    public String className;
    public List<String> emojis;
    public String iconFileName;
    public Boolean isSplash;
    public LinkedHashMap<String, List<BaseView>> eventListenerMap;
    
    public BaseView(String name) {
        super(name);
        this.addTextArea = true;
        this.borderWidth = 0;
        this.emojis = new ArrayList();
        this.eventListenerMap = new LinkedHashMap<>();
        this.isSplash = false;
    }
    
    @Override
    public RGBColor getColor() {
        RGBColor backgroundColor = this.backgroundColor;
        if (backgroundColor == null) {
            return backgroundColor;
        }
        if (!backgroundColor.isClosed()) {
            logger.log(Level.WARNING, "Background color can not be open!  color={0}", new Object[]{backgroundColor});
            return null;
        }
        return backgroundColor;
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("ApplicationView: onEvent: Unimplemented: eventName=" + eventName + ", eventValue=" + eventValue);
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
