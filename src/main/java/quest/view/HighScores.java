package quest.view;

import app.controller.BaseController;
import app.Color;
import app.Font;
import app.FontStyle;
import app.HorizontalAlignment;
import app.Layout;
import app.RelativeCoordinates;
import app.TextDecoration;
import app.VerticalAlignment;
import static app.controller.BaseController.logger;
import app.node.Label;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import quest.model.HighScore;

/**
 *
 * @author repp
 */
public class HighScores extends app.view.BaseView {

    BaseController appController;
    public List<HighScore> highScores;
    public Quest quest;
    Label scoreLabel;
    
    public HighScores(String name) {
        super(name);
        this.backgroundColor = new Color(255, 255, 255, 1.0);
        this.emojis.add("\uD83C\uDFC6"); // "trophy" Unicode emoji
        TextDecoration decoration = new TextDecoration();
        decoration.pixelSize = 16.0;
        decoration.font = Font.ROBOTO_MONO;
        decoration.style = FontStyle.BOLD;
        this.scoreLabel = new Label("high scores label", null, decoration);
    }
    
    @Override
    public void onLoad(BaseController appController) {
        logger.log(Level.INFO, "Entered: appController={0}", appController);        
        this.appController = appController;
        this.refreshScoreLabel();
    }
        
    public void refreshScoreLabel() {
        logger.log(Level.INFO, "Entered");
        
        if (this.appController == null) {
            logger.log(Level.INFO, "View hasn't been loaded yet.  Exiting.");
            return;
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        
        String text = "";
        for (HighScore highScore : this.highScores) {
            if (!text.isEmpty()) {
                text += "\n";
            }
            text += highScore.score + "   " + highScore.player + "   " + highScore.date.format(formatter);
        }
        text += "\n\nYour score: " + Quest.quest.getPlayerXP();
        
        this.scoreLabel.texts.get(0).text = text;
        
        if (this.scoreLabel.getBounds() == null) {
            this.appController.addNode(this.name, this.name, scoreLabel, new Layout(new RelativeCoordinates(0.05, 0.05), HorizontalAlignment.LEFT, VerticalAlignment.TOP));
        } else {
            this.appController.changeNode(this.name, scoreLabel, new Layout(new RelativeCoordinates(0.05, 0.05), HorizontalAlignment.LEFT, VerticalAlignment.TOP));
        }
    }

    public void setHighScores(List<HighScore> highScores) {
        logger.log(Level.INFO, "Entered: highScores={0}", highScores);
        this.highScores = highScores;
        this.refreshScoreLabel();
    }
    
}
