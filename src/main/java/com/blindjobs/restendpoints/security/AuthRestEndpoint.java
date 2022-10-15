package com.blindjobs.restendpoints.security;

import com.blindjobs.dto.Login;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.SingleItemPayload;
import com.blindjobs.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication", description = "Endpoint to manipulate and manage login")
public class AuthRestEndpoint {

    private final AuthService authService;

    public AuthRestEndpoint(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Check credentials")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "User checked", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class))))
    @PostMapping(value = "api/v1/login", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationData<?>> genericLoginV1(@RequestBody SingleItemPayload<Login> loginPayload) throws Exception {
        return new ResponseEntity<>(authService.checkCredential(loginPayload.getData()), HttpStatus.OK);
    }

}
