package tv.strohi.stfu.playlistservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tv.strohi.stfu.playlistservice.datastore.model.Account;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;
import tv.strohi.stfu.playlistservice.datastore.repository.TaskRepository;
import tv.strohi.stfu.playlistservice.runnable.YoutubePlaylistAddRunnable;

import java.util.List;

@RestController
@RequestMapping("accounts/{id}/tasks")
public class TaskController {
    private TaskRepository taskRepo;
    private AccountRepository accountRepo;

    @Autowired
    public void setTaskRepository(TaskRepository repo) {
        taskRepo = repo;
    }

    @Autowired
    public void setAccountRepository(AccountRepository repo) {
        accountRepo = repo;
    }

    @GetMapping
    public List<Task> getAllTasks(@PathVariable("id") long id) {
        return taskRepo.findByAccount_Id(id);
    }

    @PostMapping
    public Task addTask(@PathVariable("id") long id, @RequestBody Task task) {
        Account account = accountRepo.findById(id).orElse(null);

        if (account != null) {
            task.setAccount(account);
            YoutubePlaylistAddRunnable.scheduleTask(task, taskRepo);
            taskRepo.save(task);
        }

        return task;
    }

    @DeleteMapping
    public String deleteAllTasks(@PathVariable("id") long id) {
        List<Task> tasks = taskRepo.findByAccount_Id(id);
        taskRepo.deleteAll(tasks);

        return "Joah hat geklappt, alle Tasks für die Id " + id + " sind jetzt weg";
    }
}
