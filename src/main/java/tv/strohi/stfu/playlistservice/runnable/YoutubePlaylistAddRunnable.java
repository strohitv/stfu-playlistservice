package tv.strohi.stfu.playlistservice.runnable;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.model.TaskState;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;
import tv.strohi.stfu.playlistservice.datastore.repository.TaskRepository;
import tv.strohi.stfu.playlistservice.youtube.PlaylistAdder;
import tv.strohi.stfu.playlistservice.youtube.VideoInformationLoader;
import tv.strohi.stfu.playlistservice.youtube.model.YoutubeItem;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class YoutubePlaylistAddRunnable implements Runnable {
    private final TaskRepository taskRepo;
    private final AccountRepository accountRepo;

    private Task task;

    public YoutubePlaylistAddRunnable(Task task, TaskRepository taskRepo, AccountRepository accountRepo) {
        this.task = task;
        this.taskRepo = taskRepo;
        this.accountRepo = accountRepo;
    }

    public static void scheduleTask(Task task, TaskRepository taskRepo, AccountRepository accountRepo) {
        ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
        TaskScheduler scheduler = new ConcurrentTaskScheduler(localExecutor);

        scheduler.schedule(new YoutubePlaylistAddRunnable(task, taskRepo, accountRepo), task.getAddAt());
    }

    @Override
    public void run() {
        try {
            // don't execute deleted or already done / failed tasks
            task = taskRepo.findById(task.getId()).orElse(null);
            if (task == null || task.getState() != TaskState.Open || task.getAddAt().toInstant().isAfter(Instant.now())) return;
            if (task.getAccount() == null) {
                taskRepo.delete(task);
                return;
            }

            task.increaseAttempts();

            VideoInformationLoader loader = new VideoInformationLoader(accountRepo);
            YoutubeItem video = Arrays.stream(loader.loadVideoFromYoutube(task).getItems()).findFirst().orElse(null);
            YoutubeItem playlist = Arrays.stream(loader.loadPlaylistFromYoutube(task).getItems()).findFirst().orElse(null);

            if (video != null && playlist != null) {
                task.setVideoTitle(video.getSnippet().getTitle());
                task.setPlaylistTitle(playlist.getSnippet().getTitle());

                if (!video.getStatus().getPrivacyStatus().equalsIgnoreCase("private") || video.getStatus().getPublishAt().toInstant().isBefore(Instant.now())) {
                    // Video can be added
                    if (new PlaylistAdder(accountRepo).addVideoToPlaylist(task)) {
                        task.setState(TaskState.Done);
                    } else if (task.getAttemptCount() <= 12) {
                        task.setAddAt(Date.from(Instant.now().plusSeconds(300)));
                        scheduleTask(task, taskRepo, accountRepo);
                    } else {
                        // too many attempts -> won't work
                        task.setState(TaskState.Failed);
                    }
                } else {
                    // Video is scheduled for an later date
                    task.setAddAt(Date.from(video.getStatus().getPublishAt().toInstant().plusSeconds(10)));
                    scheduleTask(task, taskRepo, accountRepo);
                }
            } else {
                // no video found -> do nothing
                task.setState(TaskState.Failed);
            }
        } catch (IOException e) {
            // do nothing
            task.setState(TaskState.Failed);
        }

        taskRepo.save(task);
    }
}
