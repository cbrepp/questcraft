package quest;

import app.ApplicationController;
import app.ApplicationView;
import java.util.LinkedHashMap;

/**
 *
 * @author repp
 */
public class Designer extends ApplicationView {

    public Designer() {
        super();
    }
    
    @Override
    public void onDisplay(ApplicationController appController, ApplicationView parentView) {
        // TODO
    }

    @Override
    public LinkedHashMap<String, ApplicationView> getChildren() {
        return null;
    }
    
    @Override
    public void handleEvent(String eventName, String eventValue) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
}
