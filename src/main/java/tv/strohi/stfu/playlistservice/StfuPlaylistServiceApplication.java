package tv.strohi.stfu.playlistservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tv.strohi.stfu.playlistservice.config.ServiceSettings;
import tv.strohi.stfu.playlistservice.config.SettingsLoader;

import java.util.Properties;

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
            logger.info("checking for updates... [TODO!]");
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
