package tv.strohi.stfu.playlistservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.bind.annotation.*;
import tv.strohi.stfu.playlistservice.datastore.model.Client;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.repository.ClientRepository;
import tv.strohi.stfu.playlistservice.datastore.repository.TaskRepository;
import tv.strohi.stfu.playlistservice.runnable.YoutubePlaylistAddRunnable;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@RestController
@RequestMapping("clients/{id}/tasks")
public class TaskController {
    private TaskRepository taskRepo;
    private ClientRepository clientRepo;

    private TaskScheduler scheduler;

    @Autowired
    public void setTaskRepository(TaskRepository repo) {
        taskRepo = repo;
    }

    @Autowired
    public void setClientRepository(ClientRepository repo) {
        clientRepo = repo;
    }

    @GetMapping
    public List<Task> getAllTasks(@PathVariable("id") long id) {
        return taskRepo.findByClient_Id(id);
    }

    @PostMapping
    public Task addTask(@PathVariable("id") long id, @RequestBody Task task) {
        Client client = clientRepo.findById(id).orElse(null);

        if (client != null) {
            task.setClient(client);
            YoutubePlaylistAddRunnable.scheduleTask(task, taskRepo);
            taskRepo.save(task);
        }

        return task;
    }

    @DeleteMapping
    public String deleteAllTasks(@PathVariable("id") long id) {
        List<Task> tasks = taskRepo.findByClient_Id(id);
        taskRepo.deleteAll(tasks);

        return "Joah hat geklappt, alle Tasks für die Id " + id + " sind jetzt weg";
    }
}
