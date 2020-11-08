package tv.strohi.stfu.playlistservice.runnable;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.repository.TaskRepository;

import java.sql.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class YoutubePlaylistAddRunnable implements Runnable {
    private TaskRepository taskRepo;

    private Task task;

    public YoutubePlaylistAddRunnable(Task task, TaskRepository taskRepo) {
        this.task = task;
        this.taskRepo = taskRepo;
    }

    public static void scheduleTask(Task task, TaskRepository taskRepo) {
        ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
        TaskScheduler scheduler = new ConcurrentTaskScheduler(localExecutor);

        scheduler.schedule(new YoutubePlaylistAddRunnable(task, taskRepo), task.getTaskDate());
    }

    public void setTaskRepo(TaskRepository repo) {
        taskRepo = repo;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        Random rand = new Random();

        task.increaseAttempts();

        if (rand.nextInt(10) > 7) {
            // Speichern, dass wir es versucht haben und es funktioniert hat!
            System.out.println("Jetzt wäre ein Video veröffentlicht worden! TaskId: " + task.getId());
            task.setSuccessful(true);
        } else {
            System.out.println("Jetzt wäre Veröffentlichung gescheitert! TaskId: " + task.getId() + "; Versuch Nummer: " + task.getAttemptCount());
            task.setSuccessful(true);
            task.setTaskDate(Date.from(task.getTaskDate().toInstant().plusSeconds(10)));
            scheduleTask(task, taskRepo);
        }

        taskRepo.save(task);
    }
}
