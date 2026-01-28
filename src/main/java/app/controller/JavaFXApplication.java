package app.controller;

import app.Bootstrap;
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
import app.VerticalAlignment;
import static app.VerticalAlignment.BOTTOM;
import static app.VerticalAlignment.CENTER;
import static app.VerticalAlignment.TOP;
import static app.controller.BaseController.logger;
import app.node.BaseNode;
import app.controller.javafx.DelegateApplication;
import app.dialog.BaseDialog;
import app.node.Sprite;
import app.node.effect.BaseEffect;
import app.node.effect.Glow;
import app.node.effect.SlideTransition;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.util.Duration;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.BlendMode;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.TextFlow;
import javax.imageio.metadata.IIOMetadata;
import org.w3c.dom.NamedNodeMap;
import app.view.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Group;
import javafx.scene.control.ContentDisplay;
import javafx.scene.text.FontSmoothingType;

/**
 *
 * @author repp
 */
public class JavaFXApplication extends BaseController {
    
    public static final int DEFAULT_BUTTON_FONT_SIZE = 10;
    public static final String DEFAULT_FONT = "RobotoMono-Medium";
    public static final int DEFAULT_FONT_SIZE = 16;
    public static final boolean IS_JPRO = (System.getProperty("jpro.version") != null);
    public static List<String> TIMER_EVENTS = new ArrayList();
    
    public DelegateApplication delegateApp;
    public final Lock audioLock = new ReentrantLock();
    public Font buttonFont;
    public int buttonFontHeight = 0;
    public int buttonFontWidth = 0;
    public Map<String, JsonObject> emojiMap;
    public Image emojiSheet;
    public String emptyBook;
    public int fontHeight = 0;
    public static List<String> fontFamiliesLoaded = new ArrayList();
    public int fontWidth = 0;
    public Map<Object, EventHandler<KeyEvent>> keyBindings = new HashMap();
    public BaseView lastSelectedView;
    public HashMap<String, List<MediaPlayer>> mediaPlayers = new HashMap();
    public Font monospaceFont;
    public Map<String, Map<String, Node>> namedFXNodes; // view name -> node name -> FX node
    public Map<String, Map<String, BaseNode>> namedNodes; // view name -> node name -> app (generic) node
    public Map<String, Map<String, Layout>> nodeLayouts; // view name -> node name -> layout
    public Map<String, Map<String, String>> parentNodes; // view name -> node name -> parent node name
    public BaseView parentView;
    public Coordinates primaryDimensions;
    public Scene primaryScene;
    public BaseView splashView;
    public HashMap<String, Pane> tabContentMap;
    public TabPane tabFolder;
    public HashMap<String, Integer> tabIndexMap;
    public HashMap<String, Tab> tabItemMap;
    public HashMap<Tab, BaseView> tabItemViewMap;
    public int textColumns = 0;
    public int textRows = 0;
    public HashMap<String, BaseView> views;
    
    /**
     * The implementation of this method is a work-around to inheritance not being fully implemented in java
     * for static methods.  While child classes can inherit a static method from a parent class, there is no
     * way to know within the inherited method for which class it is being executed.  Also, there is no good
     * way to know within any static method what the name of the current class is without using a Throwable.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[1];
            args[0] = new Throwable().getStackTrace()[0].getClassName();
        }
        Bootstrap.main(args);
    }
    
    @Override
    public void open(BaseView splashView, BaseView mainView) {
        System.out.println("JavaFXApplication: open");
        
        this.splashView = splashView;
        this.parentView = mainView;
        
        // The static launching of the JavaFX app will invoke start() on the Application which will invoke setDelegate()
        // in this object to allow a reference of the app to be stored and used for future UI operations
        DelegateApplication.main(new String[1]);
    }

    @Override
    public void setDelegate(Object delegate) {
        System.out.println("JavaFXApplication: setDelegate");
        
        this.delegateApp = (DelegateApplication)delegate;
        
        if (this.splashView != null) {
            this.displayStage(splashView);
        } else {
            showPrimaryStage();
        }
    }
    
    @Override
    public void close() {
        System.out.println("JavaFXApplication: close");
        Platform.exit();    // Gracefully stop all processes in the JavaFX application
        if (IS_JPRO) {
            logger.log(Level.INFO, "System exit intentionally skipped for JPro environment");
        } else {
            System.exit(0);     // Stop any remaining framework processes, including background processes
        }
    }
    
    public void displayStage(BaseView view) {
        System.out.println("JavaFXApplication: displayStage: view=" + view.name);
        
        Stage splashStage = new Stage();
        splashStage.initStyle(StageStyle.TRANSPARENT);
        
        // Load an image for the splash screen
        Image splashImage = new Image(view.backgroundImage);
        ImageView splashImageView = new ImageView(splashImage);
        
        StackPane splashLayout = new StackPane(splashImageView);
        Scene splashScene = new Scene(splashLayout);
        
        splashStage.setScene(splashScene);
        splashStage.show();

        // Set a timer to close the splash screen after 5 seconds
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
            // Close the splash screen and show the main application
            Platform.runLater(() -> {
                splashStage.close();
                showPrimaryStage();
            });
        }).start();
    }
    
    private void showPrimaryStage() {        
        showPrimaryStageFull();
    }
    
    public void showPrimaryStageFull() {
        System.out.println("JavaFXApplication: showPrimaryStage: view=" + this.parentView.name);
        
        // Set the application title
        this.delegateApp.primaryStage.setTitle(this.parentView.name);

        // Size the application dimensions        
        this.primaryDimensions = getDimensions(this.parentView.backgroundImage);
        
        // Set the application icon
        if (this.parentView.iconFileName != null) {
            Image iconImage = loadImage(this.parentView.iconFileName);
            this.delegateApp.primaryStage.getIcons().add(iconImage);
        }
        
        // Initialize the application's tab folder and set it as the application's primary scene
        this.tabFolder = new TabPane();
        VBox.setVgrow(this.tabFolder, Priority.ALWAYS);
        //this.tabFolder.setPrefSize(dimensions.x, dimensions.y);
        this.tabFolder.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            String selectedTabTitle = newTab.getText();
            System.out.println("JavaFXApplication: showPrimaryStage: Selected tab " + selectedTabTitle);
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
        //this.tabFolder.setMinSize(800, 600);

        //this.primaryScene = new Scene(this.tabFolder, dimensions.x, dimensions.y);
        /*
        this.primaryScene = new Scene(scrollPane, dimensions.x, dimensions.y);
        final String CSS = 
            ".html-editor .tool-bar { " +
            "    -fx-max-height: 0; " +
            "    -fx-pref-height: 0; " +
            "    -fx-min-height: 0; " +
            "    -fx-padding: 0; " +
            "    -fx-border-width: 0; " +
            "    -fx-opacity: 0; " +
            "    visibility: hidden; " +
            "}";
        this.primaryScene.getStylesheets().add("data:text/css," + CSS);
        */
        //this.primaryScene = new Scene(this.tabFolder, 800, 600);
        this.primaryScene = new Scene(this.tabFolder);
        this.delegateApp.primaryStage.setScene(primaryScene);

        // Share important state with the other instance methods
        this.tabContentMap = new HashMap<>();
        //this.tabEditorMap = new HashMap<>();
        //this.tabEditorTextMap = new HashMap<>();

