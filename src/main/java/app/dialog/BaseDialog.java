package app.dialog;

/**
 *
 * @author repp
 */
public abstract class BaseDialog {
    
    public String title; // Default (null) is determined by the controller
    
    public BaseDialog() {
    }
    
    public BaseDialog(String title) {
        this.title = title;
    }
    
}
