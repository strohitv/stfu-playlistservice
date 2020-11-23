package tv.strohi.stfu.playlistservice.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

public class RootPathLoader {
    private static final Logger logger = LogManager.getLogger(RootPathLoader.class.getCanonicalName());

    public static String getRootPath() {
        try {
            String path = Path.of(RootPathLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toAbsolutePath().toString();
            logger.info("using jar file directory: '{}'", path);
            return path;
        } catch (Exception e) {
            String dir = System.getProperty("user.dir");
            logger.info("using working directory: '{}'", dir);
            return dir;
        }
    }

    public static String getRootPath(String... subdirectories) {
        return Path.of(getRootPath(), subdirectories).normalize().toString();
    }
}
