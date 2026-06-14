package app.node;

import app.view.BaseView;

/**
 *
 * @author repp
 */
public class PrimaryStage extends BaseView {
    
    public PrimaryStage(String name) {
        super(name);
    }
    
    public PrimaryStage(BaseView view) {
        super(view.name);
        this.addTextArea = view.addTextArea;
        this.backgroundImage = view.backgroundImage;
        this.className = view.className;
        this.emojis = view.emojis;
        this.iconFileName = view.iconFileName;
        this.isSplash = view.isSplash;
        this.eventListenerMap = view.eventListenerMap;
        
        // TODO - Could this be a BaseCompositeNode that returns the individual views?
    }
    
    @Override
    public boolean isParent() {
        return true;
    }
    
}
