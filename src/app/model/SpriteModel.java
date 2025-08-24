package app.model;

import app.*;

/**
 *
 * @author repp
 */
public class SpriteModel extends BaseModel {
    
    public String imageFile;
    public Double imageScale = 1.0;
    public Integer x;
    public Integer y;
    
    public SpriteModel() {}

    public SpriteModel(String text, Color backgroundColor) {
        super(text, backgroundColor);
    }
    
    public SpriteModel(String text, Color backgroundColor, Boolean isEnabled) {
        this(text, backgroundColor);
        this.isEnabled = isEnabled;
    }
    
    public SpriteModel(ApplicationController controller, String imageFile, Double imageScale, Integer x, Integer y) {
        this.imageFile = imageFile;
        this.imageScale = imageScale;
        this.x = x;
        this.y = y;
    }
    
}
