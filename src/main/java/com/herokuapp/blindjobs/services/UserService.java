package com.herokuapp.blindjobs.services;

import com.herokuapp.blindjobs.database.models.UserModel;
import com.herokuapp.blindjobs.database.repositories.UserRepository;
import com.herokuapp.blindjobs.dto.OperationData;
import com.herokuapp.blindjobs.dto.exceptions.NotFoundException;
import com.herokuapp.blindjobs.services.utils.UniqueRegisterOperationsInterface;
import com.herokuapp.blindjobs.services.utils.UtilsValidation;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UniqueRegisterOperationsInterface<UserModel> {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OperationData<?> upsertRegister(UserModel value) throws Exception {
        logger.info("Upsert Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("UserModel can't be null");
        }

        UserModel user = userRepository.save(value);

        logger.info("Finished Upsert Register...");
        return new OperationData<>(user);
    }

    @Override
    public OperationData<UUID> softDeleteRegister(UUID value) throws Exception {
        logger.info("Soft Delete Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("UserModel id can't be null");
        }

        UserModel user = userRepository.findByIdAndIsDeletedIs(value, Boolean.FALSE).orElse(null);
        if (UtilsValidation.isNull(user)) {
            throw new NotFoundException(String.format("not found UserModel with id=[%s] and isDeleted=[%s]", value, false));
        }

        user.setIsDeleted(Boolean.TRUE);
        userRepository.save(user);

        if (userRepository.findByIdAndIsDeletedIsFalse(user.getId()).isPresent()) {
            throw new NotFoundException(String.format(
                    "UserModel: id=[%s], username=[%s], email=[%s] not configured with delet in database",
                    user.getId(), user.getUsername(), user.getEmail())
            );
        }

        logger.info("Finished Soft Delete Register...");
        return new OperationData<>(user.getId());
    }

    @Override
    public OperationData<?> updateRegister(UserModel value) {
        throw new NotImplementedException("method \"updateRegister\" in UserService not Implemented. Check upsert");
    }

    @Override
    public OperationData<?> createRegister(UserModel value) {
        throw new NotImplementedException("method \"updateRegister\" in UserService not Implemented. Check upsert");
    }

    @Override
    public OperationData<?> findRegister(UUID id, String name, String uniqueKey, Boolean isDeleted) throws Exception {
        logger.info("Get Register...");

        UserModel userModel = null;
        if (!UtilsValidation.isNull(id)) {
            userModel = userRepository.findByIdAndIsDeletedIs(id, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found user with id=[%s] and isDeleted=[%s]", id, isDeleted)
            ));
        } else if (!UtilsValidation.isNull(uniqueKey)) {
            userModel = userRepository.findByUsernameAndIsDeleted(uniqueKey, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found user with username=[%s] and isDeleted=[%s]", uniqueKey, isDeleted)
            ));
        }

        List<UserModel> values = new ArrayList<>();
        if (!UtilsValidation.isNull(name)) {
            values = userRepository.findByNameAndIsDeleted(name, isDeleted);
        }

        if (!UtilsValidation.isNull(userModel)) {
            values.add(userModel);
        } else if (UtilsValidation.isNullOrEmpty(values)) {
            throw new NotFoundException(String.format(
                    "not found values in database to combination id=[%s], name=[%s], username=[%s], isDeleted=[%s]",
                    id, name, uniqueKey, isDeleted
            ));
        }

        logger.info("Finished Get Register...");
        return new OperationData<>(new HashSet<>(values), null);
    }

    @Override
    public OperationData<?> findAllRegister() {
        return new OperationData<>(new HashSet<>(userRepository.findAll()), null);
    }

}
