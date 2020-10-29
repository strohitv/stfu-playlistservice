package tv.strohi.stfu.playlistservice.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tv.strohi.stfu.playlistservice.data.model.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
}
