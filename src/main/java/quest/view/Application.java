package quest.view;

import app.controller.BaseController;
import app.Color;
import app.FontStyle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
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
public class Application extends app.view.BaseView {
    
    public BaseController appController;
    public Book bookFile;
    public String flavorText;
    
    public Application(String name) {
        super(name);
        this.backgroundColor = new Color(255, 255, 255);
        this.backgroundImage = "/assets/images/app.jpg";
        this.emojis.add("\uD83D\uDCDA"); // "books" Unicode emoji
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("Application: onEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        switch (eventName) {
            case "quest" -> {
                this.appController.stopAllSounds();
                String fileName = (String) eventValue;
                this.bookFile = deserializeBook(fileName);
                this.appController.clearScreen(this.name);
                this.display();
                this.publishEvent("book", bookFile);
            } case "create" -> {
                this.appController.displayMessageBox("Coming soon!", "Creating a new quest is not available at this time.", app.Icon.INFORMATION, null);
            } case "options" -> {
                this.appController.displayMessageBox("Coming soon!", "Application options are not available at this time.", app.Icon.INFORMATION, null);
            } case "quit" -> {
                this.appController.close();
            } default -> System.err.println("Application: onEvent: Unsupported event");
        }
    }
    
    @Override
    public void onLoad(BaseController appController) {
        System.out.println("Application: onLoad");
        
        this.appController = appController;
        
        appController.playSound("/assets/sounds/questcraft.mp3", true);
        
        String[] responses = {"Keep your hands and feet inside the quest at all times", "Presented in Quest-o-Vision (where available)", "Filmed on location", "Proudly made in your imagination", "Recommended by 4 out of 5 unicorns", "The game that plays you", "A stern warning of things to come", "Painstakingly rendered before a live studio audience", "Sock puppets not included", "The official questing game of gnomes", "Or is it?", "It makes a nice sandwich!", "Tips are not expected but appreciated", "There will be a test at the end", "Made you look!", "Proud supporter of the Lollipop Guild", "Soon to be a hit game", "Made from 100% recycled pixels", "WARNING: Do not show to axolotls", "WARNING: May cause tentacles to emerge from your screen", "Featuring a new invisible character who doesn't speak", "You have been warned", "Don't look behind you", "Ask about our new pumpkin spice flavor!"};
        int randomResponseIndex = (int) (Math.random() * responses.length);
        this.flavorText = responses[randomResponseIndex];
        
        this.display();
        
        serializeTwinQuestBook();
        serializeMadQuestBook();
    }
    
