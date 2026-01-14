package app.controller;

import app.view.BaseView;
import app.Coordinates;
import app.EventListener;
import app.FontStyle;
import static app.HorizontalAlignment.CENTER;
import static app.HorizontalAlignment.LEFT;
import static app.HorizontalAlignment.RIGHT;
import app.Icon;
import app.Layout;
import static app.VerticalAlignment.BOTTOM;
import static app.VerticalAlignment.CENTER;
import static app.VerticalAlignment.TOP;
import static app.controller.BaseController.logger;
import app.node.BaseNode;
import app.controller.javafx.DelegateApplication;
import app.dialog.BaseDialog;
import app.node.Group;
import app.node.Sprite;
import app.node.effect.BaseEffect;
import app.node.effect.Glow;
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
import java.util.concurrent.CountDownLatch;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Control;
import javafx.scene.layout.Region;

/**
 *
 * @author repp
 */
public class JavaFXApplication extends BaseController {
    
    private static final int DEFAULT_BUTTON_FONT_SIZE = 10;
    private static final String DEFAULT_FONT = "RobotoMono-Medium";
    private static final int DEFAULT_FONT_SIZE = 12;
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
    public int fontWidth = 0;
    public Map<Object, EventHandler<KeyEvent>> keyBindings = new HashMap();
    public BaseView lastSelectedView;
    public HashMap<String, List<MediaPlayer>> mediaPlayers = new HashMap();
    public Font monospaceFont;
    public Map<String, Map<String, Object>> namedControls;
    public Map<String, List<BooleanBinding>> nodeBindings = new HashMap(); // Only necessary because they need to handle UI thread actions that can outlast scoping inside of a method
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
        BaseController.main(args);
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
        boolean isJPro = System.getProperty("jpro.version") != null;
        if (isJPro) {
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
        this.namedControls = new HashMap();
            
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
        
        this.parentView.onDisplay(this);
        
        this.delegateApp.primaryStage.show();
        
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
        this.namedControls.get(viewName).put(name, overlay);
    }
    
