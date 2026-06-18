package app.controller.javafx.node;

import app.Layout;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.controller.JavaFXApplication;
import app.node.BaseDecoratedNode;
import java.net.URL;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author repp
 */
public class JavaFXVideo extends BaseJavaFXNode {
    
    public final Layout layout;
    
    public JavaFXVideo(app.node.Video node, BaseDecoratedNode parent, String viewName, BaseController controller, Layout layout) {
        super(node, new StackPane(), parent, viewName, controller);
        this.layout = layout;
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        // TODO - This control should be some sort of a container so that a button can overlay for "Skip"
        
        app.node.Video node = (app.node.Video) this.node;
        StackPane controllerNode = (StackPane) this.controllerNode;
        
        URL resource = getClass().getResource(node.file);
        
        if (resource == null) {
            logger.log(Level.SEVERE, "Video file not found");
            return;
        }
        
        String source = resource.toExternalForm();
        Media media = new Media(source);
        
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        
        // Add our own custom skip button
        Button skipButton = new Button();
        skipButton.setAlignment(Pos.CENTER);
        skipButton.setText("skip");
        Color halfTransparentWhite = Color.rgb(255, 255, 255, 0.5);
        BackgroundFill fill = new BackgroundFill(halfTransparentWhite, new CornerRadii(4), Insets.EMPTY);
        skipButton.setBackground(new Background(fill));
        StackPane.setAlignment(skipButton, Pos.BOTTOM_RIGHT);
        skipButton.setOnAction(e -> {
            logger.log(Level.INFO, "Skip button selected: name={0}", node.name);
            Duration totalDuration = mediaPlayer.getTotalDuration();
            mediaPlayer.seek(totalDuration);
            skipButton.setDisable(true);
        });

        String eventName = node.name;
        if (node.eventName != null) {
            eventName = node.eventName.toString();
        }
        final String finalEventname = eventName;
        
        if (node.loop) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.seek(Duration.ZERO);
                if (node.eventListener != null) {
                    logger.log(Level.INFO, "Video complete: name={0}", node.name);
                    node.eventListener.onEvent(finalEventname, node.name);
                }
            });
        } else {
            if (node.eventListener != null) {
                mediaPlayer.setOnEndOfMedia(() -> {
                    logger.log(Level.INFO, "Video complete: name={0}", node.name);
                    controllerNode.getChildren().remove(skipButton);
                    node.eventListener.onEvent(finalEventname, node.name);
                });
            }
        }
        
        MediaView mediaView = new MediaView();
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.setPreserveRatio(true);
        mediaView.setSmooth(true);
        controllerNode.getChildren().add(mediaView);
        controllerNode.getChildren().add(skipButton); // Add the button second so that it appears on top
        
        // Add a listener to resizes because once the media has fully loaded, the dimensions will be known
        mediaView.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            if (newBounds.getWidth() > 1 && newBounds.getHeight() > 1) {
                // TODO - The scaling isn't working because the StackPane is expanding to all the MediaView to be max size
                this.scaleNode(controllerNode);
                JavaFXApplication.positionNode((Pane) this.parent.controllerNode, node, this.layout, controllerNode);
            }
        });
    }
    
}