        this.tabIndexMap = new HashMap<>();
        this.tabItemMap = new HashMap<>();
        this.tabItemViewMap = new HashMap<>();
        this.views = new HashMap();
        this.namedFXNodes = new HashMap();
        this.namedNodes = new HashMap();
        this.parentNodes = new HashMap();
        this.nodeLayouts = new HashMap();
            
        // Init a font for all text areas and buttons to use
        this.monospaceFont = Font.font("Consolas", FontWeight.NORMAL, adjustFontSizeForDPI(DEFAULT_FONT_SIZE));
        this.buttonFont = Font.font("Consolas", FontWeight.NORMAL, adjustFontSizeForDPI(DEFAULT_BUTTON_FONT_SIZE));
        
        // Calculate the height and width of the fonts
        Text textNode = new Text("W");
        textNode.setFont(this.monospaceFont);
        Bounds bounds = textNode.getLayoutBounds();
        this.fontWidth = (int) bounds.getWidth();
        this.fontHeight = (int) bounds.getHeight();
        Text buttonTextNode = new Text("W");
        buttonTextNode.setFont(this.buttonFont);
        Bounds buttonBounds = textNode.getLayoutBounds();
        this.buttonFontWidth = (int) buttonBounds.getWidth();
        this.buttonFontHeight = (int) buttonBounds.getHeight();

