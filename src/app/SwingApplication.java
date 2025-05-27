package app;

/**
 *
 * @author repp
 */
public class SwingApplication extends ApplicationController {
    
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
    public void addView(ApplicationView view) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayMessageBox(String text) {
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
    public void displayButton(String viewName, String name, String text, int row, int column, ApplicationView listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayOpenFileButton(String viewName, String name, String text, int row, int column, ApplicationView listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public int displayImage(String viewName, String fileName, int row, int column) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayInputField(String viewName, String name, String text, int length, int row, int column, ApplicationView listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public int displayGif(String viewName, String fileName, int row, int column) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void setTimer(String name, int seconds, ApplicationView listener) {
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
