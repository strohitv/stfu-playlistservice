package tv.strohi.stfu.playlistservice.youtube.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeItem {
    private String kind;
    private String etag;
    private String id;
    private YoutubeContentDetails contentDetails;
    private YoutubeSnippet snippet;
    private YoutubeStatusResponse status;

    public YoutubeItem() {
    }

    public YoutubeItem(String kind, String etag, String id, YoutubeContentDetails contentDetails, YoutubeSnippet snippet, YoutubeStatusResponse status) {
        this.kind = kind;
        this.etag = etag;
        this.id = id;
        this.contentDetails = contentDetails;
        this.snippet = snippet;
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

    public YoutubeContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(YoutubeContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }

    public YoutubeSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(YoutubeSnippet snippet) {
        this.snippet = snippet;
    }

    public YoutubeStatusResponse getStatus() {
        return status;
    }

    public void setStatus(YoutubeStatusResponse status) {
        this.status = status;
    }
}