        // Calculate the textual height and width of a possible text area
        this.textColumns = ((int) this.primaryDimensions.x / this.fontWidth) + 1;
        this.textRows = ((int) this.primaryDimensions.y / this.fontHeight) + 1;
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < this.textRows; j++) {
            for (int i = 0; i < this.textColumns; i++) {
                    sb.append(' ');
            }
            sb.append('\n');
        }
        this.emptyBook = sb.toString();
        
        this.addView(this.parentView, true);
        
        this.delegateApp.primaryStage.show();
        
        this.parentView.onDisplay(this);
        
        // TODO - Stop all sounds
    }
    
    public static double adjustFontSizeForDPI(int fontSize) {
        Screen screen = Screen.getPrimary();
        double dpi = screen.getDpi();
        double scaleFactor = dpi / 96.0;    // Standard DPI is typically 96.0, so this calculates the scaling factor
        double newFontSize = fontSize * scaleFactor;
        return newFontSize;
    }
    
    public void addView(BaseView view, Boolean isParent) {
        Integer index = this.tabIndexMap.get(view.name);
        if (index == null) {
            index = this.tabIndexMap.size();
        }
        this.addView(view, isParent, index, false);
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
        System.out.println("JavaFXApplication: clearScreen : viewName=" + viewName); 

        if (this.namedFXNodes.get(viewName) != null) {
            // The pane's conent is named the same as the view
            this.namedFXNodes.get(viewName).keySet().removeIf(key -> !key.equals(viewName));
        }
        
        if (this.namedNodes.get(viewName) != null) {
            // The pane's conent is named the same as the view
            this.namedNodes.get(viewName).keySet().removeIf(key -> !key.equals(viewName));
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
        
        BaseNode node = this.namedNodes.get(viewName).get(nodeName);
        if (node == null) {
            logger.log(Level.WARNING, "Node could NOT be found");
            return;
        }
        
        Node fxNode = this.namedFXNodes.get(viewName).get(nodeName);
        if (fxNode == null) {
            logger.log(Level.WARNING, "FX node could NOT be found");
            return;
        }
        
        // Capture the parent name before the registry gets cleaned up
        String parentName = this.parentNodes.get(viewName).get(node.name);
        
        Layout nodeLayout = this.nodeLayouts.get(viewName).get(node.name);
        this.removeFXNode(viewName, node, fxNode);

        if (!node.effects.isEmpty()) {
            this.removeEffects(viewName, parentName, node, nodeLayout, fxNode);
        }
    }
    
    public void removeFXNode(String viewName, BaseNode node, Node fxNode) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, fxNode={3}", new Object[]{viewName, node, fxNode});
        
        String parentName = this.parentNodes.get(viewName).get(node.name);
        Node fxParentNode = this.namedFXNodes.get(viewName).get(parentName);

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
        } else {
            logger.log(Level.SEVERE, "Parent does not have a supported class: {0}", parentControlClass.getSimpleName());
            return;
        }

        this.namedNodes.get(viewName).remove(node.name);
        this.namedFXNodes.get(viewName).remove(node.name);
        this.parentNodes.get(viewName).remove(node.name);
        this.nodeLayouts.get(viewName).remove(node.name);

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
        
        this.namedFXNodes.remove(viewName);
        
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
    
    @Override
    public void addView(BaseView view) {
        this.addView(view, false);
    }
    
    public void setTabLabel(Tab tab, BaseView view) {
        System.out.println("JavaFXApplication: setTabLabel: tab=" + tab + ", view=" + view);
        tab.setText(view.name);
        if (!view.emojis.isEmpty()) {
            String emojiString = String.join(" ", view.emojis);
            TextFlow emojis = stringToTextFlow(emojiString, null, null, DEFAULT_FONT_SIZE, null, FontSmoothingType.LCD);
            tab.setGraphic(emojis);
        }
    }
    
    @Override
    public void addView(BaseView view, Boolean isParent, int index, Boolean isRefresh) {
        System.out.println("JavaFXApplication: addView: name=" + view.name + ", isParent=" + isParent + ", index=" + index + ", isRefresh=" + isRefresh);

        Pane content = new Pane();
        content.setMinSize(this.primaryDimensions.x, this.primaryDimensions.y);
        content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //content.setPrefSize(this.primaryDimensions.x, this.primaryDimensions.y);
        ScrollPane scrollPane = new ScrollPane(content);
        //scrollPane.setContent(this.tabFolder);
        
        // Configure automatic zooming
        Group zoomGroup = new Group(content);
        StackPane contentHolder = new StackPane(zoomGroup);
        scrollPane.setContent(contentHolder);
        
        double zoomFactor = 1.0; 
        zoomGroup.setScaleX(zoomFactor);
        zoomGroup.setScaleY(zoomFactor);
        
        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        //scrollPane.setPrefViewportWidth(dimensions.x);
        //scrollPane.setPrefViewportHeight(dimensions.y);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);

        if (isParent) {
            // Not supported at this time
            if (!isRefresh) {
                view.onLoad(this);
            }
            return;
        }
        
        // Create a new tab
        Tab tab = new Tab();
        this.setTabLabel(tab, view);
        tab.setClosable(false);
        this.tabFolder.getTabs().add(index, tab);
        tab.setContent(scrollPane);
        this.tabItemMap.put(view.name, tab);
        this.tabItemViewMap.put(tab, view);
        
        // Configure the background
        Coordinates dimensions = new Coordinates(1280, 793);
        Background background = null;
        if (view.backgroundImage != null) {
            System.out.println("JavaFXApplication: addView: name=" + view.name + ", using background image " + view.backgroundImage);
            Image image = loadImage(view.backgroundImage);
            Coordinates imageDimensions = this.getDimensions(view.backgroundImage);
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
                backgroundColor = Color.rgb(view.backgroundColor.red, view.backgroundColor.green, view.backgroundColor.blue);
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
            Color backgroundColor = Color.rgb(view.backgroundColor.red, view.backgroundColor.green, view.backgroundColor.blue);
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

        if (this.namedFXNodes.get(view.name) == null) {
            this.namedFXNodes.put(view.name, new HashMap());
            this.namedFXNodes.get(view.name).put(view.name, content);
            this.parentNodes.put(view.name, new HashMap());
            this.namedNodes.put(view.name, new HashMap());
            this.nodeLayouts.put(view.name, new HashMap());
        }
        
        // Add overlay pane AFTER HTMLEditor as StackPane displays its contents back-to-front
        //Pane overlayPane = new Pane();
        //content.getChildren().add(overlayPane);
        this.tabContentMap.put(view.name, content);
        
        if (!isRefresh) {
            view.onLoad(this);
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
        
        double nodeWidth = fxNode.getBoundsInLocal().getWidth();
        double parentWidth = fxParent.getPrefWidth();
        Double x = calculateNodeX(layout.horizontalAlignment, layout.position.x, parentWidth, nodeWidth);        
        if (x != null) {
            // Prevent placing the child node directly on the right edge of the parent node to prevent issues
            if ((nodeWidth < parentWidth - 1) && ((x + nodeWidth) >= parentWidth - 1)) {
                x -= 2; // Unfortunate fudge factor.  A gap of 2 pixels is needed to prevent triggering a resize of the application window.
                logger.log(Level.WARNING, "Reduced the node's width by 2 to prevent overlap with the parent");
            }
            fxNode.setLayoutX(x);
        }
        
        double nodeHeight = fxNode.getBoundsInLocal().getHeight();
        double parentHeight = fxParent.getPrefHeight();
        Double y = calculateNodeY(layout.verticalAlignment, layout.position.y, parentHeight, nodeHeight);    
        if (y != null) {
            // Prevent placing the child node directly on the bottom edge of the parent node to prevent issues
            if ((nodeHeight < parentHeight - 1) && ((y + nodeHeight) >= parentHeight - 1)) {
                y -= 2; // Unfortunate fudge factor.  A gap of 2 pixels is needed to prevent triggering a resize of the application window.
                logger.log(Level.WARNING, "Reduced the node's height by 2 to prevent overlap with the parent");
            }
            fxNode.setLayoutY(y);
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
        logger.log(Level.INFO, "Entered: alignment={0}, relativeY={1}, parentWidth={2}, nodeWidth={3}", new Object[]{alignment, relativeY, parentHeight, nodeHeight});
        
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
    
    public Node addEffects(String viewName, String parentName, app.node.BaseNode node, Layout layout, Node fxNode, app.Color offsetColor) {
        logger.log(Level.INFO, "Entered: viewName={0}, parentName={1}, node={2}, layout={3}, fxNode={4}, offsetColor={5}", new Object[]{viewName, parentName, node, layout, fxNode, offsetColor});
        
        if ((node.effects == null) || (node.effects.isEmpty())) {
            logger.log(Level.INFO, "No effects, nothing to do");
            return fxNode;
        }
        
        Pane fxWrapperNode = null;
        
        for (BaseEffect effect : node.effects) {
            Class<?> effectClass = effect.getClass();
            logger.log(Level.INFO, "Adding effect: class={0}", effectClass.getSimpleName());
            
            if (effectClass.equals(app.node.effect.Glow.class)) {
                this.addGlow(fxNode, (Glow) effect, offsetColor);
                logger.log(Level.INFO, "Added glow effect");
            } else if (effectClass.equals(app.node.effect.SlideTransition.class)) {
                // TODO - Need to do something like disable the parent pane's scrollbars during the animation so they don't get confused
                //parentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                //parentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                
                SlideTransition transitionEffect = (SlideTransition) effect;
                logger.log(Level.INFO, "Adding transition effect: path={0}, duration={1}, eventListener={2}, stage={3}", new Object[]{transitionEffect.path, transitionEffect.duration, transitionEffect.eventListener, transitionEffect.getStage()});

                if (transitionEffect.getStage() == SlideTransition.Stage.INIT) {
                    transitionEffect.onEvent(NODE_TRANSITIONED_EVENT, SlideTransition.Stage.ENTERING);
                }
                
                if ((transitionEffect.getStage() == SlideTransition.Stage.ENTERING) || (transitionEffect.getStage() == SlideTransition.Stage.EXITING)) {
                    if (fxWrapperNode != null) {
                        logger.log(Level.WARNING, "Node is being wrapped by another effect, skipping effect");
                        continue;
                    }
                    
                    // Clip the transition in a wrapper so that when the application window is maximized, the spill-over node contents aren't seen
                    fxWrapperNode = new Pane(fxNode);
                    Rectangle clip = new Rectangle();
                    clip.widthProperty().bind(fxWrapperNode.widthProperty());
                    clip.heightProperty().bind(fxWrapperNode.heightProperty());
                    fxWrapperNode.setClip(clip);

                    double nodeWidth = fxWrapperNode.prefWidth(-1);
                    double nodeHeight = fxWrapperNode.prefHeight(-1);
                    
                    Pane fxParent = (Pane) this.namedFXNodes.get(viewName).get(parentName); // TODO - This effect should only be supported when the parent is a pane
                    Layout originalLayout;
                    if (layout == null) {
                        originalLayout = null;
                    } else {
                        originalLayout = layout.copy();
                    }
                    
                    logger.log(Level.FINE, "Measured effects wrapper: width={0}, height={1}", new Object[]{nodeWidth, nodeHeight});
                    TranslateTransition slide = new TranslateTransition(Duration.seconds(transitionEffect.duration), fxNode);
                    if (transitionEffect.path == null) {
                        logger.log(Level.WARNING, "Unsupported transition path");
                    } else switch (transitionEffect.path) {
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
                                fxNode.setTranslateX(-nodeWidth);
                                slide.setFromX(-nodeWidth);
                                slide.setToX(wrapperWidth - nodeWidth);
                            } else if (transitionEffect.getStage() == SlideTransition.Stage.EXITING) {
                                fxNode.setTranslateX(wrapperWidth - nodeWidth);
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
                                fxNode.setTranslateX(wrapperWidth);
                                slide.setFromX(wrapperWidth);
                                slide.setToX(0);
                            } else if (transitionEffect.getStage() == SlideTransition.Stage.EXITING) {
                                fxNode.setTranslateX(0);
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
                                fxNode.setTranslateY(-nodeHeight);
                                slide.setFromY(-nodeHeight);
                                slide.setToY(wrapperHeight - nodeHeight);
                            } else if (transitionEffect.getStage() == SlideTransition.Stage.EXITING) {
                                fxNode.setTranslateY(wrapperHeight - nodeHeight);
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
                                fxNode.setTranslateY(wrapperHeight);
                                slide.setFromY(wrapperHeight);
                                slide.setToY(0);
                            } else if (transitionEffect.getStage() == SlideTransition.Stage.EXITING) {
                                fxNode.setTranslateY(0);
                                slide.setFromY(0);
                                slide.setToY(wrapperHeight);
                            }
                        }
                        default -> logger.log(Level.WARNING, "Unsupported transition path");
                    }
                    slide.setInterpolator(Interpolator.EASE_OUT);
                    final Pane finalWrapperNode = fxWrapperNode;
                    slide.setOnFinished(event -> {
                        if (transitionEffect.getStage() == SlideTransition.Stage.ENTERING) {
                            transitionEffect.onEvent(NODE_TRANSITIONED_EVENT, SlideTransition.Stage.READY);
                        } else if (transitionEffect.getStage() == SlideTransition.Stage.EXITING) {
                            transitionEffect.onEvent(NODE_TRANSITIONED_EVENT, SlideTransition.Stage.COMPLETE);
                        }
                        logger.log(Level.INFO, "Transition for node {0} complete, stage={1}", new Object[]{node.name, transitionEffect.getStage()});
                        // When the transition play is complete, re-position the child outside of the wrapper and added it directly to the parent
                        this.removeFXNode(viewName, node, finalWrapperNode); // Remove the wrapper
                        if (transitionEffect.getStage() == SlideTransition.Stage.READY) {
                            this.publishNode(viewName, parentName, node, originalLayout, null); // Re-publish the node to directly add it to its parent (using the original layout)
                        }
                        // Also, publish an event so that the button can be disabled during the transition and re-enabled upon completion
                        transitionEffect.eventListener.onEvent(NODE_TRANSITIONED_EVENT, node.name);
                    });
                    slide.play();
                }
            } else {
                logger.log(Level.SEVERE, "Class is not a supported effect class: {0}", effectClass.getSimpleName());
            }
        }
        
        if (fxWrapperNode != null) {
            logger.log(Level.INFO, "Wrapping node: wrapper={0}", fxWrapperNode.getClass().getSimpleName());
            return fxWrapperNode;
        }
        
        return fxNode;
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
    
    public void addGlow(Node fxNode, Glow effect, app.Color offsetColor) {
        logger.log(Level.INFO, "Entered: fxNode={0}, glowEffect={1} offsetColor={2}", new Object[]{fxNode, effect, offsetColor});
        app.Color glowColor;
        if (effect.color == null) {
            glowColor = offsetColor;
        } else {
            glowColor = effect.color;
        }
        String defaultStyle = "-fx-effect: dropshadow(three-pass-box, rgba(" + glowColor.red + ", " + glowColor.green + ", " + glowColor.blue + ", 0.8), 5, 0.8, 0, 0);";
        String hoverStyle = "-fx-effect: dropshadow(three-pass-box, rgba(" + glowColor.red + ", " + glowColor.green + ", " + glowColor.blue + ", 1), 10, 0.8, 0, 0);";
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
            case Icon.INFORMATION -> AlertType.INFORMATION;
            case Icon.WARNING -> AlertType.WARNING;
            case Icon.ERROR -> AlertType.ERROR;
            default -> AlertType.INFORMATION;
        };
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.initOwner(this.delegateApp.primaryStage);
            if (dialog.title != null) {
                alert.setTitle(dialog.title + " - " + this.parentView.name);
            } else {
                alert.setTitle(this.parentView.name);
            }
            if (dialog.header != null) {
                TextFlow header = this.stringToTextFlow(dialog.header, DEFAULT_FONT, new app.Color(0, 0, 0), DEFAULT_FONT_SIZE, FontStyle.BOLD, FontSmoothingType.LCD);
                alert.getDialogPane().setHeader(header);
            }
            if (dialog.emojis != null) {
                ImageView graphicImage = this.stringToEmoji(dialog.emojis, (int) Math.round(EMOJI_SHEET_SIZE));
                alert.setGraphic(graphicImage);
            }
            if (dialog.text != null) {
                alert.setContentText(dialog.text);
            }
            alert.show();
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
    
    public Pane newGroup(String viewName, app.node.Group node, Pane fxPane, app.Color offsetColor) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, fxPane={2}, offsetColor={3}", new Object[]{viewName, node, fxPane, offsetColor});
        
        if (fxPane != null) {
            fxPane.getChildren().clear();
        }
    
        Class<?> groupClass = node.getClass();
        if (groupClass.equals(app.node.VerticalGroup.class)) {
            VBox vbox;
            if (fxPane == null) {
                vbox = new VBox();
            } else {
                vbox = (VBox) fxPane;
            }
            vbox.setBorder(new Border(new BorderStroke(Color.rgb(offsetColor.red, offsetColor.green, offsetColor.blue), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
            vbox.setAlignment(Pos.CENTER);
            vbox.setFillWidth(false); // Allow children to stay at their preferred widths and be centered 
            fxPane = vbox;
        } else if (groupClass.equals(app.node.HorizontalGroup.class)) {
            HBox hbox;
            if (fxPane == null) {
                hbox = new HBox();
            } else {
                hbox = (HBox) fxPane;
            }
            hbox.setBorder(new Border(new BorderStroke(Color.rgb(offsetColor.red, offsetColor.green, offsetColor.blue), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(node.borderWidth))));
            hbox.setAlignment(Pos.CENTER);
            fxPane = hbox;
        } else {
            logger.log(Level.SEVERE, "Unsupported group: {0}", groupClass);
            return null;
        }
        fxPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        if (node.backgroundColor == null) {
            fxPane.setBackground(Background.EMPTY);
        } else {
            Color fxBackgroundColor = Color.rgb(node.backgroundColor.red, node.backgroundColor.green, node.backgroundColor.blue);
            fxPane.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        this.namedFXNodes.get(viewName).put(node.name, fxPane);
        
        return fxPane;
    }
    
    public Pane newInputField(String viewName, app.node.InputField node, Pane fxBox, app.Color offsetColor) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, fxBox={2}, offsetColor={3}", new Object[]{viewName, node, fxBox, offsetColor});
    
        if (fxBox == null) {
            fxBox = newGroup(viewName, node.group, fxBox, offsetColor);
        } else {
            fxBox.getChildren().clear();
        }

        app.node.Field fieldNode = new app.node.Field(node.group.name + " field");
        fieldNode.backgroundColor = node.childBackgroundColor;
        fieldNode.initialValue = node.initialValue;
        fieldNode.isEnabled = node.isEnabled;
        fieldNode.isUpperCase = node.isUpperCase;
        fieldNode.label = node.label;
        fieldNode.length = node.length;
        fieldNode.pixelSize = node.pixelSize;
        fieldNode.textColor = node.textColor;
        fieldNode.textFont = node.textFont;
        this.addNode(viewName, node.group.name, fieldNode, null);
        
        app.node.Button buttonNode = new app.node.Button(node.group.name + " button");
        buttonNode.backgroundColor = node.childBackgroundColor;
        buttonNode.eventListener = node.eventListener;
        buttonNode.eventName = node.eventName;
        buttonNode.isEnabled = node.isEnabled;
        buttonNode.isMultiUse = node.isMultiUse;
        buttonNode.pixelSize = node.pixelSize;
        buttonNode.text = node.buttonText;
        buttonNode.textColor = node.textColor;
        buttonNode.textFont = node.textFont;
        this.addNode(viewName, node.group.name, buttonNode, null);
        
        return fxBox;
    }
    
    public TextField newField(app.node.Field node, TextField fxTextField, app.Color offsetColor) {
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
        
        app.Color textColor;
        if (node.textColor == null) {
            textColor = offsetColor;
        } else {
            textColor = node.textColor;
        }
        
        int pixelSize;
        if (node.pixelSize == null) {
            pixelSize = (int) Math.round(DEFAULT_PIXEL_SIZE);
        } else {
            pixelSize = (int) Math.round(node.pixelSize);
        }
        
        Text fieldText = stringToText("temp", fontName, textColor, pixelSize, node.textStyle, FontSmoothingType.LCD); // Allow stringToText to parse the font style
        fxTextField.setFont(fieldText.getFont());
        fxTextField.setStyle("-fx-text-fill: rgb(" + textColor.red + ", " + textColor.green + ", " + textColor.blue + ");");                
         
        if (node.backgroundColor != null) {
            Color fxBackgroundColor = Color.rgb(node.backgroundColor.red, node.backgroundColor.green, node.backgroundColor.blue);
            fxTextField.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            fxTextField.setBackground(Background.EMPTY); // Transparent        
        }
        
        return fxTextField;
    }
    
    public Button newButton(app.node.Button node, Button fxButton, app.Color offsetColor) {
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
        
        app.Color textColor;
        if (node.textColor == null) {
            textColor = offsetColor;
        } else {
            textColor = node.textColor;
        }
        
        int pixelSize;
        if (node.pixelSize == null) {
            pixelSize = (int) Math.round(DEFAULT_PIXEL_SIZE);
        } else {
            pixelSize = (int) Math.round(node.pixelSize);
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
        TextFlow textFlow = this.stringToTextFlow(node.text, font, textColor, pixelSize, fontStyle, FontSmoothingType.LCD);
        textFlow.setTextAlignment(TextAlignment.CENTER);
        fxButton.setGraphic(textFlow);
        
        if (node.backgroundColor != null) {
            Color fxBackgroundColor = Color.rgb(node.backgroundColor.red, node.backgroundColor.green, node.backgroundColor.blue);
            fxButton.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        
        if (!isNew) {
            return fxButton;
        }
        
        final Button fxButtonFinal = fxButton;
        if (node.eventListener != null) {
            fxButton.setOnAction(e -> {
                logger.log(Level.INFO, "Button selected: name={0}", node.name);
                if (!node.isMultiUse) {
                    fxButtonFinal.setDisable(true);
                }
                node.eventListener.onEvent(node.eventName, null);
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
    
    public TextFlow newLabel(String viewName, app.node.Label node, TextFlow fxLabel, app.Color offsetColor, FontSmoothingType fst) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, fxLabel={2}, offsetColor={3}, fst={4}", new Object[]{viewName, node, fxLabel, offsetColor, fst});
        
        // Labels (like Strings) are immutable
        if (fxLabel != null) {
            this.removeNode(viewName, node.name);
        }
        
        String font;
        if (node.textFont == null) {
            font = DEFAULT_FONT;
        } else {
            font = node.textFont;
        }
        
        app.Color textColor;
        if (node.textColor == null) {
            textColor = offsetColor;
        } else {
            textColor = node.textColor;
        }
        
        int pixelSize;
        if (node.pixelSize == null) {
            pixelSize = (int) Math.round(DEFAULT_PIXEL_SIZE);
        } else {
            pixelSize = (int) Math.round(node.pixelSize);
        }
        
        app.FontStyle fontStyle;
        if (node.textStyle == null) {
            fontStyle = app.FontStyle.NORMAL;
        } else {
            fontStyle = node.textStyle;
        }
        
        // Use a graphic instead of text to support formatted text
        fxLabel = this.stringToTextFlow(node.text, font, textColor, pixelSize, fontStyle, fst);
        
        if (node.backgroundColor != null) {
            Color fxBackgroundColor = Color.rgb(node.backgroundColor.red, node.backgroundColor.green, node.backgroundColor.blue);
            fxLabel.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            fxLabel.setBackground(Background.EMPTY); // Transparent        
        }
        
        return fxLabel;
    }
    
    public VBox newScrollingLabel(String viewName, app.node.Label node, VBox fxTextBox, app.Color offsetColor) {
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
    
    public Rectangle newRectangle(app.node.Rectangle node, Rectangle fxRectangle, app.Color offsetColor) {
        logger.log(Level.INFO, "Entered: node={0}, fxRectangle={1}, offsetColor={2}", new Object[]{node, fxRectangle, offsetColor});
        
        if (fxRectangle == null) {
            fxRectangle = new Rectangle();
        }
        
        if (node.color == null) {
            fxRectangle.setFill(Color.rgb(offsetColor.red, offsetColor.green, offsetColor.blue, node.opacity));
        } else {
            fxRectangle.setFill(Color.rgb(node.color.red, node.color.green, node.color.blue, node.opacity));
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
    
    public Hyperlink newLink(app.node.Link node, Hyperlink fxHyperlink, app.Color offsetColor) {
        logger.log(Level.INFO, "Entered: node={0}, fxHyperlink={1}, offsetColor={2}", new Object[]{node, fxHyperlink, offsetColor});
        
        if (fxHyperlink == null) {
            fxHyperlink = new Hyperlink();
        }
        
        fxHyperlink.setDisable(!node.isEnabled);
                
        String font;
        if (node.textFont == null) {
            font = DEFAULT_FONT;
        } else {
            font = node.textFont;
        }
        
        app.Color textColor;
        if (node.textColor == null) {
            textColor = offsetColor;
        } else {
            textColor = node.textColor;
        }
        
        int pixelSize;
        if (node.pixelSize == null) {
            pixelSize = (int) Math.round(DEFAULT_PIXEL_SIZE);
        } else {
            pixelSize = (int) Math.round(node.pixelSize);
        }
        
        // Configure the font style based on whether the link is enabled
        FontStyle fontStyle;
        if (node.isEnabled) {
            fontStyle = FontStyle.UNDERLINE_LINK;
        } else {
            fontStyle = FontStyle.BOLD;
        }
        
        // Use a graphic instead of text to support formatted text
        fxHyperlink.setGraphic(this.stringToTextFlow(node.text, font, textColor, pixelSize, fontStyle, FontSmoothingType.LCD));
        
        if (node.backgroundColor != null) {
            Color fxBackgroundColor = Color.rgb(node.backgroundColor.red, node.backgroundColor.green, node.backgroundColor.blue);
            fxHyperlink.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            fxHyperlink.setBackground(Background.EMPTY); // Transparent        
        }
        
        if (node.eventListener != null) {
            fxHyperlink.setOnAction(e -> {
                logger.log(Level.INFO, "Link selected: name={0}", node.name);
                node.eventListener.onEvent(node.eventName, null);
            });
        }
        
        return fxHyperlink;
    }
    
    @Override
    public void changeNode(String viewName, BaseNode node, Layout layout) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, layout={2}", new Object[]{viewName, node, layout});
        String parentName = this.parentNodes.get(viewName).get(node.name);
        if (parentName == null) {
            logger.log(Level.WARNING, "Parent not found for node {0}", node.name);
            return;
        }
        Node fxNode = this.namedFXNodes.get(viewName).get(node.name);
        if (fxNode == null) {
            logger.log(Level.WARNING, "Node {0} not found", node.name);
            return;
        }
        if (layout == null) {
            // As a convenience, re-use the current layout
            layout = this.nodeLayouts.get(viewName).get(node.name);
        }
        this.publishNode(viewName, parentName, node, layout, fxNode);
    }
    
    @Override
    public void addNode(String viewName, String parentName, BaseNode node, Layout layout) {
        logger.log(Level.INFO, "Entered: viewName={0}, parentName={1}, node={2}, layout={3}", new Object[]{viewName, parentName, node, layout});
        this.publishNode(viewName, parentName, node,layout, null);
    }
    
    public void publishNode(String viewName, String parentName, BaseNode node, Layout layout, Node fxNode) {
        logger.log(Level.INFO, "Entered: viewName={0}, parentName={1}, node={2}, layout={3}, fxNode={4}", new Object[]{viewName, parentName, node, layout, fxNode});
        
        // TODO - Probably need a default application-level background color
        // TODO - Maintain a class-level map of child name key to parent object and traverse that to get the real background color
        BaseView view = this.views.get(viewName);
        app.Color offsetColor = view.backgroundColor.getOffset();
        Node fxParent = this.namedFXNodes.get(viewName).get(parentName);
        if (fxParent == null) {
            logger.log(Level.SEVERE, "Parent with provided name not found");
            return;
        }
        Boolean isNew = (fxNode == null);
        
        // TODO - This is ugly.  Parent nodes do not have a base type with public getPrefWidth() and getPrefHeight() methods so each parent class needs to be handled.
        double parentWidth;
        double parentHeight;
        Class<?> parentControlClass = fxParent.getClass();
        if (parentControlClass.equals(Pane.class)) {
            Pane pane = (Pane) fxParent;
            parentWidth = pane.getPrefWidth();
            parentHeight = pane.getPrefHeight();
        } else if (parentControlClass.equals(StackPane.class)) {
            StackPane pane = (StackPane) fxParent;
            parentWidth = pane.getPrefWidth();
            parentHeight = pane.getPrefHeight();
        } else if (parentControlClass.equals(HBox.class)) {
            HBox box = (HBox) fxParent;
            parentWidth = box.getPrefWidth();
            parentHeight = box.getPrefHeight();
        } else if (parentControlClass.equals(VBox.class)) {
            VBox box = (VBox) fxParent;
            parentWidth = box.getPrefWidth();
            parentHeight = box.getPrefHeight();
        } else {
            logger.log(Level.SEVERE, "Parent does not have a supported class: {0}", parentControlClass.getSimpleName());
            return;
        }
        
        Class<?> childClass = node.getClass();
        if (childClass.equals(app.node.Link.class)) {
            fxNode = this.newLink((app.node.Link) node, (Hyperlink) fxNode, offsetColor);
        } else if (childClass.equals(app.node.Button.class)) {
            fxNode = this.newButton((app.node.Button) node, (Button) fxNode, offsetColor);
            Button button = (Button) fxNode;
            if (node.scaleX != null) {
                button.setPrefWidth(parentWidth * node.scaleX);
            }
            if (node.scaleY != null) {
                button.setPrefHeight(parentHeight * node.scaleY);
            }
        } else if (childClass.equals(app.node.Field.class)) {
            fxNode = this.newField((app.node.Field) node, (TextField) fxNode, offsetColor);
        } else if (childClass.equals(app.node.InputField.class)) {
            fxNode = this.newInputField(viewName, (app.node.InputField) node, (Pane) fxNode, offsetColor);
        } else if (childClass.equals(app.node.Label.class)) {
            fxNode = this.newLabel(viewName, (app.node.Label) node, (TextFlow) fxNode, offsetColor, FontSmoothingType.LCD);
            TextFlow flow = (TextFlow) fxNode;
            if (node.scaleX != null) {
                flow.setPrefWidth(parentWidth * node.scaleX);
            }
            if (node.scaleY != null) {
                flow.setPrefHeight(parentHeight * node.scaleY);
            }
        } else if (childClass.equals(app.node.ScrollingLabel.class)) {
            fxNode = this.newScrollingLabel(viewName, (app.node.Label) node, (VBox) fxNode, offsetColor);
            VBox flowBox = (VBox) fxNode;
            ScrollPane sp = (ScrollPane)flowBox.getChildren().get(0); // TODO - This is ugly
            if (node.scaleX != null) {
                flowBox.setPrefWidth(parentWidth * node.scaleX);
                sp.setPrefWidth(parentWidth * node.scaleX);
            }
            if (node.scaleY != null) {
                flowBox.setPrefHeight(parentHeight * node.scaleY);
                sp.setPrefHeight(parentHeight * node.scaleY);
            }
        } else if (childClass.equals(app.node.Image.class)) {
            fxNode = this.newImage((app.node.Image) node, (ImageView) fxNode);
        } else if (childClass.equals(app.node.HorizontalGroup.class)) {
            fxNode = this.newGroup(viewName, (app.node.HorizontalGroup) node, (Pane) fxNode, offsetColor);
            for (BaseNode childNode : ((app.node.Group) node).nodes) {
                this.addNode(viewName, node.name, childNode, null);
            }
        } else if (childClass.equals(app.node.VerticalGroup.class)) {
            fxNode = this.newGroup(viewName, (app.node.VerticalGroup) node, (Pane) fxNode, offsetColor);
            for (BaseNode childNode : ((app.node.Group) node).nodes) {
                this.addNode(viewName, node.name, childNode, null);
            }
        } else if (childClass.equals(app.node.Rectangle.class)) {
            fxNode = this.newRectangle((app.node.Rectangle) node, (Rectangle) fxNode, offsetColor);
            Rectangle rectangle = (Rectangle) fxNode;
            if (node.scaleX != null) {
                rectangle.setWidth(parentWidth * node.scaleX);
            }
            if (node.scaleY != null) {
                rectangle.setHeight(parentHeight * node.scaleY);
            }
        } else {
            logger.log(Level.SEVERE, "Class is not a supported child class: {0}", childClass.getSimpleName());
            return;
        }

        if (isNew) {
            // TODO - This is ugly.
            // To allow the width and height to be accessed immediately, add the child to a temporary Scene.
            // The temporary Scene and Group will be garbage collected at the end of the method as they fall out of scope.
            javafx.scene.Group tempRoot = new javafx.scene.Group(fxNode);
            Scene tempScene = new Scene(tempRoot);
            fxNode.applyCss();
            double width = fxNode.prefWidth(-1);
            double height = fxNode.prefHeight(-1);
            logger.log(Level.INFO, "Temp dimensions for {0} = {1}x{2}p", new Object[]{childClass, width, height});
            ((javafx.scene.Group)fxNode.getScene().getRoot()).getChildren().remove(fxNode);
        }
        
        if ((node.effects != null) && (!node.effects.isEmpty())) {
            // Effects might require a wrapper so handle effects now before adding the node to its parent so that a wrapper can be added if needed
            fxNode = this.addEffects(viewName, parentName, node, layout, fxNode, offsetColor);
        }
        
        // TODO - This is ugly.  Parent nodes do not have a base type (Parent) with a public getChildren() method so each parent class needs to be handled.
        if (parentControlClass.equals(Pane.class)) {
            Pane pane = (Pane) fxParent;
            if (isNew) {
                pane.getChildren().add(fxNode);
            }
            pane.layout();
            positionNode(pane, node, layout, fxNode);
        } else if (parentControlClass.equals(StackPane.class)) {
            StackPane pane = (StackPane) fxParent;
            if (isNew) {
                pane.getChildren().add(fxNode);
            }
        } else if (parentControlClass.equals(HBox.class)) {
            HBox.setHgrow(fxNode, Priority.NEVER); // Preventing HBox from stretching children horizontally just to fill its width
            HBox box = (HBox) fxParent;
            if (isNew) {
                box.getChildren().add(fxNode);
            }
        } else if (parentControlClass.equals(VBox.class)) {
            VBox.setVgrow(fxNode, Priority.NEVER); // Preventing VBox from stretching children vertically just to fill its height
            VBox box = (VBox) fxParent;
            if (isNew) {
                box.getChildren().add(fxNode);
            }
        } else {
            logger.log(Level.SEVERE, "Parent does not have a supported class: {0}", parentControlClass.getSimpleName());
            return;
        }
        
        this.namedNodes.get(viewName).put(node.name, node);
        this.namedFXNodes.get(viewName).put(node.name, fxNode);
        this.parentNodes.get(viewName).put(node.name, parentName);
        this.nodeLayouts.get(viewName).put(node.name, layout);
        
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
        logger.log(Level.INFO, "Added node {0} {1} at ({2},{3}), width={4}, height={5} using parent pixel width={6}, parent pixel height={7}, pixel width={8}, pixel height={9}", new Object[]{childClass.getSimpleName(), node.name, relativeX, relativeY, relativeWidth, relativeHeight, parentWidth, parentHeight, width, height});
    }
    
    // TODO - Make this newGrid() and add to addNode()
    @Override
    public void displayGrid(String viewName, app.node.Grid grid, Layout layout) {
        logger.log(Level.INFO, "Entered: viewName={0}, grid={1}, layout={2}", new Object[]{viewName, grid, layout});
        
        // TODO - Implement layout.  Currently, the grid expands to fill the view.
        
        BaseView view = this.views.get(viewName);
        app.Color genericOffsetColor = view.backgroundColor.getOffset();
        Color offsetColor = Color.rgb(genericOffsetColor.red, genericOffsetColor.green, genericOffsetColor.blue);
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
            Color backgroundColor = Color.rgb(view.backgroundColor.red, view.backgroundColor.green, view.backgroundColor.blue);
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
                BackgroundFill cellFill = new BackgroundFill(Color.rgb(cellGroup.backgroundColor.red, cellGroup.backgroundColor.green, cellGroup.backgroundColor.blue), CornerRadii.EMPTY, Insets.EMPTY);
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
            
            this.namedFXNodes.get(viewName).put(cellGroup.name + " cell", cell);
            this.addNode(viewName, cellGroup.name + " cell", cellGroup, null);
            //Pane box = newGroup(viewName, cellGroup, genericOffsetColor);
            //cell.getChildren().add(box);
            
            gridContent.add(cell, currentColumn - 1, currentRow - 1);
        }
        
        // TODO - Apply grid's layout
        tabContent.getChildren().add(gridContent);
    }
    
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
        this.addView(view, false, index, true);
    }
    
    public Text stringToText(String string, String fontName, app.Color fontColor, Integer fontSize, app.FontStyle fontStyle, FontSmoothingType fst) {
        logger.log(Level.FINE, "Entered", new Object[]{string, fontName, fontColor, fontSize, fontStyle, fst});
        
        Text text = new Text(string);
        
        // Configure the font
        if (fontStyle == null) {
            fontStyle = FontStyle.NORMAL;
        }
        
        FontPosture fxStyle = null;
        FontWeight fxWeight = FontWeight.NORMAL;
        switch (fontStyle) {
            case FontStyle.NORMAL -> fxWeight = FontWeight.NORMAL;
            case FontStyle.BOLD -> fxWeight = FontWeight.BOLD;
            case FontStyle.ITALIC -> fxStyle = FontPosture.ITALIC;
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
                // TODO - Not supported, needs styling
                fxWeight = FontWeight.BOLD;
                text.setUnderline(true);
                app.Color offsetColor = fontColor.getOffset();
                if (offsetColor.equals(app.Color.BLACK)) {
                    fontColor = app.Color.HYPERLINK_LIGHT_BLUE;
                } else {
                    fontColor = app.Color.HYPERLINK_BLUE;
                }
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
        if (fontName == null) {
            fontName = Font.getDefault().getName();
            logger.log(Level.FINE, "Using default font", fontName);
        } else {
            // Load all font files for the font family if not already loaded.
            // The font size is required for loading a font but subsequent calls can use any size once loaded.
            if (!fontFamiliesLoaded.contains(fontName)) {
                List<String> fontFiles = app.Font.getFontFiles(fontName);
                for (String fontFileName : fontFiles) {
                    logger.log(Level.FINE, "Loading font file", fontFileName);
                    Font.loadFont(getClass().getResourceAsStream(fontFileName), fontSize);
                }
                if (!fontFiles.isEmpty()) {
                    fontFamiliesLoaded.add(fontName);
                } else {
                    logger.log(Level.WARNING, "Unsupported font family, using default", new Object[]{fontName, Font.getDefault().getName()});
                    fontName = Font.getDefault().getName();
                }
            }
        }
        
        // Adjust for DPI
        double newFontSize = adjustFontSizeForDPI(fontSize);
        Font font;
        if (fxStyle != null) {
            font = Font.font(fontName, fxStyle, newFontSize);
        } else {
            font = Font.font(fontName, fxWeight, newFontSize);
        }
        text.setFont(font);
        if (fontColor != null) {
            text.setFill(Color.rgb(fontColor.red, fontColor.green, fontColor.blue));
        }
        
        text.setFontSmoothingType(fst);
        
        return text;
    }
    
    @Override
    public void loadEmojiData() {
        System.out.println("JavaFXApplication: loadEmojiData");
        
        if (this.emojiSheet == null) {
            System.out.println("JavaFXApplication: loadEmojiData: Loading emoji sheet");
            this.emojiSheet = this.loadImage(EMOJI_SHEET);
        }

        if (this.emojiMap == null) {
            System.out.println("JavaFXApplication: loadEmojiData: Loading emoji map");
            
            this.emojiMap = new HashMap();

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
                        this.emojiMap.put(unified, emojiObj);
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
            .anyMatch(cp -> Character.isEmojiPresentation(cp) || 
                            Character.isEmojiModifier(cp) ||
                            cp == 0xFE0F); // VS-16: Forces emoji presentation
        return isVisualEmoji;
    }
    
    public static String getEmojiUnifiedValue(String emoji) {
        String unifiedValue = emoji.codePoints()
            .mapToObj(cp -> String.format("%04X", cp))
            .collect(Collectors.joining("-"));
        return unifiedValue;
    }
    
    public ImageView stringToEmoji(String string, Integer fontSize) {        
        if (!isEmoji(string)) {
            return null;
        }

        // Use lazy loading for the sprite sheet in case it's never needed
        if (this.emojiSheet == null) {
            this.loadEmojiData();
        }

        String emojiUnified = getEmojiUnifiedValue(string);
        if (!this.emojiMap.containsKey(emojiUnified)) {
            System.out.println("JavaFXApplication: stringToEmoji: Emoji does not exist in the emoji json! string=" + string + ", unified=" + emojiUnified);
            return null;
        }

        JsonObject emojiObj = this.emojiMap.get(emojiUnified);
        ImageView emojiView = new ImageView(this.emojiSheet);
        double sheetX = Double.parseDouble(emojiObj.get("sheet_x").getAsString());
        double sheetY = Double.parseDouble(emojiObj.get("sheet_y").getAsString());
        // Every emoji image in the sheet has a 1 pixel transparent border around it, so the 64px sheet is made up of 66px squares
        double x = (sheetX * (EMOJI_SHEET_SIZE + 2)) + 1;
        double y = (sheetY * (EMOJI_SHEET_SIZE + 2)) + 1;
        emojiView.setViewport(new Rectangle2D(x, y, EMOJI_SHEET_SIZE, EMOJI_SHEET_SIZE));
        emojiView.setSmooth(true); // Enables better scaling algorithm
        emojiView.setCache(true);  // Can help with performance in a long TextFlow
        emojiView.setCacheHint(CacheHint.QUALITY); 
        emojiView.setFitHeight(fontSize);
        emojiView.setPreserveRatio(true);

        return emojiView;
    }
    
    public TextFlow stringToTextFlow(String string, String fontName, app.Color fontColor, Integer fontSize, app.FontStyle fontStyle, FontSmoothingType fst) {
        if ((string == null) || (string.isEmpty())) {
            return null;
        }
        
        TextFlow textFlow = new TextFlow();
        
        // \X matches a "Unicode extended grapheme cluster" (the full emoji)
        Matcher matcher = Pattern.compile("\\X").matcher(string);
        while (matcher.find()) {
            String cluster = matcher.group();
            // Check if the cluster contains a character intended to be an emoji
            boolean isEmoji = isEmoji(cluster);
            if (isEmoji) {
                // Emoji - Add an ImageView
                System.out.println("JavaFXApplication: stringToTextFlow: Handling emoji: " + cluster);
                ImageView emojiView = this.stringToEmoji(cluster, fontSize);
                textFlow.getChildren().add(emojiView);
            } else {
                // Normal text - Add a Text node
                Text textNode = stringToText(cluster, fontName, fontColor, fontSize, fontStyle, fst);
                textFlow.getChildren().add(textNode);
            }
        }

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
    public int displayGifOrig(String viewName, String fileName, int row, int column) {
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
        
        Coordinates dimensions = getDimensions(fileName);
        int nextRow = row + ((int) dimensions.y / this.fontHeight) + 1;
        
        return nextRow;
    }
    
    @Override
    public int displayGif(String viewName, String fileName, int row, int column) {
        System.out.println("JavaFXApplication: displayGif: viewName=" + viewName + ", fileName=" + fileName + ", row=" + row + ", column=" + column);
        
        if (!IS_JPRO) {
            System.out.println("JavaFXApplication: displayGif: Detected desktop app, reverting to regular gif support");
            return this.displayGifOrig(viewName, fileName, row, column);
        }
        
        Pane content = this.tabContentMap.get(viewName);
        
        ImageView imageView = new ImageView();
        Coordinates coordinates = this.convertToCoordinates(row, column);
        this.positionNodeOld(content, imageView, coordinates);
        //imageView.setLayoutX(coordinates.x);
        //imageView.setLayoutY(coordinates.y);
        
        List<Image> frames = new ArrayList<>();
        List<Duration> frameDelays = new ArrayList<>();
        Timeline timeline;
        
        javax.imageio.ImageReader reader = javax.imageio.ImageIO.getImageReadersByFormatName("gif").next();
        try (javax.imageio.stream.ImageInputStream ciis = javax.imageio.ImageIO.createImageInputStream(getClass().getResourceAsStream(fileName))) {
            reader.setInput(ciis, false);
            int numberOfImages = reader.getNumImages(true);

            for (int i = 0; i < numberOfImages; i++) {
                java.awt.image.BufferedImage image = reader.read(i);
                Image fxImage = SwingFXUtils.toFXImage(image, null); // Convert to JavaFX Image
                frames.add(fxImage);

                // Extract frame delay
                IIOMetadata metadata = reader.getImageMetadata(i);
                int delayMs = getFrameDelay(metadata);
                frameDelays.add(Duration.millis(delayMs));
            }
        } catch (Exception e) {
            System.err.println("JavaFXApplication: displayGif: Error! " + e.toString());
            // TODO - Handle error (e.g., load a fallback static image)
        }
        
        if (frames.isEmpty()) {
            return 0;
        }

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE); // Loop indefinitely

        Duration currentTime = Duration.ZERO;
        for (int i = 0; i < frames.size(); i++) {
            final int frameIndex = i;
            // Add a KeyFrame at the specific time instant to switch the image
            KeyFrame keyFrame = new KeyFrame(currentTime, event -> {
                imageView.setImage(frames.get(frameIndex));
            });
            timeline.getKeyFrames().add(keyFrame);
            // Advance the time by the frame's duration
            currentTime = currentTime.add(frameDelays.get(i));
        }
        
        // Add and play the image
        content.getChildren().add(imageView);
        timeline.play();
        
        Coordinates dimensions = getDimensions(fileName);
        int nextRow = row + ((int) dimensions.y / this.fontHeight) + 1;
        
        return nextRow;
    }
    
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
    
    public Coordinates convertToCoordinates(int row, int column) {
        System.out.println("JavaFXApplication: convertToCoordinates: row=" + row + ", column=" + column);
        
        int x = (int) ((column) * this.fontWidth) - this.fontWidth;
        int y = (int) ((row) * this.fontHeight) - this.fontHeight;
        Coordinates coordinates = new Coordinates(x, y);
        
        return coordinates;
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
    
    @Override
    public Coordinates getDimensions(String imageFileName) {
        System.out.println("JavaFXApplication: getDimensions: imageFileName=" + imageFileName);
        
        Image image = loadImage(imageFileName);
        
        if (image == null) {
            return null;
        }
        
        Coordinates dimensions = new Coordinates((int) image.getWidth(), (int) image.getHeight());
        System.out.println("JavaFXApplication: getDimensions: image.getWidth()=" + image.getWidth() + ", image.getHeight()=" + image.getHeight());
        
        return dimensions;
    }
    
    public Image loadImage(String fileName) {
        System.out.println("JavaFXApplication: loadImage: fileName=" + fileName);
        
        Image image = null;
        try (InputStream inputStream = getClass().getResourceAsStream(fileName)) {
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
    public int getTextColumns() {
        return this.textColumns;
    }
    
    @Override
    public int getTextRows() {
        return this.textRows;
    }
    
    @Override
    public int getColumns(String fileName) {
        System.out.println("JavaFXApplication: getColumns: fileName=" + fileName);
        Coordinates dimensions = getDimensions(fileName);
        int columns = ((int) dimensions.x / this.fontWidth) + 1;
        return columns;
    }
    
    @Override
    public int getRows(String fileName) {
        System.out.println("JavaFXApplication: getRows: fileName=" + fileName);
        Coordinates dimensions = getDimensions(fileName);
        int rows = ((int) dimensions.y / this.fontHeight) + 1;
        return rows;
    }
    
    public int getColumns(int x) {
        int columns = ((int) x / this.fontWidth) + 1;
        return columns;
    }
    
    public int getRows(int y) {
        int rows = ((int) y / this.fontHeight) + 1;
        return rows;
    }
    
    @Override
    public int getButtonColumns(String buttonText) {
        int width = (buttonText.length() * this.fontWidth) + (2 * this.fontWidth);    // Calculate width of text plus buffer of two imaginary characters
        int columns = this.getColumns(width);
        return columns;
    }
    
    @Override
    public int getButtonRows() {
        int height = 2 * this.fontHeight;   // Calculate double height of text
        int rows = getRows(height);
        return rows;
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
        Coordinates topLeft = this.convertToCoordinates(row, column);
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
        glow.setColor(Color.rgb(sprite.glowColor.red, sprite.glowColor.green, sprite.glowColor.blue));
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
        Node control = (Node) this.namedFXNodes.get(viewName).get(name);
        if (control == null) {
            System.out.println("JavaFXApplication: sendToBack: Control not found");
            return;
        }
        control.toFront();
    }
    
    @Override
    public void sendToBack(String viewName, String name) {
        System.out.println("JavaFXApplication: sendToBack: viewName=" + viewName + ", name=" + name);
        Node control = (Node) this.namedFXNodes.get(viewName).get(name);
        if (control == null) {
            System.out.println("JavaFXApplication: sendToBack: Control not found");
            return;
        }
        control.toBack();
    }
}
