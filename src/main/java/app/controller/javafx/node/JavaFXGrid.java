package app.controller.javafx.node;

import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.color.RGBColor;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import static app.controller.JavaFXApplication.getFxColor;
import app.node.BaseDecoratedNode;
import app.node.Grid;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

/**
 *
 * @author repp
 */
public class JavaFXGrid extends BaseJavaFXNode {
    
    public JavaFXGrid(Grid node, BaseDecoratedNode parent, String viewName, BaseController controller) {
        super(node, new GridPane(), parent, viewName, controller);
    }
    
    @Override
    public void configure() {
        logger.log(Level.INFO, "Entered");
        
        Grid node = (Grid) this.node;
        GridPane controllerNode = (GridPane) this.controllerNode;
        
        RGBColor offsetColor = new DecoratedOffsetColor(new OffsetColor(), this.parent);
        
        // Remove all of the children in case this is an update and not an add
        controllerNode.getChildren().clear();
        
        // TODO - It's an assumption that the parent is a region
        Region parentControllerNode = (Region) this.parent.controllerNode;
        controllerNode.setPrefSize(parentControllerNode.getMinWidth(), parentControllerNode.getMinHeight());
        controllerNode.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        int cellCount = node.cells.size();

        // Configure the background (transparent or a fill color)
        if (node.backgroundColor == null) {
            controllerNode.setBackground(Background.EMPTY); // Transparent
        } else {
            if (node.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                node.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
            }
            BackgroundFill backgroundFill = new BackgroundFill(getFxColor(node.backgroundColor), CornerRadii.EMPTY, Insets.EMPTY);
            Background background = new Background(backgroundFill);
            controllerNode.setBackground(background);
        }

        // Configure borders
        BorderStroke stroke = null;
        if (node.showBorders) {
            CornerRadii cornerRadii;
            if (node.cornerRadii == 0) {
                cornerRadii = CornerRadii.EMPTY;
            } else {
                cornerRadii = new CornerRadii(node.cornerRadii);
            }
            controllerNode.setBorder(new Border(new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, cornerRadii, new BorderWidths(node.borderWidth))));
            
            stroke = new BorderStroke(getFxColor(offsetColor), BorderStrokeStyle.SOLID, cornerRadii, new BorderWidths(1));
        }
        
        // Configure outer cell padding
        if (node.borderPadding > 0) {
            controllerNode.setHgap(node.borderPadding);
            controllerNode.setVgap(node.borderPadding);
        }
        
        // Configure inner cell padding
        Insets cellPadding = null;
        if (node.padding > 0) {
            cellPadding = new Insets(node.padding);
        }
        
        // Configure dimensions
        int columns = node.columns;
        if (columns == 0) {
            double squareRoot = Math.sqrt(cellCount);
            columns = (int) Math.ceil(squareRoot);
        }
        int rows = 0;
        if (columns != 0) {
            double rowsDiv = ((double) cellCount / (double) columns);  // Make sure values are double so remainder causes rows count to round up
            rows = (int) Math.ceil(rowsDiv);
        }
        
        // Configure rows to expand as much as they can
        int rowHeight = (int) Math.floor(100 / rows);
        for (int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(rowHeight);
            row.setVgrow(Priority.ALWAYS);
            controllerNode.getRowConstraints().add(row);
        }
        
        // Allow columns to expand as much as they can
        int columnWidth = (int) Math.floor(100 / columns);
        for (int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(columnWidth);
            column.setHgrow(Priority.ALWAYS);
            controllerNode.getColumnConstraints().add(column);
        }

        logger.log(Level.INFO, "cells={0}, columns={1}, rows={2}", new Object[]{cellCount, columns, rows});
        
        // TODO - If the individual cells need to be worked with, publishNode() can call publishNode() for each at the end.
        // Would be useful to make a parent type node that publishNode can use to invoke a base method for adding children once the parent is complete.
        // For now, assume the cells need to be updated by updating the entire grid.
        
        int currentRow = 1;
        int currentColumn = 0;        
        for (app.node.Group cellGroup : node.cells) {
            logger.log(Level.INFO, "Adding cell {0}", cellGroup.name);
            
            StackPane cell = new StackPane();
            GridPane.setHgrow(cell, Priority.ALWAYS);
            GridPane.setVgrow(cell, Priority.ALWAYS);
            cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Set node to expand to fill the cell (optional)
            
            if (cellGroup.backgroundColor == null) {
                cell.setBackground(Background.EMPTY); // Transparent
            } else {
                if (cellGroup.backgroundColor instanceof OffsetColor primitiveOffsetColor) {
                    cellGroup.backgroundColor = new DecoratedOffsetColor(primitiveOffsetColor, this.parent);
                }
                BackgroundFill cellFill = new BackgroundFill(getFxColor(cellGroup.backgroundColor), CornerRadii.EMPTY, Insets.EMPTY);
                Background background = new Background(cellFill);
                cell.setBackground(background);
            }
            
            if (node.showBorders) {
                cell.setBorder(new Border(stroke));
            }
            
            if (node.padding > 0) {
                cell.setPadding(cellPadding);
            }

            currentColumn++;
            if (currentColumn > columns) {
                currentRow++;
                currentColumn = 1;
            }
            
            // TODO - Need:
            //              app.node.Grid
            //              app.node.StackPane
            //              This method needs to use publishNode() to establish the grid, then, for each grid.cells cell let publishNode iterate the list like the other collections, passing the grid as the parent
            //              
            //this.registerNode(viewName, decoratedNode, parentName, layout);
            //this.namedFXNodes.get(viewName).put(cellGroup.name + " cell", cell); // TODO - Make a StackPane constructor for this
            //this.addNode(viewName, cellGroup.name + " cell", cellGroup, null);

            //Pane box = newGroup(viewName, cellGroup, genericOffsetColor);
            //cell.getChildren().add(box);
            
            controllerNode.add(cell, currentColumn - 1, currentRow - 1);
        }
        
        this.scaleNode(controllerNode);
    }
    
}
