package tv.strohi.stfu.playlistservice.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.strohi.stfu.playlistservice.StfuPlaylistServiceApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

public class SettingsLoader {
    private final Logger logger = LogManager.getLogger(StfuPlaylistServiceApplication.class.getCanonicalName());
    private final String configFilename;

    public SettingsLoader(final String configFilename) {
        this.configFilename = configFilename;
    }

    public void loadSettings(ServiceSettings settings) {
        logger.info("load settings");

        InputStream input = StfuPlaylistServiceApplication.class.getResourceAsStream(configFilename);
        if (input != null) {
            logger.info("configuration file was found");
            Properties properties = new Properties();
            try {
                properties.load(input);
                input.close();
                logger.info("properties were loaded successfully");

                for (Map.Entry<Object, Object> prop : properties.entrySet()) {
                    String key = (String) prop.getKey();
                    switch (key) {
                        case "port":
                            try {
                                short port = Short.parseShort((String)prop.getValue());
                                settings.setPort(port);
                                logger.info("set application port to '{}'", port);
                            } catch (Exception ex) {
                                logger.error("application port '{}' could not be set. falling back to default port '{}'", prop.getValue(), settings.getPort());
                            }
                            break;
                        case "user":
                            try {
                                String user = (String) prop.getValue();
                                settings.setUser(user);
                                logger.info("set basic auth user to '{}'", user);
                            } catch (Exception ex) {
                                logger.error("basic auth user '{}' could not be set. falling back to default user '{}'", prop.getValue(), settings.getUser());
                            }
                            break;
                        case "password":
                            try {
                                String password = (String)prop.getValue();
                                settings.setPassword(password);
                                logger.info("set basic auth password");
                            } catch (Exception ex) {
                                logger.error("basic auth password '{}' could not be set. falling back to default password", prop.getValue());
                            }
                            break;
                        case "checkAtStartup":
                            try {
                                boolean checkAtStartup = Boolean.parseBoolean((String)prop.getValue());
                                settings.setCheckForUpdatesAtStartup(checkAtStartup);
                                logger.info("should the service check for updates at startup: '{}'", checkAtStartup);
                            } catch (Exception ex) {
                                logger.error("check for updates at startup property could not be set to '{}'. falling back to default setting: '{}'", prop.getValue(), settings.isCheckForUpdatesAtStartup());
                            }
                            break;
                        case "checkEach24h":
                            settings.setCheckForUpdatesEach24h(Boolean.parseBoolean((String) prop.getValue()));
                            try {
                                boolean checkEach24h = Boolean.parseBoolean((String)prop.getValue());
                                settings.setCheckForUpdatesEach24h(checkEach24h);
                                logger.info("should the service check for updates each 24 hours: '{}'", checkEach24h);
                            } catch (Exception ex) {
                                logger.error("check for updates each 24 hours property could not be set to '{}'. falling back to default setting: '{}'", prop.getValue(), settings.isCheckForUpdatesEach24h());
                            }
                            break;
                        case "downloadPreviewUpdates":
                            settings.setCheckForPreviewUpdates(Boolean.parseBoolean((String) prop.getValue()));
                            try {
                                boolean downloadPreviewUpdates = Boolean.parseBoolean((String)prop.getValue());
                                settings.setCheckForPreviewUpdates(downloadPreviewUpdates);
                                logger.info("should the service also download preview updates: '{}'", downloadPreviewUpdates);
                            } catch (Exception ex) {
                                logger.error("download preview updates property could not be set to '{}'. falling back to default setting: '{}'", prop.getValue(), settings.isCheckForPreviewUpdates());
                            }
                            break;
                        case "loglevel":
                            try {
                                Level level = Level.valueOf((String)prop.getValue());
                                settings.setLoglevel(level);
                                logger.info("set application log level to '{}'", level);
                            } catch (Exception ex) {
                                logger.error("application log level '{}' could not be set. falling back to default log level '{}'", prop.getValue(), settings.getLoglevel());
                            }
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException e) {
                logger.error("could not load properties, starting the service with the default properties...");
                logger.error("exception: " + e.getMessage());
                logger.error(e);
            }
        } else {
            // Beispielproperties schreiben
            logger.warn("no configuration file was found - creating an example file");

            Properties properties = new Properties();
            properties.setProperty("port", Short.toString(settings.getPort()));
            properties.setProperty("user", settings.getUser());
            properties.setProperty("password", settings.getPassword());
            properties.setProperty("checkAtStartup", Boolean.toString(settings.isCheckForUpdatesAtStartup()));
            properties.setProperty("checkEach24h", Boolean.toString(settings.isCheckForUpdatesEach24h()));
            properties.setProperty("downloadPreviewUpdates", Boolean.toString(settings.isCheckForPreviewUpdates()));
            properties.setProperty("loglevel", settings.getLoglevel().toString());

            try {
                properties.store(new FileWriter(Paths.get(getJarLocation(), configFilename).toString()), "Configure these settings the way you like and restart the playlist service afterwards.");
            } catch (IOException | URISyntaxException e) {
                logger.error("could not store default properties file");
                logger.error("exception: " + e.getMessage());
                logger.error(e);
            }

            logger.info("did not find properties, starting the service with the default properties...");
        }

        logger.info("finished settings loading");
    }

    private String getJarLocation() throws URISyntaxException {
        return new File(StfuPlaylistServiceApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
    }
}
