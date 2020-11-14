package tv.strohi.stfu.playlistservice.datastore.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tv.strohi.stfu.playlistservice.datastore.model.Account;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE (:channelId is null OR a.channelId = :channelId) and (:channelTitle is null OR a.title like %:channelTitle%)")
    List<Account> findByChannelIdAndTitle(@Param("channelId")String channelId, @Param("channelTitle")String channelTitle);
}
