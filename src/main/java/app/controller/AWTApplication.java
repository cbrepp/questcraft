package app.controller;

import app.view.BaseView;
import app.Coordinates;
import app.EventListener;
import app.Layout;
import app.TextDecoration;
import app.controller.desktop.SoundController;
import app.dialog.BaseDialog;
import app.node.BaseNode;
import java.util.List;
import app.view.Animation;
import app.view.BaseSplashView;

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
        Bootstrap.main(args);
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
    public void open(BaseSplashView splashView, BaseView mainView) {
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
    public void clearScreen(String name) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void removeNode(String viewName, String nodeName) {
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
    public void addView(BaseView view, int index, Boolean isRefresh) {
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
    public void refreshTabLabel(String viewName) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void removeTab(String viewName) {
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
    
    public Coordinates getDimensions(String imageFileName) {
        // TODO
        return null;
    }
    
    @Override
    public void changeNode(String viewName, BaseNode node, Layout layout) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void addNode(String viewName, String parentName, BaseNode node, Layout layout) {
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
    
    @Override
    public void setDefaultTextDecoration(String viewName, TextDecoration textDecoration) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public TextDecoration getDefaultTextDecoration(String viewName) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void addAnimation(String viewName, String name, int row, int startColumn, String backgroundImageFileName, List<String> imageFiles, double animationDelay, Animation listener) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
}
