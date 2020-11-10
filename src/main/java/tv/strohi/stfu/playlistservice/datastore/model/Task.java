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

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    private String videoId;
    private String videoTitle;
    private String playlistId;
    private String playlistTitle;

    private Date addAt = Date.from(Instant.now().plusSeconds(10));

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private TaskState state = TaskState.Open;

    private int attemptCount = 0;

    public Task() {
    }

    public Task(Account account, String videoId, String videoTitle, String playlistId, String playlistTitle, Date addAt) {
        this.account = account;
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.playlistId = playlistId;
        this.playlistTitle = playlistTitle;
        this.addAt = addAt;
    }

    public Task(long id, Account account, String videoId, String videoTitle, String playlistId, String playlistTitle, Date addAt) {
        this.id = id;
        this.account = account;
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.playlistId = playlistId;
        this.playlistTitle = playlistTitle;
        this.addAt = addAt;
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

    public Date getAddAt() {
        return addAt;
    }

    public void setAddAt(Date taskDate) {
        this.addAt = taskDate;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
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
