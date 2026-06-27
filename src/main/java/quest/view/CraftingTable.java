package quest.view;

import app.controller.BaseController;
import app.view.BaseView;
import app.color.Color;
import app.HorizontalAlignment;
import app.Layout;
import app.RelativeCoordinates;
import app.Text;
import app.TextDecoration;
import app.VerticalAlignment;
import static app.controller.BaseController.logger;
import app.dialog.InternalFileSelection;
import app.node.BaseNode;
import app.node.Button;
import app.node.ComboBox;
import app.node.Dialog;
import app.node.Field;
import app.node.Grid;
import app.node.Group;
import app.node.HorizontalGroup;
import app.node.Label;
import app.node.ScrollingPane;
import app.node.VerticalGroup;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import quest.model.Act;
import quest.model.Book;

/**
 *
 * @author repp
 */
public class CraftingTable extends BaseView {
    
    public static final String BOOK = "book";
    public static final Double DEFAULT_PIXEL_SIZE = BaseController.DEFAULT_PIXEL_SIZE - 8;
    
    BaseController appController;
    public Book book = new Book();
    public ScrollingPane editor = null;
    public String lastSelectedItem = null;
    public String lastSelectedType = null;
    public String selectedItem = null;
    public String selectedType = null;
    public TextDecoration smallWhiteText;
    public TextDecoration whiteText;

    public CraftingTable(String name) {
        super(name);
        this.backgroundColor = new Color(255, 255, 255, 1.0);
        this.backgroundImage = "/assets/images/dark-abstract.jpg";
        this.backgroundRepeat = true;
        this.emojis.add("\uD83E\uDE9A"); // "carpentry saw" Unicode emoji
        this.smallWhiteText = new TextDecoration();
        this.smallWhiteText.color = Color.WHITE;
        this.smallWhiteText.pixelSize = DEFAULT_PIXEL_SIZE;
        this.whiteText = new TextDecoration();
        this.whiteText.color = Color.WHITE;
    }
    
