package tv.strohi.stfu.playlistservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.model.TaskState;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;
import tv.strohi.stfu.playlistservice.datastore.repository.TaskRepository;

import java.util.List;

import static tv.strohi.stfu.playlistservice.runnable.YoutubePlaylistAddRunnable.scheduleTask;

@Component
public class StartupApplicationListenerExample implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<Task> openTasks = taskRepository.findByState(TaskState.Open);
        for (Task task : openTasks) {
            scheduleTask(task, taskRepository, accountRepository);
        }
    }
}
