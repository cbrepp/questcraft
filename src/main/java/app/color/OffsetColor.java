package app.color;

import java.util.Map;
import java.util.TreeMap; // Sorted keys allowing the finding of the closest value in O(log n) time

/**
 *
 * @author repp
 */
public class OffsetColor extends Color {
    
    public final TreeMap<Double, RGBColor> luminosityMap;

    public OffsetColor() {
        super(null, null, null);
        this.luminosityMap = initMap(null);
    }
    
    public OffsetColor(Double opacity) {
        super(null, null, null, opacity);
        this.luminosityMap = initMap(null);
    }
    
    public OffsetColor(Color lightColor, Color darkColor) {
        super(null, null, null);
        this.luminosityMap = initMap(lightColor, darkColor);
    }
    
    public OffsetColor(TreeMap<Double, RGBColor> luminosityMap) {
        super(null, null, null);
        this.luminosityMap = initMap(luminosityMap);
    }

    public OffsetColor(Color lightColor, Color darkColor, Double opacity) {
        super(null, null, null, opacity);
        this.luminosityMap = initMap(lightColor, darkColor);
    }
        
    public OffsetColor(TreeMap<Double, RGBColor> luminosityMap, Double opacity) {
        super(null, null, null, opacity);
        this.luminosityMap = initMap(luminosityMap);
    }
    
    private TreeMap<Double, RGBColor> initMap(Color lightColor, Color darkColor) {
        TreeMap<Double, RGBColor> luminosityMap = new TreeMap();
        
        if (lightColor == null) {
            lightColor = WHITE;
        }
        
        if (darkColor == null) {
            darkColor = BLACK;
        }
        
        luminosityMap.put(MIN_LUMINANCE, lightColor);
        luminosityMap.put(MAX_LUMINANCE, darkColor);
        
        return luminosityMap;
    }
    
    private TreeMap<Double, RGBColor> initMap(TreeMap<Double, RGBColor> luminosityMap) {
        if (luminosityMap == null) {
            luminosityMap = new TreeMap();
        }
        if (!luminosityMap.containsKey(MIN_LUMINANCE)) {
            luminosityMap.put(MIN_LUMINANCE, WHITE);
        }
        if (!luminosityMap.containsKey(MAX_LUMINANCE)) {
            luminosityMap.put(MAX_LUMINANCE, BLACK);
        }
        return luminosityMap;
    }
    
    @Override
    public Integer getRed() {
        return null;
    }
        
    @Override
    public Integer getGreen() {
        return null;
    }
    
    @Override
    public Integer getBlue() {
        return null;
    }
    
    @Override
    public Boolean isClosed() {
        return false;
    }
    
    @Override
    public String toString() {
        if (this.luminosityMap == null) {
            return "";
        }
        
        String string = "";
        
        for (Map.Entry<Double, RGBColor> entry : this.luminosityMap.entrySet()) {
            Double key = entry.getKey();
            RGBColor value = entry.getValue();
            string += "[(" + key + "), " + value.toString() + ", (" + this.getOpacity() + ")]";
        }
        return string;
    }
    
}
