package tv.strohi.stfu.playlistservice.youtube;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;
import tv.strohi.stfu.playlistservice.youtube.model.VideoPlaylistItem;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlaylistAdder {
    private final Logger logger = LogManager.getLogger(PlaylistAdder.class.getCanonicalName());

    private final AccountRepository repository;

    public PlaylistAdder(AccountRepository repository) {
        logger.debug("setting account repository...");
        this.repository = repository;
    }

    public boolean addVideoToPlaylist(Task task) throws IOException {
        logger.info("adding a video with id {} to playlist with id {}", task.getVideoId(), task.getPlaylistId());
        logger.debug("task: {}", task);

        HttpURLConnection connection = (HttpURLConnection) new URL("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet").openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", String.format("Bearer %s", new AccountConnector(repository).withValidAccessToken(task.getAccount()).getAccessToken()));

        String content = new ObjectMapper().writeValueAsString(new VideoPlaylistItem(new VideoPlaylistItem.PlaylistSnippet(task.getPlaylistId(), new VideoPlaylistItem.VideoResource(task.getVideoId()))));
        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(content);
        wr.flush();
        logger.info("request sent");

        int responseCode = connection.getResponseCode();
        logger.info("response code: {}", responseCode);

        return responseCode == HttpURLConnection.HTTP_OK;
    }
}
