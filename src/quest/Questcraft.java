package quest;

import app.ApplicationController;
import app.ApplicationView;

/**
 * book.png - "Empty book" by Darkmoon_Art (https://pixabay.com/illustrations/reserve-pages-empty-book-open-book-3057904/)
 * designer.jpg - "Digital, Binary Code, Abstract royalty-free stock illustration. Free for use & download." by wastedgeneration (https://pixabay.com/illustrations/digital-binary-code-abstract-8280790/)
 * dragon.gif - "#dragon" by Kentrius (https://gifer.com/en/6JPK)
 * epic.mp3 - "Epic Trailer Background Music" by Migfus20 (https://freesound.org/people/Migfus20/sounds/560454/)
 * old-books.jpg - "Old books, Book, Old image. Free for use." by jarmoluk (https://pixabay.com/photos/old-books-book-old-library-436498/)
 * spider.gif - "Insect, Spider, Spider web GIF. Free for use." by Tilixia-Summer (https://pixabay.com/gifs/insect-spider-spider-web-fly-858/)
 * sword-icon.png - "mini sword pixel art" by mabotoufu (https://en.ac-illust.com/clip-art/24789724/mini-sword-pixel-art)
 * turn-page.mp3 - "page turn" by partheeban (https://freesound.org/people/partheeban/sounds/457767/)
 * wayne-chung.jpg - "Image of Wayne Chung wondering what's going on" by repp
 * 
 * @author repp
 */
public class Questcraft extends app.ApplicationView {
    
    public final static String BOOK = "Book";
    public final static String CHEATS = "Cheats";
    public final static String DESIGNER = "Designer";
    public final static String HIGH_SCORES = "High Scores";
    public final static String LIBRARY = "Library";
    
    public ApplicationController appController;
    public Book book;
    public ApplicationView cheats;
    public Designer designer;
    public HighScores highScores;
    public Library library;
    
    public Questcraft(String name) {
        super(name);
        this.addTextArea = false;
        this.backgroundImage = "/assets/images/book.png";
        this.book = null;
        this.cheats = new Cheats(CHEATS);
        this.designer = null;
        this.highScores = new HighScores(HIGH_SCORES);
        this.library = new Library(LIBRARY);
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("Questcraft: onEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        switch(eventName) {
            case "book" -> {
                this.book = new Book(BOOK);
                this.book.bookFile = (BookFile) eventValue;
                this.appController.addView(this.book);
                this.highScores.setHighScores(this.book.bookFile.highScores);
                this.appController.addView(this.highScores);
                // TODO - Load cheats specific to the bookFile
                this.appController.addView(this.cheats);
                this.designer = new Designer(DESIGNER);
                this.appController.addView(this.designer);
                this.appController.displayView(this.book);
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
