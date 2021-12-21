package tv.strohi.stfu.playlistservice.datastore.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.model.TaskState;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByAccount_Id(Long accountId);
    List<Task> findByState(TaskState state);

    @Query("SELECT t FROM Task t WHERE " +
            "t.account.id = :accountId " +
            "AND (:ignoreTaskIds = true OR t.id IN :taskIds) " +
            "AND (:addAtBefore is null OR t.addAt <= :addAtBefore) " +
            "AND (:addAtAfter is null OR t.addAt >= :addAtAfter) " +
            "AND (:attemptCount is null OR t.attemptCount = :attemptCount) " +
            "AND (:minAttemptCount is null OR t.attemptCount >= :minAttemptCount) " +
            "AND (:maxAttemptCount is null OR t.attemptCount <= :maxAttemptCount) " +
            "AND (:videoId is null OR t.videoId = :videoId) " +
            "AND (:videoTitle is null OR t.videoTitle like %:videoTitle%) " +
            "AND (:playlistId is null OR t.playlistId = :playlistId) " +
            "AND (:playlistTitle is null OR t.playlistTitle like %:playlistTitle%) " +
            "AND (:ingoreStates = true OR t.state IN :states) ")
    List<Task> findByAccount_IdAndParams(
            @Param("accountId") Long accountId,
            @Param("taskIds") Collection<Long> taskIds,
            @Param("ignoreTaskIds") boolean ignoreTasks,
            @Param("addAtBefore") Date addAtBefore,
            @Param("addAtAfter") Date addAtAfter,
            @Param("attemptCount") Integer attemptCount,
            @Param("minAttemptCount") Integer attemptCountBefore,
            @Param("maxAttemptCount") Integer attemptCountAfter,
            @Param("videoId") String videoId,
            @Param("videoTitle") String videoTitle,
            @Param("playlistId") String playlistId,
            @Param("playlistTitle") String playlistTitle,
            @Param("states") Collection<TaskState> states,
            @Param("ingoreStates") boolean ignoreStates,
            Sort sort
    );
}
