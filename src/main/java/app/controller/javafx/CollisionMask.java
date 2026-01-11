package app.controller.javafx;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import java.util.BitSet;

/**
 *
 * @author repp
 */
public class CollisionMask {

    public final BitSet mask;
    public final int maskWidth;
    public final int maskHeight;
    public static final int DOWNSAMPLE_FACTOR = 10;

    public CollisionMask(Image image) {
        this.maskWidth = (int) (image.getWidth() / DOWNSAMPLE_FACTOR);
        this.maskHeight = (int) (image.getHeight() / DOWNSAMPLE_FACTOR);
        this.mask = new BitSet(this.maskWidth * this.maskHeight);
        generateMask(image);
    }

    private void generateMask(Image image) {
        PixelReader pr = image.getPixelReader();
        for (int y = 0; y < this.maskHeight; y++) {
            for (int x = 0; x < this.maskWidth; x++) {
                // Sample the center of the block of pixels
                int sampleX = x * DOWNSAMPLE_FACTOR + DOWNSAMPLE_FACTOR / 2;
                int sampleY = y * DOWNSAMPLE_FACTOR + DOWNSAMPLE_FACTOR / 2;

                // Check alpha value of the sampled pixel
                int pixel = pr.getArgb(sampleX, sampleY);
                int alpha = (pixel >> 24) & 0xFF;

                // If alpha is above a threshold, mark the bit as true (opaque)
                if (alpha > 128) {
                    this.mask.set(y * this.maskWidth + x);
                }
            }
        }
    }
    
    public boolean intersects(CollisionMask other, double thisX, double thisY, double otherX, double otherY) {
        // 1. Calculate the offset between the two masks based on screen position
        int xOffset = (int) ((otherX - thisX) / DOWNSAMPLE_FACTOR);
        int yOffset = (int) ((otherY - thisY) / DOWNSAMPLE_FACTOR);

        // 2. Iterate over potential overlap area
        for (int y = 0; y < this.maskHeight; y++) {
            for (int x = 0; x < this.maskWidth; x++) {
                if (this.isOpaque(x, y)) {
                    // Calculate the corresponding bit in the other mask's coordinate system
                    int otherXPos = x + xOffset;
                    int otherYPos = y + yOffset;

                    // 3. Check if the corresponding bit in the other mask is also opaque
                    if (other.isOpaque(otherXPos, otherYPos)) {
                        // Collision detected!
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isOpaque(int x, int y) {
        if ((x >= 0) && (x < this.maskWidth) && (y >= 0) && (y < this.maskHeight)) {
            Boolean isOpaque = this.mask.get(y * this.maskWidth + x);
            return isOpaque;
        }
        return false;
    }

}
