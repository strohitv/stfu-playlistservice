package tv.strohi.stfu.playlistservice.data.model;

import javax.persistence.*;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String clientKey;
    private String clientSecret;
    private String accessToken;
    private String refreshToken;

    public Client() {
    }

    public Client(String clientKey, String clientSecret, String accessToken, String refreshToken) {
        this.clientKey = clientKey;
        this.clientSecret = clientSecret;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Client(long id, String clientKey, String clientSecret, String accessToken, String refreshToken) {
        this.id = id;
        this.clientKey = clientKey;
        this.clientSecret = clientSecret;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