    public void display() {
        System.out.println("Application: display");
        
        int spiderColumns = appController.getColumns("/assets/images/spider.gif");
        int parentColumns = appController.getTextColumns();
        int gifColumn = parentColumns - spiderColumns + 2;    // Puts the spider in the upper right-hand corner
        appController.displayGif(this.name, "/assets/images/spider.gif", 1, gifColumn);
        
        appController.displayButton(this.name, "quit", "Quit Game", 22, 67, 24, 84, false, "Minecraft", true, this);        
        appController.displayButton(this.name, "options", "Options...", 22, 46, 24, 64, false, "Minecraft", true, this);        
        appController.displayButton(this.name, "create", "Create Quest", 19, 46, 21, 84, false, "Minecraft", true, this);        
        appController.displayOpenFileButton(this.name, "quest", "Select Quest", 16, 46, 18, 84, false, "Minecraft", true, this);        

        app.Color titleColor = new app.Color(74, 74, 74);   // Dark gray
        app.Color infoTextColor = new Color(139, 0, 139); // Dark magenta
        appController.displayFloatingText(this.name, null, this.flavorText, 14, 40, 15, 90, infoTextColor, 12, FontStyle.ITALIC, "Minecraft");
        appController.displayFloatingText(this.name, null, "- JAVA  EDITION -", 11, 42, 13, 88, titleColor, 32, FontStyle.BOLD, "Minecraft");
        appController.displayFloatingText(this.name, null, "QUESTCRAFT", 7, 32, 11, 98, titleColor, 64, FontStyle.BOLD, "Minecraft");

        if (bookFile != null) {
            // Display "Now Playing" info
            appController.displayFloatingText(this.name, null, this.bookFile.updateDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.getDefault())), 35, 42, 37, 88, infoTextColor, 18, null, "Minecraft");
            appController.displayFloatingText(this.name, null, "by " + this.bookFile.author, 33, 42, 35, 88, infoTextColor, 18, null, "Minecraft");
            appController.displayFloatingText(this.name, null, this.bookFile.title, 31, 42, 33, 88, infoTextColor, 18, null, "Minecraft");
            appController.displayFloatingText(this.name, null, "Now Playing...", 28, 42, 30, 88, infoTextColor, 18, FontStyle.BOLD, "Minecraft");
        }
        
        appController.displayFloatingText(this.name, null, "Coming soon... Twin Quest Chapter 2", 41, 90, 42, 126, null, 12, FontStyle.BOLD, "Minecraft");
    }
    
    public Book deserializeBook(String fileName) {
        System.out.println("Application: deserializeBook");
        
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
                } catch (ClassNotFoundException e) {
                    System.err.println("Application: deserializeBook: " + e.toString());
                }
            } catch (IOException e) {
                System.err.println("Application: deserializeBook: " + e.toString());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Application: deserializeBook: " + e.toString());
        }
        
        return bf;
    }
    
    // TODO - Just a temporary helper method to get the book going.  Eventually the Crafting Table should be used for this purpose.
    public void serializeMadQuestBook() {
        System.out.println("Application: serializeMadQuestBook");
        
        Book book = new Book();
        book.author = "Mr. Chris (with Google AI)";
        book.firstActName = "Opening";
        book.title = "Mad Quest";
        book.updateDate = LocalDate.now();

        Act opening = new Act();
        opening.firstSceneName = "Title Page";
        book.acts.put("Opening", opening);
        
        Scene titlePage = new Scene();
        titlePage.firstPageName = "1";
        titlePage.hidePageHeaders = true;
        opening.scenes.put("Title Page", titlePage);
        Page page1 = new Page();
        page1.story.contents.add("<color 0+0+0><b><book-title></b>");
        page1.story.contents.add("by <book-author>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("Last Updated: <book-last-updated-date>");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("TABLE OF CONTENTS");
        page1.story.contents.add("<br>");
        page1.story.contents.add("1... <link The Mystery of the Glimmering Portal>");
        page1.story.contents.add("2... <link The Giggling Gnomes of Jumbledoor>");
        page1.story.contents.add("3... <link The Creepy Clown Carnival>");
        page1.story.contents.add("</color>");
        titlePage.pages.put("1", page1);
        
        Story introStory = new Story();
        introStory.contents.add("<goto-act The Mystery of the Glimmering Portal>");
        page1.subpages.put("LINK The Mystery of the Glimmering Portal", introStory);
        Story comedyStory = new Story();
        comedyStory.contents.add("<goto-act The Giggling Gnomes of Jumbledoor>");
        page1.subpages.put("LINK The Giggling Gnomes of Jumbledoor", comedyStory);
        Story scaryStory = new Story();
        scaryStory.contents.add("<goto-act The Creepy Clown Carnival>");
        page1.subpages.put("LINK The Creepy Clown Carnival", scaryStory);
        
        Act introQuest = new Act();
        introQuest.firstSceneName = "Word Entry";
        introQuest.previousActName = "Opening";
        book.acts.put("The Mystery of the Glimmering Portal", introQuest);
        
        Scene wordEntry = new Scene();
        wordEntry.firstPageName = "1";
        wordEntry.hidePageHeaders = false;
        wordEntry.nextSceneName = "Quest";
        introQuest.scenes.put("Word Entry", wordEntry);
        page1 = new Page();
        page1.story.contents.add("<color 0+0+0>");
        page1.story.contents.add("<first-page><get-input brothers-name 20 false false true true Brother's Name>");
        page1.story.contents.add("<get-input sisters-name 20 false false true true Sister's Name>");
        page1.story.contents.add("<get-input adjective-1 20 false false true true Adjective>");
        page1.story.contents.add("<get-input adjective-2 20 false false true true Adjective>");
        page1.story.contents.add("<get-input furniture 20 false false true true Noun (a piece of furniture)>");
        page1.story.contents.add("<get-input adverb 20 false false true true Adverb (describing an adjective)>");
        page1.story.contents.add("<get-input plural-noun 20 false false true true Plural Noun (magical creatures)>");
        page1.story.contents.add("<get-input verb-ing 20 false false true true Verb (ending in -ing)>");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<get-input past-tense-verb 20 false false true true Past Tense Verb>");
        page1.story.contents.add("<get-input weather 20 false false true true Type of Weather>");
        page1.story.contents.add("<get-input mythical-object 20 false false true true Noun (a mythical object)>");
        page1.story.contents.add("<get-input number 20 false false true true A Number>");
        page1.story.contents.add("<get-input exclamation 20 false false true true Exclamation>");
        page1.story.contents.add("<get-input adjective-3 20 false false true true Adjective>");
        page1.story.contents.add("<get-input color 20 false false true true Color>");
        page1.story.contents.add("</color>");
        wordEntry.pages.put("1", page1);
        
        Scene quest = new Scene();
        quest.firstPageName = "1";
        quest.hidePageHeaders = false;
        quest.previousSceneName = "Word Entry";
        introQuest.scenes.put("Quest", quest);
        page1 = new Page();
        page1.story.contents.add("<color 0+0+0>");
        page1.story.contents.add("<first-page>It all started on a <variable adjective-1> and <variable weather> afternoon.  <variable brothers-name> was <variable verb-ing> under the <variable furniture>, when he noticed a strange glimmer.  His sister, <variable sisters-name>, came to investigate.  They looked closely and saw a swirling, magical portal!");
        page1.story.contents.add("<br>");
        page1.story.contents.add("<quote><variable exclamation>!<quote> they both shouted.  <variable brothers-name>, feeling brave, reached for it.  Suddenly, they were sucked through the portal, and they landed with a thud in a world filled with <variable plural-noun>.  The air smelled of <variable adjective-2> flowers and the sky was a bright <variable color>.");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<quote>We're in a magical realm!<quote> <variable sisters-name> said, looking at the glowing <variable mythical-object> that floated above a nearby pond. But their wonder quickly turned to alarm. A group of <variable number> <variable plural-noun> were blocking their way.");
        page1.story.contents.add("<br>");
        page1.story.contents.add("The siblings remembered the secret instructions their grandmother had given them: to always behave <variable adverb>. So instead of fighting, they <variable past-tense-verb> a song, and the <variable plural-noun> were so surprised they let the children pass. With their wits and kindness, <variable brothers-name> and <variable sisters-name> found their way home, just in time for a <variable adjective-3> dinner.");
        page1.story.contents.add("</color>");
        quest.pages.put("1", page1);
        
        Act comedyQuest = new Act();
        comedyQuest.firstSceneName = "Word Entry";
        comedyQuest.previousActName = "Opening";
        book.acts.put("The Giggling Gnomes of Jumbledoor", comedyQuest);
        
        wordEntry = new Scene();
        wordEntry.firstPageName = "1";
        wordEntry.hidePageHeaders = false;
        wordEntry.nextSceneName = "Quest";
        comedyQuest.scenes.put("Word Entry", wordEntry);
        page1 = new Page();
        page1.story.contents.add("<color 0+0+0>");
        page1.story.contents.add("<first-page><get-input brothers-name 20 false false true true Brother's Name>");
        page1.story.contents.add("<get-input sisters-name 20 false false true true Sister's Name>");
        page1.story.contents.add("<get-input magical-creatures 20 false false true true Plural Noun (magical creatures)>");
        page1.story.contents.add("<get-input food 20 false false true true Type of Food>");
        page1.story.contents.add("<get-input exclamation 20 false false true true Type of Exclamation>");
        page1.story.contents.add("<get-input number 20 false false true true A Number>");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<get-input adjective-1 20 false false true true Adjective>");
        page1.story.contents.add("<get-input verb 20 false false true true Verb (ending in -ing)>");
        page1.story.contents.add("<get-input furniture 20 false false true true Noun (a piece of furniture)>");
        page1.story.contents.add("<get-input adverb 20 false false true true Adverb (describing an action)>");
        page1.story.contents.add("<get-input noun-singular 20 false false true true Noun (singular)>");
        page1.story.contents.add("<get-input adjective-2 20 false false true true Adjective>");
        page1.story.contents.add("</color>");
        wordEntry.pages.put("1", page1);
        
        quest = new Scene();
        quest.firstPageName = "1";
        quest.hidePageHeaders = false;
        quest.previousSceneName = "Word Entry";
        comedyQuest.scenes.put("Quest", quest);
        page1 = new Page();
        page1.story.contents.add("<color 0+0+0>");
        page1.story.contents.add("<first-page>The siblings <variable brothers-name> and <variable sisters-name> stumbled through a shimmering rainbow mist and found themselves in the magical realm of Jumbledoor.  All around them, tiny, mischievous <variable magical-creatures> were <variable verb>.  <quote>What are they doing?<quote> <variable sisters-name> asked.  One of the creatures ran up to them and asked, <quote>Why did the wizard cross the road?<quote>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("<variable brothers-name> shrugged, <quote>I don't know, why?<quote>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("<quote>To get to the <variable adjective-1> side!<quote> the creature squeaked, then exploded into a puff of <variable food>.");
        page1.story.contents.add("<br>");
        page1.story.contents.add("Feeling very <variable adjective-2>, they continued on their quest to find the legendary <variable noun-singular>.  They had just passed a talking <variable furniture> when another voice was heard.  <quote>What do you call a magical bear with no teeth?<quote>");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<quote><variable exclamation>!<quote> shouted <variable sisters-name>.  <quote>This place is full of comedians!<quote>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("<quote>I don't know, what?<quote> she asked.");
        page1.story.contents.add("<br>");
        page1.story.contents.add("<quote>A gummy bear!<quote> shouted the <variable number> <variable magical-creatures>.");
        page1.story.contents.add("<br>");
        page1.story.contents.add("The siblings burst out laughing and continued their journey <variable adverb>, realizing that even in a magical land, a good joke can be the best kind of magic.");
        page1.story.contents.add("</color>");
        quest.pages.put("1", page1);
        
        Act scaryQuest = new Act();
        scaryQuest.firstSceneName = "Word Entry";
        scaryQuest.previousActName = "Opening";
        book.acts.put("The Creepy Clown Carnival", scaryQuest);
        
        wordEntry = new Scene();
        wordEntry.firstPageName = "1";
        wordEntry.hidePageHeaders = false;
        wordEntry.nextSceneName = "Quest";
        scaryQuest.scenes.put("Word Entry", wordEntry);
        page1 = new Page();
        page1.story.contents.add("<color 0+0+0>");
        page1.story.contents.add("<first-page><get-input brothers-name 20 false false true true Brother's Name>");
        page1.story.contents.add("<get-input sisters-name 20 false false true true Sister's Name>");
        page1.story.contents.add("<get-input adjective-1 20 false false true true Adjective>");
        page1.story.contents.add("<get-input adjective-2 20 false false true true Adjective>");
        page1.story.contents.add("<get-input body-part 20 false false true true Noun (a body part, plural)>");
        page1.story.contents.add("<get-input bug 20 false false true true Plural Noun (a bug or insect)>");
        page1.story.contents.add("<get-input food 20 false false true true Noun (a type of food)>");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<get-input verb 20 false false true true Verb (past tense, ended in -ed)>");
        page1.story.contents.add("<get-input adverb 20 false false true true Adverb (describing an action)>");
        page1.story.contents.add("<get-input color 20 false false true true Color>");
        page1.story.contents.add("<get-input scary-character 20 false false true true Noun (a scary character)>");
        page1.story.contents.add("<get-input number 20 false false true true A Number>");
        page1.story.contents.add("<get-input exclamation 20 false false true true Exclamation>");
        page1.story.contents.add("</color>");
        wordEntry.pages.put("1", page1);
        
        quest = new Scene();
        quest.firstPageName = "1";
        quest.hidePageHeaders = false;
        quest.previousSceneName = "Word Entry";
        scaryQuest.scenes.put("Quest", quest);
        page1 = new Page();
        page1.story.contents.add("<color 0+0+0>");
        page1.story.contents.add("<first-page>On a <variable adjective-1> night, <variable brothers-name> and <variable sisters-name> snuck into an abandoned carnival.  The ferris wheel <variable verb> slowly in the wind, and a rusty calliope played a <variable adjective-2> tune.  They had heard stories of the carnival's past: how all the clowns disappeared <variable adverb> many years ago.");
        page1.story.contents.add("<br>");
        page1.story.contents.add("As they walked past the <variable color> ticket booth, they heard a terrifying giggle.  A large <variable scary-character> appeared from the shadows, his face painted with a creepy smile.  <quote>Want some <variable bug>?<quote> he asked, holding out a dirty bag.  The children ran away screaming, their <variable body-part> pounding in their chests.");
        page1.story.contents.add("<br>");
        page1.story.contents.add("They found refuge in a funhouse, but the mirrors distorted their reflections in horrible ways.  A <variable number>-legged clown walked out of a mirrored wall, offering them a <variable food>.  The clowns weren't disappearing; they were multiplying!");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<quote><variable exclamation>!<quote> <variable brothers-name> yelled, <quote>This place is a trap!<quote>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("They ran out of the funhouse and found their way back out of the carnival.  The music had stopped, and all the creepy clowns were gone.  The <variable adjective-2> siblings went home, promising each other never to return to the Creepy Clown Carnival.");
        page1.story.contents.add("</color>");
        quest.pages.put("1", page1);
        
        String fileName = "/home/repp/Documents/quests/mad.quest";
        FileOutputStream file;
        try {
            file = new FileOutputStream(fileName);
            ObjectOutputStream out;
            try {
                out = new ObjectOutputStream(file);
                out.writeObject(book);
            } catch (IOException e) {
                System.err.println("Application: serializeMadQuestBook: " + e.toString());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Application: serializeMadQuestBook: " + e.toString());
        }
    }
    
    // TODO - Just a temporary helper method to get the book going.  Eventually the Crafting Table should be used for this purpose.
    public void serializeTwinQuestBook() {
        System.out.println("Application: serializeTwinQuestBook");
        
        Book book = new Book();
        book.animationFileName = "/assets/images/dragon.gif";
        book.preloadEmojisDuringAnimation = true;
        book.author = "R. W. Chung";
        book.firstActName = "Opening";
        book.title = "BIG CHUNG, Destroyer of Worlds";
        book.updateDate = LocalDate.now();    
        book.inventory = new LinkedHashMap<>();

        
        // Inventory items are added in alphabetical order
        InventoryItem item = new InventoryItem("Inscribed on a scroll in arcane symbols, this spell unlocks powerful magic that you can cast with your Spell Book.", "/assets/sounds/magic.wav", new ArrayList<>(List.of("\uD83D\uDCDC")));
        item.quantity = 1;
        book.inventory.put("Ancient Spell", item);
        item = new InventoryItem("The delicious yellow fruit.  Collect as many as you can!", "/assets/sounds/monkey.wav", new ArrayList<>(List.of("\uD83C\uDF4C")));
        item.quantity = 100;
        book.inventory.put("Banana", item);
        item = new InventoryItem("A cute little fuzzy bunny.  Be sure to keep it safe from predators!", "/assets/sounds/spring.wav", new ArrayList<>(List.of("\uD83D\uDC07")));
        item.quantity = 100;
        item.xp = 1;
        book.inventory.put("Bunny", item);
        item = new InventoryItem("A small, portable launcher with an endless supply of cats.", "/assets/sounds/catapult.wav", new ArrayList<>(List.of("\uD83D\uDC08", "\uD83D\uDCE4")));
        item.xp = 10;
        book.inventory.put("Cat-apult", item);
        item = new InventoryItem("He's the Big Chungus of the bunny world!", "/assets/sounds/spring.wav", new ArrayList<>(List.of("\uD83D\uDC30")));
        item.quantity = 100;
        item.xp = 10;
        book.inventory.put("Chungus Bunny", item);
        item = new InventoryItem("A foreboding monolith of enormous size.  Its existence predates that of our species.  Perhaps even the world.  What terrible powers does it hold?", "/assets/sounds/cosmic-wonder-1.wav", new ArrayList<>(List.of("\uD83C\uDF0C")));
        item.xp = 10;
        book.inventory.put("Cosmic Wonder #1", item);
        item = new InventoryItem("A small, portable launcher with an endless supply of woodland faeries.", "/assets/sounds/faery-zing.wav", new ArrayList<>(List.of("\uD83E\uDDDA", "\uD83D\uDCE4")));
        item.xp = 10;
        book.inventory.put("Faery Launcher", item);
        item = new InventoryItem("Stinky methane gas.  Don't let any of this leak out of its tank!  Gross!!!", "/assets/sounds/fart.wav", new ArrayList<>(List.of("\uD83D\uDCA8")));
        book.inventory.put("Gas", item);
        item = new InventoryItem("Stinky methane gas.  Don't let any of this leak out of its tank!  Gross!!!", "/assets/sounds/fart.wav", new ArrayList<>(List.of("\uD83D\uDCA8")));
        book.inventory.put("Gas", item);
        item = new InventoryItem("Shiny gold!", "/assets/sounds/gold.wav", new ArrayList<>(List.of("\uD83D\uDCB0")));
        item.xp = 10;
        book.inventory.put("Gold", item);
        item = new InventoryItem("A royal-looking key.  But what does it unlock?", "/assets/sounds/key.wav", new ArrayList<>(List.of("\uD83D\uDD11")));
        book.inventory.put("Golden Key", item);
        item = new InventoryItem("An impressive mid-range weapon.  Great for slaying flying monsters!", "/assets/sounds/arrow.mp3", new ArrayList<>(List.of("\uD83C\uDFF9")));
        item.xp = 10;
        book.inventory.put("Greyson's Great Bow", item);
        item = new InventoryItem("This is a serious weapon!  The high-intensity laser beam can destory large objects with a single blast.", "/assets/sounds/laser.wav", new ArrayList<>(List.of("\uD83C\uDFEE")));
        book.inventory.put("Laser Cannon", item);
        item = new InventoryItem("A magical fold-out piece of paper showing each of the locations in the current level.", "/assets/sounds/paper.wav", new ArrayList<>(List.of(SceneMap.EMOJI)));
        Story onAdd = new Story();
        onAdd.contents.add("<add-view Map>");
        item.onAdd = onAdd;
        Story onSelect = new Story();
        onSelect.contents.add("<tab-select Map>");
        item.onSelect = onSelect;
        item.xp = 0;
        book.inventory.put("Map", item);
        item = new InventoryItem("Wear this medal proudly.  It marks the honor, courage, and valour of those who have earned the right to wear it", "/assets/sounds/achievement.wav", new ArrayList<>(List.of("\uD83C\uDF96\uFE0F")));
        book.inventory.put("Military Rank", item);
        item = new InventoryItem("Anyone up for a game?  The classic back-and-forth paddle game complete with 2 paddles and a ball.", "/assets/sounds/ping-pong.wav", new ArrayList<>(List.of("\uD83C\uDFD3")));
        book.inventory.put("Ping Pong Set", item);
        item = new InventoryItem("A fine-tuned machine, this speedster can go from 0 to 60 in just one second!  A wonderful car for any racing competition.  But this car is unique in that it is fueled by methane which is hard to find in abundance.", "/assets/sounds/race-car.wav", new ArrayList<>(List.of("\uD83C\uDFCE\uFE0F")));
        book.inventory.put("Race Car", item);
        item = new InventoryItem("A magical ring forged by woodland gnomes for taming creatures.  But beware!  It does not work on large creatures.", "/assets/sounds/ring-of-taming.wav", new ArrayList<>(List.of("\uD83D\uDC8D")));
        item.xp = 10;
        book.inventory.put("Ring of Taming", item);
        item = new InventoryItem("A brand new high-altitude snowboard!  Perfect for an all-expenses paid vacation at Mount Fluff.", "/assets/sounds/snowboard.wav", new ArrayList<>(List.of("\uD83C\uDFBF")));
        book.inventory.put("Snowboard", item);
        item = new InventoryItem("A powerful book!  Spells scribed onto its pages become manifest in the world.", "/assets/sounds/spell-cast.wav", new ArrayList<>(List.of(SpellBook.EMOJI)));
        onAdd = new Story();
        onAdd.contents.add("<add-view Spell Book>");
        item.onAdd = onAdd;
        onSelect = new Story();
        onSelect.contents.add("<tab-select Spell Book>");
        item.onSelect = onSelect;
        item.xp = 0;
        book.inventory.put("Spell Book", item);
        item = new InventoryItem("The fabeled UNO reverse card.  Used to return damage back into the face of he who dealt it.", "/assets/sounds/reverse.wav", new ArrayList<>(List.of("\uD83D\uDD04")));
        item.xp = 10;
        book.inventory.put("UNO Reverse", item);
        item = new InventoryItem("A magnificent blade.  Great for slaying dragons!", "/assets/sounds/sword.wav", new ArrayList<>(List.of("\uD83D\uDDE1\uFE0F")));
        item.xp = 10;
        book.inventory.put("Zara's Sword", item);

        Story flipBookSubpage = new Story();
        flipBookSubpage.isSpell = true;
        flipBookSubpage.contents.add("<flip-book>");
        book.subpages.put("FLIP BOOK", flipBookSubpage);
        
        Story activateInventorySubpage = new Story();
        activateInventorySubpage.isSpell = true;
        activateInventorySubpage.contents.add("<variable-set activate-inventory true>");
        book.subpages.put("ACTIVATE INVENTORY", activateInventorySubpage);
        
        // Once the player has selected a difficulty level, allow them to skip straight to the first Night Owl miniboss game
        Story nightOwlSubpage = new Story();
        nightOwlSubpage.isSpell = true;
        nightOwlSubpage.contents.add("<variable-set condition=\"variable difficulty!=\" is-Gianni-tamed true>");
        nightOwlSubpage.contents.add("<variable-set condition=\"variable difficulty!=\" is-dragon-defeated true>");
        nightOwlSubpage.contents.add("<subpage-display condition=\"variable difficulty!=\" Chapter 1 Equipment>");
        nightOwlSubpage.contents.add("<goto-act condition=\"variable difficulty!=\" Chapter 1>");
        nightOwlSubpage.contents.add("<goto-scene condition=\"variable difficulty!=\" MYLEE'S ELEVATOR>");
        book.subpages.put("NIGHT OWL", nightOwlSubpage);
        
        // Once the player has selected a difficulty level, allow them to skip straight to Chapter 2
        Story act2Subpage = new Story();
        act2Subpage.isSpell = true;
        act2Subpage.contents.add("<variable-set condition=\"variable difficulty!=\" is-Gianni-tamed true>");
        act2Subpage.contents.add("<variable-set condition=\"variable difficulty!=\" is-dragon-defeated true>");
        act2Subpage.contents.add("<variable-set condition=\"variable difficulty!=\" is-night-owl-defeated true>");
        act2Subpage.contents.add("<subpage-display condition=\"variable difficulty!=\" Chapter 1 Equipment>");
        act2Subpage.contents.add("<inventory-remove condition=\"inventory-has Gold=true\" true Gold>");
        act2Subpage.contents.add("<background-color 0+0+0 Quest>");
        act2Subpage.contents.add("<background-color 0+0+0 Inventory>");
        act2Subpage.contents.add("<background-color 0+0+0 Map>");
        act2Subpage.contents.add("<background-color 0+0+0 Spell Book>");
        act2Subpage.contents.add("<background-color 0+0+0 High Scores>");
        act2Subpage.contents.add("<tab-select Quest>");
        act2Subpage.contents.add("<goto-act condition=\"variable difficulty!=\" Chapter 2>");
        book.subpages.put("A DARKNESS OVER THE LAND", act2Subpage);
        
        Story chapter1EquipmentSubpage = new Story();
        chapter1EquipmentSubpage.contents.add("<inventory-add condition=\"inventory-has Gold!=true\" true Gold>");
        chapter1EquipmentSubpage.contents.add("<inventory-add condition=\"inventory-has Ring of Taming!=true\" true Ring of Taming>");
        chapter1EquipmentSubpage.contents.add("<inventory-add condition=\"player=Greyson\" true Greyson's Great Bow>");
        chapter1EquipmentSubpage.contents.add("<inventory-add condition=\"player=Greyson\" true UNO Reverse>");
        chapter1EquipmentSubpage.contents.add("<inventory-add condition=\"player=Zara\" true Zara's Sword>");
        chapter1EquipmentSubpage.contents.add("<inventory-add condition=\"player=Zara\" true Cat-apult>");
        chapter1EquipmentSubpage.contents.add("<inventory-add condition=\"player=Shmebulock\" true Cosmic Wonder #1>");
        chapter1EquipmentSubpage.contents.add("<inventory-add condition=\"player=Shmebulock\" true Faery Launcher>");
        book.subpages.put("Chapter 1 Equipment", chapter1EquipmentSubpage);
        
        Act opening = new Act();
        opening.firstSceneName = "Title Page";
        book.acts.put("Opening", opening);
        Scene titlePage = new Scene();
        titlePage.firstPageName = "1";
        titlePage.hidePageHeaders = true;
        titlePage.nextSceneName = "Player Selection";
        titlePage.stopOtherSounds = true;
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
        page1.story.contents.add("<br>");
        page1.story.contents.add(" <b><book-title></b>");
        page1.story.contents.add("  by <book-author>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("  Last Updated: <book-last-updated-date>");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<image title-page center /assets/images/title-page.jpg>");
        titlePage.pages.put("1", page1);
        
        Scene playerSelection = new Scene();
        playerSelection.firstPageName = "1";
        playerSelection.hidePageHeaders = true;
        playerSelection.stopOtherSounds = true;
        playerSelection.soundFileName = "/assets/sounds/epic.mp3";
        opening.scenes.put("Player Selection", playerSelection);
        page1 = new Page();
        page1.story.contents.add("Select Player:");
        page1.story.contents.add("<subpage-display condition=\"summonShmebulock=true\" SHMEBULOCK input>");
        page1.story.contents.add("<subpage-display condition=\"summonShmebulock!=true\" input>");
        page1.story.contents.add("<inventory-add true Map>");
        page1.story.contents.add("<inventory-add true Spell Book>");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<image twins center /assets/images/twins.jpg>");
        Story playerGreysonSubpage = new Story();
        playerGreysonSubpage.contents.add("<set-player-symbol \uD83E\uDDD2>");
        playerGreysonSubpage.contents.add("<variable-set twin Zara>");
        playerGreysonSubpage.contents.add("<variable-set twin-with-symbol \uD83D\uDC67 Zara>");
        playerGreysonSubpage.contents.add("<variable-set battle-cry I don't know if I can, but I will try!!!>");
        playerGreysonSubpage.contents.add("<variable-set twin-voice twin's voice>");
        playerGreysonSubpage.contents.add("<variable-set player-mylee-nickname kid>");
        playerGreysonSubpage.contents.add("<variable-set mylee-fandom And I definitely wouldn't want to if I could.  You humans are just too weird.>");
        playerGreysonSubpage.contents.add("<variable-set mylee-reaction Weird>");
        playerGreysonSubpage.contents.add("<variable-set twin-was twin was>");
        playerGreysonSubpage.contents.add("<variable-set why-is-that Why is that?>");
        playerGreysonSubpage.contents.add("<variable-set eat-twin He's going to eat Zara?!>");
        playerGreysonSubpage.contents.add("<variable-set thats-horrible That's horrible!!!>");
        playerGreysonSubpage.contents.add("<goto-scene Difficulty Selection>");
        page1.subpages.put("INPUT player=Greyson", playerGreysonSubpage);
        Story playerZaraSubpage = new Story();
        playerZaraSubpage.contents.add("<set-player-symbol \uD83D\uDC67>");
        playerZaraSubpage.contents.add("<variable-set twin Greyson>");
        playerZaraSubpage.contents.add("<variable-set twin-with-symbol \uD83E\uDDD2 Greyson>");
        playerZaraSubpage.contents.add("<variable-set battle-cry I don't know if I can, but I will try!!!>");
        playerZaraSubpage.contents.add("<variable-set twin-voice twin's voice>");
        playerZaraSubpage.contents.add("<variable-set player-mylee-nickname kid>");
        playerZaraSubpage.contents.add("<variable-set mylee-fandom And I definitely wouldn't want to if I could.  You humans are just too weird.>");
        playerZaraSubpage.contents.add("<variable-set mylee-reaction Weird>");
        playerZaraSubpage.contents.add("<variable-set twin-was twin was>");
        playerZaraSubpage.contents.add("<variable-set why-is-that Why is that?>");
        playerZaraSubpage.contents.add("<variable-set eat-twin He's going to eat Greyson?!>");
        playerZaraSubpage.contents.add("<variable-set thats-horrible That's horrible!!!>");
        playerZaraSubpage.contents.add("<goto-scene Difficulty Selection>");
        page1.subpages.put("INPUT player=Zara", playerZaraSubpage);
        Story playerShmebulockSubpage = new Story();
        playerShmebulockSubpage.contents.add("<set-player-symbol \uD83C\uDF85>");
        playerShmebulockSubpage.contents.add("<variable-set twin Greyson and Zara>");
        playerShmebulockSubpage.contents.add("<variable-set twin-with-symbol \uD83E\uDDD2 Greyson and \uD83D\uDC67 Zara>");
        playerShmebulockSubpage.contents.add("<variable-set battle-cry SHMEBULOCK!!!>");
        playerShmebulockSubpage.contents.add("<variable-set twin-voice friends' voices>");
        playerShmebulockSubpage.contents.add("<variable-set player-mylee-nickname magical one>");
        playerShmebulockSubpage.contents.add("<variable-set mylee-fandom But I wish I could.  You gnomes are just fascinating!>");
        playerShmebulockSubpage.contents.add("<variable-set mylee-reaction Fascinating>");
        playerShmebulockSubpage.contents.add("<variable-set twin-was friends were>");
        playerShmebulockSubpage.contents.add("<set-magic-text true>");
        playerShmebulockSubpage.contents.add("<variable-set why-is-that SHMEBULOCK?>");
        playerShmebulockSubpage.contents.add("<variable-set eat-twin SHMEBULOCK?!>");
        playerShmebulockSubpage.contents.add("<variable-set thats-horrible SHMEBULOCK!!!>");
        playerShmebulockSubpage.contents.add("<inventory-add true Ring of Taming>");
        playerShmebulockSubpage.contents.add("<goto-scene Difficulty Selection>");
        page1.subpages.put("INPUT player=Shmebulock", playerShmebulockSubpage);
        Story shmebulockCheatSubpage = new Story();
        shmebulockCheatSubpage.isSpell = true;
        shmebulockCheatSubpage.contents.add("<variable-set summonShmebulock true>");
        shmebulockCheatSubpage.contents.add("<page-refresh>");
        page1.subpages.put("SHMEBULOCK", shmebulockCheatSubpage);
        Story inputSubpage = new Story();
        inputSubpage.contents.add("<get-validated-input player *Greyson+*Zara>");
        page1.subpages.put("input", inputSubpage);
        Story inputWithShmebulockSubpage = new Story();
        inputWithShmebulockSubpage.contents.add("<get-validated-input player *Greyson+*Zara+*Shmebulock>");
        page1.subpages.put("SHMEBULOCK input", inputWithShmebulockSubpage);
        playerSelection.pages.put("1", page1);
        
        Scene difficultySelection = new Scene();
        difficultySelection.firstPageName = "1";
        difficultySelection.hidePageHeaders = true;
        difficultySelection.stopOtherSounds = true;
        difficultySelection.soundFileName = "/assets/sounds/epic.mp3";
        opening.scenes.put("Difficulty Selection", difficultySelection);
        page1 = new Page();
        page1.story.contents.add("Select Difficulty:");
        page1.story.contents.add("<subpage-display condition=\"player=Shmebulock\" SHMEBULOCK input>");
        page1.story.contents.add("<subpage-display condition=\"player!=Shmebulock\" input>");
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
        inputSubpage.contents.add("<get-validated-input difficulty *Easy+*Normal+*Hard>");
        inputSubpage.contents.add("<second-page>");
        inputSubpage.contents.add("<image difficulty center /assets/images/difficulty.jpg>");
        page1.subpages.put("input", inputSubpage);
        inputWithShmebulockSubpage = new Story();
        inputWithShmebulockSubpage.contents.add("<get-validated-input difficulty *Magical>");
        inputWithShmebulockSubpage.contents.add("<second-page>");
        inputWithShmebulockSubpage.contents.add("<image difficulty-magical center /assets/images/difficulty-magical.jpg>");
        page1.subpages.put("SHMEBULOCK input", inputWithShmebulockSubpage);
        difficultySelection.pages.put("1", page1);
        
        Act introduction = new Act();
        introduction.firstSceneName = "Introduction";
        introduction.nextActName = "Chapter 1";
        book.acts.put("Introduction", introduction);
                
        Scene introScene = new Scene();
        introScene.firstPageName = "1a";
        introScene.hidePageHeaders = false;
        introScene.stopOtherSounds = true;
        introScene.soundFileName = "/assets/sounds/suspense.mp3";
        introduction.scenes.put("Introduction", introScene);

        Page page1a = new Page();
        page1a.nextPageName = "1";
        page1a.story.contents.add("<play-sound /assets/sounds/thunder.wav true>");
        page1a.story.contents.add("<i>Time has passed has passed by slowly.  The seconds have been monotonous.  Countless.  Like drops of rain in a storm that never ends.  And she has patiently waited.</i>");
        page1a.story.contents.add("<br>");
        page1a.story.contents.add("<i>But now in the twenty-first year of the twenty-first century, her long wait is finally over.</i>");
        page1a.story.contents.add("<br>");
        page1a.story.contents.add("<i>He has returned...</i>");
        page1a.story.contents.add("<br>");
        page1a.story.contents.add("<i><if condition=\"player=Shmebulock\" ...perhaps this time it will be different?></i>");
        page1a.story.contents.add("<second-page>");
        page1a.story.contents.add("<gif center /assets/images/cat-storm-large.gif>");
        introScene.pages.put("1a", page1a);
        
        page1 = new Page();
        page1.previousPageName = "1a";
        page1.nextPageName = "2";
        page1.story.contents.add("<stop-sound /assets/sounds/thunder.wav>");
        page1.story.contents.add("<variable twin-with-symbol>: <quote>Ahh!!!  <variable player>!!!  Save <if condition=\"player=Shmebulock\" us><if condition=\"player!=Shmebulock\" me>!!!<quote>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("<player-symbol> <variable player>: <quote><variable battle-cry><quote>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("<variable twin-with-symbol>: <quote><variable player> he got <if condition=\"player=Shmebulock\" us><if condition=\"player!=Shmebulock\" me>!!!  A big black cat got <if condition=\"player=Shmebulock\" us><if condition=\"player!=Shmebulock\" me>!!!<quote>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("You set down the game controller you were holding (and what a shame, you were about to beat the Ender Dragon) and run toward the sound of your <variable twin-voice> just in time to see the door to the leprechaun closet in the back bedroom slam shut.  You run to the closet door, open it, and what you see next takes your breath away...");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<image mystery-door center /assets/images/mystery-door.jpg>");
        introScene.pages.put("1", page1);
        
        Page page2 = new Page();
        page2.previousPageName = "1";
        page2.nextPageName = "3";
        page2.story.contents.add("Sticking your head into what was supposed to be a small closet you look around at what appears to be the surface of a cloud floating high above Pendleton, Indiana.  You wonder if this is perhaps a magic portal.");
        page2.story.contents.add("<br>");
        page2.story.contents.add("You cautiously tap the cloud with your hand and discover that it's firm enough to walk on.  You then oh so very carefully put one foot into the closet, lower your head, and step through the doorway so you can get a better look...");
        page2.story.contents.add("<second-page>");
        page2.story.contents.add("<image clouds center /assets/images/clouds.jpg>");
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
        page3.story.contents.add("<image clouds center /assets/images/clouds.jpg>");
        page3.story.contents.add("<second-page>");
        page3.story.contents.add("<br><br><br><br><br><br><br><br><br><br><br>");
        page3.story.contents.add("<image elevator-doors center /assets/images/elevator-doors.png>");
        introScene.pages.put("3", page3);
        
        Page page4 = new Page();
        page4.previousPageName = "3";
        page4.nextPageName = "5";
        page4.hidePreviousButton = true;
        page4.story.contents.add("<stop-sound>");
        page4.story.contents.add("<play-sound /assets/sounds/elevator-open.mp3 false>");
        page4.story.contents.add("<play-sound /assets/sounds/elevator.wav false>");
        page4.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Oh hey, <variable player-mylee-nickname>.<quote>");
        page4.story.contents.add("<br>");
        page4.story.contents.add("You're confused for a moment.  Inside the elevator there's a cat perched up on a shelf by the main elevator switch.  Is a cat operating this elevator?  And... did that cat just talk???");
        page4.story.contents.add("<br>");
        page4.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>You're wondering if I just talked even though I'm a cat.  And the answer is 'yes'.  Yes I did.<quote>");
        page4.story.contents.add("<br>");
        page4.story.contents.add("Wait, what?  Can this talking cat read your mind?");
        page4.story.contents.add("<br>");
        page4.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>And you're probably wondering now if I can read your mind.  The answer to that is... no.  <variable mylee-fandom><quote>");
        page4.story.contents.add("<br>");
        page4.story.contents.add("<variable mylee-reaction> is right.");
        page4.story.contents.add("<br>");
        page4.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Enough talk, <variable player-mylee-nickname>.  You look confused.  How about you ask me some questions.<quote>");
        page4.story.contents.add("<second-page>");
        page4.story.contents.add("<image mylee center /assets/images/mylee.jpg>");
        introScene.pages.put("4", page4);
        
        Page page5 = new Page();
        page5.previousPageName = "4";
        page5.hideNextButton = true;
        page5.noGlow = true;
        page5.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>You look like you're new to this so I'll help you out.  See those buttons below?  They're giving you some choices.  Pick one.  You can learn about this game before you begin your quest.<quote>");
        page5.story.contents.add("<br>");
        page5.story.contents.add("<subpage-display condition=\"player=Shmebulock\" SHMEBULOCK input>");
        page5.story.contents.add("<subpage-display condition=\"player!=Shmebulock\" input>");
        page5.story.contents.add("<second-page>");
        page5.story.contents.add("<image mylee center /assets/images/mylee.jpg>");
        introScene.pages.put("5", page5);
        inputSubpage = new Story();
        inputSubpage.contents.add("<get-validated-input mylee-prompt Who are you?+Where is my twin?+Who is Big Chung?+What's with this book I have?+And this map?+What is this elevator?+*I'm good.>");
        page5.subpages.put("input", inputSubpage);
        inputWithShmebulockSubpage = new Story();
        inputWithShmebulockSubpage.contents.add("<get-validated-input mylee-prompt SHMEBULOCK?+SHMEBULOCK??+SHMEBULOCK???+SHMEBULOCK????+SHMEBULOCK?????+SHMEBULOCK??????+*SHMEBULOCK.>");
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
        Story whatsWithSpellbookSubpage = new Story();
        whatsWithSpellbookSubpage.contents.add("<goto-page 9>");
        page5.subpages.put("INPUT mylee-prompt=What's with this book I have?", whatsWithSpellbookSubpage);
        Story andThisMapSubpage = new Story();
        andThisMapSubpage.contents.add("<goto-page 10>");
        page5.subpages.put("INPUT mylee-prompt=And this map?", andThisMapSubpage);
        Story whatIsThisElevatorSubpage = new Story();
        whatIsThisElevatorSubpage.contents.add("<goto-page 11>");
        page5.subpages.put("INPUT mylee-prompt=What is this elevator?", whatIsThisElevatorSubpage);
        Story imGoodSubpage = new Story();
        imGoodSubpage.contents.add("<goto-page 12>");
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
        page5.subpages.put("INPUT mylee-prompt=SHMEBULOCK????", schmebulock4Subpage);
        Story schmebulock5Subpage = new Story();
        schmebulock5Subpage.contents.add("<goto-page 10>");
        page5.subpages.put("INPUT mylee-prompt=SHMEBULOCK?????", schmebulock5Subpage);
        Story schmebulock6Subpage = new Story();
        schmebulock6Subpage.contents.add("<goto-page 11>");
        page5.subpages.put("INPUT mylee-prompt=SHMEBULOCK??????", schmebulock6Subpage);
        Story schmebulock7Subpage = new Story();
        schmebulock7Subpage.contents.add("<goto-page 12>");
        page5.subpages.put("INPUT mylee-prompt=SHMEBULOCK.", schmebulock7Subpage);
        
        Page page6 = new Page();
        page6.previousPageName = "5";
        page6.hideNextButton = true;
        page6.story.contents.add("<player-symbol> YOU: <quote><variable mylee-prompt><quote>");
        page6.story.contents.add("<br>");
        page6.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>I'm Mylee Marie!<quote>");
        page6.story.contents.add("<br>");
        page6.story.contents.add("<player-symbol> YOU: <quote><if condition=\"player=Shmebulock\" SHMEBULOCK?><if condition=\"player!=Shmebulock\" And... ?><quote>");
        page6.story.contents.add("<br>");
        page6.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote><if condition=\"player=Shmebulock\" And I'm magical just like you.><if condition=\"player!=Shmebulock\" And that's all you need to know for now.><quote>");
        page6.story.contents.add("<br>");
        page6.story.contents.add("...");
        page6.story.contents.add("<br>");
        page6.story.contents.add("You wait a moment for Mylee to say more but she doesn't.  Okay... at least you got her name.  Perhaps it's time to ask another question?");
        page6.story.contents.add("<second-page>");
        page6.story.contents.add("<image mylee center /assets/images/mylee.jpg>");
        introScene.pages.put("6", page6);
        
        Page page7 = new Page();
        page7.previousPageName = "5";
        page7.hideNextButton = true;
        page7.story.contents.add("<player-symbol> YOU: <quote><variable mylee-prompt><quote>");
        page7.story.contents.add("<br>");
        page7.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Oh you poor <variable player-mylee-nickname>.  I'm afraid your <variable twin-was> captured by my younger brother, Big Chung.  It is a shame.<quote>");
        page7.story.contents.add("<br>");
        page7.story.contents.add("<player-symbol> YOU: <quote><variable why-is-that><quote>");
        page7.story.contents.add("<br>");
        page7.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>You'd have to understand my brother to know why that is...<quote>");
        page7.story.contents.add("<second-page>");
        page7.story.contents.add("<image mylee center /assets/images/mylee.jpg>");
        introScene.pages.put("7", page7);
        
        Page page8 = new Page();
        page8.previousPageName = "5";
        page8.hideNextButton = true;
        page8.story.contents.add("<player-symbol> YOU: <quote><variable mylee-prompt><quote>");
        page8.story.contents.add("<br>");
        page8.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Oh he's quite the cat!  His appetite is as big as they come.  Some day he may eat the whole world.  (He's done it before.)  But for now it seems like he's settled on <variable twin>.<quote>");
        page8.story.contents.add("<br>");
        page8.story.contents.add("<player-symbol> YOU: <quote><variable eat-twin><quote>");
        page8.story.contents.add("<br>");
        page8.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Perhaps.  But you might have some time.  He just ate an entire Red Lobster restaurant for breakfast this morning so he might be full.  I bet <variable twin> will be an appetizer for later.<quote>");
        page8.story.contents.add("<br>");
        page8.story.contents.add("<player-symbol> YOU: <quote><variable thats-horrible><quote>");
        page8.story.contents.add("<br>");
        page8.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Don't I know it, <variable player-mylee-nickname>.  But he's my younger brother so we need to stop him in a way that doesn't hurt him.  Were I to fight him directly... well, let's just say he wouldn't walk away from that!  That's why I need you to help me.  You're clever, but, not exactly a tough cat like me...<quote>");
        page8.story.contents.add("<second-page>");
        page8.story.contents.add("<image mylee center /assets/images/mylee.jpg>");
        introScene.pages.put("8", page8);
        
        Page page9 = new Page();
        page9.previousPageName = "5";
        page9.hideNextButton = true;
        page9.story.contents.add("<player-symbol> YOU: <quote><variable mylee-prompt><quote>");
        page9.story.contents.add("<br>");
        page9.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>That <inventory Spell Book> tab up above?  Every brave soul that takes on a quest is equipped with one of those.  Any spell that you write into the book will be cast with the book's magic.<quote>");
        page9.story.contents.add("<br>");
        page9.story.contents.add("<player-symbol> YOU: <quote><if condition=\"player=Shmebulock\" SHMEBULOCK.><if condition=\"player!=Shmebulock\" So I can cast spells and stuff?><quote>");
        page9.story.contents.add("<br>");
        page9.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote><if condition=\"player=Shmebulock\" I know you can already cast spells.  But with this magical book, you'll be able to magnify your power and cast spells that affect the very fabric of reality.><if condition=\"player!=Shmebulock\" With the book, yes.  But you need to learn some spells first before you can write them into the book.>  Tell you what, before each level I'll share with you a magic spell.  As your quest progresses, so will the spells that you know.<quote>");
        page9.story.contents.add("<br>");
        page9.story.contents.add("<player-symbol> YOU: <quote><if condition=\"player=Shmebulock\" SHMEBULOCK!><if condition=\"player!=Shmebulock\" That sounds cool!><quote>");
        page9.story.contents.add("<br>");
        page9.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Indeed.  You'll find me to be quite helpful by the time you get to the end of your quest.  Here's your first spell... 'FLIP BOOK' (requires 0MP).  Go to the <inventory Spell Book> and try it out!  This one isn't very powerful but there's no limit to the number of times you can cast it.<quote>");
        page9.story.contents.add("<second-page>");
        page9.story.contents.add("<image mylee center /assets/images/mylee.jpg>");
        introScene.pages.put("9", page9);
        
        Page page10 = new Page();
        page10.previousPageName = "5";
        page10.hideNextButton = true;
        page10.story.contents.add("<player-symbol> YOU: <quote><variable mylee-prompt><quote>");
        page10.story.contents.add("<br>");
        page10.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>That <inventory Map> tab up above?  It's another essential item for your quest.  Every location for the current level will be shown.  But only the locations that you've actually seen will have any information.<quote>");
        page10.story.contents.add("<br>");
        page10.story.contents.add("<player-symbol> YOU: <quote><if condition=\"player=Shmebulock\" SHMEBULOCK?><if condition=\"player!=Shmebulock\" So it will show me what's out there... but not everything until I've done some exploring?><quote>");
        page10.story.contents.add("<br>");
        page10.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Precisely.  More or less it's so you don't get lost.  But you may find some additional value to it.<quote>");
        page10.story.contents.add("<br>");
        page10.story.contents.add("<player-symbol> YOU: <quote><if condition=\"player=Shmebulock\" SHMEBULOCK?><if condition=\"player!=Shmebulock\" Is there anything else I need to know?><quote>");
        page10.story.contents.add("<br>");
        page10.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>It would certainly help to look before you leap!  Think carefully before entering a new location that looks like it could be dangerous.<quote>");
        page10.story.contents.add("<second-page>");
        page10.story.contents.add("<image mylee center /assets/images/mylee.jpg>");
        introScene.pages.put("10", page10);
        
        Page page11 = new Page();
        page11.previousPageName = "5";
        page11.hideNextButton = true;
        page11.story.contents.add("<player-symbol> YOU: <quote><variable mylee-prompt><quote>");
        page11.story.contents.add("<br>");
        page11.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>This is my elevator!  And I'm quite proud of it.  It's solar-powered and emits zero greenhouse gases.<quote>");
        page11.story.contents.add("<br>");
        page11.story.contents.add("<player-symbol> YOU: <quote><if condition=\"player=Shmebulock\" SHMEBULOCK?><if condition=\"player!=Shmebulock\" But I mean, what does it do?  Where does it go?><quote>");
        page11.story.contents.add("<br>");
        page11.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Beyond these elevator doors lies a magical world.  If you want to save <variable twin>, gain some experience out there and then come back.  If you bring me some <inventory Gold> I'll take you to the next level.  There are seven levels in all and Level 7 is where Big Chung hangs out.  But be careful!  Even though the first level is the easiest, a creature named Night Owl and a fierce Dragon both like <inventory Gold>.  If you remove any from their level they might come after you.  Slay that Dragon if you can and ask my friend Gianni for help with that Night Owl problem.  Oh and don't you dare try to fight Night Owl in a dark place.  He'll swoop down on you before you have a chance to defend yourself!<quote>");
        page11.story.contents.add("<br>");
        page11.story.contents.add("<player-symbol> YOU: <quote><if condition=\"player=Shmebulock\" SHMEBULOCK?><if condition=\"player!=Shmebulock\" So this is going to be dangerous?><quote>");
        page11.story.contents.add("<br>");
        page11.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Very!  But if you keep your wits about you then a <if condition=\"player=Shmebulock\" magical one><if condition=\"player!=Shmebulock\" clever kid> like yourself should do just fine.<quote>");
        page11.story.contents.add("<second-page>");
        page11.story.contents.add("<image mylee center /assets/images/mylee.jpg>");
        introScene.pages.put("11", page11);
        
        Page page12 = new Page();
        page12.previousPageName = "5";
        page12.story.contents.add("<player-symbol> YOU: <quote><variable mylee-prompt><quote>");
        page12.story.contents.add("<br>");
        page12.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Nothing else, huh.  I'll open the magic elevator doors for you.  Just march on out and start exploring the first level.  Remember: Don't die.  And... bring me back some <inventory Gold>!!!<quote>");
        page12.story.contents.add("<br>");
        page12.story.contents.add("Mylee flips the elevator switch and the doors open.  You walk out and find that you're no longer on the cloud...");
        page12.story.contents.add("<second-page>");
        page12.story.contents.add("<image mylee center /assets/images/mylee.jpg>");
        introScene.pages.put("12", page12);

        Act chapter1 = new Act();
        chapter1.firstSceneName = "Chapter";
        book.acts.put("Chapter 1", chapter1);
        
        Story sceneHeaderSubpage = new Story();
        sceneHeaderSubpage.contents.add("<subpage-display Player Stats>");
        sceneHeaderSubpage.contents.add("You are in <scene>.");
        sceneHeaderSubpage.contents.add("Ahead (<player-direction>) you see <next-scene>.");
        book.subpages.put("Scene Header", sceneHeaderSubpage);
        
        Story playerStatsSubpage = new Story();
        playerStatsSubpage.contents.add("<first-page><player-symbol> <u><hp> HP   <mp> MP   <xp> XP</u>");
        book.subpages.put("Player Stats", playerStatsSubpage);
        
        Story navigationFooterSubpage = new Story();
        navigationFooterSubpage.contents.add("<variable-set nextScene next-scene>");
        navigationFooterSubpage.contents.add("<button-row>");
        navigationFooterSubpage.contents.add("<get-validated-input condition=\"next-scene=EDGE OF THE WORLD\" align=right navigation-prompt &left; Turn Left+!&up; Move Ahead+&right; Turn Right>");
        navigationFooterSubpage.contents.add("<get-validated-input condition=\"next-scene!=EDGE OF THE WORLD\" align=right navigation-prompt &left; Turn Left+&up; Move Ahead+&right; Turn Right>");
        book.subpages.put("Navigation Footer", navigationFooterSubpage);
        
        Story inputTurnLeftSubpage = new Story();
        inputTurnLeftSubpage.contents.add("<turn-left>");
        book.subpages.put("INPUT navigation-prompt= Turn Left", inputTurnLeftSubpage);
        
        Story inputMoveAheadSubpage = new Story();
        inputMoveAheadSubpage.contents.add("<move-ahead>");
        book.subpages.put("INPUT navigation-prompt= Move Ahead", inputMoveAheadSubpage);

        Story inputTurnRightSubpage = new Story();
        inputTurnRightSubpage.contents.add("<turn-right>");
        book.subpages.put("INPUT navigation-prompt= Turn Right", inputTurnRightSubpage);
        
        Scene chapterScene = new Scene();
        chapterScene.firstPageName = "1";
        chapterScene.hidePageHeaders = true;
        chapterScene.nextSceneName = "WILDERNESS 1";
        chapterScene.stopOtherSounds = true;
        chapterScene.soundFileName = "/assets/sounds/elevator-open.mp3";
        chapter1.scenes.put("Chapter", chapterScene);
        
        page1 = new Page();
        page1.story.contents.add("<image wayne-chung-dragon center /assets/images/wayne-chung-dragon.jpg>");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<u>CHAPTER 1</u>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("A Dragon in the Kingdom");
        page1.story.contents.add("<set-player-direction SOUTH>");
        page1.story.contents.add("<observed-scene-add MYLEE'S ELEVATOR>");
        chapterScene.pages.put("1", page1);
        
        Scene myleesElevator = new Scene();
        myleesElevator.color = new Color(192, 192, 192);    // Silver
        myleesElevator.firstPageName = "main";
        myleesElevator.stopOtherSounds = true;
        myleesElevator.soundFileName = "/assets/sounds/elevator.wav";
        myleesElevator.symbol = "\uD83D\uDED7";
        myleesElevator.x = 2;
        myleesElevator.y = 0;
        chapter1.scenes.put("MYLEE'S ELEVATOR", myleesElevator);
        
        // TODO - If inventory does not have Gold, you catch Mylee taking a bath and she asks you to leave
        // TODO - If dragon is not dead, Dragon comes crashing into the elevator and kills you
        // TODO - If Gianni isn't tamed (no long range weapon), Night Owl comes crashing into the elevator and kills you
        // TODO - Else, start the Night Owl minigame
        Page mainPage = new Page();
        mainPage.story.contents.add("<subpage-display condition=\"inventory-has Gold!=true\" No Gold><subpage-display condition=\"inventory-has Gold=true\" Has Gold>");
        myleesElevator.pages.put("main", mainPage);
        
        Story noGoldSubpage = new Story();
        noGoldSubpage.contents.add("<subpage-display Scene Header>");
        noGoldSubpage.contents.add("<color 0+0+0>");
        noGoldSubpage.contents.add("You return to the elevator and what you see surprises you...");
        noGoldSubpage.contents.add("<br>");
        noGoldSubpage.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Hey!  I'm trying to take a bath!  Off with you <variable player-mylee-nickname>!!!  And don't you come back until you have some <inventory Gold>!<quote>");
        noGoldSubpage.contents.add("<br>");
        noGoldSubpage.contents.add("So this elevator has a sink?  Does she live here?");
        noGoldSubpage.contents.add("<br>");
        noGoldSubpage.contents.add("You have so many questions.  But for now, you better leave.");
        noGoldSubpage.contents.add("<br></color>");
        noGoldSubpage.contents.add("<get-validated-input action Listen+&down; Leave Elevator>");
        noGoldSubpage.contents.add("<second-page>");
        noGoldSubpage.contents.add("<image mylee-sink center /assets/images/mylee-sink.jpg>");
        mainPage.subpages.put("No Gold", noGoldSubpage);
        
        Story hasGoldSubpage = new Story();
        hasGoldSubpage.contents.add("<subpage-display condition=\"is-dragon-defeated!=true\" Dragon Attack><subpage-display condition=\"is-dragon-defeated=true\" Dragon Defeated>");
        mainPage.subpages.put("Has Gold", hasGoldSubpage);
        
        Story dragonAttackSubpage = new Story();
        dragonAttackSubpage.contents.add("<color 0+0+0><play-sound /assets/sounds/banging-door.mp3 true><play-sound /assets/sounds/heavy-wings.mp3 true>You proudly step back into the elevator with your hard-earned <inventory Gold>.");
        dragonAttackSubpage.contents.add("<br>");
        dragonAttackSubpage.contents.add("However, Mylee does NOT look happy.");
        dragonAttackSubpage.contents.add("<br>");
        dragonAttackSubpage.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Please tell me you took care of the Dragon before coming back here with that gold!!!<quote>");
        dragonAttackSubpage.contents.add("<br>");
        dragonAttackSubpage.contents.add("<player-symbol> YOU: <quote>Oh, right.  Yeah about that...<quote>");
        dragonAttackSubpage.contents.add("<br>");
        dragonAttackSubpage.contents.add("You turn towards the elevator doors where a loud banging sound is coming from the outside.  It seems that something wants in and it's going to break through.");
        dragonAttackSubpage.contents.add("<br></color");
        dragonAttackSubpage.contents.add("<get-validated-input action *Brace Yourself>");
        dragonAttackSubpage.contents.add("<second-page>");
        dragonAttackSubpage.contents.add("<image mylee center /assets/images/mylee.jpg>");
        mainPage.subpages.put("Dragon Attack", dragonAttackSubpage);
        
        Story braceYourselfSubpage = new Story();
        braceYourselfSubpage.contents.add("<goto-page Dragon>");
        mainPage.subpages.put("INPUT action=Brace Yourself", braceYourselfSubpage);
        
        Page dragonPage = new Page();
        dragonPage.story.contents.add("<color 0+0+0><hp-change -100 false a dragon><play-sound /assets/sounds/dragon.mp3 false>The elevator door gives way and you see a rather angry-looking dragon.  With nowhere to hide, the dragon grabs you in his claws.");
        dragonPage.story.contents.add("<br>");
        dragonPage.story.contents.add("Maybe next time you'll take care of the dragon before trying to leave with its gold?</color");
        dragonPage.story.contents.add("<second-page>");
        dragonPage.story.contents.add("<image clouds center /assets/images/clouds.jpg>");
        dragonPage.story.contents.add("<second-page>");
        dragonPage.story.contents.add("<image dragon center /assets/images/dragon.png>");
        myleesElevator.pages.put("Dragon", dragonPage);
        
        Story dragonDefeatedSubpage = new Story();
        dragonDefeatedSubpage.contents.add("<subpage-display condition=\"is-Gianni-tamed!=true\" Night Owl Attack><subpage-display condition=\"is-Gianni-tamed=true\" Night Owl Minigame>");
        mainPage.subpages.put("Dragon Defeated", dragonDefeatedSubpage);
        
        Story nightOwlAttackSubpage = new Story();
        nightOwlAttackSubpage.contents.add("");
        nightOwlAttackSubpage.contents.add("<color 0+0+0><play-sound /assets/sounds/banging-door.mp3 true><play-sound /assets/sounds/heavy-wings.mp3 true>You proudly step back into the elevator with your hard-earned <inventory Gold>.");
        nightOwlAttackSubpage.contents.add("<br>");
        nightOwlAttackSubpage.contents.add("However, Mylee does NOT look happy.");
        nightOwlAttackSubpage.contents.add("<br>");
        nightOwlAttackSubpage.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Please tell me you found a long-range weapon before coming back here with that gold!!!<quote>");
        nightOwlAttackSubpage.contents.add("<br>");
        nightOwlAttackSubpage.contents.add("<player-symbol> YOU: <quote>Oh, right.  Yeah I knew I was forgetting something...<quote>");
        nightOwlAttackSubpage.contents.add("<br>");
        nightOwlAttackSubpage.contents.add("You turn towards the elevator doors where a loud banging sound is coming from the outside.  It seems that something wants in and it's going to break through.");
        nightOwlAttackSubpage.contents.add("<br></color");
        nightOwlAttackSubpage.contents.add("<get-validated-input action *Hold On Tight>");
        nightOwlAttackSubpage.contents.add("<second-page>");
        nightOwlAttackSubpage.contents.add("<image mylee center /assets/images/mylee.jpg>");
        mainPage.subpages.put("Night Owl Attack", nightOwlAttackSubpage);
        
        Story holdOnTightSubpage = new Story();
        holdOnTightSubpage.contents.add("<goto-page Night Owl>");
        mainPage.subpages.put("INPUT action=Hold On Tight", holdOnTightSubpage);
        
        Page nightOwlPage = new Page();
        nightOwlPage.story.contents.add("<color 0+0+0><hp-change -100 false Night Owl><play-sound /assets/sounds/hooting.mp3 true>The elevator door gives way and you see a rather angry-looking Night Owl.  With nowhere to hide, Night Owl grabs you in his talons.");
        nightOwlPage.story.contents.add("<br>");
        nightOwlPage.story.contents.add("Maybe next time you'll talk to Gianni about a long-range weapon?</color");
        nightOwlPage.story.contents.add("<second-page>");
        nightOwlPage.story.contents.add("<image clouds center /assets/images/clouds.jpg>");
        nightOwlPage.story.contents.add("<second-page>");
        nightOwlPage.story.contents.add("<image night-owl center /assets/images/night-owl.png>");
        myleesElevator.pages.put("Night Owl", nightOwlPage);
        
        Story nightOwlMinigameSubpage = new Story();
        nightOwlMinigameSubpage.contents.add("<color 0+0+0><play-sound /assets/sounds/banging-door.mp3 true><play-sound /assets/sounds/heavy-wings.mp3 true>You proudly step back into the elevator with your hard-earned <inventory Gold>.");
        nightOwlMinigameSubpage.contents.add("<br>");
        nightOwlMinigameSubpage.contents.add("However, Mylee does NOT look happy.");
        nightOwlMinigameSubpage.contents.add("<br>");
        nightOwlMinigameSubpage.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Please tell me you found a long-range weapon before coming back here with that gold!!!<quote>");
        nightOwlMinigameSubpage.contents.add("<br>");
        nightOwlMinigameSubpage.contents.add("<player-symbol> YOU: <quote>Of course!  Gianni gave me this rather impressive <inventory condition=\"player=Greyson\" Greyson's Great Bow><inventory condition=\"player=Zara\" Cat-apult><inventory condition=\"player=Shmebulock\" Faery Launcher>.<quote>");
        nightOwlMinigameSubpage.contents.add("<br>");
        nightOwlMinigameSubpage.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Good because I can sense that my brother is coming down the stairwell.  You take care of the beast that's on the other side of those doors and I'll take care of Big Chung.<quote>");
        nightOwlMinigameSubpage.contents.add("<br>");
        nightOwlMinigameSubpage.contents.add("There are two things to worry about?  You turn towards the elevator doors where a loud banging sound is coming from the outside.  It seems that something wants in and it's going to break through unless you do something.");
        nightOwlMinigameSubpage.contents.add("<br>");
        nightOwlMinigameSubpage.contents.add("If it's not Big Chung, what could be banging on the doors?");
        nightOwlMinigameSubpage.contents.add("<br></color>");
        nightOwlMinigameSubpage.contents.add("<get-validated-input action *Open Elevator Doors and Attack>");
        nightOwlMinigameSubpage.contents.add("<second-page>");
        nightOwlMinigameSubpage.contents.add("<image mylee center /assets/images/mylee.jpg>");
        mainPage.subpages.put("Night Owl Minigame", nightOwlMinigameSubpage);
        
        Story kickSomeTailFeathersSubpage = new Story();
        kickSomeTailFeathersSubpage.contents.add("<variable-set minigame-display false><animation-init><stop-sound><variable-set night-owl-hp 100><play-sound /assets/sounds/elevator-open.mp3 false><play-sound /assets/sounds/heavy-wings.mp3 false><play-sound /assets/sounds/boss-battle.mp3 true><goto-page Night Owl Minigame>");
        mainPage.subpages.put("INPUT action=Open Elevator Doors and Attack", kickSomeTailFeathersSubpage);
        
        // TODO - Reimplement MonsterShooter in TQ.BAS
        // TODO - Add spell for going straight to this after selecting your player
        // TODO - How to know that the page needs to be refreshed when the player is hit and hp needs to be updated?
        Page nightOwlMinigamePage = new Page();
        nightOwlMinigamePage.hideNextButton = true;
        nightOwlMinigamePage.story.contents.add("<second-page>");
        nightOwlMinigamePage.story.contents.add("<if condition=\"variable minigame-display!=true\" BOSS: Night Owl>");
        nightOwlMinigamePage.story.contents.add("<if condition=\"variable minigame-display!=true\" LEVEL: 1>");
        nightOwlMinigamePage.story.contents.add("<if condition=\"variable minigame-display!=true\" ATTACK: Lightning>");
        nightOwlMinigamePage.story.contents.add("<if condition=\"variable minigame-display!=true\" STRENGTHS: Immune to all close-range attacks>");
        nightOwlMinigamePage.story.contents.add("<if condition=\"variable minigame-display!=true\" WEAKNESSES: Long-range attacks>");
        nightOwlMinigamePage.story.contents.add("<br condition=\"variable minigame-display!=true\">");
        nightOwlMinigamePage.story.contents.add("<image condition=\"variable minigame-display!=true\" night-owl-sketch center /assets/images/night-owl-sketch.png>");
        nightOwlMinigamePage.story.contents.add("<first-page><color 0+0+0>\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Remember the name of this boss.  If you fail to defeat it, you can cast its name as a spell to fight it again.<quote>");
        nightOwlMinigamePage.story.contents.add("<br>");
        nightOwlMinigamePage.story.contents.add("You open the elevator doors and wave your weapon in the face of none other than Night Owl himself.  Night Owl sees immediately that you have a weapon worthy of battle and flies up into the sky to begin an aerial assault.");
        nightOwlMinigamePage.story.contents.add("<br>");
        nightOwlMinigamePage.story.contents.add("This is it!  It's time to fight!!!");
        nightOwlMinigamePage.story.contents.add("<br>");
        nightOwlMinigamePage.story.contents.add("<i>INSTRUCTIONS: Use the buttons or arrow keys to move left or right and launch an upwards attack.</i>");
        nightOwlMinigamePage.story.contents.add("<br>");
        nightOwlMinigamePage.story.contents.add("<get-validated-input condition=\"variable minigame-display!=true\" action *Continue>");
        nightOwlMinigamePage.story.contents.add("</color>");
        nightOwlMinigamePage.story.contents.add("<second-page>");
        nightOwlMinigamePage.story.contents.add("<subpage-display condition=\"variable minigame-display=true\" Monster Shooter>");
        nightOwlMinigamePage.story.contents.add("<subpage-display condition=\"variable animation-complete=true\" Victory>");
        myleesElevator.pages.put("Night Owl Minigame", nightOwlMinigamePage);
        
        Story monsterShooterSubpage = new Story();
        monsterShooterSubpage.contents.add("<subpage-display condition=\"player=Greyson\" Monster Shooter Greyson>");
        monsterShooterSubpage.contents.add("<subpage-display condition=\"player=Zara\" Monster Shooter Zara>");
        // TODO - Add SHMEBULOCK
        nightOwlMinigamePage.subpages.put("Monster Shooter", monsterShooterSubpage);
        
        Story monsterShooterGreysonSubpage = new Story();
        monsterShooterGreysonSubpage.contents.add("<monster-shooter condition=\"variable animation-on!=true\" night-owl left /assets/images/wilderness3.jpg /assets/images/wizard-back-facing-left.png /assets/images/wizard-back-facing-right.png /assets/images/arrow.png /assets/images/arrow.png /assets/sounds/arrow.mp3 /assets/images/flying-owl-facing-left.png /assets/images/flying-owl-facing-right.png /assets/images/lightning-bolt-left.png /assets/images/lightning-bolt-right.png /assets/sounds/zap.wav /assets/sounds/hooting.mp3 true difficulty>");
        nightOwlMinigamePage.subpages.put("Monster Shooter Greyson", monsterShooterGreysonSubpage);
        
        Story monsterShooterZaraSubpage = new Story();
        monsterShooterZaraSubpage.contents.add("<monster-shooter condition=\"variable animation-on!=true\" night-owl left /assets/images/wilderness3.jpg /assets/images/witch-back-facing-left.png /assets/images/witch-back-facing-right.png /assets/images/cat-missile-left.png /assets/images/cat-missile-right.png /assets/sounds/arrow.mp3 /assets/images/flying-owl-facing-left.png /assets/images/flying-owl-facing-right.png /assets/images/lightning-bolt-left.png /assets/images/lightning-bolt-right.png /assets/sounds/zap.wav /assets/sounds/hooting.mp3 true difficulty>");
        nightOwlMinigamePage.subpages.put("Monster Shooter Zara", monsterShooterZaraSubpage);
        
        Story continueSubpage = new Story();
        continueSubpage.contents.add("<variable-set minigame-display true>");
        continueSubpage.contents.add("<page-refresh>");
        nightOwlMinigamePage.subpages.put("INPUT action=Continue", continueSubpage);

        Story leaveElevatorSubpage = new Story();
        leaveElevatorSubpage.contents.add("<move-back>");
        mainPage.subpages.put("INPUT action= Leave Elevator", leaveElevatorSubpage);
        
        Story victorySubpage = new Story();
        victorySubpage.contents.add("<play-sound condition=\"hp&gt;0\" /assets/sounds/victory.mp3 false>");
        victorySubpage.contents.add("<xp-change condition=\"hp&gt;0\" 100 false>");
        victorySubpage.contents.add("<goto-page condition=\"hp&gt;0\" The Eyes Of Chung>");
        nightOwlMinigamePage.subpages.put("Victory", victorySubpage);
        
        Story listenSubpage = new Story();
        listenSubpage.contents.add("<goto-page Listen>");
        mainPage.subpages.put("INPUT action=Listen", listenSubpage);
        Page listenPage = new Page();
        listenPage.previousPageName = "main";
        listenPage.story.contents.add("<color 0+0+0>You hear the typical sounds of an elevator.");
        listenPage.story.contents.add("<br>");
        listenPage.story.contents.add("The music is surprisingly good and uplifting.  Mylee must have picked it herself.");
        listenPage.story.contents.add("</color>");
        listenPage.story.contents.add("<second-page>");
        listenPage.story.contents.add("<image mylee-sink left /assets/images/mylee-sink.jpg>");
        myleesElevator.pages.put("Listen", listenPage);
        
        Page eyesOfChungPage = new Page();
        eyesOfChungPage.hideNextButton = true;
        eyesOfChungPage.nextPageName = "Back To Elevator";
        eyesOfChungPage.story.contents.add("<second-page>");
        eyesOfChungPage.story.contents.add("<image wilderness3 left /assets/images/wilderness3.jpg>");
        eyesOfChungPage.story.contents.add("<first-page><subpage-display Scene Header>");
        eyesOfChungPage.story.contents.add("<color 0+0+0><remove lights-out><overlay lights-out 0+0+0 false><send-to-front VARIABLE:action>");
        eyesOfChungPage.story.contents.add("You did it!  You defeated Night Owl!!!  With great speed he flies back to his woods for safety.");
        eyesOfChungPage.story.contents.add("<br>");
        eyesOfChungPage.story.contents.add("You look around for Mylee and don't see her.  Perhaps she returned to the elevator?");
        eyesOfChungPage.story.contents.add("<br>");
        eyesOfChungPage.story.contents.add("And then... Wait.  Is the sky turning dark?  What's going on???");
        eyesOfChungPage.story.contents.add("<br>");
        eyesOfChungPage.story.contents.add("You run back into the elevator and right before the doors close you see... his eyes.");
        eyesOfChungPage.story.contents.add("<get-validated-input align=left action *Brace Yourself>");
        eyesOfChungPage.story.contents.add("</color>");
        myleesElevator.pages.put("The Eyes Of Chung", eyesOfChungPage);
                
        Story eyesOfChungSubpage = new Story();
        eyesOfChungSubpage.contents.add("<play-sound /assets/sounds/ominous.wav false>");
        eyesOfChungSubpage.contents.add("<timer-start 6 ominous>");
        eyesOfChungSubpage.contents.add("<overlay eyes 0+0+0 false>");
        eyesOfChungPage.subpages.put("INPUT action=Brace Yourself", eyesOfChungSubpage);
        
        Story showEyesSubpage = new Story();
        showEyesSubpage.contents.add("<timer-stop ominous>");
        showEyesSubpage.contents.add("<image cat-eyes fill /assets/images/cat-eyes.gif>");
        showEyesSubpage.contents.add("<timer-start 6 eyes>");
        eyesOfChungPage.subpages.put("TIMER ominous", showEyesSubpage);

        Story removeEyesSubpage = new Story();
        removeEyesSubpage.contents.add("<timer-stop eyes>");
        removeEyesSubpage.contents.add("<remove eyes>");
        removeEyesSubpage.contents.add("<remove cat-eyes>");
        removeEyesSubpage.contents.add("<timer-start 2 coast-is-clear>");
        eyesOfChungPage.subpages.put("TIMER eyes", removeEyesSubpage);
        
        Story coastIsClearSubpage = new Story();
        coastIsClearSubpage.contents.add("<timer-stop coast-is-clear>");
        coastIsClearSubpage.contents.add("<remove coast-is-clear>");
        coastIsClearSubpage.contents.add("<goto-page Back To Elevator>");
        eyesOfChungPage.subpages.put("TIMER coast-is-clear", coastIsClearSubpage);
        
        Page backToElevatorPage = new Page();
        backToElevatorPage.nextPageName = "Give The Mylee Her Due";
        backToElevatorPage.story.contents.add("<second-page>");
        backToElevatorPage.story.contents.add("<image mylee left /assets/images/mylee.jpg>");
        backToElevatorPage.story.contents.add("<first-page><subpage-display Scene Header><play-sound /assets/sounds/elevator-open.mp3 false><timer-stop lights-off><timer-stop lights-on><remove lights-out><overlay lights-out 0+0+0 false><send-to-front next-page><send-to-front VARIABLE:action><timer-start 1.0 lights-on>");
        backToElevatorPage.story.contents.add("<color 0+0+0>");
        backToElevatorPage.story.contents.add("Back in the elevator you find a panic-stricken Mylee.");
        backToElevatorPage.story.contents.add("<br>");
        backToElevatorPage.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Quick!  We need to get out of here!<quote>");
        backToElevatorPage.story.contents.add("<br>");
        backToElevatorPage.story.contents.add("<player-symbol> YOU: <quote>Why?  Is Big Chung coming after us?<quote>");
        backToElevatorPage.story.contents.add("<br>");
        backToElevatorPage.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>No, but in order to distract him while you battled Night Owl I... may have lectured him on his weight.  And because I told him that he needs to go on a <i>light diet</i>, he's now busy devouring all light in the seven kingdoms!<quote>");
        backToElevatorPage.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: he's now busy devouring all light in the seven kingdoms!<quote>");
        backToElevatorPage.story.contents.add("<br>");
        backToElevatorPage.story.contents.add("<player-symbol> YOU: <quote>So because he took you literally he now... eating <i>light</i>?<quote>");
        backToElevatorPage.story.contents.add("<br>");
        backToElevatorPage.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Yes!  And at an alarming speed!<quote>");
        backToElevatorPage.story.contents.add("<br>");
        backToElevatorPage.story.contents.add("<player-symbol> YOU: <quote>What does that even mean?<quote>");
        backToElevatorPage.story.contents.add("<br>");
        backToElevatorPage.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>It means that my solar-powered elevator that we're both in is about to run out of energy!!!<quote>");
        backToElevatorPage.story.contents.add("<br>");
        backToElevatorPage.story.contents.add("</color>");
        myleesElevator.pages.put("Back To Elevator", backToElevatorPage);
        
        Story lightsOnSubpage = new Story();
        lightsOnSubpage.contents.add("<remove lights-out>");
        lightsOnSubpage.contents.add("<timer-stop lights-on>");
        lightsOnSubpage.contents.add("<timer-start 1.0 lights-out>");
        backToElevatorPage.subpages.put("TIMER lights-on", lightsOnSubpage);
        
        Story lightsOutSubpage = new Story();
        lightsOutSubpage.contents.add("<overlay lights-out 0+0+0 false>");
        lightsOutSubpage.contents.add("<send-to-front next-page>");
        lightsOutSubpage.contents.add("<send-to-front VARIABLE:action>");
        lightsOutSubpage.contents.add("<timer-stop lights-out>");
        lightsOutSubpage.contents.add("<timer-start 1.0 lights-on>");
        backToElevatorPage.subpages.put("TIMER lights-out", lightsOutSubpage);
        
        Page giveMyleeHerDuePage = new Page();
        giveMyleeHerDuePage.hideNextButton = true;
        giveMyleeHerDuePage.story.contents.add("<second-page>");
        giveMyleeHerDuePage.story.contents.add("<image mylee left /assets/images/mylee.jpg>");
        giveMyleeHerDuePage.story.contents.add("<first-page><subpage-display Scene Header><timer-stop lights-off><timer-stop lights-on><remove lights-out><overlay lights-out 0+0+0 false><send-to-front next-page><timer-start 0.75 lights-on>");
        giveMyleeHerDuePage.story.contents.add("<color 0+0+0>");
        giveMyleeHerDuePage.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Umm... aren't you forgetting to give me something?<quote>");
        giveMyleeHerDuePage.story.contents.add("<br>");
        giveMyleeHerDuePage.story.contents.add("<player-symbol> YOU: <quote>I thought we were in a hurry?<quote>");
        giveMyleeHerDuePage.story.contents.add("<br>");
        giveMyleeHerDuePage.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>We are, but this elevator isn't going anywhere until you give me your gold!<quote>");
        giveMyleeHerDuePage.story.contents.add("<br>");
        giveMyleeHerDuePage.story.contents.add("<get-validated-input align=left action *Give Mylee Gold>");
        giveMyleeHerDuePage.story.contents.add("</color>");
        myleesElevator.pages.put("Give The Mylee Her Due", giveMyleeHerDuePage);
        
        lightsOnSubpage = new Story();
        lightsOnSubpage.contents.add("<remove lights-out>");
        lightsOnSubpage.contents.add("<timer-stop lights-on>");
        lightsOnSubpage.contents.add("<timer-start 0.75 lights-out>");
        giveMyleeHerDuePage.subpages.put("TIMER lights-on", lightsOnSubpage);
        
        lightsOutSubpage = new Story();
        lightsOutSubpage.contents.add("<overlay lights-out 0+0+0 false>");
        lightsOutSubpage.contents.add("<send-to-front next-page>");
        lightsOutSubpage.contents.add("<send-to-front VARIABLE:action>");
        lightsOutSubpage.contents.add("<timer-stop lights-out>");
        lightsOutSubpage.contents.add("<timer-start 0.75 lights-on>");
        giveMyleeHerDuePage.subpages.put("TIMER lights-out", lightsOutSubpage);
        
        Story giveMyleeHerDueSubpage = new Story();
        giveMyleeHerDueSubpage.contents.add("<inventory-remove Gold>");
        giveMyleeHerDueSubpage.contents.add("<goto-page Elevator Goes Up>");
        giveMyleeHerDuePage.subpages.put("INPUT action=Give Mylee Gold", giveMyleeHerDueSubpage);
        
        Page elevatorGoesUpPage = new Page();
        elevatorGoesUpPage.nextPageName = "Get Me Some White Meat Chicken!";
        elevatorGoesUpPage.story.contents.add("<second-page>");
        elevatorGoesUpPage.story.contents.add("<image mylee left /assets/images/mylee.jpg>");
        elevatorGoesUpPage.story.contents.add("<first-page><subpage-display Scene Header><play-sound /assets/sounds/elevator.wav true><timer-stop lights-off><timer-stop lights-on><remove lights-out><overlay lights-out 0+0+0 false><send-to-front next-page><timer-start 0.5 lights-on>");
        elevatorGoesUpPage.story.contents.add("<color 0+0+0>");
        elevatorGoesUpPage.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Thank you!  Keep your human hands and feet inside the elevator at all times.  We're going up to the Second Kingdom!<quote>");
        elevatorGoesUpPage.story.contents.add("<br>");
        elevatorGoesUpPage.story.contents.add("<player-symbol> YOU: <quote>The Second Kingdom?  Is that where <variable twin> is?<quote>");
        elevatorGoesUpPage.story.contents.add("<br>");
        elevatorGoesUpPage.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Maybe.  We're going to have to check them all.<quote>");
        elevatorGoesUpPage.story.contents.add("<br>");
        elevatorGoesUpPage.story.contents.add("<player-symbol> YOU: <quote>What's the Second Kingdom like?<quote>");
        elevatorGoesUpPage.story.contents.add("<br>");
        elevatorGoesUpPage.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Oh you know.  It's a bit more modern.  Cars.  Spaceships.  And the whole place is run by chickens.<quote>");
        elevatorGoesUpPage.story.contents.add("<br>");
        elevatorGoesUpPage.story.contents.add("<player-symbol> YOU: <quote>Chickens???<quote>");
        elevatorGoesUpPage.story.contents.add("<br>");
        elevatorGoesUpPage.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Oh yes... tasty, tasty chickens!<quote>");
        elevatorGoesUpPage.story.contents.add("</color>");
        myleesElevator.pages.put("Elevator Goes Up", elevatorGoesUpPage);
        
        lightsOnSubpage = new Story();
        lightsOnSubpage.contents.add("<remove lights-out>");
        lightsOnSubpage.contents.add("<timer-stop lights-on>");
        lightsOnSubpage.contents.add("<timer-start 0.5 lights-out>");
        elevatorGoesUpPage.subpages.put("TIMER lights-on", lightsOnSubpage);
        
        lightsOutSubpage = new Story();
        lightsOutSubpage.contents.add("<overlay lights-out 0+0+0 false>");
        lightsOutSubpage.contents.add("<send-to-front next-page>");
        lightsOutSubpage.contents.add("<send-to-front VARIABLE:action>");
        lightsOutSubpage.contents.add("<timer-stop lights-out>");
        lightsOutSubpage.contents.add("<timer-start 0.5 lights-on>");
        elevatorGoesUpPage.subpages.put("TIMER lights-out", lightsOutSubpage);
        
        Page getMeWhiteMeatChickenPage = new Page();
        getMeWhiteMeatChickenPage.hideNextButton = true;
        getMeWhiteMeatChickenPage.story.contents.add("<second-page>");
        getMeWhiteMeatChickenPage.story.contents.add("<image mylee left /assets/images/mylee.jpg>");
        getMeWhiteMeatChickenPage.story.contents.add("<first-page><subpage-display Scene Header><stop-sound><play-sound /assets/sounds/elevator-open.mp3 false><timer-stop lights-off><timer-stop lights-on><remove lights-out><overlay lights-out 0+0+0 false><send-to-front next-page><timer-start 0.25 lights-on>");
        getMeWhiteMeatChickenPage.story.contents.add("<color 0+0+0>");
        getMeWhiteMeatChickenPage.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>And... we're here!  Get out!<quote>");
        getMeWhiteMeatChickenPage.story.contents.add("<br>");
        getMeWhiteMeatChickenPage.story.contents.add("<player-symbol> YOU: <quote>But what do I do?<quote>");
        getMeWhiteMeatChickenPage.story.contents.add("<br>");
        getMeWhiteMeatChickenPage.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Go looking for <variable twin>!<quote>");
        getMeWhiteMeatChickenPage.story.contents.add("<br>");
        getMeWhiteMeatChickenPage.story.contents.add("<player-symbol> YOU: <quote>Anything I need to know first?<quote>");
        getMeWhiteMeatChickenPage.story.contents.add("<br>");
        getMeWhiteMeatChickenPage.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>Of course!  First, thanks to the lack light, this elevator is about to die and go plummeting back down the elevator shaft!<quote>");
        getMeWhiteMeatChickenPage.story.contents.add("<br>");
        getMeWhiteMeatChickenPage.story.contents.add("<player-symbol> YOU: <quote>Oh no!!!<quote>");
        getMeWhiteMeatChickenPage.story.contents.add("<br>");
        getMeWhiteMeatChickenPage.story.contents.add("\uD83D\uDC08\u200D\u2B1B MYLEE: <quote>But don't worry, you can find me again if you get help from a certain, fluffy friend.  And if you want my continued help... get me some white meat chicken!!!<quote>");
        getMeWhiteMeatChickenPage.story.contents.add("<br>");
        getMeWhiteMeatChickenPage.story.contents.add("<get-validated-input align=left action *Leave Elevator>");
        getMeWhiteMeatChickenPage.story.contents.add("</color>");
        myleesElevator.pages.put("Get Me Some White Meat Chicken!", getMeWhiteMeatChickenPage);
        
        lightsOnSubpage = new Story();
        lightsOnSubpage.contents.add("<remove condition=\"full-dark!=true\" lights-out>");
        lightsOnSubpage.contents.add("<timer-stop lights-on>");
        lightsOnSubpage.contents.add("<timer-start condition=\"full-dark!=true\" 0.25 lights-out>");
        getMeWhiteMeatChickenPage.subpages.put("TIMER lights-on", lightsOnSubpage);
        
        lightsOutSubpage = new Story();
        lightsOutSubpage.contents.add("<overlay lights-out 0+0+0 false>");
        lightsOutSubpage.contents.add("<send-to-front next-page>");
        lightsOutSubpage.contents.add("<send-to-front VARIABLE:action>");
        lightsOutSubpage.contents.add("<timer-stop lights-out>");
        lightsOutSubpage.contents.add("<timer-start condition=\"full-dark!=true\" 0.25 lights-on>");
        getMeWhiteMeatChickenPage.subpages.put("TIMER lights-out", lightsOutSubpage);
        
        Story exitElevatorSubpage = new Story();
        exitElevatorSubpage.contents.add("<play-sound /assets/sounds/falling.mp3 false>");
        exitElevatorSubpage.contents.add("<variable-set full-dark true><timer-stop lights-off><timer-stop lights-on><remove lights-out><overlay lights-out 0+0+0 false><timer-start 2 crash>");
        exitElevatorSubpage.contents.add("You step out of the elevator and watch in horror as it goes crashing back down!  You certainly hope that Mylee's okay.");
        getMeWhiteMeatChickenPage.subpages.put("INPUT action=Leave Elevator", exitElevatorSubpage);
        
        Story crashSubpage = new Story();
        crashSubpage.contents.add("<timer-stop crash>");
        crashSubpage.contents.add("<play-sound /assets/sounds/crash.wav false>");
        crashSubpage.contents.add("<br><br>");
        crashSubpage.contents.add("<get-validated-input align=left action *Continue>");
        getMeWhiteMeatChickenPage.subpages.put("TIMER crash", crashSubpage);
        
        Story onToAct2 = new Story();
        onToAct2.contents.add("<stop-sound>");
        onToAct2.contents.add("<remove lights-out>");
        onToAct2.contents.add("<timer-stop crash>");
        onToAct2.contents.add("<background-color 0+0+0 Quest>");
        onToAct2.contents.add("<background-color 0+0+0 Inventory>");
        onToAct2.contents.add("<background-color 0+0+0 Map>");
        onToAct2.contents.add("<background-color 0+0+0 Spell Book>");
        onToAct2.contents.add("<background-color 0+0+0 High Scores>");
        onToAct2.contents.add("<goto-act Chapter 2>");
        getMeWhiteMeatChickenPage.subpages.put("INPUT action=Continue", onToAct2);
        
        Scene wilderness2 = new Scene();
        wilderness2.color = new Color(0, 100, 0);
        wilderness2.firstPageName = "main";
        wilderness2.stopOtherSounds = true;
        wilderness2.soundFileName = "/assets/sounds/wilderness.mp3";
        wilderness2.symbol = "\uD83C\uDF33";
        wilderness2.x = 1;
        wilderness2.y = 1;
        chapter1.scenes.put("WILDERNESS 2", wilderness2);
        
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 0+100+0>");
        mainPage.story.contents.add("You see tall grass, a few trees, a misty stream, hills in the distance, and cute bunnies.  Also, the sun is shining brightly.");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("Could something sinister be hiding in the tall grass?");
        mainPage.story.contents.add("</color>");
        mainPage.story.contents.add("<get-validated-input action Listen+Chase Bunnies>");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        mainPage.story.contents.add("<second-page>");
        mainPage.story.contents.add("<image wilderness left /assets/images/wilderness.jpg>");
        wilderness2.pages.put("main", mainPage);
        listenSubpage = new Story();
        listenSubpage.contents.add("<goto-page Listen>");
        mainPage.subpages.put("INPUT action=Listen", listenSubpage);
        Story chaseBunniesSubpage = new Story();
        chaseBunniesSubpage.contents.add("<goto-page Chase Bunnies>");
        mainPage.subpages.put("INPUT action=Chase Bunnies", chaseBunniesSubpage);
        
        listenPage = new Page();
        listenPage.previousPageName = "main";
        listenPage.story.contents.add("<color 0+100+0>You hear birds singing and perhaps some insects.  There is also a slight breeze.  These are the typical sounds of nature.");
        listenPage.story.contents.add("<br>");
        listenPage.story.contents.add("Oh the great outdoors!");
        listenPage.story.contents.add("</color>");
        listenPage.story.contents.add("<second-page>");
        listenPage.story.contents.add("<image wilderness left /assets/images/wilderness.jpg>");
        wilderness2.pages.put("Listen", listenPage);
        
        Page chaseBunniesPage = new Page();
        chaseBunniesPage.previousPageName = "main";
        chaseBunniesPage.story.contents.add("<color 79+47+79>Seems like an unimportant thing to do but nevertheless you try to chase some of the bunnies.");
        chaseBunniesPage.story.contents.add("<br>");
        chaseBunniesPage.story.contents.add("And...");
        chaseBunniesPage.story.contents.add("<br>");
        chaseBunniesPage.story.contents.add("<random Missed+Almost+Trouble>");
        chaseBunniesPage.story.contents.add("</color>");
        Story missedBunniesSubpage = new Story();
        missedBunniesSubpage.contents.add("<play-sound /assets/sounds/spring.wav false>");
        missedBunniesSubpage.contents.add("You can't catch a single one.  They are too fast!");
        missedBunniesSubpage.contents.add("<br>");
        missedBunniesSubpage.contents.add("Oh well, next time maybe.  You need to continue on with your quest.");
        missedBunniesSubpage.contents.add("<second-page>");
        missedBunniesSubpage.contents.add("<image bunny left /assets/images/bunny.jpg>");
        chaseBunniesPage.subpages.put("Missed", missedBunniesSubpage);
        Story almostBunniesSubpage = new Story();
        // Forked between an almost fail and almost succeed
        almostBunniesSubpage.contents.add("<subpage-display condition=\"inventory-has Ring of Taming!=true\" Almost Fail>");
        almostBunniesSubpage.contents.add("<subpage-display condition=\"inventory-has Ring of Taming=true\" Almost Success>");
        chaseBunniesPage.subpages.put("Almost", almostBunniesSubpage);
        Story almostBunniesFailSubpage = new Story();
        almostBunniesFailSubpage.contents.add("<play-sound /assets/sounds/spring.wav false>");
        almostBunniesFailSubpage.contents.add("You can't catch a single one.  But you got close!");
        almostBunniesFailSubpage.contents.add("<br>");
        almostBunniesFailSubpage.contents.add("Oh well, maybe next time.  You need to continue on with your quest.");
        almostBunniesFailSubpage.contents.add("<second-page>");
        almostBunniesFailSubpage.contents.add("<image bunny2 left /assets/images/bunny2.jpg>");
        chaseBunniesPage.subpages.put("Almost Fail", almostBunniesFailSubpage);
        Story almostBunniesSuccessSubpage = new Story();
        almostBunniesSuccessSubpage.contents.add("<play-sound /assets/sounds/ring-of-taming.wav false>");
        almostBunniesSuccessSubpage.contents.add("<inventory-add true Golden Bunny");
        almostBunniesSuccessSubpage.contents.add("You caught one!");
        almostBunniesSuccessSubpage.contents.add("<br>");
        almostBunniesSuccessSubpage.contents.add("Your Ring of Taming pulls the bunny straight into your hand.  It's a golden bunny!  What a triumph!!!");
        almostBunniesSuccessSubpage.contents.add("<second-page>");
        almostBunniesSuccessSubpage.contents.add("<image golden-rabbit left /assets/images/golden-rabbit.jpg>");
        chaseBunniesPage.subpages.put("Almost Success", almostBunniesSuccessSubpage);
        Story troubleBunniesSubpage = new Story();
        troubleBunniesSubpage.contents.add("<play-sound /assets/sounds/spring.wav false>");
        troubleBunniesSubpage.contents.add("<hp-change condition=\"variable difficulty=Easy\" -20 false a Bunny Commander>");
        troubleBunniesSubpage.contents.add("<hp-change condition=\"variable difficulty=Normal\" -30 false a Bunny Commander>");
        troubleBunniesSubpage.contents.add("<hp-change condition=\"variable difficulty=Hard\" -40 false a Bunny Commander>");
        troubleBunniesSubpage.contents.add("<hp-change condition=\"variable difficulty=Magical\" -20 false a Bunny Commander>");
        troubleBunniesSubpage.contents.add("Some of the bunnies start chasing you!  What are they, bionic???");
        troubleBunniesSubpage.contents.add("<br>");
        troubleBunniesSubpage.contents.add("A very large commander of the bunnies shoots at you.  You run as fast as you can to get away.  You definitely don't want to do that again!!!");
        troubleBunniesSubpage.contents.add("<second-page>");
        troubleBunniesSubpage.contents.add("<image bionic-bunny-commander left /assets/images/bionic-bunny-commander.jpg>");
        chaseBunniesPage.subpages.put("Trouble", troubleBunniesSubpage);
        wilderness2.pages.put("Chase Bunnies", chaseBunniesPage);
        
        Scene wilderness1 = new Scene();
        wilderness1.color = new Color(79, 47, 79);
        wilderness1.firstPageName = "main";
        wilderness1.stopOtherSounds = true;
        wilderness1.soundFileName = "/assets/sounds/wilderness.mp3";
        wilderness1.symbol = "\uD83E\uDEBB";
        wilderness1.x = 2;
        wilderness1.y = 1;
        chapter1.scenes.put("WILDERNESS 1", wilderness1);
        
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 79+47+79>");
        mainPage.story.contents.add("You see a meadow full of purple flowers.  The sun is shining brightly and cute bunnies scamper around you.  A wide, well-traveled dirt path splits out in all directions.");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("In the southeast a mighty mountain range borders the world.  It stretches to a central point where a mountain appears to climb so high that it quite possibly goes up into outer space.");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("To the south the sky darkens above a menacing woods.");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("And to the southwest you see more wilderness and a series of rock formations.");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("You need to remember so you can find your way back... \uD83D\uDED7 MYLEE'S ELEVATOR is by the purple flowers.");
        mainPage.story.contents.add("</color>");
        mainPage.story.contents.add("<get-validated-input action Listen+Chase Bunnies>");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        mainPage.story.contents.add("<second-page>");
        mainPage.story.contents.add("<image wilderness3 left /assets/images/wilderness3.jpg>");
        wilderness1.pages.put("main", mainPage);
        listenSubpage = new Story();
        listenSubpage.contents.add("<goto-page Listen>");
        mainPage.subpages.put("INPUT action=Listen", listenSubpage);
        chaseBunniesSubpage = new Story();
        chaseBunniesSubpage.contents.add("<goto-page Chase Bunnies>");
        mainPage.subpages.put("INPUT action=Chase Bunnies", chaseBunniesSubpage);
        
        listenPage = new Page();
        listenPage.previousPageName = "main";
        listenPage.story.contents.add("<color 79+47+79>You hear birds singing and perhaps some insects.  There is also a slight breeze.  These are the typical sounds of nature.");
        listenPage.story.contents.add("<br>");
        listenPage.story.contents.add("Oh the great outdoors!");
        listenPage.story.contents.add("</color>");
        listenPage.story.contents.add("<second-page>");
        listenPage.story.contents.add("<image wilderness3 left /assets/images/wilderness3.jpg>");
        wilderness1.pages.put("Listen", listenPage);
        
        chaseBunniesPage = new Page();
        chaseBunniesPage.previousPageName = "main";
        chaseBunniesPage.story.contents.add("<color 79+47+79>Seems like an unimportant thing to do but nevertheless you try to chase some of the bunnies.");
        chaseBunniesPage.story.contents.add("<br>");
        chaseBunniesPage.story.contents.add("And...");
        chaseBunniesPage.story.contents.add("<br>");
        chaseBunniesPage.story.contents.add("<random Missed+Almost+Trouble>");
        chaseBunniesPage.story.contents.add("</color>");
        missedBunniesSubpage = new Story();
        missedBunniesSubpage.contents.add("<play-sound /assets/sounds/spring.wav false>");
        missedBunniesSubpage.contents.add("You can't catch a single one.  They are too fast!");
        missedBunniesSubpage.contents.add("<br>");
        missedBunniesSubpage.contents.add("Oh well, next time maybe.  You need to continue on with your quest.");
        missedBunniesSubpage.contents.add("<second-page>");
        missedBunniesSubpage.contents.add("<image bunny left /assets/images/bunny.jpg>");
        chaseBunniesPage.subpages.put("Missed", missedBunniesSubpage);
        almostBunniesSubpage = new Story();
        // Forked between an almost fail and almost succeed
        almostBunniesSubpage.contents.add("<subpage-display condition=\"inventory-has Ring of Taming!=true\" Almost Fail>");
        almostBunniesSubpage.contents.add("<subpage-display condition=\"inventory-has Ring of Taming=true\" Almost Success>");
        chaseBunniesPage.subpages.put("Almost", almostBunniesSubpage);
        almostBunniesFailSubpage = new Story();
        almostBunniesFailSubpage.contents.add("<play-sound /assets/sounds/spring.wav false>");
        almostBunniesFailSubpage.contents.add("You can't catch a single one.  But you got close!");
        almostBunniesFailSubpage.contents.add("<br>");
        almostBunniesFailSubpage.contents.add("Oh well, maybe next time.  You need to continue on with your quest.");
        almostBunniesFailSubpage.contents.add("<second-page>");
        almostBunniesFailSubpage.contents.add("<image bunny2 left /assets/images/bunny2.jpg>");
        chaseBunniesPage.subpages.put("Almost Fail", almostBunniesFailSubpage);
        almostBunniesSuccessSubpage = new Story();
        almostBunniesSuccessSubpage.contents.add("<play-sound /assets/sounds/ring-of-taming.wav false>");
        almostBunniesSuccessSubpage.contents.add("<inventory-add false Bunny>");
        almostBunniesSuccessSubpage.contents.add("You caught one!");
        almostBunniesSuccessSubpage.contents.add("<br>");
        almostBunniesSuccessSubpage.contents.add("Your Ring of Taming pulls the bunny into a magical sphere so you can keep the bunny safe in your inventory.  Time to continue on with your quest.");
        almostBunniesSuccessSubpage.contents.add("<second-page>");
        almostBunniesSuccessSubpage.contents.add("<image bunny-caught left /assets/images/bunny-caught.jpg>");
        chaseBunniesPage.subpages.put("Almost Success", almostBunniesSuccessSubpage);
        troubleBunniesSubpage = new Story();
        troubleBunniesSubpage.contents.add("<play-sound /assets/sounds/spring.wav false>");
        troubleBunniesSubpage.contents.add("<hp-change condition=\"variable difficulty=Easy\" -10 false a Bunny Solider>");
        troubleBunniesSubpage.contents.add("<hp-change condition=\"variable difficulty=Normal\" -15 false a Bunny Solider>");
        troubleBunniesSubpage.contents.add("<hp-change condition=\"variable difficulty=Hard\" -20 false a Bunny Solider>");
        troubleBunniesSubpage.contents.add("<hp-change condition=\"variable difficulty=Magical\" -10 false a Bunny Solider>");
        troubleBunniesSubpage.contents.add("Some of the bunnies start chasing you!  What are they, bionic???");
        troubleBunniesSubpage.contents.add("<br>");
        troubleBunniesSubpage.contents.add("They shoot at you and you run as fast as you can to get away.  What were you thinking trying to chase bunnies???");
        troubleBunniesSubpage.contents.add("<second-page>");
        troubleBunniesSubpage.contents.add("<image bionic-bunny left /assets/images/bionic-bunny.jpg>");
        chaseBunniesPage.subpages.put("Trouble", troubleBunniesSubpage);
        wilderness1.pages.put("Chase Bunnies", chaseBunniesPage);
        
        Scene toadstoolCircle = new Scene();
        toadstoolCircle.color = new Color(184, 115, 51);
        toadstoolCircle.firstPageName = "main";
        toadstoolCircle.stopOtherSounds = true;
        toadstoolCircle.soundFileName = "/assets/sounds/ring-of-toadstools.wav";
        toadstoolCircle.symbol = "\uD83C\uDF44";
        toadstoolCircle.x = 3;
        toadstoolCircle.y = 1;
        chapter1.scenes.put("MAGIC RING OF TOADSTOOLS", toadstoolCircle);
        
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 184+115+51>");
        mainPage.story.contents.add("This is an enchanted place of giant mushrooms.");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("<subpage-display condition=\"player=Shmebulock\" SHMEBULOCK main><subpage-display condition=\"player!=Shmebulock\" twin main>");
        toadstoolCircle.pages.put("main", mainPage);
        Story shmebulockMainStory = new Story();
        shmebulockMainStory.contents.add("Flying faeries spin in a dizzying circle before you, magnifying the power of the Ring of Toadstools!  They're your guardians.  They show wayward travelers a peaceful vision to keep them from finding your house.");
        shmebulockMainStory.contents.add("<br>");
        shmebulockMainStory.contents.add("You walk through the circle and see the magic door that will transport you to your hidden house.");
        shmebulockMainStory.contents.add("</color>");
        shmebulockMainStory.contents.add("<get-validated-input action Listen+Open Door>");
        shmebulockMainStory.contents.add("<br>");
        shmebulockMainStory.contents.add("<subpage-display Navigation Footer>");
        shmebulockMainStory.contents.add("<second-page>");
        shmebulockMainStory.contents.add("<image magic-door center /assets/images/magic-door.jpg>");
        shmebulockMainStory.contents.add("<second-page>");
        shmebulockMainStory.contents.add("<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>");
        shmebulockMainStory.contents.add("<image toadstool left /assets/images/toadstool.png>");
        mainPage.subpages.put("SHMEBULOCK main", shmebulockMainStory);
        Story twinMainStory = new Story();        
        twinMainStory.contents.add("Flying faeries spin in a dizzying circle before you, magnifying the power of the Ring of Toadstools!  They show you a magical vision of Mylee and her brother Big Chung.  Before things went wrong, that is.");
        twinMainStory.contents.add("<br>");
        twinMainStory.contents.add("This vision is a still scene.  You can not interact with it.  However, a simple wooden sign stands before it all.");
        twinMainStory.contents.add("<br>");
        twinMainStory.contents.add("On this sign reads a cryptic message:");
        twinMainStory.contents.add("<br>");
        twinMainStory.contents.add("<i>Speaketh the name");
        twinMainStory.contents.add("Of the spirit that dwelleth herein");
        twinMainStory.contents.add("And be gifted with a ring of magick</i>");
        twinMainStory.contents.add("</color>");
        twinMainStory.contents.add("<get-validated-input action Listen+Speak Name>");
        twinMainStory.contents.add("<br>");
        twinMainStory.contents.add("<subpage-display Navigation Footer>");
        twinMainStory.contents.add("<second-page>");
        twinMainStory.contents.add("<image ring-of-toadstools left /assets/images/ring-of-toadstools.jpg>");
        twinMainStory.contents.add("<second-page>");
        twinMainStory.contents.add("<image frame left /assets/images/frame.png>");
        twinMainStory.contents.add("<second-page>");
        twinMainStory.contents.add("<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><image wooden-sign center /assets/images/wooden-sign.png>");
        mainPage.subpages.put("twin main", twinMainStory);
        listenSubpage = new Story();
        listenSubpage.contents.add("<goto-page condition=\"player!=Shmebulock\" Listen>");
        listenSubpage.contents.add("<goto-page condition=\"player=Shmebulock\" Shmebulock Listen>");
        mainPage.subpages.put("INPUT action=Listen", listenSubpage);
        Story speakNameSubpage = new Story();
        speakNameSubpage.contents.add("<goto-page Speak Name>");
        mainPage.subpages.put("INPUT action=Speak Name", speakNameSubpage);
        
        listenPage = new Page();
        listenPage.previousPageName = "main";
        listenPage.story.contents.add("<color 184+115+51>You can no longer see the magic faeries.  However, you hear the peaceful racing of their flapping wings.");
        listenPage.story.contents.add("<br>");
        listenPage.story.contents.add("All other sounds are drowned out.</color>");
        listenPage.story.contents.add("<second-page>");
        listenPage.story.contents.add("<image ring-of-toadstools left /assets/images/ring-of-toadstools.jpg>");
        listenPage.story.contents.add("<second-page>");
        listenPage.story.contents.add("<image frame left /assets/images/frame.png>");
        listenPage.story.contents.add("<second-page>");
        listenPage.story.contents.add("<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><image wooden-sign center /assets/images/wooden-sign.png>");
        toadstoolCircle.pages.put("Listen", listenPage);
        
        Story openDoorSubpage = new Story();
        openDoorSubpage.contents.add("<goto-page Open Door>");
        mainPage.subpages.put("INPUT action=Open Door", openDoorSubpage);
        Page openDoorPage = new Page();
        openDoorPage.previousPageName = "main";
        openDoorPage.story.contents.add("<second-page>");
        openDoorPage.story.contents.add("<image magic-door center /assets/images/magic-door.jpg>");
        openDoorPage.story.contents.add("<second-page>");
        openDoorPage.story.contents.add("<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>");
        openDoorPage.story.contents.add("<image toadstool left /assets/images/toadstool.png>");
        openDoorPage.story.contents.add("<first-page>");
        openDoorPage.story.contents.add("<color 184+115+51>You gleefully grab the handle to the door and... and... it won't pull open.  Oh no!  You've been away for so long that you forgot that it is protected by a secret password!");
        openDoorPage.story.contents.add("<br>");
        openDoorPage.story.contents.add("Now what was that password...");
        openDoorPage.story.contents.add("<br>");
        openDoorPage.story.contents.add("<get-input password 14 true true true false Enter password here>");
        toadstoolCircle.pages.put("Open Door", openDoorPage);
        Story spokeRightPasswordSubpage = new Story();
        spokeRightPasswordSubpage.contents.add("<play-sound /assets/sounds/magic.mp3 false><player-symbol> YOU: <quote><variable password><quote>");
        spokeRightPasswordSubpage.contents.add("<br>");
        spokeRightPasswordSubpage.contents.add("A purple mist rises off the ground and the door opens.");
        spokeRightPasswordSubpage.contents.add("<br>");
        spokeRightPasswordSubpage.contents.add("<get-validated-input action Walk Through Door>");
        spokeRightPasswordSubpage.contents.add("<br>");
        spokeRightPasswordSubpage.contents.add("</color>");
        openDoorPage.subpages.put("INPUT password=SHMEBULOCK", spokeRightPasswordSubpage);
        Story spokeWrongPasswordSubpage = new Story();
        spokeWrongPasswordSubpage.contents.add("<player-symbol> YOU: <quote><mask * password><quote>");
        spokeWrongPasswordSubpage.contents.add("<br>");
        spokeWrongPasswordSubpage.contents.add("You attempt to speak the password but no sounds come out of your mouth.");
        spokeWrongPasswordSubpage.contents.add("<br>");
        spokeWrongPasswordSubpage.contents.add("Perhaps you guessed incorrectly?</color>");
        openDoorPage.subpages.put("INPUT password", spokeWrongPasswordSubpage);
        
        Page shmebulockListenPage = new Page();
        shmebulockListenPage.previousPageName = "main";
        shmebulockListenPage.story.contents.add("<color 184+115+51>The place of your home, protected by powerful magic, is quiet and peaceful.  You hear nothing but the racing of flapping faery wings.");
        shmebulockListenPage.story.contents.add("<br>");
        shmebulockListenPage.story.contents.add("All other sounds are drowned out.</color>");
        shmebulockListenPage.story.contents.add("<second-page>");
        shmebulockListenPage.story.contents.add("<image magic-door center /assets/images/magic-door.jpg>");
        shmebulockListenPage.story.contents.add("<second-page>");
        shmebulockListenPage.story.contents.add("<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>");
        shmebulockListenPage.story.contents.add("<image toadstool right /assets/images/toadstool.png>");
        toadstoolCircle.pages.put("Shmebulock Listen", shmebulockListenPage);
        
        Story walkThroughDoor = new Story();
        walkThroughDoor.contents.add("<variable-set open-door true>");
        walkThroughDoor.contents.add("<goto-page Shmebulock Home>");
        openDoorPage.subpages.put("INPUT action=Walk Through Door", walkThroughDoor);
        
        Page shmebulockHomePage = new Page();
        shmebulockHomePage.previousPageName = "main";
                spokeRightPasswordSubpage.contents.add("");

        shmebulockHomePage.story.contents.add("<play-sound condition=\"open-door=true\" /assets/sounds/open-door.wav false><variable-set open-door false><color 184+115+51>You successfully step through the magic door...");
        shmebulockHomePage.story.contents.add("<br>");
        shmebulockHomePage.story.contents.add("... and find yourself standing before your mushroom house.  Oh it's good to be home!");
        shmebulockHomePage.story.contents.add("<br>");
        shmebulockHomePage.story.contents.add("Since you're here, you might as well kick up your feet and rest.");
        shmebulockHomePage.story.contents.add("<br>");
        shmebulockHomePage.story.contents.add("</color>");
        shmebulockHomePage.story.contents.add("<get-validated-input condition=\"mp=100\" action !Recharge Magic>");
        shmebulockHomePage.story.contents.add("<get-validated-input condition=\"mp!=100\" action Recharge Magic>");
        shmebulockHomePage.story.contents.add("<br>");
        shmebulockHomePage.story.contents.add("<second-page>");
        shmebulockHomePage.story.contents.add("<image gone-sweet-gnome left /assets/images/gnome-sweet-gnome.jpg>");
        toadstoolCircle.pages.put("Shmebulock Home", shmebulockHomePage);
        Story rechargeMPStory = new Story();
        rechargeMPStory.contents.add("<mp-change 100 true>");
        shmebulockHomePage.subpages.put("INPUT action=Recharge Magic", rechargeMPStory);
        
        Page speakNamePage = new Page();
        speakNamePage.previousPageName = "main";
        speakNamePage.story.contents.add("<second-page>");
        speakNamePage.story.contents.add("<image ring-of-toadstools left /assets/images/ring-of-toadstools.jpg>");
        speakNamePage.story.contents.add("<second-page>");
        speakNamePage.story.contents.add("<image frame left /assets/images/frame.png>");
        speakNamePage.story.contents.add("<second-page>");
        speakNamePage.story.contents.add("<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><image wooden-sign center /assets/images/wooden-sign.png>");
        speakNamePage.story.contents.add("<first-page>");
        speakNamePage.story.contents.add("<color 184+115+51>You take a deep breath and loudly speak a name...");
        speakNamePage.story.contents.add("<br>");
        speakNamePage.story.contents.add("<get-input name 12 true true true false Enter name here>");
        toadstoolCircle.pages.put("Speak Name", speakNamePage);
        Story spokeRightNameSubpage = new Story();
        spokeRightNameSubpage.contents.add("<play-sound /assets/sounds/magic.mp3 false><player-symbol> YOU: <quote><variable name><quote>");
        spokeRightNameSubpage.contents.add("<br>");
        spokeRightNameSubpage.contents.add("A purple mist rises off the ground... perhaps some magical creature is manifesting?");
        spokeRightNameSubpage.contents.add("<br>");
        spokeRightNameSubpage.contents.add("<get-validated-input summon Step Into The Mist>");
        spokeRightNameSubpage.contents.add("<br>");
        spokeRightNameSubpage.contents.add("</color>");
        speakNamePage.subpages.put("INPUT name=SHMEBULOCK", spokeRightNameSubpage);
        Story spokeWrongNameSubpage = new Story();
        spokeWrongNameSubpage.contents.add("<player-symbol> YOU: <quote><variable name><quote>");
        spokeWrongNameSubpage.contents.add("<br>");
        spokeWrongNameSubpage.contents.add("You wait... and wait some more.  But nothing happens!");
        spokeWrongNameSubpage.contents.add("<br>");
        spokeWrongNameSubpage.contents.add("Perhaps you spoke the wrong name?</color>");
        speakNamePage.subpages.put("INPUT name", spokeWrongNameSubpage);
        Story summonShmebulockSubpage = new Story();
        summonShmebulockSubpage.contents.add("<goto-page Shmebulock>");
        speakNamePage.subpages.put("INPUT summon=Step Into The Mist", summonShmebulockSubpage);
        
        Page shmebulockPage = new Page();
        shmebulockPage.previousPageName = "main";
        shmebulockPage.story.contents.add("<second-page><set-magic-text true>");
        shmebulockPage.story.contents.add("              ,");
        shmebulockPage.story.contents.add("             / \\");
        shmebulockPage.story.contents.add("            /   \\");
        shmebulockPage.story.contents.add("           /     \\");
        shmebulockPage.story.contents.add("          /_______\\");
        shmebulockPage.story.contents.add("          // . . \\\\");
        shmebulockPage.story.contents.add("         (/(__7__)\\)");
        shmebulockPage.story.contents.add("         |'-' = `-'|");
        shmebulockPage.story.contents.add("         |         |");
        shmebulockPage.story.contents.add("         /\\       /\\");
        shmebulockPage.story.contents.add("        /  '.   .'  \\");
        shmebulockPage.story.contents.add("       / /|  `v`  |\\ \\");
        shmebulockPage.story.contents.add("       \\ \\|===[]==|/ /");
        shmebulockPage.story.contents.add("        '-|_______|-'");
        shmebulockPage.story.contents.add("           |__|__|");
        shmebulockPage.story.contents.add("           |--|--|");
        shmebulockPage.story.contents.add("          (__)`(__)");
        shmebulockPage.story.contents.add("<set-magic-text false>");
        shmebulockPage.story.contents.add("<first-page><color 184+115+51>");
        shmebulockPage.story.contents.add("\uD83C\uDF85 MAGIC GNOME: <quote>SHMEBULOCK!  SHMEBULOCK... SHMEBULOCK.  SHMEBULOCK?  SHMEBULOCK, SHMEBULOCK.  SHME... BU... LOCK!<quote>");
        shmebulockPage.story.contents.add("<br>");
        shmebulockPage.story.contents.add("<subpage-display condition=\"inventory-has Ring of Taming=true\" Glow Ring Of Taming>");
        shmebulockPage.story.contents.add("<subpage-display condition=\"inventory-has Ring of Taming!=true\" Gain Ring Of Taming>");
        toadstoolCircle.pages.put("Shmebulock", shmebulockPage);
        Story gainRingOfTamingSubpage = new Story();
        gainRingOfTamingSubpage.contents.add("<inventory-add true Ring of Taming>");
        gainRingOfTamingSubpage.contents.add("It looks like you received a gift!  Huzzah!");
        shmebulockPage.subpages.put("Gain Ring Of Taming", gainRingOfTamingSubpage);
        Story glowRingOfTamingSubpage = new Story();
        glowRingOfTamingSubpage.contents.add("<play-sound /assets/sounds/ring-of-taming.wav false>");
        glowRingOfTamingSubpage.contents.add("You look down at your ring and it glows with power.");
        shmebulockPage.subpages.put("Glow Ring Of Taming", glowRingOfTamingSubpage);
        
        Scene giannisDen = new Scene();
        giannisDen.color = new Color(0, 0, 0);
        giannisDen.firstPageName = "main";
        giannisDen.stopOtherSounds = true;
        giannisDen.soundFileName = "/assets/sounds/giannis-den.wav";
        giannisDen.symbol = "\uD83D\uDC08\u200D\u2B1B";
        giannisDen.x = 1;
        giannisDen.y = 2;
        chapter1.scenes.put("GIANNI'S DEN", giannisDen);
        
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 0+0+0><variable-set gianni-photo-landscape false><variable-set gianni-photo-portrait false>");
        mainPage.story.contents.add("You come across a massive cave.  After climbing inside you're able to pick out some detail.");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("The walls of the cave are strong, making this a fortified location.  A heat vent goes straight down, possibly into the very core of the earth.  And at the back of the chamber are electric lights and what appears to be... living quarters?");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("Do you dare expore further to discover what might be living here?");
        mainPage.story.contents.add("</color>");
        mainPage.story.contents.add("<get-validated-input action Listen+Explore Living Quarters>");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        mainPage.story.contents.add("<second-page>");
        mainPage.story.contents.add("<image giannis-den center /assets/images/giannis-den.jpg>");
        giannisDen.pages.put("main", mainPage);
        listenSubpage = new Story();
        listenSubpage.contents.add("<goto-page Listen>");
        mainPage.subpages.put("INPUT action=Listen", listenSubpage);
        Story exploreLivingQuartersSubpage = new Story();
        exploreLivingQuartersSubpage.contents.add("<goto-page condition=\"is-Gianni-tamed=true\" Tamed Gianni>");
        exploreLivingQuartersSubpage.contents.add("<goto-page condition=\"is-Gianni-tamed!=true\" Untamed Gianni>");
        mainPage.subpages.put("INPUT action=Explore Living Quarters", exploreLivingQuartersSubpage);
        
        listenPage = new Page();
        listenPage.previousPageName = "main";
        listenPage.story.contents.add("<color 0+0+0>There's a deep hum.  Almost like the sound of an electric fan constantly blowing heat across the walls of the cave.");
        listenPage.story.contents.add("<br>");
        listenPage.story.contents.add("And... maybe the sound of a cat meowing for his mother?  It's hard to tell... the sound is so subtle!</color>");
        listenPage.story.contents.add("<second-page>");
        listenPage.story.contents.add("<image giannis-den center /assets/images/giannis-den.jpg>");
        giannisDen.pages.put("Listen", listenPage);
        
        Page tamedGianniPage = new Page();
        tamedGianniPage.previousPageName = "main";
        tamedGianniPage.story.contents.add("<color 0+0+0>You explore the living quarters and find Gianni lying on a human bed.  The bedroom has green walls and gold trim.  With Gianni's black fur and the gold trim, you're reminded of the Pittsburgh Steelers.");
        tamedGianniPage.story.contents.add("<br>");
        tamedGianniPage.story.contents.add("Gianni is delighted to see you again and you pet him for hours.</color>");
        tamedGianniPage.story.contents.add("<br>");
        tamedGianniPage.story.contents.add("<get-validated-input action Photo 1+Photo 2+Photo 3+Photo 4+Photo 5+Photo 6+Photo 7>");
        tamedGianniPage.story.contents.add("<second-page>");
        tamedGianniPage.story.contents.add("<image condition=\"action=Explore Living Quarters\" giannis-bed center /assets/images/gianni-bed.jpg>");
        tamedGianniPage.story.contents.add("<second-page>");
        tamedGianniPage.story.contents.add("<image condition=\"gianni-photo-landscape=true\" polaroid-landscape center /assets/images/polaroid-landscape.png><image condition=\"gianni-photo-portrait=true\" polaroid-portrait center /assets/images/polaroid-portrait.png>");
        tamedGianniPage.story.contents.add("<second-page>");
        tamedGianniPage.story.contents.add("<br><br>");
        tamedGianniPage.story.contents.add("<image condition=\"action=Photo 7\" gianni-photo7 center /assets/images/gianni-photo7.jpg>");
        tamedGianniPage.story.contents.add("<second-page>");
        tamedGianniPage.story.contents.add("<br><br>");
        tamedGianniPage.story.contents.add("<image condition=\"action=Photo 1\" gianni-photo1 center /assets/images/gianni-photo1.jpg><image condition=\"action=Photo 2\" gianni-photo2 center /assets/images/gianni-photo2.jpg><image condition=\"action=Photo 3\" gianni-photo3 center /assets/images/gianni-photo3.jpg><image condition=\"action=Photo 4\" gianni-photo4 center /assets/images/gianni-photo4.jpg><image condition=\"action=Photo 5\" gianni-photo5 center /assets/images/gianni-photo5.jpg><image condition=\"action=Photo 6\" gianni-photo6 center /assets/images/gianni-photo6.jpg>");
        giannisDen.pages.put("Tamed Gianni", tamedGianniPage);
        
        Story landscapePhotoSubpage = new Story();
        landscapePhotoSubpage.contents.add("<play-sound /assets/sounds/camera.mp3 false>");
        landscapePhotoSubpage.contents.add("<variable-set gianni-photo-landscape true><variable-set gianni-photo-portrait false>");
        landscapePhotoSubpage.contents.add("<page-refresh>");
        tamedGianniPage.subpages.put("INPUT action", landscapePhotoSubpage);
        
        Story portraitPhotoSubpage = new Story();
        portraitPhotoSubpage.contents.add("<play-sound /assets/sounds/camera.mp3 false>");
        portraitPhotoSubpage.contents.add("<variable-set gianni-photo-portrait true><variable-set gianni-photo-landscape false>");
        portraitPhotoSubpage.contents.add("<page-refresh>");
        tamedGianniPage.subpages.put("INPUT action=Photo 7", portraitPhotoSubpage);
        
        Page untamedGianniPage = new Page();
        untamedGianniPage.previousPageName = "main";
        untamedGianniPage.story.contents.add("<second-page>");
        untamedGianniPage.story.contents.add("<image gianni-escape center /assets/images/gianni-escape.jpg>");
        untamedGianniPage.story.contents.add("<first-page>");
        untamedGianniPage.story.contents.add("<color 0+0+0>You explore the living quarters and find yourself face-to-face with an adorable black cat curled up on a Pittsburgh Steelers blanket.");
        untamedGianniPage.story.contents.add("<br>");
        untamedGianniPage.story.contents.add("You attempt to pet him but he climbs onto a stair railing to get away.</color>");
        untamedGianniPage.story.contents.add("<subpage-display condition=\"inventory-has Ring of Taming=true\" Untamed Gianni Ring of Taming><subpage-display condition=\"inventory-has Ring of Taming!=true\" Untamed Gianni No Ring of Taming>");
        giannisDen.pages.put("Untamed Gianni", untamedGianniPage);
        
        Story untamedGianniNoRingOfTamingSubpage = new Story();
        untamedGianniNoRingOfTamingSubpage.contents.add("<color 0+0+0>If only you had some way to tame him!</color>");
        untamedGianniPage.subpages.put("Untamed Gianni No Ring of Taming", untamedGianniNoRingOfTamingSubpage);

        Story untamedGianniRingOfTamingSubpage = new Story();
        untamedGianniRingOfTamingSubpage.contents.add("<br><get-validated-input action Tame Gianni>");
        untamedGianniPage.subpages.put("Untamed Gianni Ring of Taming", untamedGianniRingOfTamingSubpage);
        
        Story tameGianniSubpage = new Story();
        tameGianniSubpage.contents.add("<goto-page Tame Gianni>");
        untamedGianniPage.subpages.put("INPUT action=Tame Gianni", tameGianniSubpage);
        
        Page tameGianniPage = new Page();
        tameGianniPage.previousPageName = "main";
        tameGianniPage.story.contents.add("<second-page>");
        tameGianniPage.story.contents.add("<image gianni-tamed center /assets/images/gianni-tamed.jpg>");
        tameGianniPage.story.contents.add("<first-page><play-sound /assets/sounds/ring-of-taming.wav false><variable-set is-Gianni-tamed true>");
        tameGianniPage.story.contents.add("<color 0+0+0>You use your <inventory Ring of Taming> on him.");
        tameGianniPage.story.contents.add("<br>");
        tameGianniPage.story.contents.add("He jumps down from the railing and curls up on a straw mat.  He can't quite talk but he uses his meows to direct your gaze to a clock with a door that swings open.  Inside you find a long range weapon to aid you in your quest.");
        tameGianniPage.story.contents.add("<br>");
        tameGianniPage.story.contents.add("<subpage-display condition=\"player=Shmebulock\" Shmebulock Weapon>");
        tameGianniPage.story.contents.add("<subpage-display condition=\"player=Greyson\" Greyson Weapon>");
        tameGianniPage.story.contents.add("<subpage-display condition=\"player=Zara\" Zara Weapon>");
        tameGianniPage.story.contents.add("</color>");        
        giannisDen.pages.put("Tame Gianni", tameGianniPage);
        
        Story shmebulockWeaponSubpage = new Story();
        shmebulockWeaponSubpage.contents.add("<inventory-add true Faery Launcher>You now have a <inventory Faery Launcher>!</color>");
        tameGianniPage.subpages.put("Shmebulock Weapon", shmebulockWeaponSubpage);
        
        Story greysonWeaponSubpage = new Story();
        greysonWeaponSubpage.contents.add("<inventory-add true Greyson's Great Bow>You now have <inventory Greyson's Great Bow>!</color>");
        tameGianniPage.subpages.put("Greyson Weapon", greysonWeaponSubpage);
        
        Story zaraWeaponSubpage = new Story();
        zaraWeaponSubpage.contents.add("<inventory-add true Cat-apult>You now have a <inventory Cat-apult>!</color>");
        tameGianniPage.subpages.put("Zara Weapon", zaraWeaponSubpage);
        
        Scene woods = new Scene();
        woods.color = new Color(85, 85, 85);
        woods.firstPageName = "main";
        woods.stopOtherSounds = true;
        woods.soundFileName = "/assets/sounds/woods.mp3";
        //woods.symbol = "\uD83E\uDEBE";  // Branchless tree is new with Unicode v16.0 (2024) and is only supported in recent versions of Java
        woods.symbol = "\uD83E\uDD89";
        woods.x = 2;
        woods.y = 2;
        chapter1.scenes.put("WOODS", woods);
        
        mainPage = new Page();
        mainPage.story.contents.add("<second-page>");
        mainPage.story.contents.add("<image woods center /assets/images/woods.png>");
        mainPage.story.contents.add("<first-page>");
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 85+85+85>");
        mainPage.story.contents.add("You find yourself in the middle of a dark woods.");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("<random Missed+Almost+Dead>");
        mainPage.story.contents.add("</color>");
        woods.pages.put("main", mainPage);
        
        listenSubpage = new Story();
        listenSubpage.contents.add("<goto-page Listen>");
        mainPage.subpages.put("INPUT action=Listen", listenSubpage);
        Story missedSubpage = new Story();
        missedSubpage.contents.add("<color 85+85+85>The sky is dark and Night Owl is hiding somewhere.  You must take care to tread lightly lest a noise alert Night Owl to your whereabouts.  Be very, very quiet.</color>");
        missedSubpage.contents.add("<br>");
        missedSubpage.contents.add("<get-validated-input action &down; Leave Woods>");
        mainPage.subpages.put("Missed", missedSubpage);
        Story almostSubpage = new Story();
        almostSubpage.contents.add("<color 85+85+85>What was that???<play-sound /assets/sounds/heavy-wings.mp3 false>");
        almostSubpage.contents.add("<br>");
        almostSubpage.contents.add("The sky is dark and you hear the sound of flapping wings.");
        almostSubpage.contents.add("<br></color>");
        almostSubpage.contents.add("<get-validated-input action1 Look Up>");
        mainPage.subpages.put("Almost", almostSubpage);
        Story deadSubpage = new Story();
        deadSubpage.contents.add("<color 85+85+85>What was that???<play-sound /assets/sounds/heavy-wings.mp3 false>");
        deadSubpage.contents.add("<br>");
        deadSubpage.contents.add("The sky is dark and you hear the sound of flapping wings.");
        deadSubpage.contents.add("<br></color>");
        deadSubpage.contents.add("<get-validated-input action2 Look Up>");
        mainPage.subpages.put("Dead", deadSubpage);
        Story lookUp1Subpage = new Story();
        lookUp1Subpage.contents.add("<color 85+85+85><play-sound /assets/sounds/heavy-wings.mp3 false>You see a giant gray blur come sweeping down from above!  It barely misses you and then swoops back up into the night sky.");
        lookUp1Subpage.contents.add("<br>");
        lookUp1Subpage.contents.add("You have escaped death.  But... for how long?  That could have been you.  Be very, very quiet.</color>");
        lookUp1Subpage.contents.add("<br></color>");
        lookUp1Subpage.contents.add("<get-validated-input action &down; Leave Woods>");
        mainPage.subpages.put("INPUT action1=Look Up", lookUp1Subpage);
        Story lookUp2Subpage = new Story();
        lookUp2Subpage.contents.add("<goto-page Dead>");
        mainPage.subpages.put("INPUT action2=Look Up", lookUp2Subpage);
        Story leaveWoodsSubpage = new Story();
        leaveWoodsSubpage.contents.add("<move-back>");
        mainPage.subpages.put("INPUT action= Leave Woods", leaveWoodsSubpage);
        
        listenPage = new Page();
        listenPage.previousPageName = "main";
        listenPage.story.contents.add("<color 85+85+85>You hear the typical sounds of the woods at night.  There are birds.  Definitely crows.  Maybe a There are birds.  Definitely crows.  Maybe a woodpecker.  And...");
        listenPage.story.contents.add("<br>");
        listenPage.story.contents.add("...the hooting of an owl?</color>");
        listenPage.story.contents.add("<second-page>");
        listenPage.story.contents.add("<image woods center /assets/images/woods.png>");
        woods.pages.put("Listen", listenPage);
        
        Page deadPage = new Page();
        deadPage.story.contents.add("<second-page>");
        deadPage.story.contents.add("<image night-owl center /assets/images/night-owl.jpg>");
        deadPage.story.contents.add("<first-page>");
        deadPage.story.contents.add("<hp-change -100 false Night Owl><play-sound /assets/sounds/heavy-wings.mp3 false><play-sound /assets/sounds/hooting.mp3 false>");
        deadPage.story.contents.add("<color 85+85+85>Too late.</color>");
        deadPage.story.contents.add("<br>");
        woods.pages.put("Dead", deadPage);
        
        Scene mountFluff = new Scene();
        mountFluff.color = new Color(255, 255, 255);
        mountFluff.firstPageName = "main";
        mountFluff.stopOtherSounds = true;
        mountFluff.soundFileName = "/assets/sounds/wind.mp3";
        mountFluff.symbol = "\uD83C\uDFD4";
        mountFluff.x = 3;
        mountFluff.y = 2;
        chapter1.scenes.put("MOUNT FLUFF", mountFluff);
        
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 0+0+0>");
        mainPage.story.contents.add("You come upon the base of a giant snowy mountain that is so tall you can't even see the top.");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("Ahead is what appears to be the second floor of a ski lodge peeking up over a hill.  A mysterious black cat exits the lodge and surveys the landscape as she prepares to depart towards the west.");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("It may be wise to seek shelter inside the ski lodge.");
        mainPage.story.contents.add("</b></color>");
        mainPage.story.contents.add("<get-validated-input action Listen+Enter Ski Lodge>");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        mainPage.story.contents.add("<second-page>");
        mainPage.story.contents.add("<image mount-fluff center /assets/images/mount-fluff.jpg>");
        mountFluff.pages.put("main", mainPage);
        listenSubpage = new Story();
        listenSubpage.contents.add("<goto-page Listen>");
        mainPage.subpages.put("INPUT action=Listen", listenSubpage);
        Story enterSkiLodgeSubpage = new Story();
        enterSkiLodgeSubpage.contents.add("<goto-page Enter Ski Lodge>");
        mainPage.subpages.put("INPUT action=Enter Ski Lodge", enterSkiLodgeSubpage);
        
        listenPage = new Page();
        listenPage.previousPageName = "main";
        listenPage.story.contents.add("<color 0+0+0>It's a cold evening here at Mount Fluff!  The snow has muffled most sounds.  But from time to time you hear the occasional sound of the last few skiers of the day coming down the mountain.  And the cold chilly wind can be heard all about.");
        listenPage.story.contents.add("<br>");
        listenPage.story.contents.add("Despite the cold, this is a safe place.</color>");
        listenPage.story.contents.add("<second-page>");
        listenPage.story.contents.add("<image mount-fluff center /assets/images/mount-fluff.jpg>");
        mountFluff.pages.put("Listen", listenPage);
        
        Page skiLodgePage = new Page();
        skiLodgePage.previousPageName = "main";
        skiLodgePage.story.contents.add("<variable-set fluff-photo-portrait false><variable-set fluff-photo-landscape false><color 0+0+0><play-sound /assets/sounds/fluff.wav false><if condition=\"is-fluff-tamed!=true\" Inside the ski lodge the evening casts shadows around the room while the last few logs of a fireplace burn in the back.  Atop a cat tree sits a mysterious yet gallant fluffy cat.><if condition=\"is-fluff-tamed=true\" Inside the ski lodge it's become quite cheerful.  Your friend the fluffy cat is now reclining on a cat bed.  He looks up at you and smiles with flashing, blue eyes.>");
        skiLodgePage.story.contents.add("<br>");
        skiLodgePage.story.contents.add("The fluffy cat sets aside his noble spanish guitar and speaks...");
        skiLodgePage.story.contents.add("<br>");
        skiLodgePage.story.contents.add("\uD83D\uDE38 PROFESSOR FLUFF: <quote>I challenge you to a riddle!<quote></color>");
        skiLodgePage.story.contents.add("<br>");
        skiLodgePage.story.contents.add("<get-validated-input condition=\"inventory-has Ring of Taming!=true\" action !Tame Fluff+Accept Challenge><get-validated-input condition=\"inventory-has Ring of Taming=true\" action Tame Fluff+Accept Challenge>");
        skiLodgePage.story.contents.add("<subpage-display Second Page>");
        mountFluff.pages.put("Enter Ski Lodge", skiLodgePage);
        
        Story skiLodgeSecondPageSubpage = new Story();
        skiLodgeSecondPageSubpage.contents.add("<second-page>");
        skiLodgeSecondPageSubpage.contents.add("<image condition=\"variable is-fluff-tamed=true\" fluff3 center /assets/images/fluff3.jpg><image condition=\"variable is-fluff-tamed!=true\" fluff center /assets/images/fluff.jpg>");
        skiLodgePage.subpages.put("Second Page", skiLodgeSecondPageSubpage);
        
        Story tameFluffSubpage = new Story();
        tameFluffSubpage.contents.add("<variable-set fluff-photo-count 0><play-sound /assets/sounds/ring-of-taming.wav false><goto-page Tame Fluff>");
        skiLodgePage.subpages.put("INPUT action=Tame Fluff", tameFluffSubpage);
        
        Page tameFluffPage = new Page();
        tameFluffPage.previousPageName = "Enter Ski Lodge";
        tameFluffPage.story.contents.add("<color 0+0+0>You use your <inventory Ring of Taming> on him.<variable-set is-fluff-tamed true></color>");
        tameFluffPage.story.contents.add("<br>");
        tameFluffPage.story.contents.add("He puts on his best cute face... and... you must... look away.  He's just... so... cute!!!");
        tameFluffPage.story.contents.add("<br>");
        tameFluffPage.story.contents.add("<b>Fluff Level: <variable fluff-photo-count></b>");
        tameFluffPage.story.contents.add("<br>");
        tameFluffPage.story.contents.add("<get-validated-input condition=\"fluff-photo-count!=7\" action Photo 1+Photo 2+Photo 3+Photo 4+Photo 5+Photo 6+Photo 7>");
        tameFluffPage.story.contents.add("<second-page>");
        tameFluffPage.story.contents.add("<subpage-display condition=\"action=Tame Fluff\" Tamed Fluff>");
        tameFluffPage.story.contents.add("<hp-change condition=\"fluff-photo-count=7\" -100 false a fluffopotamus (you drowned in fluff)>");
        tameFluffPage.story.contents.add("<second-page>");
        tameFluffPage.story.contents.add("<image condition=\"fluff-photo-landscape=true\" polaroid-landscape center /assets/images/polaroid-landscape.png><image condition=\"fluff-photo-portrait=true\" polaroid-portrait center /assets/images/polaroid-portrait.png>");
        tameFluffPage.story.contents.add("<second-page>");
        tameFluffPage.story.contents.add("<br><br>");
        tameFluffPage.story.contents.add("<image condition=\"action=Photo 2\" fluff-photo2 center /assets/images/fluff-photo2.jpg><image condition=\"action=Photo 4\" fluff-photo4 center /assets/images/fluff-photo4.jpg><image condition=\"action=Photo 6\" fluff-photo6 center /assets/images/fluff-photo6.jpg><image condition=\"action=Photo 7\" fluff-photo7 center /assets/images/fluff-photo7.jpg>");
        tameFluffPage.story.contents.add("<second-page>");
        tameFluffPage.story.contents.add("<br><br>");
        tameFluffPage.story.contents.add("<image condition=\"action=Photo 1\" fluff-photo1 center /assets/images/fluff-photo1.jpg><image condition=\"action=Photo 3\" fluff-photo3 center /assets/images/fluff-photo3.jpg><image condition=\"action=Photo 5\" fluff-photo5 center /assets/images/fluff-photo5.jpg>");
        mountFluff.pages.put("Tame Fluff", tameFluffPage);
        
        Story photoSubpage = new Story();
        photoSubpage.contents.add("<play-sound /assets/sounds/camera.mp3 false>");
        photoSubpage.contents.add("<variable-set condition=\"action=Photo 1\" fluff-photo-1 true><variable-set condition=\"action=Photo 1\" fluff-photo-portrait true><variable-set condition=\"action=Photo 1\" fluff-photo-landscape false>");
        photoSubpage.contents.add("<variable-set condition=\"action=Photo 2\" fluff-photo-2 true><variable-set condition=\"action=Photo 2\" fluff-photo-landscape true><variable-set condition=\"action=Photo 2\" fluff-photo-portrait false>");
        photoSubpage.contents.add("<variable-set condition=\"action=Photo 3\" fluff-photo-3 true><variable-set condition=\"action=Photo 3\" fluff-photo-portrait true><variable-set condition=\"action=Photo 3\" fluff-photo-landscape false>");
        photoSubpage.contents.add("<variable-set condition=\"action=Photo 4\" fluff-photo-4 true><variable-set condition=\"action=Photo 4\" fluff-photo-landscape true><variable-set condition=\"action=Photo 4\" fluff-photo-portrait false>");
        photoSubpage.contents.add("<variable-set condition=\"action=Photo 5\" fluff-photo-5 true><variable-set condition=\"action=Photo 5\" fluff-photo-portrait true><variable-set condition=\"action=Photo 5\" fluff-photo-landscape false>");
        photoSubpage.contents.add("<variable-set condition=\"action=Photo 6\" fluff-photo-6 true><variable-set condition=\"action=Photo 6\" fluff-photo-landscape true><variable-set condition=\"action=Photo 6\" fluff-photo-portrait false>");
        photoSubpage.contents.add("<variable-set condition=\"action=Photo 7\" fluff-photo-7 true><variable-set condition=\"action=Photo 7\" fluff-photo-landscape true><variable-set condition=\"action=Photo 7\" fluff-photo-portrait false>");
        photoSubpage.contents.add("<variable-set fluff-photo-count 0>");
        photoSubpage.contents.add("<variable-add condition=\"fluff-photo-1=true\" fluff-photo-count 1>");
        photoSubpage.contents.add("<variable-add condition=\"fluff-photo-2=true\" fluff-photo-count 1>");
        photoSubpage.contents.add("<variable-add condition=\"fluff-photo-3=true\" fluff-photo-count 1>");
        photoSubpage.contents.add("<variable-add condition=\"fluff-photo-4=true\" fluff-photo-count 1>");
        photoSubpage.contents.add("<variable-add condition=\"fluff-photo-5=true\" fluff-photo-count 1>");
        photoSubpage.contents.add("<variable-add condition=\"fluff-photo-6=true\" fluff-photo-count 1>");
        photoSubpage.contents.add("<variable-add condition=\"fluff-photo-7=true\" fluff-photo-count 1>");
        photoSubpage.contents.add("<page-refresh>");
        tameFluffPage.subpages.put("INPUT action", photoSubpage);
        
        Story tamedFluffSubpage = new Story();
        tamedFluffSubpage.contents.add("<second-page><color 0+0+0>");
        tamedFluffSubpage.contents.add("                  ____");
        tamedFluffSubpage.contents.add("                  \\ _ `\\ ");
        tamedFluffSubpage.contents.add("                   | \\  `\\._");
        tamedFluffSubpage.contents.add("                   |  |  _/ `-.._");
        tamedFluffSubpage.contents.add("                   |  /-'  // /.'`-. .--.__");
        tamedFluffSubpage.contents.add("                   /-'    || // //  |    __\\");
        tamedFluffSubpage.contents.add("                 ./   .` |\\///_//   \\  /'   |");
        tamedFluffSubpage.contents.add("               .'.-.__.` \\ |/'-' .'  \\|    /");
        tamedFluffSubpage.contents.add("              / ( ____`.\\ |/ .' '.'   |\\  /");
        tamedFluffSubpage.contents.add("             /  -//   \\     /- _  '     `'|");
        tamedFluffSubpage.contents.add("             |  ||o    ;       __`--      |");
        tamedFluffSubpage.contents.add("             |   \\\\   /      //  `.  \\    |");
        tamedFluffSubpage.contents.add("             \\    `---'     |/o    \\_ )   \\");
        tamedFluffSubpage.contents.add("           _ _\\_    /       |      /       |");
        tamedFluffSubpage.contents.add("         _-_`-__-_.-'|__    \\`-..-'       /");
        tamedFluffSubpage.contents.add("        '  .--_--_-.. \\_\\/_              /");
        tamedFluffSubpage.contents.add("          ' /    \\... / .. \\_-___       / \\");
        tamedFluffSubpage.contents.add("           /      `-._| ..-._--___     /   \\");
        tamedFluffSubpage.contents.add("          /    .---.|  `-.__/`--.__---'     |");
        tamedFluffSubpage.contents.add("         /_.--/ . . \\__/   _   `--._-.      |");
        tamedFluffSubpage.contents.add("      .-'    | || | |   .-' `-.     \\ `\\    |");
        tamedFluffSubpage.contents.add("    .'       `-\\/\\|-'  |  / /  \\     `\\ \\   |");
        tamedFluffSubpage.contents.add("   /                    \\/ | .  |           |");
        tamedFluffSubpage.contents.add("  /                      \\_/_/ / \\          |");
        tamedFluffSubpage.contents.add("    magnifico               \\/   \\         |</color>");
        tameFluffPage.subpages.put("Tamed Fluff", tamedFluffSubpage);
        
        Story acceptChallengeSubpage = new Story();
        acceptChallengeSubpage.contents.add("<goto-page Accept Challenge>");
        skiLodgePage.subpages.put("INPUT action=Accept Challenge", acceptChallengeSubpage);
        
        Page acceptChallengePage = new Page();
        acceptChallengePage.previousPageName = "Enter Ski Lodge";
        acceptChallengePage.story.contents.add("<subpage-display Second Page>");
        acceptChallengePage.story.contents.add("<first-page><color 0+0+0><br><subpage-display condition=\"player!=Shmebulock\" Non-Magical Riddle><subpage-display condition=\"player=Shmebulock\" Magical Riddle>");
        acceptChallengePage.story.contents.add("<br></color>");
        acceptChallengePage.story.contents.add("<get-input condition=\"player!=Shmebulock\" answer 15 true true true false Enter answer here><get-input condition=\"player=Shmebulock\" magical-answer 15 true true true false Enter answer here>");
        mountFluff.pages.put("Accept Challenge", acceptChallengePage);
        
        skiLodgeSecondPageSubpage = new Story();
        skiLodgeSecondPageSubpage.contents.add("<second-page>");
        skiLodgeSecondPageSubpage.contents.add("<image condition=\"variable is-fluff-tamed=true\" fluff3 center /assets/images/fluff3.jpg><image condition=\"variable is-fluff-tamed!=true\" fluff center /assets/images/fluff.jpg>");
        acceptChallengePage.subpages.put("Second Page", skiLodgeSecondPageSubpage);
        
        Story nonMagicalRiddleSubpage = new Story();
        nonMagicalRiddleSubpage.contents.add("\uD83D\uDE38 PROFESSOR FLUFF: <quote>Answer me this, and a magical name I will give to you.<quote>");
        nonMagicalRiddleSubpage.contents.add("<br>");
        nonMagicalRiddleSubpage.contents.add("<player-symbol> YOU: <quote>...<quote>");
        nonMagicalRiddleSubpage.contents.add("<br>");
        nonMagicalRiddleSubpage.contents.add("\uD83D\uDE38 PROFESSOR FLUFF: <quote><i>What is blue... and is also the sky?</i><quote>");
        acceptChallengePage.subpages.put("Non-Magical Riddle", nonMagicalRiddleSubpage);
        
        Story magicalRiddleSubpage = new Story();
        magicalRiddleSubpage.contents.add("\uD83D\uDE38 PROFESSOR FLUFF: <quote>Answer me this, and a great secret about this world I will bestow upon you.  And this great secret... shall lead to an even greater treasure!<quote>");
        magicalRiddleSubpage.contents.add("<br>");
        magicalRiddleSubpage.contents.add("<player-symbol> YOU: <quote>...<quote>");
        magicalRiddleSubpage.contents.add("<br>");
        magicalRiddleSubpage.contents.add("\uD83D\uDE38 PROFESSOR FLUFF: <quote><i>This thing all things devours: Birds, beasts, trees, flowers; Gnaws iron, bites steel; Grinds hard stones to meal; Slays king, ruins town, And beats high mountain down.</i><quote>");
        acceptChallengePage.subpages.put("Magical Riddle", magicalRiddleSubpage);
        
        Story theSkyAnswerSubpage = new Story();
        theSkyAnswerSubpage.contents.add("<subpage-display condition=\"player!=Shmebulock\" Correct Answer>");
        acceptChallengePage.subpages.put("INPUT answer=THE SKY", theSkyAnswerSubpage);
        Story theBlueSkyAnswerSubpage = new Story();
        theBlueSkyAnswerSubpage.contents.add("<subpage-display condition=\"player!=Shmebulock\" Correct Answer>");
        acceptChallengePage.subpages.put("INPUT answer=THE BLUE SKY", theBlueSkyAnswerSubpage);
        Story blueSkyAnswerSubpage = new Story();
        blueSkyAnswerSubpage.contents.add("<subpage-display condition=\"player!=Shmebulock\" Correct Answer>");
        acceptChallengePage.subpages.put("INPUT answer=BLUE SKY", blueSkyAnswerSubpage);
        Story skyAnswerSubpage = new Story();
        skyAnswerSubpage.contents.add("<subpage-display condition=\"player!=Shmebulock\" Correct Answer>");
        acceptChallengePage.subpages.put("INPUT answer=SKY", skyAnswerSubpage);
        Story timeSubpage = new Story();
        timeSubpage.contents.add("<goto-page condition=\"inventory-has Ancient Spell=true\" Correct Magical Answer>");
        timeSubpage.contents.add("<subpage-display condition=\"inventory-has Ancient Spell!=true\" Wrong Magical Answer>");
        acceptChallengePage.subpages.put("INPUT magical-answer=TIME", timeSubpage);
        Story wrongAnswerSubpage = new Story();
        wrongAnswerSubpage.contents.add("<subpage-display Wrong Answer>");
        acceptChallengePage.subpages.put("INPUT answer", wrongAnswerSubpage);
        Story wrongMagicalAnswerSubpage = new Story();
        wrongMagicalAnswerSubpage.contents.add("<subpage-display Wrong Magical Answer>");
        acceptChallengePage.subpages.put("INPUT magical-answer", wrongMagicalAnswerSubpage);
        wrongMagicalAnswerSubpage = new Story();
        wrongMagicalAnswerSubpage.contents.add("<player-symbol> YOU: <quote><variable condition=\"inventory-has Ancient Spell=true\" magical-answer><mask condition=\"inventory-has Ancient Spell!=true\" * magical-answer><quote>");
        wrongMagicalAnswerSubpage.contents.add("<br>");
        wrongMagicalAnswerSubpage.contents.add("\uD83D\uDE38 PROFESSOR FLUFF: <quote>Aha!  It appears that I have bested you!  That answer is INCORRECT.  No one is as clever as I!<quote>");
        acceptChallengePage.subpages.put("Wrong Magical Answer", wrongMagicalAnswerSubpage);
        wrongAnswerSubpage = new Story();
        wrongAnswerSubpage.contents.add("<br>");
        wrongAnswerSubpage.contents.add("<player-symbol> YOU: <quote><variable answer><quote>");
        wrongAnswerSubpage.contents.add("<br>");
        wrongAnswerSubpage.contents.add("\uD83D\uDE38 PROFESSOR FLUFF: <quote>Aha!  It appears that I have bested you!  That answer is INCORRECT.  No one is as clever as I!<quote>");
        acceptChallengePage.subpages.put("Wrong Answer", wrongAnswerSubpage);
        Story correctAnswerSubpage = new Story();
        correctAnswerSubpage.contents.add("<br>");
        correctAnswerSubpage.contents.add("<play-sound /assets/sounds/fluff.wav false><player-symbol> YOU: <quote><variable answer><quote>");
        correctAnswerSubpage.contents.add("<br>");
        correctAnswerSubpage.contents.add("\uD83D\uDE38 PROFESSOR FLUFF: <quote>Yes!  That is most correct!  Alas, you have bested me... and that was my finest riddle!  Very well then.  A deal is a deal.  The magical name you seek is... SHMEBULOCK.<quote>");
        acceptChallengePage.subpages.put("Correct Answer", correctAnswerSubpage);

        // TODO - Need to echo back the answer, but for now there's not enough room.  This could be refactored to be multiple pages.
        Page correctMagicalAnswerPage = new Page();
        correctMagicalAnswerPage.previousPageName = "Enter Ski Lodge";
        correctMagicalAnswerPage.story.contents.add("<first-page><color 0+0+0><play-sound /assets/sounds/fluff.wav false>");
        correctMagicalAnswerPage.story.contents.add("\uD83D\uDE38 PROFESSOR FLUFF: <quote>Yes!  That is most correct!  Alas, you have bested me... though that perhaps was <i>not</i> my finest riddle!  Very well then.  A deal is a deal.");
        correctMagicalAnswerPage.story.contents.add("<br>");
        correctMagicalAnswerPage.story.contents.add("You see, this world is a very interesting place.  Very interesting indeed.  Time exists separately in seven levels.  Each level is in fact the same level as those above and below it... but... they exist in a different time.  Mylee knows this.  That's why she lives in an elevator along the outside of this world... so she can travel between different times and never age.");
        correctMagicalAnswerPage.story.contents.add("<br>");
        correctMagicalAnswerPage.story.contents.add("And therein lies the secret... Mylee's brother also knows this!  But he does not travel through the elevator.  He travels... through <i>the stairwell</i>.  What is this <i>the stairwell</i> you ask?  It is the seemingly vacant space that exists on either side of the elevator.  Anyone can access it.  The trick is simply to face it while holding a <inventory Golden Bunny>, cast the spell 'DOWN THE RABBIT HOLE' (requires 25MP), and a doorway will appear.<quote>");
        correctMagicalAnswerPage.story.contents.add("<br>");
        correctMagicalAnswerPage.story.contents.add("<player-symbol> YOU: <quote>A <inventory Golden Bunny>?  So does that mean Big Chung has a <inventory Golden Bunny>?<quote>");
        correctMagicalAnswerPage.story.contents.add("<br>");
        correctMagicalAnswerPage.story.contents.add("\uD83D\uDE38 PROFESSOR FLUFF: <quote>No... not exactly.  Though he has certainly eaten more than his fair share of those cute bunnies and thus wields their powerful magic.<quote>");
        correctMagicalAnswerPage.story.contents.add("<br>");
        correctMagicalAnswerPage.story.contents.add("<player-symbol> YOU: How sad!");
        correctMagicalAnswerPage.story.contents.add("<br>");
        correctMagicalAnswerPage.story.contents.add("\uD83D\uDE38 PROFESSOR FLUFF: <quote>Indeed!<quote></color>");
        correctMagicalAnswerPage.story.contents.add("<second-page>");
        correctMagicalAnswerPage.story.contents.add("<image fluff2 center /assets/images/fluff2.jpg>");
        mountFluff.pages.put("Correct Magical Answer", correctMagicalAnswerPage);
        
        // TODO - Split once for each player and display button based on whether player has item in inventory and if not, disable if player doesn't have gold and needs gold
        Scene mysteryRoom = new Scene();
        mysteryRoom.color = new Color(184, 115, 51);
        mysteryRoom.firstPageName = "main";
        mysteryRoom.stopOtherSounds = true;
        mysteryRoom.soundFileName = "";
        mysteryRoom.symbol = "\u2754";
        mysteryRoom.x = 1;
        mysteryRoom.y = 3;
        chapter1.scenes.put("MYSTERY ROOM", mysteryRoom);
        
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display condition=\"player=Shmebulock\" Shmebulock Subpage>");
        mainPage.story.contents.add("<play-sound condition=\"player=Greyson\" /assets/sounds/grey.mp3 true>");
        mainPage.story.contents.add("<subpage-display condition=\"player=Greyson\" Greyson Subpage>");
        mainPage.story.contents.add("<play-sound condition=\"player=Zara\" /assets/sounds/zara.mp3 true>");
        mainPage.story.contents.add("<subpage-display condition=\"player=Zara\" Zara Subpage>");
        mysteryRoom.pages.put("main", mainPage);
        
        Story starChildSubpage = new Story();
        starChildSubpage.isSpell = true;
        starChildSubpage.mpCost = 100;
        starChildSubpage.contents.add("<variable-set star-child true>");
        starChildSubpage.contents.add("<page-refresh>");
        mysteryRoom.subpages.put("STAR CHILD", starChildSubpage);
        
        Story shmebulockSubpage = new Story();
        shmebulockSubpage.contents.add("<subpage-display condition=\"inventory-has Cosmic Wonder #1!=true\" Shmebulock No Item Subpage>");
        shmebulockSubpage.contents.add("<subpage-display condition=\"inventory-has Cosmic Wonder #1=true\" Shmebulock Has Item Subpage>");
        mainPage.subpages.put("Shmebulock Subpage", shmebulockSubpage);
        
        Story shmebulockNoItemSubpage = new Story();
        shmebulockNoItemSubpage.contents.add("<subpage-display Scene Header>");
        shmebulockNoItemSubpage.contents.add("<first-page><br><br><br><color 184+115+51><play-sound /assets/sounds/cosmic-wonder-1.mp3 true>");
        shmebulockNoItemSubpage.contents.add("You look down and are surprised to find that you're wearing a black cloak.  The sky gives way to a vision of ancient stars and before you appears a giant black monolith.");
        shmebulockNoItemSubpage.contents.add("<br>");
        shmebulockNoItemSubpage.contents.add("The monolith fills you with fear.  Such amazing power!  It feels like the fantasy that you have known to be truth has been peeled away.");
        shmebulockNoItemSubpage.contents.add("<br>");
        shmebulockNoItemSubpage.contents.add("<if condition=\"inventory-has Gold!=true\" Perhaps if you had some Gold you could cast a powerful spell to trap the monolith.><if condition=\"inventory-has Gold=true\" Perhaps could cast a powerful spell to trap the monolith using your Gold?  Try casting spell 'STAR CHILD' (requires 100MP).>");
        shmebulockNoItemSubpage.contents.add("<br>");
        shmebulockNoItemSubpage.contents.add("<get-validated-input condition=\"star-child!=true\" action Listen+!Take Monolith><get-validated-input condition=\"star-child=true\" action Listen+Take Monolith>");
        shmebulockNoItemSubpage.contents.add("<subpage-display Navigation Footer>");
        shmebulockNoItemSubpage.contents.add("<second-page>");
        shmebulockNoItemSubpage.contents.add("<image cosmic-wonder-1 center /assets/images/cosmic-wonder-1.png>");
        mainPage.subpages.put("Shmebulock No Item Subpage", shmebulockNoItemSubpage);
        
        listenSubpage = new Story();
        listenSubpage.contents.add("<goto-page condition=\"player=Shmebulock\" Shmebulock Listen>");
        listenSubpage.contents.add("<goto-page condition=\"player=Greyson\" Greyson Listen>");
        listenSubpage.contents.add("<goto-page condition=\"player=Zara\" Zara Listen>");
        mainPage.subpages.put("INPUT action=Listen", listenSubpage);
        
        listenPage = new Page();
        listenPage.previousPageName = "main";
        listenPage.story.contents.add("<color 184+115+51><if condition=\"inventory-has Cosmic Wonder #1!=true\" You hear the cold vacuum filled with some sort of monstrous intelligence.  Was it designed?  Or is it the very manifestation of design?><if condition=\"inventory-has Cosmic Wonder #1=true\" You hear nothing.  The void emits no sound.  Or perhaps the void doesn't exist?  Or perhaps you don't exist?>");
        listenPage.story.contents.add("<br>");
        listenPage.story.contents.add("You mustn't stay here long.  Cosmic wonders like this are not to be trusted.</color>");
        listenPage.story.contents.add("<second-page>");
        listenPage.story.contents.add("<image condition=\"inventory-has Cosmic Wonder #1!=true\" cosmic-wonder-1 center /assets/images/cosmic-wonder-1.png><image condition=\"inventory-has Cosmic Wonder #1=true\" cosmic-wonder-void center /assets/images/cosmic-wonder-void.jpg>");
        mysteryRoom.pages.put("Shmebulock Listen", listenPage);
        
        Story takeMonolithSubpage = new Story();
        takeMonolithSubpage.contents.add("<stop-sound><inventory-add true Cosmic Wonder #1><page-refresh>");
        mainPage.subpages.put("INPUT action=Take Monolith", takeMonolithSubpage);
        
        Story shmebulockHasItemSubpage = new Story();
        shmebulockHasItemSubpage.contents.add("<subpage-display Scene Header>");
        shmebulockHasItemSubpage.contents.add("<first-page><color 184+115+51><br><br><br>");
        shmebulockHasItemSubpage.contents.add("The wonderous engeries of the cosmos coalesce as though you are standing in some sort of nexus.");
        shmebulockHasItemSubpage.contents.add("<br>");
        shmebulockHasItemSubpage.contents.add("There is nothing more you can do here.  You should leave this place before you become trapped inside infinity itself.");
        shmebulockHasItemSubpage.contents.add("<br>");
        shmebulockHasItemSubpage.contents.add("<get-validated-input action Listen>");
        shmebulockHasItemSubpage.contents.add("<subpage-display Navigation Footer>");
        shmebulockHasItemSubpage.contents.add("<second-page>");
        shmebulockHasItemSubpage.contents.add("<image cosmic-wonder-void center /assets/images/cosmic-wonder-void.jpg>");
        mainPage.subpages.put("Shmebulock Has Item Subpage", shmebulockHasItemSubpage);
        
        Story greysonSubpage = new Story();
        greysonSubpage.contents.add("<subpage-display condition=\"inventory-has UNO Reverse!=true\" Greyson No Item Subpage>");
        greysonSubpage.contents.add("<subpage-display condition=\"inventory-has UNO Reverse=true\" Greyson Has Item Subpage>");
        mainPage.subpages.put("Greyson Subpage", greysonSubpage);
        
        Story greysonNoItemSubpage = new Story();
        greysonNoItemSubpage.contents.add("<subpage-display Scene Header>");
        greysonNoItemSubpage.contents.add("<first-page><br><br><br><color 184+115+51>");
        greysonNoItemSubpage.contents.add("You've stumbled into Greyson's Arcade!!!");
        greysonNoItemSubpage.contents.add("<br>");
        greysonNoItemSubpage.contents.add("Oh my!  Every game in the world can be found for sale here.");
        greysonNoItemSubpage.contents.add("<br>");
        greysonNoItemSubpage.contents.add("<if condition=\"inventory-has Gold!=true\" A particular game catches your eye... UNO.  But the price for such a coveted game is a gold coin.  If only you had some Gold!><if condition=\"inventory-has Gold=true\" A particular game catches your eye... UNO.  The purchase price is a gold coin.>");
        greysonNoItemSubpage.contents.add("<br>");
        greysonNoItemSubpage.contents.add("<get-validated-input condition=\"inventory-has Gold=true\" action Listen+Buy UNO Reverse><get-validated-input condition=\"inventory-has Gold!=true\" action Listen+!Buy UNO Reverse>");
        greysonNoItemSubpage.contents.add("<subpage-display Navigation Footer>");
        greysonNoItemSubpage.contents.add("<second-page>");
        greysonNoItemSubpage.contents.add("<image arcade center /assets/images/arcade.jpg>");
        mainPage.subpages.put("Greyson No Item Subpage", greysonNoItemSubpage);
        
        listenPage = new Page();
        listenPage.previousPageName = "main";
        listenPage.story.contents.add("<color 184+115+51>You hear the typical electronic bleeps and bloops of an arcade.  And perhaps... a little magic?</color>");
        listenPage.story.contents.add("<second-page>");
        listenPage.story.contents.add("<image arcade center /assets/images/arcade.jpg>");
        mysteryRoom.pages.put("Greyson Listen", listenPage);
        
        Story buyUNOReverseSubpage = new Story();
        buyUNOReverseSubpage.contents.add("<inventory-add true UNO Reverse><page-refresh>");
        mainPage.subpages.put("INPUT action=Buy UNO Reverse", buyUNOReverseSubpage);
        
        Story greysonHasItemSubpage = new Story();
        greysonHasItemSubpage.contents.add("<subpage-display Scene Header>");
        greysonHasItemSubpage.contents.add("<first-page><color 184+115+51><br><br><br>");
        greysonHasItemSubpage.contents.add("You've stumbled into Greyson's Arcade!!!");
        greysonHasItemSubpage.contents.add("<br>");
        greysonHasItemSubpage.contents.add("This is a fun place.  Remember that time you bought an UNO deck?");
        greysonHasItemSubpage.contents.add("<br>");
        greysonHasItemSubpage.contents.add("<get-validated-input action Listen>");
        greysonHasItemSubpage.contents.add("<subpage-display Navigation Footer>");
        greysonHasItemSubpage.contents.add("<second-page>");
        greysonHasItemSubpage.contents.add("<image arcade center /assets/images/arcade.jpg>");
        mainPage.subpages.put("Greyson Has Item Subpage", greysonHasItemSubpage);
        
        Story zaraSubpage = new Story();
        zaraSubpage.contents.add("<subpage-display condition=\"inventory-has Zara's Sword!=true\" Zara No Item Subpage>");
        zaraSubpage.contents.add("<subpage-display condition=\"inventory-has Zara's Sword=true\" Zara Has Item Subpage>");
        mainPage.subpages.put("Zara Subpage", zaraSubpage);
        
        Story zaraNoItemSubpage = new Story();
        zaraNoItemSubpage.contents.add("<subpage-display Scene Header>");
        zaraNoItemSubpage.contents.add("<first-page><br><br><br><color 184+115+51>");
        zaraNoItemSubpage.contents.add("You've stumbled into Zara's Store!!!");
        zaraNoItemSubpage.contents.add("<br>");
        zaraNoItemSubpage.contents.add("There are a lot of fun things to buy.  You see clothes, foods, and weapons...");
        zaraNoItemSubpage.contents.add("<br>");
        zaraNoItemSubpage.contents.add("<if condition=\"inventory-has Gold!=true\" A sword leaning inside a display case catches your eyes.  Perhaps if you had a gold coin you could afford it.><if condition=\"inventory-has Gold=true\" A sword leaning inside a display case catches your eyes.  It only costs a single gold coin.  Should you buy it?>");
        zaraNoItemSubpage.contents.add("<br>");
        zaraNoItemSubpage.contents.add("<get-validated-input condition=\"inventory-has Gold!=true\" action Listen+!Buy Sword><get-validated-input condition=\"inventory-has Gold=true\" action Listen+Buy Sword>");
        zaraNoItemSubpage.contents.add("<subpage-display Navigation Footer>");
        zaraNoItemSubpage.contents.add("<second-page>");
        zaraNoItemSubpage.contents.add("<image store center /assets/images/store.jpg>");
        mainPage.subpages.put("Zara No Item Subpage", zaraNoItemSubpage);
        
        listenPage = new Page();
        listenPage.previousPageName = "main";
        listenPage.story.contents.add("<color 184+115+51>You hear the typical sounds of a hustling and bustling store.  A cash register.  The voices of the shoppers.  And perhaps... a little magic?");
        listenPage.story.contents.add("<second-page>");
        listenPage.story.contents.add("<image store center /assets/images/store.jpg>");
        mysteryRoom.pages.put("Zara Listen", listenPage);
        
        Story buySwordSubpage = new Story();
        buySwordSubpage.contents.add("<inventory-add true Zara's Sword><page-refresh>");
        mainPage.subpages.put("INPUT action=Buy Sword", buySwordSubpage);
        
        Story zaraHasItemSubpage = new Story();
        zaraHasItemSubpage.contents.add("<subpage-display Scene Header>");
        zaraHasItemSubpage.contents.add("<first-page><color 184+115+51><br><br><br>");
        zaraHasItemSubpage.contents.add("You've stumbled into Zara's Store!!!");
        zaraHasItemSubpage.contents.add("<br>");
        zaraHasItemSubpage.contents.add("There are a lot of fun things to buy.  You see clothes, foods, and weapons...");
        zaraHasItemSubpage.contents.add("<br>");
        zaraHasItemSubpage.contents.add("But remember-- you are on a mission and there are others things you should be doing!");
        zaraHasItemSubpage.contents.add("<br>");
        zaraHasItemSubpage.contents.add("<get-validated-input action Listen>");
        zaraHasItemSubpage.contents.add("<subpage-display Navigation Footer>");
        zaraHasItemSubpage.contents.add("<second-page>");
        zaraHasItemSubpage.contents.add("<image store center /assets/images/store.jpg>");
        mainPage.subpages.put("Zara Has Item Subpage", zaraHasItemSubpage);
        
        Scene castle = new Scene();
        castle.color = new Color(171, 145, 68);
        castle.firstPageName = "main";
        castle.stopOtherSounds = true;
        castle.soundFileName = "/assets/sounds/castle.mp3";
        castle.symbol = "\uD83C\uDFF0";
        castle.x = 2;
        castle.y = 3;
        chapter1.scenes.put("CASTLE", castle);
        
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<first-page><color 171+145+68><br><br><br>");
        mainPage.story.contents.add("What a magnificent castle!  You step inside and explore the castle but no one is home.  Perhaps the royal court fled because of Big Chung?");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("<if condition=\"inventory-has Gold!=true\" You see beautiful gold coins scattered around the maze-like interior.  They lead up to a chest overflowing with gold coins.  Will you take the gold?><if condition=\"inventory-has Gold=true\" Such an empty place!>");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("<get-validated-input condition=\"inventory-has Gold!=true\" action Listen+Take Gold><get-validated-input condition=\"inventory-has Gold=true\" action Listen>");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        mainPage.story.contents.add("</color>");
        mainPage.story.contents.add("<second-page>");
        mainPage.story.contents.add("<image castle-interior center /assets/images/castle-interior.jpg>");
        mainPage.story.contents.add("<second-page><br><br><br><br><br><br><br><br><br><br><br><br>");
        mainPage.story.contents.add("<image condition=\"inventory-has Gold!=true\" gold center /assets/images/gold.png>");
        castle.pages.put("main", mainPage);
        
        listenSubpage = new Story();
        listenSubpage.contents.add("<goto-page Listen>");
        mainPage.subpages.put("INPUT action=Listen", listenSubpage);
        
        listenPage = new Page();
        listenPage.previousPageName = "main";
        listenPage.story.contents.add("<color 171+145+68>You hear royal music... A harp!  A guitar!  And also the deep sound of a tuba.  How splendid!  The music carries loudly from the castle courtyard.  However, you hear nothing here within the castle.</color>");
        listenPage.story.contents.add("</color>");
        listenPage.story.contents.add("<second-page>");
        listenPage.story.contents.add("<image castle-interior center /assets/images/castle-interior.jpg>");
        listenPage.story.contents.add("<second-page><br><br><br><br><br><br><br><br><br><br><br><br>");
        listenPage.story.contents.add("<image condition=\"inventory-has Gold!=true\" gold center /assets/images/gold.png>");
        castle.pages.put("Listen", listenPage);
        
        Story takeGoldSubpage = new Story();
        takeGoldSubpage.contents.add("<inventory-add true Gold>");
        takeGoldSubpage.contents.add("<page-refresh>");
        mainPage.subpages.put("INPUT action=Take Gold", takeGoldSubpage);
        
        Scene dragonsDen = new Scene();
        dragonsDen.color = new Color(139, 0, 0);
        dragonsDen.firstPageName = "main";
        dragonsDen.stopOtherSounds = true;
        dragonsDen.soundFileName = "/assets/sounds/dragons-den.mp3";
        dragonsDen.symbol = "\uD83D\uDC09";
        dragonsDen.x = 3;
        dragonsDen.y = 3;
        chapter1.scenes.put("DRAGON'S DEN", dragonsDen);
        
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<stop-sound condition=\"variable is-dragon-defeated=true\" /assets/sounds/dragons-den.mp3><color 139+0+0>");
        // TODO - After defeating the dragon, a sub-page should have different text
        mainPage.story.contents.add("You come across a massive cave.  After climbing inside you're able to pick out some detail.");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("<if condition=\"variable is-dragon-defeated!=true\" It's hot in here!  The walls of the cave look molten, as though the intense heat is causing the cave to melt.  Ahead, flames lick the sides of the walls.><if condition=\"variable is-dragon-defeated=true\" It feels much cooler inside the cave without that pesky dragon!>");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("<if condition=\"variable is-dragon-defeated!=true\" Perhaps there is treasure just around the corner.  After all, this would be a perfect place to hide something of value.  Or... there could also be danger.><if condition=\"variable is-dragon-defeated=true\" This cave is empty.  There is nothing more to do here.>");
        mainPage.story.contents.add("</color>");
        mainPage.story.contents.add("<get-validated-input condition=\"variable is-dragon-defeated!=true\" action Listen+Explore Cave><get-validated-input condition=\"variable is-dragon-defeated=true\" action Listen>");
        mainPage.story.contents.add("<br>");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        mainPage.story.contents.add("<second-page>");
        mainPage.story.contents.add("<image condition=\"variable is-dragon-defeated!=true\" dragons-den.jpg center /assets/images/dragons-den.jpg><image condition=\"variable is-dragon-defeated=true\" dragons-den-empty center /assets/images/dragons-den-empty.jpg>");
        dragonsDen.pages.put("main", mainPage);
        listenSubpage = new Story();
        listenSubpage.contents.add("<goto-page Listen>");
        mainPage.subpages.put("INPUT action=Listen", listenSubpage);
        Story exploreCaveSubpage = new Story();
        exploreCaveSubpage.contents.add("<goto-page Pre-Dragon>");
        mainPage.subpages.put("INPUT action=Explore Cave", exploreCaveSubpage);
        
        listenPage = new Page();
        listenPage.previousPageName = "main";
        listenPage.story.contents.add("<color 139+0+0><if condition=\"variable is-dragon-defeated!=true\" You hear the ambient sounds of the cave.  And fire.  And... perhaps the breathing of an angry beast?><if condition=\"variable is-dragon-defeated=true\" You hear the silence of the cave.  And nothing more.>");
        listenPage.story.contents.add("</color>");
        listenPage.story.contents.add("<second-page>");
        listenPage.story.contents.add("<image condition=\"variable is-dragon-defeated!=true\" dragons-den center /assets/images/dragons-den.jpg><image condition=\"variable is-dragon-defeated=true\" dragons-den-empty center /assets/images/dragons-den-empty.jpg>");
        dragonsDen.pages.put("Listen", listenPage);
        
        // Dragon Battle:
        // - Event window 1 (no flame): 
        //  - Enable Sword button and display sword.png overlay if pressed (and reduce dragon HP in variable)
        //  - Disable UNO Reverse button and disable cosmic terror button if no damage dealt last event
        // - Event window 2 (small flame):
        //  - If dragon damage not dealt last event, display flame (flame.png)
        //  - Disable sword button and disable cosmic terror button if no damage dealt last event
        //  - Enable UNO Reverse button and display UNO-reverse.png overlay if pressed (and reduce dragon HP in variable)
        // - Event window 3 (large flame and impact):
        //  - Disable Sword and UNO Reverse buttons
        //  - If dragon damage not dealt last two events, reduce HP (and display standard red overlay)
        //  - If dragon damage dealt, enable Cosmic Terror button and display black overlay (and reduce dragon HP in variable)
        // - Difficulty determines strength of dragon impact as well as damange player can inflict on dragon:
        //  - Easy=player attack deals 100% damage, dragon attack deals 25% damage
        //  - Normal=player attack deals 50% damage, dragon attack deals 50% damage
        //  - Hard=player attack deals 25% damage, dragon attack deals 100% damage
        //  - Shmebulock's hits and dragon's hits both yeild 25% damage
        //  - Shmebulock attack causes temporary black overlay
        
        // TODO - Finish integrating this page
        Page preDragonPage = new Page();
        preDragonPage = new Page();
        preDragonPage.story.contents.add("<subpage-display Player Stats><br><color 139+0+0>You begin exploring the cave when something jumps out at you...");
        preDragonPage.story.contents.add("<br>");
        preDragonPage.story.contents.add("Oh no!  It's a dragon!!!");
        preDragonPage.story.contents.add("<br>");
        preDragonPage.story.contents.add("<if condition=\"inventory-has Gold!=true\" Thankfully you have nothing heavy to weigh you down so you have the option to run away...><if condition=\"inventory-has Gold=true\" Because of your heavy Gold you can't run away.  ><if condition=\"inventory-has UNO Reverse=true\" You need to stand your ground and fight!  You pull out your trusty UNO Reverse...><if condition=\"inventory-has Zara's Sword=true\" You need to stand your ground and fight!  You unsheathe your trusty sword.><if condition=\"inventory-has Cosmic Wonder #1=true\" You need to stand your ground and fight!  You focus on the cosmic wonder you collected and make manifest a terrible fear...>");
        preDragonPage.story.contents.add("<br>");
        preDragonPage.story.contents.add("<i><if condition=\"inventory-has UNO Reverse=true\" INSTRUCTIONS: Be prepared to attack as soon as the dragon launches its flames at you.  You need to redirect its attack back at it BEFORE the flames hit you.><if condition=\"inventory-has Zara's Sword=true\" INSTRUCTIONS: Be prepared to attack BEFORE the dragon launches its flames at you.  Strike as soon as you can.><if condition=\"inventory-has Cosmic Wonder #1=true\" INSTRUCTIONS: Be prepared to attack as soon as the dragon's flames hit you.  Your cosmic wonder can punish the dragon for its malice.> </i>");
        preDragonPage.story.contents.add("</color>");
        preDragonPage.story.contents.add("<get-validated-input action *Continue>");
        preDragonPage.story.contents.add("<second-page>");
        preDragonPage.story.contents.add("MINIBOSS: Dragon");
        preDragonPage.story.contents.add("LEVEL: 1");
        preDragonPage.story.contents.add("ATTACK: Fire");
        preDragonPage.story.contents.add("STRENGTHS: Toughness, Immune to all long-range attacks");
        preDragonPage.story.contents.add("WEAKNESSES: Easily scared");
        preDragonPage.story.contents.add("<br>");
        preDragonPage.story.contents.add("<image dragon-sketch center /assets/images/dragon-sketch.png>");
        dragonsDen.pages.put("Pre-Dragon", preDragonPage);
        
        continueSubpage = new Story();
        continueSubpage.contents.add("<variable-set event 0>");
        continueSubpage.contents.add("<variable-set dragon-hp 100>");
        continueSubpage.contents.add("<variable-set dragon-attacked false>");
        continueSubpage.contents.add("<variable-set dragon-attacking false>");
        continueSubpage.contents.add("<play-sound /assets/sounds/dragon.mp3 false>");
        continueSubpage.contents.add("<variable-set brandish-weapon false>");
        continueSubpage.contents.add("<timer-start condition=\"variable event=0\" 1 Event 1>");
        continueSubpage.contents.add("<goto-page Dragon>");
        preDragonPage.subpages.put("INPUT action=Continue", continueSubpage);
        
        dragonPage = new Page();
        dragonPage.story.contents.add("<subpage-display Player Stats><br><color 139+0+0>You begin exploring the cave when something jumps out at you...");
        dragonPage.story.contents.add("<br>");
        dragonPage.story.contents.add("Oh no!  It's a dragon!!!");
        dragonPage.story.contents.add("<br>");
        dragonPage.story.contents.add("<if condition=\"inventory-has Gold!=true\" Thankfully you have nothing heavy to weigh you down so you have the option to run away...><if condition=\"inventory-has Gold=true\" Because of your heavy Gold you can't run away.  ><if condition=\"inventory-has UNO Reverse=true\" You need to stand your ground and fight!  You pull out your trusty UNO Reverse...><if condition=\"inventory-has Zara's Sword=true\" You need to stand your ground and fight!  You unsheathe your trusty sword.><if condition=\"inventory-has Cosmic Wonder #1=true\" You need to stand your ground and fight!  You focus on the cosmic wonder you collected and make manifest a terrible fear...>");
        dragonPage.story.contents.add("<br>");
        dragonPage.story.contents.add("<i><if condition=\"inventory-has UNO Reverse=true\" INSTRUCTIONS: Be prepared to attack as soon as the dragon launches its flames at you.  You need to redirect its attack back at it BEFORE the flames hit you.><if condition=\"inventory-has Zara's Sword=true\" INSTRUCTIONS: Be prepared to attack BEFORE the dragon launches its flames at you.  Strike as soon as you can.><if condition=\"inventory-has Cosmic Wonder #1=true\" INSTRUCTIONS: Be prepared to attack as soon as the dragon's flames hit you.  Your cosmic wonder can punish the dragon for its malice.> </i>");
        dragonPage.story.contents.add("</color>");
        dragonPage.story.contents.add("<subpage-display condition=\"hp!=0\" Fight Or Flight Actions>");
        dragonPage.story.contents.add("<second-page>");
        dragonPage.story.contents.add("<image dragons-den2 center /assets/images/dragons-den2.jpg>");
        dragonPage.story.contents.add("<second-page>");
        dragonPage.story.contents.add("<image condition=\"variable dragon-attacked!=true\" dragon center /assets/images/dragon.png><subpage-display condition=\"variable dragon-attacked=true\" Display Attacked Dragon>");
        dragonPage.story.contents.add("<second-page>");
        dragonPage.story.contents.add("<subpage-display condition=\"variable dragon-attacking=true\" Display Dragon Attack>");
        dragonPage.story.contents.add("<second-page>");
        dragonPage.story.contents.add("<subpage-display condition=\"variable dragon-attacked=true\" Display Weapon>");
        dragonsDen.pages.put("Dragon", dragonPage);
        
        Story displayAttackedDragonSubpage = new Story();
        displayAttackedDragonSubpage.contents.add("<image condition=\"inventory-has Cosmic Wonder #1!=true\" dragon-red center /assets/images/dragon-red.png><image condition=\"inventory-has Cosmic Wonder #1=true\" dragon-inverted center /assets/images/dragon-inverted.png>");
        dragonPage.subpages.put("Display Attacked Dragon", displayAttackedDragonSubpage);
        
        Story displayDragonAttackSubpage = new Story();
        displayDragonAttackSubpage.contents.add("<second-page><image condition=\"variable event=3\" flame center /assets/images/flame.png>");
        displayDragonAttackSubpage.contents.add("<second-page><br><br><br><br><br><br><br><image condition=\"variable event=2\" flame-small center /assets/images/flame-small.png>");
        dragonPage.subpages.put("Display Dragon Attack", displayDragonAttackSubpage);
        
        Story fightOrFlightActions = new Story();
        fightOrFlightActions.contents.add("<get-validated-input condition=\"inventory-has Gold!=true\" action Run Away><subpage-display condition=\"inventory-has Gold=true\" Handle Weapon>");
        dragonPage.subpages.put("Fight Or Flight Actions", fightOrFlightActions);

        Story runAwaySubpage = new Story();
        runAwaySubpage.contents.add("<timer-stop Event 1><timer-stop Event 2><timer-stop Event 3><goto-page main>");
        dragonPage.subpages.put("INPUT action=Run Away", runAwaySubpage);
        
        Story attackSubpage = new Story();
        attackSubpage.contents.add("<second-page>");
        attackSubpage.contents.add("<play-sound condition=\"inventory-has UNO Reverse=true\" /assets/sounds/reverse.wav false>");
        attackSubpage.contents.add("<play-sound condition=\"inventory-has Zara's Sword=true\" /assets/sounds/sword.wav false>");
        attackSubpage.contents.add("<play-sound condition=\"inventory-has Cosmic Wonder #1=true\" /assets/sounds/cosmic-wonder-1.wav false>");
        attackSubpage.contents.add("<variable-add condition=\"variable difficulty=Easy\" dragon-hp -50>");
        attackSubpage.contents.add("<variable-add condition=\"variable difficulty=Normal\" dragon-hp -25>");
        attackSubpage.contents.add("<variable-add condition=\"variable difficulty=Hard\" dragon-hp -10>");
        attackSubpage.contents.add("<variable-add condition=\"variable difficulty=Magical\" dragon-hp -25>");
        attackSubpage.contents.add("<play-sound /assets/sounds/dragon-hurt.wav false>");
        attackSubpage.contents.add("<variable-set dragon-attacked true>");
        attackSubpage.contents.add("<variable-set brandish-weapon false>");
        attackSubpage.contents.add("<variable-set dragon-attacking false>");    // UNO Reverse will cancel the attack
        attackSubpage.contents.add("<play-sound condition=\"variable dragon-hp=0\" /assets/sounds/victory.mp3 false>");
        attackSubpage.contents.add("<play-sound condition=\"variable dragon-hp=0\" /assets/sounds/dragon.mp3 false>");
        attackSubpage.contents.add("<play-sound condition=\"variable dragon-hp=0\" /assets/sounds/heavy-wings.mp3 false>");
        attackSubpage.contents.add("<variable-set condition=\"variable dragon-hp=0\" is-dragon-defeated true>");
        attackSubpage.contents.add("<page-refresh>");
        attackSubpage.contents.add("<mp-change condition=\"inventory-has Cosmic Wonder #1=true\" -10 false>");
        attackSubpage.contents.add("<timer-start condition=\"variable dragon-hp=0\" 1 Event 4>");
        attackSubpage.contents.add("<goto-page condition=\"variable dragon-hp=0\" Dragon Defeated>");
        dragonPage.subpages.put("Attack", attackSubpage);
        
        Story displayWeaponSubpage = new Story();
        displayWeaponSubpage.contents.add("<image condition=\"inventory-has UNO Reverse=true\" UNO-reverse center /assets/images/UNO-reverse.png>");
        displayWeaponSubpage.contents.add("<image condition=\"inventory-has Zara's Sword=true\" sword center /assets/images/sword.png>");
        dragonPage.subpages.put("Display Weapon", displayWeaponSubpage);

        Story playerHitSubpage = new Story();
        playerHitSubpage.contents.add("<hp-change condition=\"variable difficulty=Easy\" -25 false a dragon>");
        playerHitSubpage.contents.add("<hp-change condition=\"variable difficulty=Normal\" -50 false a dragon>");
        playerHitSubpage.contents.add("<hp-change condition=\"variable difficulty=Hard\" -100 false a dragon>");
        playerHitSubpage.contents.add("<hp-change condition=\"variable difficulty=Magical\" -25 false a dragon>");
        playerHitSubpage.contents.add("<variable-set player-hit true>");
        dragonPage.subpages.put("Player Hit", playerHitSubpage);
        
        Story handleWeaponSubpage = new Story();
        handleWeaponSubpage.contents.add("<subpage-display condition=\"player=Greyson\" Handle Greyson Weapon>");
        handleWeaponSubpage.contents.add("<subpage-display condition=\"player=Zara\" Handle Zara Weapon>");
        handleWeaponSubpage.contents.add("<subpage-display condition=\"player=Shmebulock\" Handle Shmebulock Weapon>");
        dragonPage.subpages.put("Handle Weapon", handleWeaponSubpage);
        
        Story handleGreysonWeaponSubpage = new Story();
        handleGreysonWeaponSubpage.contents.add("<get-validated-input condition=\"variable brandish-weapon!=true\" action !Run Away+!UNO Reverse><get-validated-input condition=\"variable brandish-weapon=true\" action !Run Away+UNO Reverse>");
        dragonPage.subpages.put("Handle Greyson Weapon", handleGreysonWeaponSubpage);
        Story handleGreysonAttackSubpage = new Story();
        handleGreysonAttackSubpage.contents.add("<subpage-display Attack>");
        dragonPage.subpages.put("INPUT action=UNO Reverse", handleGreysonAttackSubpage);
        
        Story handleZaraWeaponSubpage = new Story();
        handleZaraWeaponSubpage.contents.add("<get-validated-input condition=\"variable brandish-weapon!=true\" action !Run Away+!Sword><get-validated-input condition=\"variable brandish-weapon=true\" action !Run Away+Sword>");
        dragonPage.subpages.put("Handle Zara Weapon", handleZaraWeaponSubpage);
        Story handleZaraAttackSubpage = new Story();
        handleZaraAttackSubpage.contents.add("<subpage-display Attack>");
        dragonPage.subpages.put("INPUT action=Sword", handleZaraAttackSubpage);
        
        Story handleShmebulockWeaponSubpage = new Story();
        handleShmebulockWeaponSubpage.contents.add("<get-validated-input condition=\"variable brandish-weapon!=true\" action !Run Away+!Cosmic Terror (requires 10MP)><get-validated-input condition=\"variable brandish-weapon=true\" action !Run Away+Cosmic Terror (requires 10MP)>");
        dragonPage.subpages.put("Handle Shmebulock Weapon", handleShmebulockWeaponSubpage);
        Story handleShmebulockAttackSubpage = new Story();
        handleShmebulockAttackSubpage.contents.add("<subpage-display Attack>");
        dragonPage.subpages.put("INPUT action=Cosmic Terror (requires 10MP)", handleShmebulockAttackSubpage);
        
        Story event1Subpage = new Story();
        event1Subpage.contents.add("<variable-set event 1>");
        //event1Subpage.contents.add("<remove cosmic-terror>");
        event1Subpage.contents.add("<variable-set dragon-attacking false>");
        event1Subpage.contents.add("<variable-set condition=\"inventory-has UNO Reverse=true\" brandish-weapon false>");
        event1Subpage.contents.add("<variable-set condition=\"inventory-has Zara's Sword=true\" brandish-weapon true>");
        event1Subpage.contents.add("<subpage-display condition=\"inventory-has Cosmic Wonder #1=true\" Check Shmebulock's Weapon 2>");
        event1Subpage.contents.add("<page-refresh>");
        event1Subpage.contents.add("<timer-start condition=\"hp!=0\" 1 Event 2>");
        dragonPage.subpages.put("TIMER Event 1", event1Subpage);
        
        Story event2Subpage = new Story();
        event2Subpage.contents.add("<variable-set event 2>");
        event2Subpage.contents.add("<remove cosmic-terror>");
        event2Subpage.contents.add("<variable-set player-hit false>");
        event2Subpage.contents.add("<variable-set condition=\"variable dragon-attacked!=true\" dragon-attacking true>");    // Dragon only attacks if not attacked the last round
        event2Subpage.contents.add("<subpage-display condition=\"inventory-has Cosmic Wonder #1=true\" Check Shmebulock's Weapon 2>");
        event2Subpage.contents.add("<variable-set dragon-attacked false>");
        event2Subpage.contents.add("<variable-set condition=\"inventory-has UNO Reverse=true\" brandish-weapon true>");
        event2Subpage.contents.add("<variable-set condition=\"inventory-has Zara's Sword=true\" brandish-weapon false>");
        event2Subpage.contents.add("<page-refresh>");
        event2Subpage.contents.add("<timer-start 1 Event 3>");
        dragonPage.subpages.put("TIMER Event 2", event2Subpage);
        
        Story event3Subpage = new Story();
        event3Subpage.contents.add("<variable-set event 3>");
        event3Subpage.contents.add("<remove cosmic-terror>");
        event3Subpage.contents.add("<variable-set condition=\"inventory-has UNO Reverse=true\" brandish-weapon false>");
        event3Subpage.contents.add("<variable-set condition=\"inventory-has Zara's Sword=true\" brandish-weapon false>");
        event3Subpage.contents.add("<subpage-display condition=\"inventory-has Cosmic Wonder #1=true\" Check Shmebulock's Weapon 0>");
        event3Subpage.contents.add("<page-refresh>");
        event3Subpage.contents.add("<subpage-display condition=\"variable dragon-attacking=true\" Player Hit>");
        event3Subpage.contents.add("<timer-start 1 Event 1>");
        dragonPage.subpages.put("TIMER Event 3", event3Subpage);
        
        // TODO - Only offer Cosmic Terror if the player has at least 10 MP
        // During event 3 when the dragon successfully attacks Shmebulock, Cosmic Terror is unleashed. 
        // If Shmebulock can hit the dragon during event 3, he'll have an additional chance on events 1 and 2. (Possible 3x damage for each 1x damage incurred.)
        Story checkShmebulockWeapon0Subpage = new Story();
        checkShmebulockWeapon0Subpage.contents.add("<subpage-display condition=\"mp&ge;10\" Check Shmebulock's Weapon 1>");
        dragonPage.subpages.put("Check Shmebulock's Weapon 0", checkShmebulockWeapon0Subpage);
        Story checkShmebulockWeapon1Subpage = new Story();
        checkShmebulockWeapon1Subpage.contents.add("<variable-set condition=\"variable dragon-attacking=true\" brandish-weapon true>");
        checkShmebulockWeapon1Subpage.contents.add("<variable-set condition=\"variable dragon-attacking!=true\" brandish-weapon false>");
        dragonPage.subpages.put("Check Shmebulock's Weapon 1", checkShmebulockWeapon1Subpage);
        Story checkShmebulockWeapon2Subpage = new Story();
        checkShmebulockWeapon2Subpage.contents.add("<variable-set condition=\"variable player-hit=true\" brandish-weapon true>");
        checkShmebulockWeapon2Subpage.contents.add("<variable-set condition=\"variable player-hit!=true\" brandish-weapon false>");
        dragonPage.subpages.put("Check Shmebulock's Weapon 2", checkShmebulockWeapon2Subpage);
        
        Page dragonDefeatedPage = new Page();
        dragonDefeatedPage.story.contents.add("<stop-sound /assets/sounds/dragons-den.mp3><timer-stop Event 1><timer-stop Event 2><timer-stop Event 3><subpage-display Player Stats><br><color 139+0+0>The dragon cries in pain and flies out through a hole in the roof of the cave.  You are finally safe!!!");
        dragonDefeatedPage.story.contents.add("<br>");
        dragonDefeatedPage.story.contents.add("You decide to explore the rest of the cave.  Towards the back you see what looks like a great hiding place for treasure.  But alas, there is no treasure to be found.");
        dragonDefeatedPage.story.contents.add("<br>");
        dragonDefeatedPage.story.contents.add("<get-validated-input condition=\"event=4\" action *Return To Cave Entrance>");
        dragonDefeatedPage.story.contents.add("</color>");
        dragonDefeatedPage.story.contents.add("<second-page>");
        dragonDefeatedPage.story.contents.add("<image dragons-den2 center /assets/images/dragons-den2.jpg>");
        dragonDefeatedPage.story.contents.add("<second-page>");
        dragonDefeatedPage.story.contents.add("<subpage-display condition=\"event!=4\" Display Attacked Dragon>");
        dragonDefeatedPage.story.contents.add("<second-page>");
        dragonDefeatedPage.story.contents.add("<subpage-display condition=\"event!=4\" Display Weapon>");
        dragonsDen.pages.put("Dragon Defeated", dragonDefeatedPage);
        
        displayAttackedDragonSubpage = new Story();
        displayAttackedDragonSubpage.contents.add("<image condition=\"inventory-has Cosmic Wonder #1!=true\" dragon-red center /assets/images/dragon-red.png><image condition=\"inventory-has Cosmic Wonder #1=true\" dragon-inverted center /assets/images/dragon-inverted.png>");
        dragonDefeatedPage.subpages.put("Display Attacked Dragon", displayAttackedDragonSubpage);
        
        displayWeaponSubpage = new Story();
        displayWeaponSubpage.contents.add("<image condition=\"inventory-has UNO Reverse=true\" UNO-reverse center /assets/images/UNO-reverse.png>");
        displayWeaponSubpage.contents.add("<image condition=\"inventory-has Zara's Sword=true\" sword center /assets/images/sword.png>");
        dragonDefeatedPage.subpages.put("Display Weapon", displayWeaponSubpage);
        
        Story event4Subpage = new Story();
        event4Subpage.contents.add("<variable-set event 4>");
        event4Subpage.contents.add("<xp-change 100 false>");
        event4Subpage.contents.add("<page-refresh>");
        event4Subpage.contents.add("<play-sound condition=\"inventory-has UNO Reverse=true\" /assets/sounds/reverse.wav false>");
        event4Subpage.contents.add("<play-sound condition=\"inventory-has Zara's Sword=true\" /assets/sounds/sword.wav false>");
        event4Subpage.contents.add("<play-sound condition=\"inventory-has Cosmic Wonder #1=true\" /assets/sounds/cosmic-wonder-1.wav false>");
        dragonDefeatedPage.subpages.put("TIMER Event 4", event4Subpage);
        
        Story returnSubpage = new Story();
        returnSubpage.contents.add("<goto-page main>");
        dragonDefeatedPage.subpages.put("INPUT action=Return To Cave Entrance", returnSubpage);
        
        Act chapter2 = new Act();
        chapter2.firstSceneName = "Chapter";
        book.acts.put("Chapter 2", chapter2);
        
        // TODO - Add Mylee's elevator with an image of an empty elevator shaft
        
        chapterScene = new Scene();
        chapterScene.firstPageName = "1";
        chapterScene.hidePageHeaders = true;
        chapterScene.nextSceneName = "HIGHWAY";
        chapterScene.soundFileName = "/assets/sounds/suspense3.wav";
        chapter2.scenes.put("Chapter", chapterScene);
        
        page1 = new Page();
        page1.story.contents.add("<image wayne-chung-dark center /assets/images/wayne-chung-dark.jpg>");
        page1.story.contents.add("<second-page>");
        page1.story.contents.add("<u>CHAPTER 2</u>");
        page1.story.contents.add("<br>");
        page1.story.contents.add("A Darkness over the Land");
        page1.story.contents.add("<set-player-direction SOUTH>");
        page1.story.contents.add("<observed-scene-add HIGHWAY>");
        page1.story.contents.add("<play-sound /assets/sounds/suspense3.wav true>");
        chapterScene.pages.put("1", page1);
        
        Scene elevator = new Scene();
        elevator.color = new Color(0, 0, 0);    // Black
        elevator.firstPageName = "main";
        elevator.soundFileName = "";    // TODO - Need way to specify that the current audio file needs to keep playing
        elevator.symbol = "\uD83D\uDED7";
        elevator.x = 2;
        elevator.y = 0;
        chapter2.scenes.put("MYLEE'S ELEVATOR", elevator);
 
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 0+0+0>");
        mainPage.story.contents.add("TODO");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        elevator.pages.put("main", mainPage);
        
        Scene grass = new Scene();
        grass.color = new Color(0, 0, 0);    // Black
        grass.firstPageName = "main";
        grass.soundFileName = "";    // TODO - Need way to specify that the current audio file needs to keep playing
        grass.symbol = "\uD83C\uDFD5";
        grass.x = 1;
        grass.y = 1;
        chapter2.scenes.put("ABANDONED CAMPGROUNDS", grass);
 
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 0+0+0>");
        mainPage.story.contents.add("TODO");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        grass.pages.put("main", mainPage);
        
        Scene road = new Scene();
        road.color = new Color(0, 0, 0);    // Black
        road.firstPageName = "main";
        road.soundFileName = "";    // TODO - Need way to specify that the current audio file needs to keep playing
        road.symbol = "\uD83D\uDEE3";
        road.x = 2;
        road.y = 1;
        chapter2.scenes.put("HIGHWAY", road);
 
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 0+0+0>");
        mainPage.story.contents.add("TODO");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        road.pages.put("main", mainPage);
        
        Scene speedway = new Scene();
        speedway.color = new Color(0, 0, 0);    // Black
        speedway.firstPageName = "main";
        speedway.soundFileName = "";    // TODO - Need way to specify that the current audio file needs to keep playing
        speedway.symbol = "\uD83C\uDFCE";
        speedway.x = 3;
        speedway.y = 1;
        chapter2.scenes.put("SPEEDWAY", speedway);
 
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 0+0+0>");
        mainPage.story.contents.add("TODO");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        speedway.pages.put("main", mainPage);
        
        Scene battleground = new Scene();
        battleground.color = new Color(0, 0, 0);    // Black
        battleground.firstPageName = "main";
        battleground.soundFileName = "";    // TODO - Need way to specify that the current audio file needs to keep playing
        battleground.symbol = "\uD83C\uDF96";
        battleground.x = 1;
        battleground.y = 2;
        chapter2.scenes.put("BATTLEGROUND", battleground);
 
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 0+0+0>");
        mainPage.story.contents.add("TODO");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        battleground.pages.put("main", mainPage);
        
        Scene forest = new Scene();
        forest.color = new Color(0, 0, 0);    // Black
        forest.firstPageName = "main";
        forest.soundFileName = "";    // TODO - Need way to specify that the current audio file needs to keep playing
        forest.symbol = "\uD83C\uDF32";
        forest.x = 2;
        forest.y = 2;
        chapter2.scenes.put("FOREST", forest);
 
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 0+0+0>");
        mainPage.story.contents.add("TODO");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        forest.pages.put("main", mainPage);
        
        Scene mountain = new Scene();
        mountain.color = new Color(0, 0, 0);    // Black
        mountain.firstPageName = "main";
        mountain.soundFileName = "";    // TODO - Need way to specify that the current audio file needs to keep playing
        mountain.symbol = "\uD83C\uDFD4";
        mountain.x = 3;
        mountain.y = 2;
        chapter2.scenes.put("MOUNT FLUFF", mountain);
 
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 0+0+0>");
        mainPage.story.contents.add("TODO");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        mountain.pages.put("main", mainPage);
        
        Scene space = new Scene();
        space.color = new Color(0, 0, 0);    // Black
        space.firstPageName = "main";
        space.soundFileName = "";    // TODO - Need way to specify that the current audio file needs to keep playing
        space.symbol = "\uD83C\uDF0C";
        space.x = 1;
        space.y = 3;
        chapter2.scenes.put("SPACE BASE", space);
 
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 0+0+0>");
        mainPage.story.contents.add("TODO");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        space.pages.put("main", mainPage);
        
        Scene fortress = new Scene();
        fortress.color = new Color(0, 0, 0);    // Black
        fortress.firstPageName = "main";
        fortress.soundFileName = "\uD83C\uDDEF";    // TODO - Need way to specify that the current audio file needs to keep playing
        fortress.symbol = "";
        fortress.x = 2;
        fortress.y = 3;
        chapter2.scenes.put("FORTRESS", fortress);
 
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 0+0+0>");
        mainPage.story.contents.add("TODO");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        fortress.pages.put("main", mainPage);
        
        Scene pickleball = new Scene();
        pickleball.color = new Color(0, 0, 0);    // Black
        pickleball.firstPageName = "main";
        pickleball.soundFileName = "";    // TODO - Need way to specify that the current audio file needs to keep playing
        pickleball.symbol = "\uD83C\uDFBE";
        pickleball.x = 3;
        pickleball.y = 3;
        chapter2.scenes.put("PICKLEBALL COURT", pickleball);
 
        mainPage = new Page();
        mainPage.story.contents.add("<subpage-display Scene Header>");
        mainPage.story.contents.add("<color 0+0+0>");
        mainPage.story.contents.add("TODO");
        mainPage.story.contents.add("<subpage-display Navigation Footer>");
        pickleball.pages.put("main", mainPage);
        
        // TODO - Review vector images of car profiles (ie, https://pixabay.com/vectors/automobile-car-gs-1300464/).
        // Add a cropped image of a chicken over the car to make it look like the chicken is driving (ie, https://pixabay.com/vectors/chicken-poultry-hen-barn-farm-40898/).
        // Could make the first minigame a frogger animation with 4 cars the player has to hop on and sync up the timing with the suspense music.
        // Each stretch would then be followed up with another with cars that are going faster
        
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
            } catch (IOException e) {
                System.err.println("Application: serializeTwinQuestBook: " + e.toString());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Application: serializeTwinQuestBook: " + e.toString());
        }
    }

}