    @Override
    public void clearScreen(String viewName) {
        System.out.println("JavaFXApplication: clearScreen : viewName=" + viewName); 

        if (this.namedControls.get(viewName) != null) {
            // The pane's conent is named the same as the view
            this.namedControls.get(viewName).keySet().removeIf(key -> !key.equals(viewName));
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
        this.nodeBindings.get(viewName).clear();
    }
    
    @Override
    public void clearControl(String viewName, String controlName) {
        System.out.println("JavaFXApplication: clearControl : viewName=" + viewName + ", controlName=" + controlName);
        Object control = this.namedControls.get(viewName).get(controlName);
        if (control != null) {
            Pane content = this.tabContentMap.get(viewName);
            content.getChildren().remove((Node) control);
            this.namedControls.get(viewName).remove(controlName);
            
            // If the control is a button with a key binding remove the event filter from the scene
            if (this.keyBindings.containsKey(control)) {
                this.primaryScene.removeEventFilter(KeyEvent.KEY_PRESSED, this.keyBindings.get(control));
                this.keyBindings.remove(control);
            }
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
        
        this.namedControls.remove(viewName);
        
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
            TextFlow emojis = stringToTextFlow(emojiString, null, null, DEFAULT_FONT_SIZE, null);
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
        this.nodeBindings.put(view.name, new ArrayList());
        
        // Configure the background
        Background background = null;
        if (view.backgroundImage != null) {
            System.out.println("JavaFXApplication: addView: name=" + view.name + ", using background image " + view.backgroundImage);
            Image image = loadImage(view.backgroundImage);
            Coordinates dimensions = this.getDimensions(view.backgroundImage);
            content.setMinSize(dimensions.x, dimensions.y);
            content.setPrefSize(dimensions.x, dimensions.y);
            BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT, // Repeat in X direction
                BackgroundRepeat.NO_REPEAT, // Repeat in Y direction
                BackgroundPosition.DEFAULT,   // Position of the image
                new BackgroundSize(1.0, 1.0, true, true, false, false)
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

        if (this.namedControls.get(view.name) == null) {
            this.namedControls.put(view.name, new HashMap());
            this.namedControls.get(view.name).put(view.name, content);
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
    
    public void positionNode(String viewName, BaseNode node, Node fxNode, Pane fxParent) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, fxNode={2}, fxParent={3}", new Object[]{viewName, node, fxNode, fxParent});
        
        if (node.layout == null) {
            logger.log(Level.INFO, "No layout, node will be managed by parent");
            return;
        }
        
        if (node.getClass().equals(app.node.Button.class)) {
            Button button = (Button) fxNode;
            
            if ((button.getWidth() == 0) || (button.getHeight() == 0)) {
                logger.log(Level.INFO, "Subscribing to size change : width={0}, height={1}", new Object[]{button.getWidth(), button.getHeight()});
                
                // Run in a background thread to prevent UI locking
                new Thread(() -> {
                    try {
                        // Wait until the UI thread updates both the width and the height for the control
                        CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(() -> {                
                            BooleanBinding sizeEstablished = Bindings.and(
                                button.widthProperty().greaterThan(0),
                                button.heightProperty().greaterThan(0)
                            );
                            this.nodeBindings.get(viewName).add(sizeEstablished);

                            sizeEstablished.when(sizeEstablished).subscribe(isSet -> {
                                if (isSet) {
                                    logger.log(Level.INFO, "Both set! width={0}, height={1}", new Object[]{button.getWidth(), button.getHeight()});
                                    this.positionNode(viewName, node, fxNode, fxParent);
                                }
                            });
                        });
                        latch.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
                return;
            }
        }
        
        Double x = null;
        if (node.layout.horizontalAlignment == null) {
            logger.log(Level.WARNING, "Horizontal alignment was not specified");
        } else {
            switch (node.layout.horizontalAlignment) {
                case LEFT -> {
                    x = fxParent.getPrefWidth() * node.layout.position.x;
                }
                case CENTER -> {
                    x = (fxParent.getPrefWidth() - fxNode.getBoundsInLocal().getWidth()) / 2;
                }
                case RIGHT -> {
                    x = (fxParent.getPrefWidth() * node.layout.position.x) - fxNode.getBoundsInLocal().getWidth();
                }
            }
        }
        
        if (x != null) {
            fxNode.setLayoutX(x);
        }
        
        Double y = null;
        if (node.layout.verticalAlignment == null) {
            logger.log(Level.WARNING, "Vertical alignment was not specified");
        } else {
            switch (node.layout.verticalAlignment) {
                case TOP -> {
                    y = fxParent.getPrefHeight() * node.layout.position.y;
                }
                case CENTER -> {
                    y = (fxParent.getPrefHeight() - fxNode.getBoundsInLocal().getHeight()) / 2;
                }
                case BOTTOM -> {
                    y = (fxParent.getPrefHeight() * node.layout.position.y) - fxNode.getBoundsInLocal().getHeight();
                }
            }
        }
        
        if (y != null) {
            fxNode.setLayoutY(y);
        }
            
        // TODO - node.scaleX and node.scaleY
        
        logger.log(Level.INFO, "Calculated coordinates ({0}, {1}) for parent width {2} and height {3} and node width {4} and height {5}", new Object[]{x, y, fxParent.getPrefWidth(), fxParent.getPrefHeight(), fxNode.getBoundsInLocal().getWidth(), fxNode.getBoundsInLocal().getHeight()});
        

        
        /*
        if (fxNode.isManaged()) {
            logger.log(Level.WARNING, "A layout was specified but the node is managed");
            return;
        }
        */

        /*
        // Handle the configured horizontal alignment
        if (node.layout.horizontalAlignment == null) {
            logger.log(Level.WARNING, "Horizontal alignment was not specified");
        } else {
            switch (node.layout.horizontalAlignment) {
                case LEFT -> fxNode.layoutXProperty().bind(fxParent.widthProperty().multiply(node.layout.position.x));
                case CENTER -> { fxNode.layoutXProperty().bind(Bindings.createDoubleBinding(() -> {
                        double parentWidth = fxParent.getWidth();
                        double nodeWidth = fxNode.getLayoutBounds().getWidth();
                        logger.log(Level.INFO, "H align center: parent width={0}, node width={1}, h position={2}, name={3}", new Object[]{fxParent.getWidth(), fxNode.getLayoutBounds().getWidth(), ((fxParent.getWidth() - fxNode.getLayoutBounds().getWidth()) / 2), node.name});
                        return (parentWidth - nodeWidth) / 2;
                    }, fxParent.widthProperty(), fxNode.layoutBoundsProperty()));
                    logger.log(Level.INFO, "H align center: parent width={0}, node width={1}, h position={2}, name={3}", new Object[]{fxParent.getWidth(), fxNode.getLayoutBounds().getWidth(), ((fxParent.getWidth() - fxNode.getLayoutBounds().getWidth()) / 2), node.name});
                }
                case RIGHT -> fxNode.layoutXProperty().bind(Bindings.createDoubleBinding(() -> {
                        double parentWidth = fxParent.getWidth();
                        double nodeWidth = fxNode.getLayoutBounds().getWidth();
                        return (parentWidth * node.layout.position.x) - nodeWidth;
                    }, fxParent.widthProperty(), fxNode.layoutBoundsProperty()));
                }
        }
        
        // Handle the configured horizontal alignment
        if (node.layout.verticalAlignment == null) {
            logger.log(Level.WARNING, "Vertical alignment was not specified");
        } else {
            switch (node.layout.verticalAlignment) {
                case TOP -> fxNode.layoutYProperty().bind(fxParent.heightProperty().multiply(node.layout.position.y));
                case CENTER -> { fxNode.layoutYProperty().bind(Bindings.createDoubleBinding(() -> {
                        double parentHeight = fxParent.getHeight();
                        double nodeHeight = fxNode.getLayoutBounds().getHeight();
                        logger.log(Level.INFO, "V align center update: parent height={0}, node height={1}, v position={2}, name={3}", new Object[]{fxParent.getHeight(), fxNode.getLayoutBounds().getHeight(), ((fxParent.getHeight() - fxNode.getLayoutBounds().getHeight()) / 2), node.name});
                        return (parentHeight - nodeHeight) / 2;
                    }, fxParent.heightProperty(), fxNode.layoutBoundsProperty()));
                    logger.log(Level.INFO, "V align center: parent height={0}, node height={1}, v position={2}, name={3}", new Object[]{fxParent.getHeight(), fxNode.getLayoutBounds().getHeight(), ((fxParent.getHeight() - fxNode.getLayoutBounds().getHeight()) / 2), node.name});
                }
                case BOTTOM -> fxNode.layoutYProperty().bind(Bindings.createDoubleBinding(() -> {
                        double parentHeight = fxParent.getHeight();
                        double nodeHeight = fxNode.getLayoutBounds().getHeight();
                        return (parentHeight * node.layout.position.y) - nodeHeight;
                    }, fxParent.heightProperty(), fxNode.layoutBoundsProperty()));
                }
        }
        
        if (node.scaleY != null) {
            Pane box = (Pane) fxNode;
            box.prefHeightProperty().bind(fxParent.heightProperty().multiply(node.scaleY));
        }
        */
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
        
        this.namedControls.get(viewName).put(name, button);
        
        return button;
    }
    */
    
    public void addEffects(app.node.BaseNode node, Node fxNode, app.Color offsetColor) {
        logger.log(Level.INFO, "Entered: node={0}, fxNode={1} offsetColor={2}", new Object[]{node, fxNode, offsetColor});
        if ((node.effects != null) && (!node.effects.isEmpty())) {
            for (BaseEffect effect : node.effects) {
                Class<?> effectClass = effect.getClass();
                if (effectClass.equals(app.node.effect.Glow.class)) {
                    this.addGlow(fxNode, (Glow)effect, offsetColor);
                } else {
                    logger.log(Level.SEVERE, "Class is not a supported effect class: {0}", effectClass.getSimpleName());
                }
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
                TextFlow header = this.stringToTextFlow(dialog.header, DEFAULT_FONT, new app.Color(0, 0, 0), DEFAULT_FONT_SIZE, FontStyle.BOLD);
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
    
    public Pane newGroup(String viewName, app.node.Group node, app.Color offsetColor) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, offsetColor={2}", new Object[]{viewName, node, offsetColor});
    
        Class<?> groupClass = node.getClass();
        Pane box;
        if (groupClass.equals(app.node.VerticalGroup.class)) {
            VBox vbox = new VBox(node.borderWidth);
            vbox.setAlignment(Pos.CENTER);
            box = vbox;
        } else if (groupClass.equals(app.node.HorizontalGroup.class)) {
            HBox hbox = new HBox(node.borderWidth);
            hbox.setAlignment(Pos.CENTER);
            box = hbox;
        } else {
            logger.log(Level.SEVERE, "Unsupported group: {0}", groupClass);
            return null;
        }
        box.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        if (node.backgroundColor == null) {
            box.setBackground(Background.EMPTY);
        } else {
            Color fxBackgroundColor = Color.rgb(node.backgroundColor.red, node.backgroundColor.green, node.backgroundColor.blue);
            box.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        this.namedControls.get(viewName).put(node.name, box);
        
        return box;
    }
    
    public Pane newInputField(String viewName, app.node.InputField node, app.Color offsetColor) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, offsetColor={2}", new Object[]{viewName, node, offsetColor});
    
        Pane box = newGroup(viewName, node.group, offsetColor);

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
        this.addNode(viewName, fieldNode, node.group.name);
        
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
        this.addNode(viewName, buttonNode, node.group.name);
        
        return box;
    }
    
    public TextField newField(app.node.Field node, app.Color offsetColor) {
        logger.log(Level.INFO, "Entered: node={0}, offsetColor={1}", new Object[]{node, offsetColor});
        
        TextField fxTextField = new TextField();
        
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
        
        Text fieldText = stringToText("temp", fontName, textColor, pixelSize, node.textStyle); // Allow stringToText to parse the font style
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
    
    public Button newButton(app.node.Button node, app.Color offsetColor) {
        logger.log(Level.INFO, "Entered: node={0}, offsetColor={1}", new Object[]{node, offsetColor});
        
        Button fxButton = new Button();
        
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
            fontStyle = FontStyle.BOLD;
        } else {
            fontStyle = FontStyle.NORMAL;
        }
        
        // Use a graphic instead of text to support formatted text
        fxButton.setAlignment(Pos.CENTER);
        TextFlow textFlow = this.stringToTextFlow(node.text, font, textColor, pixelSize, fontStyle);
        textFlow.setTextAlignment(TextAlignment.CENTER);
        fxButton.setGraphic(textFlow);
        
        if (node.backgroundColor != null) {
            Color fxBackgroundColor = Color.rgb(node.backgroundColor.red, node.backgroundColor.green, node.backgroundColor.blue);
            fxButton.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            //fxButton.setBackground(Background.EMPTY); // Transparent        
        }
        
        if (node.eventListener != null) {
            fxButton.setOnAction(e -> {
                logger.log(Level.INFO, "Button selected: name={0}", node.name);
                if (!node.isMultiUse) {
                    fxButton.setDisable(true);
                }
                node.eventListener.onEvent(node.eventName, null);
            });
        }
        
        fxButton.prefHeightProperty().bind(
            Bindings.createDoubleBinding(
                () -> textFlow.prefHeight(fxButton.getWidth()) + 10, // +10 for button padding
                fxButton.widthProperty(), 
                textFlow.widthProperty()
            )
        );
        fxButton.setMaxHeight(Control.USE_PREF_SIZE);
        
        return fxButton;
    }
    
    public TextFlow newLabel(app.node.Label node, app.Color offsetColor) {
        logger.log(Level.INFO, "Entered: node={0}, offsetColor={1}", new Object[]{node, offsetColor});
        
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
        TextFlow label = this.stringToTextFlow(node.text, font, textColor, pixelSize, fontStyle);
        
        if (node.backgroundColor != null) {
            Color fxBackgroundColor = Color.rgb(node.backgroundColor.red, node.backgroundColor.green, node.backgroundColor.blue);
            label.setBackground(new Background(new BackgroundFill(fxBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            label.setBackground(Background.EMPTY); // Transparent        
        }
        
        return label;
    }
    
    public ImageView newImage(app.node.Image node) {
        logger.log(Level.INFO, "Entered: node={0}", node);
        final Image image = loadImage(node.file);
        ImageView imageView = new ImageView(image);
        imageView.setSmooth(true);
        return imageView;
    }
    
    public Hyperlink newLink(app.node.Link node, app.Color offsetColor) {
        logger.log(Level.INFO, "Entered: node={0}, offsetColor={1}", new Object[]{node, offsetColor});
        
        Hyperlink fxHyperlink = new Hyperlink();
        
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
        fxHyperlink.setGraphic(this.stringToTextFlow(node.text, font, textColor, pixelSize, fontStyle));
        
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
    public void addNode(String viewName, BaseNode node, String parentName) {
        logger.log(Level.INFO, "Entered: viewName={0}, node={1}, parentName={2}", new Object[]{viewName, node, parentName});
        
        // TODO - Probably need a default application-level background color
        // TODO - Maintain a class-level map of child name key to parent object and traverse that to get the real background color
        BaseView view = this.views.get(viewName);
        app.Color offsetColor = view.backgroundColor.getOffset();
        Object actualParent = (Object) this.namedControls.get(viewName).get(parentName);
        if (actualParent == null) {
            logger.log(Level.SEVERE, "Parent with provided name not found");
            return;
        }
        Pane fxParent = (Pane)actualParent;
        Class<?> childClass = node.getClass();
                
        // TODO - Move each class to its own separate handler method
        // TODO - Implement layout and size control
        Node fxChild;
        if (childClass.equals(app.node.Link.class)) {
            fxChild = this.newLink((app.node.Link) node, offsetColor);
        } else if (childClass.equals(app.node.Button.class)) {
            fxChild = this.newButton((app.node.Button) node, offsetColor);
        } else if (childClass.equals(app.node.Field.class)) {
            fxChild = this.newField((app.node.Field) node, offsetColor);
        } else if (childClass.equals(app.node.InputField.class)) {
            fxChild = this.newInputField(viewName, (app.node.InputField) node, offsetColor);
        } else if (childClass.equals(app.node.Label.class)) {
            fxChild = this.newLabel((app.node.Label) node, offsetColor);
        } else if (childClass.equals(app.node.Image.class)) {
            fxChild = this.newImage((app.node.Image) node);
        } else if (childClass.equals(app.node.HorizontalGroup.class)) {
            fxChild = this.newGroup(viewName, (app.node.HorizontalGroup) node, offsetColor);
            for (BaseNode childNode : ((app.node.Group) node).nodes) {
                this.addNode(viewName, childNode, node.name);
            }
        } else if (childClass.equals(app.node.VerticalGroup.class)) {
            fxChild = this.newGroup(viewName, (app.node.VerticalGroup) node, offsetColor);
            for (BaseNode childNode : ((app.node.Group) node).nodes) {
                this.addNode(viewName, childNode, node.name);
            }
        } else {
            logger.log(Level.SEVERE, "Class is not a supported child class: {0}", childClass.getSimpleName());
            return;
        }
        
        // TODO - This is ugly.  Parent nodes do not have a base type (Parent) with a public getChildren() method so each parent class needs to be handled.
        Class<?> parentControlClass = actualParent.getClass();
        if (parentControlClass.equals(Pane.class)) {
            Pane pane = (Pane) actualParent;
            pane.getChildren().add(fxChild);
            this.positionNode(viewName, node, fxChild, fxParent);
        } else if (parentControlClass.equals(StackPane.class)) {
            StackPane pane = (StackPane) actualParent;
            pane.getChildren().add(fxChild);
        } else if (parentControlClass.equals(HBox.class)) {
            HBox.setHgrow(fxChild, Priority.NEVER); // Preventing HBox from stretching children horizontally just to fill its width
            HBox box = (HBox) actualParent;
            box.getChildren().add(fxChild); 
        } else if (parentControlClass.equals(VBox.class)) {
            VBox.setVgrow(fxChild, Priority.NEVER); // Preventing VBox from stretching children vertically just to fill its height
            VBox box = (VBox) actualParent;
            box.getChildren().add(fxChild);            
        } else {
            logger.log(Level.SEVERE, "Parent does not have a supported class: {0}", parentControlClass.getSimpleName());
            return;
        }
        
        if ((node.effects != null) && (!node.effects.isEmpty())) {
            this.addEffects(node, fxChild, offsetColor);
        }
        
        this.namedControls.get(viewName).put(node.name, fxChild);
        
        logger.log(Level.INFO, "Added node: {0} {1}", new Object[]{childClass.getSimpleName(), node.name});
    }
    
    // TODO - Make this newGrid() and add to addNode()
    @Override
    public void displayGrid(String viewName, app.node.Grid grid) {
        System.out.println("JavaFXApplication: displayGrid: viewName=" + viewName + ", grid=" + grid);
        
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
            
            this.namedControls.get(viewName).put(cellGroup.name + " cell", cell);
            this.addNode(viewName, cellGroup, cellGroup.name + " cell");
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
            this.namedControls.get(viewName).put(name, imageView);
            
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
            this.namedControls.get(viewName).put(name, imageView);
            
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
    
    public static Text stringToText(String string, String fontName, app.Color fontColor, Integer fontSize, app.FontStyle fontStyle) {
        //System.out.println("JavaFXApplication: stringToText: string=" + string + ", fontName=" + fontName + ", fontColor=" + fontColor + ", fontSize=" + fontSize + ", fontStyle=" + fontStyle);
        
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
    
    public TextFlow stringToTextFlow(String string, String fontName, app.Color fontColor, Integer fontSize, app.FontStyle fontStyle) {
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
                Text textNode = stringToText(cluster, fontName, fontColor, fontSize, fontStyle);
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
            this.namedControls.get(viewName).put(name, textFlow);
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
        
        this.namedControls.get(viewName).put(name, flowPane);
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
        
        boolean isJPro = System.getProperty("jpro.version") != null;
        if (!isJPro) {
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
        
        boolean isJPro = System.getProperty("jpro.version") != null;
        if (isJPro) {
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
        
        boolean isJPro = System.getProperty("jpro.version") != null;
        if (isJPro) {
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
        
        boolean isJPro = System.getProperty("jpro.version") != null;
        if (isJPro) {
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
        
        boolean isJPro = System.getProperty("jpro.version") != null;
        if (isJPro) {
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
        
        boolean isJPro = System.getProperty("jpro.version") != null;
        if (isJPro) {
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
        Node control = (Node) this.namedControls.get(viewName).get(name);
        if (control == null) {
            System.out.println("JavaFXApplication: sendToBack: Control not found");
            return;
        }
        control.toFront();
    }
    
    @Override
    public void sendToBack(String viewName, String name) {
        System.out.println("JavaFXApplication: sendToBack: viewName=" + viewName + ", name=" + name);
        Node control = (Node) this.namedControls.get(viewName).get(name);
        if (control == null) {
            System.out.println("JavaFXApplication: sendToBack: Control not found");
            return;
        }
        control.toBack();
    }
}