    @Override
    public void onLoad(BaseController appController) {
        logger.log(Level.INFO, "Entered: appController={0}", appController);
        
        this.appController = appController;
        
        // Book
        this.refreshBook();
        
        // Book parts
        Grid bookPartsGrid = new Grid("book parts grid");
        bookPartsGrid.backgroundColor = null; // Transparent
        bookPartsGrid.borderWidth = 1;
        bookPartsGrid.columns = 4;
        bookPartsGrid.cells = new ArrayList();
        bookPartsGrid.showBorders = true;
        bookPartsGrid.expandCells = true;
        
        Label newAct = new Label("book parts acts new");
        newAct.alignment = HorizontalAlignment.CENTER;
        newAct.backgroundColor = new Color(Color.GOLD, 0.5);
        newAct.borderWidth = 1;
        newAct.texts = List.of(new Text("New Act", this.smallWhiteText));
        bookPartsGrid.cells.add(newAct);
        Label newScene = new Label("book parts scenes new");
        newScene.alignment = HorizontalAlignment.CENTER;
        newScene.backgroundColor = new Color(Color.GOLD, 0.5);
        newScene.borderWidth = 1;
        newScene.texts = List.of(new Text("New Scene", this.smallWhiteText));
        bookPartsGrid.cells.add(newScene);
        Label newPage = new Label("book parts pages new");
        newPage.alignment = HorizontalAlignment.CENTER;
        newPage.backgroundColor = new Color(Color.GOLD, 0.5);
        newPage.borderWidth = 1;
        newPage.texts = List.of(new Text("New Page", this.smallWhiteText));
        bookPartsGrid.cells.add(newPage);
        Label newContent = new Label("book parts content new");
        newContent.alignment = HorizontalAlignment.CENTER;
        newContent.backgroundColor = new Color(Color.GOLD, 0.5);
        newContent.borderWidth = 1;
        newContent.texts = List.of(new Text("New Content", this.smallWhiteText));
        bookPartsGrid.cells.add(newContent);
        
        Group actsDocGroup = new VerticalGroup("acts document group");
        ScrollingPane actsPane = new ScrollingPane("acts document");
        actsPane.backgroundColor = new Color(Color.BLACK, 0.5);
        actsPane.borderWidth = 1;
        actsDocGroup.nodes.put(actsPane, null);
        bookPartsGrid.cells.add(actsDocGroup);
        Group scenesDocGroup = new VerticalGroup("scenes document group");
        ScrollingPane scenesPane = new ScrollingPane("scenes document");
        scenesPane.backgroundColor = new Color(Color.BLACK, 0.5);
        scenesPane.borderWidth = 1;
        scenesDocGroup.nodes.put(scenesPane, null);
        bookPartsGrid.cells.add(scenesDocGroup);
        Group pagesDocGroup = new VerticalGroup("pages document group");
        ScrollingPane pagesPane = new ScrollingPane("pages document");
        pagesPane.backgroundColor = new Color(Color.BLACK, 0.5);
        pagesPane.borderWidth = 1;
        pagesDocGroup.nodes.put(pagesPane, null);
        bookPartsGrid.cells.add(pagesDocGroup);
        Group storiesDocGroup = new VerticalGroup("content document group");
        ScrollingPane storiesPane = new ScrollingPane("content document");
        storiesPane.backgroundColor = new Color(Color.BLACK, 0.5);
        storiesPane.borderWidth = 1;
        storiesDocGroup.nodes.put(storiesPane, null);
        bookPartsGrid.cells.add(storiesDocGroup);
        
        bookPartsGrid.scaleX = 0.48;
        bookPartsGrid.scaleY = 0.83;
        this.appController.addNode(this.name, this.name, bookPartsGrid, new Layout(new RelativeCoordinates(0.01, 0.16), HorizontalAlignment.LEFT, VerticalAlignment.TOP));
        
        // Acts
        Grid actsGrid = new Grid("acts grid");
        actsGrid.backgroundColor = null; // Transparent
        actsGrid.borderWidth = 1;
        actsGrid.columns = 1;
        actsGrid.cells = new ArrayList();
        //actsGrid.showBorders = true;
        //actsGrid.borderPadding = 5;
        //actsGrid.padding = 5;
        for (String actName : this.book.acts.keySet()) {
            Act act = this.book.acts.get(actName);
            Group actGroup = new VerticalGroup("act group " + actName);
            actGroup.backgroundColor = Color.RED;
            actGroup.borderWidth = 0;
            /*
            Button actButton = new Button("act button " + actName);
            actButton.eventListener = this;
            actButton.eventName = "act " + actName;
            actButton.isMultiUse = true;
            actButton.pixelSize = DEFAULT_PIXEL_SIZE;
            actButton.text = actName;
            actButton.backgroundColor = new Color(Color.BLACK, 0.5);
            actButton.textColor = Color.WHITE;
            actButton.expand = true;
            actGroup.nodes.put(actButton, null);
            */
            actsGrid.cells.add(actGroup);
        }
        this.appController.addNode(this.name, "acts document", actsGrid, null);
        
        for (String actName : this.book.acts.keySet()) {
            Act act = this.book.acts.get(actName);
            Button actButton = new Button("act button " + actName);
            actButton.eventListener = this;
            actButton.eventName = "act " + actName;
            actButton.isMultiUse = true;
            actButton.pixelSize = DEFAULT_PIXEL_SIZE;
            actButton.text = actName;
            actButton.backgroundColor = new Color(Color.BLACK, 0.5);
            actButton.textColor = Color.WHITE;
            actButton.expand = true;
            actButton.scaleX = 1.0;
            this.appController.addNode(this.name, "act group " + actName, actButton, null);
        }        
        
        // Save
        Button saveButton = new Button("save");
        saveButton.eventListener = this;
        saveButton.eventName = "save";
        saveButton.isMultiUse = true;
        saveButton.text = "Save";
        appController.addNode(this.name, this.name, saveButton, new Layout(new RelativeCoordinates(0.97, 0.97), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM));
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        logger.log(Level.INFO, "Entered: eventName={0}, eventValue={1}", new Object[]{eventName, eventValue});
        switch (eventName) {
            case BOOK -> {
                this.selectedItem = BOOK;
                this.selectedType = BOOK;
                this.refreshEditor();
            }
            case "title" -> {
                this.book.title = eventValue.toString();
                this.refreshBook();
            }
            case "animation file" -> {
                InternalFileSelection fileSelection = new InternalFileSelection("file selection");
                fileSelection.emoji = "\uD83D\uDCC2";
                fileSelection.eventListener = this;
                fileSelection.eventName = "animation file selected";
                fileSelection.title = "Select Resource";
                fileSelection.path = "/assets/videos";
                this.appController.newDialog(fileSelection);
            }
            case "animation file selected" -> {
                if (!eventValue.equals("")) {
                    this.book.animationFileName = "/assets/videos/" + eventValue.toString();
                    this.refreshEditor();
                }
            }
            case "save" -> {
                this.book.updateDate = LocalDate.now();
                this.refreshBook();
            }
            case "first act button" -> {
                Map<BaseNode, Layout> options = new LinkedHashMap();

                ComboBox comboBox = new ComboBox("Opening Act");
                for (String actName : this.book.acts.keySet()) {
                    comboBox.values.add(new Text(actName));
                }
                comboBox.defaultValue = this.book.firstActName;
                options.put(comboBox, null);

                Dialog optionsDialog = new Dialog("first act dialog", "Opening Act", "Select the quest's opening act", options);
                optionsDialog.eventName = "first act selected";
                optionsDialog.eventListener = this;
                optionsDialog.relativeSize = 0.25;
                this.appController.addNode(this.name, this.name, optionsDialog, null);
            }
            case "first act selected" -> {
                List<String> resultList = (ArrayList) eventValue;
                if ((!resultList.isEmpty()) && (!resultList.get(0).equals(""))) {
                    logger.log(Level.INFO, "Selected {0}", resultList.get(0));
                    this.book.firstActName = resultList.get(0);
                    this.refreshEditor();
                } else {
                    logger.log(Level.INFO, "Nothing selected");
                }
            }
            default -> logger.log(Level.WARNING, "Unsupported event!");
        }
    }
    
