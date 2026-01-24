package app.node.effect;

import app.EventListener;

/**
 *
 * @author repp
 */
public class SlideTransition extends BaseEffect {
    
    public enum Path { FROM_LEFT, FROM_RIGHT, FROM_TOP, FROM_BOTTOM }
    public enum Stage { INIT, ENTERING, READY, EXITING, COMPLETE }

    public Double duration; // Seconds until the transition is complete
    public EventListener eventListener;
    public Path path;
    public Stage stage = Stage.INIT;
    
    public SlideTransition(Path path, Double duration) {
        this.duration = duration;
        this.path = path;
    }

    public SlideTransition(Path path, Double duration, EventListener eventListener) {
        this.duration = duration;
        this.eventListener = eventListener;
        this.path = path;
    }
    
}
