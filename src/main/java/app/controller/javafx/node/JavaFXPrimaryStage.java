package app.controller.javafx.node;

import app.Coordinates;
import app.Icon;
import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.controller.JavaFXApplication;
import static app.controller.JavaFXApplication.getFxColor;
import app.dialog.Alert;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
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
        // TODO - It's pretty ugly having an image on the view that's only used for dimensions
        Image image = JavaFXApplication.loadImage(node.backgroundImage);
        Coordinates imageDimensions = JavaFXApplication.getDimensions(image);
        
        // TODO - The 59.0 is a fudge factor for the added height of the TabPane to prevent seeing the vertical scroll bar by default
        double initialWidth = imageDimensions.x;
        double initialHeight = imageDimensions.y + 59.0;
        
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
        
        // TODO - Abstract this out onto the BaseController
        String infoEmoji = "\u2139\uFE0F";
        Button infoButton = new Button(infoEmoji);
        double emojiSize = JavaFXApplication.DEFAULT_PIXEL_SIZE - 2;
        infoButton.setStyle("-fx-background-radius: 999; -fx-min-width: " + (emojiSize) + "px; -fx-min-height: " + (emojiSize) + "px; -fx-alignment: center; -fx-max-width: " + (emojiSize) + "px; -fx-max-height: " + (emojiSize) + "px; -fx-padding: 0;");
        infoButton.setOnAction(e -> {
                Alert alert = new Alert("About", "by " + controller.props.getProperty("app.author") + "\n\n" + controller.props.getProperty("app.description"));
                alert.icon = Icon.INFORMATION;
                alert.emojis = String.join(" ", infoEmoji);
                alert.header = node.name;
                this.controller.newDialog(alert);
        });
        
        StackPane primaryPane = new StackPane();
        primaryPane.getChildren().addAll(root, infoButton);
        StackPane.setAlignment(infoButton, Pos.TOP_RIGHT);
        StackPane.setMargin(infoButton, new javafx.geometry.Insets(5, 5, 0, 0));
        
        //Scene primaryScene = new Scene(root, initialWidth, initialHeight);
        Scene primaryScene = new Scene(primaryPane, initialWidth, initialHeight);
        controllerNode.setScene(primaryScene);
        controllerNode.show();
    }
    
}
