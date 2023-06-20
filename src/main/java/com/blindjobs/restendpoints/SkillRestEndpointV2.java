package com.blindjobs.restendpoints;

import com.blindjobs.database.models.complement.Skill;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.SingleItemPayload;
import com.blindjobs.dto.command.DeleteItemCommand;
import com.blindjobs.dto.command.FindItemByParameterCommand;
import com.blindjobs.dto.command.UpsertItemCommand;
import com.blindjobs.services.SkillServiceV2;
import com.blindjobs.utils.UtilsOperation;
import com.blindjobs.utils.UtilsValidation;
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
public class SkillRestEndpointV2 {

    private final SkillServiceV2 skillService;

    public SkillRestEndpointV2(SkillServiceV2 skillService) {
        this.skillService = skillService;
    }

    @Operation(summary = "Create or Update Skill values",
            description = "If you pass the ID and there is a Skill in the corresponding database, it will be updated")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Skill Created or Updated",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @PostMapping(value = "api/v1/skill", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> upsertUser(
            @RequestBody SingleItemPayload<Skill> skillPayload
    ) throws Exception {
        UpsertItemCommand<Skill> command = UpsertItemCommand.<Skill>builder().data(skillPayload).build();
        return new ResponseEntity<>(skillService.upsertRegister(command), HttpStatus.OK);
    }

    @Operation(summary = "Delete (Soft-Delete) one Skill",
            description = "You only need to enter the Skill's ID in the request body")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Skill Deleted",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @DeleteMapping(value = "api/v1/skill", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> softDeleteUser(
            @RequestBody SingleItemPayload<UUID> skillPayload
    ) throws Exception {
        DeleteItemCommand command = DeleteItemCommand.builder().id(UtilsValidation.ifNull(skillPayload,
                new SingleItemPayload<UUID>()).getData()).build();
        return new ResponseEntity<>(skillService.softDeleteRegister(command), HttpStatus.OK);
    }

    @Operation(summary = "Get database skill values", description = "You must enter one of the filter values")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Matching  skill values",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/skill", produces = "application/json")
    public ResponseEntity<OperationData<?>> getUser(
            @RequestParam(required = false) String id, @RequestParam(required = false) String uniqueName,
            @RequestParam(required = false) String name, @RequestParam(required = false) boolean isDeleted
    ) throws Exception {
        UUID uuid = UtilsOperation.convertStringToUUID(id);
        FindItemByParameterCommand find = FindItemByParameterCommand.builder().id(uuid)
                .name(name).uniqueKey(uniqueName).isDeleted(isDeleted).build();
        return new ResponseEntity<>(skillService.findRegister(find), HttpStatus.OK);
    }

    // TODO ALLOW ONLY FOR MASTER ADMIN
    @Operation(summary = "Get all skill values in database", description = "This method is only allowed for debug")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "All Skill Registers",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/skill/all", produces = "application/json")
    public ResponseEntity<OperationData<?>> getAllUser() throws Exception {
        return new ResponseEntity<>(skillService.findAllRegister(), HttpStatus.OK);
    }

}
