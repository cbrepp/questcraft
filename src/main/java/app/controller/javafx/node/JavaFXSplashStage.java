package app.controller.javafx.node;

import app.Coordinates;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.controller.JavaFXApplication;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author repp
 */
public class JavaFXSplashStage extends BaseJavaFXNode {
    
    public JavaFXSplashStage(app.view.BaseSplashView node, String viewName, BaseController controller) {
        super(node, new Stage(), null, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.view.BaseSplashView node = (app.view.BaseSplashView) this.node;
        Stage controllerNode = (Stage) this.controllerNode;
        
        controllerNode.initStyle(StageStyle.UNDECORATED);
        
        // The JavaFX Scene constructor requires a non-null Parent node upon creation, so prepare that first
        Pane root = new Pane();
        Image image = JavaFXApplication.loadImage(node.backgroundImage);
        Coordinates imageDimensions = JavaFXApplication.getDimensions(image);
        BackgroundImage backgroundImage = new BackgroundImage(
            image,
            BackgroundRepeat.NO_REPEAT,  // Don't repeat horizontally
            BackgroundRepeat.NO_REPEAT,  // Don't repeat vertically
            BackgroundPosition.CENTER,   // Center the image
            new BackgroundSize(
                BackgroundSize.AUTO,     // Width
                BackgroundSize.AUTO,     // Height
                false, false,            // Use absolute pixel values
                true,                    // Contain: scale to fit window safely
                false                    // Cover: fill window completely
            )
        );        
        root.setBackground(new Background(backgroundImage));

        Scene splashScene = new Scene(root, imageDimensions.x, imageDimensions.y);
        root.setPrefWidth(imageDimensions.x);
        root.setPrefHeight(imageDimensions.y);
        controllerNode.setScene(splashScene);
        controllerNode.show();

        // Set a timer to close the splash screen after 5 seconds
        new Thread(() -> {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(Math.round(node.timeoutSeconds)));
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "A critical error occurred", e);
            }
            // Close the splash screen and show the main application
            Platform.runLater(() -> {
                controllerNode.close();
                ((JavaFXApplication) this.controller).showPrimaryStage();
            });
        }).start();
    }
    
}
