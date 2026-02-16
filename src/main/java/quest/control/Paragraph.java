
package quest.control;

import static app.controller.BaseController.logger;
import app.node.BaseNode;
import app.node.Label;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public class Paragraph extends BaseQuestControl {
    
    public List<BaseNode> nodes;
    
    public Paragraph() {
        super();
        this.init();
    }
    
    public Paragraph(List nodes) {
        this.nodes = nodes;
    }

    public void init() {
        this.nodes = new ArrayList();
    }
    
    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        
        if ((this.nodes == null) || (this.nodes.isEmpty())) {
            logger.log(Level.WARNING, "No nodes.  Only a double new line will be added.");
        } else {
            logger.log(Level.WARNING, "Handling {0} nodes and a double new line", this.nodes.size());
        }
        
        for (BaseNode node : this.nodes) {
            logger.log(Level.INFO, "Requesting display of node: {0}", node);
            if (node.name == null) {
                node.name = Quest.quest.newNodeIndex();
            }
            Quest.quest.displayStoryNode(node);
        }
        
        Label newLine = new Label(Quest.quest.newNodeIndex(), "\n\n");
        Quest.quest.displayStoryNode(newLine);
    }
    
}
