package tv.strohi.stfu.playlistservice.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tv.strohi.stfu.playlistservice.StfuPlaylistServiceApplication;
import tv.strohi.stfu.playlistservice.update.UpdateChecker;
import tv.strohi.stfu.playlistservice.update.utils.UpdateExtractor;

import java.io.IOException;

import static tv.strohi.stfu.playlistservice.utils.RootPathLoader.getRootPath;

@Component
public class ServiceUpdater {
    private final Logger logger = LogManager.getLogger(ServiceUpdater.class.getCanonicalName());

    public boolean update() {
        logger.info("checking for updates...");

        String currentVersion = StfuPlaylistServiceApplication.class.getPackage().getImplementationVersion();
        if (currentVersion == null) {
            currentVersion = "0.0.0-SNAPSHOT";
        }
        logger.info("current service version: {}", currentVersion);

        UpdateChecker checker = new UpdateChecker("AIzaSyC8l0tHuvhFLU6HtzHXwW2Nraf-ARMm1wE", getRootPath(), currentVersion);

        String zipPath = checker.downloadUpdate(StfuPlaylistServiceApplication.getSettings().downloadPreviewUpdates());

        if (zipPath != null) {
            logger.info("Update found, extracting...");
            String targetDir = getRootPath();
            String updaterJar = new UpdateExtractor(zipPath, targetDir).extractSingle(c -> c.toLowerCase().contains("updater") && c.toLowerCase().endsWith(".jar"));

            if (updaterJar != null) {
                logger.info("Update extracted, starting the updater now...");

                String command = String.format("java -jar \"%s\" \"%s\" \"%s\" \"%s\"", updaterJar, zipPath, targetDir, "(?i).*service.*\\.jar");
                logger.info("running command: {}", command);

                try {
                    ProcessBuilder pb = new ProcessBuilder("java", "-jar", updaterJar, zipPath, targetDir, "(?i).*service.*\\.jar");
                    pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                    pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                    pb.start();

                    return true;
                } catch (IOException e) {
                    logger.error(e);
                    logger.warn("Updater could not be started, starting the service without update...");
                }
            }
        } else {
            logger.info("No newer version found. Skipping update.");
        }

        return false;
    }

    @Scheduled(cron="0 ${random.int[0,59]} 4 * * ?")
    public void updateScheduled() {
        if (StfuPlaylistServiceApplication.getSettings().checkForUpdatesEach24h()) {
            if (update()) {
                StfuPlaylistServiceApplication.getContext().close();
            }
        }
    }
}
