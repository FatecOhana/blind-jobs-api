package com.herokuapp.blindjobs.restendpoints;

import com.herokuapp.blindjobs.database.models.UserModel;
import com.herokuapp.blindjobs.dto.OperationData;
import com.herokuapp.blindjobs.dto.SingleItemPayload;
import com.herokuapp.blindjobs.services.UserService;
import com.herokuapp.blindjobs.services.utils.UtilsValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserRestEndpoint {

    private final UserService userService;

    public UserRestEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("v1/user")
    public ResponseEntity<?> upsertUser(@RequestBody SingleItemPayload<UserModel> userPayload) {
        try {
            return new ResponseEntity<>(userService.upsertRegister(userPayload.getData()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new OperationData<>(ex), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("v1/user")
    public ResponseEntity<?> softDeleteUser(@RequestBody SingleItemPayload<UUID> userPayload) {
        try {
            return new ResponseEntity<>(userService.softDeleteRegister(userPayload.getData()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new OperationData<>(ex), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("v1/user")
    public ResponseEntity<?> getUser(@RequestParam(required = false) String id,
                                     @RequestParam(required = false) String username,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) boolean isDeleted) {
        try {
            UUID uuid = UtilsValidation.isNullOrEmpty(id) ? null : UUID.fromString(id);
            return new ResponseEntity<>(userService.findRegister(uuid, name, username, isDeleted), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new OperationData<>(ex), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // TODO ALLOW ONLY FOR MASTER ADMIN
    @GetMapping("v1/user/all")
    public ResponseEntity<?> getAllUser() {
        try {
            return new ResponseEntity<>(userService.findAllRegister(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new OperationData<>(ex), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
