package quest.view;

import app.Font;
import app.FontStyle;
import app.HorizontalAlignment;
import app.Layout;
import app.RelativeCoordinates;
import app.TextDecoration;
import app.VerticalAlignment;
import app.controller.BaseController;
import app.color.Color;
import static app.controller.BaseController.logger;
import app.node.Label;
import java.util.logging.Level;

/**
 *
 * @author repp
 */
public class SplashScreen extends app.view.BaseSplashView {
    
    public BaseController appController;
    
    public SplashScreen(String name) {
        super(name);
        this.isSplash = true;
        this.backgroundImage = "/assets/images/wayne-chung.jpg";
        this.timeoutSeconds = 4.0;
    }
    
    @Override
    public void onLoad(BaseController appController) {
        logger.log(Level.INFO, "Entered: appController={0}", appController);
        this.appController = appController;
        
        /*
        TextDecoration companyDecoration = new TextDecoration();
        companyDecoration.pixelSize = 32.0;
        companyDecoration.color = Color.SHADOW;
        companyDecoration.font = Font.ROBOTO_BLACK;
        companyDecoration.style = FontStyle.BOLD;
        Label companyALabel = new Label("company A", "Wayne", companyDecoration);
        appController.addNode(this.name, this.name, companyALabel, new Layout(new RelativeCoordinates(0.05, 0.05), HorizontalAlignment.LEFT, VerticalAlignment.TOP));
        Label companyBLabel = new Label("company B", "Chung", companyDecoration);
        appController.addNode(this.name, this.name, companyBLabel, new Layout(new RelativeCoordinates(0.05, 0.15), HorizontalAlignment.LEFT, VerticalAlignment.TOP));
        Label companyCLabel = new Label("company C", "Enterprises", companyDecoration);
        appController.addNode(this.name, this.name, companyCLabel, new Layout(new RelativeCoordinates(0.05, 0.25), HorizontalAlignment.LEFT, VerticalAlignment.TOP));

        TextDecoration bylineDecoration = new TextDecoration();
        bylineDecoration.pixelSize = 16.0;
        bylineDecoration.color = Color.SHADOW;
        bylineDecoration.font = Font.ROBOTO_THIN;
        bylineDecoration.style = FontStyle.BOLD;
        Label bylineLabel = new Label("byline", "A SOFTWARE COMPANY", bylineDecoration);
        appController.addNode(this.name, this.name, bylineLabel, new Layout(new RelativeCoordinates(0.05, 0.35), HorizontalAlignment.LEFT, VerticalAlignment.TOP));
        */
        
        this.appController.playSound("/assets/sounds/wayne-chung.wav", false);
    }
    
    @Override
    public void onDisplay(BaseController appController) {
        logger.log(Level.INFO, "Entered: appController={0}", appController);
    }
    
}
