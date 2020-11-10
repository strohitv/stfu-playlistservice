package tv.strohi.stfu.playlistservice.youtube;

import com.fasterxml.jackson.databind.ObjectMapper;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;
import tv.strohi.stfu.playlistservice.youtube.model.YoutubeVideoArrayResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static tv.strohi.stfu.playlistservice.youtube.utils.Utils.readResult;

public class VideoInformationLoader {
    private final AccountRepository repository;

    public VideoInformationLoader(AccountRepository repository) {
        this.repository = repository;
    }

    public YoutubeVideoArrayResponse loadVideoFromYoutube(Task task) throws IOException {
        YoutubeVideoArrayResponse response = null;

        HttpURLConnection connection = (HttpURLConnection) new URL(String.format("https://www.googleapis.com/youtube/v3/videos?part=status&id=%s", task.getVideoId())).openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", String.format("Bearer %s", new AccountConnector(repository).withValidAccessToken(task.getAccount()).getAccessToken()));

        int HttpResult = connection.getResponseCode();
        if (HttpResult == HttpURLConnection.HTTP_OK) {
            String result = readResult(connection);
            response = new ObjectMapper().readValue(result, YoutubeVideoArrayResponse.class);
        } else {
            System.out.println(connection.getResponseMessage());
        }

        return response;
    }
}
