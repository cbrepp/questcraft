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
        this.backgroundImage = "/assets/images/designer.jpg";
    }
    
    @Override
    public void onDisplay(ApplicationController appController, ApplicationView parentView) {}
    
    @Override
    public void onLoad(ApplicationController appController, ApplicationView parentView) {
        appController.addDesigner();
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
