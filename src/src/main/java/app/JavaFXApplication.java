package app;

import app.model.BaseModel;
import app.javafx.DelegateApplication;
import app.model.Coordinates;
import app.model.SpriteModel;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;

/**
 *
 * @author repp
 */
public class JavaFXApplication extends ApplicationController {
    
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
    public HashMap<String, String> tabEditorTextMap;
    public TabPane tabFolder;
    public HashMap<String, Integer> tabIndexMap;
    public HashMap<String, Tab> tabItemMap;
    public HashMap<Tab, ApplicationView> tabItemViewMap;
    public int textColumns = 0;
    public int textRows = 0;
    public HashMap<String, ApplicationView> views;
    
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
        /*
        // Create Text objects with different styles
        Text text1 = new Text("Big italic red text. ");
        text1.setFill(Color.RED);
        text1.setFont(Font.font("Helvetica", FontPosture.ITALIC, 40));

        Text text2 = new Text("This is normal black text. ");
        text2.setFill(Color.BLACK);
        text2.setFont(Font.font("Helvetica", 20));

        Text text3 = new Text("And this is bold blue text.");
        text3.setFill(Color.BLUE);
        text3.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));

        // Create a TextFlow and add the Text objects
        TextFlow textFlow = new TextFlow(text1, text2, text3);
        textFlow.setLineSpacing(10);

        // Load the background image
        Image image = new Image(this.parentView.backgroundImage);
        /*
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setFitWidth(backgroundImage.getWidth());
        imageView.setFitHeight(backgroundImage.getHeight());
        //imageView.fitWidthProperty().bind(this.app.primaryStage.widthProperty()); // Bind to stage width
        //imageView.fitHeightProperty().bind(this.app.primaryStage.heightProperty()); // Bind to stage height
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
        */
        
        /*
        
        BackgroundImage backgroundImage = new BackgroundImage(
            image,
            BackgroundRepeat.NO_REPEAT, // Repeat in X direction
            BackgroundRepeat.NO_REPEAT, // Repeat in Y direction
            BackgroundPosition.CENTER,   // Position of the image
            new BackgroundSize(1.0, 1.0, true, true, false, false) // Size of the image (100% width and height)
        );
        Background background = new Background(backgroundImage);

        // Create a StackPane to layer the background image and text
        StackPane root = new StackPane(textFlow);
        root.setBackground(background);
        //root.set
        root.setStyle("-fx-background-color: black;");
        Scene scene = new Scene(root, 1280, 793);

        this.app.primaryStage.setTitle(this.parentView.name);
        this.app.primaryStage.setScene(scene);
        this.app.primaryStage.show();
        */
        
