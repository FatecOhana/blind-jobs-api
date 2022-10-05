package com.blindjobs.restendpoints;

import com.blindjobs.database.models.UserModel;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.SingleItemPayload;
import com.blindjobs.services.UserService;
import com.blindjobs.services.utils.UtilsValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "User", description = "Endpoint to manipulate and manage users and their data")
public class UserRestEndpoint {

    private final UserService userService;

    public UserRestEndpoint(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create or Update User values")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "User Created or Updated", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))
    ))
    @PostMapping(value = "api/v1/user", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> upsertUser(@RequestBody SingleItemPayload<UserModel> userPayload) throws Exception {
        return new ResponseEntity<>(userService.upsertRegister(userPayload.getData()), HttpStatus.OK);
    }

    @Operation(summary = "Delete (Soft-Delete) one User", description = "You only need to enter the User's ID in the request body")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "User Deleted", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))
    ))
    @DeleteMapping(value = "api/v1/user", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> softDeleteUser(@RequestBody SingleItemPayload<UUID> userPayload) throws Exception {
        return new ResponseEntity<>(userService.softDeleteRegister(userPayload.getData()), HttpStatus.OK);
    }

    @Operation(summary = "Get database user values", description = "You must enter one of the filter values")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Matching  user values", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))
    ))
    @GetMapping(value = "api/v1/user", produces = "application/json")
    public ResponseEntity<OperationData<?>> getUser(@RequestParam(required = false) String id,
                                                    @RequestParam(required = false) String username,
                                                    @RequestParam(required = false) String name,
                                                    @RequestParam(required = false) boolean isDeleted) throws Exception {
        UUID uuid = UtilsValidation.isNullOrEmpty(id) ? null : UUID.fromString(id);
        return new ResponseEntity<>(userService.findRegister(uuid, name, username, isDeleted), HttpStatus.OK);
    }

    // TODO ALLOW ONLY FOR MASTER ADMIN
    @Operation(summary = "Get all user values in database", description = "This method is only allowed for debug")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "All User Registers", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))
    ))
    @GetMapping(value = "api/v1/user/all", produces = "application/json")
    public ResponseEntity<OperationData<?>> getAllUser() {
        return new ResponseEntity<>(userService.findAllRegister(), HttpStatus.OK);
    }

}
