package tv.strohi.stfu.playlistservice.youtube;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;
import tv.strohi.stfu.playlistservice.youtube.model.YoutubeArrayResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static tv.strohi.stfu.playlistservice.youtube.utils.Utils.readResult;

public class VideoInformationLoader {
    private final Logger logger = LogManager.getLogger(VideoInformationLoader.class.getCanonicalName());
    private final AccountRepository repository;

    public VideoInformationLoader(AccountRepository repository) {
        this.repository = repository;
    }

    public YoutubeArrayResponse loadVideoFromYoutube(Task task) {
        logger.debug("loading video from youtube...");
        return executeGetRequest(task, String.format("https://www.googleapis.com/youtube/v3/videos?part=snippet&part=status&id=%s", task.getVideoId()));
    }

    public YoutubeArrayResponse loadPlaylistFromYoutube(Task task) {
        logger.debug("loading playlist from youtube...");
        return executeGetRequest(task, String.format("https://www.googleapis.com/youtube/v3/playlists?part=snippet&id=%s", task.getPlaylistId()));
    }

    private YoutubeArrayResponse executeGetRequest(Task task, String url) {
        logger.debug("executing request...");
        YoutubeArrayResponse response = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", String.format("Bearer %s", new AccountConnector(repository).withValidAccessToken(task.getAccount()).getAccessToken()));

            int responseCode = connection.getResponseCode();
            logger.info("response code: {}", responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String result = readResult(connection);
                response = new ObjectMapper().readValue(result, YoutubeArrayResponse.class);
                logger.info("request was successful.");
                logger.debug("response: {}", response);
            } else {
                logger.error("request was not successful. Message: {}", connection.getResponseMessage());
            }
        } catch (IOException ex) {
            logger.error("request could not be sent. Message: {}", ex.getMessage());
            logger.error(ex);
        }

        return response;
    }
}