        showPrimaryStageFull();
    }
    
    public void showPrimaryStageFull() {
        System.out.println("JavaFXApplication: showPrimaryStage: view=" + this.parentView.name);
        
        // Set the application title
        this.app.primaryStage.setTitle(this.parentView.name);

        // Size the application dimensions        
        Coordinates dimensions = getDimensions(this.parentView.backgroundImage);
        //this.app.primaryStage.setWidth(dimensions.x);
        //this.app.primaryStage.setHeight(dimensions.y);
        
        // Set the application icon
        Image iconImage = null;
        if (this.parentView.iconFileName != null) {
            iconImage = loadImage(this.parentView.iconFileName);
            this.app.primaryStage.getIcons().add(iconImage);
        }
        
        // Initialize the application's tab folder and set it as the application's primary scene
        this.tabFolder = new TabPane();
        final JavaFXApplication thisApplication = this; // TODO - Can we use "this" inside of the listener?
        this.tabFolder.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            String selectedTabTitle = newTab.getText();
            System.out.println("JavaFXApplication: showPrimaryStage: Selected tab " + selectedTabTitle);
            //ApplicationView selectedView = thisApplication.tabItemViewMap.get(newTab);
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
        this.primaryScene = new Scene(this.tabFolder, dimensions.x, dimensions.y); // TODO - Do we need to set the dimensions here?
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
        this.tabEditorTextMap = new HashMap<>();

        this.tabIndexMap = new HashMap<>();
        this.tabItemMap = new HashMap<>();
        this.tabItemViewMap = new HashMap<>();
        this.views = new HashMap();
        this.namedControls = new HashMap();
            
        // Init a font for all text areas and buttons to use
        this.monospaceFont = Font.font("Consolas", FontWeight.NORMAL, 12);
        this.buttonFont = Font.font("Consolas", FontWeight.NORMAL, 10);
        
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
    
    public void addView(ApplicationView view, Boolean isParent) {
        Integer index = this.tabIndexMap.get(view.name);
        if (index == null) {
            index = this.tabIndexMap.size();
        }
        this.addView(view, isParent, index);
    }
    
    @Override
    public void displayView(ApplicationView view) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayView(String viewName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayOverlay(String viewName, String name, app.Color color, Integer startRow, Integer startColumn, Integer endRow, Integer endColumn, Integer transparency) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void clearScreen(String name) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void clearControl(String viewName, String controlName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void addDesigner(String viewName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void selectTab(String viewName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void renameTab(String viewName, String newViewName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void removeTab(String viewName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public Integer getTabIndex(String viewName) {
        throw new UnsupportedOperationException("Not supported.");
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
        String tabName = view.name;
        Tab tab = new Tab(tabName);
        if (view.emoji != null) {
            //tabName = view.emoji + " " + tabName;
            
            //tab.setGraphic(createEmoji(view.emoji)); // Add the emoji graphic
            
            //Text emojiNode = new Text(view.emoji);
            //emojiNode.setStyle(String.format("-fx-font: %dpx %s;", 14, "NotoColorEmoji"));
            //tab.setGraphic(new TextFlow(emojiNode));

            Image iconImage = loadImage("/assets/images/books.png");

            // 2. Create an ImageView and set the image
            ImageView iconView = new ImageView(iconImage);
            iconView.setFitWidth(16); // Set the size of the image
            iconView.setFitHeight(16);
            tab.setGraphic(iconView);
        }
        this.tabFolder.getTabs().add(index, tab);
        tab.setContent(content);
        this.tabItemMap.put(view.name, tab);
        this.tabItemViewMap.put(tab, view);
        
        // Configure the background
        Background background = null;
        if (view.backgroundImage != null) {
            Image image = loadImage(view.backgroundImage);
            Coordinates dimensions = this.getDimensions(view.backgroundImage);
            BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT, // Repeat in X direction
                BackgroundRepeat.NO_REPEAT, // Repeat in Y direction
                BackgroundPosition.DEFAULT,   // Position of the image
                // TODO - Other examples use 1.0
                new BackgroundSize(dimensions.x, dimensions.y, true, true, false, false) // Size of the image (100% width and height)
            );
            background = new Background(backgroundImage);
            content.setBackground(background);
            content.setPrefSize(dimensions.x, dimensions.y);
            // TODO - How to specify background color for images with transparency?
        } else if (view.backgroundColor != null) {
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
           
        if (view.addTextArea) {
            // Add a text area to the composite
            tabEditorTextMap.put(view.name, this.emptyBook);
            HTMLEditor htmlEditor = new HTMLEditor();            
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
                /*
                String editorControlStyles = 
                            "-fx-background-color: transparent;" +
                            ".html-editor .tool-bar { " +
                            "    -fx-max-height: 0; " +
                            "    -fx-pref-height: 0; " +
                            "    -fx-min-height: 0; " +
                            "    -fx-padding: 0; " +
                            "    -fx-border-width: 0; " +
                            "    -fx-opacity: 0; " +
                            "    visibility: hidden; " +
                            "}";
                htmlEditor.setStyle(editorControlStyles);
                */
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
                htmlEditor.setPrefWidth(backgroundImageDimensions.x);
                htmlEditor.setPrefHeight(backgroundImageDimensions.y);
                System.out.println("TEST: backgroundImageDimensions.x=" + backgroundImageDimensions.x + ", backgroundImageDimensions.y=" + backgroundImageDimensions.y);
            }
            htmlEditor.setHtmlText(initialHTML);
            
            //htmlEditor.lookup(".web-view").setStyle("-fx-background-color: transparent;");
            content.getChildren().add(htmlEditor);
            //htmlEditor.setPrefHeight(250);
        }
        
        // Add overlay pane AFTER HTMLEditor as StackPane displays its contents back-to-front
        Pane overlayPane = new Pane();
        content.getChildren().add(overlayPane);
        this.tabContentMap.put(view.name, overlayPane);
        
        view.onLoad(this);
    }
    
    @Override
    public void displayMessageBox(String title, String text, int level) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayText(String viewName, String text, Integer row, Integer column) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayText(String viewName, String text, Integer row, Integer column, app.Color color) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayText(String viewName, String text, Integer row, Integer column, app.Color color, int style) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayGrid(String viewName, Map<String, ArrayList<BaseModel>> linkTexts, int columns, Boolean showBorders, EventListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
      
    @Override
    public void displayLink(String viewName, String name, String linkText, int row, int column, int length, EventListener listener) {
        throw new UnsupportedOperationException("Not supported.");
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
            
            String defaultStyle = "-fx-effect: dropshadow(three-pass-box, rgba(139, 0, 139, 0.8), 10, 0.8, 0, 0);";
            String hoverStyle = "-fx-effect: dropshadow(three-pass-box, rgba(139, 0, 139, 1), 20, 0.8, 0, 0);";
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
        throw new UnsupportedOperationException("Not supported.");
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
        Font font;
        if (fxStyle != null) {
            // TODO - For some reason this doesn't use Italics
            font = Font.font(fontName, fxStyle, fontSize);
        } else {
            font = Font.font(fontName, fxWeight, fontSize);
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
        } else if (fontSize <= 12) {
            endCoordinates = this.convertToCoordinates(startRow + 1, startColumn + text.length());
        } else {
            endCoordinates = this.convertToCoordinates(startRow + 4, (int) (startColumn + (text.length() * (fontSize / 12) * 1.5) + 1));
        }
        
        label.setLayoutX(startCoordinates.x);
        label.setLayoutY(startCoordinates.y);
        label.setPrefSize(endCoordinates.x - startCoordinates.x - 1, endCoordinates.y - startCoordinates.y - 1);
        content.getChildren().add(label);
    }
    
    @Override
    public void displayInputField(String viewName, String name, String label, int length, int row, int column, String initValue, Boolean addButton, Boolean isMonospace, Boolean isUpperCase, Boolean isMultiUse, EventListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayValidatedInputField(String viewName, String name, List<String> values, int row, int startColumn, int endColumn, int alignment, EventListener listener, Boolean allowRepeatClicks) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public int displayGif(String viewName, String fileName, int row, int column) {
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
    
    public Coordinates convertToCoordinates(int row, int column) {
        System.out.println("JavaFXApplication: convertToCoordinates: row=" + row + ", column=" + column);
        
        int x = (int) (column * this.fontWidth) - this.fontWidth;
        int y = (int) (row * this.fontHeight) - this.fontHeight;
        Coordinates coordinates = new Coordinates(x, y);
        
        return coordinates;
    }
    
    @Override
    public void setTimer(String name, double seconds, EventListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void removeTimer(String name) {
        throw new UnsupportedOperationException("Not supported.");
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
    
    @Override
    public int getButtonColumns(String buttonText) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public int getButtonRows() {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void setBackgroundImage(String viewName, String imageFileName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void addAnimation(String viewName, String name, int row, int startColumn, String backgroundImageFileName, List<SpriteModel> sprites, double animationDelay, AnimationView listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public Boolean checkOverlap(int image1X, int image1Y, int image1Width, int image1Height, int image2X, int image2Y, int image2Width, int image2Height) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    public static Node createEmoji(String emoji) {
        // Load the custom emoji font before use
        // Note: The font is already loaded via CSS (@font-face)
        
        // TODO - Only load once
        //Font.loadFont(JavaFXApplication.class.getResourceAsStream("/assets/fonts/NotoColorEmoji.ttf"), 14);
        
        Text emojiNode = new Text(emoji);
        emojiNode.setStyle(String.format("-fx-font: %dpx %s;", 14, "NotoColorEmoji"));

        return new TextFlow(emojiNode);
    }
}
