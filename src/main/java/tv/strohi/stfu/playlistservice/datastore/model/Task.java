package tv.strohi.stfu.playlistservice.datastore.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    private String videoId;
    private String videoTitle;
    private String playlistId;
    private String playlistTitle;

    private Date taskDate = Date.from(Instant.now().plusSeconds(1 * 60));

    private boolean successful = false;
    private int attemptCount = 0;

    public Task() {
    }

    public Task(Account account, String videoId, String videoTitle, String playlistId, String playlistTitle, Date taskDate) {
        this.account = account;
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.playlistId = playlistId;
        this.playlistTitle = playlistTitle;
        this.taskDate = taskDate;
    }

    public Task(long id, Account account, String videoId, String videoTitle, String playlistId, String playlistTitle, Date taskDate) {
        this.id = id;
        this.account = account;
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.playlistId = playlistId;
        this.playlistTitle = playlistTitle;
        this.taskDate = taskDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int tryCount) {
        this.attemptCount = tryCount;
    }

    public void increaseAttempts() {
        this.attemptCount++;
    }
}
