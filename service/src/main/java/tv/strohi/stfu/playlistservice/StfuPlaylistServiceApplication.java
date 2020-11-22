package tv.strohi.stfu.playlistservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tv.strohi.stfu.playlistservice.config.ServiceSettings;
import tv.strohi.stfu.playlistservice.config.SettingsLoader;
import tv.strohi.stfu.playlistservice.update.UpdateChecker;
import tv.strohi.stfu.playlistservice.update.utils.UpdateExtractor;
import tv.strohi.stfu.playlistservice.utils.RootPathLoader;

import java.io.IOException;
import java.util.Properties;

import static tv.strohi.stfu.playlistservice.utils.RootPathLoader.getRootPath;

@SpringBootApplication
public class StfuPlaylistServiceApplication {
    private static final Logger logger = LogManager.getLogger(StfuPlaylistServiceApplication.class.getCanonicalName());
    private static final String configFilename = "/config.properties";

    private static final ServiceSettings settings = new ServiceSettings();

    public static void main(String[] args) {
        logger.info("service is starting...");
        // load service properties
        new SettingsLoader(configFilename).loadSettings(settings);
        logger.info("service properties: {}", settings);

        if (settings.checkForUpdatesAtStartup()) {
            // later: check for updates and install if needed
            logger.info("checking for updates...");

            String currentVersion = StfuPlaylistServiceApplication.class.getPackage().getImplementationVersion();
            if (currentVersion == null) {
                currentVersion = "0.0.0-SNAPSHOT";
            }
            logger.info("current service version: {}", currentVersion);

            UpdateChecker checker = new UpdateChecker("AIzaSyC8l0tHuvhFLU6HtzHXwW2Nraf-ARMm1wE", getRootPath(), currentVersion);

            String zipPath = checker.downloadUpdate(settings.downloadPreviewUpdates());

            if (zipPath != null) {
                // extract updater
                logger.info("Update found, extracting...");
                String updaterJar = new UpdateExtractor(zipPath, getRootPath()).extract();

                if (updaterJar != null) {
                    logger.info("Update extracted, starting the updater now...");

                    String command = String.format("java -jar \"%s\"", updaterJar);
                    logger.info("running command: {}", command);

                    try {
                        ProcessBuilder pb = new ProcessBuilder("java", "-jar", updaterJar);
                        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                        pb.start();
                        return;
                    } catch (IOException e) {
                        logger.error(e);
                        logger.warn("Updater could not be started, starting the service without update...");
                    }
                }
            } else {
                logger.info("No newer version found. Skipping update.");
            }
        }

        logger.info("starting service...");
        // start service
        SpringApplication app = new SpringApplication(StfuPlaylistServiceApplication.class);

        Properties properties = new Properties();
        properties.put("server.port", settings.getPort());
        properties.put("logging.level.root", settings.getLoglevelRoot().toString());
        properties.put("logging.level.tv.strohi.stfu", settings.getLoglevelService().toString());
        app.setDefaultProperties(properties);

        app.run(args);
        logger.info("application started successfully");
    }

    public static ServiceSettings getSettings() {
        return settings;
    }
}
