package app.model;

import app.*;

/**
 *
 * @author repp
 */
public class BaseModel {
    
    public Color backgroundColor;
    public Boolean isEnabled = true;
    public Double pixelSize = 14.0;
    public String text;
    
    public BaseModel() {}
    
    public BaseModel(String text, Color backgroundColor) {
        this.text = text;
        this.backgroundColor = backgroundColor;
    }
    
}
