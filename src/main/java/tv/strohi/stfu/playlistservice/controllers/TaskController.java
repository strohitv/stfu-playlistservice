package tv.strohi.stfu.playlistservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.bind.annotation.*;
import tv.strohi.stfu.playlistservice.datastore.model.Account;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.model.TaskState;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;
import tv.strohi.stfu.playlistservice.datastore.repository.TaskRepository;

import java.util.List;

import static tv.strohi.stfu.playlistservice.runnable.YoutubePlaylistAddRunnable.scheduleTask;

@RestController
@RequestMapping("accounts/{id}/tasks")
public class TaskController implements ApplicationListener<ContextRefreshedEvent> {
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

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<Task> openTasks = taskRepo.findByState(TaskState.Open);
        for (Task task : openTasks) {
            scheduleTask(task, taskRepo, accountRepo);
        }
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
            scheduleTask(task, taskRepo, accountRepo);
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

    @GetMapping("tid}")
    public Task getTask(@PathVariable("id") long accountId, @PathVariable("tid") long taskId) {
        List<Task> tasks = taskRepo.findByAccount_Id(accountId);
        return tasks.stream().filter(t -> t.getId() == taskId).findFirst().orElse(null);
    }

    @PutMapping("{tid}")
    public Task updateTask(@PathVariable("id") long accountId, @PathVariable("tid") long taskId, @RequestBody Task task) {
        List<Task> tasks = taskRepo.findByAccount_Id(accountId);
        Task taskToEdit = tasks.stream().filter(t -> t.getId() == taskId).findFirst().orElse(null);

        if (taskToEdit != null) {
            taskToEdit.setVideoId(task.getVideoId());
            taskToEdit.setVideoTitle(task.getVideoTitle());
            taskToEdit.setPlaylistId(task.getPlaylistId());
            taskToEdit.setPlaylistTitle(task.getPlaylistTitle());
            taskToEdit.setAddAt(task.getAddAt());
            taskToEdit.setState(TaskState.Open);

            scheduleTask(taskToEdit, taskRepo, accountRepo);
            taskRepo.save(taskToEdit);
        }

        return taskToEdit;
    }

    @DeleteMapping("{tid}")
    public String deleteTask(@PathVariable("id") long accountId, @PathVariable("tid") long taskId) {
        taskRepo.deleteById(taskId);
        return "Joah hat geklappt, der Task mit Id " + taskId + " ist jetzt weg";
    }
}
