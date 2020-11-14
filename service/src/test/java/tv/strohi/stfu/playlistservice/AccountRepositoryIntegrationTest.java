package tv.strohi.stfu.playlistservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tv.strohi.stfu.playlistservice.datastore.model.Account;
import tv.strohi.stfu.playlistservice.datastore.repository.AccountRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AccountRepositoryIntegrationTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void TestAccountRepositoryExists() {
        accountRepository.save(new Account());
        List<Account> users = (List<Account>) accountRepository.findAll();

        assertEquals(users.size(), 1);
    }
}
