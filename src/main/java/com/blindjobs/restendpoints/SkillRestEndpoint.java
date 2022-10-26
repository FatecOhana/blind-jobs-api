package com.blindjobs.restendpoints;

import com.blindjobs.database.models.complement.Skill;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.SingleItemPayload;
import com.blindjobs.services.SkillService;
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
@Tag(name = "Skill", description = "Endpoint to manipulate and manage skills and their data")
public class SkillRestEndpoint {

    private final SkillService skillService;

    public SkillRestEndpoint(SkillService skillService) {
        this.skillService = skillService;
    }

    @Operation(summary = "Create or Update Skill values",
            description = "If you pass the ID and there is a Skill in the corresponding database, it will be updated")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Skill Created or Updated", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @PostMapping(value = "api/v1/skill", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> upsertUser(
            @RequestBody SingleItemPayload<Skill> skillPayload
    ) throws Exception {
        return new ResponseEntity<>(skillService.upsertRegister(skillPayload.getData()), HttpStatus.OK);
    }

    @Operation(summary = "Delete (Soft-Delete) one Skill", description = "You only need to enter the Skill's ID in the request body")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Skill Deleted", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @DeleteMapping(value = "api/v1/skill", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> softDeleteUser(
            @RequestBody SingleItemPayload<UUID> skillPayload
    ) throws Exception {
        return new ResponseEntity<>(skillService.softDeleteRegister(skillPayload.getData()), HttpStatus.OK);
    }

    @Operation(summary = "Get database skill values", description = "You must enter one of the filter values")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Matching  skill values", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/skill", produces = "application/json")
    public ResponseEntity<OperationData<?>> getUser(
            @RequestParam(required = false) String id, @RequestParam(required = false) String uniqueName,
            @RequestParam(required = false) String name, @RequestParam(required = false) boolean isDeleted
    ) throws Exception {
        UUID uuid = UtilsOperation.convertStringToUUID(id);
        return new ResponseEntity<>(skillService.findRegister(uuid, name, uniqueName, null, isDeleted), HttpStatus.OK);
    }

    // TODO ALLOW ONLY FOR MASTER ADMIN
    @Operation(summary = "Get all skill values in database", description = "This method is only allowed for debug")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "All Skill Registers", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/skill/all", produces = "application/json")
    public ResponseEntity<OperationData<?>> getAllUser() throws Exception {
        return new ResponseEntity<>(skillService.findAllRegister(), HttpStatus.OK);
    }

}
