package tv.strohi.stfu.playlistservice.update;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.strohi.stfu.playlistservice.update.utils.UpdateExtractor;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class.getCanonicalName());

    public static void main(String... args) {
        if (args.length < 3) {
            logger.error("there must be at least two arguments to run the updater:");
            logger.error("    1 -> path to the zip directory");
            logger.error("    2 -> target directory path");
            logger.error("    3 -> regex to find the java application jar that should be started afterwards");
            return;
        }

        try {
            String zipPath = args[0];
            String targetDir = args[1];
            String startFileRegex = args[2];
            logger.info("updater started with arguments:");
            logger.info("zip file: '{}'", zipPath);
            logger.info("target dir: '{}'", targetDir);
            logger.info("start file regex: '{}'", startFileRegex);

            UpdateExtractor extractor = new UpdateExtractor(zipPath, targetDir);
            String[] files = extractor.extractAll(c -> !isUpdaterJar(c), 0);

            String fileToStart = Arrays.stream(files)
                    .filter(f -> Paths.get(f).getFileName().toString().matches(startFileRegex))
                    .findFirst()
                    .orElse(null);

            new File(zipPath).deleteOnExit();

            if (fileToStart != null) {
                ProcessBuilder pb = new ProcessBuilder("java", "-jar", fileToStart);
                pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                pb.start();
            }
        } catch (Exception e) {
            logger.error("could not install update...");
            logger.error("error message: {}", e.getMessage());
            logger.error(e);
        }
    }

    private static boolean isUpdaterJar(String name) {
        return name.toLowerCase().contains("updater") && name.toLowerCase().endsWith(".jar");
    }
}
