package app.controller.javafx.node;

import app.controller.BaseController;
import static app.controller.BaseController.logger;
import static app.controller.JavaFXApplication.IS_JPRO;
import static app.controller.JavaFXApplication.getFrameDelay;
import static app.controller.JavaFXApplication.loadImage;
import app.node.BaseDecoratedNode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javax.imageio.metadata.IIOMetadata;

/**
 *
 * @author repp
 */
public class JavaFXImage extends BaseJavaFXNode {
    
    public JavaFXImage(app.node.Image node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new ImageView(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.Image node = (app.node.Image) this.node;
        ImageView controllerNode = (ImageView) this.controllerNode;
        
        final Image image = loadImage(node.file);
        controllerNode.setImage(image);
        controllerNode.setSmooth(true);

        int dotIndex = node.file.lastIndexOf('.');
        String extension = (dotIndex > 0) ? node.file.substring(dotIndex + 1) : "";
        final ImageView fxImageViewFinal = controllerNode;

        // JPro does not support animated gifs, so animation needs to be manually handled
        if ((IS_JPRO) && (extension.toLowerCase().equals("gif"))) {
            List<Image> frames = new ArrayList<>();
            List<Duration> frameDelays = new ArrayList<>();
            Timeline timeline;

            javax.imageio.ImageReader reader = javax.imageio.ImageIO.getImageReadersByFormatName("gif").next();
            try (javax.imageio.stream.ImageInputStream ciis = javax.imageio.ImageIO.createImageInputStream(getClass().getResourceAsStream(node.file))) {
                reader.setInput(ciis, false);
                int numberOfImages = reader.getNumImages(true);

                for (int i = 0; i < numberOfImages; i++) {
                    java.awt.image.BufferedImage frameImage = reader.read(i);
                    Image fxImage = SwingFXUtils.toFXImage(frameImage, null); // Convert to JavaFX Image
                    frames.add(fxImage);

                    // Extract frame delay
                    IIOMetadata metadata = reader.getImageMetadata(i);
                    int delayMs = getFrameDelay(metadata);
                    frameDelays.add(Duration.millis(delayMs));
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "A critical error occurred", e);
            }

            if (frames.isEmpty()) {
                return;
            }

            timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE); // Loop indefinitely

            Duration currentTime = Duration.ZERO;
            for (int i = 0; i < frames.size(); i++) {
                final int frameIndex = i;
                // Add a KeyFrame at the specific time instant to switch the image
                KeyFrame keyFrame = new KeyFrame(currentTime, event -> {
                    fxImageViewFinal.setImage(frames.get(frameIndex));
                });
                timeline.getKeyFrames().add(keyFrame);
                // Advance the time by the frame's duration
                currentTime = currentTime.add(frameDelays.get(i));
            }

            // Add and play the image
            timeline.play(); // TODO - Probably needs to happen after the image has been added to its parent
        }
        
        this.scaleNode(controllerNode);
    }
    
}
