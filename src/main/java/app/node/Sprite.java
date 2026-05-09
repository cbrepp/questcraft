package app.node;

import app.color.RGBColor;
import java.util.List;

/**
 *
 * @author repp
 */
public class Sprite extends BaseNode {
    
    public List<Sprite> collisionSprites;
    public RGBColor glowColor;
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
    
    @Override
    public RGBColor getColor() {
        return null;
    }
    
    public void onCollision(Sprite collidingSprite) {}
    
}
