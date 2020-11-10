package tv.strohi.stfu.playlistservice.runnable;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
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

    private final Task task;

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
            task.increaseAttempts();

            YoutubeVideoResponse response = Arrays.stream(new VideoInformationLoader(accountRepo).loadVideoFromYoutube(task).getItems()).findFirst().orElse(null);
            if (response != null) {
                if (!response.getStatus().getPrivacyStatus().equalsIgnoreCase("private") || response.getStatus().getPublishAt().toInstant().isBefore(Instant.now())) {
                    // Video can be added
                    if (new PlaylistAdder(accountRepo).addVideoToPlaylist(task)) {
                        task.setSuccessful(true);
                    }
                } else {
                    // Video is scheduled for an later date
                    task.setAddAt(Date.from(response.getStatus().getPublishAt().toInstant().plusSeconds(10)));
                    scheduleTask(task, taskRepo, accountRepo);
                }
            } else {
                // no video found -> do nothing
            }
        } catch (IOException e) {
            // do nothing
            System.out.println(e);
        }

        taskRepo.save(task);
    }
}
