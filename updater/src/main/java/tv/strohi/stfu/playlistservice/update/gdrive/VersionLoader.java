package tv.strohi.stfu.playlistservice.update.gdrive;

import com.fasterxml.jackson.databind.ObjectMapper;
import tv.strohi.stfu.playlistservice.update.gdrive.model.GoogleDriveFilesArray;
import tv.strohi.stfu.playlistservice.update.model.PlaylistServiceVersion;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import static tv.strohi.stfu.playlistservice.update.gdrive.Utils.readResult;

public class VersionLoader {
    private static final String REQUEST_URL = "https://www.googleapis.com/drive/v3/files?q=%%27%s%%27+in+parents&fields=files%%2Fid%%2C%%20files%%2Fname%%2C%%20files%%2FmimeType%%2C%%20files%%2FwebContentLink&key=%s";
    private static final String DRIVE_URL = "1BXym0jke5IAbjfYdzO5GGeaeUnrwYg2-";

    private final String apiKey;

    public VersionLoader(String apiKey) {
        this.apiKey = apiKey;
    }

    public PlaylistServiceVersion[] getServiceVersions() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(String.format(REQUEST_URL, DRIVE_URL, apiKey)).openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String result = readResult(connection);
            GoogleDriveFilesArray response = new ObjectMapper().readValue(result, GoogleDriveFilesArray.class);

            return Arrays.stream(response.getFiles())
                    .filter(f -> !f.getMimeType().equalsIgnoreCase("application/vnd.google-apps.folder"))
                    .map(f -> new PlaylistServiceVersion(f.getName(), f.getName().toLowerCase().contains("preview"), f.getWebContentLink()))
                    .toArray(PlaylistServiceVersion[]::new);
        } else {
            return new PlaylistServiceVersion[0];
        }
    }
}
