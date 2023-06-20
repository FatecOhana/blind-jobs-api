package com.blindjobs.restendpoints;

import com.blindjobs.database.models.entities.User;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.SingleItemPayload;
import com.blindjobs.dto.command.DeleteItemCommand;
import com.blindjobs.dto.command.FindItemByParameterCommand;
import com.blindjobs.dto.command.UpsertItemCommand;
import com.blindjobs.dto.types.UserType;
import com.blindjobs.services.UserServiceV2;
import com.blindjobs.utils.UtilsOperation;
import com.blindjobs.utils.UtilsValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "User", description = "Endpoint to manipulate and manage users and their data")
public class UserRestEndpointV2 {

    private final UserServiceV2 userService;

    public UserRestEndpointV2(UserServiceV2 userService) {
        this.userService = userService;
    }

    // TODO: Create example object not in hardcode
    @Operation(summary = "Create or Update Users values",
            description = "If you pass the ID and there is a Users in the corresponding database, it will be updated",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {
                            @ExampleObject(
                                    name = "Student body",
                                    description = "student body example to upsert register",
                                    value = """
                                            {
                                                "data": {
                                                    "name": "Gabriel Olae",
                                                    "identifierName": "GAB_OL4e",
                                                    "isDeleted": false,
                                                    "userType": "ESTUDANTE",
                                                    "contactNumber": "115984928470",
                                                    "email": "gabriel@email.com",
                                                    "password": "gabriel852109712",
                                                    "documentType": "CPF",
                                                    "documentValue": "12346578901"
                                                }
                                            }"""
                            ),
                            @ExampleObject(
                                    name = "Enterprise body",
                                    description = "enterprise body example to upsert register",
                                    value = """
                                            {
                                                "data": {
                                                    "name": "Nest",
                                                    "identifierName": "NEST_ENTERPRISE",
                                                    "isDeleted": false,
                                                    "userType": "EMPRESA",
                                                    "contactNumber": "119284928470",
                                                    "contactEmail": "nestRecruiter@email.com",
                                                    "email": "nestCorp@email.com",
                                                    "password": "nest852109712",
                                                    "documentType": "CNPJ",
                                                    "documentValue": "123465789010001"
                                                }
                                            }"""
                            )
                    }
            ))
    )
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Users Created or Updated", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @PostMapping(value = "api/v1/user", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> upsertUser(
            @RequestBody SingleItemPayload<User> userPayload
    ) throws Exception {
        UpsertItemCommand<User> command = UpsertItemCommand.<User>builder().data(userPayload).build();
        return new ResponseEntity<>(userService.upsertRegister(command), HttpStatus.OK);
    }

    @Operation(summary = "Delete (Soft-Delete) one Users", description = "You only need to enter the Users's ID in the request body")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Users Deleted", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @DeleteMapping(value = "api/v1/user", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> softDeleteUser(
            @RequestBody SingleItemPayload<UUID> userPayload
    ) throws Exception {
        DeleteItemCommand command = DeleteItemCommand.builder().id(UtilsValidation.ifNull(userPayload, new SingleItemPayload<UUID>()).getData()).build();
        return new ResponseEntity<>(userService.softDeleteRegister(command), HttpStatus.OK);
    }

    @Operation(summary = "Get database user values", description = "You must enter one of the filter values")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Matching  user values", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/user", produces = "application/json")
    public ResponseEntity<OperationData<?>> getUser(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String uniqueName,
            @RequestParam(required = false) String name,
            @Parameter(description = "UserType value of users that will be searched", name = "userType",
                    schema = @Schema(implementation = UserType.class)) @RequestParam UserType userType,
            @RequestParam(required = false) boolean isDeleted
    ) throws Exception {
        UUID uuid = UtilsOperation.convertStringToUUID(id);
        FindItemByParameterCommand find = FindItemByParameterCommand.builder()
                .id(uuid).name(name).uniqueKey(uniqueName).type(userType).isDeleted(isDeleted)
                .build();
        return new ResponseEntity<>(userService.findRegister(find), HttpStatus.OK);
    }

    // TODO ALLOW ONLY FOR MASTER ADMIN
    @Operation(summary = "Get all user values in database", description = "This method is only allowed for debug")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "All Users Registers", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/user/all", produces = "application/json")
    public ResponseEntity<OperationData<?>> getAllUser() throws Exception {
        return new ResponseEntity<>(userService.findAllRegister(), HttpStatus.OK);
    }

    @Operation(summary = "Get all User Jobs", description = "if UserType is EMPRESA, all candidatures for the company " +
            "will be displayed. But if UserType is ESTUDANTE, all candidatures of student will be displayed")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "All User candidatures", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/user/job", produces = "application/json")
    public ResponseEntity<OperationData<?>> getAllUserCandidatures(
            @RequestParam(required = false) String id, @RequestParam(required = false) String uniqueName,
            @RequestParam(required = false) String name, @RequestParam(required = false) boolean isDeleted,
            @Parameter(name = "userType", required = true, schema = @Schema(implementation = UserType.class),
                    description = "UserType value of users that will be searched") @RequestParam UserType userType
    ) throws Exception {
        throw new NotImplementedException("Not implemented getAllUserCandidatures");
    }

}
