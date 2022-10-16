package com.blindjobs.services;

import com.blindjobs.database.models.entities.User;
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
public class UserService implements UniqueRegisterOperationsInterface<User> {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OperationData<?> upsertRegister(User value) throws Exception {
        logger.info("Upsert Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("UniqueUser can't be null");
        }

        User user = null;

        // Get the uniqueUser in the database (if exists) and copy its values to the received uniqueUser (value)
        if (!UtilsValidation.isNull(value.getId())) {
            User existentUser = userRepository.findById(value.getId()).orElse(null);
            if (!UtilsValidation.isNull(existentUser)) {
                BeanUtils.copyProperties(value, existentUser);
                user = userRepository.save(existentUser);
            }
        }

        if (UtilsValidation.isNull(user)) {
            user = userRepository.save(value);
        }


        logger.info("Finished Upsert Register...");
        return new OperationData<>(user);
    }

    @Override
    public OperationData<UUID> softDeleteRegister(UUID value) throws Exception {
        logger.info("Soft Delete Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("UniqueUser id can't be null");
        }

        User user = userRepository.findByIdAndIsDeletedIs(value, Boolean.FALSE).orElse(null);
        if (UtilsValidation.isNull(user)) {
            throw new NotFoundException(String.format("not found UniqueUser with id=[%s] and isDeleted=[%s]", value, false));
        }

        user.setIsDeleted(Boolean.TRUE);
        userRepository.save(user);

        if (userRepository.findByIdAndIsDeletedIsFalse(user.getId()).isPresent()) {
            throw new NotFoundException(String.format(
                    "UniqueUser: id=[%s], uniqueKey=[%s], email=[%s] not configured with delet in database",
                    user.getId(), user.getIdentifierName(), user.getEmail())
            );
        }

        logger.info("Finished Soft Delete Register...");
        return new OperationData<>(user.getId());
    }

    @Override
    public OperationData<?> updateRegister(User value) {
        throw new NotImplementedException("method \"updateRegister\" in UserService not Implemented. Check upsert");
    }

    @Override
    public OperationData<?> createRegister(User value) {
        throw new NotImplementedException("method \"updateRegister\" in UserService not Implemented. Check upsert");
    }

    @Override
    public OperationData<User> findRegister(UUID id, String name, String uniqueKey, Object type, Boolean isDeleted) throws Exception {
        logger.info("Get Register...");

        UserType userType;
        try {
            userType = (UserType) type;
            if (UtilsValidation.isNull(userType)) {
                logger.error(String.format("not found matching value to userType=[%s] in UserType", type));
                throw new NotFoundException("not found value to UserType");
            }
        } catch (Exception ex) {
            logger.error(String.format("not found matching value to userType=[%s] in UserType", type), ex);
            throw new NotFoundException("not found value to UserType", ex);
        }

        User user = null;
        if (!UtilsValidation.isNull(id)) {
            user = userRepository.findByIdAndIsDeletedIs(id, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found uniqueUser with id=[%s] and isDeleted=[%s]", id, isDeleted)
            ));
        } else if (!UtilsValidation.isNull(uniqueKey)) {
            user = userRepository.findByIdentifierNameAndUserTypeAndIsDeleted(uniqueKey, userType, isDeleted)
                    .orElseThrow(() -> new NotFoundException(String.format(
                            "not found uniqueUser with uniqueKey=[%s], userType=[%s] and isDeleted=[%s]", uniqueKey,
                            userType, isDeleted)
                    ));
        }

        List<User> values = new ArrayList<>();
        if (!UtilsValidation.isNull(name)) {
            values = userRepository.findByNameAndUserTypeAndIsDeleted(name, userType, isDeleted);
        }

        if (!UtilsValidation.isNull(user)) {
            values.add(user);
        } else if (UtilsValidation.isNullOrEmpty(values)) {
            throw new NotFoundException(String.format(
                    "not found values in database to combination id=[%s], name=[%s], uniqueKey=[%s], isDeleted=[%s], userType=[%s]",
                    id, name, uniqueKey, isDeleted, userType
            ));
        }

        logger.info("Finished Get Register...");
        return new OperationData<>(new HashSet<>(values), null);
    }

    /**
     * This method return all register in user table. It was deprecated because of bringing a lot of data at once,
     * which can crash the api
     *
     * @return {@link OperationData}
     * @deprecated
     */
    @Deprecated
    @Override
    public OperationData<?> findAllRegister() {
        return new OperationData<>(new HashSet<>(userRepository.findAll()), null);
    }

    public OperationData<?> findAllRegisterV2(UserType userType) {
        return new OperationData<>(new HashSet<>(userRepository.findAllByUserType(userType)), null);
    }

}
