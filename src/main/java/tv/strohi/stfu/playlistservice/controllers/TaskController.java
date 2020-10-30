package tv.strohi.stfu.playlistservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tv.strohi.stfu.playlistservice.datastore.model.Client;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.repository.ClientRepository;
import tv.strohi.stfu.playlistservice.datastore.repository.TaskRepository;

import java.util.List;

@RestController
@RequestMapping("clients/{id}/tasks")
public class TaskController {
    private TaskRepository taskRepo;
    private ClientRepository clientRepo;

    @Autowired
    public void getTaskRepository(TaskRepository repo) {
        taskRepo = repo;
    }

    @Autowired
    public void getClientRepository(ClientRepository repo) {
        clientRepo = repo;
    }

    @GetMapping
    public List<Task> getAllTasks(@PathVariable("id") long id) {
        List<Task> tasks = taskRepo.findByClient_Id(id);
        return tasks;
    }

    @PostMapping
    public String addTask(@PathVariable("id") long id, @RequestBody Task task) {
        Client client = clientRepo.findById(id).orElse(null);

        if (client != null) {
            task.setClient(client);
            taskRepo.save(task);
        }

        return "Jaja, hat schon geklappt ganz bestimmt";
    }
}
