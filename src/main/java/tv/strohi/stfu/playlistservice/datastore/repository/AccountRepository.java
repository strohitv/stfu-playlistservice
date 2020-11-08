package tv.strohi.stfu.playlistservice.datastore.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tv.strohi.stfu.playlistservice.datastore.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
}
