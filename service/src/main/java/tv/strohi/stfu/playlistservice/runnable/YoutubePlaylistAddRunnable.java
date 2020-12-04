package tv.strohi.stfu.playlistservice.runnable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(YoutubePlaylistAddRunnable.class.getCanonicalName());

    private final TaskRepository taskRepo;
    private final AccountRepository accountRepo;

    private Task task;

    public YoutubePlaylistAddRunnable(Task task, TaskRepository taskRepo, AccountRepository accountRepo) {
        logger.info("creating new runnable for task with id {}", task.getId());
        logger.debug("task of this runnable: {}", task);

        this.task = task;
        this.taskRepo = taskRepo;
        this.accountRepo = accountRepo;
    }

    public static void scheduleTask(Task task, TaskRepository taskRepo, AccountRepository accountRepo) {
        logger.info("scheduling runnable for task with id {}", task.getId());

        ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
        TaskScheduler scheduler = new ConcurrentTaskScheduler(localExecutor);

        scheduler.schedule(new YoutubePlaylistAddRunnable(task, taskRepo, accountRepo), task.getAddAt());
        logger.info("finished scheduling");
    }

    @Override
    public void run() {
        logger.info("running playlist adding for task with id {}", task.getId());

        try {
            // don't execute deleted or already done / failed tasks
            task = taskRepo.findById(task.getId()).orElse(null);
            if (task == null || task.getState() != TaskState.Open || task.getAddAt().toInstant().isAfter(Instant.now())) return;
            if (task.getAccount() == null) {
                logger.info("task with id {} has no account and is therefore defective -> deleting task", task.getId());
                logger.debug("defective task: {}", task);
                taskRepo.delete(task);
                return;
            }

            task.increaseAttempts();
            logger.info("this is attempt number {} for this task", task.getAttemptCount());
            logger.info("loading video and playlist data from youtube...");

            VideoInformationLoader loader = new VideoInformationLoader(accountRepo);

            YoutubeItem video = Arrays.stream(loader.loadVideoFromYoutube(task).getItems()).findFirst().orElse(null);
            logger.debug("loaded video from youtube: {}", video);

            YoutubeItem playlist = Arrays.stream(loader.loadPlaylistFromYoutube(task).getItems()).findFirst().orElse(null);
            logger.debug("loaded playlist from youtube: {}", playlist);

            if (video != null && playlist != null) {
                logger.info("found video and playlist");
                logger.info("refreshing video and playlist title in database");
                task.setVideoTitle(video.getSnippet().getTitle());
                task.setPlaylistTitle(playlist.getSnippet().getTitle());

                if (!video.getStatus().getPrivacyStatus().equalsIgnoreCase("private") || video.getStatus().getPublishAt().toInstant().isBefore(Instant.now())) {
                    logger.info("video can be added to playlist");

                    // Video can be added
                    if (new PlaylistAdder(accountRepo).addVideoToPlaylist(task)) {
                        logger.info("added video to playlist");
                        task.setState(TaskState.Done);
                    } else if (task.getAttemptCount() <= 12) {
                        logger.warn("adding failed. rescheduling the task to be executed again in five minutes.");
                        task.setAddAt(Date.from(Instant.now().plusSeconds(300)));
                        scheduleTask(task, taskRepo, accountRepo);
                    } else {
                        // too many attempts -> won't work
                        logger.warn("adding failed and max attempt count was reached. adding video to playlist won't be tried again.");
                        task.setState(TaskState.Failed);
                    }
                } else {
                    // Video is scheduled for an later date
                    logger.warn("video will be published at {}. rescheduling the task to add the video 10 seconds after release.", video.getStatus().getPublishAt());
                    task.setAddAt(Date.from(video.getStatus().getPublishAt().toInstant().plusSeconds(10)));
                    scheduleTask(task, taskRepo, accountRepo);
                }
            } else if (video == null) {
                // no video found
                logger.error("video could not be found -> aborting task");
                task.setState(TaskState.Failed);
            } else {
                // no playlist found
                logger.error("playlist could not be found -> aborting task");
                task.setState(TaskState.Failed);
            }
        } catch (IOException e) {
            // do nothing
            logger.error("exception occured: {}", e.getMessage());
            logger.error(e);
            task.setState(TaskState.Failed);
        }

        logger.info("refreshing task in database");
        taskRepo.save(task);
    }
}
