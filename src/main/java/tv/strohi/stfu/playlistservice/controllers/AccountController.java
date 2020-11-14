package tv.strohi.stfu.playlistservice.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tv.strohi.stfu.playlistservice.datastore.model.Account;
import tv.strohi.stfu.playlistservice.datastore.model.AuthCode;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;
import tv.strohi.stfu.playlistservice.youtube.AccountConnector;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("accounts")
public class AccountController {
    private final Logger logger = LogManager.getLogger(AccountController.class.getCanonicalName());
    private AccountRepository accountRepository;

    @Autowired
    public void setAccountRepository(AccountRepository repo) {
        logger.debug("setting account repository...");
        accountRepository = repo;
    }

    @GetMapping
    public Account[] getAllAccounts(@RequestParam(value = "channelId", required = false) String channelId, @RequestParam(value = "channelTitle", required = false) String channelTitle) {
        logger.info("get all accounts was called");
        Account[] accounts = accountRepository.findByChannelIdAndTitle(channelId, channelTitle).toArray(Account[]::new);
        logger.info("returning {} accounts", accounts.length);
        Arrays.stream(accounts).forEach(a -> logger.debug("returning account {}", a));
        return accounts;
    }

    @GetMapping("{id}")
    public Account getAccount(@PathVariable("id") long id) {
        logger.info("get account with id {} was called", id);
        Account account = accountRepository.findById(id).orElse(null);
        logger.info("does this account exist? answer: {}", account != null);
        logger.debug("returning account {}", account);
        return account;
    }

    @PostMapping
    public Account addAccount(@RequestBody AuthCode connectInformation) throws IOException {
        logger.info("create account was called");
        logger.debug("connect infomation: {}", connectInformation.toString());
        return new AccountConnector(accountRepository).connectAccount(connectInformation);
    }

    @DeleteMapping
    public String removeAllAccounts() {
        logger.info("remove all accounts was called");
        accountRepository.deleteAll();
        return "Alle Accounts wurden gelöscht.";
    }

    @DeleteMapping("{id}")
    public String removeAccount(@PathVariable("id") long id) {
        logger.info("remove account with id {} was called", id);
        accountRepository.deleteById(id);
        return String.format("Account wurde gelöscht. %s Elemente befinden sich im Repo.", StreamSupport.stream(accountRepository.findAll().spliterator(), false).count());
    }
}
