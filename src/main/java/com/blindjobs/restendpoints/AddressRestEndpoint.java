package com.blindjobs.restendpoints;

import com.blindjobs.database.models.complement.Address;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.SingleItemPayload;
import com.blindjobs.services.AddressService;
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
@Tag(name = "Address", description = "Endpoint to manipulate and manage addresss and their data")
public class AddressRestEndpoint {

    private final AddressService addressService;

    public AddressRestEndpoint(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(summary = "Create or Update Address values",
            description = "If you pass the ID and there is a Address in the corresponding database, it will be updated")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Address Created or Updated", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @PostMapping(value = "api/v1/address", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> upsertUser(@RequestBody SingleItemPayload<Address> addressPayload) throws Exception {
        return new ResponseEntity<>(addressService.upsertRegister(addressPayload.getData()), HttpStatus.OK);
    }

    @Operation(summary = "Delete (Soft-Delete) one Address", description = "You only need to enter the Address's ID in the request body")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Address Deleted", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @DeleteMapping(value = "api/v1/address", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> softDeleteUser(@RequestBody SingleItemPayload<UUID> addressPayload) throws Exception {
        return new ResponseEntity<>(addressService.softDeleteRegister(addressPayload.getData()), HttpStatus.OK);
    }

    @Operation(summary = "Get database address values", description = "You must enter one of the filter values")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Matching  address values", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/address", produces = "application/json")
    public ResponseEntity<OperationData<?>> getUser(@RequestParam(required = false) String id,
                                                    @RequestParam(required = false) String uniqueName,
                                                    @RequestParam(required = false) String name,
                                                    @RequestParam(required = false) boolean isDeleted) throws Exception {
        UUID uuid = UtilsValidation.isNullOrEmpty(id) ? null : UUID.fromString(id);
        return new ResponseEntity<>(addressService.findRegister(uuid, name, uniqueName, isDeleted), HttpStatus.OK);
    }

    // TODO ALLOW ONLY FOR MASTER ADMIN
    @Operation(summary = "Get all address values in database", description = "This method is only allowed for debug")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "All Address Registers", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @GetMapping(value = "api/v1/address/all", produces = "application/json")
    public ResponseEntity<OperationData<?>> getAllUser() {
        return new ResponseEntity<>(addressService.findAllRegister(), HttpStatus.OK);
    }

}
