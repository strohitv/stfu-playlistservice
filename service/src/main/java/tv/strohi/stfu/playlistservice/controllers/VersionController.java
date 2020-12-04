package tv.strohi.stfu.playlistservice.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tv.strohi.stfu.playlistservice.StfuPlaylistServiceApplication;

@RestController
@RequestMapping("version")
public class VersionController {
    private final Logger logger = LogManager.getLogger(VersionController.class.getCanonicalName());

    @GetMapping
    public String getVersion() {
        logger.info("get all accounts was called");

        String currentVersion = StfuPlaylistServiceApplication.class.getPackage().getImplementationVersion();
        if (currentVersion == null) {
            currentVersion = "0.0.0-SNAPSHOT";
        }
        logger.info("current service version: {}", currentVersion);

        return currentVersion;
    }
}
