package app.controller.javafx.node;

import app.Coordinates;
import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.color.RGBColor;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.controller.JavaFXApplication;
import static app.controller.JavaFXApplication.getFxColor;
import static app.controller.JavaFXApplication.loadImage;
import app.node.BaseDecoratedNode;
import app.view.BaseView;
import java.util.Collections;
import java.util.logging.Level;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

/**
 *
 * @author repp
 */
public class JavaFXPane extends BaseJavaFXNode {
    
    public JavaFXPane(app.node.Pane node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new Pane(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        if (this.node instanceof app.view.BaseView view) {
            this.configureView(view);
            return;
        }
        
        app.node.Pane node = (app.node.Pane) this.node;
        Pane controllerNode = (Pane) this.controllerNode;
        
        controllerNode.setSnapToPixel(true);
        controllerNode.setPadding(Insets.EMPTY);

        controllerNode.setCache(false);
        Color fxBackgroundColor;
        if (node.backgroundColor == null) {
            fxBackgroundColor = Color.TRANSPARENT;
        } else {
            if (node.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                node.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            fxBackgroundColor = getFxColor(node.backgroundColor);
        }
        controllerNode.setBackground(new Background(new BackgroundFill(
            fxBackgroundColor,
            CornerRadii.EMPTY, 
            Insets.EMPTY      // To prevent blurry text
        )));
        controllerNode.setPadding(Insets.EMPTY);
        controllerNode.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        controllerNode.setCache(false);
        if ((node.borderWidth != null) && (node.borderWidth > 0)) {
            RGBColor offsetColor = new DecoratedOffsetColor(new OffsetColor(), this.parent);
            controllerNode.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
        }
        
        this.scaleNode(controllerNode);
    }
    
    public void configureView(BaseView view) {
        logger.log(Level.INFO, "Entered: view={0}", view.name);

        Pane content = (Pane) this.controllerNode;
        
        /*

        // Set the pane's size explicitly to the image size
        content.setPrefSize(imageWidth, imageHeight);
        content.setMinSize(imageWidth, imageHeight);
        content.setMaxSize(imageWidth, imageHeight);

        if (view.backgroundImage != null) {
            // Set the background image
            BackgroundImage backgroundImage = new BackgroundImage(
                    image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT // Keep native 1:1 image size
            );
            content.setBackground(new Background(backgroundImage));
        } else {
            Color fxBackgroundColor;
            if (view.backgroundColor == null) {
                fxBackgroundColor = Color.TRANSPARENT;
            } else {
                if (view.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                    view.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
                }
                fxBackgroundColor = getFxColor(view.backgroundColor);
            }
            content.setBackground(new Background(new BackgroundFill(
                fxBackgroundColor,
                CornerRadii.EMPTY, 
                Insets.EMPTY      // To prevent blurry text
            )));
        }
        */

        /*
        Coordinates imageDimensions = null;
        if (view.backgroundImage != null) {
            imageDimensions = JavaFXApplication.getDimensions(view.backgroundImage);
            logger.log(Level.INFO, "Dimensions x={0}, y={1}", new Object[]{imageDimensions.x, imageDimensions.y});
            content.setMinSize(imageDimensions.x, imageDimensions.y);
        }
        content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        content.setSnapToPixel(true);
        //content.setPrefSize(this.primaryDimensions.x, this.primaryDimensions.y);
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setSnapToPixel(true);
        scrollPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsW, oldW, newW) -> {
                    if (newW != null) {
                        newW.setOnShown(e -> {
                            scrollPane.lookup(".viewport").setCache(false);
                        });
                    }
                });
            }
        });
        //scrollPane.setContent(this.tabFolder);
        
        // Configure automatic zooming
        Group zoomGroup = new Group(content);
        StackPane contentHolder = new StackPane(zoomGroup);
        scrollPane.setContent(contentHolder);
        
        double zoomFactor = 1.0; 
        zoomGroup.setScaleX(zoomFactor);
        zoomGroup.setScaleY(zoomFactor);
        
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        //scrollPane.setPrefViewportWidth(dimensions.x);
        //scrollPane.setPrefViewportHeight(dimensions.y);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        
        if (this.parent == null) {
            // TODO - The parent view should really be the application shell, not a pane
            return;
        }
        
        // Configure the background
        Coordinates dimensions = new Coordinates(1280, 793);
        Background background = null;
        if (view.backgroundImage != null) {
            System.out.println("JavaFXApplication: addView: name=" + view.name + ", using background image " + view.backgroundImage);
            Image image = loadImage(view.backgroundImage);
            dimensions.x = imageDimensions.x;
            dimensions.y = imageDimensions.y;

            BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT, // Repeat in X direction
                BackgroundRepeat.NO_REPEAT, // Repeat in Y direction
                BackgroundPosition.DEFAULT,   // Position of the image
                new BackgroundSize(dimensions.x, dimensions.y, false, false, false, false)
            );
            
            Color backgroundColor;
            if (view.backgroundColor != null) {
                backgroundColor = Color.rgb(view.backgroundColor.getRed(), view.backgroundColor.getGreen(), view.backgroundColor.getBlue(), view.backgroundColor.getOpacity());
            } else {
                backgroundColor = Color.BLACK;
            }
            BackgroundFill backgroundFill = new BackgroundFill(
                backgroundColor, // The color to use
                CornerRadii.EMPTY, // No rounded corners
                Insets.EMPTY // No padding
            );
            
            background = new Background(Collections.singletonList(backgroundFill), Collections.singletonList(backgroundImage));
            content.setBackground(background);
            content.setPrefSize(dimensions.x, dimensions.y);
        } else if (view.backgroundColor != null) {
            System.out.println("JavaFXApplication: addView: name=" + view.name + ", using background color " + view.backgroundColor);
            Color backgroundColor = getFxColor(view.backgroundColor);
            BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY);
            background = new Background(backgroundFill);
            content.setBackground(background);    
        }
        
        content.setMinSize(dimensions.x, dimensions.y);
        content.setPrefSize(dimensions.x, dimensions.y);
        
        // Create automatic scaling binding to finish configuring the zoom group
        DoubleBinding dynamicScale = Bindings.createDoubleBinding(() -> {
            double viewportW = scrollPane.getViewportBounds().getWidth();
            double viewportH = scrollPane.getViewportBounds().getHeight();

            // Calculate scale factors for both dimensions
            double scaleX = viewportW / dimensions.x;
            double scaleY = viewportH / dimensions.y;
            
            // Maintain aspect ratio
            double fitScale = Math.min(scaleX, scaleY);

            // Prevent shrinking below the preferred size
            return Math.max(1.0, fitScale);
        }, scrollPane.viewportBoundsProperty());
        zoomGroup.scaleXProperty().bind(dynamicScale);
        zoomGroup.scaleYProperty().bind(dynamicScale);

        // Center the zoom group
        contentHolder.minWidthProperty().bind(Bindings.createDoubleBinding(
            () -> scrollPane.getViewportBounds().getWidth(), scrollPane.viewportBoundsProperty()));
        contentHolder.minHeightProperty().bind(Bindings.createDoubleBinding(
            () -> scrollPane.getViewportBounds().getHeight(), scrollPane.viewportBoundsProperty()));
        */
    }
    
}
