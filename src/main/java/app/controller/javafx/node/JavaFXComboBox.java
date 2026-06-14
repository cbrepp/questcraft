package app.controller.javafx.node;

import app.Text;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import app.node.BaseDecoratedNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.text.Font;

/**
 *
 * @author repp
 */
public class JavaFXComboBox extends BaseJavaFXNode {
    
    public JavaFXComboBox(app.node.ComboBox node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new ComboBox<String>(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        app.node.ComboBox node = (app.node.ComboBox) this.node;
        ComboBox<String> controllerNode = (ComboBox<String>) this.controllerNode;
        
        List<String> values = new ArrayList();
        Map<String, Text> textMap = new HashMap();
        if (node.values != null) {
            for (Text text : node.values) {
                String textString = text.text.toString();
                values.add(textString);
                textMap.put(textString, text);
            }
        }
        
        List<String> disabledValues = new ArrayList();
        if (node.disabledValues != null) {
            for (Object disabledObject : node.disabledValues) {
                String disabledString = disabledObject.toString();
                disabledValues.add(disabledString);
            }
        }
        
        ObservableList<String> observableList = FXCollections.observableArrayList(values);
        controllerNode.setItems(observableList);
        
        if (node.defaultValue != null) {
            String defaultValue = node.defaultValue.toString();
            if (values.contains(defaultValue)) {
                controllerNode.setValue(defaultValue);
            } else {
                logger.log(Level.WARNING, "Default value is not a possible value: {0}", defaultValue);
            }            
        }
        
        controllerNode.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if ((empty) || (item == null)) {
                    setText(null);
                    setFont(Font.getDefault());
                    setDisable(true);
                } else if (disabledValues.contains(item)) {
                    setText(item);
                    setStyle("-fx-opacity: 0.4;");
                    setDisable(true);
                } else {
                    setText(item);
                    if (textMap.containsKey(item)) {
                        Text text = textMap.get(item);
                        if (text.decoration != null) {
                            // TODO - Other decoration attributes are not supported
                            if (text.decoration.font != null) {
                                setFont(Font.font(text.decoration.font));
                            }
                        }
                    }
                    setDisable(false);
                }
            }
        });
        
        controllerNode.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if ((empty) || (item == null)) {
                    setText(null);
                    setFont(Font.getDefault());
                } else {
                    setText(item);
                    if (textMap.containsKey(item)) {
                        Text text = textMap.get(item);
                        if (text.decoration != null) {
                            // TODO - Other decoration attributes are not supported
                            if (text.decoration.font != null) {
                                setFont(Font.font(text.decoration.font));
                            }
                        }
                    }
                }
            }
        });
    }
    
}
