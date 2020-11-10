package tv.strohi.stfu.playlistservice.runnable;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.model.TaskState;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;
import tv.strohi.stfu.playlistservice.datastore.repository.TaskRepository;
import tv.strohi.stfu.playlistservice.youtube.PlaylistAdder;
import tv.strohi.stfu.playlistservice.youtube.VideoInformationLoader;
import tv.strohi.stfu.playlistservice.youtube.model.YoutubeVideoResponse;

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
            if (task == null || task.getState() != TaskState.Open) return;

            task.increaseAttempts();

            YoutubeVideoResponse response = Arrays.stream(new VideoInformationLoader(accountRepo).loadVideoFromYoutube(task).getItems()).findFirst().orElse(null);
            if (response != null) {
                if (!response.getStatus().getPrivacyStatus().equalsIgnoreCase("private") || response.getStatus().getPublishAt().toInstant().isBefore(Instant.now())) {
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
                    task.setAddAt(Date.from(response.getStatus().getPublishAt().toInstant().plusSeconds(10)));
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
