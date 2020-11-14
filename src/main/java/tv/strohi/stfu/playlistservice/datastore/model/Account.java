package tv.strohi.stfu.playlistservice.datastore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

import static tv.strohi.stfu.playlistservice.utils.EmtyOrNull.nullOrWhitespace;

@Entity
@Table
@Cacheable(false)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;

    private String channelId;

    @JsonIgnore
    private String clientKey;

    @JsonIgnore
    private String clientSecret;

    @JsonIgnore
    private String accessToken;

    @JsonIgnore
    private String refreshToken;

    @JsonIgnore
    private String tokenType;

    @JsonIgnore
    private Date expirationDate;

    public Account() {
    }

    public Account(String clientKey, String clientSecret, String accessToken, String refreshToken, String tokenType, Date expirationDate, String title, String channelId) {
        this.clientKey = clientKey;
        this.clientSecret = clientSecret;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expirationDate = expirationDate;
        this.title = title;
        this.channelId = channelId;
    }

    public Account(long id, String clientKey, String clientSecret, String accessToken, String refreshToken, String tokenType, Date expirationDate, String title, String channelId) {
        this.id = id;
        this.clientKey = clientKey;
        this.clientSecret = clientSecret;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expirationDate = expirationDate;
        this.title = title;
        this.channelId = channelId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "Account {" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", channelId='" + channelId + '\'' +
                ", clientKey='" + clientKey + '\'' +
                ", clientSecret='" + (nullOrWhitespace(clientSecret) ? "[NULL/EMPTY/WHITESPACE]" : "[HIDDEN]") + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
