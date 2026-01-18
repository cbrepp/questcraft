package app;

import static app.controller.BaseController.logger;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author repp
 */
public class Changelog {
    
    public static String DEFAULT_LOCATION = "CHANGELOG.md";
    public static String UNRELEASED_VERSION = "Unreleased";
    
    public Changelog() {}

    public static String findFirstReleasedVersion(String changelog) {
        logger.log(Level.FINE, "Entered");
        
        // Find the first line that starts with "##" and a space, with special handling for the unreleased version
        String version = null;
        Boolean isUnreleased = false;
        Pattern pattern = Pattern.compile("(?m)^##\\s+(.*)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(changelog);
        while (matcher.find()) {
            String rawLine = matcher.group(0);
            String tempVersion = rawLine.replaceFirst("^##\\s+", "");
            if (tempVersion.contains(UNRELEASED_VERSION)) {
                logger.log(Level.FINE, "Found version: {0}", UNRELEASED_VERSION);
                isUnreleased = true;
            } else {
                if (Character.isDigit(tempVersion.charAt(0))) {
                    version = "v" + tempVersion.trim();
                } else {
                    version = tempVersion.trim();
                }
                logger.log(Level.FINE, "Found version: {0}", version);
                break;
            }
        }
        if (isUnreleased) {
            if (version == null) {
                version = UNRELEASED_VERSION;
            } else {
                version += " with " + UNRELEASED_VERSION + " changes";
            }
        }
        
        if (version != null) {
            logger.log(Level.FINE, "Found first released version: {0}", version);
        } else {
            logger.log(Level.FINE, "Did NOT find first released version");
        }
        
        return version;
    }
    
    public static String get() {
        logger.log(Level.FINE, "Entered");
        
        String changelog = null;
        try (InputStream is = Changelog.class.getClassLoader().getResourceAsStream(DEFAULT_LOCATION)) {
            if (is == null) {
                logger.log(Level.WARNING, "Failed to read location: {0}", DEFAULT_LOCATION);
                return changelog;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                changelog = reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "A critical error occurred: {0}", e);
        }
        
        if (changelog != null) {
            logger.log(Level.FINE, "Read changelog");
            logger.log(Level.FINER, "contents={0}", changelog);
        } else {
            logger.log(Level.FINE, "Did NOT read changelog");
        }
        
        return changelog;
    }
    
}
