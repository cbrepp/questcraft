package app.controller;

import app.view.BaseView;
import app.Coordinates;
import app.EventListener;
import app.FontStyle;
import app.HorizontalAlignment;
import static app.HorizontalAlignment.CENTER;
import static app.HorizontalAlignment.LEFT;
import static app.HorizontalAlignment.RIGHT;
import app.Icon;
import app.Layout;
import app.RelativeBounds;
import app.RelativeCoordinates;
import app.TextDecoration;
import app.VerticalAlignment;
import static app.VerticalAlignment.BOTTOM;
import static app.VerticalAlignment.CENTER;
import static app.VerticalAlignment.TOP;
import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.color.RGBColor;
import static app.controller.BaseController.NODE_TRANSITIONED_EVENT;
import static app.controller.BaseController.logger;
import app.node.BaseNode;
import app.controller.javafx.DelegateApplication;
import app.controller.javafx.node.JavaFXButton;
import app.controller.javafx.node.JavaFXButtonGroup;
import app.controller.javafx.node.JavaFXDocument;
import app.controller.javafx.node.JavaFXField;
import app.controller.javafx.node.JavaFXGrid;
import app.controller.javafx.node.JavaFXHorizontalGroup;
import app.controller.javafx.node.JavaFXImage;
import app.controller.javafx.node.JavaFXInputField;
import app.controller.javafx.node.JavaFXLabel;
import app.controller.javafx.node.JavaFXLink;
import app.node.BaseDecoratedNode;
import app.controller.javafx.node.JavaFXPane;
import app.controller.javafx.node.JavaFXPrimaryStage;
import app.controller.javafx.node.JavaFXRectangle;
import app.controller.javafx.node.JavaFXScrollingDocument;
import app.controller.javafx.node.JavaFXScrollingLabel;
import app.controller.javafx.node.JavaFXScrollingPane;
import app.controller.javafx.node.JavaFXSeparator;
import app.controller.javafx.node.JavaFXSplashStage;
import app.controller.javafx.node.JavaFXVerticalGroup;
import app.dialog.BaseDialog;
import app.node.BaseCompositeNode;
import app.node.Sprite;
import app.node.effect.BaseEffect;
import app.node.effect.Glow;
import app.node.effect.SlideTransition;
import static app.node.effect.SlideTransition.Path.FROM_BOTTOM;
import static app.node.effect.SlideTransition.Path.FROM_LEFT;
import static app.node.effect.SlideTransition.Path.FROM_RIGHT;
import static app.node.effect.SlideTransition.Path.FROM_TOP;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.PixelReader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.TextFlow;
import javax.imageio.metadata.IIOMetadata;
import org.w3c.dom.NamedNodeMap;
import app.view.Animation;
import app.view.BaseSplashView;
import java.util.Collections;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Separator;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;

/**
 *
 * @author repp
 */
public class JavaFXApplication extends BaseController {

    public static final int DEFAULT_BUTTON_FONT_SIZE = 10;
    public static final String DEFAULT_FONT = app.Font.ROBOTO_MONO;
    public static final double DEFAULT_FONT_SIZE = 18.0;
    public static final RGBColor DEFAULT_OFFSET_COLOR = app.color.Color.BLACK;
    public static Map<String, JsonObject> EMOJI_MAP;
    public static Image EMOJI_SHEET;
    public static final boolean IS_JPRO = (System.getProperty("jpro.version") != null);
    public static List<String> TIMER_EVENTS = new ArrayList();

    public DelegateApplication delegateApp;
    public final Lock audioLock = new ReentrantLock();
    public Map<String, TextDecoration> defaultTextDecorations = new HashMap();
    public static List<String> fontFamiliesLoaded = new ArrayList();
    public Map<Object, EventHandler<KeyEvent>> keyBindings = new HashMap();
    public BaseView lastSelectedView;
    public HashMap<String, List<MediaPlayer>> mediaPlayers = new HashMap();
    public Map<String, Map<String, BaseDecoratedNode>> namedDecoratedNodes; // view name -> node name -> FX node decorator
    public Map<String, Map<String, Layout>> nodeLayouts; // view name -> node name -> layout
    public JavaFXPrimaryStage parentDecoratedNode;
    public BaseView parentView;
    public Scene primaryScene;
    public BaseSplashView splashView;
    public HashMap<String, Pane> tabContentMap;
    public TabPane tabFolder;
    public HashMap<String, Integer> tabIndexMap;
    public HashMap<String, Tab> tabItemMap;
    public HashMap<Tab, BaseView> tabItemViewMap;
    public HashMap<String, BaseView> views;

    /**
     * The implementation of this method is a work-around to inheritance not being fully implemented in java for static methods. While child classes can inherit a static method from a parent class, there is no way to know within the inherited method for which class it is being executed. Also, there is no good way to know within any static method what the name of the current class is without using a Throwable.
     */
    public static void main(String[] args) {
        logger.log(Level.INFO, "Entered");
        if (args.length == 0) {
            args = new String[1];
            args[0] = new Throwable().getStackTrace()[0].getClassName();
        }
        Bootstrap.main(args);
    }

    @Override
    public void open(BaseSplashView splashView, BaseView mainView) {
        logger.log(Level.INFO, "Entered");

        this.splashView = splashView;
        this.parentView = mainView;

        // The static launching of the JavaFX app will invoke start() on the Application which will invoke setDelegate()
        // in this object to allow a reference of the app to be stored and used for future UI operations
        DelegateApplication.main(new String[1]);
    }

    @Override
    public void setDelegate(Object delegate) {
        logger.log(Level.INFO, "Entered");

        this.delegateApp = (DelegateApplication) delegate;
        
        /*
            Splash:
            Stage
                Scene
                    Pane - parent view

            Primary:
            Stage
                Scene
                    TabPane - parent view
                        Tab
                            ScrollPane (for scrolling with decreased application window size)
                                StackPane (for bindings and layout for the zoom group)
                                    Group (for zooming with increased application window size)
                                        Pane - child view
        */
        
        if (this.splashView != null) {
            // TODO - We really need a decorated view for the stage's scene's pane
            JavaFXSplashStage decoratedSplashStage = new JavaFXSplashStage(this.splashView, this.splashView.name, this);
            decoratedSplashStage.configure();
            this.namedDecoratedNodes = new HashMap();
            this.namedDecoratedNodes.put(this.splashView.name, new HashMap());
            this.nodeLayouts = new HashMap();
            this.nodeLayouts.put(this.splashView.name, new HashMap());
            this.registerNode(this.splashView.name, decoratedSplashStage, null, null);
            this.splashView.onLoad(this);
            this.splashView.onDisplay(this);
        } else {
            this.showPrimaryStage();
        }
    }

    @Override
    public void close() {
        logger.log(Level.INFO, "Entered");
        Platform.exit();    // Gracefully stop all processes in the JavaFX application
        if (IS_JPRO) {
            logger.log(Level.INFO, "System exit intentionally skipped for JPro environment");
        } else {
            System.exit(0);     // Stop any remaining framework processes, including background processes
        }
    }

