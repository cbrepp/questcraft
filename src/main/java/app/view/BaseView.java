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
        logger.log(Level.INFO, "Entered: eventName={0}, eventValue={1}", new Object[]{eventName, eventValue});
    }
    
    public void addListener(String eventName, BaseView listener) {
        logger.log(Level.INFO, "Entered: eventName={0}, listener={1}", new Object[]{eventName, listener});
        List<BaseView> eventListeners = this.eventListenerMap.get(eventName);
        if (eventListeners == null) {
            eventListeners = new ArrayList<>();
            this.eventListenerMap.put(eventName, eventListeners);
        }
        eventListeners.add(listener);
    }
    
    public void onDisplay(BaseController appController) {
        logger.log(Level.INFO, "Entered: UNIMPLEMENTED for {0}", this.name);
    }
    
    public void onLoad(BaseController appController) {
        logger.log(Level.INFO, "Entered: UNIMPLEMENTED for {0}", this.name);
    }
    
    public void onSelected(BaseController appController) {
        logger.log(Level.INFO, "Entered: UNIMPLEMENTED for {0}", this.name);
    }
    
    public void onUnselected(BaseController appController) {
        logger.log(Level.INFO, "Entered: UNIMPLEMENTED for {0}", this.name);
    }
    
    public void publishEvent(String eventName, Object eventValue) {
        logger.log(Level.INFO, "Entered: eventName={0}, eventValue={1}", new Object[]{eventName, eventValue});
        List<BaseView> eventListeners = this.eventListenerMap.get(eventName);
        if (eventListeners == null) {
            logger.log(Level.INFO, "Entered: No listeners");
            return;
        }
        eventListeners.forEach(
            eventListener -> {
                logger.log(Level.INFO, "Entered: Publishing event for {0}", eventListener.name);
                eventListener.onEvent(eventName, eventValue);
            }
        );
    }
    
    public void removeListener(String eventName, BaseView listener) {
        logger.log(Level.INFO, "Entered: eventName={0}, listener={1}", new Object[]{eventName, listener});
        List<BaseView> eventListeners = this.eventListenerMap.get(eventName);
        if (eventListeners == null) {
            return;
        }
        eventListeners.remove(listener);
    }
    
}
