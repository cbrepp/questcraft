package app.node.effect;

import app.EventListener;
import static app.controller.BaseController.NODE_TRANSITIONED_EVENT;
import static app.controller.BaseController.logger;
import java.util.logging.Level;

/**
 *
 * @author repp
 */
public class SlideTransition extends BaseEffect implements EventListener {
    
    public enum Path { FROM_LEFT, FROM_RIGHT, FROM_TOP, FROM_BOTTOM }
    public enum Stage { INIT, ENTERING, READY, EXITING, COMPLETE }

    public Double duration = 0.5; // Seconds until the transition is complete
    public EventListener eventListener;
    public Path path;
    private Stage stage = Stage.INIT;
    
    public SlideTransition(Path path) {
        this.path = path;
    }
    
    public SlideTransition(Path path, EventListener eventListener) {
        this.eventListener = eventListener;
        this.path = path;
    }

    public SlideTransition(Path path, EventListener eventListener, double duration) {
        this.duration = duration;
        this.eventListener = eventListener;
        this.path = path;
    }
    
    public Stage getStage() {
        return this.stage;
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        logger.log(Level.INFO, "Entered: eventName={0}, eventValue={1}", new Object[]{eventName, eventValue});
        if (eventName.equals(NODE_TRANSITIONED_EVENT)) {
            Stage stage = (Stage) eventValue;
            this.stage = stage;
        }
    }
    
}
