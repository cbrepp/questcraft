package app.model;

import app.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author repp
 */
public class SpriteModel extends BaseModel {
    
    public List<SpriteModel> collisionSprites;
    public String imageFile;
    public Double imageScale;
    public String name;
    public List<String> potentialCollisionNames;
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
    
    public SpriteModel(ApplicationController controller, String name, String imageFile, Double imageScale, Integer x, Integer y, List<String> potentialCollisionNames) {
        this.collisionSprites = new ArrayList();
        this.imageFile = imageFile;
        this.imageScale = imageScale;
        this.name = name;
        this.x = x;
        this.y = y;
        this.potentialCollisionNames = potentialCollisionNames;
    }
    
}
