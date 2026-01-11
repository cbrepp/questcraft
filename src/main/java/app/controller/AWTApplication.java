package app.controller;

import app.view.BaseView;
import app.Color;
import app.Coordinates;
import app.EventListener;
import app.Icon;
import app.Layout;
import app.controller.desktop.SoundController;
import app.dialog.BaseDialog;
import app.node.BaseNode;
import app.node.Grid;
import java.util.List;
import app.view.Animation;

/**
 *
 * @author repp
 */
public class AWTApplication extends BaseController {
    
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
        BaseController.main(args);
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
    public void open(BaseView splashView, BaseView mainView) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayView(BaseView view) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayView(String viewName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void newDialog(BaseDialog dialog) {
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
    public Integer getTabIndex(String viewName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void addView(BaseView view) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void addView(BaseView view, Boolean isParent, int index, Boolean isRefresh) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void addDesigner(String viewName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void loadEmojiData() {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void selectTab(String viewName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void refreshTabLabel(String viewName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void removeTab(String viewName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayGrid(String viewName, Grid control) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void displayValidatedInputField(String viewName, String name, List<String> values, int row, int startColumn, int endColumn, Layout layout, EventListener listener, Boolean allowRepeatClicks) {
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
    public void addNode(String viewName, BaseNode node, String parentName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void addAnimation(String viewName, String name, int row, int startColumn, String backgroundImageFileName, List<String> imageFiles, double animationDelay, Animation listener) {
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
    
    @Override
    public void refreshView(String viewName) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
