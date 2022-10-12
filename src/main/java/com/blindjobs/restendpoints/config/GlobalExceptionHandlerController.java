package com.blindjobs.restendpoints.config;

import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlerController.class);

    @ApiResponse(responseCode = "404", description = "Not Found Value in Database or in Resources", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class)
    ))
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<OperationData<?>> handleNotFoundException(NotFoundException ex) {
        logger.error("an exception occurred", ex);
        return new ResponseEntity<>(new OperationData<>(ex), HttpStatus.NOT_FOUND);
    }

    @ApiResponse(responseCode = "500", description = "Generic Exception happen in API", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class)
    ))
    @ExceptionHandler(Exception.class)
    public ResponseEntity<OperationData<?>> handleInternalException(Exception ex) {
        logger.error("an exception occurred", ex);
        return new ResponseEntity<>(new OperationData<>(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiResponse(responseCode = "500", description = "Exception when manage Database values", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class)
    ))
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<OperationData<?>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.error("an exception occurred", ex);
        return new ResponseEntity<>(new OperationData<>(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiResponse(responseCode = "500", description = "Exception when read values recived", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationData.class)
    ))
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<OperationData<?>> handleDataIntegrityViolationException(HttpMessageNotReadableException ex) {
        logger.error("an exception occurred", ex);
        return new ResponseEntity<>(new OperationData<>(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}