package app;

import static app.controller.BaseController.logger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author repp
 */
public class Font {
    
    public static final String LATO = "Lato";
    public static final String MINECRAFT = "Minecraft";
    public static final String ROBOTO = "Roboto";
    public static final String ROBOTO_BLACK = "Roboto Black";
    public static final String ROBOTO_LIGHT = "Roboto Light";
    public static final String ROBOTO_MEDIUM = "Roboto Medium";
    public static final String ROBOTO_MONO = "Roboto Mono";
    public static final String ROBOTO_MONO_LIGHT = "Roboto Mono Light";
    public static final String ROBOTO_MONO_MEDIUM = "Roboto Mono Medium";
    public static final String ROBOTO_MONO_THIN = "Roboto Mono Thin";
    public static final String ROBOTO_THIN = "Roboto Thin";
    
    public static List<String> getFontFiles(String fontFamily) {
        List<String> fontFiles = new ArrayList();
        switch (fontFamily) {
            case "Lato" -> {
                fontFiles.add("/assets/fonts/Lato-Bold.ttf");
                fontFiles.add("/assets/fonts/Lato-BoldItalic.ttf");
                fontFiles.add("/assets/fonts/Lato-Italic.ttf");
                fontFiles.add("/assets/fonts/Lato-Regular.ttf");
            }
            case "Minecraft" -> fontFiles.add("/assets/fonts/Minecraft.ttf");
            case "Roboto" -> {
                fontFiles.add("/assets/fonts/Roboto-Bold.ttf");
                fontFiles.add("/assets/fonts/Roboto-BoldItalic.ttf");
                fontFiles.add("/assets/fonts/Roboto-Italic.ttf");
                fontFiles.add("/assets/fonts/Roboto-Regular.ttf");
            }
            case "Roboto Black" -> {
                fontFiles.add("/assets/fonts/Roboto-Black.ttf");
                fontFiles.add("/assets/fonts/Roboto-BlackItalic.ttf");
            }
            case "Roboto Light" -> {
                fontFiles.add("/assets/fonts/Roboto-Light.ttf");
                fontFiles.add("/assets/fonts/Roboto-LightItalic.ttf");
            }
            case "Roboto Medium" -> {
                fontFiles.add("/assets/fonts/Roboto-Medium.ttf");
                fontFiles.add("/assets/fonts/Roboto-MediumItalic.ttf");
            }
            case "Roboto Mono" -> {
                fontFiles.add("/assets/fonts/RobotoMono-Bold.ttf");
                fontFiles.add("/assets/fonts/RobotoMono-BoldItalic.ttf");
                fontFiles.add("/assets/fonts/RobotoMono-Italic.ttf");
                fontFiles.add("/assets/fonts/RobotoMono-Regular.ttf");
            }
            case "Roboto Mono Light" -> {
                fontFiles.add("/assets/fonts/RobotoMono-Light.ttf");
                fontFiles.add("/assets/fonts/RobotoMono-LightItalic.ttf");
            }
            case "Roboto Mono Medium" -> {
                fontFiles.add("/assets/fonts/RobotoMono-Medium.ttf");
                fontFiles.add("/assets/fonts/RobotoMono-MediumItalic.ttf");
            }
            case "Roboto Mono Thin" -> {
                fontFiles.add("/assets/fonts/RobotoMono-Thin.ttf");
                fontFiles.add("/assets/fonts/RobotoMono-ThinItalic.ttf");
            }
            case "Roboto Thin" -> {
                fontFiles.add("/assets/fonts/Roboto-Thin.ttf");
                fontFiles.add("/assets/fonts/Roboto-ThinItalic.ttf");
            }
            default -> logger.log(Level.WARNING, "Unknown font family", fontFamily);
        }
        return fontFiles;
    }
    
}
