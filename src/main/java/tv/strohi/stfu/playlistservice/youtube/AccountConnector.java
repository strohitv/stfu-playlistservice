package tv.strohi.stfu.playlistservice.youtube;

import com.fasterxml.jackson.databind.ObjectMapper;
import tv.strohi.stfu.playlistservice.datastore.model.Account;
import tv.strohi.stfu.playlistservice.datastore.model.AuthCode;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;
import tv.strohi.stfu.playlistservice.youtube.model.YoutubeAuthResponse;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.time.Instant;

import static tv.strohi.stfu.playlistservice.youtube.utils.Utils.readResult;

public class AccountConnector {
    private final AccountRepository accountRepository;

    public AccountConnector(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account connectAccount(AuthCode connectInformation) throws IOException {
        Account newAccount = new Account();
        newAccount.setClientKey(connectInformation.getClientId());
        newAccount.setClientSecret(connectInformation.getClientSecret());

        String content = String.format(
                "code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code",
                connectInformation.getCode(),
                connectInformation.getClientId(),
                connectInformation.getClientSecret(),
                connectInformation.getRedirectUri()
        );
        getAccessToken(newAccount, content);

        return newAccount.getAccessToken() != null ? newAccount : null;
    }

    public Account withValidAccessToken(Account account) throws IOException {
        if (account.getExpirationDate().toInstant().isBefore(Instant.now().minusSeconds(60))) {
            String content = String.format("client_id=%s&client_secret=%s&refresh_token=%s&grant_type=refresh_token", account.getClientKey(), account.getClientSecret(), account.getRefreshToken());
            getAccessToken(account, content);
        }

        return account;
    }

    private void getAccessToken(Account account, String content) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("https://www.googleapis.com/oauth2/v4/token").openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept", "application/json");

        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(content);
        wr.flush();

        int HttpResult = connection.getResponseCode();
        if (HttpResult == HttpURLConnection.HTTP_OK) {
            String result = readResult(connection);
            YoutubeAuthResponse response = new ObjectMapper().readValue(result, YoutubeAuthResponse.class);

            account.setAccessToken(response.access_token);
            account.setRefreshToken(response.refresh_token);
            account.setTokenType(response.token_type);
            account.setExpirationDate(Date.from(Instant.now().plusSeconds(response.expires_in)));

            accountRepository.save(account);
        } else {
            System.out.println(connection.getResponseMessage());
        }
    }
}
