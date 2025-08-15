package app;

import app.control.BaseControl;
import app.javafx.DelegateApplication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;

/**
 *
 * @author repp
 */
public class JavaFXApplication extends ApplicationController {
    
    public DelegateApplication app;
    //public Display display;
    public String emptyBook;
    public int fontHeight = 0;
    public int fontWidth = 0;
    //public Font monospaceFont;
    public ApplicationView parentView;
    public ApplicationView splashView;
    //public Shell shell;
    //public CTabFolder tabFolder;
    //public HashMap<String, Composite> tabCompositeMap;
    public HashMap<String, Integer> tabIndexMap;
    //public HashMap<String, List<StyleRange>> tabStyleRangesMap;
    //public HashMap<String, StyledText> tabStyledTextMap;
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
                e.printStackTrace();
            }
            // Close the splash screen and show the main application
            Platform.runLater(() -> {
                splashStage.close();
                showPrimaryStage();
            });
        }).start();
    }
    
    private void showPrimaryStage() {
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
        Image backgroundImage = new Image(this.parentView.backgroundImage);
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setFitWidth(backgroundImage.getWidth());
        imageView.setFitHeight(backgroundImage.getHeight());

        // Create a StackPane to layer the background image and text
        StackPane root = new StackPane(imageView, textFlow);
        root.setStyle("-fx-background-color: black;");
        Scene scene = new Scene(root, 1280, 793);

        this.app.primaryStage.setTitle(this.parentView.name);
        this.app.primaryStage.setScene(scene);
        this.app.primaryStage.show();
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
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void addView(ApplicationView view, Boolean isParent, int index) {
        throw new UnsupportedOperationException("Not supported.");
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
    public void displayGrid(String viewName, Map<String, ArrayList<BaseControl>> linkTexts, int columns, Boolean showBorders, EventListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
      
    @Override
    public void displayLink(String viewName, String name, String linkText, int row, int column, int length, EventListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayButton(String viewName, String name, String text, int row, int column, Boolean isMonospace, Boolean glow, EventListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayOpenFileButton(String viewName, String name, String text, int row, int column, Boolean isMonospace, Boolean glow, EventListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public int displayImage(String viewName, String fileName, int row, int column) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayFloatingText(String viewName, String text, int row, int column, int fontSize) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayInputField(String viewName, String name, String label, int length, int row, int column, Boolean isMonospace, Boolean isUpperCase, Boolean isMultiUse, EventListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayValidatedInputField(String viewName, String name, List<String> values, int row, int startColumn, int endColumn, int alignment, EventListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public int displayGif(String viewName, String fileName, int row, int column) {
        throw new UnsupportedOperationException("Not supported.");
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
    public int getTextColumns() {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public int getTextRows() {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public int getColumns(String fileName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public int getRows(String fileName) {
        throw new UnsupportedOperationException("Not supported.");
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
    
}
