
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.Condition;
import quest.control_deprecated.BaseQuestControl;
import quest.node.ValidatedVariablePrompt;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class Prompt extends BaseQuestControl {
    
    public ValidatedVariablePrompt prompt;
    
    public Prompt() {
        super();
        this.init();
    }
    
    public Prompt(ValidatedVariablePrompt prompt) {
        this.prompt = prompt;
    }
    
    public Prompt(ValidatedVariablePrompt prompt, Condition condition) {
        this(prompt);
        this.condition = condition;
    }

    public void init() {
        this.prompt = null;
    }
    
    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        
        Quest.quest.displayStoryPrompt(this.prompt);
    }
    
}
