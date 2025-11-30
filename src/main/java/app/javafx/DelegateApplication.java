package app.javafx;

import app.ApplicationController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author repp
 */
public class DelegateApplication extends Application {
    
    public Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) {
        System.out.println("DelegateApplication: start");
        
        this.primaryStage = primaryStage;
        ApplicationController.appController.setDelegate(this);
    }

    public static void main(String[] args) {
        System.out.println("DelegateApplication: main");
        launch(args); // Launch the JavaFX application
    }
    
}
