package tv.strohi.stfu.playlistservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import tv.strohi.stfu.playlistservice.config.ServiceSettings;
import tv.strohi.stfu.playlistservice.config.SettingsLoader;
import tv.strohi.stfu.playlistservice.utils.ServiceUpdater;

import java.util.Properties;

@SpringBootApplication
public class StfuPlaylistServiceApplication {
    private static final Logger logger = LogManager.getLogger(StfuPlaylistServiceApplication.class.getCanonicalName());
    private static final String configFilename = "/config.properties";

    private static final ServiceSettings settings = new ServiceSettings();

    private static ConfigurableApplicationContext context = null;

    public static void main(String[] args) {
        logger.info("service is starting...");

        new SettingsLoader(configFilename).loadSettings(settings);
        logger.info("service properties: {}", settings);

        if (settings.checkForUpdatesAtStartup() && new ServiceUpdater().update()) {
            return;
        }

        logger.info("starting service...");

        SpringApplication app = new SpringApplication(StfuPlaylistServiceApplication.class);

        Properties properties = new Properties();
        properties.put("server.port", settings.getPort());
        properties.put("logging.level.root", settings.getLoglevelRoot().toString());
        properties.put("logging.level.tv.strohi.stfu", settings.getLoglevelService().toString());
        app.setDefaultProperties(properties);

        context = app.run(args);
        logger.info("application started successfully");
    }

    public static ServiceSettings getSettings() {
        return settings;
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }
}
