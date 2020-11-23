package tv.strohi.stfu.playlistservice.youtube;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.strohi.stfu.playlistservice.datastore.model.Account;
import tv.strohi.stfu.playlistservice.datastore.model.AuthCode;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;
import tv.strohi.stfu.playlistservice.youtube.model.YoutubeArrayResponse;
import tv.strohi.stfu.playlistservice.youtube.model.YoutubeAuthResponse;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.time.Instant;

import static tv.strohi.stfu.playlistservice.update.gdrive.Utils.readResult;

public class AccountConnector {
    private final Logger logger = LogManager.getLogger(AccountConnector.class.getCanonicalName());

    private final AccountRepository accountRepository;

    public AccountConnector(AccountRepository accountRepository) {
        logger.debug("setting account repository...");
        this.accountRepository = accountRepository;
    }

    public Account connectAccount(AuthCode connectInformation) throws IOException {
        logger.info("connecting a new account...");
        logger.debug("connect information: {}", connectInformation);

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

        if (newAccount.getAccessToken() != null) {
            logger.info("account connected.");
            return newAccount;
        } else {
            logger.error("account could not be connected.");
            return null;
        }
    }

    public Account withValidAccessToken(Account account) throws IOException {
        if (account.getExpirationDate().toInstant().isBefore(Instant.now().minusSeconds(60))) {
            logger.info("refreshing access on account with id: {}", account.getId());
            logger.debug("account: {}", account);

            String content = String.format("client_id=%s&client_secret=%s&refresh_token=%s&grant_type=refresh_token", account.getClientKey(), account.getClientSecret(), account.getRefreshToken());
            getAccessToken(account, content);
        }

        return account;
    }

    private void getAccessToken(Account account, String content) throws IOException {
        logger.info("getting access token for account with id: {}", account.getId());
        HttpURLConnection connection = (HttpURLConnection) new URL("https://www.googleapis.com/oauth2/v4/token").openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept", "application/json");

        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(content);
        wr.flush();
        logger.info("request sent");

        int responseCode = connection.getResponseCode();
        logger.info("response code: {}", responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            logger.info("successful request. parsing information from youtubes response.");
            String result = readResult(connection);
            YoutubeAuthResponse response = new ObjectMapper().readValue(result, YoutubeAuthResponse.class);
            logger.debug("response: {}", response);

            account.setAccessToken(response.access_token);
            account.setTokenType(response.token_type);
            account.setExpirationDate(Date.from(Instant.now().plusSeconds(response.expires_in)));

            if (response.refresh_token != null) {
                account.setRefreshToken(response.refresh_token);
            }

            loadAccountDetails(account);

            logger.info("saving account to database.");
            accountRepository.save(account);
            logger.info("get access token finished successful.");
        } else {
            logger.error("request failed. Message: {}", connection.getResponseMessage());
        }
    }

    private void loadAccountDetails(Account account) throws IOException {
        logger.info("loading account details from youtube...");

        HttpURLConnection connection = (HttpURLConnection) new URL("https://www.googleapis.com/youtube/v3/channels?part=snippet&mine=true&key={YoutubeClientData.YoutubeApiKey}").openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", String.format("Bearer %s", withValidAccessToken(account).getAccessToken()));

        int responseCode = connection.getResponseCode();
        logger.info("response code: {}", responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            logger.info("successful request. parsing information from youtubes response.");
            String result = readResult(connection);
            YoutubeArrayResponse response = new ObjectMapper().readValue(result, YoutubeArrayResponse.class);

            if (response.getItems().length > 0) {
                logger.info("setting account title and channel id...");
                account.setTitle(response.getItems()[0].getSnippet().getTitle());
                account.setChannelId(response.getItems()[0].getId());
                logger.info("done.");
                logger.debug("new account: {}", account);
            }
        } else {
            logger.error("request failed. Message: {}", connection.getResponseMessage());
        }
    }
}
