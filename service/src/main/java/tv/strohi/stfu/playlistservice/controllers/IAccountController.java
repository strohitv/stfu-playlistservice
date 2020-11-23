package tv.strohi.stfu.playlistservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import tv.strohi.stfu.playlistservice.datastore.model.Account;
import tv.strohi.stfu.playlistservice.datastore.model.AuthCode;

import java.io.IOException;

@RestController
@RequestMapping("accounts")
public interface IAccountController {
    @GetMapping
    @Operation(summary = "get all registered accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "accounts could be found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Account[].class))}),
            @ApiResponse(responseCode = "403", description = "authentification is needed - check config.properties for set username and password and provide them via basic auth",
                    content = @Content)
    })
    Account[] getAllAccounts(@RequestParam(value = "channelId", required = false)
                             @Parameter(description = "only show accounts with that specific channel id")
                                     String channelId,
                             @RequestParam(value = "channelTitle", required = false)
                             @Parameter(description = "only show accounts which title contains the given String")
                                     String channelTitle);

    @GetMapping("{id}")
    @Operation(summary = "get registered account with the specified id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "account exists and could be found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))}),
            @ApiResponse(responseCode = "403", description = "authentification is needed - check config.properties for set username and password and provide them via basic auth",
                    content = @Content)
    })
    Account getAccount(@PathVariable("id") @Parameter(description = "id of the account that should be searched", required = true) long id);

    @PostMapping
    @Operation(summary = "register a new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "account was successfully registered",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))}),
            @ApiResponse(responseCode = "403", description = "authentification is needed - check config.properties for set username and password and provide them via basic auth",
                    content = @Content)
    })
    Account addAccount(@RequestBody @Parameter(description = "auth code information to register that account", required = true) AuthCode connectInformation) throws IOException;

    @DeleteMapping
    @Operation(summary = "delete all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "all accounts were deleted",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "403", description = "authentification is needed - check config.properties for set username and password and provide them via basic auth",
                    content = @Content)
    })
    String removeAllAccounts();

    @DeleteMapping("{id}")
    @Operation(summary = "delete an account with a specific id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the account with the given id was deleted",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "403", description = "authentification is needed - check config.properties for set username and password and provide them via basic auth",
                    content = @Content)
    })
    String removeAccount(@PathVariable("id") @Parameter(description = "id of the account that should be deleted", required = true) long id);
}
