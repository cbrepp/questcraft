package app.controller.javafx.node;

import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.controller.JavaFXApplication;
import app.node.BaseDecoratedNode;
import java.util.List;
import java.util.logging.Level;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author repp
 */
public class JavaFXDialog extends BaseJavaFXNode {
    
    public JavaFXDialog(app.node.Dialog node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new Dialog(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.Dialog node = (app.node.Dialog) this.node;
        Dialog<List<String>> controllerNode = (Dialog) this.controllerNode;
        
        Stage parentStage = (Stage) this.parent.controllerNode;
        controllerNode.initOwner(parentStage);
        controllerNode.setTitle(node.title);
        controllerNode.setHeaderText(node.headerText);
        controllerNode.getDialogPane().setPrefWidth(parentStage.getWidth() * 0.75);
        controllerNode.getDialogPane().setPrefHeight(parentStage.getHeight() * 0.75);
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        controllerNode.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        controllerNode.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                logger.log(Level.INFO, "Processing save button");
                
                GridPane gp = (GridPane) controllerNode.getDialogPane().getContent();
                List<String> result = JavaFXGrid.getValues(gp);
                return result;
            }
            return null;
        });
        
        String eventName;
        if (node.eventName == null) {
            eventName = node.name;
        } else {
            eventName = node.eventName.toString();
        }
                
        controllerNode.setOnHidden(event -> {
            List<String> result = controllerNode.getResult();
            if (result != null) {
                if (node.eventListener != null) {
                    logger.log(Level.INFO, "Dialog saved: name={0}", node.name);
                    node.eventListener.onEvent(eventName, result);
                }
            } else {
                logger.log(Level.INFO, "No result");
            }
            this.controller.removeNode(this.viewName, this.node.name);
        });
        
        controllerNode.show();
    }
    
}
