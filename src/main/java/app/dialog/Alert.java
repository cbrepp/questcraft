package app.dialog;

import app.Icon;

/**
 *
 * @author repp
 */
public class Alert extends BaseDialog {
    
    public String emojis;
    public String header;
    public Icon icon;
    public String text;

    public Alert () {
        super();
    }
    
    public Alert (String title) {
        super(title);
    }
    
    public Alert (String title, String text) {
        super(title);
        this.text = text;
    }
    
}
