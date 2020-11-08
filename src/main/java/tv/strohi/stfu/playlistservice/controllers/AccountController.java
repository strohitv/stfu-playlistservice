package tv.strohi.stfu.playlistservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tv.strohi.stfu.playlistservice.datastore.model.AuthCode;
import tv.strohi.stfu.playlistservice.datastore.model.Account;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;
import tv.strohi.stfu.playlistservice.youtube.AccountConnector;

import java.io.IOException;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("accounts")
public class AccountController {
    private AccountRepository accountRepository;

    @Autowired
    public void setAccountRepository(AccountRepository repo) {
        accountRepository = repo;
    }

    @GetMapping
    public Account[] getAllAccounts() {
        return StreamSupport.stream(accountRepository.findAll().spliterator(), false).toArray(Account[]::new);
    }

    @GetMapping("{id}")
    public Account getAccount(@PathVariable("id") long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Account addAccount(@RequestBody AuthCode connectInformation) throws IOException {
        return new AccountConnector(accountRepository).connectAccount(connectInformation);
    }

    @DeleteMapping
    public String removeAllAccounts() {
        accountRepository.deleteAll();
        return "Alle Accounts wurden gelöscht.";
    }

    @DeleteMapping("{id}")
    public String removeAccount(@PathVariable("id") long id) {
        accountRepository.deleteById(id);
        return String.format("Account wurde gelöscht. %s Elemente befinden sich im Repo.", StreamSupport.stream(accountRepository.findAll().spliterator(), false).count());
    }
}
