package quest.model;

import java.io.Serializable;

/**
 *
 * @author repp
 */
public class Page extends BookPart implements Serializable {
    
    public Boolean hideNextButton;
    public Boolean hidePreviousButton;
    public String nextPageName;
    public Boolean noGlow;
    public String previousPageName;
    public Story story;
    
    public Page() {
        this.hideNextButton = false;
        this.hidePreviousButton = false;
        noGlow = false;
        this.story = new Story();
    }
    
}
