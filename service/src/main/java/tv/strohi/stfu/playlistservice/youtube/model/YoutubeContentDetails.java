package tv.strohi.stfu.playlistservice.youtube.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeContentDetails {
    private String videoId;
    private String videoPublishedAt;

    public YoutubeContentDetails() {
    }

    public YoutubeContentDetails(String videoId, String videoPublishedAt) {
        this.videoId = videoId;
        this.videoPublishedAt = videoPublishedAt;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoPublishedAt() {
        return videoPublishedAt;
    }

    public void setVideoPublishedAt(String videoPublishedAt) {
        this.videoPublishedAt = videoPublishedAt;
    }

    @Override
    public String toString() {
        return "YoutubeContentDetails {" +
                "videoId='" + videoId + '\'' +
                ", videoPublishedAt='" + videoPublishedAt + '\'' +
                '}';
    }
}
