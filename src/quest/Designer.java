package quest;

import app.ApplicationController;
import app.ApplicationView;

/**
 *
 * @author repp
 */
public class Designer extends ApplicationView {

    public Designer(String name) {
        super(name);
        this.backgroundImage = "/assets/images/designer.jpg";
        this.addTextArea = false;
    }
    
    @Override
    public void onLoad(ApplicationController appController) {
        appController.addDesigner(this.name);
    }
    
}