    public void refreshEditor() {
        //if ((this.lastSelectedType != null) && (!this.lastSelectedType.equals(this.selectedType)) && (!this.lastSelectedItem.equals(this.selectedItem))) {
            logger.log(Level.INFO, "Removing editor");
            this.appController.removeNode(this.name, "editor");
            this.editor = null;
        //}

        // Add/Re-add the editor to the view
        if (this.editor == null) {
            logger.log(Level.INFO, "Adding new editor instance");
            this.editor = new ScrollingPane("editor");
            this.editor.backgroundColor = new Color(Color.BLACK, 0.5);
            this.editor.borderWidth = 1;
            this.editor.scaleX = 0.47;
            this.editor.scaleY = 0.7;
            this.appController.addNode(this.name, this.name, this.editor, new Layout(new RelativeCoordinates(0.51, 0.16), HorizontalAlignment.LEFT, VerticalAlignment.TOP));
        }
        
        //if ((this.selectedType == null) || ((this.lastSelectedType != null) && (this.lastSelectedItem != null) && (this.lastSelectedType.equals(this.selectedType)) && (this.lastSelectedItem.equals(this.selectedItem)))) {
        //    logger.log(Level.INFO, "Either nothing was selected or the selection hasn't changed");
        //} else if (this.selectedType == BOOK) {
            this.refreshBookEditor();
        //}

        this.lastSelectedItem = this.selectedItem;
        this.lastSelectedType = this.selectedType;        
    }
    
