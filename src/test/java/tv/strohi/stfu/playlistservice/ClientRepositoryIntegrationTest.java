package tv.strohi.stfu.playlistservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tv.strohi.stfu.playlistservice.datastore.model.Client;
import tv.strohi.stfu.playlistservice.datastore.repository.ClientRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ClientRepositoryIntegrationTest {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void TestClientRepositoryExists() {
        clientRepository.save(new Client());
        List<Client> users = (List<Client>) clientRepository.findAll();

        assertEquals(users.size(), 1);
    }
}
