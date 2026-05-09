
package quest.control;

import static app.controller.BaseController.logger;
import app.node.BaseNode;
import app.node.Label;
import app.node.Separator;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import quest.Condition;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class Scribe extends BaseQuestControl {
    
    public List<BaseNode> nodes;
    
    public Scribe() {
        super();
        this.init();
    }
    
    public Scribe(List<BaseNode> nodes) {
        this.nodes = nodes;
    }
    
    public Scribe(List<BaseNode> nodes, Condition condition) {
        this(nodes);
        this.condition = condition;
    }

    public void init() {
        this.nodes = new ArrayList();
    }
    
    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        
        if (Quest.quest.storySectionCount > 0) {
            logger.log(Level.INFO, "Additional section, adding separator");
            
            Separator separator = new Separator(Quest.quest.newNodeIndex(), Separator.Orientation.HORIZONTAL);
            Quest.quest.displayStoryNode(separator);

            Label newLines = new Label(Quest.quest.newNodeIndex(), System.lineSeparator());
            Quest.quest.displayStoryNode(newLines);
        }
        
        for (BaseNode node : this.nodes) {
            logger.log(Level.INFO, "Requesting display of node: {0}", node);
            if (node.name == null) {
                node.name = Quest.quest.newNodeIndex();
            }
            Quest.quest.displayStoryNode(node);
        }
        
        Label newLine = new Label(Quest.quest.newNodeIndex(), System.lineSeparator());
        Quest.quest.displayStoryNode(newLine);
        
        Quest.quest.storySectionCount++;        
    }
    
}
