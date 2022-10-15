package com.blindjobs.restendpoints;

import com.blindjobs.database.models.entities.Student;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.SingleItemPayload;
import com.blindjobs.services.StudentService;
import com.blindjobs.utils.UtilsOperation;
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
@Tag(name = "Student", description = "Endpoint to manipulate and manage students and their data")
public class StudentRestEndpoint {

    private final StudentService userService;

    public StudentRestEndpoint(StudentService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create or Update Student values",
            description = "If you pass the ID and there is a Student in the corresponding database, it will be updated")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Student Created or Updated", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @PostMapping(value = "api/v1/student", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> upsertUser(@RequestBody SingleItemPayload<Student> userPayload) throws Exception {
        return new ResponseEntity<>(userService.upsertRegister(userPayload.getData()), HttpStatus.OK);
    }

    @Operation(summary = "Delete (Soft-Delete) one Student", description = "You only need to enter the Student's ID in the request body")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Student Deleted", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @DeleteMapping(value = "api/v1/student", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> softDeleteUser(@RequestBody SingleItemPayload<UUID> userPayload) throws Exception {
        return new ResponseEntity<>(userService.softDeleteRegister(userPayload.getData()), HttpStatus.OK);
    }

    @Operation(summary = "Get database student values", description = "You must enter one of the filter values")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Matching  student values", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/student", produces = "application/json")
    public ResponseEntity<OperationData<?>> getUser(@RequestParam(required = false) String id,
                                                    @RequestParam(required = false) String uniqueName,
                                                    @RequestParam(required = false) String name,
                                                    @RequestParam(required = false) boolean isDeleted) throws Exception {
        UUID uuid = UtilsOperation.convertStringToUUID(id);
        return new ResponseEntity<>(userService.findRegister(uuid, name, uniqueName, isDeleted), HttpStatus.OK);
    }

    // TODO ALLOW ONLY FOR MASTER ADMIN
    @Operation(summary = "Get all student values in database", description = "This method is only allowed for debug")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "All Student Registers", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/student/all", produces = "application/json")
    public ResponseEntity<OperationData<?>> getAllUser() {
        return new ResponseEntity<>(userService.findAllRegister(), HttpStatus.OK);
    }

}
