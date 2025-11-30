package app;

import app.model.BaseModel;
import app.javafx.DelegateApplication;
import app.model.Coordinates;
import app.model.SpriteModel;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Label;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javax.imageio.metadata.IIOMetadata;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author repp
 */
public class JavaFXApplication extends ApplicationController {
    
    public static List<String> TIMER_EVENTS = new ArrayList();
    
    public DelegateApplication app;
    public Font buttonFont;
    public int buttonFontHeight = 0;
    public int buttonFontWidth = 0;
    public String emptyBook;
    public int fontHeight = 0;
    public int fontWidth = 0;
    public ApplicationView lastSelectedView;
    public Font monospaceFont;
    public HashMap<String, Map<String, Object>> namedControls;
    public ApplicationView parentView;
    public Scene primaryScene;
    public ApplicationView splashView;
    public HashMap<String, Pane> tabContentMap;
    public TabPane tabFolder;
    public HashMap<String, Integer> tabIndexMap;
    public HashMap<String, Tab> tabItemMap;
    public HashMap<Tab, ApplicationView> tabItemViewMap;
    public int textColumns = 0;
    public int textRows = 0;
    public HashMap<String, ApplicationView> views;
    public HashMap<String, List<MediaPlayer>> mediaPlayers = new HashMap();
    
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
        ApplicationController.main(args);
    }
    
    @Override
    public void open(ApplicationView splashView, ApplicationView mainView) {
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
        
        this.app = (DelegateApplication)delegate;
        
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
        System.exit(0);     // Stop any remaining framework processes, including background processes
    }
    
    public void displayStage(ApplicationView view) {
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
        this.app.primaryStage.setTitle(this.parentView.name);

        // Size the application dimensions        
        Coordinates dimensions = getDimensions(this.parentView.backgroundImage);
        
        // Set the application icon
        if (this.parentView.iconFileName != null) {
            Image iconImage = loadImage(this.parentView.iconFileName);
            this.app.primaryStage.getIcons().add(iconImage);
        }
        
        // Initialize the application's tab folder and set it as the application's primary scene
        this.tabFolder = new TabPane();
        this.tabFolder.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            String selectedTabTitle = newTab.getText();
            System.out.println("JavaFXApplication: showPrimaryStage: Selected tab " + selectedTabTitle);
            ApplicationView selectedView = this.tabItemViewMap.get(newTab);
            if (selectedView != null) {
                selectedView.onSelected(this);
                ApplicationView lastSelectedView = this.lastSelectedView;
                this.lastSelectedView = selectedView;
                if ((lastSelectedView != null) && (!lastSelectedView.equals(selectedView))) {
                    lastSelectedView.onUnselected(this);
                }
            }
        });
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(this.tabFolder);
        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        //this.primaryScene = new Scene(this.tabFolder, dimensions.x, dimensions.y);
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
        this.app.primaryStage.setScene(primaryScene);

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
        this.monospaceFont = Font.font("Consolas", FontWeight.NORMAL, adjustFontSizeForDPI(12));
        this.buttonFont = Font.font("Consolas", FontWeight.NORMAL, adjustFontSizeForDPI(10));
        
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
        this.textColumns = ((int) dimensions.x / this.fontWidth) + 1;
        this.textRows = ((int) dimensions.y / this.fontHeight) + 1;
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
        
        this.app.primaryStage.show();
        
        // TODO - Stop all sounds
    }
    
    public static double adjustFontSizeForDPI(int fontSize) {
        Screen screen = Screen.getPrimary();
        double dpi = screen.getDpi();
        double scaleFactor = dpi / 96.0;    // Standard DPI is typically 96.0, so this calculates the scaling factor
        double newFontSize = fontSize * scaleFactor;
        return newFontSize;
    }
    
    public void addView(ApplicationView view, Boolean isParent) {
        Integer index = this.tabIndexMap.get(view.name);
        if (index == null) {
            index = this.tabIndexMap.size();
        }
        this.addView(view, isParent, index);
    }
    
    @Override
    public void displayView(ApplicationView view) {
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
        
        ApplicationView view = views.get(viewName);
        if (view == null) {
            System.out.println("JavaFXApplication: displayView: View does not exist!");
            return;
        }
        
        this.displayView(view);
    }
    
    @Override
    public void displayOverlay(String viewName, String name, app.Color color, Integer startRow, Integer startColumn, Integer endRow, Integer endColumn, Integer transparency) {
        System.out.println("JavaFXApplication: displayOverlay: viewName=" + viewName + ", name=" + name + ", color=" + color + ", startRow=" + startRow + ", startColumn=" + startColumn + ", endRow=" + endRow + ", endColumn=" + endColumn + ", transparency=" + transparency);
        
        Pane content = this.tabContentMap.get(viewName);
        
        Coordinates topLeftCoordinates;
        Coordinates bottomRightCoordinates;
        if (startRow == null) {
            topLeftCoordinates = new Coordinates(0, 0);
            bottomRightCoordinates = new Coordinates((int) content.getWidth(), (int) content.getHeight());
            transparency = 128;
        } else {
            topLeftCoordinates = this.convertToCoordinates(startRow, startColumn);
            bottomRightCoordinates = this.convertToCoordinates(endRow, endColumn);
        }
        
        int width = bottomRightCoordinates.x - topLeftCoordinates.x;
        int height = bottomRightCoordinates.y - topLeftCoordinates.y;
        Rectangle overlay = new Rectangle(topLeftCoordinates.x, topLeftCoordinates.y, width, height);
        double opacityPercent = (1.0 - ((double)transparency / 255.0)); // Transparency is 0-255
        System.out.println("JavaFXApplication: displayOverlay: Converted opacity percent to " + opacityPercent);
        overlay.setFill(new Color((color.red + 1) / 255, (color.green + 1) / 255, (color.blue + 1) / 255, opacityPercent));
        content.getChildren().add(overlay);
        this.namedControls.get(viewName).put(name, overlay);
    }
    
    @Override
    public void clearScreen(String viewName) {
        System.out.println("JavaFXApplication: clearScreen : viewName=" + viewName); 

        if (this.namedControls.get(viewName) != null) {
            this.namedControls.get(viewName).clear();
        }
        
        Pane content = this.tabContentMap.get(viewName);
        //HTMLEditor editor = this.tabEditorMap.get(viewName);
        
        if (content != null) {
            //content.getChildren().removeIf(node -> node != editor);
            content.getChildren().clear();
        }
        
        // Clear the book text if this view uses a text area
        /*
        if (editor != null) {
            this.tabEditorTextMap.put(viewName, this.emptyBook);
        }
        */
    }
    
    @Override
    public void clearControl(String viewName, String controlName) {
        System.out.println("JavaFXApplication: clearControl : viewName=" + viewName + ", controlName=" + controlName);
        Object control = this.namedControls.get(viewName).get(controlName);
        if (control != null) {
            Pane content = this.tabContentMap.get(viewName);
            content.getChildren().remove((Node) control);
            this.namedControls.get(viewName).remove(controlName);
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
    public void renameTab(String viewName, String newViewName) {
        System.out.println("JavaFXApplication: renameTab: viewName=" + viewName + ", newViewName=" + newViewName);
        Tab tab = this.tabItemMap.get(viewName);
        tab.setText(newViewName);
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
    public void addView(ApplicationView view) {
        this.addView(view, false);
    }
    
    @Override
    public void addView(ApplicationView view, Boolean isParent, int index) {
        System.out.println("JavaFXApplication: addView: name=" + view.name + ", isParent=" + isParent + ", index=" + index);

        StackPane content = new StackPane();

        if (isParent) {
            // Not supported at this time
            view.onLoad(this);
            return;
        }
        
        // Create a new tab
        String tabName;
        if (view.emoji != null) {
            tabName = view.emoji + " " + view.name;
        } else {
            tabName = view.name;
        }
        Tab tab = new Tab(tabName);
        tab.setClosable(false);
        this.tabFolder.getTabs().add(index, tab);
        tab.setContent(content);
        this.tabItemMap.put(view.name, tab);
        this.tabItemViewMap.put(tab, view);
        
        // Configure the background
        Background background = null;
        if (view.backgroundImage != null) {
            System.out.println("JavaFXApplication: addView: name=" + view.name + ", using background image " + view.backgroundImage);
            Image image = loadImage(view.backgroundImage);
            Coordinates dimensions = this.getDimensions(view.backgroundImage);
            BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT, // Repeat in X direction
                BackgroundRepeat.NO_REPEAT, // Repeat in Y direction
                BackgroundPosition.DEFAULT,   // Position of the image
                // TODO - Other examples use 1.0
                new BackgroundSize(dimensions.x, dimensions.y, true, true, true, false) // Size of the image (100% width and height)
            );
            
            // TODO - Check and handle for no background color
            Color backgroundColor = Color.rgb(view.backgroundColor.red, view.backgroundColor.green, view.backgroundColor.blue);
            BackgroundFill backgroundFill = new BackgroundFill(
                backgroundColor, // The color to use
                CornerRadii.EMPTY, // No rounded corners
                Insets.EMPTY // No padding
            );
            
            background = new Background(Collections.singletonList(backgroundFill), Collections.singletonList(backgroundImage));
            content.setBackground(background);
            content.setPrefSize(dimensions.x, dimensions.y);
            // TODO - How to specify background color for images with transparency?
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
        }
        
        /*
        if (view.addTextArea) {
            // Add a text area to the composite
            this.tabEditorTextMap.put(view.name, this.emptyBook);
            HTMLEditor htmlEditor = new HTMLEditor();
            this.tabEditorMap.put(view.name, htmlEditor);
            htmlEditor.setBackground(background);
            // Without contentEditable=\"true\" in the body the editor is read-only by default
            //String transparentContentCss = "<style>"
            //                             + "body { background-color: transparent !important; }"
            //                             + "</style>";
            String initialHTML;
            if (view.backgroundImage == null) {
                initialHTML = "<body style='background-color: transparent;'>"
                                        + this.emptyBook
                                        + "</body>";

                //htmlEditor.setStyle("-fx-background-color: rgba(0, 0, 0, 0);"); // Transparent black
            } else {
                String imagePath = getClass().getResource(view.backgroundImage).toExternalForm();
                Coordinates backgroundImageDimensions = this.getDimensions(view.backgroundImage);
                initialHTML = String.format(
                    "<body style='background-image: url(\"%s\"); background-size: %dpx %dpx; background-repeat: no-repeat;'>Type on top of the image...</body>",
                    imagePath,
                    backgroundImageDimensions.x,
                    backgroundImageDimensions.y
                );
                htmlEditor.setPrefSize(backgroundImageDimensions.x, backgroundImageDimensions.y);
                System.out.println("TEST: backgroundImageDimensions.x=" + backgroundImageDimensions.x + ", backgroundImageDimensions.y=" + backgroundImageDimensions.y);
            }
            htmlEditor.setHtmlText(initialHTML);
            
            //htmlEditor.lookup(".web-view").setStyle("-fx-background-color: transparent;");
            content.getChildren().add(htmlEditor);
            //htmlEditor.setPrefHeight(250);
        }
        */
        
        // Add overlay pane AFTER HTMLEditor as StackPane displays its contents back-to-front
        Pane overlayPane = new Pane();
        content.getChildren().add(overlayPane);
        this.tabContentMap.put(view.name, overlayPane);
        
        view.onLoad(this);
    }
    
    @Override
    public void displayMessageBox(String title, String text, int level) {
        System.out.println("JavaFXApplication: displayMessageBox: title=" + title + ", text=" + text + ", level=" + level);
        
        AlertType type = switch (level) {
            case Icon.INFORMATION -> AlertType.INFORMATION;
            case Icon.WARNING -> AlertType.WARNING;
            case Icon.ERROR -> AlertType.ERROR;
            default -> AlertType.INFORMATION;
        };
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.initOwner(this.app.primaryStage);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(text);
            alert.show();
        });
    }
    
    @Override
    public void displayText(String viewName, String text, Integer row, Integer column) {
        System.out.println("JavaFXApplication: displayText: viewName=" + viewName + ", text=" + text + ", row=" + row + ", column=" + column);
        displayText(viewName, text, row, column, new app.Color(0, 0, 0));
    }
    
    @Override
    public void displayText(String viewName, String text, Integer row, Integer column, app.Color color) {
        System.out.println("JavaFXApplication: displayText: viewName=" + viewName + ", text=" + text + ", row=" + row + ", column=" + column + ", color=" + color);
        displayText(viewName, text, row, column, color, FontStyle.NORMAL);
    }
    
    @Override
    public void displayText(String viewName, String text, Integer row, Integer column, app.Color color, int style) {
        System.out.println("JavaFXApplication: displayText: viewName=" + viewName + ", text=" + text + ", row=" + row + ", column=" + column + ", color=" + color + ", style=" + style);
        
        this.displayFloatingText(viewName, text, row, column, null, null, color, 12, style, "Arial Unicode MS"); // Previously, "Consolas"
        
        /*
        
        HTMLEditor editor = this.tabEditorMap.get(viewName);
        
        Color fxColor = Color.rgb(color.red, color.green, color.blue);
        
        FontPosture fxStyle = null;
        FontWeight fxWeight = FontWeight.NORMAL;
        switch (style) {
            case FontStyle.NORMAL -> fxWeight = FontWeight.NORMAL;
            case FontStyle.BOLD -> fxWeight = FontWeight.BOLD;
            case FontStyle.ITALIC -> fxStyle = FontPosture.ITALIC;
            case FontStyle.UNDERLINE_DOUBLE -> {
                // TODO - Not supported, needs styling
                fxWeight = FontWeight.NORMAL;
                //label.setUnderline(true);
            }
            case FontStyle.UNDERLINE_ERROR -> {
                // TODO - Not supported, needs styling
                fxWeight = FontWeight.NORMAL;
                //label.setUnderline(true);
            }
            case FontStyle.UNDERLINE_LINK -> {
                // TODO - Not supported, needs styling
                fxWeight = FontWeight.NORMAL;
                //label.setUnderline(true);
            }
            case FontStyle.UNDERLINE_SINGLE -> {
                fxWeight = FontWeight.NORMAL;
                //label.setUnderline(true);
            }
            case FontStyle.UNDERLINE_SQUIGGLE -> {
                // TODO - Not supported, needs styling
                fxWeight = FontWeight.NORMAL;
                //label.setUnderline(true);
            }
            default -> {
                System.err.println("JavaFXApplication: displayText: Unsupported font style!");
                fxWeight = FontWeight.NORMAL;
            }
        }
        
        String currentText = editor.getHtmlText();
        Integer position = column - 1; // String positions start at zero
        position = position + (this.textColumns * (row - 1)) + (row - 1);
        System.out.println("JavaFXApplication: displayText: this.textColumns=" + this.textColumns + ", position=" + position + ", SWTStyle=" + fxStyle + ", color=" + fxColor);
        StringBuilder sb = new StringBuilder(currentText);
        sb.replace(position, position + text.length(), text);
        editor.setHtmlText(sb.toString());
        
        // TODO - Add styling by tracking each entry in a separate data structure with proper HTML styling tags
        */
    }
    
    @Override
    public void displayGrid(String viewName, Map<String, ArrayList<BaseModel>> gridCells, int columns, Boolean showBorders, EventListener listener) {
        System.out.println("JavaFXApplication: displayGrid: viewName=" + viewName + ", cells=" + gridCells.size());
        
        Tab tab = this.tabItemMap.get(viewName);
        GridPane content = new GridPane();
        tab.setContent(content);
        if (showBorders) {
            String borderColor = "#000000"; // Black
            content.setStyle("-fx-background-color: " + borderColor + ";");
        }
        content.setPadding(new Insets(10));
        content.setHgap(5);
        content.setVgap(5);
        Background background;
        ApplicationView view = this.views.get(viewName);
        if (view.backgroundImage != null) {
            System.out.println("JavaFXApplication: displayGrid: name=" + view.name + ", using background image " + view.backgroundImage);
            Image image = loadImage(view.backgroundImage);
            Coordinates dimensions = this.getDimensions(view.backgroundImage);
            BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT, // Repeat in X direction
                BackgroundRepeat.NO_REPEAT, // Repeat in Y direction
                BackgroundPosition.DEFAULT,   // Position of the image
                // TODO - Other examples use 1.0
                new BackgroundSize(dimensions.x, dimensions.y, true, true, true, false) // Size of the image (100% width and height)
            );
            
            // TODO - Check and handle for no background color
            Color backgroundColor = Color.rgb(view.backgroundColor.red, view.backgroundColor.green, view.backgroundColor.blue);
            BackgroundFill backgroundFill = new BackgroundFill(
                backgroundColor, // The color to use
                CornerRadii.EMPTY, // No rounded corners
                Insets.EMPTY // No padding
            );
            
            background = new Background(Collections.singletonList(backgroundFill), Collections.singletonList(backgroundImage));
            content.setBackground(background);
            content.setPrefSize(dimensions.x, dimensions.y);
            // TODO - How to specify background color for images with transparency?
        } else if (view.backgroundColor != null) {
            System.out.println("JavaFXApplication: displayGrid: name=" + view.name + ", using background color " + view.backgroundColor);
            Color backgroundColor = Color.rgb(view.backgroundColor.red, view.backgroundColor.green, view.backgroundColor.blue);
            BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY);
            background = new Background(backgroundFill);
            content.setBackground(background);
        }
                    
        if (columns == 0) {
            double squareRoot = Math.sqrt(gridCells.size());
            columns = (int) Math.ceil(squareRoot);
        }
        int rows = 0;
        if (columns != 0) {
            double rowsDiv = ((double) gridCells.size() / (double) columns);  // Make sure values are double so remainder causes rows count to round up
            rows = (int) Math.ceil(rowsDiv);
        }
        
        // Allow rows to expand as much as they can
        int rowHeight = (int) Math.floor(100 / rows);
        for (int rowI = 1; rowI <= rows; rowI++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(rowHeight);
            content.getRowConstraints().add(row);
        }
        
        // Allow columns to expand as much as they can
        int columnWidth = (int) Math.floor(100 / columns);
        for (int colI = 1; colI <= columns; colI++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(columnWidth);
            content.getColumnConstraints().add(column);
        }

        System.out.println("JavaFXApplication: displayGrid: columns=" + columns + ", rows=" + rows + ", cells=" + gridCells.size());
        
        //GridLayout gridLayout = new GridLayout(columns, true); // 3 columns, equal width
        //composite.setLayout(gridLayout);
        int currentRow = 1;
        int currentColumn = 0;
        
        for (String cellName : gridCells.keySet()) {
            ArrayList<BaseModel> controls = gridCells.get(cellName);
            
            StackPane cell = new StackPane();
            //cell.setStyle("-fx-background-color: transparent;");
            currentColumn++;
            if (currentColumn > columns) {
                currentRow++;
                currentColumn = 1;
            }
            String backgroundColor = "transparent";
            Color fontColor = Color.rgb(0, 0, 0);
            Boolean addBorder = false;
            if (!controls.isEmpty()) {
                app.Color genericBackgroundColor = controls.getFirst().backgroundColor;
                if (genericBackgroundColor != null) {
                    backgroundColor = String.format("#%02X%02X%02X", genericBackgroundColor.red, genericBackgroundColor.green, genericBackgroundColor.blue);
                    addBorder = true;
                    double luminance = (0.299 * genericBackgroundColor.red) + (0.587 * genericBackgroundColor.green) + (0.114 * genericBackgroundColor.blue);
                    System.out.println("JavaFXApplication: displayGrid: luminance for " + genericBackgroundColor + " is " + luminance);
                    if (luminance < 128) {
                        fontColor = Color.rgb(255, 255, 255);
                    }
                }
            }
            content.add(this.createBorderedCellContent(cell, backgroundColor, addBorder), currentColumn - 1, currentRow - 1);
            
            // Ensure the cell content grows horizontally and vertically (Priority.ALWAYS)
            GridPane.setHgrow(cell, Priority.ALWAYS);
            GridPane.setVgrow(cell, Priority.ALWAYS);
            cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); 
            
            VBox verticalContainer = new VBox(10); // 10px spacing between children
            verticalContainer.setAlignment(Pos.CENTER);
            verticalContainer.setPadding(new Insets(20)); // Add padding around the edges
            cell.getChildren().add(verticalContainer);

            //cellComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
            // TODO - Using the first control's background color is a little cludgy
            //Color backgroundColor = null;
            /*
            int rgbSum = 0;
            if (!controls.isEmpty()) {
                System.out.println("JavaFXApplication: displayGrid: Cell count: " + controls.size());
                app.Color genericBackgroundColor = controls.getFirst().backgroundColor;
                if (genericBackgroundColor != null) {
                    backgroundColor = new Color(this.display, genericBackgroundColor.red, genericBackgroundColor.green, genericBackgroundColor.blue);
                    rgbSum = genericBackgroundColor.red + genericBackgroundColor.green + genericBackgroundColor.blue;
                }
            } else {
                System.out.println("JavaFXApplication: displayGrid: Empty cell");
            }
            
            cellComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); // Fill the cell
            cellComposite.setLayout(new GridLayout());
            Color foregroundColor = null;
            if (backgroundColor != null) {
                cellComposite.setBackground(backgroundColor);
                if (rgbSum > 382) {
                    // 255 * 3 = 765 as a maximum value for white.  Use black as the font color if on the lighter half of the color scale.
                    foregroundColor = new Color(this.display, 0, 0, 0);
                } else {
                    foregroundColor = new Color(this.display, 255, 255, 255);
                }
            } else {
                // SWT.COLOR_TRANSPARENT isn't available until SWT 4.5
                cellComposite.setBackground(this.display.getSystemColor(SWT.COLOR_TRANSPARENT)); // Set label background to transparent
            }
            */
            
            // Add zero to many controls to the grid cell
            for (BaseModel abstractControl : controls) {
                System.out.println("JavaFXApplication: displayGrid: Adding control " + abstractControl.getClass().getName());
                Node control = null;
                if (abstractControl.getClass().equals(app.model.LinkModel.class)) {
                    String linkText = abstractControl.text.replace("<a>", "");
                    linkText = linkText.replace("</a>", "");
                    Hyperlink hyperlink = new Hyperlink(linkText);
                    Font currentFont = hyperlink.getFont();
                    hyperlink.setFont(Font.font(currentFont.getFamily(), FontWeight.BOLD, currentFont.getSize()));
                    hyperlink.setDisable(!abstractControl.isEnabled);
                    if (listener != null) {
                        hyperlink.setOnAction(e -> {
                            System.out.println("JavaFXApplication: displayGrid: Link clicked: name=" + cellName);
                            listener.onEvent(cellName, null);
                        });
                    }
                    verticalContainer.getChildren().add(hyperlink);
                    control = hyperlink;
                    System.out.println("JavaFXApplication: displayGrid: Added link " + abstractControl.text + " for " + cellName);
                } else if (abstractControl.getClass().equals(app.model.ButtonModel.class)) {
                    Button button = new Button(abstractControl.text);
                    button.setFont(this.buttonFont);
                    if (listener != null) {
                        System.out.println("JavaFXApplication: displayGrid: Button clicked: name=" + cellName);
                        button.setOnAction(e -> listener.onEvent(cellName, null));
                    }
                    button.setDisable(!abstractControl.isEnabled);
                    verticalContainer.getChildren().add(button);
                    control = button;
                    System.out.println("JavaFXApplication: displayGrid: Added button " + abstractControl.text + " for " + cellName);
                } else if (abstractControl.getClass().equals(app.model.LabelModel.class)) {
                    Label label = new Label(abstractControl.text);
                    Font currentFont = label.getFont();
                    label.setFont(Font.font(currentFont.getFamily(), FontWeight.BOLD, currentFont.getSize()));
                    label.setTextFill(fontColor);
                    label.setWrapText(true);
                    label.setAlignment(Pos.CENTER);
                    label.setTextAlignment(TextAlignment.CENTER);
                    verticalContainer.getChildren().add(label);
                    control = label;
                    System.out.println("JavaFXApplication: displayGrid: Added label " + abstractControl.text + " for " + cellName);
                } else if (abstractControl.getClass().equals(app.model.ImageModel.class)) {
                    final Image image = loadImage(abstractControl.text);
                    ImageView imageView = new ImageView(image);
                    verticalContainer.getChildren().add(imageView);
                    control = imageView;
                    System.out.println("JavaFXApplication: displayGrid: Added image " + abstractControl.text + " for " + cellName);
                }
                
                if (control != null) {
                    control.setStyle("-fx-background-color: transparent;");
                    System.out.println("JavaFXApplication: displayGrid: Added " + abstractControl.getClass().getName() + " control " + abstractControl.text + " for " + cellName);
                }
            }
        }
    }
    
    public StackPane createBorderedCellContent(Node content, String bgColor, Boolean addBorder) {
        System.out.println("JavaFXApplication: createBorderedCellContent: bgColor=" + bgColor + ", addBorder=" + addBorder);
        
        StackPane cellWrapper = new StackPane(content);
        
        // --- Apply Borders and Background using Java Style Strings ---
        String borderStyle = "-fx-border-color: black; -fx-border-width: 1px;";
        String backgroundStyle = "-fx-background-color: " + bgColor + ";";
        if (addBorder) {
            cellWrapper.setStyle(borderStyle + backgroundStyle);
        } else {
            cellWrapper.setStyle(backgroundStyle);
        }
        
        // Ensure the content inside the stackpane is centered (StackPane default)
        if (content instanceof Label label) {
            label.setAlignment(Pos.CENTER);
        }

        // Ensure the wrapper expands to fill the grid cell space
        cellWrapper.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        // Set growth priorities so the GridPane expands this wrapper
        GridPane.setHgrow(cellWrapper, Priority.ALWAYS);
        GridPane.setVgrow(cellWrapper, Priority.ALWAYS);
        
        return cellWrapper;
    }
      
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
        
        content.getChildren().add(hyperlink);
        
        /*
        HTMLEditor editor = this.tabEditorMap.get(viewName);
        
        String currentText = editor.getHtmlText();
        Integer position = column - 1; // String positions start at zero
        position = position + (this.textColumns * (row - 1)) + (row - 1);
        StringBuilder sb = new StringBuilder(currentText);
        linkText = "<a href=\"" + name + "\">" + linkText + "</a>";
        sb.replace(position, position + linkText.length(), linkText);
        editor.setHtmlText(sb.toString());
        
        // After the HTMLEditor is laid out, access its WebEngine
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == javafx.concurrent.Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("listener", listener);

                String script = """
                    document.body.addEventListener('click', function(event) {
                        var target = event.target;
                        if (target.tagName === 'A' || target.closest('a')) {
                            event.preventDefault();
                            var anchor = target.tagName === 'A' ? target : target.closest('a');
                            var eventName = anchor.href;
                            console.log('Link clicked with URL: ' + eventName);
                            listener.onEvent(eventName);
                        }
                    });
                """;
                webEngine.executeScript(script);
            }
        });
        */
    }
    
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
        button.setLayoutX(coordinates.x);
        button.setLayoutY(coordinates.y);
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
            Font font = Font.font(fontName, FontWeight.NORMAL, 12);
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
            // The standard glow effect just makes the controls exceptionally bright
            /*
            Glow glowEffect = new Glow(0.8);
            button.setEffect(glowEffect);
            */
            
            // Drop shadow is better but not as elegant as providing styling for normal shadow and mouse hover
            /*
            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetX(0);   // Centered
            dropShadow.setOffsetY(0);   // Centered
            Color glowColor = Color.rgb(139, 0, 139);   // Dark Magenta
            dropShadow.setColor(glowColor);
            dropShadow.setRadius(15);
            dropShadow.setSpread(0.6);
            button.setEffect(dropShadow);
            */
            
            String defaultStyle = "-fx-effect: dropshadow(three-pass-box, rgba(139, 0, 139, 0.8), 5, 0.8, 0, 0);";
            String hoverStyle = "-fx-effect: dropshadow(three-pass-box, rgba(139, 0, 139, 1), 10, 0.8, 0, 0);";
            button.setStyle(defaultStyle);
            button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
            button.setOnMouseExited(e -> button.setStyle(defaultStyle));
        }
        
        return button;
    }
    
    @Override
    public void displayOpenFileButton(String viewName, String name, String text, Integer row, Integer column, Integer endRow, Integer endColumn, Boolean isMonospace, String fontName, Boolean glow, EventListener listener) {
        System.out.println("JavaFXApplication: displayOpenFileButton: viewName=" + viewName + ", name=" + name + ", text=" + text + ", row=" + row + ", column=" + column + ", endRow=" + endRow + ", endColumn=" + endColumn + ", isMonospace=" + isMonospace + ", fontName=" + fontName + ", glow=" + glow);
        
        Button button = this.newButton(viewName, name, text, row, column, endRow, endColumn, isMonospace, fontName, glow, listener);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("/home/repp/Documents/quests/"));
        
        button.setOnAction(event -> {
            File selectedFile = fileChooser.showOpenDialog(this.app.primaryStage);
            
            if (selectedFile != null) {
                String selectedFilePath = selectedFile.getAbsolutePath();
                System.out.println("JavaFXApplication: displayOpenFileButton: Selected file=" + selectedFilePath);
                listener.onEvent(name, selectedFilePath);
            } else {
                System.out.println("JavaFXApplication: displayOpenFileButton: No file was selected");
            }
        });
    }
    
    @Override
    public int displayImage(String viewName, String fileName, int row, int column) {
        System.out.println("JavaFXApplication: displayImage: viewName=" + viewName + ", fileName=" + fileName + ", row=" + row + ", column=" + column);
        
        Pane content = this.tabContentMap.get(viewName);

        final Image image = loadImage(fileName);
        Coordinates dimensions = getDimensions(fileName);
        
        ImageView imageView = new ImageView(image);
        Coordinates coordinates = this.convertToCoordinates(row, column);
        imageView.setLayoutX(coordinates.x + 1);
        imageView.setLayoutY(coordinates.y + 1);
        content.getChildren().add(imageView);
        
        // Advance the text cursor automatically
        int nextRow = row + this.getRows(dimensions.y);
        return nextRow;
    }
    
    @Override
    public void displayFloatingText(String viewName, String text, Integer startRow, Integer startColumn, Integer endRow, Integer endColumn, app.Color fontColor, Integer fontSize, Integer fontStyle, String fontName) {
        System.out.println("JavaFXApplication: displayFloatingText: viewName=" + viewName + ", text=" + text + ", startRow=" + startRow + ", startColumn=" + startColumn + ", endRow=" + endRow + ", endColumn=" + endColumn + ", fontColor=" + fontColor + ", fontSize=" + fontSize + ", fontName=" + fontName);
        
        Pane content = this.tabContentMap.get(viewName);

        Label label = new Label(text);
        label.setStyle("-fx-background-color: transparent;");
        label.setWrapText(true);
        if ((endRow != null) && (endColumn != null)) {
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
        }
        
        if (fontStyle == null) {
            fontStyle = FontStyle.NORMAL;
        }
        
        // TODO - Move this to a helper method
        FontPosture fxStyle = null;
        FontWeight fxWeight = FontWeight.NORMAL;
        switch (fontStyle) {
            case FontStyle.NORMAL -> fxWeight = FontWeight.NORMAL;
            case FontStyle.BOLD -> fxWeight = FontWeight.BOLD;
            case FontStyle.ITALIC -> fxStyle = FontPosture.ITALIC;
            case FontStyle.UNDERLINE_DOUBLE -> {
                // TODO - Not supported, needs styling
                fxWeight = FontWeight.NORMAL;
                label.setUnderline(true);
            }
            case FontStyle.UNDERLINE_ERROR -> {
                // TODO - Not supported, needs styling
                fxWeight = FontWeight.NORMAL;
                label.setUnderline(true);
            }
            case FontStyle.UNDERLINE_LINK -> {
                // TODO - Not supported, needs styling
                fxWeight = FontWeight.NORMAL;
                label.setUnderline(true);
            }
            case FontStyle.UNDERLINE_SINGLE -> {
                fxWeight = FontWeight.NORMAL;
                label.setUnderline(true);
            }
            case FontStyle.UNDERLINE_SQUIGGLE -> {
                // TODO - Not supported, needs styling
                fxWeight = FontWeight.NORMAL;
                label.setUnderline(true);
            }
            default -> {
                System.err.println("JavaFXApplication: displayText: Unsupported font style!");
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
            // TODO - For some reason this doesn't use Italics
            font = Font.font(fontName, fxStyle, newFontSize);
        } else {
            font = Font.font(fontName, fxWeight, newFontSize);
        }
        label.setFont(font);
        if (fontColor != null) {
            label.setTextFill(Color.rgb(fontColor.red, fontColor.green, fontColor.blue));
        }
        
        // Calculate font height and width
        Coordinates startCoordinates = this.convertToCoordinates(startRow, startColumn);
        Coordinates endCoordinates;
        if ((endRow != null) && (endColumn != null)) {
            endCoordinates = this.convertToCoordinates(endRow, endColumn);
        } else if (newFontSize <= 12) {
            endCoordinates = this.convertToCoordinates(startRow + 1, startColumn + text.length());
        } else {
            endCoordinates = this.convertToCoordinates(startRow + 4, (int) (startColumn + (text.length() * (newFontSize / 12) * 1.5) + 1));
        }
        
        label.setLayoutX(startCoordinates.x);
        label.setLayoutY(startCoordinates.y);
        label.setPrefSize(endCoordinates.x - startCoordinates.x - 1, endCoordinates.y - startCoordinates.y - 1);
        content.getChildren().add(label);
    }
    
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
        field.setLayoutX(startCoordinates.x);
        field.setLayoutY(startCoordinates.y);
        // TODO - setPrefSize?
        
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
    
    @Override
    public void displayValidatedInputField(String viewName, String name, List<String> values, int row, int startColumn, int endColumn, int alignment, EventListener listener, Boolean allowRepeatClicks) {
        System.out.println("JavaFXApplication: displayValidatedInputField: viewName=" + viewName + ", name=" + name + ", row=" + row + ", startColumn=" + startColumn + ", endColumn=" + endColumn + ", alignment=" + alignment + ", listener=" + listener + ", allowRepeatClicks=" + allowRepeatClicks);
        
        Pane content = this.tabContentMap.get(viewName);

        // Display a row of buttons with the possible input values
        int buttonHeight = 2 * this.buttonFontHeight;   // Calculate double height of text
        Coordinates coordinates = this.convertToCoordinates(row, startColumn);
        Coordinates terminalCoordinates = this.convertToCoordinates(row, endColumn);
        int buttonX;
        int buttonY;
        switch (alignment) {
            case Alignment.LEFT -> {
                System.out.println("SWTApplication: displayValidatedInputField: Left alignment");
            }
            case Alignment.CENTER -> {
                System.out.println("SWTApplication: displayValidatedInputField: Center alignment");
                // Calculate the full width of the button row
                int rowWidth = 0;
                for (String value : values) {
                    value = value.toUpperCase().replaceFirst("&UP;", "K");
                    value = value.toUpperCase().replaceFirst("&DOWN;", "K");
                    value = value.toUpperCase().replaceFirst("&LEFT;", "K");
                    value = value.toUpperCase().replaceFirst("&RIGHT;", "K");
                    if (value.charAt(0) == '*') {
                        value = value.substring(1, value.length());
                    }
                    int tempButtonWidth = (value.length() * this.buttonFontWidth) + (2 * this.buttonFontWidth);    // Calculate width of text plus buffer of two imaginary characters
                    rowWidth += tempButtonWidth + (1 * this.buttonFontWidth);   // Add a spacer between this button and the next
                }
                coordinates.x = (int) (terminalCoordinates.x - Math.floor(rowWidth / 2));
            }
            case Alignment.RIGHT -> {
                System.out.println("SWTApplication: displayValidatedInputField: Right alignment");
                // Calculate the full width of the button row
                int rowWidth = 0;
                for (String value : values) {
                    value = value.toUpperCase().replaceFirst("&UP;", "K");
                    value = value.toUpperCase().replaceFirst("&DOWN;", "K");
                    value = value.toUpperCase().replaceFirst("&LEFT;", "K");
                    value = value.toUpperCase().replaceFirst("&RIGHT;", "K");
                    if (value.charAt(0) == '*') {
                        value = value.substring(1, value.length());
                    }
                    int tempButtonWidth = (value.length() * this.buttonFontWidth) + (2 * this.buttonFontWidth);    // Calculate width of text plus buffer of two imaginary characters
                    rowWidth += tempButtonWidth + (1 * this.buttonFontWidth);   // Add a spacer between this button and the next
                }
                coordinates.x = terminalCoordinates.x - rowWidth;
            }
            default -> {
                System.err.println("SWTApplication: displayValidatedInputField: Unsupported alignment!");
            }
        }
        buttonX = coordinates.x + 1;
        buttonY = coordinates.y + 1;
        
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
            Integer keyBinding = null;
            String eventValue = value;
            if (value.toUpperCase().contains("&UP;")) {
                //keyBinding = SWT.ARROW_UP;
                eventValue = value.replaceFirst("(?i)" + "&UP;", "");
                value = value.replaceFirst("(?i)" + "&UP;", "\u2B06");  // Case insensitive reg ex
            } else if (value.toUpperCase().contains("&DOWN;")) {
                //keyBinding = SWT.ARROW_DOWN;
                eventValue = value.replaceFirst("(?i)" + "&DOWN;", "");
                value = value.replaceFirst("(?i)" + "&DOWN;", "\u2B07");  // Case insensitive reg ex
            } else if (value.toUpperCase().contains("&LEFT;")) {
                //keyBinding = SWT.ARROW_LEFT;
                eventValue = value.replaceFirst("(?i)" + "&LEFT;", "");
                value = value.replaceFirst("(?i)" + "&LEFT;", "\u2190");  // Case insensitive reg ex
            } else if (value.toUpperCase().contains("&RIGHT;")) {
                //keyBinding = SWT.ARROW_RIGHT;
                eventValue = value.replaceFirst("(?i)" + "&RIGHT;", "");
                value = value.replaceFirst("(?i)" + "&RIGHT;", "\u27A1");  // Case insensitive reg ex
            }
            final String finalValue = eventValue;
            Button button = new Button(value);
            if (disable) {
                button.setDisable(true);
            }
            button.setFont(this.buttonFont);
            button.setOnAction(e -> {
                if (!allowRepeatClicks) {
                    button.setDisable(true);
                }
                listener.onEvent(name, finalValue);
            });
            
            /*
            if (keyBinding != null) {
                // A button can only trap key events when it has focus, so add a key listener to the shell that gets removed when the button is disposed
                final int finalKeyBinding = (int)keyBinding;
                final SWTApplication thisAppController = this;
                final KeyListener textAreaKeyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if ((e.keyCode == finalKeyBinding) && (!button.isDisposed())) {
                            if ((thisAppController.lastProcessedKeyEvent == null) || (thisAppController.lastProcessedKeyEvent.time != e.time) || (thisAppController.lastProcessedKeyEvent.keyCode != e.keyCode)) {
                                e.doit = false; // e hasn't been checked yet and the refreshed page's event handling will process the event in an infinite loop
                                if (!allowRepeatClicks) {
                                    button.setEnabled(false);
                                }
                                thisAppController.lastProcessedKeyEvent = e; // e.doit isn't processed until this method returns so to further prevent an infinite loop guarantee the key event is new
                                listener.onEvent(name, finalValue);
                            }
                        }
                    }
                };
                textArea.addKeyListener(textAreaKeyListener);
                button.addDisposeListener(e -> {
                    textArea.removeKeyListener(textAreaKeyListener);
                });
            }
            */
            int buttonWidth = (finalValue.length() * this.buttonFontWidth) + (2 * this.buttonFontWidth);    // Calculate width of text plus buffer of two imaginary characters
            if ((buttonX + buttonWidth) > terminalCoordinates.x) {
                // Wrap the button onto a new line
                buttonX = coordinates.x + 1;
                buttonY = (int) (buttonY + buttonHeight + ((1 * this.buttonFontWidth)));
            }
            //button.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
            //button.moveAbove(textArea);
            buttonX = buttonX + buttonWidth + (1 * this.buttonFontWidth);   // Add a spacer between this button and the next
            
            // TODO - newButton should be used to prevent code duplication
            if (glow) {
                String defaultStyle = "-fx-effect: dropshadow(three-pass-box, rgba(139, 0, 139, 0.8), 5, 0.8, 0, 0);";
                String hoverStyle = "-fx-effect: dropshadow(three-pass-box, rgba(139, 0, 139, 1), 10, 0.8, 0, 0);";
                button.setStyle(defaultStyle);
                button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
                button.setOnMouseExited(e -> button.setStyle(defaultStyle));
            }
            
            button.setLayoutX(buttonX);
            button.setLayoutY(buttonY);
            content.getChildren().add(button);
        }
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
        gifView.setLayoutX(coordinates.x);
        gifView.setLayoutY(coordinates.y);
        
        // Add the image
        content.getChildren().add(gifView);
        
        Coordinates dimensions = getDimensions(fileName);
        int nextRow = row + ((int) dimensions.y / this.fontHeight) + 1;
        
        return nextRow;
    }
    
    @Override
    public int displayGif(String viewName, String fileName, int row, int column) {
        System.out.println("JavaFXApplication: displayGif: viewName=" + viewName + ", fileName=" + fileName + ", row=" + row + ", column=" + column);
        
        Pane content = this.tabContentMap.get(viewName);
        
        ImageView imageView = new ImageView();
        Coordinates coordinates = this.convertToCoordinates(row, column);
        imageView.setLayoutX(coordinates.x);
        imageView.setLayoutY(coordinates.y);
        
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
        
        int x = (int) (column * this.fontWidth) - this.fontWidth;
        int y = (int) (row * this.fontHeight) - this.fontHeight;
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
    public void setBackgroundImage(String viewName, String imageFileName) {
        System.out.println("JavaFXApplication: setBackgroundImage: viewName=" + viewName + ", imageFileName=" + imageFileName);
        
        Pane content = this.tabContentMap.get(viewName);
        //HTMLEditor editor = this.tabEditorMap.get(viewName);
        
        Image image = loadImage(imageFileName);
        Coordinates dimensions = this.getDimensions(imageFileName);
        BackgroundImage backgroundImage = new BackgroundImage(
            image,
            BackgroundRepeat.NO_REPEAT, // Repeat in X direction
            BackgroundRepeat.NO_REPEAT, // Repeat in Y direction
            BackgroundPosition.DEFAULT,   // Position of the image
            // TODO - Other examples use 1.0
            new BackgroundSize(dimensions.x, dimensions.y, true, true, false, false) // Size of the image (100% width and height)
        );
        
        Background background = new Background(backgroundImage);
        
        content.setBackground(background);
        content.setPrefSize(dimensions.x, dimensions.y);
        /*
        if (editor != null) {
            editor.setBackground(background);
            editor.setPrefSize(dimensions.x, dimensions.y);
        }
        */
    }
    
    @Override
    public void addAnimation(String viewName, String name, int row, int column, String backgroundImageFileName, List<SpriteModel> sprites, double animationDelay, AnimationView listener) {
        System.out.println("JavaFXApplication: addAnimation: viewName=" + viewName + ", name=" + name + ", row=" + row + ", column=" + column + ", backgroundImageFileName=" + backgroundImageFileName + ", sprite count=" + sprites.size() + ", animationDelay=" + animationDelay + ", listener=" + listener);
                
        // Cache each sprite image
        Map<String, Image> spriteImages = new HashMap();
        for (SpriteModel sprite : sprites) {
            if (sprite.imageFile == null) {
                continue;
            }
            if (spriteImages.containsKey(sprite.imageFile)) {
                continue;
            }
            Image spriteImage = this.loadImage(sprite.imageFile);
            spriteImages.put(sprite.imageFile, spriteImage);
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
    
    public void animate(String viewName, Coordinates topLeft, PauseTransition pause, AnimationView listener, Coordinates animationDimensions, Pane animationBackground, ImageView backgroundImageView, Map<String, Image> spriteImages) {
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
        
        // Retrieve updated sprites
        List<SpriteModel> sprites = listener.onAnimate();
        
        // Clean up the animation 
        if (sprites == null) {
            System.out.println("JavaFXApplication: animate: No more sprite data, done");
            pause.stop();
            return;
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
        Map<SpriteModel, ImageView> spriteImageViewMap = new HashMap();
        for (SpriteModel sprite : sprites) {
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
        for (SpriteModel sprite : spriteImageViewMap.keySet()) {
            if (sprite.potentialCollisionNames != null) {
                ImageView imageView = spriteImageViewMap.get(sprite);
                //System.out.println("JavaFXApplication: animate: level 4 : " + sprite.name);
                for (String potentialCollisionName : sprite.potentialCollisionNames) {
                    //System.out.println("JavaFXApplication: animate: level 3 : " + potentialCollisionName);
                    for (SpriteModel potentialCollisionSprite : spriteImageViewMap.keySet()) {
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
                                // Glow the sprite if needed
                                sprite.onCollision(potentialCollisionSprite);
                                this.glowSprite(sprite, imageView, colorAdjust);
                                potentialCollisionSprite.onCollision(sprite);
                                this.glowSprite(potentialCollisionSprite, potentialCollisionImageView, colorAdjust);
                            }
                        }
                    }
                }
            }
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
    
    public void glowSprite(SpriteModel sprite, ImageView spriteView, Effect currentEffect) {
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

        /*
        URL resource = getClass().getResource(fileName);

        if (resource != null) {
            Media media = new Media(resource.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            if (this.mediaPlayers.containsKey(fileName)) {
                List<MediaPlayer> list = this.mediaPlayers.get(fileName);
                list.add(mediaPlayer);
                System.out.println("JavaFXApplication: playSound: Added new collection for file");
            } else {
                List<MediaPlayer> list = new ArrayList();
                list.add(mediaPlayer);
                mediaPlayers.put(fileName, list);
                System.out.println("JavaFXApplication: playSound: Added file to collection");
            }
            mediaPlayer.play();
        } else {
            System.err.println("JavaFXApplication: playSound: File not found!");
        }
    */
    }
    
    @Override
    public void stopSound(String fileName, Boolean removeAudioPlayer) {
        System.out.println("JavaFXApplication: stopSound: fileName=" + fileName + ", removeAudioPlayer=" + removeAudioPlayer);
        if (this.mediaPlayers.containsKey(fileName)) {
            List<MediaPlayer> list = this.mediaPlayers.get(fileName);
            for (MediaPlayer mediaPlayer : list) {
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.stop();
                    System.out.println("JavaFXApplication: stopSound: Stopped media");
                }
                if (removeAudioPlayer) {
                    HashMap<String, List<MediaPlayer>> allMediaPlayers = this.mediaPlayers;
                    mediaPlayer.statusProperty().addListener((ObservableValue<? extends MediaPlayer.Status> observable, MediaPlayer.Status oldValue, MediaPlayer.Status newValue) -> {
                        if (newValue == MediaPlayer.Status.STOPPED) {
                            System.out.println("JavaFXApplication: stopSound: Media has successfully stopped. Performing cleanup tasks now.");
                            list.remove(mediaPlayer);
                            if (list.isEmpty()) {
                                allMediaPlayers.remove(fileName);
                                mediaPlayer.dispose();
                                System.out.println("JavaFXApplication: stopSound: Removed media player");
                            }
                        }
                    });

                }
            }
        } else {
            System.out.println("JavaFXApplication: stopSound: Collection for file not found");
        }
    }
    
    @Override
    public void stopAllSounds() {
        System.out.println("JavaFXApplication: stopAllSvounds");
        Iterator<Map.Entry<String, List<MediaPlayer>>> iterator = this.mediaPlayers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<MediaPlayer>> entry = iterator.next();
            this.stopSound(entry.getKey(), false);
            iterator.remove();
        }
    }
    
}
