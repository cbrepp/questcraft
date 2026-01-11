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
    
}
