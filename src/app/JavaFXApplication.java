package app;

import app.control.BaseControl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author repp
 */
public class JavaFXApplication extends ApplicationController {
    
    /**
     * The implementation of this method is a work-around to inheritance not being fully implemented in java
     * for static methods.  While child classes can inherit a static method from a parent class, there is no
     * way to know within the inherited method for which class it is being executed.  Also, there is no good
     * way to know within any static method what the name of the current class is without using a Throwable.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            args[0] = new Throwable().getStackTrace()[0].getClassName();
        }
        ApplicationController.main(args);
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void displayApplication(ApplicationView view) {
        throw new UnsupportedOperationException("Not supported.");
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
    public void initialize(ApplicationView view) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void clearScreen(String name) {
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
    public void displayText(String viewName, String text, Integer row, Integer column, Color color) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayText(String viewName, String text, Integer row, Integer column, Color color, int style) {
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
    public void displayButton(String viewName, String name, String text, int row, int column, EventListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayOpenFileButton(String viewName, String name, String text, int row, int column, EventListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public int displayImage(String viewName, String fileName, int row, int column) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayInputField(String viewName, String name, String label, int length, int row, int column, EventListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayValidatedInputField(String viewName, String name, List<String> values, int row, int startColumn, int endColumn, EventListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public int displayGif(String viewName, String fileName, int row, int column) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void setTimer(String name, int seconds, EventListener listener) {
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
