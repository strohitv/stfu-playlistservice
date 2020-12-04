package tv.strohi.stfu.playlistservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tv.strohi.stfu.playlistservice.datastore.model.Task;
import tv.strohi.stfu.playlistservice.datastore.model.TaskOrderField;
import tv.strohi.stfu.playlistservice.datastore.model.TaskState;

import java.util.Date;
import java.util.List;

public interface ITaskController {
    @GetMapping
    @Operation(summary = "get a filtered list of tasks of the specified account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "tasks could be found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Task[].class))}),
            @ApiResponse(responseCode = "403", description = "authentification is needed - check config.properties for set username and password and provide them via basic auth",
                    content = @Content)
    })
    List<Task> getAllTasks(@PathVariable("id") @Parameter(description = "id of the account to search for tasks", required = true) long accountId,
                           @RequestParam(name = "id", required = false) @Parameter(description = "list of ids that should be searched") Long[] taskIds,
                           @RequestParam(name = "addAtBefore", required = false) @Parameter(description = "latest planned date of the tasks to search") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date addAtBefore,
                           @RequestParam(name = "addAtAfter", required = false) @Parameter(description = "earliest planned date of the tasks to search") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date addAtAfter,
                           @RequestParam(name = "attemptCount", required = false) @Parameter(description = "exact count of attempts of the tasks to search") Integer attemptCount,
                           @RequestParam(name = "maxAttemptCount", required = false) @Parameter(description = "maximum count of attempts of the tasks to search") Integer maxAttemptCount,
                           @RequestParam(name = "minAttemptCount", required = false) @Parameter(description = "minimum count of attempts of the tasks to search") Integer minAttemptCount,
                           @RequestParam(name = "playlistTitle", required = false) @Parameter(description = "filter tasks which are for a playlist with its title containing the given words") String playlistTitle,
                           @RequestParam(name = "playlistId", required = false) @Parameter(description = "filter tasks which are for a playlist with the given playlist id") String playlistId,
                           @RequestParam(name = "videoTitle", required = false) @Parameter(description = "filter tasks which are for a video with its title containing the given words") String videoTitle,
                           @RequestParam(name = "videoId", required = false) @Parameter(description = "filter tasks which are for a video with the given video id") String videoId,
                           @RequestParam(name = "state", required = false) @Parameter(description = "filter tasks which have the given state") TaskState[] state,
                           @RequestParam(name = "orderby", required = false) @Parameter(description = "order returned tasks by the given criteria") TaskOrderField[] orderBy,
                           @RequestParam(name = "direction", required = false) @Parameter(description = "order returned tasks ascending or descending") Sort.Direction direction
    );

    @GetMapping("tid}")
    @Operation(summary = "get the task with the specified id of the specified account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "task could be found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "403", description = "authentification is needed - check config.properties for set username and password and provide them via basic auth",
                    content = @Content)
    })
    Task getTask(@PathVariable("id") @Parameter(description = "id of the account to search for tasks", required = true) long accountId,
                 @PathVariable("tid") @Parameter(description = "id of the task", required = true) long taskId);

    @PostMapping
    @Operation(summary = "create a task for the specified account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "task could be found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "403", description = "authentification is needed - check config.properties for set username and password and provide them via basic auth",
                    content = @Content)
    })
    Task addTask(@PathVariable("id") @Parameter(description = "id of the account to search for tasks", required = true) long id,
                 @RequestBody @Parameter(description = "Task that should be created", required = true) Task task);

    @PutMapping("{tid}")
    @Operation(summary = "update the task with the specified id of the specified account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "task could be updated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "403", description = "authentification is needed - check config.properties for set username and password and provide them via basic auth",
                    content = @Content)
    })
    Task updateTask(@PathVariable("id") @Parameter(description = "id of the account to search for tasks", required = true) long accountId,
                    @PathVariable("tid") @Parameter(description = "id of the task", required = true) long taskId,
                    @RequestBody @Parameter(description = "new data for the task", required = true) Task task);

    @DeleteMapping("{tid}")
    @Operation(summary = "deleted the task with the specified id of the specified account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "task could be delete",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "403", description = "authentification is needed - check config.properties for set username and password and provide them via basic auth",
                    content = @Content)
    })
    String deleteTask(@PathVariable("tid") @Parameter(description = "id of the task", required = true) long taskId);

    @DeleteMapping
    @Operation(summary = "deleted all tasks of the specified account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "tasks could be delete",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "403", description = "authentification is needed - check config.properties for set username and password and provide them via basic auth",
                    content = @Content)
    })
    String deleteAllTasks(@PathVariable("id") @Parameter(description = "id of the account to delete all tasks from", required = true) long id);
}
