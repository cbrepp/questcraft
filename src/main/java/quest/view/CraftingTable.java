package quest.view;

import app.ApplicationController;
import app.ApplicationView;
import app.Color;
import app.FontStyle;

/**
 *
 * @author repp
 */
public class CraftingTable extends ApplicationView {

    public CraftingTable(String name) {
        super(name);
        this.backgroundColor = new Color(255, 255, 255);
        this.backgroundImage = "/assets/images/designer.jpg";
        //this.addTextArea = false;
        this.emojis.add("\uD83E\uDE9A"); // "carpentry saw" Unicode emoji
    }
    
    @Override
    public void onLoad(ApplicationController appController) {
        System.out.println("CraftingTable: display");
        
        // displayFloatingText(String viewName, String text, Integer startRow, Integer startColumn, Integer endRow, Integer endColumn, app.Color fontColor, Integer fontSize, Integer fontStyle, String fontName)
        appController.displayFloatingText(this.name, null, "Coming soon!", 4, 6, null, null, null, 16, FontStyle.BOLD, null);
        appController.displayOverlay(this.name, "crafting-table", new Color(255, 255, 255), 3, 5, 7, 30, 200, false);
    }
    
}
