package app.node;

import app.EventListener;
import app.color.RGBColor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author repp
 */
public class Dialog extends BaseNode implements BaseCompositeNode {
    
    public List<BaseNode> children = new ArrayList();
    public EventListener eventListener;
    public Object eventName; // Default (null) is the button's name
    public String headerText;
    public String title;
    
    public Dialog (String name) {
        super(name);
    }
    
    public Dialog (String name, String title, String headerText, List<BaseNode> children) {
        super(name);
        this.title = title;
        this.headerText = headerText;
        this.children = children;
    }
    
    @Override
    public List<? extends BaseNode> getChildren() {
        List<Group> labeledChildren = new ArrayList();
        for (BaseNode node : this.children) {
            Group childrenGroup = new VerticalGroup(node.name + " group");
            childrenGroup.borderWidth = 0;
            Label label = new Label(node.name + " label", node.name + ":");
            childrenGroup.nodes.add(label);
            childrenGroup.nodes.add(node);
            labeledChildren.add(childrenGroup);
        }
        
        Grid dialogGrid = new Grid(this.name + " grid");
        dialogGrid.showBorders = false;
        dialogGrid.cells = labeledChildren;
        List<BaseNode> children = new ArrayList();
        children.add(dialogGrid);
        
        return children;
    }

    @Override
    public RGBColor getColor() {
        return null;
    }
    
    @Override
    public boolean isParent() {
        return true;
    }
        
}
