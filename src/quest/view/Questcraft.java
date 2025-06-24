package quest.view;

import app.ApplicationController;
import quest.model.Book;

/**
 * arrow.mp3 - "arrow-shot.mp3, An arrow being shot as part of my iPad game Knights vs Knightesses (http://versuspad.com). This sound is a simulation." by JPhilipp (https://freesound.org/people/JPhilipp/sounds/119060/)
 * book.png - "Empty book" by Darkmoon_Art (https://pixabay.com/illustrations/reserve-pages-empty-book-open-book-3057904/)
 * catapult.wav - "06177 wire launch.wav, Launching wire harpoon - simulated" by Robinhood76 (https://freesound.org/people/Robinhood76/sounds/329683/)
 * clouds.png - OpenClipart-Vectors (https://pixabay.com/vectors/sunrise-clouds-rising-sun-sky-sun-153600/)
 * compass-small.png - "Compass Rose, Wind, Directions royalty-free vector graphic. Free for use & download." by clker-free-vector-images-3736 (https://pixabay.com/vectors/compass-rose-wind-directions-305254/)
 * designer.jpg - "Digital, Binary Code, Abstract royalty-free stock illustration. Free for use & download." by wastedgeneration (https://pixabay.com/illustrations/digital-binary-code-abstract-8280790/)
 * dragon.gif - "#dragon" by Kentrius (https://gifer.com/en/6JPK)
 * elevator.wav - music elevator ext (https://freesound.org/people/Jay_You/sounds/467240/)
 * elevator-doors.png - Krolestwo_Nauki (https://pixabay.com/vectors/elevator-elevator-door-building-6232385/)
 * epic.mp3 - "Epic Trailer Background Music" by Migfus20 (https://freesound.org/people/Migfus20/sounds/560454/)
 * gold.wav - "Cash Register Purchase, Cash Register Sound by CapsLok remixed for extra depth. Created in Audacity." by Zott820 (https://freesound.org/people/Zott820/sounds/209578/)
 * map.jpg - "Paper, Stationery, Parchment royalty-free stock illustration. Free for use & download." by geralt (https://pixabay.com/illustrations/paper-stationery-parchment-old-68829/)
 * mylee.jpg - Mylee Marie by Mr. Chris
 * mystery-door.jpg - "Hello :)" by qimono (https://pixabay.com/illustrations/door-open-doorway-entrance-1590024/)
 * old-books.jpg - "Old books, Book, Old image. Free for use." by jarmoluk (https://pixabay.com/photos/old-books-book-old-library-436498/)
 * paper.wav - "Crumpled Up Paper, Me crumpling up a paper. Recorded with a Genius MIC-01A Black 3.5mm Connector Metallic Microphone." by Natty23 (https://freesound.org/people/Natty23/sounds/257272/)
 * path.jpg - "Fantasy Photosmanipulation & Digital Art. KI Bilder, Creative Commons Attribution 3.0 License" by 1tamara2 (https://pixabay.com/illustrations/ai-generated-mountains-path-world-8684181/)
 * reverse.wav - "reverse fx 12.wav, just some reverse" by reathance (https://freesound.org/people/reathance/sounds/503813/)
 * ring-of-taming.wav - "10891 magic time complete.wav", "Magic time complete sound for games and multimedia" by Robinhood76 (https://freesound.org/people/Robinhood76/sounds/614810/)
 * spider.gif - "Insect, Spider, Spider web GIF. Free for use." by Tilixia-Summer (https://pixabay.com/gifs/insect-spider-spider-web-fly-858/)
 * sword.wav - "Sword Shing 26, simple light sword scrape no ring" by Department64 (https://freesound.org/people/Department64/sounds/718807/)
 * sword-icon.png - "mini sword pixel art" by mabotoufu (https://en.ac-illust.com/clip-art/24789724/mini-sword-pixel-art)
 * turn-page.mp3 - "page turn" by partheeban (https://freesound.org/people/partheeban/sounds/457767/)
 * twins.jpg - "Image of the twins taken by Holly then manipulated by AI to make them look like video game characters" by repp
 * wayne-chung-classic.jpg - "Image of Wayne Chung manipulated by AI to make him look like a video game character" by repp
 * wayne-chung.jpg - "Image of Wayne Chung wondering what's going on" by repp
 * wilderness.mp3 - birds_210513_0088.mp3 (https://freesound.org/people/titi2/sounds/571247/)
 * wilderness3.jpg and wilderness3-small.jpg - Ai Generated Meadow Mountain royalty-free stock illustration. Free for use & download. (https://pixabay.com/illustrations/ai-generated-meadow-mountain-8190587/)
 * woods.mp3 - forestsurroundings.mp3 (https://freesound.org/people/Luftrum/sounds/48411/)
 * 
 * @author repp
 */
public class Questcraft extends app.ApplicationView {
    
    public final static String CHEATS = "Cheats";
    public final static String CRAFTING_TABLE = "Crafting Table";
    public final static String HIGH_SCORES = "High Scores";
    public final static String INVENTORY = "Inventory";
    public final static String LIBRARY = "Library";
    public final static String QUEST = "Quest";
    
    public ApplicationController appController;
    public Quest quest;
    public Cheats cheats;
    public CraftingTable craftingTable;
    public HighScores highScores;
    public Inventory inventory;
    public Library library;
    
    public Questcraft(String name) {
        super(name);
        this.addTextArea = false;
        this.backgroundImage = "/assets/images/book.png";
        this.quest = null;
        this.cheats = new Cheats(CHEATS);
        this.craftingTable = null;
        this.highScores = new HighScores(HIGH_SCORES);
        this.inventory = new Inventory(INVENTORY);
        this.library = new Library(LIBRARY);
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("Questcraft: onEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        switch(eventName) {
            case "book" -> {
                this.quest = new Quest(QUEST);
                this.quest.book = (Book) eventValue;
                this.quest.inventoryView = this.inventory;
                this.appController.addView(this.quest);
                this.inventory.quest = this.quest;
                this.appController.addView(this.inventory);
                this.highScores.setHighScores(this.quest.book.highScores);
                this.appController.addView(this.highScores);
                this.cheats.quest = this.quest;
                this.appController.addView(this.cheats);
                this.craftingTable = new CraftingTable(CRAFTING_TABLE);
                this.appController.addView(this.craftingTable);
                this.appController.displayView(this.quest);
            }
            default -> System.err.println("Library: onEvent: Unsupported event");
        }
    }

    @Override
    public void onLoad(ApplicationController appController) {
        this.appController = appController;
        this.library.addListener("book", this);
        appController.addView(this.library);
    }

}
