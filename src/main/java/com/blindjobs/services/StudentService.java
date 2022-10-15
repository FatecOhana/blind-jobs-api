package com.blindjobs.services;

import com.blindjobs.database.models.entities.UserModel;
import com.blindjobs.database.repositories.entities.UserRepository;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.exceptions.NotFoundException;
import com.blindjobs.dto.types.UserType;
import com.blindjobs.services.interfaces.UniqueRegisterOperationsInterface;
import com.blindjobs.utils.UtilsValidation;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class StudentService implements UniqueRegisterOperationsInterface<UserModel> {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private static final UserType USER_TYPE = UserType.STUDENT;
    private final UserRepository userRepository;

    public StudentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OperationData<?> upsertRegister(UserModel value) throws Exception {
        logger.info("Upsert Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("UserModel can't be null");
        }

        UserModel userSaved = null;

        // Get the user in the database (if exists) and copy its values to the received user (value)
        if (!UtilsValidation.isNull(value.getId())) {    //   if (!UtilsValidation.isNull(existentUser)) {
            UserModel existentUser = userRepository.findById(value.getId()).orElse(null);
            if (!UtilsValidation.isNull(existentUser)) {
                BeanUtils.copyProperties(value, existentUser);
                userSaved = userRepository.save(existentUser);
            }
        }

        if (UtilsValidation.isNull(userSaved)) {
            userSaved = userRepository.save(value);
        }


        logger.info("Finished Upsert Register...");
        return new OperationData<>(userSaved);
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
                    "UserModel: id=[%s], identifierName=[%s], email=[%s] not configured with delet in database",
                    user.getId(), user.getIdentifierName(), user.getEmail())
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
            userModel = userRepository.findByIdentifierNameAndIsDeleted(uniqueKey, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found user with identifierName=[%s] and isDeleted=[%s]", uniqueKey, isDeleted)
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
                    "not found values in database to combination id=[%s], name=[%s], identifierName=[%s], isDeleted=[%s]",
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
