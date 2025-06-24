package quest.view;

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
import quest.model.Act;
import quest.model.Book;
import quest.model.HighScore;
import quest.model.InventoryItem;
import quest.model.Page;
import quest.model.Scene;
import quest.model.Story;

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
                Book bookFile = deserializeBook(fileName);
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
    
    public Book deserializeBook(String fileName) {
        Book bf = null;
        FileInputStream file;
        try {
            file = new FileInputStream(fileName);
            ObjectInputStream in;
            try {
                in = new ObjectInputStream(file);
                try {
                    bf = (Book) in.readObject();
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
    
    // TODO - Just a temporary helper method to get the book going.  Eventually the Craft Table should be used for this purpose.
    public void serializeBook() {
        Book book = new Book();
        book.animationFileName = "/assets/images/dragon.gif";
        book.author = "R. W. Chung";
        book.firstActName = "Opening";
        book.title = "BIG CHUNG, Destroyer of Worlds";
        book.updateDate = LocalDate.now();
        
        // Inventory items are added in alphabetical order
        InventoryItem item = new InventoryItem("A small catapult capable of launching an endless supply of cats into the air.", "/assets/sounds/catapult.wav", "\uD83D\uDC08");
        book.inventory.put("Cat-apult", item);
        item = new InventoryItem("Shiny gold!", "/assets/sounds/gold.wav", "\uD83D\uDCB0");
        book.inventory.put("Gold", item);
        item = new InventoryItem("An impressive mid-range weapon.  Great for slaying dragons!", "/assets/sounds/arrow.mp3", "\uD83C\uDFF9");
        book.inventory.put("Greyson's Great Bow", item);
        item = new InventoryItem("A magical fold-out piece of paper showing each of the locations in the current level.", "/assets/sounds/paper.wav", "\uD83D\uDDFA");
        Story onAdd = new Story();
        onAdd.contents.add("<add-view Map>");
        item.onAdd = onAdd;
        Story onSelect = new Story();
        onSelect.contents.add("<tab-select Map>");
        item.onSelect = onSelect;
        book.inventory.put("Map", item);
        item = new InventoryItem("A magical ring forged by woodland gnomes for taming creatures.  But beware!  It does not work on large creatures.", "/assets/sounds/ring-of-taming.wav", "\uD83D\uDC8D");
        book.inventory.put("Ring of Taming", item);
        item = new InventoryItem("The fabeled UNO reverse card.  Used to return damage back into the face of he who dealt it.", "/assets/sounds/reverse.wav", "\uD83C\uDFB4");
        book.inventory.put("UNO Reverse", item);
        item = new InventoryItem("A magnificent blade.  Great for slaying dragons!", "/assets/sounds/sword.wav", "\uD83D\uDDE1");
        book.inventory.put("Zara's Sword", item);

        Story flipBookSubpage = new Story();
        flipBookSubpage.isCheat = true;
        flipBookSubpage.contents.add("<flip-book>");
        book.subpages.put("FLIP BOOK", flipBookSubpage);
        
        Act opening = new Act();
        opening.firstSceneName = "Title Page";
        book.acts.put("Opening", opening);
        Scene titlePage = new Scene();
        titlePage.firstPageName = "1";
        titlePage.hidePageHeaders = true;
        titlePage.nextSceneName = "Player Selection";
        titlePage.soundFileName = "/assets/sounds/epic.mp3";
        opening.scenes.put("Title Page", titlePage);
        Page page1 = new Page();
        page1.story.contents.add("<color 139+0+0>");
        page1.story.contents.add("                             ___, ____--'");
        page1.story.contents.add("                        _,-.'_,-'      (");
        page1.story.contents.add("                     ,-' _.-''....____(");
        page1.story.contents.add("           ,))_     /  ,'\\ `'-.     (          /\\");
        page1.story.contents.add("   __ ,+..a`  \\(_   ) /   \\    `'-..(         /  \\");
        page1.story.contents.add("   )`-;...,_   \\(_ ) /     \\  ('''    ;'^^`\\ <./\\.>");
        page1.story.contents.add("       ,_   )   |( )/   ,./^``_..._  < /^^\\ \\_.))");
        page1.story.contents.add("      `=;; (    (/_')-- -'^^`      ^^-.`_.-` >-'");
        page1.story.contents.add("      `=\\ (                             _,./");
        page1.story.contents.add("        ,\\`(                         )^^^");
        page1.story.contents.add("          ``;         __-'^^\\       /");
        page1.story.contents.add("            / _>---^^^   `\\..`-.    ``'.");
        page1.story.contents.add("           / /               / /``'`; /");
        page1.story.contents.add("          / /          ,-=='-`=-'  / /");
        page1.story.contents.add("    ,-=='-`=-.               ,-=='-`=-.");
        page1.story.contents.add("<color 0+0+0>");
        page1.story.contents.add("");
        page1.story.contents.add("  *******************************************");
        page1.story.contents.add("");
        page1.story.contents.add("              T W I N   Q U E S T");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<b><book-title></b>");
        page1.story.contents.add("by <book-author>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("Last Updated: <book-last-updated-date>");
        titlePage.pages.put("1", page1);
        
        Scene playerSelection = new Scene();
        playerSelection.firstPageName = "1";
        playerSelection.hidePageHeaders = true;
        playerSelection.soundFileName = "/assets/sounds/epic.mp3";
        opening.scenes.put("Player Selection", playerSelection);
        page1 = new Page();
        page1.story.contents.add("Select Player:");
        page1.story.contents.add("<subpage-display summonShmebulock=true SHMEBULOCK input>");
        page1.story.contents.add("<subpage-display summonShmebulock= input>");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<image center /assets/images/twins.jpg>");
        Story playerGreysonSubpage = new Story();
        playerGreysonSubpage.contents.add("<set-player-symbol \uD83E\uDD77>");
        playerGreysonSubpage.contents.add("<variable-set twin Zara>");
        playerGreysonSubpage.contents.add("<variable-set twin-me me>");
        playerGreysonSubpage.contents.add("<variable-set battle-cry I don't know if I can, but I will try!!!>");
        playerGreysonSubpage.contents.add("<variable-set twin-voice twin's voice>");
        playerGreysonSubpage.contents.add("<variable-set player-mylee-nickname kid>");
        playerGreysonSubpage.contents.add("<variable-set mylee-fandom And I definitely wouldn't want to if I could.  You humans are just too weird.>");
        playerGreysonSubpage.contents.add("<variable-set mylee-reaction Weird>");
        playerGreysonSubpage.contents.add("<variable-set twin-was twin was>");
        playerGreysonSubpage.contents.add("<variable-set why-is-that Why is that?>");
        playerGreysonSubpage.contents.add("<variable-set eat-twin He's going to eat ZARA???>");
        playerGreysonSubpage.contents.add("<variable-set thats-horrible That's horrible!!!>");
        playerGreysonSubpage.contents.add("<goto-scene Difficulty Selection>");
        page1.subpages.put("INPUT player=Greyson", playerGreysonSubpage);
        Story playerZaraSubpage = new Story();
        playerZaraSubpage.contents.add("<set-player-symbol \uD83E\uDD77>");
        playerZaraSubpage.contents.add("<variable-set twin Greyson>");
        playerZaraSubpage.contents.add("<variable-set twin-me me>");
        playerZaraSubpage.contents.add("<variable-set battle-cry I don't know if I can, but I will try!!!>");
        playerZaraSubpage.contents.add("<variable-set twin-voice twin's voice>");
        playerZaraSubpage.contents.add("<variable-set player-mylee-nickname kid>");
        playerZaraSubpage.contents.add("<variable-set mylee-fandom And I definitely wouldn't want to if I could.  You humans are just too weird.>");
        playerZaraSubpage.contents.add("<variable-set mylee-reaction Weird>");
        playerZaraSubpage.contents.add("<variable-set twin-was twin was>");
        playerZaraSubpage.contents.add("<variable-set why-is-that Why is that?>");
        playerZaraSubpage.contents.add("<variable-set eat-twin He's going to eat GREYSON???>");
        playerZaraSubpage.contents.add("<variable-set thats-horrible That's horrible!!!>");
        playerZaraSubpage.contents.add("<goto-scene Difficulty Selection>");
        page1.subpages.put("INPUT player=Zara", playerZaraSubpage);
        Story playerShmebulockSubpage = new Story();
        playerShmebulockSubpage.contents.add("<set-player-symbol \uD83C\uDF85>");
        playerShmebulockSubpage.contents.add("<variable-set twin Greyson and Zara>");
        playerShmebulockSubpage.contents.add("<variable-set twin-me us>");
        playerShmebulockSubpage.contents.add("<variable-set battle-cry SHMEBULOCK!!!>");
        playerShmebulockSubpage.contents.add("<variable-set twin-voice friends' voices>");
        playerShmebulockSubpage.contents.add("<variable-set player-mylee-nickname magical one>");
        playerShmebulockSubpage.contents.add("<variable-set mylee-fandom But I wish I could.  You gnomes are just fascinating!>");
        playerShmebulockSubpage.contents.add("<variable-set mylee-reaction Fascinating>");
        playerShmebulockSubpage.contents.add("<variable-set twin-was friends were>");
        playerShmebulockSubpage.contents.add("<set-magic-text true>");
        playerShmebulockSubpage.contents.add("<variable-set why-is-that SHMEBULOCK?>");
        playerShmebulockSubpage.contents.add("<variable-set eat-twin SHMEBULOCK???>");
        playerShmebulockSubpage.contents.add("<variable-set thats-horrible SHMEBULOCK!!!>");
        playerShmebulockSubpage.contents.add("<inventory-add Ring of Taming>");
        playerShmebulockSubpage.contents.add("<inventory-add Zara's Sword>");
        playerShmebulockSubpage.contents.add("<inventory-add Greyson's Great Bow>");
        
        playerShmebulockSubpage.contents.add("<goto-scene Difficulty Selection>");
        page1.subpages.put("INPUT player=Shmebulock", playerShmebulockSubpage);
        Story shmebulockCheatSubpage = new Story();
        shmebulockCheatSubpage.isCheat = true;
        shmebulockCheatSubpage.contents.add("<variable-set summonShmebulock true>");
        shmebulockCheatSubpage.contents.add("<page-refresh>");
        page1.subpages.put("SHMEBULOCK", shmebulockCheatSubpage);
        Story inputSubpage = new Story();
        inputSubpage.contents.add("<get-input player Greyson+Zara>");
        page1.subpages.put("input", inputSubpage);
        Story inputWithShmebulockSubpage = new Story();
        inputWithShmebulockSubpage.contents.add("<get-input player Greyson+Zara+Shmebulock>");
        page1.subpages.put("SHMEBULOCK input", inputWithShmebulockSubpage);
        playerSelection.pages.put("1", page1);
        
        Scene difficultySelection = new Scene();
        difficultySelection.firstPageName = "1";
        difficultySelection.hidePageHeaders = true;
        difficultySelection.soundFileName = "/assets/sounds/epic.mp3";
        opening.scenes.put("Difficulty Selection", difficultySelection);
        page1 = new Page();
        page1.story.contents.add("Select Difficulty:");
        page1.story.contents.add("<subpage-display player=Shmebulock SHMEBULOCK input>");
        page1.story.contents.add("<subpage-display player=Greyson input>");
        page1.story.contents.add("<subpage-display player=Zara input>");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<image center /assets/images/path.jpg>");
        Story difficultyEasySubpage = new Story();
        difficultyEasySubpage.contents.add("<goto-act Introduction>");
        page1.subpages.put("INPUT difficulty=Easy", difficultyEasySubpage);
        Story difficultyNormalSubpage = new Story();
        difficultyNormalSubpage.contents.add("<goto-act Introduction>");
        page1.subpages.put("INPUT difficulty=Normal", difficultyNormalSubpage);
        Story difficultyHardSubpage = new Story();
        difficultyHardSubpage.contents.add("<goto-act Introduction>");
        page1.subpages.put("INPUT difficulty=Hard", difficultyHardSubpage);
        Story difficultyMagicalSubpage = new Story();
        difficultyMagicalSubpage.contents.add("<goto-act Introduction>");
        page1.subpages.put("INPUT difficulty=Magical", difficultyMagicalSubpage);
        inputSubpage = new Story();
        inputSubpage.contents.add("<get-input difficulty Easy+Normal+Hard>");
        page1.subpages.put("input", inputSubpage);
        inputWithShmebulockSubpage = new Story();
        inputWithShmebulockSubpage.contents.add("<get-input difficulty Magical>");
        page1.subpages.put("SHMEBULOCK input", inputWithShmebulockSubpage);
        difficultySelection.pages.put("1", page1);
        
        Act introduction = new Act();
        introduction.firstSceneName = "Introduction";
        introduction.nextActName = "Chapter 1";
        book.acts.put("Introduction", introduction);
        
        Scene introScene = new Scene();
        introScene.firstPageName = "1";
        introScene.hidePageHeaders = false;
        introScene.soundFileName = "/assets/sounds/suspense.mp3";
        introduction.scenes.put("Introduction", introScene);
        
        page1 = new Page();
        page1.nextPageName = "2";
        page1.story.contents.add("<variable twin>: <quote>Ahh!!!  <variable player>!!!  Save <variable twin-me>!!!<quote>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("<variable player>: <quote><variable battle-cry><quote>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("<variable twin>: <quote><variable player> he got <variable twin-me>!!!  A big black cat got <variable twin-me>!!!<quote>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("You set down the game controller you were holding (and what a shame, you were about to beat the Ender Dragon) and run toward the sound of your <variable twin-voice> just in time to see the door to the leprechaun closet in the back bedroom slam shut.  You run to the closet door, open it, and what you see next takes your breath away...");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<image center /assets/images/mystery-door.jpg>");
        introScene.pages.put("1", page1);
        
        Page page2 = new Page();
        page2.previousPageName = "1";
        page2.nextPageName = "3";
        page2.story.contents.add("Sticking your head into what was supposed to be a small closet you look around at what appears to be the surface of a cloud floating high above Pendleton, Indiana.  You wonder if this is perhaps a magic portal.");
        page2.story.contents.add("<br>");
        page2.story.contents.add("You cautiously tap the cloud with your hand and discover that it's firm enough to walk on.  You then oh so very carefully put one foot into the closet, lower your head, and step through the doorway so you can get a better look...");
        page2.story.contents.add("<second-page>");
        page2.story.contents.add("<image center /assets/images/clouds.png>");
        introScene.pages.put("2", page2);
        
        Page page3 = new Page();
        page3.previousPageName = "2";
        page3.nextPageName = "4";
        page3.story.contents.add("You are now standing on the surface of a cloud floating high up in the sky.  You look around and see nothing but the large poofy white cloud, the blue sky, the bright yellow sun... and... what appears to be elevator doors in the middle of the cloud.");
        page3.story.contents.add("<br>");
        page3.story.contents.add("Anxiety and fear grip you.  But you have no choice.  You must save <variable twin>.  And so...");
        page3.story.contents.add("<br>");
        page3.story.contents.add("You press the elevator button to open the door.  And... you step inside...");
        page3.story.contents.add("<second-page>");
        page3.story.contents.add("<image center /assets/images/elevator-doors.png+/assets/images/clouds.png>");
        introScene.pages.put("3", page3);
        
        Page page4 = new Page();
        page4.previousPageName = "3";
        page4.nextPageName = "5";
        page4.hidePreviousButton = true;
        page4.story.contents.add("<stop-sound>");
        page4.story.contents.add("<play-sound /assets/sounds/elevator-open.mp3>");
        page4.story.contents.add("<play-sound /assets/sounds/elevator.wav>");
        page4.story.contents.add("MYLEE: <quote>Oh hey, <variable player-mylee-nickname>.<quote>");
        page4.story.contents.add("<br>");
        page4.story.contents.add("You're confused for a moment.  Inside the elevator there's a cat perched up on a shelf by the main elevator switch.  Is a cat operating this elevator?  And... did that cat just talk???");
        page4.story.contents.add("<br>");
        page4.story.contents.add("MYLEE: <quote>You're wondering if I just talked even though I'm a cat.  And the answer is 'yes'.  Yes I did.<quote>");
        page4.story.contents.add("<br>");
        page4.story.contents.add("Wait, what?  Can this talking cat read your mind?");
        page4.story.contents.add("<br>");
        page4.story.contents.add("MYLEE: <quote>And you're probably wondering now if I can read your mind.  The answer to that is... no.  <variable mylee-fandom><quote>");
        page4.story.contents.add("<br>");
        page4.story.contents.add("<variable mylee-reaction> is right.");
        page4.story.contents.add("<br>");
        page4.story.contents.add("MYLEE: <quote>Enough talk, <variable player-mylee-nickname>.  You look confused.  How about you ask me some questions.<quote>");
        page4.story.contents.add("<second-page>");
        page4.story.contents.add("<image center /assets/images/mylee.jpg>");
        introScene.pages.put("4", page4);
        
        Page page5 = new Page();
        page5.previousPageName = "4";
        page5.hideNextButton = true;
        page5.story.contents.add("MYLEE: <quote>You look like you're new to this so I'll help you out.  See those buttons below?  They're giving you some choices.  Pick one.  You can learn about this game before you begin your quest.<quote>");
        page5.story.contents.add("<br>");
        page5.story.contents.add("<subpage-display player=Shmebulock SHMEBULOCK input>");
        page5.story.contents.add("<subpage-display player=Greyson input>");
        page5.story.contents.add("<subpage-display player=Zara input>");
        page5.story.contents.add("<second-page>");
        page5.story.contents.add("<image center /assets/images/mylee.jpg>");
        introScene.pages.put("5", page5);
        inputSubpage = new Story();
        inputSubpage.contents.add("<get-input mylee-prompt Who are you?+Where is my twin?+Who is Big Chung?+I'm good.>");
        page5.subpages.put("input", inputSubpage);
        inputWithShmebulockSubpage = new Story();
        inputWithShmebulockSubpage.contents.add("<get-input mylee-prompt SHMEBULOCK?+SHMEBULOCK??+SHMEBULOCK???+SHMEBULOCK.>");
        page5.subpages.put("SHMEBULOCK input", inputWithShmebulockSubpage);
        Story whoAreYouSubpage = new Story();
        whoAreYouSubpage.contents.add("<goto-page 6>");
        page5.subpages.put("INPUT mylee-prompt=Who are you?", whoAreYouSubpage);
        Story whereIsTwinSubpage = new Story();
        whereIsTwinSubpage.contents.add("<goto-page 7>");
        page5.subpages.put("INPUT mylee-prompt=Where is my twin?", whereIsTwinSubpage);
        Story whoIsChungSubpage = new Story();
        whoIsChungSubpage.contents.add("<goto-page 8>");
        page5.subpages.put("INPUT mylee-prompt=Who is Big Chung?", whoIsChungSubpage);
        Story imGoodSubpage = new Story();
        imGoodSubpage.contents.add("<goto-page 9>");
        page5.subpages.put("INPUT mylee-prompt=I'm good.", imGoodSubpage);
        Story schmebulock1Subpage = new Story();
        schmebulock1Subpage.contents.add("<goto-page 6>");
        page5.subpages.put("INPUT mylee-prompt=SHMEBULOCK?", schmebulock1Subpage);
        Story schmebulock2Subpage = new Story();
        schmebulock2Subpage.contents.add("<goto-page 7>");
        page5.subpages.put("INPUT mylee-prompt=SHMEBULOCK??", schmebulock2Subpage);
        Story schmebulock3Subpage = new Story();
        schmebulock3Subpage.contents.add("<goto-page 8>");
        page5.subpages.put("INPUT mylee-prompt=SHMEBULOCK???", schmebulock3Subpage);
        Story schmebulock4Subpage = new Story();
        schmebulock4Subpage.contents.add("<goto-page 9>");
        page5.subpages.put("INPUT mylee-prompt=SHMEBULOCK.", schmebulock4Subpage);
        
        Page page6 = new Page();
        page6.previousPageName = "5";
        page6.hideNextButton = true;
        page6.story.contents.add("YOU: <quote><variable mylee-prompt><quote>");
        page6.story.contents.add("<br>");
        page6.story.contents.add("MYLEE: <quote>I'm Mylee Marie!<quote>");
        page6.story.contents.add("<br>");
        page6.story.contents.add("...");
        page6.story.contents.add("<br>");
        page6.story.contents.add("You wait a moment for MYLEE to say more but she doesn't.  Okay... at least you got her name.  Perhaps it's time to ask another question?");
        page6.story.contents.add("<second-page>");
        page6.story.contents.add("<image center /assets/images/mylee.jpg>");
        introScene.pages.put("6", page6);
        
        Page page7 = new Page();
        page7.previousPageName = "5";
        page7.hideNextButton = true;
        page7.story.contents.add("YOU: <quote><variable mylee-prompt><quote>");
        page7.story.contents.add("<br>");
        page7.story.contents.add("MYLEE: <quote>Oh you poor <variable player-mylee-nickname>.  I'm afraid your <variable twin-was> captured by my younger brother, Big Chung.  It is a shame.<quote>");
        page7.story.contents.add("<br>");
        page7.story.contents.add("YOU: <quote><variable why-is-that><quote>");
        page7.story.contents.add("<br>");
        page7.story.contents.add("MYLEE: <quote>You'd have to understand my brother to know why that is...<quote>");
        page7.story.contents.add("<second-page>");
        page7.story.contents.add("<image center /assets/images/mylee.jpg>");
        introScene.pages.put("7", page7);
        
        Page page8 = new Page();
        page8.previousPageName = "5";
        page8.hideNextButton = true;
        page8.story.contents.add("YOU: <quote><variable mylee-prompt><quote>");
        page8.story.contents.add("<br>");
        page8.story.contents.add("MYLEE: <quote>Oh he's quite the cat!  His appetite is as big as they come.  Some day he may eat the whole world.  (He's done it before.)  But for now it seems like he's settled on <variable twin>.<quote>");
        page8.story.contents.add("<br>");
        page8.story.contents.add("YOU: <quote><variable eat-twin><quote>");
        page8.story.contents.add("<br>");
        page8.story.contents.add("MYLEE: <quote>Perhaps.  But you might have some time.  He just ate an entire Red Lobster restaurant for breakfast this morning so he might be full.  I bet <variable twin> will be an appetizer for later.<quote>");
        page8.story.contents.add("<br>");
        page8.story.contents.add("YOU: <quote><variable thats-horrible><quote>");
        page8.story.contents.add("<br>");
        page8.story.contents.add("MYLEE: <quote>Don't I know it, <variable player-mylee-nickname>.  But hey, tell you what... these elevator doors can take you to a magical world.  If you want to save <variable twin>, gain some experience out there and then come back.  If you bring me some GOLD I'll take you to the level that Big Chung hangs out on.  But be careful, Night Owl and a dragon like GOLD and if you remove any from their level they might come after you.  You better slay that dragon if you can and ask my friend Gianni for help with that Night Owl problem.  Oh and don't you dare try to fight Night Owl in a dark place.  He'll swoop down on you before you have a chance to defend yourself.<quote>");
        page8.story.contents.add("<second-page>");
        page8.story.contents.add("<image center /assets/images/mylee.jpg>");
        introScene.pages.put("8", page8);
        
        Page page9 = new Page();
        page9.previousPageName = "5";
        page9.story.contents.add("YOU: <quote><variable mylee-prompt><quote>");
        page9.story.contents.add("<br>");
        page9.story.contents.add("MYLEE: <quote>Nothing else, huh.  Okay.  Maybe if you can find a SPELL BOOK I lost you'll have a way to defeat Big Chung.  But don't worry, things like that have a way of turning up.  Even when you don't think you'll find them again they fly right back to you!<quote>");
        page9.story.contents.add("<br>");
        page9.story.contents.add("YOU: ...");
        page9.story.contents.add("<br>");
        page9.story.contents.add("MYLEE: <quote>That was a clue.  You're welcome.  Take this MAP so you don't get lost.  I'll open the magic elevator doors for you.  Just march on out and start exploring.  Remember: Don't die.  And... bring me back some GOLD!!!<quote>");
        page9.story.contents.add("<inventory-add Map>");
        page9.story.contents.add("<br>");
        page9.story.contents.add("It would seem like you're no longer welcome in this elevator.  Mylee flips a switch and the doors open.  You walk out and find that you are no longer on the cloud...");
        page9.story.contents.add("<second-page>");
        page9.story.contents.add("<image center /assets/images/mylee.jpg>");
        introScene.pages.put("9", page9);

        Act chapter1 = new Act();
        chapter1.firstSceneName = "Chapter";
        book.acts.put("Chapter 1", chapter1);
        
        Story sceneHeaderSubpage = new Story();
        sceneHeaderSubpage.contents.add("You are in <scene>.");
        sceneHeaderSubpage.contents.add("Ahead (<player-direction>) you see <next-scene>.");
        book.subpages.put("Scene Header", sceneHeaderSubpage);
        
        Story navigationFooterSubpage = new Story();
        navigationFooterSubpage.contents.add("<get-input navigation-prompt Turn Left+Move Ahead+Turn Right>");
        book.subpages.put("Navigation Footer", navigationFooterSubpage);
        
        Story inputTurnLeftSubpage = new Story();
        inputTurnLeftSubpage.contents.add("<turn-left>");
        book.subpages.put("INPUT navigation-prompt=Turn Left", inputTurnLeftSubpage);
        
        Story inputMoveAheadSubpage = new Story();
        inputMoveAheadSubpage.contents.add("<move-ahead>");
        book.subpages.put("INPUT navigation-prompt=Move Ahead", inputMoveAheadSubpage);

        Story inputTurnRightSubpage = new Story();
        inputTurnRightSubpage.contents.add("<turn-right>");
        book.subpages.put("INPUT navigation-prompt=Turn Right", inputTurnRightSubpage);
        
        Scene chapterScene = new Scene();
        chapterScene.firstPageName = "1";
        chapterScene.hidePageHeaders = true;
        chapterScene.nextSceneName = "WILDERNESS 1";
        chapterScene.soundFileName = "/assets/sounds/elevator-open.mp3";
        chapter1.scenes.put("Chapter", chapterScene);
        
        page1 = new Page();
        page1.story.contents.add("<image center /assets/images/wayne-chung-classic.jpg>");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<u>CHAPTER 1</u>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("A Dragon in the Kingdom");
        page1.story.contents.add("<set-player-direction SOUTH>");
        page1.story.contents.add("<observed-scene-add MYLEE'S ELEVATOR>");
        chapterScene.pages.put("1", page1);
        
        Scene myleesElevator = new Scene();
        myleesElevator.color = new Color(211, 211, 211);
        myleesElevator.firstPageName = "main";
        myleesElevator.soundFileName = "/assets/sounds/elevator.wav";
        myleesElevator.symbol = "\uD83D\uDED7";
        myleesElevator.x = 2;
        myleesElevator.y = 0;
        chapter1.scenes.put("MYLEE'S ELEVATOR", myleesElevator);
        
        Scene wilderness2 = new Scene();
        wilderness2.color = new Color(0, 100, 0);
        wilderness2.firstPageName = "main";
        wilderness2.soundFileName = "/assets/sounds/wilderness.mp";
        wilderness2.symbol = "\uD83C\uDF33";
        wilderness2.x = 1;
        wilderness2.y = 1;
        chapter1.scenes.put("WILDERNESS 2", wilderness2);
        
        Scene wilderness1 = new Scene();
        wilderness1.color = new Color(79, 47, 79);
        wilderness1.firstPageName = "main";
        wilderness1.soundFileName = "/assets/sounds/wilderness.mp3";
        wilderness1.symbol = "\uD83E\uDEBB";
        wilderness1.x = 2;
        wilderness1.y = 1;
        chapter1.scenes.put("WILDERNESS 1", wilderness1);
        
        Page mainPage = new Page();
        mainPage.story.contents.add("<subpage-display = Scene Header>");
        mainPage.story.contents.add("<color 79+47+79>");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("You see a meadow full of lavendar, then some trees, and then a mighty mountain range off in the distance.  The mountain range stretches to the south where a central point appears to climb so high that the mountain quite possibly goes up into outer space.");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("Here in the meadow the sun is shining peacefully and cute bunnies scamper around you.");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("You need to remember so you don't get lost... the ELEVATOR is by the purple lavendar.");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("</color>");
        mainPage.story.contents.add("<get-input action Listen+Chase Bunnies>");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("<subpage-display = Navigation Footer>");
        mainPage.story.contents.add("<second-page>");
        mainPage.story.contents.add("<u> <variable player>   HP: 100   MP: 0   XP: 0</u>");
        mainPage.story.contents.add("<inventory>");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("<image left /assets/images/wilderness3-small.jpg>");
        wilderness1.pages.put("main", mainPage);
        Story listenSubpage = new Story();
        listenSubpage.contents.add("<goto-page Listen>");
        mainPage.subpages.put("INPUT action=Listen", listenSubpage);
        Story chaseBunniesSubpage = new Story();
        chaseBunniesSubpage.contents.add("<goto-page Chase Bunnies>");
        mainPage.subpages.put("INPUT action=Chase Bunnies", chaseBunniesSubpage);
        
        Page listenPage = new Page();
        listenPage.nextPageName = "main";
        listenPage.story.contents.add("<color 79+47+79>");
        listenPage.story.contents.add("You hear birds singing and perhaps some insects.  There is also a slight breeze.  These are the typical sounds of nature.");
        listenPage.story.contents.add("<br>");
        listenPage.story.contents.add("Oh the great outdoors!");
        listenPage.story.contents.add("</color>");
        wilderness1.pages.put("Listen", listenPage);
        
        Page chaseBunniesPage = new Page();
        chaseBunniesPage.nextPageName = "main";
        chaseBunniesPage.story.contents.add("<color 79+47+79>");
        chaseBunniesPage.story.contents.add("Seems like an unimportant thing to do but nevertheless you try to chase some of the bunnies.");
        chaseBunniesPage.story.contents.add("<br>");
        chaseBunniesPage.story.contents.add("And...");
        chaseBunniesPage.story.contents.add("</color>");
        wilderness1.pages.put("Chase Bunnies", chaseBunniesPage);
        
        Scene toadstoolCircle = new Scene();
        toadstoolCircle.color = new Color(184, 115, 51);
        toadstoolCircle.firstPageName = "main";
        toadstoolCircle.soundFileName = "/assets/sounds/magic-chimes.mp3";
        toadstoolCircle.symbol = "\uD83C\uDF44";
        toadstoolCircle.x = 3;
        toadstoolCircle.y = 1;
        chapter1.scenes.put("MAGIC RING OF TOADSTOOLS", toadstoolCircle);
        
        Scene giannisDen = new Scene();
        giannisDen.color = new Color(0, 0, 0);
        giannisDen.firstPageName = "main";
        giannisDen.soundFileName = "/assets/sounds/bonfire.mp3";
        giannisDen.symbol = "\uD83D\uDC08\u200D\u2B1B";
        giannisDen.x = 1;
        giannisDen.y = 2;
        chapter1.scenes.put("GIANNI'S DEN", giannisDen);
        
        Scene woods = new Scene();
        woods.color = new Color(85, 85, 85);
        woods.firstPageName = "main";
        woods.soundFileName = "/assets/sounds/woods.mp3";
        woods.symbol = "\uD83E\uDEBE";  // Branchless tree is new with Unicode v16.0 (2024) and is only supported in recent versions of Java
        woods.symbol = "\uD83E\uDD89";
        woods.x = 2;
        woods.y = 2;
        chapter1.scenes.put("WOODS", woods);
        
        Scene mountFluff = new Scene();
        mountFluff.color = new Color(255, 255, 255);
        mountFluff.firstPageName = "main";
        mountFluff.soundFileName = "/assets/sounds/wind.mp3";
        mountFluff.symbol = "\uD83C\uDFD4";
        mountFluff.x = 3;
        mountFluff.y = 2;
        chapter1.scenes.put("MOUNT FLUFF", mountFluff);
        
        Scene mysteryRoom = new Scene();
        mysteryRoom.color = new Color(184, 115, 51);
        mysteryRoom.firstPageName = "main";
        mysteryRoom.soundFileName = "";
        mysteryRoom.symbol = "\u2754";
        mysteryRoom.x = 1;
        mysteryRoom.y = 3;
        chapter1.scenes.put("MYSTERY ROOM", mysteryRoom);
        
        Scene castle = new Scene();
        castle.color = new Color(171, 145, 68);
        castle.firstPageName = "main";
        castle.soundFileName = "/assets/sounds/castle.mp3";
        castle.symbol = "\uD83C\uDFF0";
        castle.x = 2;
        castle.y = 3;
        chapter1.scenes.put("CASTLE", castle);
        
        Scene dragonsDen = new Scene();
        dragonsDen.color = new Color(255, 0, 0);
        dragonsDen.firstPageName = "main";
        dragonsDen.soundFileName = "/assets/sounds/bonfire.mp3";
        dragonsDen.symbol = "\uD83D\uDC09";
        dragonsDen.x = 3;
        dragonsDen.y = 3;
        chapter1.scenes.put("DRAGON'S DEN", dragonsDen);
        
        book.highScores = new ArrayList<>();
        book.highScores.add(new HighScore(300, "GRT", LocalDate.of(2024, Month.MARCH, 9)));
        book.highScores.add(new HighScore(300, "ZRT", LocalDate.of(2024, Month.MARCH, 9)));
        book.highScores.add(new HighScore(300, "GRT", LocalDate.of(2024, Month.MARCH, 23)));
        book.highScores.add(new HighScore(300, "ZRT", LocalDate.of(2024, Month.MARCH, 23)));
        book.highScores.add(new HighScore(300, "ZRT", LocalDate.of(2024, Month.MARCH, 23)));
        book.highScores.add(new HighScore(200, "ZRT", LocalDate.of(2024, Month.MARCH, 23)));
        String fileName = "/home/repp/Documents/quests/twin.quest";
        FileOutputStream file;
        try {
            file = new FileOutputStream(fileName);
            ObjectOutputStream out;
            try {
                out = new ObjectOutputStream(file);
                out.writeObject(book);
            } catch (IOException ex) {
                Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
