package com.blindjobs.restendpoints;

import com.blindjobs.database.models.entities.Job;
import com.blindjobs.dto.CandidaturePayload;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.Payload;
import com.blindjobs.dto.SingleItemPayload;
import com.blindjobs.dto.types.UserType;
import com.blindjobs.services.JobService;
import com.blindjobs.utils.UtilsOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@Tag(name = "Job", description = "Endpoint to manipulate and manage companies and their data")
public class JobRestEndpoint {

    private final JobService jobService;

    public JobRestEndpoint(JobService jobService) {
        this.jobService = jobService;
    }

    @Operation(summary = "Create or Update Job values",
            description = "If you pass the ID and there is a Job in the corresponding database, it will be updated")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Job Created or Updated", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @PostMapping(value = "api/v1/job", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> upsertJob(@RequestBody SingleItemPayload<Job> jobPayload) throws Exception {
        return new ResponseEntity<>(jobService.upsertRegister(jobPayload.getData()), HttpStatus.OK);
    }

    @Operation(summary = "Create or Update Many Job values",
            description = "If you pass the ID and there is a Job in the corresponding database, it will be updated")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Jobs Created or Updated", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @PostMapping(value = "api/v1/job/bulkOperation", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> upsertJobs(@RequestBody Payload<Job> jobPayload) throws Exception {
        return new ResponseEntity<>(jobService.upsertRegisters(jobPayload.getData()), HttpStatus.OK);
    }

    @Operation(summary = "Delete (Soft-Delete) one Job", description = "You only need to enter the Job's ID in the request body")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Job Deleted", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @DeleteMapping(value = "api/v1/job", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> softDeleteJob(@RequestBody SingleItemPayload<UUID> jobPayload) throws Exception {
        return new ResponseEntity<>(jobService.softDeleteRegister(jobPayload.getData()), HttpStatus.OK);
    }

    @Operation(summary = "Delete (Soft-Delete) many Jobs", description = "You only need to enter the Jobs IDs in the request body")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Jobs Deleted", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @DeleteMapping(value = "api/v1/job/bulkOperation", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> softDeleteJobs(@RequestBody Payload<UUID> jobPayload) throws Exception {
        return new ResponseEntity<>(jobService.softDeleteRegisters(jobPayload.getData()), HttpStatus.OK);
    }

    @Operation(summary = "Get database job values", description = "You must enter one of the filter values")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Matching job values", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/job", produces = "application/json")
    public ResponseEntity<OperationData<?>> getJob(@RequestParam(required = false) String id,
                                                   @RequestParam(required = false) String uniqueName,
                                                   @RequestParam(required = false) String name,
                                                   @RequestParam(required = false) boolean isDeleted) throws Exception {
        UUID uuid = UtilsOperation.convertStringToUUID(id);
        return new ResponseEntity<>(jobService.findRegister(uuid, name, uniqueName, null, isDeleted), HttpStatus.OK);
    }

    @Operation(summary = "Get database job values using many matches", description = "You must enter one or many values in filter")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Matching job values", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/job/manyMatch", produces = "application/json")
    public ResponseEntity<OperationData<?>> getJobWithManyMatch(@RequestParam(required = false) Set<String> id,
                                                                @RequestParam(required = false) Set<String> uniqueName,
                                                                @RequestParam(required = false) Set<String> name,
                                                                @RequestParam(required = false) boolean isDeleted) throws Exception {
        Set<UUID> uuids = UtilsOperation.convertStringsToUUIDs(id);
        return new ResponseEntity<>(jobService.findManyMatchRegisters(uuids, name, uniqueName, null, isDeleted), HttpStatus.OK);
    }

    @Operation(summary = "Get all job values in database", description = "This method is only allowed for debug")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "All Job Registers", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/job/all", produces = "application/json")
    public ResponseEntity<OperationData<?>> getAllJob() throws Exception {
        return new ResponseEntity<>(jobService.findAllRegister(), HttpStatus.OK);
    }

    @Operation(summary = "Candidate User in Job",
            description = "Is necessary pass the User and Job ID")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "User candidate in Job", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @PostMapping(value = "api/v1/job/candidate", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> candidateInJob(@RequestBody SingleItemPayload<CandidaturePayload> candidaturePayload)
            throws Exception {
        return new ResponseEntity<>(jobService.candidateUserInJob(candidaturePayload.getData()), HttpStatus.OK);
    }

}
