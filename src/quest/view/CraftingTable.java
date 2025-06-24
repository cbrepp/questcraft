package quest.view;

import app.ApplicationController;
import app.ApplicationView;
import quest.model.Book;

/**
 *
 * @author repp
 */
public class CraftingTable extends ApplicationView {

    public CraftingTable(String name) {
        super(name);
        this.backgroundImage = "/assets/images/designer.jpg";
        this.addTextArea = false;
    }
    
    @Override
    public void onLoad(ApplicationController appController) {
        appController.addDesigner(this.name);
    }
    
}
