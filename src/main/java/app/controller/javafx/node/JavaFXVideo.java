package app.controller.javafx.node;

import app.Coordinates;
import app.EventListener;
import app.HorizontalAlignment;
import app.Layout;
import app.RelativeCoordinates;
import app.VerticalAlignment;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.controller.JavaFXApplication;
import app.node.BaseCompositeNode;
import app.node.BaseDecoratedNode;
import app.node.BaseNode;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
 *
 * @author repp
 */
public class JavaFXVideo extends BaseJavaFXNode implements BaseCompositeNode, EventListener {
    
    public Layout buttonLayout;
    public final Layout layout;
    public MediaPlayer mediaPlayer;
    public MediaView mediaView;
    
    public JavaFXVideo(app.node.Video node, BaseDecoratedNode parent, String viewName, BaseController controller, Layout layout) {
        super(node, new Pane(), parent, viewName, controller);
        this.layout = layout;
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        // TODO - This control should be some sort of a container so that a button can overlay for "Skip"
        
        app.node.Video node = (app.node.Video) this.node;
        Pane controllerNode = (Pane) this.controllerNode;
        
        URL resource = getClass().getResource(node.file);
        
        if (resource == null) {
            logger.log(Level.SEVERE, "Video file not found");
            return;
        }
        
        String source = resource.toExternalForm();
        Media media = new Media(source);
        
        this.mediaPlayer = new MediaPlayer(media);
        this.mediaPlayer.setAutoPlay(true);

        String eventName = node.name;
        if (node.eventName != null) {
            eventName = node.eventName.toString();
        }
        final String finalEventname = eventName;
        
        if (node.loop) {
            this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            this.mediaPlayer.setOnEndOfMedia(() -> {
                this.mediaPlayer.seek(Duration.ZERO);
                if (node.eventListener != null) {
                    logger.log(Level.INFO, "Video complete: name={0}", node.name);
                    node.eventListener.onEvent(finalEventname, node.name);
                }
            });
        } else {
            if (node.eventListener != null) {
                this.mediaPlayer.setOnEndOfMedia(() -> {
                    logger.log(Level.INFO, "Video complete: name={0}", node.name);
                    //controllerNode.getChildren().remove(skipButton);
                    node.eventListener.onEvent(finalEventname, node.name);
                });
            }
        }
        
        this.mediaView = new MediaView();
        this.mediaView.setMediaPlayer(this.mediaPlayer);
        this.mediaView.setPreserveRatio(true);
        this.mediaView.setSmooth(true);
        controllerNode.getChildren().add(this.mediaView);
        
        // Add a listener to resizes because once the media has fully loaded, the dimensions will be known
        this.mediaView.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            logger.log(Level.INFO, "New media view bounds for {0}", this.node.name);
            if (newBounds.getWidth() > 1 && newBounds.getHeight() > 1) {
                controllerNode.setPrefWidth(newBounds.getWidth());
                controllerNode.setPrefHeight(newBounds.getHeight());
                this.scaleNode(controllerNode);
                JavaFXApplication.positionNode((Pane) this.parent.controllerNode, node, this.layout, controllerNode);
                BaseDecoratedNode buttonDecoratedNode = ((JavaFXApplication) this.controller).getDecoratedNode(this.viewName, this.node.name + "/skip");
                if (buttonDecoratedNode != null) {
                    JavaFXApplication.positionNode((Pane) this.controllerNode, buttonDecoratedNode.node, this.buttonLayout, (Node) buttonDecoratedNode.controllerNode);
                }
            }
        });
    }
    
    @Override
    public Map<? extends BaseNode, Layout> getChildren() {
        Map<BaseNode, Layout> children = new LinkedHashMap();
        app.node.Button skipButton = new app.node.Button(this.node.name + "/skip");
        skipButton.backgroundColor = new app.color.Color(app.color.Color.BLACK, 0.5);
        skipButton.eventListener = this;
        skipButton.isMultiUse = false;
        skipButton.text = "skip";
        skipButton.textColor = app.color.Color.WHITE;
        this.buttonLayout = new Layout(new RelativeCoordinates(1.0, 1.0), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
        children.put(skipButton, this.buttonLayout);
        return children;
    }

    @Override
    public void onEvent(String eventName, Object eventValue) {
        if (eventName.equals(this.node.name + "/skip")) {
            logger.log(Level.INFO, "Skip button selected: name={0}", node.name);
            Duration totalDuration = this.mediaPlayer.getTotalDuration();
            this.mediaPlayer.seek(totalDuration);
        } else {
            logger.log(Level.WARNING, "Unsupported event!");
        }
    }
    
    @Override
    public void scaleNode(Node controllerNode) {
        logger.log(Level.INFO, "New media view bounds for {0}", this.node.name);
        
        if ((controllerNode == null) || (this.parent == null)) {
            logger.log(Level.INFO, "Null nodes, nothing to do");
            return;
        }
        
        Coordinates parentDimensions = this.getParentDimensions(controllerNode);
        if (parentDimensions == null) {
            logger.log(Level.WARNING, "No parent dimensions!");
            return;
        }

        if (this.node.scaleX != null) {
            logger.log(Level.INFO, "Scaling MediaView to parent width {0} by {1}", new Object[]{parentDimensions.x, this.node.scaleX});
            double prefWidth = Math.round(parentDimensions.x * this.node.scaleX);
            this.mediaView.setFitWidth(prefWidth);
        }
        if (this.node.scaleY != null) {
            logger.log(Level.INFO, "Scaling MediaView to parent height {0} by {1}", new Object[]{parentDimensions.y, this.node.scaleY});
            double prefHeight = Math.round(parentDimensions.y * this.node.scaleY);
            this.mediaView.setFitHeight(prefHeight);
        }
    }
    
}
