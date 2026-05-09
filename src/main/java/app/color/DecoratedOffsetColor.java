package app.color;

import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.node.BaseDecoratedNode;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author repp
 */
public class DecoratedOffsetColor extends Color {
    
    public final OffsetColor offsetColor;
    public final BaseDecoratedNode offsetNode;

    public DecoratedOffsetColor(OffsetColor offsetColor, BaseDecoratedNode offsetNode) {
        super(null, null, null);
        this.offsetColor = offsetColor;
        this.offsetNode = offsetNode;
    }
        
    @Override
    public Integer getRed() {
        if (!this.exists()) {
            logger.log(Level.INFO, "Color not fully initialized: offsetColor={0}, offsetNode={1}", new Object[]{this.offsetColor, this.offsetNode});
            return null;
        }
        
        RGBColor luminosityColor = this.getLuminosityColor();
        Integer red = luminosityColor.getRed();
        
        return red;
    }
        
    @Override
    public Integer getGreen() {
        if (!this.exists()) {
            logger.log(Level.INFO, "Color not fully initialized: offsetColor={0}, offsetNode={1}", new Object[]{this.offsetColor, this.offsetNode});
            return null;
        }
        
        RGBColor luminosityColor = this.getLuminosityColor();
        Integer green = luminosityColor.getGreen();
        
        return green;
    }
    
    @Override
    public Integer getBlue() {
        if (!this.exists()) {
            logger.log(Level.INFO, "Color not fully initialized: offsetColor={0}, offsetNode={1}", new Object[]{this.offsetColor, this.offsetNode});
            return null;
        }
        
        RGBColor luminosityColor = this.getLuminosityColor();
        Integer blue = luminosityColor.getBlue();
        
        return blue;
    }
    
    public Boolean exists() {
        Boolean exists = !((this.offsetColor == null) || (this.offsetColor.luminosityMap == null) || (this.offsetColor.luminosityMap.isEmpty()) || (this.getOffsetNodeColor() == null));
        return exists;
    }
    
    public RGBColor getLuminosityColor() {
        if (!this.exists()) {
            return null;
        }
        Double luminance = this.getOffsetNodeColor().getLuminance();
        RGBColor luminosityColor = this.getLuminosityColor(luminance);
        return luminosityColor;
    }
    
    public RGBColor getLuminosityColor(Double luminosity) {
        Double low = this.offsetColor.luminosityMap.floorKey(luminosity);
        Double high = this.offsetColor.luminosityMap.ceilingKey(luminosity);
        
        Double mapLuminosity;
        if (low == null) {
            mapLuminosity = high;
        } else if (high == null) {
            mapLuminosity = low;
        } else {
            mapLuminosity = (luminosity - low <= high - luminosity) ? low : high;
        }
        
        RGBColor luminosityColor = this.offsetColor.luminosityMap.get(mapLuminosity);
        
        return luminosityColor;
    }
    
    public RGBColor getOffsetNodeColor() {
        if (this.offsetNode == null) {
            return null;
        }
        
        BaseDecoratedNode offsetNode = this.offsetNode;
        RGBColor offsetNodeColor = offsetNode.node.getColor();
        while (offsetNodeColor == null) {
            offsetNode = offsetNode.parent;
            if (offsetNode == null) {
                offsetNodeColor = BaseController.DEFAULT_COLOR;
                break;
            } else {
                offsetNodeColor = offsetNode.node.getColor();
            }
        }
        
        return offsetNodeColor;
    }
    
    @Override
    public Boolean isClosed() {
        return false;
    }
    
    @Override
    public String toString() {
        if (!this.exists()) {
            return "";
        }
        
        String string = "";
        
        for (Map.Entry<Double, RGBColor> entry : this.offsetColor.luminosityMap.entrySet()) {
            Double key = entry.getKey();
            RGBColor value = entry.getValue();
            string += "[(" + key + "), " + value.toString() + ", (" + this.getOpacity() + ")]";
        }
        return string;
    }
    
}
