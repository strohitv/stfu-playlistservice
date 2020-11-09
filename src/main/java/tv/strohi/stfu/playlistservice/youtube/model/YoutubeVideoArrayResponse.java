package tv.strohi.stfu.playlistservice.youtube.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeVideoArrayResponse {
    private String kind;
    private String etag;
    private YoutubeVideoResponse[] items;

    public YoutubeVideoArrayResponse() {
    }

    public YoutubeVideoArrayResponse(String kind, String etag, YoutubeVideoResponse[] items) {
        this.kind = kind;
        this.etag = etag;
        this.items = items;
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

    public YoutubeVideoResponse[] getItems() {
        return items;
    }

    public void setItems(YoutubeVideoResponse[] items) {
        this.items = items;
    }
}
