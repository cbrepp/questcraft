package app;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class SWTApplication extends ApplicationController {
    
    public Display display;
    public ApplicationView parentView;
    public Shell shell;
    public CTabFolder tabFolder;
    public CTabItem tab;
    public Composite currentComposite;
    public StyledText currentTextArea;
    public List<StyleRange> currentStyleRanges;
    public String emptyBook;
    public int tabFolderHeight = 0;
    public LinkedHashMap<String, StyledText> tabMap;
    public LinkedHashMap<String, Composite> tabCompositeMap;
    public LinkedHashMap<String, List<StyleRange>> tabStyleRangesMap;
    public LinkedHashMap<ApplicationView, Integer> tabViewMap;
    public int fontHeight = 0;
    public int fontWidth = 0;
    public int textColumns = 0;
    public int textRows = 0;
    
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
    public void initialize(ApplicationView view) {
        this.display = new Display();
        this.parentView = view;
    }
    
    @Override
    public void close() {
        this.display.dispose();
    }

    @Override
    public void displaySplash() {
        // Initialize the splash screen
        this.shell = new Shell(this.display, SWT.NO_TRIM | SWT.ON_TOP);
        this.currentTextArea = new StyledText(shell, SWT.NONE);
        int backgroundColor;
        if (this.parentView.backgroundColor != null) {
            backgroundColor = this.parentView.backgroundColor;
        } else {
            backgroundColor = SWT.COLOR_BLACK;
        }
        Point dimensions = getDimensions(parentView.splashImageFileName);
        this.decorateShell(dimensions, backgroundColor, this.parentView.splashImageFileName);
        
        StyleRange redStyle = new StyleRange();
        redStyle.start = 0;
        redStyle.length = 17;
        redStyle.foreground = this.display.getSystemColor(SWT.COLOR_RED);
        this.currentTextArea.setStyleRange(redStyle);
        
        displayText("Wayne Chung Enterprises", 7, 2);
        displayText("presents", 9, 9);
        
        this.shell.open();
        
        String splashSoundFileName = this.parentView.splashSoundFileName;
        int splashSeconds = this.parentView.splashSeconds;

        // Create a separate thread to handle the delay and close the splash screen
        Shell shell = this.shell;
        Thread splashThread;
        splashThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Play splash sound
                if (splashSoundFileName != null) {
                    Utility.playSound(splashSoundFileName, false);
                }
                
                long milliseconds = splashSeconds * 1000;
                try {
                    Thread.sleep(milliseconds);
                } catch (InterruptedException e) {
                    Logger.getLogger(SWTApplication.class.getName()).log(Level.SEVERE, null, e);
                }

                // Close the splash screen after the delay
                SWTApplication.this.display.syncExec(() -> {
                    shell.close();
                });
            }
        });
        splashThread.start();

        // Run the event loop until the splash shell is closed
        while (!shell.isDisposed()) {
            if (!this.display.readAndDispatch()) {
                this.display.sleep();
            }
        }
        
        if (!shell.isDisposed()) {
            this.shell.dispose();     // Dispose of the shell
        }
        if (!this.currentTextArea.isDisposed()) {
            if (!this.currentTextArea.getBackgroundImage().isDisposed()) {
                this.currentTextArea.getBackgroundImage().dispose();     // Dispose of the image
            }
        }
        if (splashSoundFileName != null) {
            Utility.stopSound(splashSoundFileName);
        }
    }
    
    public void decorateView(ApplicationView view) {
        System.out.println("SWTApplication: decorateView: Decorating application view: " + view + ", class=" + view.getClass().toString());
        
        if (view == null) {
            System.out.println("SWTApplication: decorateView: View is missing!");
            return;
        }
        
        Integer imageColor = null;
        String appImageFile = this.parentView.backgroundImage;
        String imageFile = null;
        
        // Evaluate background
        System.out.println("SWTApplication: decorateView: Retrieved view");

        // Defer to using the primary child's background color and image
        imageColor = view.backgroundColor;
        if (imageColor == null) {
            imageColor = this.parentView.backgroundColor;
        }
        imageFile = view.backgroundImage;   // It's okay for this to be null
        System.out.println("SWTApplication: decorateView: View background image: " + imageFile);

        // Use the primary child's background image for the application if not set
        if (appImageFile == null) {
            appImageFile = view.backgroundImage;
        }

        if (imageColor == null) {
            imageColor = SWT.COLOR_BLACK;
        }
		
        // Load the application's image
        System.out.println("SWTApplication: decorateView: Determined image: " + appImageFile + " and color " + imageColor);
        
        // Decorate the application shell
        Point dimensions = getDimensions(appImageFile);
        //this.currentTextArea = new StyledText(this.shell, SWT.BORDER);
        if (view.getClass().toString().equals("class quest.Library")) {   // TODO - Hack!
            this.decorateShell(dimensions, imageColor, imageFile);
        }
        
        // Open the shell if it has never been opened
        if (!this.shell.isVisible()) {
            System.out.println("SWTApplication: decorateView: Opening shell for first time");
            this.shell.open();
        } else {
            //this.shell.redraw(0, 0, dimensions.x, dimensions.y, true);
            this.shell.update();
            this.shell.pack();
        }
    }
    
    @Override
    public void displayView(String name) {
        System.out.println("SWTApplication: displayView: Displaying application view: " + name);
        
        ApplicationView view = this.parentView.getChildren().get(name);
        view.name = name;
        
        this.displayView(view);
    }

    @Override
    public void displayView(ApplicationView view) {
        System.out.println("SWTApplication: displayView: Displaying application view: " + view);
        // TODO - view may not have been initialized yet
        int tabIndex = this.tabViewMap.get(view);
        System.out.println("SWTApplication: displayView: Tab index=" + tabIndex + ", Tab view name=" + view.name);
        this.tabFolder.setSelection(tabIndex);
        this.currentTextArea = this.tabMap.get(view.name);
        this.currentComposite = this.tabCompositeMap.get(view.name);
        this.currentStyleRanges = this.tabStyleRangesMap.get(view.name);
        System.out.println("SWTApplication: displayView: Text length = " + this.currentTextArea.getText().length() + ", object=" + Objects.toIdentityString(this.currentTextArea));
        view.onDisplay(this, this.parentView);
    }

    /*
    TODO:
    Shell has a Composite borderComposite = new Composite(shell, SWT.BORDER);
        borderComposite.setLayout(new FillLayout());
    Shell also has a TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
    Each tabItem uses a composite as its control
    The textArea is added to the composite
    */
    @Override
    public void displayApplication() {
        System.out.println("SWTApplication: displayApplication");

        // Initialize the application window
        this.shell = new Shell(this.display);
        shell.setLayout(new FillLayout());
        this.shell.setText(this.parentView.name);
        
        // Set the application icon
        final Image iconImage = loadImage(this.parentView.iconFileName);
        this.shell.setImage(iconImage);
        
        // Create a Composite with a border
        Composite borderComposite = new Composite(this.shell, SWT.BORDER);
        borderComposite.setLayout(new FillLayout());
        borderComposite.setBounds(0,0,this.shell.getBounds().width,this.shell.getBounds().height);

        // Create a TabFolder
        this.tabFolder = new CTabFolder(borderComposite, SWT.NONE);
        final CTabFolder tf = this.tabFolder;
        this.tabFolder.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CTabItem item = tf.getSelection();
                System.out.println("SWTApplication: displayApplication: Selected tab " + item.getText());
            }
        });
        //borderComposite.setBounds(0,0,borderComposite.getBounds().width,borderComposite.getBounds().height);
        //this.tabFolder.moveAbove(borderComposite);
        this.tabMap = new LinkedHashMap<>();
        this.tabCompositeMap = new LinkedHashMap<>();
        this.tabStyleRangesMap = new LinkedHashMap<>();
        this.tabViewMap = new LinkedHashMap<>();
        
        String appImageFile = this.parentView.backgroundImage;
        Point dimensions = SWTApplication.getDimensions(appImageFile);
        this.shell.setSize(dimensions);
        
        int preTabFolderHeight = this.shell.getBounds().height;

        // Parse the application view tree and build the application menu
        String primaryChildName = null;
        
        LinkedHashMap<String, ApplicationView> children = this.parentView.getChildren();
        for (Map.Entry<String, ApplicationView> child : children.entrySet()) {
            String childName = child.getKey();
            ApplicationView childView = child.getValue();
            
            if (childView == null) {
                // If the child view hasn't been instanced yet, add it later
                continue;
            }
            
            // Set the primary child
            if (primaryChildName == null) {
                primaryChildName = childName;
            }
            
            this.addView(childName, childView);
        }
        
        // Open the shell if it has never been opened
        if (!this.shell.isVisible()) {
            System.out.println("SWTApplication: decorateView: Opening shell for first time");
            this.shell.open();
        } else {
            //this.shell.redraw(0, 0, dimensions.x, dimensions.y, true);
            this.shell.update();
            this.shell.pack();
        }
        
        this.shell.pack();
        int postTabFolderHeight = this.shell.getBounds().height;
        this.tabFolderHeight = postTabFolderHeight - preTabFolderHeight;
        
        this.displayView(primaryChildName);
        
        System.out.println("Post displayView: height=" + this.shell.getBounds().height + ", width=" + this.shell.getBounds().width);
        
        while (!shell.isDisposed ()) {
            if (!this.display.readAndDispatch ()) {
                this.display.sleep ();
            }
        }
        
        iconImage.dispose();
        
        if (!shell.isDisposed()) {
            this.cleanShell();
            this.shell.dispose();
        }
        
        Utility.stopAllSounds();
    }
    
    @Override
    public void addView(String childName, ApplicationView childView) {
        Integer index = this.tabViewMap.get(childView);
        if (index == null) {
            index = this.tabViewMap.size();
        }
        this.addView(childName, childView, index);
    }
    
    public void addView(String childName, ApplicationView childView, int index) {
        System.out.println("SWTApplication: addView: " + childName);
        
        this.tab = new CTabItem(this.tabFolder, SWT.NONE, index);
        this.tab.setText(Utility.toPascalCase(childName));

        // Create a Composite for the first TabItem
        this.currentComposite = new Composite(this.tabFolder, SWT.NONE);
        this.currentComposite.setLayout(null);  // We intentionally do NOT 
        //composite.moveAbove(borderComposite);
        this.tab.setControl(this.currentComposite);

        this.currentTextArea = new StyledText(this.currentComposite, SWT.BORDER);
        String appImageFile = this.parentView.backgroundImage;
        Point dimensions = SWTApplication.getDimensions(appImageFile);
        this.currentTextArea.setBounds(0, 0, dimensions.x, dimensions.y);
        this.currentTextArea.moveAbove(this.currentComposite);
        System.out.println("SWTApplication: addView: " + childName + " text area is " + System.identityHashCode(this.currentTextArea));

        System.out.println("SWTApplication: addView : Setting text area for " + childName);
        this.tabMap.put(childName, this.currentTextArea);
        this.tabCompositeMap.put(childName, this.currentComposite);

        Integer imageColor = null;
        String imageFile = null;

        // Evaluate background
        System.out.println("SWTApplication: addView: Retrieved view");

        // Defer to using the primary child's background color and image
        imageColor = childView.backgroundColor;
        if (imageColor == null) {
            imageColor = this.parentView.backgroundColor;
        }
        imageFile = childView.backgroundImage;   // It's okay for this to be null
        System.out.println("SWTApplication: addView: View background image: " + imageFile);

        // Use the primary child's background image for the application if not set
        if (appImageFile == null) {
            appImageFile = childView.backgroundImage;
        }

        if (imageColor == null) {
            imageColor = SWT.COLOR_BLACK;
        }

        // Load the application's image
        System.out.println("SWTApplication: addView: Determined image: " + appImageFile + " and color " + imageColor);

        // Decorate the application shell

        System.out.println("SWTApplication: addView: backgroundColor=" + imageColor + ", backgroundImageFile=" + imageFile + ",  x=" + dimensions.x +", y=" + dimensions.y);

        this.currentStyleRanges = new ArrayList<>();
        this.tabStyleRangesMap.put(childName, this.currentStyleRanges);

        // Initialize the shell.
        // Not using a layout allows controls to overlay.  This is needed so controls can display on top of the text area.
        //this.shell.setLayout(null);

        // The text area should be read-only by default
        //     with specified bounds and a location because the shell has no layout
        //     and a background color and image if provided
        //     and a monospaced font
        this.currentTextArea.setEditable(false);
        this.setBackground(imageColor, imageFile);
        Font monospaceFont = new Font(this.display, "Consolas", 12, SWT.NORMAL);
        this.currentTextArea.setFont(monospaceFont);

        // Calculate the height and width of the font
        GC gc = new GC(this.currentTextArea);
        gc.setFont(monospaceFont);
        Point extent = gc.stringExtent("W");
        this.fontHeight = extent.y;
        this.fontWidth = extent.x;
        System.out.println("SWTApplication: addView: fontWidth=" + this.fontWidth + ", fontHeight=" + this.fontHeight);

        // Prepare a terminal text area
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
        this.currentTextArea.setText(this.emptyBook);

        System.out.println("SWTApplication: addView : Text area length for " + childName + " is " + this.currentTextArea.getText().length() + ", object=" + Objects.toIdentityString(this.currentTextArea));

        this.tabViewMap.put(childView, this.tabViewMap.size());
        
        childView.onLoad(this, this.parentView);
    }
    
    public void decorateShell(Point dimensions, int backgroundColor, String backgroundImageFile) {
        System.out.println("SWTApplication: decorateShell: backgroundColor=" + backgroundColor + ", backgroundImageFile=" + backgroundImageFile + ",  x=" + dimensions.x +", y=" + dimensions.y);

        this.currentStyleRanges = new ArrayList<>();

        // Initialize the shell.
        // Not using a layout allows controls to overlay.  This is needed so controls can display on top of the text area.
        this.shell.setLayout(null);
        this.shell.setSize(dimensions);
        
        // The text area should be read-only by default
        //     with specified bounds and a location because the shell has no layout
        //     and a background color and image if provided
        //     and a monospaced font
        this.currentTextArea.setEditable(false);
        this.currentTextArea.setBounds(0, 0, dimensions.x, dimensions.y);
        this.currentTextArea.setLocation(0, 0);
        this.setBackground(backgroundColor, backgroundImageFile);
        Font monospaceFont = new Font(this.display, "Consolas", 12, SWT.NORMAL);
        this.currentTextArea.setFont(monospaceFont);
        
        // Calculate the height and width of the font
        GC gc = new GC(this.currentTextArea);
        gc.setFont(monospaceFont);
        Point extent = gc.stringExtent("W");
        this.fontHeight = extent.y;
        this.fontWidth = extent.x;
        System.out.println("SWTApplication: decorateShell: fontWidth=" + this.fontWidth + ", fontHeight=" + this.fontHeight);

        // Prepare a terminal text area
        this.textColumns = this.getColumns(dimensions.x) - 1; // Subtract by 1 to prevent rounding from exceeding the available space
        this.textRows = this.getRows(dimensions.y) - 1; // Subtract by 1 to prevent rounding from exceeding the available space
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < textRows; j++) {
        	for (int i = 0; i < textColumns; i++) {
        		sb.append(' ');
        	}
        	sb.append('\n');
        }
        String emptyBook = sb.toString();
        this.currentTextArea.setText(emptyBook);
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
    
    public void setBackground(int backgroundColor, String backgroundImageFile) {
        System.out.println("SWTApplication: setBackgroundImage: Background color: " + backgroundColor + ", background image: " + backgroundImageFile);
        this.currentTextArea.setBackground(this.display.getSystemColor(backgroundColor));
        
        if (backgroundImageFile != null) {
            final Image backgroundImage = loadImage(backgroundImageFile);
            this.currentTextArea.setBackgroundImage(backgroundImage);
            this.currentTextArea.addListener(SWT.Resize, event -> {
                this.setBackground(backgroundColor, backgroundImageFile);
            });
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
    
    public Point convertToCoordinates(int row, int column) {
        System.out.println("SWTApplication: convertToCoordinates: row=" + row + ", column=" + column);
        int x = (column * this.fontWidth) - this.fontWidth;
        int y = (row * this.fontHeight) - this.fontHeight;
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
    public void clearScreen() {
        System.out.println("SWTApplication: clearScreen");            
        Control[] controls = this.currentComposite.getChildren();
        for (Control control : controls) {
            System.out.println("SWTApplication: clearScreen: Disposing " + control);
            if (control.getBackgroundImage() != null) {
                control.getBackgroundImage().dispose();
            }
            if (!control.getClass().toString().equals("class org.eclipse.swt.custom.StyledText")) {
                System.out.println("SWTApplication: class is " + control.getClass().toString());
                control.dispose();
            }
        }
        this.currentTextArea.setText(this.emptyBook);
        this.currentStyleRanges.clear();
    }
    
    @Override
    public void displayMessageBox(String text) {
        MessageBox messageBox = new MessageBox(this.shell, SWT.ICON_INFORMATION | SWT.OK);
        messageBox.setText(this.parentView.name);
        messageBox.setMessage(text);
        messageBox.open();
    }
    
    @Override
    public void displayText(String text, Integer row, Integer column, app.Color color) {
        System.out.println("SWTApplication: displayText: text=" + text + ", row=" + row + ", column=" + column + ", color=" + color);
        Color textColor = new Color(color.red, color.green, color.blue);
        displayText(text, row, column, textColor);
    }
    
    @Override
    public void displayText(String text, Integer row, Integer column, int color) {
        System.out.println("SWTApplication: displayText: text=" + text + ", row=" + row + ", column=" + column + ", color=" + color);
        Color textColor = this.currentTextArea.getDisplay().getSystemColor(color);
        displayText(text, row, column, textColor);
    }
    
    public void displayText(String text, Integer row, Integer column, Color color) {
        System.out.println("SWTApplication: displayText: text=" + text + ", row=" + row + ", column=" + column + ", color=" + color);
        
        String currentText = this.currentTextArea.getText();
        Integer position = column - 1; // String positions start at zero
        position = position + (this.textColumns * (row - 1)) + (row - 1);
        System.out.println("SWTApplication: displayText: this.textColumns=" + this.textColumns + ", position=" + position);
        StringBuilder sb = new StringBuilder(currentText);
        sb.replace(position, position + text.length(), text);
        this.currentTextArea.setText(sb.toString());
        StyleRange textRange = new StyleRange();
        textRange.start = position;
        textRange.length = text.length();
        textRange.foreground = color;
        if (!color.equals(SWT.COLOR_BLACK)) {
            // Skip styling if the color is black
            this.currentStyleRanges.add(textRange);
        }
        // Thanks to a limitation with SWT, each previous style range needs to be reapplied
        for (StyleRange range : this.currentStyleRanges) {
            this.currentTextArea.setStyleRange(range);
        }
        this.currentTextArea.redraw();
    }

    @Override
    public void displayText(String text, Integer row, Integer column) {
        displayText(text, row, column, SWT.COLOR_BLACK);
    }
    
    public Button newButton(String name, String text, int row, int column, ApplicationView listener) {
        System.out.println("SWTApplication: newButton: text=" + text + ", row=" + row + ", column=" + column);
        
        Button button = new Button(this.currentComposite, SWT.PUSH);
        button.setText(text);
        Point coordinates = this.convertToCoordinates(row, column);
        int width = (text.length() * this.fontWidth) + (2 * this.fontWidth);    // Calculate width of text plus buffer of two imaginary characters
        int height = 2 * this.fontHeight;   // Calculate double height of text
        button.setBounds(coordinates.x + 1, coordinates.y + 1, width, height);
        System.out.println("SWTApplication: Moved button above text area " + System.identityHashCode(this.currentTextArea));
        button.moveAbove(this.currentTextArea);
        
        return button;
    }
   
    @Override
    public void displayButton(String name, String text, int row, int column, ApplicationView listener) {
        System.out.println("SWTApplication: displayButton: text=" + text + ", row=" + row + ", column=" + column);
        Button button = this.newButton(name, text, row, column, listener);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                listener.handleEvent(name, null);
            }
        });
    }
    
    @Override
    public void displayOpenFileButton(String name, String text, int row, int column, ApplicationView listener) {
        System.out.println("SWTApplication: displayOpenFileButton: text=" + text + ", row=" + row + ", column=" + column);
        Button button = this.newButton(name, text, row, column, listener);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(shell, SWT.OPEN);
                // TODO - The default location shouldn't be hardcoded
                dialog.setFilterPath("/home/repp/Documents/quests/");
                String path = dialog.open();
                if (path != null) {
                    listener.handleEvent(name, path);
                }
            }
        });
    }
    
    @Override
    public int displayImage(String fileName, int row, int column) {
        System.out.println("SWTApplication: displayImage: fileName=" + fileName + ", row=" + row + ", column=" + column);
        Label label = new Label(this.currentComposite, SWT.BORDER);
        final Image image = loadImage(fileName);
        Point dimensions = getDimensions(fileName);
        label.setImage(image);
        Point coordinates = this.convertToCoordinates(row, column);
        label.setBounds(coordinates.x + 1, coordinates.y + 1, dimensions.x, dimensions.y);
        label.moveAbove(this.currentTextArea);
        int nextRow = row + this.getRows(dimensions.y);
        return nextRow;
    }
    
    // TODO - This is broken in the Linux implementation of SWT (https://bugs.eclipse.org/bugs/show_bug.cgi?id=560298)
    public void displayGifDONOTUSE(String fileName, int row, int column) {
        System.out.println("SWTApplication: displayAnimation: fileName=" + fileName + ", row=" + row + ", column=" + column);
        int x = column * (768 / this.textRows);
        int y = row * (1024 / this.textColumns);
        ImageLoader loader = new ImageLoader();
        loader.load(getClass().getResourceAsStream(fileName));
        Canvas canvas = new Canvas(this.currentComposite,SWT.NONE);
        Image image = new Image(this.display,loader.data[0]);
        Point dimensions = getDimensions(fileName);
        canvas.setBounds(x, y, dimensions.x, dimensions.y);
        canvas.moveAbove(this.currentTextArea);
        
        Display display = this.display;
        StyledText currentTextArea = this.currentTextArea;
        
        final GC gc = new GC(image);
        canvas.addPaintListener((PaintEvent event) -> {
            event.gc.drawImage(image,0,0);
        });
        
        Thread thread = new Thread(){
            int imageNumber = 0;
            @Override
            public void run(){
                System.out.println("SWTApplication: displayAnimation: Animating");
                while (imageNumber < loader.data.length-1) {
                    long currentTime = System.currentTimeMillis();
                    int delayTime = loader.data[imageNumber].delayTime;
                    try {
                        //while(currentTime + delayTime * 10 > System.currentTimeMillis()){
                        // Wait till the delay time has passed
                        //}
                        Thread.sleep(loader.data[imageNumber].delayTime);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SWTApplication.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    display.syncExec(() -> {
                        // Increase the variable holding the frame number
                        imageNumber = imageNumber == loader.data.length-1 ? 0 : imageNumber+1;
                        // Draw the new data onto the image
                        ImageData nextFrameData = loader.data[imageNumber];
                        Image frameImage = new Image(display,nextFrameData);
                        gc.drawImage(frameImage,nextFrameData.x,nextFrameData.y);
                        frameImage.dispose();
                        //canvas.setBounds(x, y, dimensions.x, dimensions.y);
                        //canvas.moveAbove(textArea);
                        canvas.redraw();
                    });
                }
                System.out.println("SWTApplication: displayAnimation: Animation complete");
            }
        };
        thread.start();
    }
    
    @Override
    public int displayGif(String fileName, int row, int column) {
        System.out.println("SWTApplication: displayGif: fileName=" + fileName + ", row=" + row + ", column=" + column);
        
        Browser browser = new Browser(this.currentComposite, SWT.NONE);
        
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
        browser.moveAbove(this.currentTextArea);
        int nextRow = row + this.getRows(dimensions.y);
        
        return nextRow;
    }
    
    @Override
    public void setTimer(String name, int seconds, ApplicationView listener) {
        System.out.println("SWTApplication: setTimer: name=" + name + ", seconds=" + seconds + ", listener=" + listener);
        this.display.timerExec(seconds * 1000, () -> {
            listener.handleEvent(name, null);
        });
    }
    
    @Override
    public void addDesigner() {
        System.out.println("SWTApplication: addDesigner");
        Composite composite = new Composite(this.tabFolder, SWT.NONE);

        final Image backgroundImage = loadImage("/assets/images/designer.jpg");
        composite.setBackgroundImage(backgroundImage);
        composite.addListener(SWT.Resize, event -> {
            composite.setBackgroundImage(backgroundImage);
        });

        this.tab.setControl(composite);
        composite.setLayout(new GridLayout(3, true));

        // First Panel: Source of Labels
        Group sourcePanel = new Group(composite, SWT.NONE);
        sourcePanel.setBackground(this.display.getSystemColor(SWT.COLOR_WHITE));
        sourcePanel.setText("Story Elements");
        sourcePanel.setLayout(new GridLayout());
        org.eclipse.swt.widgets.List sourceList = new org.eclipse.swt.widgets.List(sourcePanel, SWT.BORDER | SWT.V_SCROLL);
        sourceList.setItems(new String[]{"Label 1", "Label 2", "Label 3", "Label 4"});
        sourceList.setBounds(10, 10, 100, 200);
        
        // Drag Source for the first panel
        DragSource dragSource = new DragSource(sourceList, DND.DROP_MOVE);
        dragSource.setTransfer(new Transfer[]{TextTransfer.getInstance()});
        dragSource.addDragListener(new DragSourceAdapter() {
            @Override
            public void dragSetData(DragSourceEvent event) {
                event.data = sourceList.getSelection()[0];
            }
        });

        // Second Panel: Target for Dropped Labels
        Group targetPanel = new Group(composite, SWT.NONE);
        targetPanel.setBackground(this.display.getSystemColor(SWT.COLOR_WHITE));
        targetPanel.setText("Quest");
        targetPanel.setLayout(new GridLayout());
        org.eclipse.swt.widgets.List targetList = new org.eclipse.swt.widgets.List(targetPanel, SWT.BORDER | SWT.V_SCROLL);
        targetList.setBounds(10, 10, 100, 200);
        
        // Drop Target for the second panel
        DropTarget dropTarget = new DropTarget(targetList, DND.DROP_MOVE);
        dropTarget.setTransfer(new Transfer[]{TextTransfer.getInstance()});
        dropTarget.addDropListener(new DropTargetAdapter() {
            @Override
            public void drop(DropTargetEvent event) {
                if (event.data instanceof String) {
                    String label = (String) event.data;

                    // Calculate the index based on event.y
                    int itemHeight = targetList.getItemHeight();
                    int index = event.y / itemHeight;

                    // Ensure the index is within bounds
                    if (index > targetList.getItemCount()) {
                        index = targetList.getItemCount();
                    }

                    targetList.add(label, index); // Add the label at the calculated index
                }
            }
        });

        // Third Panel: Displays Selected Label
        Group displayPanel = new Group(composite, SWT.NONE);
        displayPanel.setBackground(this.display.getSystemColor(SWT.COLOR_WHITE));
        displayPanel.setText("Properties");
        displayPanel.setLayout(new GridLayout());
        Label selectedLabel = new Label(displayPanel, SWT.NONE);
        selectedLabel.setText("Selected Label: None - Select an item from the target panel");

        // Listener for selection in the target list
        targetList.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String selected = targetList.getSelection()[0];
                selectedLabel.setText("Selected Label: " + selected);
                displayPanel.layout(); // Refresh the display panel
                System.out.println(selected);
            }
        });
    }
    
}