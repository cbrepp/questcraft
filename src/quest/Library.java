package quest;

import app.ApplicationController;
import app.ApplicationView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.swt.SWT;

/**
 *
 * @author repp
 */
public class Library extends app.ApplicationView {
    
    public ApplicationController appController;
    public ApplicationView parentView;
    
    public Library() {
        super();
        this.backgroundColor = SWT.COLOR_WHITE;
    }
    
    @Override
    public void onDisplay(ApplicationController appController, ApplicationView parentView) {
        this.appController = appController;
        this.parentView = parentView;
        appController.displayText("SELECT STORY", 3, 12);
        int nextRow = appController.displayImage("/assets/images/old-books.jpg", 5, 12);
        appController.displayOpenFileButton("quest", "Choose Quest", nextRow, 12, this);
        
        int spiderColumns = appController.getColumns("/assets/images/spider.gif");
        int parentColumns = appController.getColumns(parentView.backgroundImage);
        int gifColumn = parentColumns - spiderColumns - 9;    // Puts the spider in the upper right-hand corner
        appController.displayGif("/assets/images/spider.gif", 1, gifColumn);
        
        serializeBook();
    }

    @Override
    public LinkedHashMap<String, ApplicationView> getChildren() {
        return null;
    }
    
    @Override
    public void handleEvent(String eventName, String eventValue) {
        System.out.println("Library: handleEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        switch(eventName) {
            case "quest" -> {
                // Deserialize the book
                BookFile bf = deserializeBook(eventValue);

                Book book = new Book();
                book.bookFile = bf;

                this.parentView.getChildren().put(Questcraft.BOOK, book);
                this.appController.displayView(new LoadingScreen(book, bf.animationFileName, bf.musicFileName));
            }
            default -> System.err.println("Library: handleEvent: Unsupported event");
        }
    }
    
    public BookFile deserializeBook(String fileName) {
        BookFile bf = null;
        FileInputStream file;
        try {
            file = new FileInputStream(fileName);
            ObjectInputStream in;
            try {
                in = new ObjectInputStream(file);
                try {
                    bf = (BookFile) in.readObject();
                    System.out.println("Read book! title=" + bf.title + " by " + bf.author);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return bf;
    }
    
    // TODO - Just a dumb helper method to get the book going
    public void serializeBook() {
        BookFile bf = new BookFile();
        bf.animationFileName = "/assets/images/dragon.gif";
        bf.author = "R. W. Chung";
        bf.musicFileName = "/assets/sounds/epic.mp3";
        bf.title = "BIG CHUNG, Destroyer of Worlds";
        bf.updateDate = LocalDate.now();
        bf.titlePage = new ArrayList<>();
        bf.titlePage.add("<color 139+0+0>");
        bf.titlePage.add("                              ___, ____--'");
        bf.titlePage.add("                         _,-.'_,-'      (");
        bf.titlePage.add("                      ,-' _.-''....____(");
        bf.titlePage.add("            ,))_     /  ,'\\ `'-.     (          /\\");
        bf.titlePage.add("    __ ,+..a`  \\(_   ) /   \\    `'-..(         /  \\");
        bf.titlePage.add("    )`-;...,_   \\(_ ) /     \\  ('''    ;'^^`\\ <./\\.>");
        bf.titlePage.add("        ,_   )   |( )/   ,./^``_..._  < /^^\\ \\_.))");
        bf.titlePage.add("       `=;; (    (/_')-- -'^^`      ^^-.`_.-` >-'");
        bf.titlePage.add("       `=\\ (                             _,./");
        bf.titlePage.add("         ,\\`(                         )^^^");
        bf.titlePage.add("           ``;         __-'^^\\       /");
        bf.titlePage.add("             / _>---^^^   `\\..`-.    ``'.");
        bf.titlePage.add("            / /               / /``'`; /");
        bf.titlePage.add("           / /          ,-=='-`=-'  / /");
        bf.titlePage.add("     ,-=='-`=-.               ,-=='-`=-.");
        bf.titlePage.add("<color 0+0+0>");
        bf.titlePage.add("");
        bf.titlePage.add("   *******************************************");
        bf.titlePage.add("");
        bf.titlePage.add("               T W I N   Q U E S T");
        String fileName = "/home/repp/Documents/quests/twin.quest";
        FileOutputStream file;
        try {
            file = new FileOutputStream(fileName);
            ObjectOutputStream out;
            try {
                out = new ObjectOutputStream(file);
                out.writeObject(bf);
            } catch (IOException ex) {
                Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
