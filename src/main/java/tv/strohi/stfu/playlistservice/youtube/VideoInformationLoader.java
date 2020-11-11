package tv.strohi.stfu.playlistservice.youtube;

import com.fasterxml.jackson.databind.ObjectMapper;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;
import tv.strohi.stfu.playlistservice.youtube.model.YoutubeArrayResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static tv.strohi.stfu.playlistservice.youtube.utils.Utils.readResult;

public class VideoInformationLoader {
    private final AccountRepository repository;

    public VideoInformationLoader(AccountRepository repository) {
        this.repository = repository;
    }

    public YoutubeArrayResponse loadVideoFromYoutube(Task task) {
        return executeGetRequest(task, String.format("https://www.googleapis.com/youtube/v3/videos?part=snippet&part=status&id=%s", task.getVideoId()));
    }

    public YoutubeArrayResponse loadPlaylistFromYoutube(Task task) {
        return executeGetRequest(task, String.format("https://www.googleapis.com/youtube/v3/playlists?part=snippet&id=%s", task.getPlaylistId()));
    }

    private YoutubeArrayResponse executeGetRequest(Task task, String url) {
        YoutubeArrayResponse response = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", String.format("Bearer %s", new AccountConnector(repository).withValidAccessToken(task.getAccount()).getAccessToken()));

            int HttpResult = connection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                String result = readResult(connection);
                response = new ObjectMapper().readValue(result, YoutubeArrayResponse.class);
            } else {
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return response;
    }
}
