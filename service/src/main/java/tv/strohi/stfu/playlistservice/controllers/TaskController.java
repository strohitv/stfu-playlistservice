package tv.strohi.stfu.playlistservice.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tv.strohi.stfu.playlistservice.datastore.model.Account;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.model.TaskOrderField;
import tv.strohi.stfu.playlistservice.datastore.model.TaskState;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;
import tv.strohi.stfu.playlistservice.datastore.repository.TaskRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static tv.strohi.stfu.playlistservice.runnable.YoutubePlaylistAddRunnable.scheduleTask;

@RestController
@RequestMapping("accounts/{id}/tasks")
public class TaskController implements ApplicationListener<ContextRefreshedEvent>, ITaskController {
    private final Logger logger = LogManager.getLogger(TaskController.class.getCanonicalName());

    private TaskRepository taskRepo;
    private AccountRepository accountRepo;

    @Autowired
    public void setTaskRepository(TaskRepository repo) {
        logger.debug("setting task repository...");
        taskRepo = repo;
    }

    @Autowired
    public void setAccountRepository(AccountRepository repo) {
        logger.debug("setting account repository...");
        accountRepo = repo;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<Task> openTasks = taskRepo.findByState(TaskState.Open);
        logger.info("reschedule {} open tasks from database...", openTasks.size());
        for (Task task : openTasks) {
            logger.info("rescheduling task with id: {}", task.getId());
            if (task.getAccount() != null) {
                logger.debug("task data: {}", task);
                scheduleTask(task, taskRepo, accountRepo);
            } else {
                logger.warn("task with id {} did not have an account. deleting task...", task.getId());
                logger.debug("task data: {}", task);
                taskRepo.delete(task);
            }
        }
        logger.info("rescheduling done");
    }

    @Override
    @GetMapping
    public List<Task> getAllTasks(@PathVariable("id") long accountId,
                                  @RequestParam(name = "addAtBefore", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date addAtBefore,
                                  @RequestParam(name = "addAtAfter", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date addAtAfter,
                                  @RequestParam(name = "attemptCount", required = false) Integer attemptCount,
                                  @RequestParam(name = "maxAttemptCount", required = false) Integer maxAttemptCount,
                                  @RequestParam(name = "minAttemptCount", required = false) Integer minAttemptCount,
                                  @RequestParam(name = "playlistTitle", required = false) String playlistTitle,
                                  @RequestParam(name = "playlistId", required = false) String playlistId,
                                  @RequestParam(name = "videoTitle", required = false) String videoTitle,
                                  @RequestParam(name = "videoId", required = false) String videoId,
                                  @RequestParam(name = "state", required = false) TaskState state,
                                  @RequestParam(name = "orderby", required = false) TaskOrderField[] orderBy,
                                  @RequestParam(name = "direction", required = false) Sort.Direction direction
    ) {
        logger.info("get all tasks for account id {} was called", accountId);

        if (direction == null) direction = Sort.Direction.ASC;
        if (orderBy == null || orderBy.length == 0) orderBy = new TaskOrderField[]{TaskOrderField.id};

        Sort sort = Sort.by(direction, Arrays.stream(orderBy).filter(Objects::nonNull).map(Enum::name).toArray(String[]::new));
        List<Task> tasks = taskRepo.findByAccount_IdAndParams(accountId, addAtBefore, addAtAfter, attemptCount, minAttemptCount, maxAttemptCount, videoId, videoTitle, playlistId, playlistTitle, state, sort);

        logger.info("returning {} tasks", tasks.size());
        tasks.forEach(t -> logger.debug("returning task {}", t));
        return tasks;
    }

    @Override
    @PostMapping
    public Task addTask(@PathVariable("id") long id, @RequestBody Task task) {
        logger.info("create task for account id {} was called", id);

        if (task == null || task.getAddAt() == null || task.getPlaylistId() == null || task.getVideoId() == null) {
            return null;
        }

        Account account = accountRepo.findById(id).orElse(null);
        logger.debug("account to add the new task to: {}", account);

        if (account != null) {
            logger.info("account exists, adding task...");
            task.setAccount(account);

            if (task.getAddAt().toInstant().isBefore(Instant.now().plusSeconds(10))) {
                Instant oldDate = task.getAddAt().toInstant();
                Instant newDate = Instant.now().plusSeconds(10);
                logger.warn("scheduled date of the task {} was too early. Setting it to {}...", oldDate, newDate);
                task.setAddAt(Date.from(newDate));
            }

            logger.info("task to add: {}", task);
            taskRepo.save(task);

            logger.info("task stored into database, scheduling it now...");
            scheduleTask(task, taskRepo, accountRepo);
            logger.info("task scheduled");
        } else {
            // TODO: throw an exception for that specific case
            logger.error("task could not be added as an account with that id does not exist! Account id: {}", id);
        }

        return task;
    }

    @Override
    @DeleteMapping
    public String deleteAllTasks(@PathVariable("id") long id) {
        logger.info("remove all tasks of account id {} was called", id);
        List<Task> tasks = taskRepo.findByAccount_Id(id);
        logger.info("found {} tasks", tasks.size());
        tasks.forEach(t -> logger.debug("found task {}", t));
        taskRepo.deleteAll(tasks);

        logger.info("all tasks were removed");

        return "Joah hat geklappt, alle Tasks für die Id " + id + " sind jetzt weg";
    }

    @Override
    @GetMapping("tid}")
    public Task getTask(@PathVariable("id") long accountId, @PathVariable("tid") long taskId) {
        logger.info("get task with id {} for account id {} was called", taskId, accountId);
        List<Task> tasks = taskRepo.findByAccount_Id(accountId);
        logger.info("found {} tasks", tasks.size());
        tasks.forEach(t -> logger.debug("found task {}", t));
        Task task = tasks.stream().filter(t -> t.getId() == taskId).findFirst().orElse(null);
        logger.info("returning task {}", task);

        return task;
    }

    @Override
    @PutMapping("{tid}")
    public Task updateTask(@PathVariable("id") long accountId, @PathVariable("tid") long taskId, @RequestBody Task task) {
        logger.info("update task with id {} for account id {} was called", taskId, accountId);
        logger.info("update fields: {}", task);
        List<Task> tasks = taskRepo.findByAccount_Id(accountId);
        Task taskToEdit = tasks.stream().filter(t -> t.getId() == taskId).findFirst().orElse(null);
        logger.info("old task: {}", taskToEdit);

        if (taskToEdit != null) {
            logger.info("task found, updating fields...");
            taskToEdit.setVideoId(task.getVideoId());
            taskToEdit.setVideoTitle(task.getVideoTitle());
            taskToEdit.setPlaylistId(task.getPlaylistId());
            taskToEdit.setPlaylistTitle(task.getPlaylistTitle());
            taskToEdit.setAddAt(task.getAddAt());
            taskToEdit.setState(TaskState.Open);
            logger.info("update done. New task: {}", taskToEdit);

            logger.info("scheduling and saving task...");
            scheduleTask(taskToEdit, taskRepo, accountRepo);
            taskRepo.save(taskToEdit);
            logger.info("updated task saved and scheduled");
        } else {
            logger.error("task with id {} for account with id {} could not be found!", taskId, accountId);
        }

        return taskToEdit;
    }

    @Override
    @DeleteMapping("{tid}")
    public String deleteTask(@PathVariable("tid") long taskId) {
        logger.info("remove task with id {} was called", taskId);
        taskRepo.deleteById(taskId);
        return "Joah hat geklappt, der Task mit Id " + taskId + " ist jetzt weg";
    }
}
