package tv.strohi.stfu.playlistservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tv.strohi.stfu.playlistservice.config.ServiceSettings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
public class StfuPlaylistServiceApplication {
	private static final String configFilename = "/config.properties";
    private static final ServiceSettings settings = new ServiceSettings();

    public static void main(String[] args) {
        // load service properties
        loadSettings();

        if (settings.isCheckForUpdatesAtStartup()) {
            // later: check for updates and install if needed
        }

        // start service
        SpringApplication app = new SpringApplication(StfuPlaylistServiceApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", settings.getPort()));
        app.run(args);
    }

    public static ServiceSettings getSettings() {
        return settings;
    }

    private static void loadSettings() {
        InputStream input = StfuPlaylistServiceApplication.class.getResourceAsStream(configFilename);
        if (input != null) {
            Properties properties = new Properties();
            try {
                properties.load(input);
                input.close();

                for (Map.Entry<Object, Object> prop : properties.entrySet()) {
                    String key = (String) prop.getKey();
                    switch (key) {
                        case "port":
                            settings.setPort(Short.parseShort((String)prop.getValue()));
                            break;
                        case "user":
                            settings.setUser((String) prop.getValue());
                            break;
                        case "password":
                            settings.setPassword((String) prop.getValue());
                            break;
                        case "checkAtStartup":
                            settings.setCheckForUpdatesAtStartup(Boolean.parseBoolean((String) prop.getValue()));
                            break;
                        case "checkEach24h":
                            settings.setCheckForUpdatesEach24h(Boolean.parseBoolean((String) prop.getValue()));
                            break;
                        case "downloadPreviewUpdates":
                            settings.setCheckForPreviewUpdates(Boolean.parseBoolean((String) prop.getValue()));
                            break;
                        case "loglevel":
                            settings.setLoglevel((String) prop.getValue());
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } else {
            // Beispielproperties schreiben
            Properties properties = new Properties();
            properties.setProperty("port", Short.toString(settings.getPort()));
            properties.setProperty("user", settings.getUser());
            properties.setProperty("password", settings.getPassword());
            properties.setProperty("checkAtStartup", Boolean.toString(settings.isCheckForUpdatesAtStartup()));
            properties.setProperty("checkEach24h", Boolean.toString(settings.isCheckForUpdatesEach24h()));
            properties.setProperty("downloadPreviewUpdates", Boolean.toString(settings.isCheckForPreviewUpdates()));
            properties.setProperty("loglevel", settings.getLoglevel());

            try {
                properties.store(new FileWriter(Paths.get(getJarLocation(), configFilename).toString()), "");
            } catch (IOException | URISyntaxException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static String getJarLocation() throws URISyntaxException {
        return new File(StfuPlaylistServiceApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
    }
}
