package tv.strohi.stfu.playlistservice.youtube.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeVideoResponse {
    private String kind;
    private String etag;
    private String id;
    private YoutubeVideoStatusResponse status;

    public YoutubeVideoResponse() {
    }

    public YoutubeVideoResponse(String kind, String etag, String id, YoutubeVideoStatusResponse status) {
        this.kind = kind;
        this.etag = etag;
        this.id = id;
        this.status = status;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public YoutubeVideoStatusResponse getStatus() {
        return status;
    }

    public void setStatus(YoutubeVideoStatusResponse status) {
        this.status = status;
    }
}