    public void refreshBook() {
        this.appController.removeNode(this.name, "book");
        this.appController.removeNode(this.name, BOOK + " last update");
        
        Button bookButton = new Button("book");
        bookButton.backgroundColor = new Color(Color.BLACK, 0.5);
        bookButton.textColor = Color.WHITE;
        bookButton.borderWidth = 0;
        bookButton.eventListener = this;
        bookButton.eventName = BOOK;
        bookButton.isMultiUse = true;
        bookButton.text = this.book.title;
        bookButton.scaleX = 0.48;
        bookButton.scaleY = 0.10;
        this.appController.addNode(this.name, this.name, bookButton, new Layout(new RelativeCoordinates(0.01, 0.01), HorizontalAlignment.LEFT, VerticalAlignment.TOP));
        
        Label lastUpdateLabel = new Label(BOOK + " last update");
        lastUpdateLabel.alignment = HorizontalAlignment.CENTER;
        lastUpdateLabel.backgroundColor = new Color(Color.BLACK, 0.5);
        lastUpdateLabel.borderWidth = 0;
        lastUpdateLabel.texts = List.of(new Text("Last Updated: " + this.book.updateDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.getDefault())), this.smallWhiteText));
        lastUpdateLabel.alignment = HorizontalAlignment.LEFT;
        lastUpdateLabel.scaleX = 0.48;
        lastUpdateLabel.scaleY = 0.03;
        this.appController.addNode(this.name, this.name, lastUpdateLabel, new Layout(new RelativeCoordinates(0.01, 0.12), HorizontalAlignment.LEFT, VerticalAlignment.TOP));
    }
    
    public void refreshBookEditor() {
        logger.log(Level.INFO, "Configuring the editor for the book");
        
        this.appController.removeNode(this.name, BOOK + " grid");
        
        // TODO - Add a one column grid of input fields
        Grid propertyGrid = new Grid(BOOK + " grid");
        propertyGrid.backgroundColor = null; // Transparent
        propertyGrid.borderWidth = 1;
        propertyGrid.columns = 2;
        propertyGrid.cells = new ArrayList();
        propertyGrid.expandCells = true;
        propertyGrid.showBorders = false;
        
        Label titleLabel = new Label(BOOK + " title label");
        titleLabel.backgroundColor = null;
        titleLabel.borderWidth = 0;
        titleLabel.texts = List.of(new Text("Title:", this.smallWhiteText));
        titleLabel.alignment = HorizontalAlignment.RIGHT;
        propertyGrid.cells.add(titleLabel);
        
        Field titleField = new Field(BOOK + " title input");
        titleField.isUpperCase = false;
        titleField.length = 40;
        titleField.displayLength = 40;
        titleField.textColor = Color.WHITE;
        titleField.pixelSize = DEFAULT_PIXEL_SIZE;
        titleField.initialValue = this.book.title;
        titleField.eventName = "title";
        titleField.eventListener = this;
        propertyGrid.cells.add(titleField);
        
        Label authorLabel = new Label(BOOK + " author label");
        authorLabel.backgroundColor = null;
        authorLabel.borderWidth = 0;
        authorLabel.texts = List.of(new Text("Author:", this.smallWhiteText));
        authorLabel.alignment = HorizontalAlignment.RIGHT;
        propertyGrid.cells.add(authorLabel);
        
        Field authorField = new Field(BOOK + " author input");
        authorField.isUpperCase = false;
        authorField.length = 40;
        authorField.displayLength = 40;
        authorField.textColor = Color.WHITE;
        authorField.pixelSize = DEFAULT_PIXEL_SIZE;
        authorField.initialValue = this.book.author;
        authorField.eventName = "author";
        authorField.eventListener = this;
        propertyGrid.cells.add(authorField);
        
        Label animationFileLabel = new Label(BOOK + " animation File label");
        animationFileLabel.backgroundColor = null;
        animationFileLabel.borderWidth = 0;
        animationFileLabel.texts = List.of(new Text("Opening Animation:", this.smallWhiteText));
        animationFileLabel.alignment = HorizontalAlignment.RIGHT;
        propertyGrid.cells.add(animationFileLabel);
        
        HorizontalGroup animationFileGroup = new HorizontalGroup("animation file group");
        animationFileGroup.alignment = HorizontalAlignment.LEFT;
        animationFileGroup.borderWidth = 1;
        
        Field animationFile = new Field(BOOK + " animation file");
        animationFile.borderWidth = 0;
        animationFile.isUpperCase = false;
        animationFile.length = 50;
        animationFile.displayLength = 44;
        animationFile.textColor = Color.WHITE;
        animationFile.pixelSize = DEFAULT_PIXEL_SIZE;
        animationFile.initialValue = this.book.animationFileName;
        animationFile.eventName = "animation file field";
        animationFile.eventListener = this;
        animationFileGroup.nodes.put(animationFile, null);
        
        Button animationFileButton = new Button("animation file button");
        animationFileButton.eventListener = this;
        animationFileButton.eventName = "animation file";
        animationFileButton.isMultiUse = true;
        animationFileButton.text = "\uD83D\uDCC2";
        animationFileButton.pixelSize = DEFAULT_PIXEL_SIZE + 4;
        animationFileGroup.nodes.put(animationFileButton, null);
        propertyGrid.cells.add(animationFileGroup);
 
        Label firstActLabel = new Label(BOOK + " first act label");
        firstActLabel.backgroundColor = null;
        firstActLabel.borderWidth = 0;
        firstActLabel.texts = List.of(new Text("Opening Act:", this.smallWhiteText));
        firstActLabel.alignment = HorizontalAlignment.RIGHT;
        propertyGrid.cells.add(firstActLabel);
        
        HorizontalGroup firstActGroup = new HorizontalGroup("first act group");
        firstActGroup.alignment = HorizontalAlignment.LEFT;
        firstActGroup.borderWidth = 1;
        
        Field firstAct = new Field(BOOK + " first act");
        firstAct.borderWidth = 0;
        firstAct.isUpperCase = false;
        firstAct.length = 50;
        firstAct.displayLength = 44;
        firstAct.textColor = Color.WHITE;
        firstAct.pixelSize = DEFAULT_PIXEL_SIZE;
        firstAct.initialValue = this.book.firstActName;
        firstAct.eventName = "first act field";
        firstAct.eventListener = this;
        firstActGroup.nodes.put(firstAct, null);
        
        Button firstActButton = new Button("first act button");
        firstActButton.eventListener = this;
        firstActButton.eventName = "first act button";
        firstActButton.isMultiUse = true;
        firstActButton.text = "\uD83D\uDCC2";
        firstActButton.pixelSize = DEFAULT_PIXEL_SIZE + 4;
        firstActGroup.nodes.put(firstActButton, null);
        propertyGrid.cells.add(firstActGroup);
        
        propertyGrid.scaleX = 1.0;
        propertyGrid.columnWidths = new ArrayList();
        propertyGrid.columnWidths.add(0.2);
        propertyGrid.columnWidths.add(0.8);
        propertyGrid.columnHAlignments = new HashMap();
        propertyGrid.columnHAlignments.put(0, HorizontalAlignment.RIGHT);
        propertyGrid.columnHAlignments.put(1, HorizontalAlignment.LEFT);
                
        this.appController.addNode(this.name, this.editor.name, propertyGrid, null);
    }
    
}