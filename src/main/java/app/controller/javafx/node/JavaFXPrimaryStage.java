package app.controller.javafx.node;

import app.Coordinates;
import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.controller.JavaFXApplication;
import static app.controller.JavaFXApplication.getFxColor;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author repp
 */
public class JavaFXPrimaryStage extends BaseJavaFXNode {
    
    public JavaFXPrimaryStage(app.view.BaseView node, String viewName, BaseController controller) {
        super(node, ((JavaFXApplication) controller).delegateApp.primaryStage, null, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.view.BaseView node = (app.view.BaseView) this.node;
        Stage controllerNode = (Stage) this.controllerNode;
        
        controllerNode.setTitle(node.name);
        
        if (node.iconFileName != null) {
            Image iconImage = JavaFXApplication.loadImage(node.iconFileName);
            controllerNode.getIcons().add(iconImage);
        }
        
        // The JavaFX Scene constructor requires a non-null Parent node upon creation, so prepare that first
        TabPane root = new TabPane();
        Color fxBackgroundColor;
        if (node.backgroundColor == null) {
            fxBackgroundColor = Color.TRANSPARENT;
        } else {
            if (node.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                // TODO - Defer to the app config
                node.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            fxBackgroundColor = getFxColor(node.backgroundColor);
        }
        root.setBackground(new Background(new BackgroundFill(
            fxBackgroundColor,
            CornerRadii.EMPTY, 
            Insets.EMPTY      // To prevent blurry text
        )));
        Image image = JavaFXApplication.loadImage(node.backgroundImage);
        Coordinates imageDimensions = JavaFXApplication.getDimensions(image);
        
        // TODO - It's pretty ugly having an image on the view that's only used for dimensions
        Scene splashScene = new Scene(root, imageDimensions.x, imageDimensions.y);
        controllerNode.setScene(splashScene);
        controllerNode.show();
    }
    
}
