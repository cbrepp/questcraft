package app.node;

import app.controller.BaseController;
import app.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author repp
 */
public class Sprite extends BaseNode {
    
    public List<Sprite> collisionSprites;
    public Color glowColor;
    public String imageFile;
    public Double imageScale;
    public String name;
    public List<String> potentialCollisionNames;
    public Double viewPortBuffer; // Percent of animation background that defines the edge beyond which the image will not be displayed
    public Integer x;
    public Integer y;
    
    public Sprite (String name) {
        super(name);
    }
    
    public Sprite(String name, Layout layout) {
        super(name, layout);
    }
    
    /*
    public Sprite(BaseController controller, String name, String imageFile, Double imageScale, Integer x, Integer y, List<String> potentialCollisionNames, Double viewPortBuffer, Color glowColor) {
        this.collisionSprites = new ArrayList();
        this.imageFile = imageFile;
        this.imageScale = imageScale;
        this.name = name;
        this.x = x;
        this.y = y;
        this.potentialCollisionNames = potentialCollisionNames;
        this.viewPortBuffer = viewPortBuffer;
        this.glowColor = glowColor;
    }
    */
    
    public void onCollision(Sprite collidingSprite) {}
}
