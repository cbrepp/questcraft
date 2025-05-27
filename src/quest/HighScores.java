package quest;

import app.ApplicationController;
import app.Color;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author repp
 */
public class HighScores extends app.ApplicationView {

    List<HighScore> highScores;
    
    public HighScores(String name) {
        super(name);
        this.backgroundColor = new Color(255, 255, 255);
    }
    
    @Override
    public void onLoad(ApplicationController appController) {
        System.out.println("HighScores: onLoad");
        
        int row = 3;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        for (HighScore highScore : this.highScores) {
            appController.displayText(this.name, highScore.score + "   " + highScore.player + "   " + highScore.date.format(formatter), row++, 5);
        }
    }
        
    public void setHighScores(List<HighScore> highScores) {
        System.out.println("HighScores: setHighScores");
        this.highScores = highScores;   
    }

}
