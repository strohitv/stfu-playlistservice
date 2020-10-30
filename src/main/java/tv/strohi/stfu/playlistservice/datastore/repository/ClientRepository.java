package tv.strohi.stfu.playlistservice.datastore.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tv.strohi.stfu.playlistservice.datastore.model.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
}
