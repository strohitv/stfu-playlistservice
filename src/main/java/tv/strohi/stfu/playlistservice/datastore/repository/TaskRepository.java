package tv.strohi.stfu.playlistservice.datastore.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.model.TaskState;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByAccount_Id(Long accountId);
    List<Task> findByState(TaskState state);
}
