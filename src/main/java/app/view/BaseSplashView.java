package app.view;

/**
 *
 * @author repp
 */
public class BaseSplashView extends BaseView {
    
    public Double timeoutSeconds;
    
    public BaseSplashView(String name) {
        super(name);
        this.timeoutSeconds = 0.0;
    }
    
}
