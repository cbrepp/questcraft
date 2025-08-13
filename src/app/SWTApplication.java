package app;

import app.control.BaseControl;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEffect;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SWTApplication extends ApplicationController {

    // Settings for Follow-The-Button glow effect for buttons
    public static int CURRENT_COLOR_INDEX = 0;
    public static final int ANIMATION_DELAY = 100; // milliseconds
    public static List<String> TIMER_EVENTS = new ArrayList();
    
    public static int direction = 1;
    public Display display;
    public String emptyBook;
    public int fontHeight = 0;
    public int fontWidth = 0;
    public int buttonFontHeight = 0;
    public int buttonFontWidth = 0;
    public Font buttonFont;
    public KeyEvent lastProcessedKeyEvent;
    public Font monospaceFont;
    public HashMap<String, Map<String, Composite>> namedControls;
    public ApplicationView parentView;
    public Shell shell;
    public CTabFolder tabFolder;
    public HashMap<String, Composite> tabCompositeMap;
    public HashMap<String, Integer> tabIndexMap;
    public HashMap<String, CTabItem> tabItemMap;
    public HashMap<CTabItem, ApplicationView> tabItemViewMap;
    public HashMap<String, List<StyleRange>> tabStyleRangesMap;
    public HashMap<String, StyledText> tabStyledTextMap;
    public int textColumns = 0;
    public int textRows = 0;
    public HashMap<String, ApplicationView> views;
    
    /**
     * The implementation of this method is a work-around to inheritance not being fully implemented in java
     * for static methods.  While child classes can inherit a static method from a parent class, there is no
     * way to know within the inherited method for which class it is being executed.  Also, there is no good
     * way to know within any static method what the name of the current class is without using a Throwable.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            args[0] = new Throwable().getStackTrace()[0].getClassName();
        }
        ApplicationController.main(args);
    }
    
    @Override
    public void setDelegate(Object delegate) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void open(ApplicationView splashView, ApplicationView mainView) {
        if (splashView != null) {
            this.display = new Display();
            this.displayShell(splashView);
        }
        
        if (mainView != null) {
            this.display = new Display();
            this.displayShell(mainView);
        }
    }
    
    @Override
    public void close() {
        this.display.dispose();
    }
    
    public void displayShell(ApplicationView view) {
        System.out.println("SWTApplication: displayShell: view=" + view.name);

        // Initialize the application window
        int shellStyle = view.isSplash ? SWT.NO_TRIM | SWT.ON_TOP : SWT.SHELL_TRIM;
        Shell shell = new Shell(this.display, shellStyle);
        shell.setLayout(new FillLayout());
        shell.setText(view.name);

        // Size the application window        
        Point dimensions = getDimensions(view.backgroundImage);
        shell.setSize(dimensions);
        
        // Set the application icon
        Image iconImage = null;
        if (view.iconFileName != null) {
            iconImage = loadImage(view.iconFileName);
            shell.setImage(iconImage);
        }
        
        // Create the application window composite
        Composite composite = new Composite(shell, SWT.NONE);
        composite.setLayout(new FillLayout());
        composite.setBounds(0,0,dimensions.x,dimensions.y);
        
        // Create a tab folder to contain the child views
        CTabFolder tabFolder;
        final SWTApplication thisController = this;
        tabFolder = new CTabFolder(composite, SWT.BORDER);
        if (view.backgroundColor != null) {
            tabFolder.setBackground(new Color(view.backgroundColor.red, view.backgroundColor.green, view.backgroundColor.blue));
        }
        tabFolder.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int selectedIndex = thisController.tabFolder.getSelectionIndex();
                String selectedTabTitle = thisController.tabFolder.getItem(selectedIndex).getText();
                CTabItem item = tabFolder.getSelection();
                System.out.println("SWTApplication: displayShell: Selected tab " + selectedTabTitle + ", map size = " + thisController.tabItemViewMap.size());
                ApplicationView selectedView = thisController.tabItemViewMap.get(item);
                if (selectedView != null) {
                    selectedView.onSelected(thisController);
                }
            }
        });

        // Share important state with the other instance methods
        this.shell = shell;
        this.parentView = view;
        this.tabFolder = tabFolder;
        this.tabStyledTextMap = new HashMap<>();
        this.tabCompositeMap = new HashMap<>();
        this.tabCompositeMap.put(view.name, composite);
        this.tabStyleRangesMap = new HashMap<>();
        this.tabIndexMap = new HashMap<>();
        this.tabItemMap = new HashMap<>();
        this.tabItemViewMap = new HashMap<>();
        this.views = new HashMap();
        this.namedControls = new HashMap();
            
        // Init a font for all text areas to use
        this.monospaceFont = new Font(this.display, "Consolas", 12, SWT.NORMAL);
        this.buttonFont = new Font(this.display, "Consolas", 10, SWT.NORMAL);
        
        // Calculate the height and width of the font (TODO - this works for instancing GC with a text area, not sure about composite)
        GC gc = new GC(composite);
        gc.setFont(this.buttonFont);
        Point extent = gc.stringExtent("W");
        this.buttonFontHeight = extent.y;
        this.buttonFontWidth = extent.x;
        System.out.println("SWTApplication: displayShell: buttonFontWidth=" + this.buttonFontWidth + ", buttonFontHeight=" + this.buttonFontHeight);
        gc.setFont(this.monospaceFont);
        extent = gc.stringExtent("W");
        this.fontHeight = extent.y;
        this.fontWidth = extent.x;
        System.out.println("SWTApplication: displayShell: fontWidth=" + this.fontWidth + ", fontHeight=" + this.fontHeight);

        // Calculate the textual height and width of a possible text area
        this.textColumns = this.getColumns(dimensions.x) - 1; // Subtract by 1 to prevent rounding from exceeding the available space
        this.textRows = this.getRows(dimensions.y) - 1; // Subtract by 1 to prevent rounding from exceeding the available space
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < textRows; j++) {
                for (int i = 0; i < textColumns; i++) {
                        sb.append(' ');
                }
                sb.append('\n');
        }
        this.emptyBook = sb.toString();
        
        this.addView(view, true);
        
        // Open or update the shell as needed
        if (!this.shell.isVisible()) {
            System.out.println("SWTApplication: displayShell: Opening shell");
            this.shell.open();
        } else {
            System.out.println("SWTApplication: displayShell: Updating shell");
            this.shell.update();
        }
        
        // Only pack if the main view isn't a text area
        if (!view.addTextArea) {
            this.shell.pack();
        }
        
        view.onDisplay(this);
        
        while (!shell.isDisposed ()) {
            if (!this.display.readAndDispatch ()) {
                this.display.sleep ();
            }
        }
        
        if (view.iconFileName != null) {
            iconImage.dispose();
        }
        
        if (!shell.isDisposed()) {
            this.cleanShell();
            this.shell.dispose();
        }
        
        Utility.stopAllSounds();
    }
    
    @Override
    public void selectTab(String viewName) {
        System.out.println("SWTApplication: selectTab: viewName=" + viewName);
        
        Integer index = this.getTabIndex(viewName);
        if (index == null) {
            System.err.println("SWTApplication: selectTab: The view does not have a tab!");
            return;
        }
        
        System.out.println("SWTApplication: selectTab: Setting tab selection to index " + index);
        this.tabFolder.setSelection(index);
    }

    @Override
    public void displayView(ApplicationView view) {
        System.out.println("SWTApplication: displayView: Displaying application view: " + view.name);
        
        int tabIndex = this.tabIndexMap.get(view.name);
        System.out.println("SWTApplication: displayTab: Tab index=" + tabIndex);
        
        if (this.tabFolder != null) {
            this.tabFolder.setSelection(tabIndex);
        }
        
        view.onDisplay(this);
    }
    
    @Override
    public Integer getTabIndex(String viewName) {
        System.out.println("SWTApplication: getTabIndex: viewName=" + viewName);
        Integer index = this.tabIndexMap.get(viewName);
        return index;
    }
    
    @Override
    public void displayView(String viewName) {
        System.out.println("SWTApplication: displayTab: viewName=" + viewName);
        ApplicationView view = this.views.get(viewName);
        this.displayView(view);
    }
    
    @Override
    public void renameTab(String viewName, String newViewName) {
        System.out.println("SWTApplication: renameView: viewName=" + viewName + ", newViewName=" + newViewName);
        CTabItem tabItem = this.tabItemMap.get(viewName);
        tabItem.setText(newViewName);
    }
    
    @Override
    public void addView(ApplicationView view) {
        this.addView(view, false);
    }
    
    public void addView(ApplicationView view, Boolean isParent) {
        Integer index = this.tabIndexMap.get(view.name);
        if (index == null) {
            index = this.tabIndexMap.size();
        }
        this.addView(view, isParent, index);
    }
    
    @Override
    public void addView(ApplicationView view, Boolean isParent, int index) {
        System.out.println("SWTApplication: addView: name=" + view.name + ", isParent=" + isParent + ", index=" + index);
        
        Composite composite;
        if (isParent) {
            composite = tabCompositeMap.get(view.name);
        } else {
            // Create a new tab with a composite
            CTabItem tab = new CTabItem(this.tabFolder, SWT.NONE, index);
            this.tabItemMap.put(view.name, tab);
            this.tabItemViewMap.put(tab, view);
            if (view.emoji == null) {
                tab.setText(view.name);
            } else {
                tab.setText(view.emoji + " " + view.name);
            }
            composite = new Composite(this.tabFolder, SWT.NONE);
            if (view.backgroundColor != null) {
                composite.setBackground(new Color(view.backgroundColor.red, view.backgroundColor.green, view.backgroundColor.blue));
            }
            composite.setLayout(null);
            
            String appImageFile = this.parentView.backgroundImage;
            Point dimensions = getDimensions(appImageFile);
            composite.setBounds(0, 0, dimensions.x, dimensions.y);
            
            tab.setControl(composite);
            this.tabCompositeMap.put(view.name, composite);
            
            // Track the tab position of each view.
            // Inserting a new view shifts all of the other views to the right.
            HashMap<String, Integer> indicesToAdd = new HashMap();
            Iterator<String> iterator = this.tabIndexMap.keySet().iterator();
            while (iterator.hasNext()) {
                String viewName = iterator.next();
                int currentIndex = this.tabIndexMap.get(viewName);
                if (currentIndex >= index) {
                    currentIndex++;
                    iterator.remove();
                    indicesToAdd.put(viewName, currentIndex);
                }
            }
            for (String viewName : indicesToAdd.keySet()) {
                Integer currentIndex = indicesToAdd.get(viewName);
                this.tabIndexMap.put(viewName, currentIndex);
            }
            this.tabIndexMap.put(view.name, index);
            
            this.views.put(view.name, view);
            
            if (this.namedControls.get(view.name) == null) {
                this.namedControls.put(view.name, new HashMap());
            }
        }
           
        Control control = composite;
        if (view.addTextArea) {
            // Add a text area to the composite.
            // Not using a layout allows controls to overlay.  This is needed so controls can display on top of the text area.
            StyledText textArea = new StyledText(composite, SWT.NONE);
            String appImageFile = this.parentView.backgroundImage;
            Point dimensions = getDimensions(appImageFile);
            textArea.setBounds(0, 0, dimensions.x, dimensions.y);
            textArea.moveAbove(composite);
            textArea.setEditable(false);
            textArea.setFont(this.monospaceFont);
            textArea.setText(this.emptyBook);
            this.tabStyledTextMap.put(view.name, textArea);
            
            control = textArea;

            // Init style ranges
            List<StyleRange> styleRanges = new ArrayList<>();
            this.tabStyleRangesMap.put(view.name, styleRanges);
        }
        
        // Set the text area's background color and image
        app.Color imageColor = view.backgroundColor;
        if (imageColor == null) {
            imageColor = this.parentView.backgroundColor;
        }
        if (imageColor == null) {
            imageColor = new app.Color(0, 0, 0);
        }
        control.setBackground(new Color(this.display, imageColor.red, imageColor.green, imageColor.blue));
        if (view.backgroundImage != null) {
            final Image backgroundImage = loadImage(view.backgroundImage);
            control.setBackgroundImage(backgroundImage);
            System.out.println("SWTApplication: addView: Set background image to " + view.backgroundImage);
            final Control backgroundContainer = control;
            control.addListener(SWT.Resize, event -> {
                backgroundContainer.setBackgroundImage(backgroundImage);
            });
        }
        System.out.println("SWTApplication: addView: backgroundColor=" + imageColor + ", backgroundImageFile=" + view.backgroundImage);
        
        view.onLoad(this);
    }
    
    public void cleanShell() {
        System.out.println("SWTApplication: cleanShell");            
        Control[] controls = this.shell.getChildren();
        for (Control control : controls) {
            System.out.println("SWTApplication: cleanShell: Disposing " + control);
            if (control.getBackgroundImage() != null) {
                control.getBackgroundImage().dispose();
            }
            control.dispose();
        }
    }
    
    public static Point getDimensions(String imageFile) {
        System.out.println("SWTApplication: getDimensions: imageFile=" + imageFile);
        Point dimensions = null;
        try (InputStream inputStream = SWTApplication.class.getResourceAsStream(imageFile)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Image not found at path: " + imageFile);
            }
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            dimensions = new Point(bufferedImage.getWidth(), bufferedImage.getHeight());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image from path: " + imageFile, e);
        }
        return dimensions;
    }
    
    public Point convertToCoordinates(double row, double column) {
        System.out.println("SWTApplication: convertToCoordinates: row=" + row + ", column=" + column);
        int x = (int)(column * this.fontWidth) - this.fontWidth;
        int y = (int)(row * this.fontHeight) - this.fontHeight;
        Point coordinates = new Point(x, y);
        System.out.println("SWTApplication: convertToCoordinates: font width=" + this.fontWidth + ", font height=" + this.fontHeight + ", x=" + coordinates.x + ", y=" + coordinates.y);
        return coordinates;
    }
    
    public Image loadImage(String fileName) {
        InputStream imageStream = SWTApplication.class.getResourceAsStream(fileName);
        if (imageStream == null) {
            System.err.println("SWTApplication: loadImage: Image not found in classpath: " + fileName);
            return null;
        }
        final Image image = new Image(this.display, imageStream);
        return image;
    }
    
    public int getColumns(int x) {
        int columns = ((int) x / this.fontWidth) + 1;
        return columns;
    }
    
    public int getRows(int y) {
        int rows = ((int) y / this.fontHeight) + 1;
        return rows;
    }
    
    @Override
    public void setBackgroundImage(String viewName, String imageFileName) {
        StyledText textArea = this.tabStyledTextMap.get(viewName);
        final Image backgroundImage = loadImage(imageFileName);
        textArea.setBackgroundImage(backgroundImage);
        textArea.addListener(SWT.Resize, event -> {
            textArea.setBackgroundImage(backgroundImage);
        });
    }
    
    @Override
    public int getTextColumns() {
        return this.textColumns;
    }
    
    @Override
    public int getTextRows() {
        return this.textRows;
    }
    
    @Override
    public int getColumns(String fileName) {
        System.out.println("SWTApplication: getColumns: fileName=" + fileName);
        Point dimensions = getDimensions(fileName);
        int columns = getColumns(dimensions.x);
        return columns;
    }
    
    @Override
    public int getRows(String fileName) {
        System.out.println("SWTApplication: getRows: fileName=" + fileName);
        Point dimensions = getDimensions(fileName);
        int rows = getRows(dimensions.x);
        return rows;
    }
    
    @Override
    public int getButtonColumns(String buttonText) {
        int width = (buttonText.length() * this.fontWidth) + (2 * this.fontWidth);    // Calculate width of text plus buffer of two imaginary characters
        int columns = this.getColumns(width);
        return columns;
    }
    
    @Override
    public int getButtonRows() {
        int height = 2 * this.fontHeight;   // Calculate double height of text
        int rows = getRows(height);
        return rows;
    }
    
    @Override
    public void clearScreen(String viewName) {
        System.out.println("SWTApplication: clearScreen : viewName=" + viewName); 

        this.namedControls.get(viewName).clear();

        Composite tabComposite = this.tabCompositeMap.get(viewName);
        Control[] controls = tabComposite.getChildren();
        for (Control control : controls) {
            System.out.println("SWTApplication: clearScreen: Disposing " + control);
            if (!control.getClass().toString().equals("class org.eclipse.swt.custom.StyledText")) {
                System.out.println("SWTApplication: class is " + control.getClass().toString());
                control.dispose();
            }
        }

        StyledText textArea = this.tabStyledTextMap.get(viewName);
        if (textArea != null) {
            textArea.setText(this.emptyBook);
            List<StyleRange> styleRanges = tabStyleRangesMap.get(viewName);
            styleRanges.clear();
        }        
    }
    
    @Override
    public void clearControl(String viewName, String controlName) {
        System.out.println("SWTApplication: clearControl : viewName=" + viewName + ", controlName=" + controlName);
        Composite control = this.namedControls.get(viewName).get(controlName);
        if (control != null) {
            control.dispose();
            this.namedControls.get(viewName).remove(controlName);
        }
    }
    
    @Override
    public void displayMessageBox(String title, String text, int level) {
        int SWTIcon;
        SWTIcon = switch (level) {
            case Icon.INFORMATION -> SWT.ICON_INFORMATION;
            case Icon.WARNING -> SWT.ICON_WARNING;
            case Icon.ERROR -> SWT.ICON_ERROR;
            case Icon.CANCEL -> SWT.ICON_CANCEL;
            case Icon.QUESTION -> SWT.ICON_QUESTION;
            case Icon.SEARCH -> SWT.ICON_SEARCH;
            case Icon.WORKING -> SWT.ICON_WORKING;
            default -> SWT.ICON_INFORMATION;
        };

        MessageBox messageBox = new MessageBox(this.shell, SWTIcon | SWT.OK);
        if (title == null) {
            title = this.parentView.name;
        }
        messageBox.setText(title);
        messageBox.setMessage(text);
        messageBox.open();
    }
    
    @Override
    public void displayText(String viewName, String text, Integer row, Integer column) {
        System.out.println("SWTApplication: displayText: viewName=" + viewName + ", text=" + text + ", row=" + row + ", column=" + column);
        displayText(viewName, text, row, column, new app.Color(0, 0, 0));
    }
    
    @Override
    public void displayText(String viewName, String text, Integer row, Integer column, app.Color color) {
        System.out.println("SWTApplication: displayText: viewName=" + viewName + ", text=" + text + ", row=" + row + ", column=" + column + ", color=" + color);
        displayText(viewName, text, row, column, color, FontStyle.NORMAL);
    }
    
    @Override
    public void displayText(String viewName, String text, Integer row, Integer column, app.Color color, int style) {
        System.out.println("SWTApplication: displayText: viewName=" + viewName + ", text=" + text + ", row=" + row + ", column=" + column + ", color=" + color + ", style=" + style);
        
        Color SWTColor = new Color(color.red, color.green, color.blue);
        
        int SWTStyle;
        int SWTUnderlineStyle = 0;
        switch (style) {
            case FontStyle.NORMAL -> SWTStyle = SWT.NORMAL;
            case FontStyle.BOLD -> SWTStyle = SWT.BOLD;
            case FontStyle.ITALIC -> SWTStyle = SWT.ITALIC;
            case FontStyle.UNDERLINE_DOUBLE -> {
                SWTStyle= SWT.NORMAL;
                SWTUnderlineStyle = SWT.UNDERLINE_DOUBLE;
            }
            case FontStyle.UNDERLINE_ERROR -> {
                SWTStyle= SWT.NORMAL;
                SWTUnderlineStyle = SWT.UNDERLINE_ERROR;
            }
            case FontStyle.UNDERLINE_LINK -> {
                SWTStyle= SWT.NORMAL;
                SWTUnderlineStyle = SWT.UNDERLINE_LINK;
            }
            case FontStyle.UNDERLINE_SINGLE -> {
                SWTStyle= SWT.NORMAL;
                SWTUnderlineStyle = SWT.UNDERLINE_SINGLE;
            }
            case FontStyle.UNDERLINE_SQUIGGLE -> {
                SWTStyle= SWT.NORMAL;
                SWTUnderlineStyle = SWT.UNDERLINE_SQUIGGLE;
            }
            default -> {
                System.out.println("SWTApplication: displayText: Unsupported font style!");
                SWTStyle = SWT.NORMAL;
            }
        }
        
        StyledText textArea = this.tabStyledTextMap.get(viewName);
        List<StyleRange> styleRanges = tabStyleRangesMap.get(viewName);
        String currentText = textArea.getText();
        Integer position = column - 1; // String positions start at zero
        position = position + (this.textColumns * (row - 1)) + (row - 1);
        System.out.println("SWTApplication: displayText: this.textColumns=" + this.textColumns + ", position=" + position + ", SWTStyle=" + SWTStyle);
        StringBuilder sb = new StringBuilder(currentText);
        sb.replace(position, position + text.length(), text);
        textArea.setText(sb.toString());
        StyleRange textRange = new StyleRange();
        textRange.start = position;
        textRange.length = text.length();
        textRange.foreground = SWTColor;
        textRange.fontStyle = SWTStyle;
        if (style >= 3) {
            textRange.underline = true;
            textRange.underlineStyle = SWTUnderlineStyle;
        }
        if (((color.red != 0) || (color.green != 0) || (color.blue != 0)) || (SWTStyle != SWT.NORMAL) || (textRange.underline)) {
            // Skip styling if the color is black and the styling is normal
            styleRanges.add(textRange);
        }
        // Thanks to a limitation with SWT, each previous style range needs to be reapplied
        for (StyleRange range : styleRanges) {
            textArea.setStyleRange(range);
        }
        textArea.redraw();
    }
    
    @Override
    public void displayLink(String viewName, String name, String linkText, int row, int column, int length, EventListener listener) {
        System.out.println("SWTApplication: displayLink: viewName=" + viewName + ", name=" + name + ", linkText=" + linkText + ",row=" + row + ", column=" + column + ", length=" + length);
        
        StyledText textArea = this.tabStyledTextMap.get(viewName);
        Composite composite = this.tabCompositeMap.get(viewName);
        
        // Length needs to be provided because characters represented by two 16-bit Unicode characters will inflate the length
        Point upperLeftCoordinates = this.convertToCoordinates(row, column);
        Point upperRightCoordinates = this.convertToCoordinates(row, column + length);
        int width = upperRightCoordinates.x - upperLeftCoordinates.x;
        int height = 2 * this.fontHeight;
        
        Link link = new Link(composite, SWT.NONE);
        link.setFont(this.monospaceFont);
        link.setText(linkText);
        link.setBounds(upperLeftCoordinates.x + 1, upperLeftCoordinates.y + 1, width, height);
        link.moveAbove(textArea);
        link.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                System.out.println("SWTApplication: displayLink: Link clicked: " + e.text);
                listener.onEvent(name, null);
            }
        });
    }
    
    @Override
    public void displayGrid(String viewName, Map<String, ArrayList<BaseControl>> gridCells, int columns, Boolean showBorders, EventListener listener) {
        System.out.println("SWTApplication: displayGrid: viewName=" + viewName + ", cells=" + gridCells.size());
        
        StyledText textArea = this.tabStyledTextMap.get(viewName);
        Composite composite = this.tabCompositeMap.get(viewName);
        
        // This control is only available when the view does NOT have a text area
        if (textArea != null) {
            System.err.println("SWTApplication: displayGrid: Grids can NOT overlay a text area!");
            return;
        }
                    
        if (columns == 0) {
            double squareRoot = Math.sqrt(gridCells.size());
            columns = (int) Math.ceil(squareRoot);
        }

        System.out.println("SWTApplication: displayGrid: columns=" + columns);
        
        GridLayout gridLayout = new GridLayout(columns, true); // 3 columns, equal width
        composite.setLayout(gridLayout);
        
        for (String cellName : gridCells.keySet()) {
            ArrayList<BaseControl> controls = gridCells.get(cellName);
            
            int gridItemStyle;
            if (showBorders) {
                gridItemStyle = SWT.BORDER;
            } else {
                gridItemStyle = SWT.NONE;
            }
            Composite cellComposite = new Composite(composite, gridItemStyle);

            cellComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
            // TODO - Using the first control's background color is a little cludgy
            Color backgroundColor = null;
            int rgbSum = 0;
            if (!controls.isEmpty()) {
                System.out.println("SWTApplication: displayGrid: Cell count: " + controls.size());
                app.Color genericBackgroundColor = controls.getFirst().backgroundColor;
                if (genericBackgroundColor != null) {
                    backgroundColor = new Color(genericBackgroundColor.red, genericBackgroundColor.green, genericBackgroundColor.blue);
                    rgbSum = genericBackgroundColor.red + genericBackgroundColor.green + genericBackgroundColor.blue;
                }
            } else {
                System.out.println("SWTApplication: displayGrid: Empty cell");
            }
            
            cellComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); // Fill the cell
            cellComposite.setLayout(new GridLayout());
            Color foregroundColor = null;
            if (backgroundColor != null) {
                cellComposite.setBackground(backgroundColor);
                if (rgbSum > 382) {
                    // 255 * 3 = 765 as a maximum value for white.  Use black as the font color if on the lighter half of the color scale.
                    foregroundColor = new Color(0, 0, 0);
                } else {
                    foregroundColor = new Color(255, 255, 255);
                }
            } else {
                cellComposite.setBackground(this.display.getSystemColor(SWT.COLOR_TRANSPARENT)); // Set label background to transparent
            }
            
            // Add zero to many controls to the grid cell
            for (BaseControl abstractControl : controls) {
                System.out.println("SWTApplication: displayGrid: Adding control " + abstractControl.getClass().getName());
                Control control = null;
                if (abstractControl.getClass().equals(app.control.LinkControl.class)) {
                    Link link = new Link(cellComposite, SWT.NONE);
                    link.setText(abstractControl.text);
                    link.setEnabled(abstractControl.isEnabled);
                    if (listener != null) {
                        link.addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                System.out.println("SWTApplication: displayGrid: Link clicked: " + e.text);
                                listener.onEvent(cellName, null);
                            }
                        });
                    }
                    control = link;
                    System.out.println("SWTApplication: displayGrid: Added link " + abstractControl.text + " for " + cellName);
                } else if (abstractControl.getClass().equals(app.control.ButtonControl.class)) {
                    Button button = new Button(cellComposite, SWT.PUSH);
                    button.setFont(this.buttonFont);
                    button.setText(abstractControl.text);
                    button.setEnabled(abstractControl.isEnabled);
                    if (listener != null) {
                        button.addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                System.out.println("SWTApplication: displayGrid: Button clicked: " + e.text);
                                listener.onEvent(cellName, null);
                            }
                        });
                    }
                    control = button;
                    System.out.println("SWTApplication: displayGrid: Added button " + abstractControl.text + " for " + cellName);
                } else if (abstractControl.getClass().equals(app.control.LabelControl.class)) {
                    Label label = new Label(cellComposite, SWT.NONE);
                    label.setText(abstractControl.text);
                    control = label;
                    System.out.println("SWTApplication: displayGrid: Added label " + abstractControl.text + " for " + cellName);
                } else if (abstractControl.getClass().equals(app.control.ImageControl.class)) {
                    Label label = new Label(cellComposite, SWT.NONE);
                    final Image image = loadImage(abstractControl.text);
                    label.setImage(image);
                    control = label;
                    System.out.println("SWTApplication: displayGrid: Added image " + abstractControl.text + " for " + cellName);
                }
                
                if (control != null) {
                    control.setFont(this.monospaceFont);
                    control.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
                    control.setBackground(this.display.getSystemColor(SWT.COLOR_TRANSPARENT));
                    if (backgroundColor != null) {
                        control.setBackground(backgroundColor);
                        control.setForeground(foregroundColor);
                    }
                    System.out.println("SWTApplication: displayGrid: Added " + abstractControl.getClass().getName() + " control " + abstractControl.text + " for " + cellName);
                }
            }
        }

        composite.pack();
    }
    
    public Button newButton(String viewName, String name, String text, double row, double column, Boolean isMonospace, Boolean glow, EventListener listener) {
        System.out.println("SWTApplication: newButton: viewName=" + viewName + ", text=" + text + ", row=" + row + ", column=" + column + ", isMonospace=" + isMonospace + ", glow=" + glow);
        
        StyledText textArea = this.tabStyledTextMap.get(viewName);
        Composite composite = this.tabCompositeMap.get(viewName);
        Button button = new Button(composite, SWT.PUSH);
        int fontWidth = this.fontWidth;
        int fontHeight = this.fontHeight;
        if (isMonospace) {
            button.setFont(this.buttonFont);
            fontWidth = this.buttonFontWidth;
            fontHeight = this.buttonFontHeight;
        }
        button.setText(text);
        Point coordinates = this.convertToCoordinates(row, column);
        int width = (text.length() * fontWidth) + (2 * fontWidth);    // Calculate width of text plus buffer of two imaginary characters
        int height = 2 * fontHeight;   // Calculate double height of text
        button.setBounds(coordinates.x + 1, coordinates.y + 1, width, height);
        System.out.println("SWTApplication: Moved button above text area " + System.identityHashCode(textArea));
        button.moveAbove(textArea);
        
        // Add a special glow effect to the button to call the user's attention to it
        if (glow) {
            Display localDisplay = this.display;
            button.addPaintListener((PaintEvent e) -> {
                GC gc = e.gc;
                Rectangle bounds = ((Button) e.widget).getBounds();
                
                // Draw a rectangle around the button with the current glow color
                int red = 0;
                int blue = 0;
                red = CURRENT_COLOR_INDEX;
                blue = CURRENT_COLOR_INDEX;
                gc.setForeground(new Color(localDisplay, red, 0, blue, 100));
                gc.setLineWidth(1); // Thin border for a glowing effect
                gc.drawRectangle(0, 0, bounds.width - 1, bounds.height - 1);
            });
            
            // Timer for animating the border color
            this.display.timerExec(ANIMATION_DELAY, new Runnable() {
                @Override
                public void run() {
                    if (!button.isDisposed()) {
                        CURRENT_COLOR_INDEX += (10 * direction);
                        if (CURRENT_COLOR_INDEX > 255) {
                            direction = -1;
                            CURRENT_COLOR_INDEX = 255;
                        } else if (CURRENT_COLOR_INDEX < 125) {
                            direction = 1;
                            CURRENT_COLOR_INDEX = 125;
                        }
                        button.redraw(); // Request a repaint to update the border
                        display.timerExec(ANIMATION_DELAY, this); // Schedule the next flash
                    }
                }
            });
        }
        
        return button;
    }
   
    @Override
    public void displayButton(String viewName, String name, String text, int row, int column, Boolean isMonospace, Boolean glow, EventListener listener) {
        System.out.println("SWTApplication: displayButton: viewName=" + viewName + ", text=" + text + ", row=" + row + ", column=" + column + ", isMonospace=" + isMonospace + ", glow=" + glow);
        Button button = this.newButton(viewName, name, text, row, column, isMonospace, glow, listener);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                listener.onEvent(name, null);
            }
        });
    }
    
    @Override
    public void displayOpenFileButton(String viewName, String name, String text, int row, int column, Boolean isMonospace, Boolean glow, EventListener listener) {
        System.out.println("SWTApplication: displayOpenFileButton: viewName=" + viewName + ", text=" + text + ", row=" + row + ", column=" + column + ", isMonospace=" + isMonospace);
        Button button = this.newButton(viewName, name, text, row, column, isMonospace, glow, listener);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(shell, SWT.OPEN);
                // TODO - The default location shouldn't be hardcoded
                dialog.setFilterPath("/home/repp/Documents/quests/");
                String path = dialog.open();
                if (path != null) {
                    listener.onEvent(name, path);
                }
            }
        });
    }
    
    @Override
    public int displayImage(String viewName, String fileName, int row, int column) {
        System.out.println("SWTApplication: displayImage: viewName=" + viewName + ", fileName=" + fileName + ", row=" + row + ", column=" + column);
        StyledText textArea = this.tabStyledTextMap.get(viewName);
        Composite composite = this.tabCompositeMap.get(viewName);
        Label label = new Label(composite, SWT.NONE);
        final Image image = loadImage(fileName);
        Point dimensions = getDimensions(fileName);
        label.setImage(image);
        Point coordinates = this.convertToCoordinates(row, column);
        label.setBounds(coordinates.x + 1, coordinates.y + 1, dimensions.x, dimensions.y);
        label.moveAbove(textArea);
        int nextRow = row + this.getRows(dimensions.y);
        return nextRow;
    }
    
    @Override
    public int displayGif(String viewName, String fileName, int row, int column) {
        System.out.println("SWTApplication: displayGif: viewName=" + viewName + ", fileName=" + fileName + ", row=" + row + ", column=" + column);
        
        StyledText textArea = this.tabStyledTextMap.get(viewName);
        Composite composite = this.tabCompositeMap.get(viewName);
        
        Browser browser = new Browser(composite, SWT.NONE);
        
        URL url = SWTApplication.class.getResource(fileName);        
        if (url != null) {
            System.out.println("SWTApplication: displayGif: Looking for file at " + url.toString());
            browser.setUrl(url.toString());
        } else {
            System.err.println("SWTApplication: displayGif: Resource not found");
            return row;
        }
        
        Point dimensions = getDimensions(fileName);
        Point coordinates = this.convertToCoordinates(row, column);
        browser.setBounds(coordinates.x + 1, coordinates.y + 1, dimensions.x, dimensions.y);
        browser.moveAbove(textArea);
        int nextRow = row + this.getRows(dimensions.y);
        
        return nextRow;
    }
    
    @Override
    public void setTimer(String name, double seconds, EventListener listener) {
        System.out.println("SWTApplication: setTimer: name=" + name + ", seconds=" + seconds + ", listener=" + listener);
        if (TIMER_EVENTS.contains(name)) {
            System.out.println("SWTApplication: setTimer: Timer already exists for " + name + "!");
            return;
        }
        TIMER_EVENTS.add(name);
        this.display.timerExec((int)(seconds * 1000), () -> {
            System.out.println("SWTApplication: setTimer: Timer elapsed: name=" + name + ", seconds=" + seconds + ", listener=" + listener);
            if (!TIMER_EVENTS.contains(name)) {
                System.out.println("SWTApplication: setTimer: Timer " + name + " was removed!");
                return;
            }
            TIMER_EVENTS.remove(name);
            listener.onEvent(name, seconds);
        });
    }
    
    @Override
    public void removeTimer(String name) {
        System.out.println("SWTApplication: removeTimer: name=" + name);
        if (!TIMER_EVENTS.contains(name)) {
            System.out.println("SWTApplication: removeTimer: Timer " + name + " was already removed!");
        } else {
            TIMER_EVENTS.remove(name);
        }
    }
    
    @Override
    public void displayInputField(String viewName, String name, String label, int length, int row, int column, Boolean isMonospace, Boolean isUpperCase, Boolean isMultiUse, EventListener listener) {
        System.out.println("SWTApplication: displayInputField: viewName=" + viewName + ", text=" + label + ", row=" + row + ", column=" + column + ", isMonospace=" + isMonospace + ", isUpperCase=" + isUpperCase + ", isMultiUse=" + isMultiUse + ", listener=" + listener);
        
        StyledText textArea = this.tabStyledTextMap.get(viewName);
        Composite composite = this.tabCompositeMap.get(viewName);
        
        // Display a label for the input field
        //this.displayText(viewName, label, row, column);

        // Display the input field
        Text textInput = new Text(composite, SWT.BORDER);
        if (isUpperCase) {
            // Add a VerifyListener to force uppercase
            textInput.addVerifyListener((VerifyEvent event) -> { event.text = event.text.toUpperCase(); });
        }
        textInput.setMessage(label);
        textInput.setTextLimit(length);
        textInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        Point coordinates = this.convertToCoordinates(row - 0.5, column);
        int labelWidth = label.length() * this.fontWidth;
        int inputWidth = (length * this.fontWidth) + (2 * this.fontWidth);    // Calculate width of text plus buffer of two imaginary characters
        int height = 2 * this.fontHeight;   // Calculate double height of text
        //textInput.setBounds(coordinates.x + 1 + labelWidth + (1 * this.fontWidth), coordinates.y + 1, inputWidth, height);
        textInput.setBounds(coordinates.x + 1, coordinates.y + 1, inputWidth, height);
        textInput.moveAbove(textArea);
        
        // Display a button for submitting the input
        Button button = this.newButton(viewName, name, "Submit", row - 0.5, column + length + 1 + 1, isMonospace, false, listener);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                listener.onEvent(name, textInput.getText());
                if (isMultiUse) {
                    if (!textInput.isDisposed()) {
                        textInput.setText("");
                    }
                } else {
                    if (!textInput.isDisposed()) {
                        textInput.setEnabled(false);
                    }
                    if (!button.isDisposed()) {
                        button.setEnabled(false);
                    }
                }
            }
        });
    }
    
    @Override
    public void displayOverlay(String viewName, String name, app.Color color, Integer startRow, Integer startColumn, Integer endRow, Integer endColumn, Integer transparency) {
        System.out.println("SWTApplication: displayOverlay: viewName=" + viewName + ", name=" + name + ", color=" + color + ", startRow=" + startRow + ", startColumn=" + startColumn + ", endRow=" + endRow + ", endColumn=" + endColumn + ", transparency=" + transparency);
        
        if (this.namedControls.get(viewName).containsKey(name)) {
            System.out.println("SWTApplication: displayOverlay: View already contains a control with this name:" + this.namedControls.get(viewName).get(name));
            return;
        }
        
        StyledText textArea = this.tabStyledTextMap.get(viewName);
        Composite composite = this.tabCompositeMap.get(viewName);
        
        Canvas overlay = new Canvas(composite, SWT.NO_BACKGROUND);
        overlay.setBackground(this.display.getSystemColor(SWT.COLOR_TRANSPARENT));
        if ((startRow == null) || (startRow == 0) || (startColumn == null) || (startColumn == 0) || (endRow == null) || (endRow == 0) || (endColumn == null) || (endColumn == 0)) {
            System.out.println("SWTApplication: displayOverlay: Using composite dimensions");
            overlay.setBounds(0, 0, composite.getSize().x, composite.getSize().y);
        } else {
            System.out.println("SWTApplication: displayOverlay: Using composite dimensions");
            Point topLeftCoordinates = this.convertToCoordinates(startRow, startColumn);
            Point bottomRightCoordinates = this.convertToCoordinates(endRow + 1, endColumn + 1);
            bottomRightCoordinates.x = bottomRightCoordinates.x - 1;
            bottomRightCoordinates.y = bottomRightCoordinates.y - 1;
            int length = bottomRightCoordinates.x - topLeftCoordinates.x;
            int height = bottomRightCoordinates.y - topLeftCoordinates.y;
            overlay.setBounds(topLeftCoordinates.x, topLeftCoordinates.y, length, height);
        }
        if ((transparency == null) || (transparency == 0)) {
            transparency = 128;
        }
        final int finalTransparency = transparency;
        overlay.moveAbove(textArea);
        overlay.addPaintListener(e -> {
            Color overlayColor = new Color(this.display, color.red, color.green, color.blue);
            e.gc.setAlpha(finalTransparency); // Transparency (0-255)
            e.gc.setBackground(overlayColor);
            Rectangle clientArea = overlay.getClientArea();
            e.gc.fillRectangle(clientArea);
            overlayColor.dispose(); // Dispose of the color
        });
        
        this.namedControls.get(viewName).put(name, overlay);
    }
    
    // TODO - Pass in a list of app.Controls with button text and isEnabled
    @Override
    public void displayValidatedInputField(String viewName, String name, List<String> values, int row, int startColumn, int endColumn, int alignment, EventListener listener) {
        System.out.println("SWTApplication: displayValidatedInputField: viewName=" + viewName + ", name=" + name + ", row=" + row + ", startColumn=" + startColumn + ", endColumn=" + endColumn + ", alignment=" + alignment + ", listener=" + listener);
        
        StyledText textArea = this.tabStyledTextMap.get(viewName);
        Composite composite = this.tabCompositeMap.get(viewName);

        // Display a row of buttons with the possible input values
        int buttonHeight = 2 * this.buttonFontHeight;   // Calculate double height of text
        Point coordinates = this.convertToCoordinates(row, startColumn);
        Point terminalCoordinates = this.convertToCoordinates(row, endColumn);
        int buttonX;
        int buttonY;
        switch (alignment) {
            case Alignment.LEFT -> {
                System.out.println("SWTApplication: displayValidatedInputField: Left alignment");
            }
            case Alignment.CENTER -> {
                System.out.println("SWTApplication: displayValidatedInputField: Center alignment");
                // Calculate the full width of the button row
                int rowWidth = 0;
                for (String value : values) {
                    value = value.toUpperCase().replaceFirst("&UP;", "K");
                    value = value.toUpperCase().replaceFirst("&DOWN;", "K");
                    value = value.toUpperCase().replaceFirst("&LEFT;", "K");
                    value = value.toUpperCase().replaceFirst("&RIGHT;", "K");
                    if (value.charAt(0) == '*') {
                        value = value.substring(1, value.length());
                    }
                    int tempButtonWidth = (value.length() * this.buttonFontWidth) + (2 * this.buttonFontWidth);    // Calculate width of text plus buffer of two imaginary characters
                    rowWidth += tempButtonWidth + (1 * this.buttonFontWidth);   // Add a spacer between this button and the next
                }
                coordinates.x = (int) (terminalCoordinates.x - Math.floor(rowWidth / 2));
            }
            case Alignment.RIGHT -> {
                System.out.println("SWTApplication: displayValidatedInputField: Right alignment");
                // Calculate the full width of the button row
                int rowWidth = 0;
                for (String value : values) {
                    value = value.toUpperCase().replaceFirst("&UP;", "K");
                    value = value.toUpperCase().replaceFirst("&DOWN;", "K");
                    value = value.toUpperCase().replaceFirst("&LEFT;", "K");
                    value = value.toUpperCase().replaceFirst("&RIGHT;", "K");
                    if (value.charAt(0) == '*') {
                        value = value.substring(1, value.length());
                    }
                    int tempButtonWidth = (value.length() * this.buttonFontWidth) + (2 * this.buttonFontWidth);    // Calculate width of text plus buffer of two imaginary characters
                    rowWidth += tempButtonWidth + (1 * this.buttonFontWidth);   // Add a spacer between this button and the next
                }
                coordinates.x = terminalCoordinates.x - rowWidth;
            }
            default -> {
                System.err.println("SWTApplication: displayValidatedInputField: Unsupported alignment!");
            }
        }
        buttonX = coordinates.x + 1;
        buttonY = coordinates.y + 1;
        
        for (String value : values) {
            Button button = new Button(composite, SWT.PUSH);
            if (value.charAt(0) == '!') {
                // TODO - This is just a hack to support disabling buttons
                value = value.substring(1, value.length());
                button.setEnabled(false);
            }
            Boolean glow = false;
            if (value.charAt(0) == '*') {
                value = value.substring(1, value.length());
                glow = true;
            }
            Integer keyBinding = null;
            String eventValue = value;
            if (value.toUpperCase().contains("&UP;")) {
                keyBinding = SWT.ARROW_UP;
                eventValue = value.replaceFirst("(?i)" + "&UP;", "");
                value = value.replaceFirst("(?i)" + "&UP;", "\u2B06");  // Case insensitive reg ex
            } else if (value.toUpperCase().contains("&DOWN;")) {
                keyBinding = SWT.ARROW_DOWN;
                eventValue = value.replaceFirst("(?i)" + "&DOWN;", "");
                value = value.replaceFirst("(?i)" + "&DOWN;", "\u2B07");  // Case insensitive reg ex
            } else if (value.toUpperCase().contains("&LEFT;")) {
                keyBinding = SWT.ARROW_LEFT;
                eventValue = value.replaceFirst("(?i)" + "&LEFT;", "");
                value = value.replaceFirst("(?i)" + "&LEFT;", "\u2190");  // Case insensitive reg ex
            } else if (value.toUpperCase().contains("&RIGHT;")) {
                keyBinding = SWT.ARROW_RIGHT;
                eventValue = value.replaceFirst("(?i)" + "&RIGHT;", "");
                value = value.replaceFirst("(?i)" + "&RIGHT;", "\u27A1");  // Case insensitive reg ex
            }
            final String finalValue = eventValue;
            button.setFont(this.buttonFont);
            button.setText(value);
            button.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    ((Button) e.widget).setEnabled(false);
                    listener.onEvent(name, finalValue);
                }
            });
            if (keyBinding != null) {
                // A button can only trap key events when it has focus, so add a key listener to the shell that gets removed when the button is disposed
                final int finalKeyBinding = (int)keyBinding;
                final SWTApplication thisAppController = this;
                final KeyListener textAreaKeyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if ((e.keyCode == finalKeyBinding) && (!button.isDisposed())) {
                            if ((thisAppController.lastProcessedKeyEvent == null) || (thisAppController.lastProcessedKeyEvent.time != e.time) || (thisAppController.lastProcessedKeyEvent.keyCode != e.keyCode)) {
                                e.doit = false; // e hasn't been checked yet and the refreshed page's event handling will process the event in an infinite loop
                                thisAppController.lastProcessedKeyEvent = e; // e.doit isn't processed until this method returns so to further prevent an infinite loop guarantee the key event is new
                                listener.onEvent(name, finalValue);
                            }
                        }
                    }
                };
                textArea.addKeyListener(textAreaKeyListener);
                button.addDisposeListener(e -> {
                    textArea.removeKeyListener(textAreaKeyListener);
                });
            }
            int buttonWidth = (finalValue.length() * this.buttonFontWidth) + (2 * this.buttonFontWidth);    // Calculate width of text plus buffer of two imaginary characters
            if ((buttonX + buttonWidth) > terminalCoordinates.x) {
                // Wrap the button onto a new line
                buttonX = coordinates.x + 1;
                buttonY = (int) (buttonY + buttonHeight + ((1 * this.buttonFontWidth)));
            }
            button.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
            button.moveAbove(textArea);
            buttonX = buttonX + buttonWidth + (1 * this.buttonFontWidth);   // Add a spacer between this button and the next
            
            // TODO - newButton should be used to prevent code duplication
            if (glow) {
                Display localDisplay = this.display;
                button.addPaintListener((PaintEvent e) -> {
                    GC gc = e.gc;
                    Rectangle bounds = ((Button) e.widget).getBounds();

                    // Draw a rectangle around the button with the current glow color
                    int red = 0;
                    int blue = 0;
                    red = CURRENT_COLOR_INDEX;
                    blue = CURRENT_COLOR_INDEX;
                    gc.setForeground(new Color(localDisplay, red, 0, blue, 100));
                    gc.setLineWidth(1); // Thin border for a glowing effect
                    gc.drawRectangle(0, 0, bounds.width - 1, bounds.height - 1);
                });

                // Timer for animating the border color
                this.display.timerExec(ANIMATION_DELAY, new Runnable() {
                    @Override
                    public void run() {
                        if (!button.isDisposed()) {
                            CURRENT_COLOR_INDEX += (10 * direction);
                            if (CURRENT_COLOR_INDEX > 255) {
                                direction = -1;
                                CURRENT_COLOR_INDEX = 255;
                            } else if (CURRENT_COLOR_INDEX < 125) {
                                direction = 1;
                                CURRENT_COLOR_INDEX = 125;
                            }
                            button.redraw(); // Request a repaint to update the border
                            display.timerExec(ANIMATION_DELAY, this); // Schedule the next flash
                        }
                    }
                });
            }
        }
    }
    
    @Override
    public void addDesigner(String viewName) {
        System.out.println("SWTApplication: addDesigner: viewName=" + viewName);
        
        //StyledText textArea = this.tabStyledTextMap.get(viewName);
        Composite composite = this.tabCompositeMap.get(viewName);
        
        //int index = this.tabIndexMap.get(viewName);
        /*
        int index = this.tabIndexMap.size();
        CTabItem tab = new CTabItem(this.tabFolder, SWT.NONE, index);
        tab.setText(viewName);
        Composite composite = new Composite(this.tabFolder, SWT.NONE);
        this.tabCompositeMap.put(viewName, composite);        
        composite.setLayout(null);
        tab.setControl(composite);
        this.tabCompositeMap.put(viewName, composite);
        */
        
        Composite backgroundComposite = composite;
        

        final Image backgroundImage = loadImage("/assets/images/designer.jpg");
        composite.setBackgroundImage(backgroundImage);
        composite.addListener(SWT.Resize, event -> {
            backgroundComposite.setBackgroundImage(backgroundImage);
        });

        //tab.setControl(composite);
        composite.setLayout(new GridLayout(5, false));

        // First Panel
        this.addDragSource("Collections", composite, new String[]{"Scene", "Page"});

        // Second Panel
        org.eclipse.swt.widgets.List sceneList = this.addDropTarget("Quest", composite);
        
        // Third Panel
        this.addDragSource("Elements", composite, new String[]{"<break>", "<choices-add>", "<choices-remove>", "<color>", "<end-page>", "<get-input>", "<get-story-variable>",
            "<goto-chapter>", "<hp-remove>", "<if-game-variable>", "<if-inventory-contains>", "<if-story-variable>", "<image>", "<inventory-add>", "<load-scene>",
            "<monster-shooter>", "<play-sound>", "<player>", "<press-any-key>", "<quote>", "<random>", "<score-add>", "<set-game-variable>", "<set-player-mode>", 
            "<set-story-variable>", "<stop-sound>", "<story-prompt>", "<story-sound>", "<story-variable>", "<turn-page>", "<twin>", "<unbreak>"});
        
        // Fourth Panel
        org.eclipse.swt.widgets.List elementList = this.addDropTarget("Elements", composite);

        // Fifth Panel: Displays Selected Label
        Group displayPanel = new Group(composite, SWT.NONE);
        displayPanel.setBackground(this.display.getSystemColor(SWT.COLOR_WHITE));
        displayPanel.setText("Properties");
        displayPanel.setLayout(new GridLayout());
        Label selectedLabel = new Label(displayPanel, SWT.NONE);
        selectedLabel.setText("Selected Label: None - Select an item from the target panel");

        // Listener for selection in the element list
        elementList.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String selected = elementList.getSelection()[0];
                selectedLabel.setText("Selected Label: " + selected);
                displayPanel.layout(); // Refresh the display panel
                System.out.println(selected);
            }
        });
    }
    
    public void addDragSource(String name, Composite composite, String[] items) {
        Group sourcePanel = new Group(composite, SWT.NONE);
        sourcePanel.setBackground(this.display.getSystemColor(SWT.COLOR_WHITE));
        sourcePanel.setText(name);
        sourcePanel.setLayout(new GridLayout());
        
        ScrolledComposite scrolledComposite = new ScrolledComposite(sourcePanel, SWT.BORDER | SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        
        org.eclipse.swt.widgets.List sourceList = new org.eclipse.swt.widgets.List(scrolledComposite, SWT.BORDER | SWT.V_SCROLL);
        sourceList.setItems(items);
        
        scrolledComposite.setContent(sourceList);
        scrolledComposite.setMinSize(sourceList.computeSize(SWT.DEFAULT, SWT.DEFAULT)); // Set minimum size for scrolling
        
        // Drag Source for the first panel
        DragSource dragSource = new DragSource(sourceList, DND.DROP_MOVE);
        dragSource.setTransfer(new Transfer[]{TextTransfer.getInstance()});
        dragSource.addDragListener(new DragSourceAdapter() {
            @Override
            public void dragSetData(DragSourceEvent event) {
                event.data = sourceList.getSelection()[0];
            }
        });
        dragSource.setDragSourceEffect(new DragSourceEffect(sourceList) {
            @Override
            public void dragStart(DragSourceEvent event) {
                    event.image = display.getSystemImage(SWT.ICON_SEARCH);
            }
	});
    }
    
    public org.eclipse.swt.widgets.List addDropTarget(String name, Composite composite) {
        Group targetPanel = new Group(composite, SWT.NONE);
        targetPanel.setBackground(this.display.getSystemColor(SWT.COLOR_WHITE));
        targetPanel.setText(name);
        targetPanel.setLayout(new GridLayout());
        
        ScrolledComposite scrolledComposite = new ScrolledComposite(targetPanel, SWT.BORDER | SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        
        org.eclipse.swt.widgets.List targetList = new org.eclipse.swt.widgets.List(scrolledComposite, SWT.BORDER | SWT.V_SCROLL);

        scrolledComposite.setContent(targetList);
        scrolledComposite.setMinSize(targetList.computeSize(SWT.DEFAULT, SWT.DEFAULT)); // Set minimum size for scrolling
        
        // Drop Target for the second panel
        DropTarget dropTarget = new DropTarget(targetList, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT);
        dropTarget.setTransfer(new Transfer[]{TextTransfer.getInstance()});
        Shell shell = this.shell;
        Display display = this.display;
        dropTarget.addDropListener(new DropTargetAdapter() {
            @Override
            public void dragEnter(DropTargetEvent event) {
                shell.setCursor(new Cursor(display, SWT.CURSOR_HAND));
            }

            @Override
            public void dragLeave(DropTargetEvent event) {
                System.out.println("dragLeave");
                shell.setCursor(null); // Reset cursor when drag leaves
            }
            
            @Override
            public void drop(DropTargetEvent event) {
                if (event.data instanceof String) {
                    String label = (String) event.data;

                    // Calculate the index based on event.y
                    // TODO - Need to factor in if there is scrolling in effect for the list
                    // getTopIndex() will return the index of the top visible item
                    // The current calculation seems jenky
                    int itemHeight = targetList.getItemHeight();
                    Point targetListLocation = targetList.getLocation();
                    int targetListLocationOnScreen = targetList.toDisplay(targetListLocation).y;
                    int index = ((event.y - targetListLocationOnScreen) - (targetList.getTopIndex() * itemHeight)) / itemHeight;

                    System.out.println("SWTApplication: addDesigner: item height=" + itemHeight + ", event.y=" + event.y + ", index=" + index + ", targetListLocation=" + targetListLocationOnScreen);

                    // Ensure the index is within bounds
                    if (index > targetList.getItemCount()) {
                        index = targetList.getItemCount();
                    }

                    targetList.add(label, index); // Add the label at the calculated index
                }
            }
        });
        
        return targetList;
    }
    
}