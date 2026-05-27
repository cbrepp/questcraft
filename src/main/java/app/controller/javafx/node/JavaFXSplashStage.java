package app.controller.javafx.node;

import app.Coordinates;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.controller.JavaFXApplication;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
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
        
        double initialWidth = imageDimensions.x;
        double initialHeight = imageDimensions.y;
        
        // Find the largest screen
        Screen targetScreen = Screen.getPrimary();
        double maxSize = 0;
        var screens = Screen.getScreens();
        for (Screen screen : screens) {
            Rectangle2D screenBounds = screen.getVisualBounds();
            double screenSize = screenBounds.getWidth() * screenBounds.getHeight();
            if (screenSize > maxSize) {
                targetScreen = screen;
            }
        }

        Rectangle2D screenBounds = targetScreen.getVisualBounds();
        double centerX = screenBounds.getMinX() + (screenBounds.getWidth() - initialWidth) / 2;
        double centerY = screenBounds.getMinY() + (screenBounds.getHeight() - initialHeight) / 2;

        // 5. Apply the coordinates and sizes to the Stage
        controllerNode.setX(centerX);
        controllerNode.setY(centerY);
        controllerNode.setWidth(initialWidth);
        controllerNode.setHeight(initialHeight);

        Scene splashScene = new Scene(root, initialWidth, initialHeight);
        root.setPrefWidth(initialWidth);
        root.setPrefHeight(initialHeight);
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
