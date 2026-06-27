package app.controller.javafx.node;

import app.Coordinates;
import app.HorizontalAlignment;
import static app.HorizontalAlignment.CENTER;
import static app.HorizontalAlignment.LEFT;
import static app.HorizontalAlignment.RIGHT;
import app.Layout;
import app.VerticalAlignment;
import app.color.DecoratedOffsetColor;
import app.color.OffsetColor;
import app.color.RGBColor;
import app.controller.BaseController;
import static app.controller.BaseController.logger;
import static app.controller.JavaFXApplication.getFxColor;
import app.node.BaseCompositeNode;
import app.node.BaseDecoratedNode;
import app.node.BaseNode;
import app.node.Grid;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author repp
 */
public class JavaFXGrid extends BaseJavaFXNode implements BaseCompositeNode {
    
    // Internal trackers for where to add the next child
    private int currentColumn = 0;
    private int currentRow = 1;
    
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
        this.resetCurrentCell();
        
        Double parentWidth = null;
        Double parentHeight = null;
        if (this.parent.controllerNode instanceof Region region) {
            parentWidth = region.getMinWidth();
            parentHeight = region.getMinHeight();
        } else if (this.parent.controllerNode instanceof Dialog dialog) {
            parentWidth = dialog.getDialogPane().getPrefWidth();
            parentHeight = dialog.getDialogPane().getPrefHeight();
        } else {
            logger.log(Level.SEVERE, "Grid's parent is not supported");
        }
        controllerNode.setPrefSize(parentWidth, parentHeight);
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
        if (node.padding > 0) {
            Insets cellPadding = new Insets(node.padding);
            controllerNode.setPadding(cellPadding);
        }
        
        // Configure dimensions
        if (node.columns == 0) {
            double squareRoot = Math.sqrt(cellCount);
            node.columns = (int) Math.ceil(squareRoot);
        }
        int rows = 0;
        if (node.columns != 0) {
            double rowsDiv = ((double) cellCount / (double) node.columns);  // Make sure values are double so remainder causes rows count to round up
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
        
        if ((node.columnWidths == null) || (node.columnWidths.isEmpty())) {
            // Allow columns to expand as much as they can
            int columnWidth = (int) Math.floor(100 / node.columns);
            for (int i = 0; i < node.columns; i++) {
                ColumnConstraints column = new ColumnConstraints();
                column.setPercentWidth(columnWidth);
                if (node.expandCells) {
                    column.setHgrow(Priority.ALWAYS);
                }
                controllerNode.getColumnConstraints().add(column);
                column.setFillWidth(node.expandCells);
            }
        } else {
            for (int i = 0; i < node.columns; i++) {
                ColumnConstraints column = new ColumnConstraints();
                Double columnWidth = node.columnWidths.get(i) * 100;
                column.setPercentWidth(columnWidth);
                controllerNode.getColumnConstraints().add(column);
                if (node.expandCells) {
                    column.setHgrow(Priority.NEVER);
                }
                column.setFillWidth(node.expandCells);
            }            
        }
        
        logger.log(Level.INFO, "cells={0}, columns={1}, rows={2}", new Object[]{cellCount, node.columns, rows});
        
        this.scaleNode(controllerNode);
    }
    
    public void advanceNextCell() {
        Grid node = (Grid) this.node;
        this.currentColumn++;
        if (this.currentColumn > node.columns) {
            this.currentRow++;
            this.currentColumn = 1;
        }
    }
    
    public void resetCurrentCell() {
        this.currentColumn = 0;
        this.currentRow = 1;
    }
    
    public Coordinates getCurrentCell() {
        return new Coordinates(this.currentColumn - 1, this.currentRow - 1);
    }
    
    public static List<String> getValues(GridPane gp) {
        logger.log(Level.INFO, "Entered");
        
        List<String> result = new ArrayList();
        
        for (Node gridChild : gp.getChildren()) {
            logger.log(Level.INFO, "Processing grid child");
            if (gridChild instanceof VBox box) {
                logger.log(Level.INFO, "Processing box");
                for (Node boxChild : box.getChildren()) {
                    logger.log(Level.INFO, "Processing box child");
                    if (boxChild instanceof Spinner spinner) {
                        logger.log(Level.INFO, "Processing spinner");
                        result.add((String) spinner.getValue());
                    } else if (boxChild instanceof ComboBoxBase cb) {
                        logger.log(Level.INFO, "Processing combo box");
                        result.add((String) cb.getValue());
                    } else if (boxChild instanceof ChoiceBox cb) {
                        logger.log(Level.INFO, "Processing choice box");
                        result.add((String) cb.getValue());
                    } else if (boxChild instanceof Label) {
                        logger.log(Level.INFO, "Skipping label");
                    } else {
                        logger.log(Level.WARNING, "Unsupported box child type : {0}", box.getClass().getSimpleName());
                    }
                }
            } else {
                logger.log(Level.WARNING, "Unsupported grid child type : {0}", gridChild.getClass().getSimpleName());
            }
        }
        
        return result;
    }
    
    @Override
    public Map<? extends BaseNode, Layout> getChildren() {
        Map<BaseNode, Layout> children = new LinkedHashMap();
        app.node.Grid grid = (app.node.Grid) this.node;
        for (BaseNode cell : grid.cells) {
            children.put(cell, null);
        }
        return children;
    }
    
    @Override
    public void onChildAdded(BaseDecoratedNode childDecoratedNode) {
        Node newChildNode = (Node) childDecoratedNode.controllerNode;
        Grid node = (Grid) this.node;
        GridPane controllerNode = (GridPane) this.controllerNode;
        for (Node gridChildNode : controllerNode.getChildren()) {
            if (!gridChildNode.equals(newChildNode)) {
                continue;
            }
            Integer nodeCol = GridPane.getColumnIndex(gridChildNode);
            if (node.columnHAlignments != null) {
                HorizontalAlignment alignment = node.columnHAlignments.get(nodeCol);
                if (alignment != null) {
                    logger.log(Level.INFO, "Setting H alignment for column {0} to {1}", new Object[]{nodeCol, alignment});
                    switch (alignment) {
                        case LEFT -> GridPane.setHalignment(newChildNode, HPos.LEFT);
                        case CENTER -> GridPane.setHalignment(newChildNode, HPos.CENTER);
                        case RIGHT -> GridPane.setHalignment(newChildNode, HPos.RIGHT);
                        default -> {
                            logger.log(Level.WARNING, "Unsupported H alignment {}", alignment);
                        }
                    }
                }
            }
            if (node.columnVAlignments != null) {
                VerticalAlignment alignment = node.columnVAlignments.get(nodeCol);
                if (alignment != null) {
                    logger.log(Level.INFO, "Setting V alignment for column {0} to {1}", new Object[]{nodeCol, alignment});
                    switch (alignment) {
                        case TOP -> GridPane.setValignment(newChildNode, VPos.TOP);
                        case CENTER -> GridPane.setValignment(newChildNode, VPos.CENTER);
                        case BOTTOM -> GridPane.setValignment(newChildNode, VPos.BOTTOM);
                        default -> {
                            logger.log(Level.WARNING, "Unsupported V alignment {}", alignment);
                        }
                    }
                }
            }
        }
    }
    
}
