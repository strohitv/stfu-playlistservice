package tv.strohi.stfu.playlistservice.datastore.model;

public class AuthCode {
    private String code;
    private String clientId;
    private String clientSecret;
    private String redirectUri;

    public AuthCode() {
    }

    public AuthCode(String code, String clientId, String clientSecret, String redirectUri) {
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    @Override
    public String toString() {
        return "AuthCode {" +
                "code='" + code + '\'' +
                ", clientId='" + clientId + '\'' +
                ", redirectUri='" + redirectUri + '\'' +
                '}';
    }
}
