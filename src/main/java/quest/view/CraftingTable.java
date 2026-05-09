package quest.view;

import app.controller.BaseController;
import app.view.BaseView;
import app.color.Color;
import app.Font;
import app.FontStyle;
import app.HorizontalAlignment;
import app.Layout;
import app.RelativeCoordinates;
import app.TextDecoration;
import app.VerticalAlignment;
import static app.controller.BaseController.logger;
import app.node.Label;
import java.util.logging.Level;

/**
 *
 * @author repp
 */
public class CraftingTable extends BaseView {

    public CraftingTable(String name) {
        super(name);
        this.backgroundColor = new Color(255, 255, 255, 1.0);
        this.backgroundImage = "/assets/images/designer.jpg";
        this.emojis.add("\uD83E\uDE9A"); // "carpentry saw" Unicode emoji
    }
    
    @Override
    public void onLoad(BaseController appController) {
        logger.log(Level.INFO, "Entered: appController={0}", appController);
        
        TextDecoration decoration = new TextDecoration();
        decoration.pixelSize = 86.0;
        decoration.color = Color.SHADOW;
        decoration.font = Font.ROBOTO_BLACK;
        decoration.style = FontStyle.BOLD;
        Label comingSoonLabel = new Label("coming soon", "Coming soon", decoration);
        comingSoonLabel.backgroundColor = Color.WHITE;
        appController.addNode(this.name, this.name, comingSoonLabel, new Layout(new RelativeCoordinates(0.0, 0.0), HorizontalAlignment.CENTER, VerticalAlignment.CENTER));
    }
    
}