    public void showPrimaryStage() {
        logger.log(Level.INFO, "Entered");
        
        this.parentDecoratedNode = new JavaFXPrimaryStage(this.parentView, this.parentView.name, this);
        this.parentDecoratedNode.configure();
        this.primaryScene = ((Stage) this.parentDecoratedNode.controllerNode).getScene();
        this.tabFolder = (TabPane) this.primaryScene.getRoot();
        this.tabFolder.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            String selectedTabTitle = newTab.getText();
            logger.log(Level.INFO, "Selected tab {0}", selectedTabTitle);
            BaseView selectedView = this.tabItemViewMap.get(newTab);
            if (selectedView != null) {
                selectedView.onSelected(this);
                BaseView lastSelectedView = this.lastSelectedView;
                this.lastSelectedView = selectedView;
                if ((lastSelectedView != null) && (!lastSelectedView.equals(selectedView))) {
                    lastSelectedView.onUnselected(this);
                }
            }
        });
        this.tabContentMap = new HashMap<>();
        this.tabIndexMap = new HashMap<>();
        this.tabItemMap = new HashMap<>();
        this.tabItemViewMap = new HashMap<>();
        this.views = new HashMap();
        this.namedDecoratedNodes = new HashMap();
        this.nodeLayouts = new HashMap();
        this.parentView.onLoad(this);
        this.parentView.onDisplay(this);
    }

    @Override
    public void addView(BaseView view) {
        Integer index = this.tabIndexMap.get(view.name);
        if (index == null) {
            index = this.tabIndexMap.size();
        }
        this.addView(view, index, false);
    }
    
    @Override
    public void addView(BaseView view, int index, Boolean isRefresh) {
        logger.log(Level.INFO, "JavaFXApplication: addView: name={0}, index={1}, isRefresh={2}", new Object[]{view.name, index, isRefresh});

        // TODO - A view should be the full Tab added to the TabPane
        BaseDecoratedNode decoratedContent = new JavaFXPane(view, this.parentDecoratedNode, view.name, this);
        decoratedContent.configure(); // TODO - Need constructor for JavaFXPane that handles a view and takes care of the complex config seen below
        Pane content = (Pane) decoratedContent.controllerNode;
        
        Coordinates imageDimensions;
        if (view.backgroundImage != null) {
            imageDimensions = getDimensions(view.backgroundImage);
        } else {
            imageDimensions = getDimensions(this.parentView.backgroundImage);
        }
        content.setMinSize(imageDimensions.x, imageDimensions.y);
        content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        content.setSnapToPixel(true);
        content.setCacheHint(CacheHint.QUALITY);
        
        // Configure automatic zooming.  A StackPane is used for bindings and layout.  A Group is used for scaling.
        Group zoomGroup = new Group(content);
        StackPane contentHolder = new StackPane(zoomGroup);
        contentHolder.setSnapToPixel(true);
        
        double zoomFactor = 1.0; 
        zoomGroup.setScaleX(zoomFactor);
        zoomGroup.setScaleY(zoomFactor);
        
        ScrollPane scrollPane = new ScrollPane(contentHolder);
        scrollPane.setSnapToPixel(true);
        // Fix blurry text and images during zoom by disabling JavaFX's internal bitmap caching mechanism for the visible area
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
                
        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setPadding(Insets.EMPTY);
        scrollPane.getStyleClass().add("edge-to-edge"); // Removes the border
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-padding: 0;");
        scrollPane.setCache(false);
        
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
                backgroundColor = getFxColor(view.backgroundColor);
            } else {
                backgroundColor = Color.TRANSPARENT;
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

        // Create a new tab
        Tab tab = new Tab();
        JavaFXApplication.setTabLabel(tab, view);
        tab.setClosable(false);
        this.tabFolder.getTabs().add(index, tab);
        tab.setContent(scrollPane);
        this.tabItemMap.put(view.name, tab);
        this.tabItemViewMap.put(tab, view);

        // Track the tab position of each view.  (Inserting a new view shifts all of the other views to the right.)
        HashMap<String, Integer> indicesToAdd = new HashMap();
        Iterator<String> iterator = this.tabIndexMap.keySet().iterator();
        while (iterator.hasNext()) {
            String viewName = iterator.next();
            int currentIndex = this.tabIndexMap.get(viewName);
            if (currentIndex >= index) {
                currentIndex++;
                iterator.remove();
                indicesToAdd.put(viewName, currentIndex);
            }
        }
        for (String viewName : indicesToAdd.keySet()) {
            Integer currentIndex = indicesToAdd.get(viewName);
            this.tabIndexMap.put(viewName, currentIndex);
        }
        this.tabIndexMap.put(view.name, index);

        this.views.put(view.name, view);

        if (this.namedDecoratedNodes.get(view.name) == null) {
            this.namedDecoratedNodes.put(view.name, new HashMap());
            this.namedDecoratedNodes.get(view.name).put(view.name, decoratedContent);
            this.nodeLayouts.put(view.name, new HashMap());
        }

        this.tabContentMap.put(view.name, content);

        if (!isRefresh) {
            view.onLoad(this);
        }
    }
    
    public static double adjustFontSizeForDPI(double fontSize) {
        return fontSize;
        // TODO - Make this an app option.  Scaling for DPI creates a more predictable font size, however, clarity is often lost with the adjustment,
        // especially with emojis.
        /*
        Screen screen = Screen.getPrimary();
        double dpi = screen.getDpi();
        double scaleFactor = dpi / 96.0;    // Standard DPI is typically 96.0, so this calculates the scaling factor
        double newFontSize = fontSize * scaleFactor;
        newFontSize = Math.round(newFontSize);
        return newFontSize;
         */
    }
    
    @Override
    public void displayView(BaseView view) {
        System.out.println("JavaFXApplication: displayView: Displaying application view: " + view.name);

        int tabIndex = this.tabIndexMap.get(view.name);
        System.out.println("JavaFXApplication: displayTab: Tab index=" + tabIndex);

        if (this.tabFolder != null) {
            this.tabFolder.getSelectionModel().select(tabIndex);
        }

        view.onDisplay(this);
    }

    @Override
    public void displayView(String viewName) {
        System.out.println("JavaFXApplication: displayView: Displaying application view: " + viewName);

        BaseView view = views.get(viewName);
        if (view == null) {
            System.out.println("JavaFXApplication: displayView: View does not exist!");
            return;
        }

        this.displayView(view);
    }

    /*
    @Override
    public void displayOverlay(String viewName, String name, app.Color color, Integer startRow, Integer startColumn, Integer endRow, Integer endColumn, Integer transparency, Boolean invert) {
        System.out.println("JavaFXApplication: displayOverlay: viewName=" + viewName + ", name=" + name + ", color=" + color + ", startRow=" + startRow + ", startColumn=" + startColumn + ", endRow=" + endRow + ", endColumn=" + endColumn + ", transparency=" + transparency + ", invert=" + invert);
        
        Pane content = this.tabContentMap.get(viewName);
        
        Rectangle overlay;
        if (startRow == null) {
            transparency = 128;
            overlay = new Rectangle();
            overlay.widthProperty().bind(content.widthProperty());
            overlay.heightProperty().bind(content.heightProperty());
        } else {
            Coordinates topLeftCoordinates;
            Coordinates bottomRightCoordinates;
            topLeftCoordinates = this.convertToCoordinates(startRow, startColumn);
            bottomRightCoordinates = this.convertToCoordinates(endRow, endColumn);
            int width = bottomRightCoordinates.x - topLeftCoordinates.x;
            int height = bottomRightCoordinates.y - topLeftCoordinates.y;
            overlay = new Rectangle(topLeftCoordinates.x, topLeftCoordinates.y, width, height);
        }

        double opacityPercent = (1.0 - ((double)transparency / 255.0)); // Transparency is 0-255
        System.out.println("JavaFXApplication: displayOverlay: Converted opacity percent to " + opacityPercent);
        
        if (invert) {
            // TODO - This just doesn't work
            overlay.setFill(Color.BLACK); // Using black gives the strongest inversion effect
            overlay.setOpacity(1.0);
            overlay.setBlendMode(BlendMode.DIFFERENCE);
        } else {
            overlay.setFill(new Color((color.red + 1) / 255, (color.green + 1) / 255, (color.blue + 1) / 255, opacityPercent));
        }
        content.getChildren().add(overlay);
        this.namedFXNodes.get(viewName).put(name, overlay);
    }
     */
    @Override
    public void clearScreen(String viewName) {
        logger.log(Level.INFO, "Entered: viewName={0}", viewName);

        if (this.namedDecoratedNodes.get(viewName) != null) {
            // The pane's conent is named the same as the view
            this.namedDecoratedNodes.get(viewName).keySet().removeIf(key -> !key.equals(viewName));
        }

        Pane content = this.tabContentMap.get(viewName);

        if (content != null) {
            //content.getChildren().removeIf(node -> node != editor);
            content.getChildren().clear();
        }

        // Remove all bindings
        this.primaryScene.getAccelerators().clear();
        for (EventHandler<KeyEvent> eventHandler : this.keyBindings.values()) {
            this.primaryScene.removeEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
        }
        this.keyBindings.clear();
    }

    @Override
    public void removeNode(String viewName, String nodeName) {
        logger.log(Level.INFO, "Entered: viewName={0}, nodeName={1}", new Object[]{viewName, nodeName});

        BaseDecoratedNode decoratedNode = this.namedDecoratedNodes.get(viewName).get(nodeName);
        if (decoratedNode == null) {
            logger.log(Level.WARNING, "FX node decorator could NOT be found");
            return;
        }

        // Capture the parent name before the registry gets cleaned up
        String parentName = decoratedNode.parent.node.name;

        Layout nodeLayout = this.nodeLayouts.get(viewName).get(nodeName);
        this.removeFXNode(viewName, decoratedNode);

        BaseNode node = decoratedNode.node;
        Node fxNode = (Node) decoratedNode.controllerNode;

        if (!node.effects.isEmpty()) {
            this.removeEffects(viewName, parentName, node, nodeLayout, fxNode);
        }
    }

    public void removeFXNode(String viewName, BaseDecoratedNode decoratedNode) {
        logger.log(Level.INFO, "Entered: viewName={0}, decoratedNode={1}", new Object[]{viewName, decoratedNode});

        String parentName = decoratedNode.parent.node.name;
        BaseDecoratedNode decoratedParentNode = this.namedDecoratedNodes.get(viewName).get(parentName);
        Object fxParentNode = decoratedParentNode.controllerNode;
        Node fxNode = (Node) decoratedNode.controllerNode;

        // TODO - This is ugly.  Parent nodes do not have a base type (Parent) with a public getChildren() method so each parent class needs to be handled.
        Class<?> parentControlClass = fxParentNode.getClass();
        if (parentControlClass.equals(Pane.class)) {
            Pane pane = (Pane) fxParentNode;
            pane.getChildren().remove(fxNode);
            pane.layout();
        } else if (parentControlClass.equals(StackPane.class)) {
            StackPane pane = (StackPane) fxParentNode;
            pane.getChildren().remove(fxNode);
            pane.layout();
        } else if (parentControlClass.equals(HBox.class)) {
            HBox box = (HBox) fxParentNode;
            box.getChildren().remove(fxNode);
            box.layout();
        } else if (parentControlClass.equals(VBox.class)) {
            VBox box = (VBox) fxParentNode;
            box.getChildren().remove(fxNode);
            box.layout();
        } else if (parentControlClass.equals(Stage.class)) {
            Stage stage = (Stage) fxParentNode;
            Scene scene = stage.getScene();
            Pane pane = (Pane) scene.getRoot();
            pane.getChildren().remove(fxNode);
            pane.layout();
        } else {
            logger.log(Level.SEVERE, "Parent does not have a supported class: {0}", parentControlClass.getSimpleName());
            return;
        }

        String nodeName = decoratedNode.node.name;
        this.deregisterNode(viewName, nodeName);

        // If the control is a button with a key binding remove the event filter from the scene
        if (this.keyBindings.containsKey(fxNode)) {
            this.primaryScene.removeEventFilter(KeyEvent.KEY_PRESSED, this.keyBindings.get(fxNode));
            this.keyBindings.remove(fxNode);
        }
    }

    @Override
    public void addDesigner(String viewName) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void selectTab(String viewName) {
        System.out.println("JavaFXApplication: selectTab: viewName=" + viewName);

        Integer index = this.getTabIndex(viewName);
        if (index == null) {
            System.err.println("JavaFXApplication: selectTab: The view does not have a tab!");
            return;
        }

        System.out.println("JavaFXApplication: selectTab: Setting tab selection to index " + index);
        SingleSelectionModel<Tab> selectionModel = this.tabFolder.getSelectionModel();
        selectionModel.select(index);
    }

    @Override
    public void refreshTabLabel(String viewName) {
        System.out.println("JavaFXApplication: refreshTabLabel: viewName=" + viewName);
        BaseView view = this.views.get(viewName);
        if (view == null) {
            System.out.println("JavaFXApplication: refreshTabLabel: View not found");
            return;
        }
        Tab tab = this.tabItemMap.get(viewName);
        if (tab == null) {
            System.out.println("JavaFXApplication: refreshTabLabel: Tab not found for view");
            return;
        }
        this.setTabLabel(tab, view);
    }

    @Override
    public void removeTab(String viewName) {
        System.out.println("JavaFXApplication: removeTab: viewName=" + viewName);

        this.clearScreen(viewName);

        if (!tabItemMap.containsKey(viewName)) {
            return;
        }

        // *** Dispose of the UI objects ***
        Tab tab = this.tabItemMap.get(viewName);
        this.tabFolder.getTabs().remove(tab);

        this.tabItemViewMap.remove(tab);

        this.tabItemMap.remove(viewName);

        this.namedDecoratedNodes.remove(viewName);

        // Remove all references to the view
        if ((this.lastSelectedView != null) && (this.lastSelectedView.name.equals(viewName))) {
            this.lastSelectedView = null;
        }

        // Shift all indices to the right left by 1
        int tabIndex = tabIndexMap.get(viewName);
        tabIndexMap.remove(viewName);
        for (String tabViewName : this.tabIndexMap.keySet()) {
            int currentIndex = this.tabIndexMap.get(tabViewName);
            if (currentIndex > tabIndex) {
                currentIndex--;
                this.tabIndexMap.put(tabViewName, currentIndex);
            }
        }

        this.views.remove(viewName);
    }

    @Override
    public Integer getTabIndex(String viewName) {
        System.out.println("JavaFXApplication: getTabIndex: viewName=" + viewName);
        Integer index = this.tabIndexMap.get(viewName);
        return index;
    }

    public static void setTabLabel(Tab tab, BaseView view) {
        logger.log(Level.INFO, "Entered: tab={0}, view={1}", new Object[]{tab, view});
        tab.setText(view.name);
        if (!view.emojis.isEmpty()) {
            String emojiString = String.join(" ", view.emojis);
            TextDecoration decoration = new TextDecoration();
            decoration.pixelSize = DEFAULT_FONT_SIZE;
            TextFlow emojis = stringToTextFlow(emojiString, DEFAULT_OFFSET_COLOR, decoration, FontSmoothingType.LCD);
            tab.setGraphic(emojis);
        }
    }

    /*
    @Override
    public void displayMessageBox(String title, String text, int level, List<String> emojis) {
        System.out.println("JavaFXApplication: displayMessageBox: title=" + title + ", text=" + text + ", level=" + level + ", graphic=" + emojis);
        
        AlertType type = switch (level) {
            case Icon.INFORMATION -> AlertType.INFORMATION;
            case Icon.WARNING -> AlertType.WARNING;
            case Icon.ERROR -> AlertType.ERROR;
            default -> AlertType.INFORMATION;
        };
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.initOwner(this.delegateApp.primaryStage);
            alert.setTitle(this.parentView.name);
            TextFlow header = this.stringToTextFlow(title, DEFAULT_FONT, new app.Color(0, 0, 0), DEFAULT_FONT_SIZE, FontStyle.BOLD);
            if ((emojis != null) && (!emojis.isEmpty())) {
                String emojiString = String.join(" ", emojis);
                ImageView graphicImage = this.stringToEmoji(emojiString, (int) Math.round(EMOJI_SHEET_SIZE));
                alert.setGraphic(graphicImage);
            }
            alert.getDialogPane().setHeader(header);
            alert.setContentText(text);
            alert.show();
        });
    }
     */
 /*
    @Override
    public void displayText(String viewName, String text, Integer row, Integer column) {
        System.out.println("JavaFXApplication: displayText: viewName=" + viewName + ", text=" + text + ", row=" + row + ", column=" + column);
        this.displayText(viewName, text, row, column, new app.Color(0, 0, 0));
    }
    
    @Override
    public void displayText(String viewName, String text, Integer row, Integer column, app.Color color) {
        System.out.println("JavaFXApplication: displayText: viewName=" + viewName + ", text=" + text + ", row=" + row + ", column=" + column + ", color=" + color);
        this.displayText(viewName, text, row, column, color, FontStyle.NORMAL);
    }
    
    @Override
    public void displayText(String viewName, String text, Integer row, Integer column, app.Color color, int style) {
        System.out.println("JavaFXApplication: displayText: viewName=" + viewName + ", text=" + text + ", row=" + row + ", column=" + column + ", color=" + color + ", style=" + style);
        this.displayFloatingText(viewName, null, text, row, column, null, null, color, DEFAULT_FONT_SIZE, style, DEFAULT_FONT); // Previously, "Consolas"
    }
     */
 /*
    @Override
    public void displayLink(String viewName, String name, String linkText, int row, int column, int length, EventListener listener) {
        System.out.println("JavaFXApplication: displayLink: viewName=" + viewName + ", name=" + name + ", linkText=" + linkText + ",row=" + row + ", column=" + column + ", length=" + length);
        
        Pane content = this.tabContentMap.get(viewName);
        
        // TODO - The other controllers should NOT expect the <a> tag to be included.  If needed, they should wrap the text themselves.
        linkText = linkText.replace("<a>", "");
        linkText = linkText.replace("</a>", "");

        Hyperlink hyperlink = new Hyperlink(linkText);
        Coordinates coordinates = this.convertToCoordinates(row, column);
        hyperlink.relocate(coordinates.x, coordinates.y);
        hyperlink.setOnAction(e -> {
            System.out.println("JavaFXApplication: displayLink: Link clicked: name=" + name);
            listener.onEvent(name, null);
        });
        
        //this.positionNode(content, textFlow, startCoordinates);
        
        content.getChildren().add(hyperlink);
    }
     */
    public static void positionNode(Pane fxParent, BaseNode node, Layout layout, Node fxNode) {
        logger.log(Level.INFO, "Entered: fxParent={0}, node={1}, layout={2}, fxNode={3}", new Object[]{fxParent, node, layout, fxNode});

        if (layout == null) {
            logger.log(Level.INFO, "No layout, node will be managed by parent");
            return;
        }

        double positionX;
        if (layout.position == null) {
            positionX = 0;
        } else {
            positionX = layout.position.x;
        }
        double nodeWidth = fxNode.prefWidth(-1);
        double parentWidth = fxParent.getPrefWidth();
        Double x = calculateNodeX(layout.horizontalAlignment, positionX, parentWidth, nodeWidth);
        if (x != null) {
            // Prevent placing the child node directly on the right edge of the parent node to prevent issues
            if ((nodeWidth < parentWidth - 1) && ((x + nodeWidth) >= parentWidth - 1)) {
                x -= 2; // Unfortunate fudge factor.  A gap of 2 pixels is needed to prevent triggering a resize of the application window.
                logger.log(Level.WARNING, "Reduced the node's width by 2 to prevent overlap with the parent");
            }
            fxNode.setLayoutX(Math.rint(x));
        }

        double positionY;
        if (layout.position == null) {
            positionY = 0;
        } else {
            positionY = layout.position.y;
        }
        double nodeHeight = fxNode.prefHeight(-1);
        double parentHeight = fxParent.getPrefHeight();
        Double y = calculateNodeY(layout.verticalAlignment, positionY, parentHeight, nodeHeight);
        if (y != null) {
            // Prevent placing the child node directly on the bottom edge of the parent node to prevent issues
            if ((nodeHeight < parentHeight - 1) && ((y + nodeHeight) >= parentHeight - 1)) {
                y -= 2; // Unfortunate fudge factor.  A gap of 2 pixels is needed to prevent triggering a resize of the application window.
                logger.log(Level.WARNING, "Reduced the node's height by 2 to prevent overlap with the parent");
            }
            fxNode.setLayoutY(Math.rint(y));
        }

        logger.log(Level.INFO, "Calculated coordinates ({0}, {1}) for parent width {2} and height {3} and node width {4} and height {5}", new Object[]{x, y, fxParent.getPrefWidth(), fxParent.getPrefHeight(), fxNode.getBoundsInLocal().getWidth(), fxNode.getBoundsInLocal().getHeight()});
    }

    public static Double calculateNodeX(HorizontalAlignment alignment, double relativeX, double parentWidth, double nodeWidth) {
        logger.log(Level.INFO, "Entered: alignment={0}, relativeX={1}, parentWidth={2}, nodeWidth={3}", new Object[]{alignment, relativeX, parentWidth, nodeWidth});

        Double x = null;
        if (alignment == null) {
            logger.log(Level.WARNING, "Horizontal alignment was not specified");
        } else {
            switch (alignment) {
                case LEFT -> {
                    x = parentWidth * relativeX;
                }
                case CENTER -> {
                    x = (parentWidth - nodeWidth) / 2;
                }
                case RIGHT -> {
                    x = (parentWidth * relativeX) - nodeWidth;
                }
            }
        }

        return x;
    }

    public static Double calculateNodeY(VerticalAlignment alignment, double relativeY, double parentHeight, double nodeHeight) {
        logger.log(Level.INFO, "Entered: alignment={0}, relativeY={1}, parentHeight={2}, nodeHeight={3}", new Object[]{alignment, relativeY, parentHeight, nodeHeight});

        Double y = null;
        if (alignment == null) {
            logger.log(Level.WARNING, "Vertical alignment was not specified");
        } else {
            switch (alignment) {
                case TOP -> {
                    y = parentHeight * relativeY;
                }
                case CENTER -> {
                    y = (parentHeight - nodeHeight) / 2;
                }
                case BOTTOM -> {
                    y = (parentHeight * relativeY) - nodeHeight;
                }
            }
        }

        return y;
    }

    /*
    @Override
    public void displayButton(String viewName, String name, String text, Integer row, Integer column, Integer endRow, Integer endColumn, Boolean isMonospace, String fontName, Boolean glow, EventListener listener) {
        System.out.println("JavaFXApplication: displayButton: viewName=" + viewName + ", name=" + name + ", text=" + text + ", row=" + row + ", column=" + column + ", endRow=" + endRow + ", endColumn=" + endColumn + ", isMonospace=" + isMonospace + ", fontName=" + fontName + ", glow=" + glow);
        
        Button button = this.newButton(viewName, name, text, row, column, endRow, endColumn, isMonospace, fontName, glow, listener);
        button.setOnAction(e -> listener.onEvent(name, null));
    }
    
    public Button newButton(String viewName, String name, String text, Integer row, Integer column, Integer endRow, Integer endColumn, Boolean isMonospace, String fontName, Boolean glow, EventListener listener) {
        System.out.println("JafaFXApplication: newButton: viewName=" + viewName + ", name=" + name + ", text=" + text + ", row=" + row + ", column=" + column + ", endRow=" + endRow + ", endColumn=" + endColumn + ", isMonospace=" + isMonospace + ", fontName=" + fontName + ", glow=" + glow);
        
        Pane content = this.tabContentMap.get(viewName);
        
        Button button = new Button(text);
        Coordinates coordinates = this.convertToCoordinates(row, column);
        this.positionNodeOld(content, button, coordinates);
        //button.setLayoutX(coordinates.x);
        //button.setLayoutY(coordinates.y);
        content.getChildren().add(button);
        
        int fontWidth;
        int fontHeight;
        if (fontName == null) {
            button.setFont(this.monospaceFont);
            fontWidth = this.fontWidth;
            fontHeight = this.fontHeight;
            if (isMonospace) {
                button.setFont(this.buttonFont);
                fontWidth = this.buttonFontWidth;
                fontHeight = this.buttonFontHeight;
            }
        } else {
            Font font = Font.font(fontName, FontWeight.NORMAL, DEFAULT_BUTTON_FONT_SIZE);
            Text textNode = new Text("W");
            textNode.setFont(font);
            Bounds bounds = textNode.getLayoutBounds();
            fontWidth = (int) bounds.getWidth();
            fontHeight = (int) bounds.getHeight();
            button.setFont(font);
        }
        int width;
        int height;
        if ((endRow != null) && (endColumn != null)) {
            Coordinates endCoordinates = this.convertToCoordinates(endRow, endColumn);
            width = endCoordinates.x - coordinates.x;
            height = endCoordinates.y - coordinates.y;
            button.setPrefSize(width, height);
        } else {
            width = (text.length() * fontWidth) + (2 * fontWidth);    // Calculate width of text plus buffer of two imaginary characters
            height = 2 * fontHeight;   // Calculate double height of text
            button.setPrefSize(width, height);
        }
        
        // Add a special glow effect to the button to call the user's attention to it
        if (glow) {
            String defaultStyle = "-fx-effect: dropshadow(three-pass-box, rgba(139, 0, 139, 0.8), 5, 0.8, 0, 0);";
            String hoverStyle = "-fx-effect: dropshadow(three-pass-box, rgba(139, 0, 139, 1), 10, 0.8, 0, 0);";
            button.setStyle(defaultStyle);
            button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
            button.setOnMouseExited(e -> button.setStyle(defaultStyle));
        }
        
        this.namedFXNodes.get(viewName).put(name, button);
        
        return button;
    }
     */
    public app.node.BaseDecoratedNode addEffects(String viewName, String parentName, app.node.BaseDecoratedNode decoratedNode, Layout layout) {
        logger.log(Level.INFO, "Entered: viewName={0}, parentName={1}, decoratedNode={2}, layout={3},", new Object[]{viewName, parentName, decoratedNode, layout});

        app.node.BaseNode node = decoratedNode.node;
        Node originalFxNode = (Node) decoratedNode.controllerNode;

        if ((node.effects == null) || (node.effects.isEmpty())) {
            logger.log(Level.INFO, "No effects, nothing to do");
            return decoratedNode;
        }

        BaseDecoratedNode fxDecoratedWrapperNode = null;
        for (BaseEffect effect : node.effects) {
            logger.log(Level.INFO, "Adding effect: class={0}", effect.getClass().getSimpleName());

            switch (effect) {
                case app.node.effect.Glow glowEffect -> {
                    this.addGlow(decoratedNode, glowEffect);
                    logger.log(Level.INFO, "Added glow effect");
                }
                case app.node.effect.SlideTransition transitionEffect -> {
                    // TODO - Need to do something like disable the parent pane's scrollbars during the animation so they don't get confused
                    //parentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    //parentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

                    logger.log(Level.INFO, "Adding transition effect: path={0}, duration={1}, eventListener={2}, stage={3}", new Object[]{transitionEffect.path, transitionEffect.duration, transitionEffect.eventListener, transitionEffect.getStage()});

                    if (transitionEffect.getStage() == SlideTransition.Stage.INIT) {
                        transitionEffect.onEvent(NODE_TRANSITIONED_EVENT, SlideTransition.Stage.ENTERING);
                    }

                    if ((transitionEffect.getStage() == SlideTransition.Stage.ENTERING) || (transitionEffect.getStage() == SlideTransition.Stage.EXITING)) {
                        // Clip the transition in a wrapper so that when the application window is maximized, the spill-over node contents aren't seen
                        app.node.Pane paneNode = new app.node.Pane(node.name);
                        paneNode.borderWidth = 0;
                        fxDecoratedWrapperNode = newDecoratedNode(paneNode, decoratedNode.parent, viewName);
                        decoratedNode.configure();
                        Pane fxWrapperNode = (Pane) fxDecoratedWrapperNode.controllerNode;
                        fxWrapperNode.getChildren().add(originalFxNode);

                        Rectangle clip = new Rectangle();
                        clip.widthProperty().bind(fxWrapperNode.widthProperty());
                        clip.heightProperty().bind(fxWrapperNode.heightProperty());
                        fxWrapperNode.setClip(clip);

                        double nodeWidth = fxWrapperNode.prefWidth(-1);
                        double nodeHeight = fxWrapperNode.prefHeight(-1);

                        Pane fxParent = (Pane) this.namedDecoratedNodes.get(viewName).get(parentName).controllerNode; // TODO - This effect should only be supported when the parent is a pane
                        Layout originalLayout;
                        if (layout == null) {
                            originalLayout = null;
                        } else {
                            originalLayout = layout.copy();
                        }

                        logger.log(Level.FINE, "Measured effects wrapper: width={0}, height={1}", new Object[]{nodeWidth, nodeHeight});
                        TranslateTransition slide = new TranslateTransition(Duration.seconds(transitionEffect.duration), originalFxNode);
                        if (transitionEffect.path == null) {
                            logger.log(Level.WARNING, "Unsupported transition path");
                        } else {
                            switch (transitionEffect.path) {
                                case FROM_LEFT -> {
                                    // Increase the wrapper's width by the intended x-coordinate for the child node so the wrapper can stretch to the left edge of the parent
                                    double parentWidth = fxParent.getPrefWidth();
                                    Double nodeX = calculateNodeX(layout.horizontalAlignment, layout.position.x, parentWidth, nodeWidth);
                                    double wrapperWidth = nodeWidth + nodeX;
                                    fxWrapperNode.setPrefWidth(wrapperWidth);
                                    // Anchor the wrapper to the left edge of the parent
                                    layout.position = new RelativeCoordinates(0.0, layout.position.y);
                                    layout.horizontalAlignment = HorizontalAlignment.LEFT;
                                    if (transitionEffect.getStage() == SlideTransition.Stage.ENTERING) {
                                        originalFxNode.setTranslateX(-nodeWidth);
                                        slide.setFromX(-nodeWidth);
                                        slide.setToX(wrapperWidth - nodeWidth);
                                    } else if (transitionEffect.getStage() == SlideTransition.Stage.EXITING) {
                                        originalFxNode.setTranslateX(wrapperWidth - nodeWidth);
                                        slide.setFromX(wrapperWidth - nodeWidth);
                                        slide.setToX(-nodeWidth);
                                    }
                                }
                                case FROM_RIGHT -> {
                                    // Increase the wrapper's width to the difference between the parent's width and the intended x-coordinate for the child node so the wrapper can stretch to the right edge of the parent
                                    double parentWidth = fxParent.getPrefWidth();
                                    Double nodeX = calculateNodeX(layout.horizontalAlignment, layout.position.x, parentWidth, nodeWidth);
                                    double wrapperWidth = parentWidth - nodeX;
                                    fxWrapperNode.setPrefWidth(wrapperWidth);
                                    // Anchor the wrapper to the right edge of the parent
                                    layout.position = new RelativeCoordinates(1.0, layout.position.y);
                                    layout.horizontalAlignment = HorizontalAlignment.RIGHT;
                                    if (transitionEffect.getStage() == SlideTransition.Stage.ENTERING) {
                                        originalFxNode.setTranslateX(wrapperWidth);
                                        slide.setFromX(wrapperWidth);
                                        slide.setToX(0);
                                    } else if (transitionEffect.getStage() == SlideTransition.Stage.EXITING) {
                                        originalFxNode.setTranslateX(0);
                                        slide.setFromX(0);
                                        slide.setToX(wrapperWidth);
                                    }
                                }
                                case FROM_TOP -> {
                                    // Increase the wrapper's height by the intended y-coordinate for the child node so the wrapper can stretch to the top edge of the parent
                                    double parentHeight = fxParent.getPrefHeight();
                                    Double nodeY = calculateNodeY(layout.verticalAlignment, layout.position.y, parentHeight, nodeHeight);
                                    double wrapperHeight = nodeHeight + nodeY;
                                    fxWrapperNode.setPrefHeight(wrapperHeight);
                                    // Anchor the wrapper to the top edge of the parent
                                    layout.position = new RelativeCoordinates(layout.position.x, 0.0);
                                    layout.verticalAlignment = VerticalAlignment.TOP;
                                    if (transitionEffect.getStage() == SlideTransition.Stage.ENTERING) {
                                        originalFxNode.setTranslateY(-nodeHeight);
                                        slide.setFromY(-nodeHeight);
                                        slide.setToY(wrapperHeight - nodeHeight);
                                    } else if (transitionEffect.getStage() == SlideTransition.Stage.EXITING) {
                                        originalFxNode.setTranslateY(wrapperHeight - nodeHeight);
                                        slide.setFromY(wrapperHeight - nodeHeight);
                                        slide.setToY(-nodeHeight);
                                    }
                                }
                                case FROM_BOTTOM -> {
                                    // Increase the wrapper's height to the difference between the parent's height and the intended y-coordinate for the child node so the wrapper can stretch to the bottom edge of the parent
                                    double parentHeight = fxParent.getPrefHeight();
                                    Double nodeY = calculateNodeY(layout.verticalAlignment, layout.position.y, parentHeight, nodeHeight);
                                    double wrapperHeight = parentHeight - nodeY;
                                    fxWrapperNode.setPrefHeight(wrapperHeight);
                                    // Anchor the wrapper to the bottom edge of the parent
                                    layout.position = new RelativeCoordinates(layout.position.x, 1.0);
                                    layout.verticalAlignment = VerticalAlignment.BOTTOM;
                                    if (transitionEffect.getStage() == SlideTransition.Stage.ENTERING) {
                                        originalFxNode.setTranslateY(wrapperHeight);
                                        slide.setFromY(wrapperHeight);
                                        slide.setToY(0);
                                    } else if (transitionEffect.getStage() == SlideTransition.Stage.EXITING) {
                                        originalFxNode.setTranslateY(0);
                                        slide.setFromY(0);
                                        slide.setToY(wrapperHeight);
                                    }
                                }
                                default ->
                                    logger.log(Level.WARNING, "Unsupported transition path");
                            }
                        }
                        slide.setInterpolator(Interpolator.EASE_OUT);
                        final app.node.BaseDecoratedNode finalFxDecoratedWrapperNode = fxDecoratedWrapperNode;
                        slide.setOnFinished(event -> {
                            if (transitionEffect.getStage() == SlideTransition.Stage.ENTERING) {
                                transitionEffect.onEvent(NODE_TRANSITIONED_EVENT, SlideTransition.Stage.READY);
                            } else if (transitionEffect.getStage() == SlideTransition.Stage.EXITING) {
                                transitionEffect.onEvent(NODE_TRANSITIONED_EVENT, SlideTransition.Stage.COMPLETE);
                            }
                            logger.log(Level.INFO, "Transition for node {0} complete, stage={1}", new Object[]{node.name, transitionEffect.getStage()});
                            // When the transition play is complete, re-position the child outside of the wrapper and added it directly to the parent
                            this.removeFXNode(viewName, finalFxDecoratedWrapperNode); // Remove the wrapper
                            if (transitionEffect.getStage() == SlideTransition.Stage.READY) {
                                this.publishNode(viewName, parentName, node, originalLayout, null); // Re-publish the node to directly add it to its parent (using the original layout)
                            }
                            // Also, publish an event so that the button can be disabled during the transition and re-enabled upon completion
                            transitionEffect.eventListener.onEvent(NODE_TRANSITIONED_EVENT, node.name);
                        });
                        slide.play();
                    }
                }
                default -> {
                    logger.log(Level.SEVERE, "Class is not a supported effect class: {0}", effect.getClass().getSimpleName());
                }
            }
        }

        if (fxDecoratedWrapperNode != null) {
            return fxDecoratedWrapperNode;
        }

        return decoratedNode;
    }

    public void removeEffects(String viewName, String parentName, BaseNode node, Layout layout, Node fxNode) {
        logger.log(Level.INFO, "Entered: viewName={0}, parentName={1}, node={2}, layout={3}, fxNode={4}", new Object[]{viewName, parentName, node, layout, fxNode});

        if ((node.effects == null) || (node.effects.isEmpty())) {
            logger.log(Level.INFO, "No effects, nothing to do");
            return;
        }

        for (BaseEffect effect : node.effects) {
            Class<?> effectClass = effect.getClass();
            if (effectClass.equals(app.node.effect.SlideTransition.class)) {
                SlideTransition transitionEffect = (SlideTransition) effect;
                if (transitionEffect.getStage() == SlideTransition.Stage.READY) {
                    transitionEffect.onEvent(NODE_TRANSITIONED_EVENT, SlideTransition.Stage.EXITING);
                    // Re-add the node without it's fx self which will call back into addEffects() which will see
                    // the advanced stage on the effect and know to remove it with a slide out.
                    this.publishNode(viewName, parentName, node, layout, null);
                }
            } else {
                logger.log(Level.SEVERE, "Class is not a supported effect class: {0}", effectClass.getSimpleName());
            }
        }
    }

    public void addGlow(BaseDecoratedNode decoratedNode, Glow effect) {
        logger.log(Level.INFO, "Entered: decoratedNode={0}, glowEffect={1}", new Object[]{decoratedNode, effect});
        RGBColor glowColor;
        if (effect.color == null) {
            glowColor = DEFAULT_OFFSET_COLOR;
        } else {
            glowColor = effect.color;
        }
        if (glowColor instanceof OffsetColor primitiveOffsetColor) {
            glowColor = new DecoratedOffsetColor(primitiveOffsetColor, decoratedNode.parent);
            if (effect.color == null) {
                glowColor = DEFAULT_OFFSET_COLOR;
            }
        }
        Node fxNode = (Node) decoratedNode.controllerNode;
        logger.log(Level.INFO, "Glow color={0}", glowColor);
        String defaultStyle = "-fx-effect: dropshadow(three-pass-box, rgba(" + glowColor.getRed() + ", " + glowColor.getGreen() + ", " + glowColor.getBlue() + ", 0.8), 5, 0.8, 0, 0);";
        String hoverStyle = "-fx-effect: dropshadow(three-pass-box, rgba(" + glowColor.getRed() + ", " + glowColor.getGreen() + ", " + glowColor.getBlue() + ", 1), 10, 0.8, 0, 0);";
        fxNode.setStyle(defaultStyle);
        fxNode.setOnMouseEntered(e -> fxNode.setStyle(hoverStyle));
        fxNode.setOnMouseExited(e -> fxNode.setStyle(defaultStyle));
    }

    @Override
    public void newDialog(BaseDialog dialog) {
        logger.log(Level.INFO, "Entered: dialog={0}", dialog);

        Class<?> dialogClass = dialog.getClass();

        // TODO - Move each dialog to its own separate handler method
        if (dialogClass.equals(app.dialog.FileSelection.class)) {
            newFileSelection((app.dialog.FileSelection) dialog);
        } else if (dialogClass.equals(app.dialog.Alert.class)) {
            newAlert((app.dialog.Alert) dialog);
        } else {
            logger.log(Level.SEVERE, "Class is not a supported dialog class: {0}", dialogClass.getSimpleName());
        }
    }

    public void newAlert(app.dialog.Alert dialog) {
        logger.log(Level.INFO, "Entered: dialog={0}", dialog);

        AlertType type = switch (dialog.icon) {
            case Icon.INFORMATION ->
                AlertType.INFORMATION;
            case Icon.WARNING ->
                AlertType.WARNING;
            case Icon.ERROR ->
                AlertType.ERROR;
            default ->
                AlertType.INFORMATION;
        };
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.initOwner(this.delegateApp.primaryStage);

            if (dialog.title != null) {
                alert.setTitle(dialog.title + " - " + this.parentView.name);
            } else {
                alert.setTitle(this.parentView.name);
            }

            HBox customHeader = new HBox(10); // 10px spacing
            customHeader.setPadding(new Insets(20));
            customHeader.setAlignment(Pos.CENTER_LEFT);
            String emojis = dialog.emojis;
            if (emojis == null) {
                emojis = switch (dialog.icon) {
                    case Icon.CANCEL ->
                        "\uD83D\uDEAB"; // No Entry
                    case Icon.ERROR ->
                        "\uD83D\uDEA8"; // Police Car Light
                    case Icon.INFORMATION ->
                        "\uD83D\uDCA1"; // Light Bulb.  Alternate would be "\uD83D\uDEC8" (Circled Information Source).
                    case Icon.QUESTION ->
                        "\uD83E\uDD14"; // Thinking Face.  Alternate would be Red Question Mark.
                    case Icon.SEARCH ->
                        "\uD83D\uDD0D"; // Left-Pointing Magnifying Glass
                    case Icon.WARNING ->
                        "\u26A0"; // Warning Sign
                    case Icon.WORKING ->
                        "\uD83D\uDE80"; // Rocket
                    default ->
                        "\uD83D\uDCA1"; // Light Bulb
                };
            }
            TextDecoration decoration = new TextDecoration();
            decoration.color = app.color.Color.BLACK;
            decoration.pixelSize = EMOJI_SHEET_SIZE;
            decoration.style = FontStyle.NORMAL;
            TextFlow emojiFlow = stringToTextFlow(emojis, DEFAULT_OFFSET_COLOR, decoration, FontSmoothingType.LCD);
            customHeader.getChildren().add(emojiFlow);

            String header = dialog.header;
            if (header == null) {
                header = dialog.icon.name();
            }
            decoration = new TextDecoration();
            decoration.color = app.color.Color.BLACK;
            decoration.pixelSize = EMOJI_SHEET_SIZE / 2;
            decoration.style = FontStyle.BOLD;
            TextFlow headerFlow = stringToTextFlow(header, DEFAULT_OFFSET_COLOR, decoration, FontSmoothingType.LCD);
            customHeader.getChildren().add(headerFlow);

            alert.getDialogPane().setHeader(customHeader);

            if (dialog.text != null) {
                alert.setContentText(dialog.text);
            }

            alert.showAndWait();
        });
    }

    public void newFileSelection(app.dialog.FileSelection dialog) {
        logger.log(Level.INFO, "Entered: dialog={0}", dialog);

        FileChooser fileChooser = new FileChooser();

        if (dialog.title != null) {
            fileChooser.setTitle(dialog.title);
        }

        if (dialog.initialFolder != null) {
            fileChooser.setInitialDirectory(new File(dialog.initialFolder));
        }

        if ((dialog.extensionFilters != null) && (!dialog.extensionFilters.isEmpty())) {
            for (String filter : dialog.extensionFilters) {
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(filter, filter);
                fileChooser.getExtensionFilters().add(extFilter);
            }
            fileChooser.setInitialDirectory(new File(dialog.initialFolder));
        }

        File selectedFile = fileChooser.showOpenDialog(this.delegateApp.primaryStage);

        if (selectedFile != null) {
            String selectedFilePath = selectedFile.getAbsolutePath();
            logger.log(Level.INFO, "File selected: path={0}", selectedFilePath);
            dialog.eventListener.onEvent(dialog.eventName, selectedFilePath);
        } else {
            logger.log(Level.INFO, "No file was selected");
        }
    }

    public Node newGroup(String viewName, app.node.Group node, Pane fxNode, RGBColor offsetColor) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, fxNode={2}, offsetColor={3}", new Object[]{viewName, node, fxNode, offsetColor});

        if (fxNode != null) {
            fxNode.getChildren().clear();
        }

        Class<?> groupClass = node.getClass();
        if (groupClass.equals(app.node.VerticalGroup.class)) {
            VBox vbox;
            if (fxNode == null) {
                vbox = new VBox();
            } else {
                vbox = (VBox) fxNode;
            }
            vbox.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
            vbox.setAlignment(Pos.CENTER);
            vbox.setFillWidth(false); // Allow children to stay at their preferred widths and be centered 
            fxNode = vbox;
        } else if (groupClass.equals(app.node.HorizontalGroup.class)) {
            HBox hbox;
            if (fxNode == null) {
                hbox = new HBox();
            } else {
                hbox = (HBox) fxNode;
            }
            hbox.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
            hbox.setAlignment(Pos.CENTER);
            fxNode = hbox;
        } else if (groupClass.equals(app.node.Document.class)) {
            TextFlow flow;
            if (fxNode == null) {
                flow = this.newLabel(viewName, new app.node.Label(node.name), null, offsetColor, FontSmoothingType.LCD);
            } else {
                flow = this.newLabel(viewName, new app.node.Label(node.name), (TextFlow) fxNode, offsetColor, FontSmoothingType.LCD);
            }
        } else {
            logger.log(Level.SEVERE, "Unsupported group: {0}", groupClass);
            return null;
        }
        fxNode.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        if (node.backgroundColor == null) {
            fxNode.setBackground(Background.EMPTY);
        } else {
            Color fxBackgroundColor = getFxColor(node.backgroundColor);
            fxNode.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        return fxNode;
    }

    public Pane newPane(String viewName, app.node.Pane node, Pane fxPane, RGBColor offsetColor) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, fxTextBox={2}, offsetColor={3}", new Object[]{viewName, node, fxPane, offsetColor});

        if (fxPane == null) {
            fxPane = new Pane();
            fxPane.setSnapToPixel(true);
            fxPane.setPadding(Insets.EMPTY);
        }

        fxPane.setCache(false);
        Color fxBackgroundColor;
        if (node.backgroundColor == null) {
            fxBackgroundColor = Color.TRANSPARENT;
        } else {
            fxBackgroundColor = getFxColor(node.backgroundColor);
        }
        fxPane.setBackground(new Background(new BackgroundFill(
                fxBackgroundColor,
                CornerRadii.EMPTY,
                Insets.EMPTY // To prevent blurry text
        )));
        fxPane.setPadding(Insets.EMPTY);
        fxPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        fxPane.setCache(false);
        if (node.borderWidth != null) {
            fxPane.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
        }

        return fxPane;
    }

    public ScrollPane newScrollingPane(String viewName, app.node.ScrollingPane node, ScrollPane scrollPane, RGBColor offsetColor) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, fxTextBox={2}, offsetColor={3}", new Object[]{viewName, node, scrollPane, offsetColor});

        Pane fxPane;
        if (scrollPane != null) {
            fxPane = (Pane) scrollPane.getContent();
        } else {
            fxPane = new Pane();
            fxPane.setManaged(true);
            fxPane.setSnapToPixel(true);
            fxPane.setPadding(Insets.EMPTY);
            scrollPane = new ScrollPane();
            scrollPane.setContent(fxPane);
            scrollPane.setSnapToPixel(true);

            // Disable the view port's cache once it's ready
            final ScrollPane finalScrollPane = scrollPane;
            scrollPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.windowProperty().addListener((obsW, oldW, newW) -> {
                        if (newW != null) {
                            newW.setOnShown(e -> {
                                finalScrollPane.lookup(".viewport").setCache(false);
                            });
                        }
                    });
                }
            });
        }

        // Allow text to be scrolled if needed
        fxPane.setCache(false);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        Color fxBackgroundColor;
        if (node.backgroundColor == null) {
            fxBackgroundColor = Color.TRANSPARENT;
        } else {
            fxBackgroundColor = getFxColor(node.backgroundColor);
        }
        scrollPane.setBackground(new Background(new BackgroundFill(
                fxBackgroundColor,
                CornerRadii.EMPTY,
                Insets.EMPTY // To prevent blurry text
        )));
        scrollPane.setPadding(Insets.EMPTY);
        scrollPane.getStyleClass().add("edge-to-edge"); // Removes the border
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setCache(false);
        fxPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Fill the scroll pane.  The scroll pane will be scaled as needed.

        return scrollPane;
    }

    public ScrollPane newScrollingDocument(String viewName, app.node.ScrollingDocument node, ScrollPane scrollPane, RGBColor offsetColor) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, fxTextBox={2}, offsetColor={3}", new Object[]{viewName, node, scrollPane, offsetColor});

        TextFlow fxDocument;
        if (scrollPane != null) {
            fxDocument = (TextFlow) scrollPane.getContent();
            fxDocument = newDocument(viewName, node, fxDocument, offsetColor);
            scrollPane.setContent(fxDocument);
        } else {
            fxDocument = newDocument(viewName, node, null, offsetColor);
            fxDocument.setManaged(true);
            fxDocument.setSnapToPixel(true);
            fxDocument.setPadding(Insets.EMPTY);
            scrollPane = new ScrollPane();
            scrollPane.setContent(fxDocument);
            scrollPane.setSnapToPixel(true);

            // Disable the view port's cache once it's ready
            final ScrollPane finalScrollPane = scrollPane;
            scrollPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.windowProperty().addListener((obsW, oldW, newW) -> {
                        if (newW != null) {
                            newW.setOnShown(e -> {
                                finalScrollPane.lookup(".viewport").setCache(false);
                            });
                        }
                    });
                }
            });
        }

        // Allow text to be scrolled if needed
        fxDocument.setCache(false);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        Color fxBackgroundColor;
        if (node.backgroundColor == null) {
            fxBackgroundColor = Color.TRANSPARENT;
        } else {
            fxBackgroundColor = getFxColor(node.backgroundColor);
        }
        scrollPane.setBackground(new Background(new BackgroundFill(
                fxBackgroundColor,
                CornerRadii.EMPTY,
                Insets.EMPTY // To prevent blurry text
        )));
        scrollPane.setPadding(Insets.EMPTY);
        scrollPane.getStyleClass().add("edge-to-edge"); // Removes the border
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setCache(false);
        fxDocument.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Fill the scroll pane.  The scroll pane will be scaled as needed.

        return scrollPane;
    }

    public FlowPane newInputField(app.node.InputField node, FlowPane fxInputField, RGBColor offsetColor) {
        logger.log(Level.INFO, "Entered: node={0}, fxInputField={1}, offsetColor={2}", new Object[]{node, fxInputField, offsetColor});

        if (fxInputField == null) {
            fxInputField = new FlowPane();
        }

        if (node.spacerPixels != null) {
            fxInputField.setHgap(node.spacerPixels);
            fxInputField.setVgap(node.spacerPixels);
            fxInputField.setPadding(new Insets(node.spacerPixels));
        }

        if (node.backgroundColor != null) {
            Color fxBackgroundColor = getFxColor(node.backgroundColor);
            BackgroundFill backgroundFill = new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY);
            Background background = new Background(backgroundFill);
            fxInputField.setBackground(background);
        }

        // TODO - This is an interesting way to subscribe to the button click and raise the main event, passing the entered text
        final FlowPane finalFxInputField = fxInputField;
        node.internalEventListener = (String eventName, Object eventValue) -> {
            String enteredText = "";
            for (Node child : finalFxInputField.getChildren()) {
                if (child instanceof TextField field) {
                    enteredText = field.getText();
                }
            }
            node.eventListener.onEvent(node.name, enteredText);

            // Clear the entered text
            for (Node child : finalFxInputField.getChildren()) {
                if (child instanceof TextField field) {
                    field.clear();
                }
            }
        };

        return fxInputField;
    }

    public Region newSeparator(app.node.Separator node, Separator fxSeparator) {
        logger.log(Level.INFO, "Entered: node={0}, fxSpacer={1}", new Object[]{node, fxSeparator});
        if (fxSeparator == null) {
            fxSeparator = new Separator();
        }
        if (node.orientation == app.node.Separator.Orientation.HORIZONTAL) {
            fxSeparator.setOrientation(Orientation.HORIZONTAL);
        } else {
            fxSeparator.setOrientation(Orientation.VERTICAL);
        }
        return fxSeparator;
    }

    public TextField newField(app.node.Field node, TextField fxTextField, RGBColor offsetColor) {
        logger.log(Level.INFO, "Entered: node={0}, fxTextField={1}, offsetColor={2}", new Object[]{node, fxTextField, offsetColor});

        if (fxTextField == null) {
            fxTextField = new TextField();
        }

        fxTextField.setDisable(!node.isEnabled);

        if (node.label != null) {
            fxTextField.setPromptText(node.label);
        }

        if (node.initialValue != null) {
            fxTextField.setText(node.initialValue);
        }

        if (node.displayLength != null) {
            fxTextField.setPrefColumnCount(node.displayLength);
        }

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (node.isUpperCase) {
                // Apply the uppercase conversion to the new text
                change.setText(change.getText().toUpperCase());
            }

            // Enforce the character limit
            if ((node.length != null) && (change.getControlNewText().length() > node.length)) {
                return null; // Reject the change
            }

            if (node.eventListener != null) {
                // Raise an event for each entered character
                String currentText = change.getControlNewText();
                logger.log(Level.INFO, "Text entered: name={0}, text={1}", new Object[]{node.name, currentText});
                node.eventListener.onEvent(node.eventName, currentText);
            }

            return change; // Accept the change
        });
        fxTextField.setTextFormatter(textFormatter);

        String fontName;
        if (node.textFont == null) {
            fontName = DEFAULT_FONT;
        } else {
            fontName = node.textFont;
        }

        app.color.RGBColor textColor;
        if (node.textColor == null) {
            textColor = offsetColor;
        } else {
            textColor = node.textColor;
        }

        double pixelSize;
        if (node.pixelSize == null) {
            pixelSize = DEFAULT_PIXEL_SIZE;
        } else {
            pixelSize = node.pixelSize;
        }

        TextDecoration decoration = new TextDecoration();
        decoration.font = fontName;
        decoration.color = textColor;
        decoration.pixelSize = pixelSize;
        decoration.style = node.textStyle;
        Text fieldText = stringToText("temp", offsetColor, decoration, FontSmoothingType.LCD); // Allow stringToText to parse the font style
        fxTextField.setFont(fieldText.getFont());
        fxTextField.setStyle("-fx-text-fill: rgb(" + textColor.getRed() + ", " + textColor.getGreen() + ", " + textColor.getBlue() + ");");

        if (node.backgroundColor != null) {
            Color fxBackgroundColor = getFxColor(node.backgroundColor);
            fxTextField.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            fxTextField.setBackground(Background.EMPTY); // Transparent        
        }

        if ((node.borderWidth != null) && (node.borderWidth > 0)) {
            fxTextField.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
        }

        return fxTextField;
    }

    public Button newButton(app.node.Button node, Button fxButton, RGBColor offsetColor) {
        logger.log(Level.INFO, "Entered: node={0}, fxButton={1}, offsetColor={2}", new Object[]{node, fxButton, offsetColor});

        Boolean isNew = true;
        if (fxButton == null) {
            fxButton = new Button();
        } else {
            isNew = false;
        }

        fxButton.setDisable(!node.isEnabled);

        String font;
        if (node.textFont == null) {
            font = DEFAULT_FONT;
        } else {
            font = node.textFont;
        }

        app.color.RGBColor textColor;
        if (node.textColor == null) {
            if (node.backgroundColor != null) {
                textColor = offsetColor;
            } else {
                textColor = DEFAULT_OFFSET_COLOR; // By default, light gray is the background color
            }
        } else {
            textColor = node.textColor;
        }

        double pixelSize;
        if (node.pixelSize == null) {
            pixelSize = DEFAULT_PIXEL_SIZE;
        } else {
            pixelSize = node.pixelSize;
        }

        // Configure the font style based on whether the button is enabled
        FontStyle fontStyle;
        if (node.isEnabled) {
            fontStyle = FontStyle.NORMAL;
        } else {
            fontStyle = FontStyle.ITALIC;
        }

        // Use a graphic instead of text to support formatted text
        fxButton.setContentDisplay(ContentDisplay.CENTER);
        if (node.scaleY != null) {
            // TODO - An unfortunate work-around for the label/graphic not vertically aligning when the node is scaled vertically and there is no text
            fxButton.setText("\u200B"); // Unicode zero-width space
        }
        fxButton.setAlignment(Pos.CENTER);
        String buttonText = node.text.toString();
        TextDecoration decoration = new TextDecoration();
        decoration.font = font;
        decoration.color = textColor;
        decoration.pixelSize = pixelSize;
        decoration.style = fontStyle;
        TextFlow textFlow = JavaFXApplication.stringToTextFlow(buttonText, offsetColor, decoration, FontSmoothingType.LCD);
        textFlow.setTextAlignment(TextAlignment.CENTER);
        fxButton.setGraphic(textFlow);

        if (node.backgroundColor != null) {
            Color fxBackgroundColor = getFxColor(node.backgroundColor);
            fxButton.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        if ((node.borderWidth != null) && (node.borderWidth > 0)) {
            fxButton.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
        }

        if (!isNew) {
            return fxButton;
        }

        if (node.keyBinding != null) {
            KeyCode keyBinding = null;
            if (null != node.keyBinding) {
                switch (node.keyBinding) {
                    case UP ->
                        keyBinding = KeyCode.UP;
                    case DOWN ->
                        keyBinding = KeyCode.DOWN;
                    case LEFT ->
                        keyBinding = KeyCode.LEFT;
                    case RIGHT ->
                        keyBinding = KeyCode.RIGHT;
                    default -> {
                        logger.log(Level.WARNING, "Unsupported key binding {0}", node.keyBinding);
                    }
                }
            }

            if (keyBinding != null) {
                final KeyCode finalKeyBinding = keyBinding;
                final Button finalFxButton = fxButton;
                EventHandler<KeyEvent> keyHandler = event -> {
                    if (event.getCode() == finalKeyBinding) {
                        finalFxButton.fire();
                        event.consume(); // Prevent key from triggering other events
                    }
                };
                this.primaryScene.addEventFilter(KeyEvent.KEY_PRESSED, keyHandler);
                this.keyBindings.put(fxButton, keyHandler);
            }
        }

        String eventName;
        if (node.eventName == null) {
            eventName = node.name;
        } else {
            eventName = node.eventName.toString();
        }

        final Button fxButtonFinal = fxButton;
        if (node.eventListener != null) {
            fxButton.setOnAction(e -> {
                logger.log(Level.INFO, "Button selected: name={0}", node.name);
                if (!node.isMultiUse) {
                    fxButtonFinal.setDisable(true);
                }
                node.eventListener.onEvent(eventName, node.text);
            });
        }

        if (node.scaleY == null) {
            // Fix the height of the button scaling beyond control.  This may be an issue caused by the scroll pane.
            fxButton.prefHeightProperty().bind(
                    Bindings.createDoubleBinding(
                            () -> textFlow.prefHeight(fxButtonFinal.getWidth()) + 10, // +10 for button padding
                            fxButton.widthProperty(),
                            textFlow.widthProperty()
                    )
            );
        }

        return fxButton;
    }

    @Override
    public void setDefaultTextDecoration(String viewName, TextDecoration textDecoration) {
        logger.log(Level.INFO, "Entered: viewName={0}, textDecoration={1}", new Object[]{viewName, textDecoration});
        this.defaultTextDecorations.put(viewName, textDecoration);
    }

    @Override
    public TextDecoration getDefaultTextDecoration(String viewName) {
        logger.log(Level.INFO, "Entered: viewName={0}", viewName);
        TextDecoration textDecoration = this.defaultTextDecorations.get(viewName);
        return textDecoration;
    }

    public TextFlow newLabel(String viewName, app.node.Label node, TextFlow fxLabel, RGBColor offsetColor, FontSmoothingType fst) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, fxLabel={2}, offsetColor={3}, fst={4}", new Object[]{viewName, node, fxLabel, offsetColor, fst});

        fxLabel = new TextFlow(); // TODO - Support updating
        TextDecoration defaultTextDecoration = this.defaultTextDecorations.get(viewName);

        for (app.Text text : node.texts) {
            if (text.decoration == null) {
                text.decoration = new TextDecoration();
            }

            if (text.decoration.font == null) {
                if ((defaultTextDecoration != null) && (defaultTextDecoration.font != null)) {
                    text.decoration.font = defaultTextDecoration.font;
                } else {
                    text.decoration.font = DEFAULT_FONT;
                }
            }

            if (text.decoration.color == null) {
                if ((defaultTextDecoration != null) && (defaultTextDecoration.color != null)) {
                    text.decoration.color = defaultTextDecoration.color;
                } else {
                    text.decoration.color = offsetColor;
                }
            }

            if (text.decoration.pixelSize == null) {
                if ((defaultTextDecoration != null) && (defaultTextDecoration.pixelSize != null)) {
                    text.decoration.pixelSize = defaultTextDecoration.pixelSize;
                } else {
                    text.decoration.pixelSize = DEFAULT_PIXEL_SIZE;
                }
            }

            if (text.decoration.style == null) {
                if ((defaultTextDecoration != null) && (defaultTextDecoration.style != null)) {
                    text.decoration.style = defaultTextDecoration.style;
                } else {
                    text.decoration.style = app.FontStyle.NORMAL;
                }
            }

            TextFlow fxTempLabel = this.stringToTextFlow(text.text.toString(), offsetColor, text.decoration, fst);
            fxLabel.getChildren().addAll(fxTempLabel.getChildren());
        }

        // TODO - Only set the following values if they're changing
        if (node.backgroundColor != null) {
            Color fxBackgroundColor = getFxColor(node.backgroundColor);
            fxLabel.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            fxLabel.setBackground(Background.EMPTY); // Transparent        
        }

        if (node.borderWidth != null) {
            Color fxBorderColor;
            if (node.borderColor != null) {
                fxBorderColor = getFxColor(node.borderColor);
            } else {
                fxBorderColor = getFxColor(offsetColor);
            }
            fxLabel.setBorder(new Border(new BorderStroke(fxBorderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
        }

        return fxLabel;
    }

    public static Color getFxColor(RGBColor color) {
        logger.log(Level.INFO, "Entered: color class={0}, color={1}", new Object[]{color.getClass(), color});
        Color fxColor;
        if (color.getOpacity() < 1.0) {
            fxColor = Color.rgb(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
        } else {
            fxColor = Color.rgb(color.getRed(), color.getGreen(), color.getBlue());
        }
        return fxColor;
    }

    public VBox newScrollingLabel(String viewName, app.node.Label node, VBox fxTextBox, RGBColor offsetColor) {
        logger.log(Level.INFO, "Entered: node={0}, offsetColor={1}", new Object[]{node, offsetColor});

        // Text boxes (like Strings) are immutable
        if (fxTextBox != null) {
            fxTextBox.getChildren().clear();
            this.removeNode(viewName, node.name);
        }

        TextFlow label = newLabel(viewName, node, null, offsetColor, FontSmoothingType.GRAY); // Needed because scroll pane's dork with FontSmoothingType.LCD

        // Allow text to be scrolled if needed
        label.setCache(false);
        ScrollPane scrollPane = new ScrollPane(label);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setBackground(Background.EMPTY);
        fxTextBox = new VBox(scrollPane);
        fxTextBox.setBackground(Background.EMPTY);
        fxTextBox.setSnapToPixel(true);
        scrollPane.getStyleClass().add("edge-to-edge"); // Removes the border
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setCache(false);
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Fill the scroll pane.  The scroll pane will be scaled as needed.

        return fxTextBox;
    }

    public TextFlow newDocument(String viewName, app.node.Document node, TextFlow fxDocument, RGBColor offsetColor) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, fxLabel={2}, offsetColor={3}", new Object[]{viewName, node, fxDocument, offsetColor});

        if (fxDocument == null) {
            fxDocument = new TextFlow();
        }

        // TODO - Only set the following values if they're changing
        if (node.backgroundColor != null) {
            Color fxBackgroundColor = getFxColor(node.backgroundColor);
            fxDocument.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            fxDocument.setBackground(Background.EMPTY); // Transparent        
        }

        if (node.borderWidth > 0) {
            fxDocument.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
        }

        return fxDocument;
    }

    public Rectangle newRectangle(app.node.Rectangle node, Rectangle fxRectangle, RGBColor offsetColor) {
        logger.log(Level.INFO, "Entered: node={0}, fxRectangle={1}, offsetColor={2}", new Object[]{node, fxRectangle, offsetColor});

        if (fxRectangle == null) {
            fxRectangle = new Rectangle();
        }

        if (node.color == null) {
            fxRectangle.setFill(Color.rgb(offsetColor.getRed(), offsetColor.getGreen(), offsetColor.getBlue(), offsetColor.getOpacity()));
        } else {
            fxRectangle.setFill(Color.rgb(node.color.getRed(), node.color.getGreen(), node.color.getBlue(), node.color.getOpacity()));
        }

        return fxRectangle;
    }

    public ImageView newImage(app.node.Image node, ImageView fxImageView) {
        logger.log(Level.INFO, "Entered: node={0}, fxImageView={1}", new Object[]{node, fxImageView});

        if (fxImageView == null) {
            final Image image = loadImage(node.file);
            fxImageView = new ImageView(image);
            fxImageView.setSmooth(true);
        }

        int dotIndex = node.file.lastIndexOf('.');
        String extension = (dotIndex > 0) ? node.file.substring(dotIndex + 1) : "";
        final ImageView fxImageViewFinal = fxImageView;

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
                return null;
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

        return fxImageView;
    }

    public BaseDecoratedNode newDecoratedNode(app.node.BaseNode node, BaseDecoratedNode decoratedParentNode, String viewName) {
        logger.log(Level.INFO, "Entered: node={0}, decoratedParentNode={1}, offsetColor={2}, viewName={3}", new Object[]{node, decoratedParentNode, viewName});

        if ((node == null) || (decoratedParentNode == null)) {
            logger.log(Level.SEVERE, "Entered: Null value was provided");
            return null;
        }

        BaseDecoratedNode decoratedNode;
        switch (node) {
            case app.node.Link link -> {
                decoratedNode = new JavaFXLink(link, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.Button button -> {
                decoratedNode = new JavaFXButton(button, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.ButtonGroup bg -> {
                decoratedNode = new JavaFXButtonGroup(bg, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.Field field -> {
                decoratedNode = new JavaFXField(field, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.InputField inputField -> {
                decoratedNode = new JavaFXInputField(inputField, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.ScrollingLabel sl -> {
                decoratedNode = new JavaFXScrollingLabel(sl, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.Label label -> {
                decoratedNode = new JavaFXLabel(label, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.ScrollingDocument sd -> {
                decoratedNode = new JavaFXScrollingDocument(sd, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.Document document -> {
                decoratedNode = new JavaFXDocument(document, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.Pane p -> {
                decoratedNode = new JavaFXPane(p, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.ScrollingPane sp -> {
                decoratedNode = new JavaFXScrollingPane(sp, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.Image image -> {
                decoratedNode = new JavaFXImage(image, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.Grid grid -> {
                decoratedNode = new JavaFXGrid(grid, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.HorizontalGroup hg -> {
                decoratedNode = new JavaFXHorizontalGroup(hg, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.VerticalGroup vg -> {
                decoratedNode = new JavaFXVerticalGroup(vg, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.Rectangle rectangle -> {
                decoratedNode = new JavaFXRectangle(rectangle, decoratedParentNode, viewName, this);
                break;
            }
            case app.node.Separator separator -> {
                decoratedNode = new JavaFXSeparator(separator, decoratedParentNode, viewName, this);
                break;
            }
            default -> {
                Class<?> nodeClass = node.getClass();
                logger.log(Level.SEVERE, "Class is not a supported child class: {0}", nodeClass.getSimpleName());
                return null;
            }
        }

        return decoratedNode;
    }

    @Override
    public void changeNode(String viewName, BaseNode node, Layout layout) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, layout={2}", new Object[]{viewName, node, layout});
        BaseDecoratedNode decoratedNode = this.namedDecoratedNodes.get(viewName).get(node.name);
        if (decoratedNode == null) {
            logger.log(Level.WARNING, "Decorated node {0} not found", node.name);
            return;
        }
        String parentName = decoratedNode.parent.node.name;
        if (parentName == null) {
            logger.log(Level.WARNING, "Parent not found for node {0}", node.name);
            return;
        }
        if (layout == null) {
            // As a convenience, re-use the current layout
            layout = this.nodeLayouts.get(viewName).get(node.name);
        }
        this.publishNode(viewName, parentName, node, layout, decoratedNode);
    }

    @Override
    public void addNode(String viewName, String parentName, BaseNode node, Layout layout) {
        logger.log(Level.INFO, "Entered: viewName={0}, parentName={1}, node={2}, layout={3}", new Object[]{viewName, parentName, node, layout});
        this.publishNode(viewName, parentName, node, layout, null);
    }

    public void publishNode(String viewName, String parentName, BaseNode node, Layout layout, BaseDecoratedNode decoratedNode) {
        logger.log(Level.INFO, "Entered: viewName={0}, parentName={1}, node={2}, layout={3}, decoratedNode={4}", new Object[]{viewName, parentName, node, layout, decoratedNode});

        if (node.name == null) {
            logger.log(Level.SEVERE, "Node does not have a name!");
            return;
        }

        BaseDecoratedNode decoratedParentNode = this.namedDecoratedNodes.get(viewName).get(parentName);
        if (decoratedParentNode == null) {
            logger.log(Level.SEVERE, "Parent node does not exist for the view");
            return;
        }

        // TODO - The view should be stored in namedDecoratedNodes
        Object fxParent = decoratedParentNode.controllerNode;
        if (fxParent == null) {
            if (viewName.equals(parentName)) {
                fxParent = this.tabContentMap.get(viewName);
            } else {
                logger.log(Level.SEVERE, "FX parent with provided name not found");
                return;
            }
        }
        BaseNode parent = decoratedParentNode.node;

        // TODO - This is ugly.  Parent nodes do not have a base type with public getPrefWidth() and getPrefHeight() methods so each parent class needs to be handled.
        double parentWidth;
        double parentHeight;
        if (fxParent instanceof Region region) {
            parentWidth = region.getPrefWidth();
            parentHeight = region.getPrefHeight();
        } else if (fxParent instanceof Stage stage) {
            Scene scene = stage.getScene();
            Pane pane = (Pane) scene.getRoot();
            parentWidth = pane.getWidth();
            parentHeight = pane.getHeight();
        } else {
            Class<?> parentControlClass = fxParent.getClass();
            logger.log(Level.SEVERE, "Parent does not have a supported class: {0}", parentControlClass.getSimpleName());
            return;
        }

        // TODO - The container nodes should call into publishNode instead of addNode, looking up any pre-existing fxChild node
        // TODO - Continue refactoring here.  Use a factory method to decorate an FX node for each node type.
        logger.log(Level.INFO, "Decorating node {0}", node.name);
        Boolean isNew = (decoratedNode == null);
        if (isNew) {
            decoratedNode = newDecoratedNode(node, decoratedParentNode, viewName);
        }
        decoratedNode.configure();
        Node fxNode = (Node) decoratedNode.controllerNode;

        if (isNew) {
            // TODO - This is ugly.
            // To allow the width and height to be accessed immediately, add the child to a temporary Scene.
            // The temporary Scene and Group will be garbage collected at the end of the method as they fall out of scope.
            javafx.scene.Group tempRoot = new javafx.scene.Group(fxNode);
            Scene tempScene = new Scene(tempRoot);
            fxNode.applyCss();
            double width = fxNode.prefWidth(-1);
            double height = fxNode.prefHeight(-1);
            logger.log(Level.INFO, "Temp dimensions for {0} = {1}x{2}p", new Object[]{node, width, height});
            ((javafx.scene.Group) fxNode.getScene().getRoot()).getChildren().remove(fxNode);
        }

        if ((node.effects != null) && (!node.effects.isEmpty())) {
            // Effects might require a wrapper so handle effects now before adding the node to its parent so that a wrapper can be added if needed
            decoratedNode = this.addEffects(viewName, parentName, decoratedNode, layout);
            fxNode = (Node) decoratedNode.controllerNode;
        }

        // TODO - This is ugly.  Parent nodes do not have a base type (Parent) with a public getChildren() method so each parent class needs to be handled.
        Class<?> parentClass;
        if (parent != null) {
            parentClass = parent.getClass();
            logger.log(Level.INFO, "Evaluating parent {0}", parentClass);
        } else {
            parentClass = null;
            logger.log(Level.INFO, "Evaluating null parent");
        }
        if (fxParent instanceof GridPane grid) {
            logger.log(Level.INFO, "Adding node to grid");
            JavaFXGrid decoratedParentGrid = (JavaFXGrid) decoratedParentNode;
            decoratedParentGrid.advanceNextCell();
            Coordinates currentCell = decoratedParentGrid.getCurrentCell();
            for (Node gridChildNode : grid.getChildren()) {
                Integer nodeCol = GridPane.getColumnIndex(gridChildNode);
                Integer nodeRow = GridPane.getRowIndex(gridChildNode);

                if ((nodeCol != null) && (nodeRow != null) && (nodeCol == currentCell.x) && (nodeRow == currentCell.y)) {
                    if (gridChildNode instanceof StackPane sp) {
                        sp.getChildren().add(fxNode);
                        break;
                    }
                }
            }
        } else if (fxParent instanceof FlowPane fp) {
            if (isNew) {
                logger.log(Level.INFO, "Adding node to flow pane");
                fp.getChildren().add(fxNode);
            }
        } else if (fxParent instanceof StackPane sp) {
            if (isNew) {
                logger.log(Level.INFO, "Adding node to stack pane");
                sp.getChildren().add(fxNode);
            }
        } else if (fxParent instanceof Pane pane) {
            if (isNew) {
                logger.log(Level.INFO, "Adding node to pane");
                pane.getChildren().add(fxNode);
            }
            pane.layout();
            positionNode(pane, node, layout, fxNode);
        } else if (fxParent instanceof Stage stage) {
            Scene scene = stage.getScene();
            Pane pane = (Pane) scene.getRoot();
            if (isNew) {
                logger.log(Level.INFO, "Adding node to stage");
                pane.getChildren().add(fxNode);
            }
            pane.layout();
            positionNode(pane, node, layout, fxNode);
        } else if (parent instanceof app.node.ScrollingPane) {
            ScrollPane sp = (ScrollPane) fxParent;
            if (isNew) {
                logger.log(Level.INFO, "Adding node to scroll pane");
                Pane pane = (Pane) sp.getContent();
                pane.getChildren().add(fxNode);
                pane.layout();
                positionNode(pane, node, layout, fxNode);
            }
        } else if (fxParent instanceof HBox hbox) {
            HBox.setHgrow(fxNode, Priority.NEVER); // Preventing HBox from stretching children horizontally just to fill its width
            if (isNew) {
                logger.log(Level.INFO, "Adding node to HBox");
                hbox.getChildren().add(fxNode);
            }
        } else if (parentClass == app.node.ScrollingDocument.class) {
            // TODO - Most of this is redundant with Document handling above
            ScrollPane scrollPane = (ScrollPane) fxParent;
            TextFlow flow = (TextFlow) scrollPane.getContent();
            if (isNew) {
                if (node instanceof app.node.Label label) {
                    // TODO - This is ugly and breaks any effects that might be added
                    // Decompose the textflow into its individual children and add them one by one to allow the FlowPane to handle the baseline alignment.
                    TextFlow fxLabel = (TextFlow) fxNode;
                    List<Node> textFlowNodes = new ArrayList();
                    for (Node flowNode : fxLabel.getChildren()) {
                        textFlowNodes.add(flowNode);
                    }
                    logger.log(Level.INFO, "Adding TextFlow children to scrolling document");
                    for (Node textFlowNode : textFlowNodes) {
                        if (textFlowNode.getClass() == Text.class) {
                            Text textNode = (Text) textFlowNode;
                            if (textNode.getFontSmoothingType() != FontSmoothingType.GRAY) {
                                logger.log(Level.INFO, "Fixing font smoothing type for scrolling document's text");
                                textNode.setFontSmoothingType(FontSmoothingType.GRAY);
                            }
                        }
                        flow.getChildren().add(textFlowNode);
                    }
                } else {
                    Class<?> nodeClass = node.getClass();
                    logger.log(Level.INFO, "Adding node {0} to scrolling document", nodeClass);
                    flow.getChildren().add(fxNode);
                }
                flow.requestLayout();
            }
        } else if (fxParent instanceof VBox vbox) {
            VBox.setVgrow(fxNode, Priority.NEVER); // Preventing VBox from stretching children vertically just to fill its height
            if (isNew) {
                logger.log(Level.INFO, "Adding node to VBox");
                vbox.getChildren().add(fxNode);
            }
        } else if (parentClass == app.node.Document.class) {
            TextFlow flow = (TextFlow) fxParent;
            if (isNew) {
                if (node instanceof app.node.Label) {
                    // Decompose the textflow into its individual children and add them one by one to allow the FlowPane to handle the baseline alignment.
                    TextFlow label = (TextFlow) fxNode;
                    List<Node> textFlowNodes = new ArrayList();
                    for (Node flowNode : label.getChildren()) {
                        textFlowNodes.add(flowNode);
                    }
                    for (Node textFlowNode : textFlowNodes) {
                        flow.getChildren().add(textFlowNode);
                    }
                } else {
                    flow.getChildren().add(fxNode);
                }
                flow.layout();
            }
        } else {
            Class<?> parentControlClass = fxParent.getClass();
            logger.log(Level.SEVERE, "Parent does not have a supported class: {0}", parentControlClass.getSimpleName());
            return;
        }

        this.registerNode(viewName, decoratedNode, parentName, layout);

        double width = fxNode.prefWidth(-1); //fxChild.getBoundsInLocal().getWidth();
        double relativeWidth = width / parentWidth;
        double height = fxNode.prefHeight(-1); //fxChild.getBoundsInLocal().getHeight();
        double relativeHeight = height / parentHeight;
        double x = fxNode.getLayoutX();
        double relativeX = x / parentWidth;
        double y = fxNode.getLayoutY();
        double relativeY = y / parentHeight;
        RelativeBounds relativeBounds = new RelativeBounds(new RelativeCoordinates(relativeX, relativeY), relativeWidth, relativeHeight);
        node.onEvent(NODE_PUBLISHED_EVENT, relativeBounds); // Let the node know its bounds
        logger.log(Level.INFO, "Added node {0} {1} at ({2},{3}), width={4}, height={5} using parent pixel width={6}, parent pixel height={7}, pixel width={8}, pixel height={9}", new Object[]{node, node.name, relativeX, relativeY, relativeWidth, relativeHeight, parentWidth, parentHeight, width, height});

        // Publish any child nodes for the node
        if (node instanceof BaseCompositeNode baseCompositeNode) {
            logger.log(Level.INFO, "Publishing child nodes of composite node");
            for (BaseNode childNode : baseCompositeNode.getChildren()) {
                BaseDecoratedNode childDecoratedNode = null;
                if (this.namedDecoratedNodes.get(viewName).containsKey(childNode.name)) {
                    childDecoratedNode = this.namedDecoratedNodes.get(viewName).get(childNode.name); // If the child node already exists, get it so it can be updated
                }
                Layout childLayout = this.nodeLayouts.get(viewName).get(childNode.name); // If the child node's layout already exists, get it
                this.publishNode(viewName, node.name, childNode, childLayout, childDecoratedNode);
            }
        }
    }

    public void registerNode(String viewName, BaseDecoratedNode decoratedNode, String parentName, app.Layout layout) {
        logger.log(Level.INFO, "Entered: viewName={0}, decoratedNode={1}, parentName={2}, layout={3}", new Object[]{viewName, decoratedNode, parentName, layout});
        String nodeName = decoratedNode.node.name;
        this.namedDecoratedNodes.get(viewName).put(nodeName, decoratedNode);
        this.nodeLayouts.get(viewName).put(nodeName, layout);
    }

    public void deregisterNode(String viewName, String nodeName) {
        logger.log(Level.INFO, "Entered: viewName={0}, nodeName={1}", new Object[]{viewName, nodeName});
        this.namedDecoratedNodes.get(viewName).remove(nodeName);
        this.nodeLayouts.get(viewName).remove(nodeName);
    }

    /*
    // TODO - Make this newGrid() and add to addNode()
    @Override
    public void displayGrid(String viewName, app.node.Grid grid, Layout layout) {
        logger.log(Level.INFO, "Entered: viewName={0}, grid={1}, layout={2}", new Object[]{viewName, grid, layout});
        
        // TODO - Implement layout.  Currently, the grid expands to fill the view.
        
        BaseView view = this.views.get(viewName);
        app.color.RGBColor genericOffsetColor = new OffsetColor(view);
        Color offsetColor = getFxColor(genericOffsetColor);
        Pane tabContent = this.tabContentMap.get(viewName);
        
        GridPane gridContent = new GridPane();
        gridContent.setPrefSize(this.primaryDimensions.x, this.primaryDimensions.y);
        gridContent.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        int cellCount = grid.cells.size();

        // Configure the background (transparent or a fill color)
        if (grid.backgroundColor == null) {
            System.out.println("JavaFXApplication: displayGrid: Configuring background to be transparent");
            gridContent.setBackground(Background.EMPTY); // Transparent
        } else {
            System.out.println("JavaFXApplication: displayGrid: Fill color " + grid.backgroundColor);
            Color backgroundColor = getFxColor(view.backgroundColor);
            BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY);
            Background background = new Background(backgroundFill);
            gridContent.setBackground(background);
        }

        // Configure borders
        BorderStroke stroke = null;
        if (grid.showBorders) {
            System.out.println("JavaFXApplication: displayGrid: Adding borders: width=" + grid.borderWidth + ", offsetColor=" + offsetColor + ", corner radii=" + grid.cornerRadii);
            CornerRadii cornerRadii;
            if (grid.cornerRadii == 0) {
                cornerRadii = CornerRadii.EMPTY;
            } else {
                cornerRadii = new CornerRadii(grid.cornerRadii);
            }
            gridContent.setBorder(new Border(new BorderStroke(offsetColor, BorderStrokeStyle.SOLID, cornerRadii, new BorderWidths(grid.borderWidth))));
            
            stroke = new BorderStroke(offsetColor, BorderStrokeStyle.SOLID, cornerRadii, new BorderWidths(1));
        }
        
        // Configure outer cell padding
        if (grid.borderPadding > 0) {
            gridContent.setHgap(grid.borderPadding);
            gridContent.setVgap(grid.borderPadding);
        }
        
        // Configure inner cell padding
        Insets cellPadding = null;
        if (grid.padding > 0) {
            cellPadding = new Insets(grid.padding);
        }
        
        // Configure dimensions
        int columns = grid.columns;
        if (columns == 0) {
            double squareRoot = Math.sqrt(cellCount);
            columns = (int) Math.ceil(squareRoot);
        }
        int rows = 0;
        if (columns != 0) {
            double rowsDiv = ((double) cellCount / (double) columns);  // Make sure values are double so remainder causes rows count to round up
            rows = (int) Math.ceil(rowsDiv);
        }
        
        // Configure rows to expand as much as they can
        int rowHeight = (int) Math.floor(100 / rows);
        for (int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(rowHeight);
            row.setVgrow(Priority.ALWAYS);
            gridContent.getRowConstraints().add(row);
        }
        
        // Allow columns to expand as much as they can
        int columnWidth = (int) Math.floor(100 / columns);
        for (int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(columnWidth);
            column.setHgrow(Priority.ALWAYS);
            gridContent.getColumnConstraints().add(column);
        }

        System.out.println("JavaFXApplication: displayGrid: cells=" + cellCount + ", columns=" + columns + ", rows=" + rows);
        
        int currentRow = 1;
        int currentColumn = 0;        
        for (app.node.Group cellGroup : grid.cells) {
            System.out.println("JavaFXApplication: displayGrid: Adding cell " + cellGroup.name);
            
            StackPane cell = new StackPane();
            GridPane.setHgrow(cell, Priority.ALWAYS);
            GridPane.setVgrow(cell, Priority.ALWAYS);
            cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Set node to expand to fill the cell (optional)
            
            if (cellGroup.backgroundColor == null) {
                cell.setBackground(Background.EMPTY); // Transparent
            } else {
                BackgroundFill cellFill = new BackgroundFill(getFxColor(cellGroup.backgroundColor), CornerRadii.EMPTY, Insets.EMPTY);
                Background background = new Background(cellFill);
                cell.setBackground(background);
            }
            
            if (grid.showBorders) {
                cell.setBorder(new Border(stroke));
            }
            
            if (grid.padding > 0) {
                cell.setPadding(cellPadding);
            }

            currentColumn++;
            if (currentColumn > columns) {
                currentRow++;
                currentColumn = 1;
            }
            
            // TODO - Need:
            //              app.node.Grid
            //              app.node.StackPane
            //              This method needs to use publishNode() to establish the grid, then, for each grid.cells cell let publishNode iterate the list like the other collections, passing the grid as the parent
            //              
            //this.registerNode(viewName, decoratedNode, parentName, layout);
            this.namedFXNodes.get(viewName).put(cellGroup.name + " cell", cell); // TODO - Make a StackPane constructor for this
            this.addNode(viewName, cellGroup.name + " cell", cellGroup, null);
            //Pane box = newGroup(viewName, cellGroup, genericOffsetColor);
            //cell.getChildren().add(box);
            
            gridContent.add(cell, currentColumn - 1, currentRow - 1);
        }
        
        // TODO - Apply grid's layout
        tabContent.getChildren().add(gridContent);
    }
     */
 /*
    @Override
    public int displayImage(String viewName, String name, String fileName, int row, int column, Boolean fillParent) {
        System.out.println("JavaFXApplication: displayImage: viewName=" + viewName + ", name=" + name + ", fileName=" + fileName + ", row=" + row + ", column=" + column + ", fillParent=" + fillParent);
        
        Pane content = this.tabContentMap.get(viewName);

        final Image image = loadImage(fileName);        
        ImageView imageView = new ImageView(image);
        
        // TODO - To scale images, need something like this:
                
        int nextRow;
        if (fillParent) {
            //StackPane parentPane = new StackPane(content);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.fitWidthProperty().bind(content.widthProperty());
            imageView.fitHeightProperty().bind(content.heightProperty());
            content.getChildren().add(imageView);
            this.namedFXNodes.get(viewName).put(name, imageView);
            
            nextRow = 0; // Advance the text cursor automatically
        } else {
            Coordinates coordinates = this.convertToCoordinates(row, column);
            this.positionNodeOld(content, imageView, coordinates);
            
            Coordinates dimensions = getDimensions(fileName);
            Double relativeWidth = dimensions.x / content.getPrefWidth();
            Double relativeHeight = dimensions.y / content.getPrefHeight();
            imageView.fitWidthProperty().bind(content.widthProperty().multiply(relativeWidth));
            imageView.fitHeightProperty().bind(content.heightProperty().multiply(relativeHeight));
            imageView.setSmooth(true);
            
            content.getChildren().add(imageView);
            this.namedFXNodes.get(viewName).put(name, imageView);
            
            nextRow = row + this.getRows(dimensions.y); // Advance the text cursor automatically
        }
        
        return nextRow;
    }
     */
    public void positionNodeOld(Pane parent, Node childNode, Coordinates prefCoordinates) {
        // Position the node relative to the parent content
        Double paneWidth = parent.getPrefWidth();
        Double paneHeight = parent.getPrefHeight();
        Double relativePositionX = (prefCoordinates.x + 1) / paneWidth;
        Double relativePositionY = (prefCoordinates.y + 1) / paneHeight;
        childNode.layoutXProperty().bind(parent.widthProperty().multiply(relativePositionX));
        childNode.layoutYProperty().bind(parent.heightProperty().multiply(relativePositionY));
        System.out.println("JavaFXApplication: positionNode: parent=" + parent + ", childNode=" + childNode + ", prefCoordinates=" + prefCoordinates + ", paneWidth=" + paneWidth + ", paneHeight=" + paneHeight + ", relativePositionX=" + relativePositionX + ", relativePositionY=" + relativePositionY);
    }

    @Override
    public void refreshView(String viewName) {
        System.out.println("JavaFXApplication: refreshView: viewName=" + viewName);

        BaseView view = this.views.get(viewName);
        int index = this.getTabIndex(viewName);
        this.removeTab(viewName);
        this.addView(view, index, true);
    }

    public static Text stringToText(String string, RGBColor offsetColor, TextDecoration decoration, FontSmoothingType fst) {
        logger.log(Level.FINE, "Entered: string={0}, offsetColor={1}, decoration={2}, fst={3}", new Object[]{string, offsetColor, decoration, fst});

        Text text = new Text(string);

        if (decoration == null) {
            decoration = new TextDecoration();
        }

        // Configure the font
        if (decoration.style == null) {
            decoration.style = FontStyle.NORMAL;
        }

        FontPosture fxStyle = null;
        FontWeight fxWeight = FontWeight.NORMAL;
        switch (decoration.style) {
            case FontStyle.NORMAL ->
                fxWeight = FontWeight.NORMAL;
            case FontStyle.BOLD ->
                fxWeight = FontWeight.BOLD;
            case FontStyle.ITALIC ->
                fxStyle = FontPosture.ITALIC;
            case FontStyle.UNDERLINE_DOUBLE -> {
                // TODO - Not supported, needs styling
                fxWeight = FontWeight.NORMAL;
                text.setUnderline(true);
            }
            case FontStyle.UNDERLINE_ERROR -> {
                // TODO - Not supported, needs styling
                fxWeight = FontWeight.NORMAL;
                text.setUnderline(true);
            }
            case FontStyle.UNDERLINE_LINK -> {
                fxWeight = FontWeight.BOLD;
                text.setUnderline(true);
                if (offsetColor.equals(app.color.Color.WHITE)) {
                    decoration.color = app.color.Color.HYPERLINK_LIGHT_BLUE;
                    text.setOnMouseEntered(e -> text.setFill(getFxColor(app.color.Color.HYPERLINK_BLUE)));
                    text.setOnMouseExited(e -> text.setFill(getFxColor(app.color.Color.HYPERLINK_LIGHT_BLUE)));
                } else {
                    decoration.color = app.color.Color.HYPERLINK_BLUE;
                    text.setOnMouseEntered(e -> text.setFill(getFxColor(app.color.Color.HYPERLINK_LIGHT_BLUE)));
                    text.setOnMouseExited(e -> text.setFill(getFxColor(app.color.Color.HYPERLINK_BLUE)));
                }
                text.setCursor(Cursor.HAND);
                TextDecoration finalDecoration = decoration;
                text.setOnMouseClicked(e -> {
                    logger.log(Level.INFO, "Link selected: text={0}", string);
                    finalDecoration.eventListener.onEvent(string, null);
                });
            }
            case FontStyle.UNDERLINE_SINGLE -> {
                fxWeight = FontWeight.NORMAL;
                text.setUnderline(true);
            }
            case FontStyle.UNDERLINE_SQUIGGLE -> {
                // TODO - Not supported, needs styling
                fxWeight = FontWeight.NORMAL;
                text.setUnderline(true);
            }
            default -> {
                System.err.println("JavaFXApplication: stringToText: Unsupported font style!");
                fxWeight = FontWeight.NORMAL;
            }
        }

        // Configure the text font
        if (decoration.font == null) {
            decoration.font = Font.getDefault().getName();
            logger.log(Level.FINE, "Using default font", decoration.font);
        } else {
            // Load all font files for the font family if not already loaded.
            // The font size is required for loading a font but subsequent calls can use any size once loaded.
            if (!fontFamiliesLoaded.contains(decoration.font)) {
                List<String> fontFiles = app.Font.getFontFiles(decoration.font);
                for (String fontFileName : fontFiles) {
                    logger.log(Level.FINE, "Loading font file", fontFileName);
                    Font.loadFont(JavaFXApplication.class.getResourceAsStream(fontFileName), decoration.pixelSize);
                }
                if (!fontFiles.isEmpty()) {
                    fontFamiliesLoaded.add(decoration.font);
                } else {
                    logger.log(Level.WARNING, "Unsupported font family, using default", new Object[]{decoration.font, Font.getDefault().getName()});
                    decoration.font = Font.getDefault().getName();
                }
            }
        }

        // Adjust for DPI
        if (decoration.pixelSize == null) {
            decoration.pixelSize = DEFAULT_FONT_SIZE;
        }
        double newFontSize = adjustFontSizeForDPI(decoration.pixelSize);
        Font font;
        if (fxStyle != null) {
            font = Font.font(decoration.font, fxStyle, newFontSize);
        } else {
            font = Font.font(decoration.font, fxWeight, newFontSize);
        }
        text.setFont(font);
        if (decoration.color != null) {
            text.setFill(getFxColor(decoration.color));
        }

        text.setFontSmoothingType(fst);
        text.setSmooth(true);
        
        // By default, JavaFX measures text bounds using Logical line heights (including font design spaces like ascenders and descenders), which can mess up vertical sub-pixel layouts
        text.setBoundsType(TextBoundsType.VISUAL);

        return text;
    }

    public static void loadEmojiData() {
        System.out.println("JavaFXApplication: loadEmojiData");

        if (EMOJI_SHEET == null) {
            System.out.println("JavaFXApplication: loadEmojiData: Loading emoji sheet");
            EMOJI_SHEET = loadImage(EMOJI_SHEET_FILE);
        }

        if (EMOJI_MAP == null) {
            System.out.println("JavaFXApplication: loadEmojiData: Loading emoji map");

            EMOJI_MAP = new HashMap();

            // Build a map of every emoji according to its unified value
            try (InputStream inputStream = JavaFXApplication.class.getResourceAsStream(EMOJI_SHEET_JSON)) {
                if (inputStream == null) {
                    System.err.println("JavaFXApplication: loadEmojiData: Failed to find emoji sheet json! " + EMOJI_SHEET_JSON);
                } else {
                    int count = 0;
                    String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonArray emojiList = JsonParser.parseString(json).getAsJsonArray();
                    for (JsonElement element : emojiList) {
                        JsonObject emojiObj = element.getAsJsonObject();
                        String unified = emojiObj.get("unified").getAsString();
                        EMOJI_MAP.put(unified, emojiObj);
                        count++;
                    }
                    System.out.println("JavaFXApplication: loadEmojiData: Mapped " + count + " emoji objects");
                }
            } catch (IOException e) {
                System.err.println("JavaFXApplication: loadEmojiData: Failed to read emoji sheet json! " + e.toString());
            }
        }
    }

    public static Boolean isEmoji(String string) {
        // Check if the cluster contains a character intended to be an emoji
        boolean isVisualEmoji = string.codePoints()
                .anyMatch(cp -> Character.isEmojiPresentation(cp)
                || Character.isEmojiModifier(cp)
                || cp == 0xFE0F); // VS-16: Forces emoji presentation
        return isVisualEmoji;
    }

    public static String getEmojiUnifiedValue(String emoji) {
        String unifiedValue = emoji.codePoints()
                .mapToObj(cp -> String.format("%04X", cp))
                .collect(Collectors.joining("-"));
        return unifiedValue;
    }

    public static ImageView stringToEmoji(String string, TextDecoration decoration) {
        if (!isEmoji(string)) {
            return null;
        }

        // Use lazy loading for the sprite sheet in case it's never needed
        if (EMOJI_SHEET == null) {
            loadEmojiData();
        }

        String emojiUnified = getEmojiUnifiedValue(string);
        if (!EMOJI_MAP.containsKey(emojiUnified)) {
            System.out.println("JavaFXApplication: stringToEmoji: Emoji does not exist in the emoji json! string=" + string + ", unified=" + emojiUnified);
            return null;
        }

        JsonObject emojiObj = EMOJI_MAP.get(emojiUnified);
        ImageView emojiView = new ImageView(EMOJI_SHEET);
        double sheetX = Double.parseDouble(emojiObj.get("sheet_x").getAsString());
        double sheetY = Double.parseDouble(emojiObj.get("sheet_y").getAsString());
        // Every emoji image in the sheet has a 1 pixel transparent border around it, so the 64px sheet is made up of 66px squares
        double x = (sheetX * (EMOJI_SHEET_SIZE + 2)) + 1;
        double y = (sheetY * (EMOJI_SHEET_SIZE + 2)) + 1;
        emojiView.setViewport(new Rectangle2D(x, y, EMOJI_SHEET_SIZE, EMOJI_SHEET_SIZE));
        emojiView.setSmooth(true); // Enables better scaling algorithm
        emojiView.setCache(true);  // Can help with performance in a long TextFlow
        emojiView.setCacheHint(CacheHint.QUALITY);
        double pixelSize;
        if ((decoration == null) || (decoration.pixelSize == null)) {
            pixelSize = DEFAULT_FONT_SIZE;
        } else {
            pixelSize = decoration.pixelSize;
        }
        pixelSize = adjustFontSizeForDPI(pixelSize);
        emojiView.setFitHeight(pixelSize);
        emojiView.setPreserveRatio(true);

        if ((decoration != null) && (decoration.style == FontStyle.UNDERLINE_LINK)) {
            emojiView.setOnMouseEntered(e -> emojiView.setOpacity(0.8));
            emojiView.setOnMouseExited(e -> emojiView.setOpacity(1.0));
            emojiView.setCursor(Cursor.HAND);
            TextDecoration finalDecoration = decoration;
            emojiView.setOnMouseClicked(e -> {
                logger.log(Level.INFO, "Link selected: text={0}", string);
                finalDecoration.eventListener.onEvent(string, null);
            });
        }

        return emojiView;
    }

    public static TextFlow stringToTextFlow(String string, RGBColor offsetColor, TextDecoration decoration, FontSmoothingType fst) {
        logger.log(Level.INFO, "Entered: string={0}, offsetColor={1}, decoration={2}, fst={3}", new Object[]{string, offsetColor, decoration, fst});

        if ((string == null) || (string.isEmpty())) {
            return new TextFlow();
        }

        TextFlow textFlow = new TextFlow();

        String nonEmojiText = "";

        // \X matches a "Unicode extended grapheme cluster" (the full emoji)
        Matcher matcher = Pattern.compile("\\X").matcher(string);
        while (matcher.find()) {
            String cluster = matcher.group();
            // Check if the cluster contains a character intended to be an emoji
            boolean isEmoji = isEmoji(cluster);
            if (isEmoji) {
                // Add and reset accumulated normal text
                if (!nonEmojiText.isEmpty()) {
                    Text textNode = stringToText(nonEmojiText, offsetColor, decoration, fst);
                    textFlow.getChildren().add(textNode);
                    nonEmojiText = "";
                }

                // Emoji - Add an ImageView
                System.out.println("JavaFXApplication: stringToTextFlow: Handling emoji: " + cluster);
                ImageView emojiView = stringToEmoji(cluster, decoration);
                textFlow.getChildren().add(emojiView);
            } else {
                // Normal text - Accumulate character(s)
                nonEmojiText += cluster;
            }
        }

        if (!nonEmojiText.isEmpty()) {
            Text textNode = stringToText(nonEmojiText, offsetColor, decoration, fst);
            textFlow.getChildren().add(textNode);
        }
        
        textFlow.setLineSpacing(1.0); 
        textFlow.setCache(false);
        
        // Add warnings for state that can lead to blurry text
        textFlow.layoutXProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() % 1 != 0) {
                logger.log(Level.WARNING, "TextFlow LayoutX is fractional: {0}\n", newVal.doubleValue());
            }
        });
        textFlow.layoutYProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() % 1 != 0) {
                logger.log(Level.WARNING, "TextFlow LayoutY is fractional: {0}\n", newVal.doubleValue());
            }
        });
        textFlow.boundsInParentProperty().addListener((obs, oldBounds, newBounds) -> {
            if ((newBounds.getMinX() % 1 != 0) || (newBounds.getMinY() % 1 != 0) || (newBounds.getWidth() % 1 != 0) || (newBounds.getHeight() % 1 != 0)) {
                logger.log(Level.WARNING, "TextFlow bounds is fractional: MinX: {0}, MinY: {1}, Width: {2}, Height: {3}\n",
                    new Object[]{newBounds.getMinX(), newBounds.getMinY(), newBounds.getWidth(), newBounds.getHeight()});
            }
            
        });

        return textFlow;
    }

    /*
    @Override
    public void displayFloatingText(String viewName, String name, String text, Integer startRow, Integer startColumn, Integer endRow, Integer endColumn, app.Color fontColor, Integer fontSize, Integer fontStyle, String fontName) {
        System.out.println("JavaFXApplication: displayFloatingText: viewName=" + viewName + ", name=" + name + ", text=" + text + ", startRow=" + startRow + ", startColumn=" + startColumn + ", endRow=" + endRow + ", endColumn=" + endColumn + ", fontColor=" + fontColor + ", fontSize=" + fontSize + ", fontName=" + fontName);
        
        // TODO - Instead of start/end row and column, provide a percentage that indicates position according to the parent
        
        Pane content = this.tabContentMap.get(viewName);

        TextFlow textFlow = stringToTextFlow(text, fontName, fontColor, fontSize, fontStyle);
        textFlow.setStyle("-fx-background-color: transparent;");
        
        if (endColumn != null) {
            int x1 = (int) ((startColumn - 1) * this.fontWidth) - this.fontWidth;
            int x2 = (int) ((endColumn - 1) * this.fontWidth) - this.fontWidth;
            int width = x2 - x1;
            textFlow.setPrefWidth(width);
        }
        
        if (endRow != null) {
            int y1 = (int) ((startRow - 1) * this.fontHeight) - this.fontHeight;
            int y2 = (int) ((endRow - 1) * this.fontHeight) - this.fontHeight;
            int height = y2 - y1;
            textFlow.setPrefHeight(height);
        }
        
        // TODO - Need a method param for alignment
        if ((endRow != null) && (endColumn != null)) {
            textFlow.setTextAlignment(TextAlignment.CENTER);
        }
                
        // Position the node
        Coordinates startCoordinates = this.convertToCoordinates(startRow, startColumn);
        this.positionNodeOld(content, textFlow, startCoordinates);
        //textFlow.setLayoutX(startCoordinates.x);
        //textFlow.setLayoutY(startCoordinates.y);
        
        // Add the node to the parent
        content.getChildren().add(textFlow);
        
        if (name != null) {
            this.namedFXNodes.get(viewName).put(name, textFlow);
        }
        
        // TODO - Return the final height
        // textFlow.layout(); // Force a layout pass to ensure all bounds are updated
        // double finalHeight = textFlow.getBoundsInLocal().getHeight();
    }
     */
 /*
    @Override
    public void displayInputField(String viewName, String name, String label, int length, int row, int column, String initValue, Boolean addButton, Boolean isMonospace, Boolean isUpperCase, Boolean isMultiUse, EventListener listener) {
        System.out.println("JavaFXApplication: displayInputField: viewName=" + viewName + ", name=" + name + ", label=" + label + ", length=" + length + ", row=" + row + ", column=" + column + ", initValue=" + initValue + ", addButton=" + addButton + ", isMonospace=" + isMonospace + ", isUpperCase=" + isUpperCase + ", isMultiUse=" + isMultiUse + ", listener=" + listener);
        
        Pane content = this.tabContentMap.get(viewName);
        
        TextField field = new TextField();
        field.setPromptText(label);
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (isUpperCase) {
                // Apply the uppercase conversion to the new text
                change.setText(change.getText().toUpperCase());
            }

            // Enforce the character limit
            if (change.getControlNewText().length() > length) {
                return null; // Reject the change
            }
            
            if (!addButton) {
                // Raise an event for each entered character
                listener.onEvent(name, change.getControlNewText());
            }
            
            return change; // Accept the change
        });
        field.setTextFormatter(textFormatter);
        
        Font font;
        if (isMonospace) {
            font = this.monospaceFont;
        } else {
            font = this.buttonFont;
        }
        field.setFont(font);
        
        Coordinates startCoordinates = this.convertToCoordinates(row, column);
        this.positionNodeOld(content, field, startCoordinates);
        //field.setLayoutX(startCoordinates.x);
        //field.setLayoutY(startCoordinates.y);
        
        if ((initValue != null) && (!initValue.equals(""))) {
            field.setText(initValue);
        }
        
        content.getChildren().add(field);
        
        // Handle entered text
        if (addButton) {
            // Display a button for submitting the input
            Button button = this.newButton(viewName, name, "Submit", row, column + length + 1 + 1, null, null, isMonospace, null, false, listener);
            button.setOnAction(e -> {
                    listener.onEvent(name, field.getText());
                    if (isMultiUse) {
                        field.setText("");
                    } else {
                        field.setDisable(true);
                        button.setDisable(true);
                    }
                }
            );
        }
    }
     */
    public FlowPane newButtonGroup(app.node.ButtonGroup node, FlowPane fxButtonGroup, RGBColor offsetColor) {
        logger.log(Level.INFO, "Entered: node={0}, fxButtonGroup={1}, offsetColor={2}", new Object[]{node, fxButtonGroup, offsetColor});

        if (fxButtonGroup == null) {
            fxButtonGroup = new FlowPane();
        }

        if (node.spacerPixels != null) {
            fxButtonGroup.setHgap(node.spacerPixels);
            fxButtonGroup.setVgap(node.spacerPixels);
            fxButtonGroup.setPadding(new Insets(node.spacerPixels));
        }

        if (node.backgroundColor != null) {
            Color fxBackgroundColor = getFxColor(node.backgroundColor);
            BackgroundFill backgroundFill = new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY);
            Background background = new Background(backgroundFill);
            fxButtonGroup.setBackground(background);
        }

        return fxButtonGroup;
    }

    /*
    @Override
    public void displayValidatedInputField(String viewName, String name, List<String> values, int row, int startColumn, int endColumn, Layout layout, EventListener listener, Boolean allowRepeatClicks) {
        System.out.println("JavaFXApplication: displayValidatedInputField: viewName=" + viewName + ", name=" + name + ", row=" + row + ", startColumn=" + startColumn + ", endColumn=" + endColumn + ", layout=" + layout + ", listener=" + listener + ", allowRepeatClicks=" + allowRepeatClicks);
        
        // TODO - param "alignment" is not supported
        // TODO - If using a custom font, calculate its dimensions

        Pane content = this.tabContentMap.get(viewName);
        Coordinates coordinates = this.convertToCoordinates(row, startColumn);
        Coordinates terminalCoordinates = this.convertToCoordinates(row + 2, endColumn);
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        flowPane.setPadding(new Insets(10));
        content.getChildren().add(flowPane);
        this.positionNodeOld(content, flowPane, coordinates);
        //flowPane.setLayoutX(coordinates.x);
        //flowPane.setLayoutY(coordinates.y);
        flowPane.setPrefWidth(terminalCoordinates.x - coordinates.x);
        System.out.println("JavaFXApplication: displayValidatedInputField: animation placed at " + coordinates.x + " , " + coordinates.y + ", width=" + flowPane.getMaxWidth() + ", height=" + flowPane.getMaxHeight());
        
        // Display a row of buttons with the possible input values
        int buttonHeight = 2 * this.buttonFontHeight;   // Calculate double height of text
        
        Boolean disable;
        for (String value : values) {
            if (value.charAt(0) == '!') {
                // TODO - This is just a hack to support disabling buttons
                value = value.substring(1, value.length());
                disable = true;
            } else {
                disable = false;
            }
            Boolean glow = false;
            if (value.charAt(0) == '*') {
                value = value.substring(1, value.length());
                glow = true;
            }
            KeyCode keyBinding = null;
            String eventValue = value;
            if (value.toUpperCase().contains("&UP;")) {
                keyBinding = KeyCode.UP;
                eventValue = value.replaceFirst("(?i)" + "&UP;", "");
                value = value.replaceFirst("(?i)" + "&UP;", "\u2B06");  // Case insensitive reg ex
            } else if (value.toUpperCase().contains("&DOWN;")) {
                keyBinding = KeyCode.DOWN;
                eventValue = value.replaceFirst("(?i)" + "&DOWN;", "");
                value = value.replaceFirst("(?i)" + "&DOWN;", "\u2B07");  // Case insensitive reg ex
            } else if (value.toUpperCase().contains("&LEFT;")) {
                keyBinding = KeyCode.LEFT;
                eventValue = value.replaceFirst("(?i)" + "&LEFT;", "");
                value = value.replaceFirst("(?i)" + "&LEFT;", "\u2190");  // Case insensitive reg ex
            } else if (value.toUpperCase().contains("&RIGHT;")) {
                keyBinding = KeyCode.RIGHT;
                eventValue = value.replaceFirst("(?i)" + "&RIGHT;", "");
                value = value.replaceFirst("(?i)" + "&RIGHT;", "\u27A1");  // Case insensitive reg ex
            }
            final String finalValue = eventValue;
            Button button = new Button(value);
            if (disable) {
                button.setDisable(true);
            }
            int buttonWidth = (value.length() * this.buttonFontWidth) + (1 * this.buttonFontWidth); // Add one character for padding
            button.setPrefSize(buttonWidth, buttonHeight);
            button.setFont(this.buttonFont);
            button.setOnAction(e -> {
                if (!allowRepeatClicks) {
                    button.setDisable(true);
                    button.setStyle(null);
                }
                listener.onEvent(name, finalValue);
            });
            
            if (keyBinding != null) {
                final KeyCode keyBindingLamda = keyBinding;
                EventHandler<KeyEvent> arrowKeyHandler = event -> {
                    if (event.getCode() == keyBindingLamda) {
                        button.fire();
                        event.consume(); // Stop TabPane from using it
                    }
                };
                this.primaryScene.addEventFilter(KeyEvent.KEY_PRESSED, arrowKeyHandler);
                this.keyBindings.put(button, arrowKeyHandler);
            }
            
            // TODO - newButton should be used to prevent code duplication
            if (glow) {
                String defaultStyle = "-fx-effect: dropshadow(three-pass-box, rgba(139, 0, 139, 0.8), 5, 0.8, 0, 0);";
                String hoverStyle = "-fx-effect: dropshadow(three-pass-box, rgba(139, 0, 139, 1), 10, 0.8, 0, 0);";
                button.setStyle(defaultStyle);
                button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
                button.setOnMouseExited(e -> button.setStyle(defaultStyle));
            }
            
            flowPane.getChildren().add(button);
        }
        
        this.namedFXNodes.get(viewName).put(name, flowPane);
    }
    
    //@Override
    public void displayGifOrig(String viewName, String fileName, int row, int column) {
        System.out.println("JavaFXApplication: displayGif: viewName=" + viewName + ", fileName=" + fileName + ", row=" + row + ", column=" + column);
        
        Pane content = this.tabContentMap.get(viewName);
        
        // Prepare the image
        Image gif = loadImage(fileName);
        ImageView gifView = new ImageView(gif);
        gifView.setPreserveRatio(true);
        
        // Position the image
        Coordinates coordinates = this.convertToCoordinates(row, column);
        this.positionNodeOld(content, gifView, coordinates);
        //gifView.setLayoutX(coordinates.x);
        //gifView.setLayoutY(coordinates.y);
        
        // Add the image
        content.getChildren().add(gifView);
    }
     */
    public static int getFrameDelay(IIOMetadata metadata) {
        String metadataFormat = metadata.getNativeMetadataFormatName();
        org.w3c.dom.Node root = metadata.getAsTree(metadataFormat);
        org.w3c.dom.Node graphicsControlExtension = getNode(root, "GraphicControlExtension");

        if (graphicsControlExtension != null) {
            NamedNodeMap attributes = graphicsControlExtension.getAttributes();
            org.w3c.dom.Node delayTimeNode = attributes.getNamedItem("delayTime");
            if (delayTimeNode != null) {
                try {
                    // Delay time is in hundredths of a second (centiseconds)
                    int delay = Integer.parseInt(delayTimeNode.getNodeValue());
                    // Convert to milliseconds
                    return delay * 10;
                } catch (NumberFormatException e) {
                    // Handle parse error, return default
                }
            }
        }
        return 100; // Default to 100ms if info can't be found
    }

    public static org.w3c.dom.Node getNode(org.w3c.dom.Node rootNode, String nodeName) {
        for (int i = 0; i < rootNode.getChildNodes().getLength(); i++) {
            if (rootNode.getChildNodes().item(i).getNodeName().equalsIgnoreCase(nodeName)) {
                return rootNode.getChildNodes().item(i);
            }
        }
        return null;
    }

    @Override
    public void setTimer(String name, double seconds, EventListener listener) {
        System.out.println("JavaFXApplication: setTimer: name=" + name + ", seconds=" + seconds + ", listener=" + listener);
        if (TIMER_EVENTS.contains(name)) {
            System.out.println("JavaFXApplication: setTimer: Timer already exists for " + name + "!");
            return;
        }
        Timeline timeline = new Timeline();
        TIMER_EVENTS.add(name);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(seconds), (ActionEvent event) -> {
            System.out.println("JavaFXApplication: setTimer: Timer elapsed: name=" + name + ", seconds=" + seconds + ", listener=" + listener);
            if (!TIMER_EVENTS.contains(name)) {
                System.out.println("JavaFXApplication: setTimer: Timer " + name + " was removed!");
                return;
            }
            TIMER_EVENTS.remove(name);
            listener.onEvent(name, seconds);
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }

    @Override
    public void removeTimer(String name) {
        System.out.println("JavaFXApplication: removeTimer: name=" + name);
        if (!TIMER_EVENTS.contains(name)) {
            System.out.println("JavaFXApplication: removeTimer: Timer " + name + " was already removed!");
        } else {
            TIMER_EVENTS.remove(name);
        }
        // TODO - Could store the timeline and call timeline.stop();
    }

    public static Coordinates getDimensions(String imageFileName) {
        logger.log(Level.INFO, "Entered: imageFileName={0}", imageFileName);
        Image image = loadImage(imageFileName);
        return getDimensions(image);
    }
    
    public static Coordinates getDimensions(Image image) {
        logger.log(Level.INFO, "Entered: image={0}", image);

        if (image == null) {
            return new Coordinates(0, 0);
        }

        Coordinates dimensions = new Coordinates((int) image.getWidth(), (int) image.getHeight());

        return dimensions;
    }

    public static Image loadImage(String fileName) {
        System.out.println("JavaFXApplication: loadImage: fileName=" + fileName);

        Image image = null;
        try (InputStream inputStream = JavaFXApplication.class.getResourceAsStream(fileName)) {
            if (inputStream != null) {
                image = new Image(inputStream);
            } else {
                System.err.println("JavaFXApplication: getDimensions: Image not found!  fileName=" + fileName);
            }
        } catch (Exception e) {
            System.err.println("JavaFXApplication: getDimensions: Error loading image!  fileName=" + fileName);
        }

        return image;
    }

    @Override
    public void addAnimation(String viewName, String name, int row, int column, String backgroundImageFileName, List<String> imageFiles, double animationDelay, Animation listener) {
        System.out.println("JavaFXApplication: addAnimation: viewName=" + viewName + ", name=" + name + ", row=" + row + ", column=" + column + ", backgroundImageFileName=" + backgroundImageFileName + ", image file count=" + imageFiles.size() + ", animationDelay=" + animationDelay + ", listener=" + listener);

        // Cache each sprite image
        Map<String, Image> spriteImages = new HashMap();
        for (String imageFile : imageFiles) {
            if (spriteImages.containsKey(imageFile)) {
                continue;
            }
            Image spriteImage = this.loadImage(imageFile);
            spriteImages.put(imageFile, spriteImage);
        }

        // Place the animation background
        //Coordinates topLeft = this.convertToCoordinates(row, column);
        // TODO - This should be handled by a Layout
        Coordinates topLeft = new Coordinates(0, 0);
        Image backgroundImage = this.loadImage(backgroundImageFileName);
        ImageView backgroundImageView = new ImageView(backgroundImage);
        Coordinates animationDimensions = this.getDimensions(backgroundImageFileName);
        backgroundImageView.setFitWidth(animationDimensions.x);
        backgroundImageView.setFitHeight(animationDimensions.y);
        backgroundImageView.setLayoutX(0);
        backgroundImageView.setLayoutY(0);
        Pane animationBackground = new Pane();
        animationBackground.getChildren().add(backgroundImageView);

        // Set a timer to fetch and display the current sprites
        PauseTransition pause = new PauseTransition(Duration.seconds(animationDelay));
        pause.setOnFinished(event -> animate(viewName, topLeft, pause, listener, animationDimensions, animationBackground, backgroundImageView, spriteImages));
        pause.play();
    }

    public void animate(String viewName, Coordinates topLeft, PauseTransition pause, Animation listener, Coordinates animationDimensions, Pane animationBackground, ImageView backgroundImageView, Map<String, Image> spriteImages) {
        // Retrieve updated sprites
        List<Sprite> sprites = listener.onAnimate();

        // Clean up the animation 
        if (sprites == null) {
            System.out.println("JavaFXApplication: animate: No more sprite data, done");
            pause.stop();
            if (animationBackground.getParent() != null) {
                Pane content = this.tabContentMap.get(viewName);
                content.getChildren().remove(animationBackground);
            }
            return;
        }

        // Add animation background to the parent if needed.
        // (Expected for the first display of the animation and any subsequent refresh of the page.)
        if (animationBackground.getParent() == null) {
            System.out.println("JavaFXApplication: animate: No parent for animation background, adding to tab content");
            animationBackground.setPrefWidth(animationDimensions.x);
            animationBackground.setPrefHeight(animationDimensions.y);
            animationBackground.setLayoutX(topLeft.x + 1);
            animationBackground.setLayoutY(topLeft.y + 1);
            Pane content = this.tabContentMap.get(viewName);
            content.getChildren().add(animationBackground);
        }

        // Clear the animation background and re-add the sprites
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : animationBackground.getChildren()) {
            // Check if the node is an ImageView
            if (node instanceof ImageView currentImageView) {

                // Check if this ImageView is NOT the one we want to keep (using reference equality)
                if (currentImageView != backgroundImageView) {
                    nodesToRemove.add(node);
                }
            }
        }
        animationBackground.getChildren().removeAll(nodesToRemove);

        // Build a list of sprite image views
        List<ImageView> spriteImageViews = new ArrayList();
        Map<Sprite, ImageView> spriteImageViewMap = new HashMap();
        for (Sprite sprite : sprites) {
            Image spriteImage = spriteImages.get(sprite.imageFile);
            ImageView spriteView = new ImageView(spriteImage);
            spriteView.setLayoutX(sprite.x);
            spriteView.setLayoutY(sprite.y);
            //System.out.println("JavaFXApplication: animate: Added " + sprite.imageFile + " to " + sprite.x + ", " + sprite.y);

            // Scale each sprite relative to the size of the animation background
            //Double spriteHeight = spriteView.getFitHeight();
            //Double spriteWidth = spriteView.getFitWidth();
            Double scaledSpriteHeight = animationDimensions.y * sprite.imageScale;
            //Double scaledSpriteWidth = spriteWidth * (scaledSpriteHeight / spriteHeight);
            spriteView.setFitHeight(scaledSpriteHeight);
            spriteView.setPreserveRatio(true);
            //System.out.println("JavaFXApplication: animate: Sprite " + sprite.imageFile + " scale " + sprite.imageScale + ", height=" + spriteView.getFitHeight() + ", width=" + spriteView.getFitWidth());
            //spriteView.setFitWidth(scaledSpriteWidth);

            // Make the sprite size responsive to changes in the size of the animation background
            //spriteView.fitWidthProperty().bind(animationBackground.widthProperty());
            //spriteView.fitHeightProperty().bind(animationBackground.heightProperty());
            // Collect each ImageView in a list
            spriteImageViews.add(spriteView);
            spriteImageViewMap.put(sprite, spriteView);
        }

        // For each collision, apply a red tint effect to both image views.
        // Saturation 1.0 makes colors vivid.  Reducing it moves toward grayscale first.
        // Hue adjustment shifts the color spectrum.  Red is around 0.0 or -1.0/1.0.
        // Setting Hue to -0.3 is a good value to pull colors towards a strong red/magenta range.
        // Might need to experiment with values between -1.0 and 1.0 to find the perfect shade of red.
        // Increase brightness/contrast if the resulting image is too dark
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(1.0);
        colorAdjust.setHue(-0.3);
        colorAdjust.setBrightness(0.1);

        // Check for collisions
        for (Sprite sprite : spriteImageViewMap.keySet()) {
            if (sprite.potentialCollisionNames != null) {
                ImageView imageView = spriteImageViewMap.get(sprite);
                //System.out.println("JavaFXApplication: animate: level 4 : " + sprite.name);
                for (String potentialCollisionName : sprite.potentialCollisionNames) {
                    //System.out.println("JavaFXApplication: animate: level 3 : " + potentialCollisionName);
                    for (Sprite potentialCollisionSprite : spriteImageViewMap.keySet()) {
                        //System.out.println("JavaFXApplication: animate: level 2 : " + potentialCollisionSprite.name);
                        if ((potentialCollisionSprite.name != null) && (potentialCollisionSprite.name.equals(potentialCollisionName))) {
                            //System.out.println("JavaFXApplication: animate: level 1 : " + sprite.name + " vs " + potentialCollisionSprite.name);
                            ImageView potentialCollisionImageView = spriteImageViewMap.get(potentialCollisionSprite);
                            if (JavaFXApplication.isColliding(imageView, potentialCollisionImageView)) {
                                //System.out.println("JavaFXApplication: animate: Detected collision between " + sprite.name + " and " + potentialCollisionName);
                                // Update each sprite to reference the other
                                sprite.collisionSprites.add(potentialCollisionSprite);
                                potentialCollisionSprite.collisionSprites.add(sprite);
                                // Adjust the color of each sprite
                                imageView.setEffect(colorAdjust);
                                potentialCollisionImageView.setEffect(colorAdjust);
                                // Raise the sprite event for a collision on both sprites
                                sprite.onCollision(potentialCollisionSprite);
                                potentialCollisionSprite.onCollision(sprite);
                            }
                        }
                    }
                }
            }
        }

        // Glow sprites
        for (Sprite sprite : spriteImageViewMap.keySet()) {
            if (sprite.glowColor == null) {
                continue;
            }
            ImageView imageView = spriteImageViewMap.get(sprite);
            this.glowSprite(sprite, imageView, colorAdjust);
        }
        // TODO - Handle edge of animation background boundaries
        // This is disabled for now because preserving scaling and preserving the image's ratio causes the clipped image to grow
        /*
        int animationBackgroundWidth = (int) animationBackground.getWidth();
        int animationBackgroundHeight = (int) animationBackground.getHeight();
        for (SpriteModel sprite : spriteImageViewMap.keySet()) {
            if (sprite.viewPortBuffer > 0) {
                int x1Boundary = (int) Math.round(((double) animationBackgroundWidth) * sprite.viewPortBuffer);
                int y1Boundary = (int) Math.round(((double) animationBackgroundHeight) * sprite.viewPortBuffer);
                int x2Boundary = animationBackgroundWidth - x1Boundary;
                int y2Boundary = animationBackgroundHeight - y1Boundary;
                ImageView spriteView = spriteImageViewMap.get(sprite);
                Bounds bounds = spriteView.getBoundsInParent();
                double currentTopY = bounds.getMinY();
                double currentBottomY = bounds.getMaxY();
                double sourceImageHeight = spriteView.getImage().getHeight();

                if (currentBottomY < y2Boundary) {
                    // No clipping needed if we are just ensuring it doesn't show below threshold
                } else if (currentTopY >= y2Boundary) {
                    // Clip it completely (set height to 0 or 1 pixel, effectively hiding it)
                    spriteView.setViewport(new Rectangle2D(0, 0, spriteView.getImage().getWidth(), 1));
                } else {
                    // Calculate the distance from the parent's top (Y=0) to the clip threshold line (e.g., Y=250)
                    double distanceAboveThresholdInParent = y2Boundary - currentTopY;

                    // Calculate the total scaled height of the image in the parent system
                    double totalHeightInParent = bounds.getHeight();

                    // Determine what fraction of the total *scaled* image is above the threshold line
                    double fractionAboveLine = distanceAboveThresholdInParent / totalHeightInParent;

                    // Translate that fraction back to the height in *original source image pixels*
                    double visibleSourceHeight = sourceImageHeight * fractionAboveLine;

                    // Apply the new dynamic viewport
                    spriteView.setViewport(new Rectangle2D(
                        0, 
                        0, // Start Y coordinate at the very top (Y=0) of the source image
                        spriteView.getImage().getWidth(), 
                        visibleSourceHeight // Only show the calculated height of the top portion
                    ));
                }
            }
        }
         */

        Rectangle clipRectangle = new Rectangle();
        clipRectangle.setWidth(animationBackground.getWidth());
        clipRectangle.setHeight(animationBackground.getHeight());
        animationBackground.setClip(clipRectangle);

        // Re-add the sprites to the animation background
        for (ImageView spriteImageView : spriteImageViews) {
            animationBackground.getChildren().add(spriteImageView);
        }

        // Reset the timer
        pause.playFromStart();
    }

    public void glowSprite(Sprite sprite, ImageView spriteView, Effect currentEffect) {
        if (sprite.glowColor == null) {
            return;
        }

        DropShadow glow = new DropShadow();
        glow.setRadius(20);
        glow.setColor(getFxColor(sprite.glowColor));
        glow.setSpread(0.5);
        glow.setOffsetX(0);
        glow.setOffsetY(0);
        glow.setInput(currentEffect);
        spriteView.setEffect(glow);
    }

    public static boolean isColliding(ImageView node1, ImageView node2) {
        // 1. Check for bounding box overlap in the scene coordinates
        // We use localToParent transform to get bounds relative to a common parent/scene
        Bounds bounds1 = node1.localToParent(node1.getBoundsInLocal());
        Bounds bounds2 = node2.localToParent(node2.getBoundsInLocal());

        if (!bounds1.intersects(bounds2)) {
            return false; // No overlap at all
        }

        // TODO - For performance, use app.javafx.CollisionMask
        //return mask.intersects(otherMask, this.getX(), this.getY(), other.getX(), other.getY());
        // Determine the overlapping rectangle in scene coordinates
        double intersectionMinX = Math.max(bounds1.getMinX(), bounds2.getMinX());
        double intersectionMinY = Math.max(bounds1.getMinY(), bounds2.getMinY());
        double intersectionMaxX = Math.min(bounds1.getMaxX(), bounds2.getMaxX());
        double intersectionMaxY = Math.min(bounds1.getMaxY(), bounds2.getMaxY());

        // Iterate through every pixel in the intersection area (in scene coordinates)
        for (double sceneX = intersectionMinX; sceneX < intersectionMaxX; sceneX += 1) {
            for (double sceneY = intersectionMinY; sceneY < intersectionMaxY; sceneY += 1) {

                // 2. Translate scene coordinates back to local coordinates for each ImageView
                // localToParent().inverse() gets you back to the node's local bounds
                // The coordinates returned will be relative to the ImageView's top-left, scaled bounds
                double iv1LocalX = node1.parentToLocal(sceneX, sceneY).getX();
                double iv1LocalY = node1.parentToLocal(sceneX, sceneY).getY();
                double iv2LocalX = node2.parentToLocal(sceneX, sceneY).getX();
                double iv2LocalY = node2.parentToLocal(sceneX, sceneY).getY();

                // 3. Translate local (scaled) coordinates to original image coordinates
                double iv1OriginalX = translateToOriginalX(node1, iv1LocalX);
                double iv1OriginalY = translateToOriginalY(node1, iv1LocalY);
                double iv2OriginalX = translateToOriginalX(node2, iv2LocalX);
                double iv2OriginalY = translateToOriginalY(node2, iv2LocalY);

                // 4. Perform the pixel-perfect check
                double alpha1 = JavaFXApplication.getPixelAlpha(node1.getImage(), iv1OriginalX, iv1OriginalY);
                double alpha2 = JavaFXApplication.getPixelAlpha(node2.getImage(), iv2OriginalX, iv2OriginalY);

                if (alpha1 >= 0.0001 && alpha2 >= 0.0001) {
                    // Collision detected at this specific pixel!
                    return true;
                }
            }
        }

        return false; // No colliding pixels found in the intersection area
    }

    public static double getPixelAlpha(Image image, double x, double y) {
        // Check bounds first
        if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
            PixelReader pr = image.getPixelReader();
            if (pr != null) {
                // Read color and return alpha component
                return pr.getColor((int) x, (int) y).getOpacity();
            }
        }
        return 0.0; // Transparent if out of bounds or no reader
    }

    public static double translateToOriginalX(ImageView iv, double localX) {
        return localX * (iv.getImage().getWidth() / iv.getBoundsInLocal().getWidth());
    }

    public static double translateToOriginalY(ImageView iv, double localY) {
        return localY * (iv.getImage().getHeight() / iv.getBoundsInLocal().getHeight());
    }

    @Override
    public void playSound(String fileName, Boolean isLoop) {
        System.out.println("JavaFXApplication: playSound: fileName=" + fileName + ", isLoop=" + isLoop);

        if (IS_JPRO) {
            logger.log(Level.WARNING, "Sound is not supported for JPro environments");
            return;
        }

        URL resource = getClass().getResource(fileName);
        if (resource == null) {
            System.err.println("JavaFXApplication: playSound: File not found!");
            return;
        }

        this.audioLock.lock();
        System.out.println("JavaFXApplication: playSound: Claimed lock");

        try {
            Media media = new Media(resource.toURI().toString());
            final MediaPlayer mediaPlayer = new MediaPlayer(media);
            if (this.mediaPlayers.containsKey(fileName)) {
                List<MediaPlayer> list = this.mediaPlayers.get(fileName);
                list.add(mediaPlayer);
                System.out.println("JavaFXApplication: playSound: Added new collection for file");
            } else {
                List<MediaPlayer> list = new ArrayList();
                list.add(mediaPlayer);
                this.mediaPlayers.put(fileName, list);
                System.out.println("JavaFXApplication: playSound: Added file to collection");
            }
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(() -> {
                this.audioLock.lock();
                System.out.println("JavaFXApplication: playSound: End of media: Claimed lock");
                try {
                    if (isLoop) {
                        mediaPlayer.seek(javafx.util.Duration.ZERO);
                    } else {
                        List<MediaPlayer> playerList = this.mediaPlayers.get(fileName);
                        if (playerList != null) {
                            playerList.remove(mediaPlayer);
                            if (playerList.isEmpty()) {
                                this.mediaPlayers.remove(fileName);
                            }
                        }
                        mediaPlayer.dispose();
                    }
                } catch (Exception e) {
                    System.err.println("JavaFXApplication: playSound: End of media: Error: " + e.getMessage());
                } finally {
                    this.audioLock.unlock();
                }
            });
        } catch (Exception e) {
            System.err.println("JavaFXApplication: playSound: Error: " + e.getMessage());
        } finally {
            this.audioLock.unlock();
        }

        // Fallback code for troublshooting whether missing codecs are to blame
        /*
        AudioClip audioClip = new AudioClip(resource.toExternalForm());
        audioClip.setVolume(0.8);
        audioClip.play();
         */
    }

    @Override
    public void stopSound(String fileName, Boolean removeAudioPlayer) {
        System.out.println("JavaFXApplication: stopSound: fileName=" + fileName + ", removeAudioPlayer=" + removeAudioPlayer);

        if (IS_JPRO) {
            logger.log(Level.WARNING, "Sound is not supported for JPro environments");
            return;
        }

        if (!this.mediaPlayers.containsKey(fileName)) {
            System.out.println("JavaFXApplication: stopSound: Collection for file not found");
            return;
        }

        this.audioLock.lock();
        System.out.println("JavaFXApplication: stopSound: Claimed lock");

        try {
            List<MediaPlayer> list = this.mediaPlayers.get(fileName);
            for (MediaPlayer mediaPlayer : list) {
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.stop();
                    System.out.println("JavaFXApplication: stopSound: Stopped media");
                }
                if (removeAudioPlayer) {
                    HashMap<String, List<MediaPlayer>> allMediaPlayers = this.mediaPlayers;
                    list.remove(mediaPlayer);
                    if (list.isEmpty()) {
                        allMediaPlayers.remove(fileName);
                        mediaPlayer.dispose();
                        System.out.println("JavaFXApplication: stopSound: Removed media player");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("JavaFXApplication: stopSound: Error: " + e.getMessage());
        } finally {
            this.audioLock.unlock();
        }
    }

    @Override
    public void stopAllSounds() {
        System.out.println("JavaFXApplication: stopAllSounds");

        if (IS_JPRO) {
            logger.log(Level.WARNING, "Sound is not supported for JPro environments");
            return;
        }

        this.audioLock.lock();
        System.out.println("JavaFXApplication: stopAllSounds: Claimed lock");

        try {
            for (String fileName : this.mediaPlayers.keySet()) {
                for (MediaPlayer mediaPlayer : this.mediaPlayers.get(fileName)) {
                    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        mediaPlayer.stop();
                        mediaPlayer.dispose();
                        System.out.println("JavaFXApplication: stopSound: Stopped media : " + fileName);
                    }
                }
            }
            this.mediaPlayers.clear();
        } catch (Exception e) {
            System.err.println("JavaFXApplication: stopAllSounds: Error: " + e.getMessage());
        } finally {
            this.audioLock.unlock();
        }
    }

    @Override
    public void pauseAllSounds() {
        System.out.println("JavaFXApplication: pauseAllSounds");

        if (IS_JPRO) {
            logger.log(Level.WARNING, "Sound is not supported for JPro environments");
            return;
        }

        this.audioLock.lock();
        System.out.println("JavaFXApplication: pauseAllSounds: Claimed lock");

        try {
            for (Map.Entry<String, List<MediaPlayer>> entry : this.mediaPlayers.entrySet()) {
                List<MediaPlayer> mediaPlayers = entry.getValue();
                for (MediaPlayer mediaPlayer : mediaPlayers) {
                    if (mediaPlayer.getStatus() == Status.PLAYING) {
                        System.out.println("JavaFXApplication: pauseAllSounds: Pausing " + entry.getKey());
                        mediaPlayer.pause();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("JavaFXApplication: pauseAllSounds: Error: " + e.getMessage());
        } finally {
            this.audioLock.unlock();
        }
    }

    @Override
    public void unpauseAllSounds() {
        System.out.println("JavaFXApplication: unpauseAllSounds");

        if (IS_JPRO) {
            logger.log(Level.WARNING, "Sound is not supported for JPro environments");
            return;
        }

        this.audioLock.lock();
        System.out.println("JavaFXApplication: unpauseAllSounds: Claimed lock");

        try {
            for (Map.Entry<String, List<MediaPlayer>> entry : this.mediaPlayers.entrySet()) {
                List<MediaPlayer> mediaPlayers = entry.getValue();
                for (MediaPlayer mediaPlayer : mediaPlayers) {
                    if (mediaPlayer.getStatus() == Status.PAUSED) {
                        System.out.println("JavaFXApplication: unpauseAllSounds: Unpausing " + entry.getKey());
                        mediaPlayer.play();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("JavaFXApplication: unpauseAllSounds: Error: " + e.getMessage());
        } finally {
            this.audioLock.unlock();
        }
    }

    @Override
    public void sendToFront(String viewName, String name) {
        System.out.println("JavaFXApplication: sendToFront: viewName=" + viewName + ", name=" + name);
        Node control = (Node) this.namedDecoratedNodes.get(viewName).get(name).controllerNode;
        if (control == null) {
            System.out.println("JavaFXApplication: sendToBack: Control not found");
            return;
        }
        control.toFront();
    }

    @Override
    public void sendToBack(String viewName, String name) {
        System.out.println("JavaFXApplication: sendToBack: viewName=" + viewName + ", name=" + name);
        Node control = (Node) this.namedDecoratedNodes.get(viewName).get(name).controllerNode;
        if (control == null) {
            System.out.println("JavaFXApplication: sendToBack: Control not found");
            return;
        }
        control.toBack();
    }
}
