package quest;

import app.ApplicationController;
import app.ApplicationView;
import java.util.LinkedHashMap;

/**
 * book.png - "Empty book" by Darkmoon_Art (https://pixabay.com/illustrations/reserve-pages-empty-book-open-book-3057904/)
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
    
    public final static String LIBRARY = "Library";
    public final static String BOOK = "Book";
    public final static String HIGH_SCORES = "High Scores";
    public final static String CHEATS = "Cheats";
    public final static String DESIGNER = "Designer";
    
    public Library library;
    public Book book;
    public ApplicationView highScores;
    public ApplicationView cheats;
    public Designer designer;
    public LinkedHashMap<String, ApplicationView> map;
    
    public Questcraft() {
        this.backgroundImage = "/assets/images/book.png";
        this.library = new Library();
        this.book = null;
        this.cheats = null;
        this.designer = new Designer();
        this.map = new LinkedHashMap<>();
        map.put(LIBRARY, this.library);
        map.put(BOOK, this.book);
        map.put(HIGH_SCORES, this.highScores);
        map.put(CHEATS, this.cheats);
        map.put(DESIGNER, this.designer);
    }
    
    @Override
    public LinkedHashMap<String, ApplicationView> getChildren() {
        return this.map;
    }

    @Override
    public void onDisplay(ApplicationController appController, ApplicationView parentView) {}
    
    @Override
    public void handleEvent(String eventName, String eventValue) {
        throw new UnsupportedOperationException("Not supported.");
    }

}
