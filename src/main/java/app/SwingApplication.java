package app;

import app.desktop.SoundController;
import app.model.BaseModel;
import app.model.Coordinates;
import app.model.SpriteModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author repp
 */
public class SwingApplication extends ApplicationController {
    
    public SoundController soundController = new SoundController();
    
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
    public void setDelegate(Object delegate) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void open(ApplicationView splashView, ApplicationView mainView) {
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
    public void displayOverlay(String viewName, String name, app.Color color, Integer startRow, Integer startColumn, Integer endRow, Integer endColumn, Integer transparency, Boolean invert) {
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
    public void displayText(String viewName, String text, Integer row, Integer column, Color color) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayText(String viewName, String text, Integer row, Integer column, Color color, int style) {
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
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayOpenFileButton(String viewName, String name, String text, Integer row, Integer column, Integer endRow, Integer endColumn, Boolean isMonospace, String fontName, Boolean glow, EventListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public int displayImage(String viewName, String name, String fileName, int row, int column, Boolean fillParent) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void updateFloatingText(String viewName, String name, String text) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayFloatingText(String viewName, String name, String text, Integer startRow, Integer startColumn, Integer endRow, Integer endColumn, app.Color fontColor, Integer fontSize, Integer fontStyle, String fontName) {
        throw new UnsupportedOperationException("Not supported.");
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
    public Coordinates getDimensions(String imageFileName) {
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
    
    @Override
    public void addAnimation(String viewName, String name, int row, int startColumn, String backgroundImageFileName, List<String> imageFiles, double animationDelay, AnimationView listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void playSound(String fileName, Boolean isLoop) {
        System.out.println("SWTApplication: playSound: fileName=" + fileName + ", isLoop=" + isLoop);
        this.soundController.playSound(fileName, isLoop);
    }
    
    @Override
    public void stopSound(String fileName, Boolean removeAudioPlayer) {
        System.out.println("SWTApplication: stopSound: fileName=" + fileName + ", removeAudioPlayer=" + removeAudioPlayer);
        this.soundController.stopSound(fileName, removeAudioPlayer);
    }
    
    @Override
    public void stopAllSounds() {
        System.out.println("SWTApplication: stopAllSounds");
        this.soundController.stopAllSounds();
    }
    
    @Override
    public void pauseAllSounds() {
        System.out.println("SWTApplication: pauseAllSounds");
        this.soundController.pauseAllSounds();
    }

    @Override
    public void unpauseAllSounds() {
        System.out.println("SWTApplication: unpauseAllSounds");
        this.soundController.unpauseAllSounds();
    }
    
    @Override
    public void sendToFront(String viewName, String name) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void sendToBack(String viewName, String name) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
