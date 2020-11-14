package tv.strohi.stfu.playlistservice.youtube.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeArrayResponse {
    private String kind;
    private String etag;
    private String prevPageToken;
    private String nextPageToken;
    private PageInfo pageInfo;
    private YoutubeItem[] items;

    public YoutubeArrayResponse() {
    }

    public YoutubeArrayResponse(String kind, String etag, String prevPageToken, String nextPageToken, PageInfo pageInfo, YoutubeItem[] items) {
        this.kind = kind;
        this.etag = etag;
        this.prevPageToken = prevPageToken;
        this.nextPageToken = nextPageToken;
        this.pageInfo = pageInfo;
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

    public String getPrevPageToken() {
        return prevPageToken;
    }

    public void setPrevPageToken(String prevPageToken) {
        this.prevPageToken = prevPageToken;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public YoutubeItem[] getItems() {
        return items;
    }

    public void setItems(YoutubeItem[] items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "YoutubeArrayResponse {" +
                "kind='" + kind + '\'' +
                ", etag='" + etag + '\'' +
                ", prevPageToken='" + prevPageToken + '\'' +
                ", nextPageToken='" + nextPageToken + '\'' +
                ", pageInfo=" + pageInfo +
                ", items=" + Arrays.toString(items) +
                '}';
    }
}
