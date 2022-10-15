package com.blindjobs.restendpoints;

import com.blindjobs.database.models.entities.User;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.SingleItemPayload;
import com.blindjobs.services.EnterpriseService;
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
@Tag(name = "Enterprise", description = "Endpoint to manipulate and manage companies and their data")
public class EnterpriseRestEndpoint {

    private final EnterpriseService enterpriseService;

    public EnterpriseRestEndpoint(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    @Operation(summary = "Create or Update Company values",
            description = "If you pass the ID and there is a Company in the corresponding database, it will be updated")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Company Created or Updated", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @PostMapping(value = "api/v1/enterprise", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> upsertCompany(@RequestBody SingleItemPayload<User> enterprisePayload) throws Exception {
        return new ResponseEntity<>(enterpriseService.upsertRegister(enterprisePayload.getData()), HttpStatus.OK);
    }

    @Operation(summary = "Delete (Soft-Delete) one Company", description = "You only need to enter the Company's ID in the request body")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Company Deleted", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @DeleteMapping(value = "api/v1/enterprise", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> softDeleteCompany(@RequestBody SingleItemPayload<UUID> enterprisePayload) throws Exception {
        return new ResponseEntity<>(enterpriseService.softDeleteRegister(enterprisePayload.getData()), HttpStatus.OK);
    }

    @Operation(summary = "Get database enterprise values", description = "You must enter one of the filter values")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Matching  enterprise values", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/enterprise", produces = "application/json")
    public ResponseEntity<OperationData<?>> getCompany(@RequestParam(required = false) String id,
                                                       @RequestParam(required = false) String uniqueName,
                                                       @RequestParam(required = false) String name,
                                                       @RequestParam(required = false) boolean isDeleted) throws Exception {
        UUID uuid = UtilsOperation.convertStringToUUID(id);
        return new ResponseEntity<>(enterpriseService.findRegister(uuid, name, uniqueName, isDeleted), HttpStatus.OK);
    }

    @Operation(summary = "Get all enterprise values in database", description = "This method is only allowed for debug")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "All Company Registers", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/enterprise/all", produces = "application/json")
    public ResponseEntity<OperationData<?>> getAllCompany() {
        return new ResponseEntity<>(enterpriseService.findAllRegister(), HttpStatus.OK);
    }

}
