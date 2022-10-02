package com.herokuapp.blindjobs.restendpoints.config;

import com.herokuapp.blindjobs.dto.OperationData;
import com.herokuapp.blindjobs.dto.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @ApiResponse(responseCode = "404", description = "Not Found Value in Database or in Resources", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class)
    ))
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<OperationData<?>> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(new OperationData<>(ex), HttpStatus.NOT_FOUND);
    }

    @ApiResponse(responseCode = "500", description = "Generic Exception happen in API", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class)
    ))
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<OperationData<?>> handleInternalException(NotFoundException ex) {
        return new ResponseEntity<>(new OperationData<>(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}