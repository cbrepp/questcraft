package app.controller.javafx.node;

import app.Layout;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.controller.JavaFXApplication;
import app.node.BaseDecoratedNode;
import java.net.URL;
import java.util.logging.Level;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
 *
 * @author repp
 */
public class JavaFXVideo extends BaseJavaFXNode {
    
    public final Layout layout;
    
    public JavaFXVideo(app.node.Video node, BaseDecoratedNode parent, String viewName, BaseController controller, Layout layout) {
        super(node, new MediaView(), parent, viewName, controller);
        this.layout = layout;
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.Video node = (app.node.Video) this.node;
        MediaView controllerNode = (MediaView) this.controllerNode;
        
        URL resource = getClass().getResource(node.file);
        
        if (resource == null) {
            logger.log(Level.SEVERE, "Video file not found");
            return;
        }
        
        String source = resource.toExternalForm();
        Media media = new Media(source);
        
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);

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
                    node.eventListener.onEvent(finalEventname, node.name);
                });
            }
        }
        
        controllerNode.setMediaPlayer(mediaPlayer);
        controllerNode.setPreserveRatio(true);
        controllerNode.setSmooth(true);
        
        // Add a listener to resizes because once the media has fully loaded, the dimensions will be known
        controllerNode.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            if (newBounds.getWidth() > 1 && newBounds.getHeight() > 1) {
                this.scaleNode(controllerNode);
                JavaFXApplication.positionNode((Pane) this.parent.controllerNode, node, this.layout, controllerNode);
            }
        });        
    }
    
}
