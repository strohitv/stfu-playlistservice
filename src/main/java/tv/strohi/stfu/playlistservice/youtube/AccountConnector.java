package tv.strohi.stfu.playlistservice.youtube;

import com.fasterxml.jackson.databind.ObjectMapper;
import tv.strohi.stfu.playlistservice.datastore.model.AuthCode;
import tv.strohi.stfu.playlistservice.datastore.model.Client;
import tv.strohi.stfu.playlistservice.datastore.repository.ClientRepository;
import tv.strohi.stfu.playlistservice.youtube.model.YoutubeAuthResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;

public class AccountConnector {
    private ClientRepository clientRepository;

    public AccountConnector(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientRepository getClientRepository() {
        return clientRepository;
    }

    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client connectAccount(AuthCode connectInformation) throws IOException {
        Client newClient = new Client();
        newClient.setClientKey(connectInformation.getClientId());
        newClient.setClientSecret(connectInformation.getClientSecret());

        String content = String.format(
                "code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code",
                connectInformation.getCode(),
                connectInformation.getClientId(),
                connectInformation.getClientSecret(),
                connectInformation.getRedirectUri()
        );

        HttpURLConnection connection = (HttpURLConnection) new URL("https://www.googleapis.com/oauth2/v4/token").openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept", "application/json");

        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(content);
        wr.flush();

        StringBuilder sb = new StringBuilder();
        int HttpResult = connection.getResponseCode();
        if (HttpResult == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            System.out.println("" + sb.toString());

            YoutubeAuthResponse response = new ObjectMapper().readValue(sb.toString(), YoutubeAuthResponse.class);

            newClient.setAccessToken(response.access_token);
            newClient.setRefreshToken(response.refresh_token);
            newClient.setTokenType(response.token_type);
            newClient.setExpirationDate(Date.from(Instant.now().plusSeconds(response.expires_in)));

            clientRepository.save(newClient);
            return newClient;
        } else {
            System.out.println(connection.getResponseMessage());
        }

        return null;
    }
}
