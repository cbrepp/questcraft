package quest;

import app.ApplicationController;
import app.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author repp
 */
public class Library extends app.ApplicationView {
    
    public ApplicationController appController;
    
    public Library(String name) {
        super(name);
        this.backgroundColor = new Color(255, 255, 255);
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("Library: onEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        switch (eventName) {
            case "quest" -> {
                String fileName = (String) eventValue;
                BookFile bookFile = deserializeBook(fileName);
                this.publishEvent("book", bookFile);
            }
            default ->
                System.err.println("Library: onEvent: Unsupported event");
        }
    }
    
    @Override
    public void onLoad(ApplicationController appController) {
        this.appController = appController;
        appController.displayText(this.name, "SELECT STORY", 3, 5);
        int nextRow = appController.displayImage(this.name, "/assets/images/old-books.jpg", 5, 5);
        appController.displayOpenFileButton(this.name, "quest", "Choose Quest", nextRow, 5, this);
        
        int spiderColumns = appController.getColumns("/assets/images/spider.gif");
        int parentColumns = appController.getTextColumns();
        int gifColumn = parentColumns - spiderColumns + 2;    // Puts the spider in the upper right-hand corner
        appController.displayGif(this.name, "/assets/images/spider.gif", 1, gifColumn);
        
        serializeBook();
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
        bf.title = "BIG CHUNG, Destroyer of Worlds";
        bf.updateDate = LocalDate.now();
        Chapter titlePage = new Chapter();
        titlePage.soundFileName = "/assets/sounds/epic.mp3";
        List<String> page1 = new ArrayList<>();
        page1.add("<color 139+0+0>");
        page1.add("                             ___, ____--'");
        page1.add("                        _,-.'_,-'      (");
        page1.add("                     ,-' _.-''....____(");
        page1.add("           ,))_     /  ,'\\ `'-.     (          /\\");
        page1.add("   __ ,+..a`  \\(_   ) /   \\    `'-..(         /  \\");
        page1.add("   )`-;...,_   \\(_ ) /     \\  ('''    ;'^^`\\ <./\\.>");
        page1.add("       ,_   )   |( )/   ,./^``_..._  < /^^\\ \\_.))");
        page1.add("      `=;; (    (/_')-- -'^^`      ^^-.`_.-` >-'");
        page1.add("      `=\\ (                             _,./");
        page1.add("        ,\\`(                         )^^^");
        page1.add("          ``;         __-'^^\\       /");
        page1.add("            / _>---^^^   `\\..`-.    ``'.");
        page1.add("           / /               / /``'`; /");
        page1.add("          / /          ,-=='-`=-'  / /");
        page1.add("    ,-=='-`=-.               ,-=='-`=-.");
        page1.add("<color 0+0+0>");
        page1.add("");
        page1.add("  *******************************************");
        page1.add("");
        page1.add("              T W I N   Q U E S T");
        titlePage.pages.put(0, page1);
        bf.chapters.put(Book.TITLE_PAGE, titlePage);
        Chapter introduction = new Chapter();
        introduction.soundFileName = "/assets/sounds/suspense.mp3";
        page1 = new ArrayList<>();
        page1.add("<twin>: <quote>Ahh!!!  <player>!!!  Save me!!!<quote>");
        page1.add("<br>");
        page1.add("<player>: <quote>I don't know if I can, but I will try!!!<quote>");
        page1.add("<br>");
        page1.add("<twin>: <quote><player> he got me!!!  A big black cat got me!!!<quote>");
        page1.add("<br>");
        page1.add("You set down the game controller you were holding (and what a shame, you were about to beat the Ender Dragon) and run toward the sound of your twin's voice just in time to see the door to the leprechaun closet in the back bedroom slam shut.  You run to the closet door, open it, and what you see next takes your breath away...");
        page1.add("<br>");
        page1.add("Sticking your head into what was supposed to be a small closet you look around at what appears to be the surface of a cloud floating high above Pendleton, Indiana.  You wonder if this is perhaps a magic portal.");
        page1.add("<br>");
        page1.add("You cautiously tap the cloud with your hand and discover that it's firm enough to walk on.  You then oh so very carefully put one foot into the closet, lower your head, and step through the doorway so you can get a better look...");
        page1.add("<second-page>");
        page1.add("<image clouds.png>");
        introduction.pages.put(0, page1);
        bf.chapters.put(Book.INTRODUCTION, introduction);
        bf.highScores = new ArrayList<>();
        bf.highScores.add(new HighScore(300, "GRT", LocalDate.of(2024, Month.MARCH, 9)));
        bf.highScores.add(new HighScore(300, "ZRT", LocalDate.of(2024, Month.MARCH, 9)));
        bf.highScores.add(new HighScore(300, "GRT", LocalDate.of(2024, Month.MARCH, 23)));
        bf.highScores.add(new HighScore(300, "ZRT", LocalDate.of(2024, Month.MARCH, 23)));
        bf.highScores.add(new HighScore(300, "ZRT", LocalDate.of(2024, Month.MARCH, 23)));
        bf.highScores.add(new HighScore(200, "ZRT", LocalDate.of(2024, Month.MARCH, 23)));
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
