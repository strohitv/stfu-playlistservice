package tv.strohi.stfu.playlistservice.youtube;

import com.fasterxml.jackson.databind.ObjectMapper;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;
import tv.strohi.stfu.playlistservice.youtube.model.VideoPlaylistItem;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlaylistAdder {
    private final AccountRepository repository;

    public PlaylistAdder(AccountRepository repository) {
        this.repository = repository;
    }

    public boolean addVideoToPlaylist(Task task) throws IOException {
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

        int HttpResult = connection.getResponseCode();
        return HttpResult == HttpURLConnection.HTTP_OK;
    }
}
