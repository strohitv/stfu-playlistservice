package tv.strohi.stfu.playlistservice.runnable;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.repository.TaskRepository;
import tv.strohi.stfu.playlistservice.youtube.model.YoutubeVideoResponse;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static tv.strohi.stfu.playlistservice.youtube.PlaylistAdder.addVideoToPlaylist;
import static tv.strohi.stfu.playlistservice.youtube.VideoInformationLoader.loadVideoFromYoutube;

public class YoutubePlaylistAddRunnable implements Runnable {
    private final TaskRepository taskRepo;

    private final Task task;

    public YoutubePlaylistAddRunnable(Task task, TaskRepository taskRepo) {
        this.task = task;
        this.taskRepo = taskRepo;
    }

    public static void scheduleTask(Task task, TaskRepository taskRepo) {
        ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
        TaskScheduler scheduler = new ConcurrentTaskScheduler(localExecutor);

        scheduler.schedule(new YoutubePlaylistAddRunnable(task, taskRepo), task.getAddAt());
    }

    @Override
    public void run() {
        try {
            task.increaseAttempts();

            YoutubeVideoResponse response = Arrays.stream(loadVideoFromYoutube(task).getItems()).findFirst().orElse(null);
            if (response != null) {
                if (!response.getStatus().getPrivacyStatus().equalsIgnoreCase("private") || response.getStatus().getPublishAt().toInstant().isBefore(Instant.now())) {
                    // Video can be added
                    if (addVideoToPlaylist(task)) {
                        task.setSuccessful(true);
                    }
                } else {
                    // Video is scheduled for an later date
                    task.setAddAt(Date.from(response.getStatus().getPublishAt().toInstant().plusSeconds(10)));
                    scheduleTask(task, taskRepo);
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
