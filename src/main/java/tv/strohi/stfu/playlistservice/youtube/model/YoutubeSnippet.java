package tv.strohi.stfu.playlistservice.youtube.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeSnippet {
    private String title;
    private String description;
    private String customUrl;
    private Date publishedAt;
    private String country;
    private String channelId;
    private String hl;
    private String name;
    private boolean assignable;
    private Map<String, YoutubeUrl> thumbnails;
    private YoutubeLocalization localized;

    public YoutubeSnippet() {
    }

    public YoutubeSnippet(String title, String description, String customUrl, Date publishedAt, String country, String channelId, String hl, String name, boolean assignable, Map<String, YoutubeUrl> thumbnails, YoutubeLocalization localized) {
        this.title = title;
        this.description = description;
        this.customUrl = customUrl;
        this.publishedAt = publishedAt;
        this.country = country;
        this.channelId = channelId;
        this.hl = hl;
        this.name = name;
        this.assignable = assignable;
        this.thumbnails = thumbnails;
        this.localized = localized;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getHl() {
        return hl;
    }

    public void setHl(String hl) {
        this.hl = hl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAssignable() {
        return assignable;
    }

    public void setAssignable(boolean assignable) {
        this.assignable = assignable;
    }

    public Map<String, YoutubeUrl> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Map<String, YoutubeUrl> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public YoutubeLocalization getLocalized() {
        return localized;
    }

    public void setLocalized(YoutubeLocalization localized) {
        this.localized = localized;
    }
}
