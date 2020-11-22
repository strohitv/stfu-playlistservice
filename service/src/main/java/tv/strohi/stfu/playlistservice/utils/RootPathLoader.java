package tv.strohi.stfu.playlistservice.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

public class RootPathLoader {
    private static final Logger logger = LogManager.getLogger(RootPathLoader.class.getCanonicalName());

    public static String getRootPath() {
        try {
            return Path.of(RootPathLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toAbsolutePath().toString();
        } catch (Exception e) {
            String dir = System.getProperty("user.dir");

            logger.error("could not load location of jar file, will use working dir '{}' instead", dir);
            logger.error("error message: {}", e.getMessage());
            logger.error(e);

            return dir;
        }
    }

    public static String getRootPath(String... subdirectories) {
        return Path.of(getRootPath(), subdirectories).normalize().toString();
    }
}
