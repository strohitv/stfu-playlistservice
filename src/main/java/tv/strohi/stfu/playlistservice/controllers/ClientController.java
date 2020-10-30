package tv.strohi.stfu.playlistservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tv.strohi.stfu.playlistservice.datastore.model.Client;
import tv.strohi.stfu.playlistservice.datastore.repository.ClientRepository;

import java.util.stream.StreamSupport;

@RestController
@RequestMapping("clients")
public class ClientController {
    private ClientRepository clientRepo;

    @Autowired
    public void setClientRepo(ClientRepository repo) {
        clientRepo = repo;
    }

    @GetMapping
    public Client[] getAllClients() {
        return StreamSupport.stream(clientRepo.findAll().spliterator(), false).toArray(Client[]::new);
    }

    @GetMapping("{id}")
    public Client getClient(@PathVariable("id") long id) {
        return clientRepo.findById(id).orElse(null);
    }

    @PostMapping
    public Client addClient(@RequestBody Client newClient) {
        clientRepo.save(newClient);
        return newClient;
    }

    @DeleteMapping
    public String removeAllClients() {
        clientRepo.deleteAll();
        return "Alle Clients wurden gelöscht.";
    }

    @DeleteMapping("{id}")
    public String removeClient(@PathVariable("id") long id) {
        clientRepo.deleteById(id);
        return String.format("Client wurde gelöscht. %s Elemente befinden sich im Repo.", StreamSupport.stream(clientRepo.findAll().spliterator(), false).count());
    }
}
