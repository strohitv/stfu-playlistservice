package tv.strohi.stfu.playlistservice.youtube.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeStatusResponse {
    private String uploadStatus;
    private String failureReason;
    private String rejectionReason;
    private String privacyStatus;
    private Date publishAt;
    private String license;
    private boolean embeddable;
    private boolean publicStatsViewable;
    private boolean madeForKids;
    private boolean selfDeclaredMadeForKids;

    public YoutubeStatusResponse() {
    }

    public YoutubeStatusResponse(String uploadStatus, String failureReason, String rejectionReason, String privacyStatus, Date publishAt, String license, boolean embeddable, boolean publicStatsViewable, boolean madeForKids, boolean selfDeclaredMadeForKids) {
        this.uploadStatus = uploadStatus;
        this.failureReason = failureReason;
        this.rejectionReason = rejectionReason;
        this.privacyStatus = privacyStatus;
        this.publishAt = publishAt;
        this.license = license;
        this.embeddable = embeddable;
        this.publicStatsViewable = publicStatsViewable;
        this.madeForKids = madeForKids;
        this.selfDeclaredMadeForKids = selfDeclaredMadeForKids;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getPrivacyStatus() {
        return privacyStatus;
    }

    public void setPrivacyStatus(String privacyStatus) {
        this.privacyStatus = privacyStatus;
    }

    public Date getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(Date publishAt) {
        this.publishAt = publishAt;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public boolean isEmbeddable() {
        return embeddable;
    }

    public void setEmbeddable(boolean embeddable) {
        this.embeddable = embeddable;
    }

    public boolean isPublicStatsViewable() {
        return publicStatsViewable;
    }

    public void setPublicStatsViewable(boolean publicStatsViewable) {
        this.publicStatsViewable = publicStatsViewable;
    }

    public boolean isMadeForKids() {
        return madeForKids;
    }

    public void setMadeForKids(boolean madeForKids) {
        this.madeForKids = madeForKids;
    }

    public boolean isSelfDeclaredMadeForKids() {
        return selfDeclaredMadeForKids;
    }

    public void setSelfDeclaredMadeForKids(boolean selfDeclaredMadeForKids) {
        this.selfDeclaredMadeForKids = selfDeclaredMadeForKids;
    }
}
