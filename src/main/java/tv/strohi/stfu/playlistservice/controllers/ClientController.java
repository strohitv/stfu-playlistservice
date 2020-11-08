package tv.strohi.stfu.playlistservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tv.strohi.stfu.playlistservice.datastore.model.AuthCode;
import tv.strohi.stfu.playlistservice.datastore.model.Client;
import tv.strohi.stfu.playlistservice.datastore.repository.ClientRepository;
import tv.strohi.stfu.playlistservice.youtube.AccountConnector;

import java.io.IOException;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("clients")
public class ClientController {
    private ClientRepository clientRepository;

    @Autowired
    public void setClientRepository(ClientRepository repo) {
        clientRepository = repo;
    }

    @GetMapping
    public Client[] getAllClients() {
        return StreamSupport.stream(clientRepository.findAll().spliterator(), false).toArray(Client[]::new);
    }

    @GetMapping("{id}")
    public Client getClient(@PathVariable("id") long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Client addClient(@RequestBody AuthCode connectInformation) throws IOException {
        return new AccountConnector(clientRepository).connectAccount(connectInformation);
    }

    @DeleteMapping
    public String removeAllClients() {
        clientRepository.deleteAll();
        return "Alle Clients wurden gelöscht.";
    }

    @DeleteMapping("{id}")
    public String removeClient(@PathVariable("id") long id) {
        clientRepository.deleteById(id);
        return String.format("Client wurde gelöscht. %s Elemente befinden sich im Repo.", StreamSupport.stream(clientRepository.findAll().spliterator(), false).count());
    }
}
