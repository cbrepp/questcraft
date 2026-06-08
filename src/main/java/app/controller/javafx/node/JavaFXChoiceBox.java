package app.controller.javafx.node;

import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.node.BaseDecoratedNode;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

/**
 *
 * @author repp
 */
public class JavaFXChoiceBox extends BaseJavaFXNode {
    
    public JavaFXChoiceBox(app.node.ChoiceBox node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new ChoiceBox<String>(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.ChoiceBox node = (app.node.ChoiceBox) this.node;
        ChoiceBox<String> controllerNode = (ChoiceBox<String>) this.controllerNode;
        
        ObservableList<String> observableList = FXCollections.observableArrayList(node.values);
        controllerNode.setItems(observableList);
        
        if (node.defaultValue != null) {
            String defaultValue = node.defaultValue.toString();
            if (node.values.contains(defaultValue)) {
                controllerNode.setValue(defaultValue);
            } else {
                logger.log(Level.WARNING, "Default value is not a possible value: {0}", defaultValue);
            }            
        }
    }
    
}
