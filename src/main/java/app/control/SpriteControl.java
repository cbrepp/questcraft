package app.control;

import app.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author repp
 */
public class SpriteControl extends BaseControl {
    
    public List<SpriteControl> collisionSprites;
    public Color glowColor;
    public String imageFile;
    public Double imageScale;
    public String name;
    public List<String> potentialCollisionNames;
    public Double viewPortBuffer; // Percent of animation background that defines the edge beyond which the image will not be displayed
    public Integer x;
    public Integer y;
    
    public SpriteControl() {}

    public SpriteControl(String name, Layout layout) {
        this.name = name;
        this.layout = layout;
    }
    
    public SpriteControl(ApplicationController controller, String name, String imageFile, Double imageScale, Integer x, Integer y, List<String> potentialCollisionNames, Double viewPortBuffer, Color glowColor) {
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
    
    public void onCollision(SpriteControl collidingSprite) {}
}
