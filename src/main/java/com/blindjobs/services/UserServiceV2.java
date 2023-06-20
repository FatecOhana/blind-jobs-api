package com.blindjobs.services;

import com.blindjobs.database.models.entities.User;
import com.blindjobs.database.repositories.entities.UserRepository;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.command.DeleteItemCommand;
import com.blindjobs.dto.command.FindItemByParameterCommand;
import com.blindjobs.dto.command.UpsertItemCommand;
import com.blindjobs.dto.exceptions.NotFoundException;
import com.blindjobs.dto.types.UserType;
import com.blindjobs.services.interfaces.UniqueRegisterOperationsTemplateV2;
import com.blindjobs.utils.UtilsValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceV2 implements UniqueRegisterOperationsTemplateV2<User> {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceV2.class);
    private final UserRepository userRepository;

    public UserServiceV2(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OperationData<?> upsertRegister(UpsertItemCommand<User> command) throws Exception {
        logger.info("Upsert Register...");
        if (UtilsValidation.isNull(command) || UtilsValidation.isNull(command.getData())) {
            throw new NotFoundException("User Payload can't be null");
        }

        User data = command.getData().getData();
        User userToSave = data;

        // Get the uniqueUser in the database (if exists) and copy its values to the received uniqueUser (value)
        if (!UtilsValidation.isNull(data.getId())) {
            User existentUser = userRepository.findById(data.getId()).orElse(null);
            if (!UtilsValidation.isNull(existentUser)) {
                BeanUtils.copyProperties(data, existentUser);
                userToSave = existentUser;
            }
        }

        User newUser = userRepository.save(userToSave);

        logger.info("Finished Upsert Register...");
        return new OperationData<>(newUser);
    }

    @Override
    public OperationData<UUID> softDeleteRegister(DeleteItemCommand command) throws Exception {
        logger.info("Soft Delete Register...");
        if (UtilsValidation.isNull(command) || UtilsValidation.isNull(command.getId())) {
            throw new NotFoundException("User's id can't be null");
        }

        User user = userRepository.findByIdAndIsDeletedIs(command.getId(), Boolean.FALSE).orElse(null);
        if (UtilsValidation.isNull(user)) {
            throw new NotFoundException(String.format("not found User with id=[%s] and isDeleted=[%s]", command.getId(), false));
        }

        user.setIsDeleted(Boolean.TRUE);
        userRepository.save(user);

        if (userRepository.findByIdAndIsDeletedIsFalse(user.getId()).isPresent()) {
            throw new NotFoundException(String.format(
                    "User: id=[%s], uniqueKey=[%s], email=[%s] not configured as delete in database",
                    user.getId(), user.getIdentifierName(), user.getEmail())
            );
        }

        logger.info("Finished Soft Delete Register...");
        return new OperationData<>(user.getId());
    }

    @Override
    public OperationData<?> updateRegister(UpsertItemCommand<User> command) throws Exception {
        logger.info("Update Register...");
        return this.upsertRegister(command);
    }

    @Override
    public OperationData<?> createRegister(UpsertItemCommand<User> command) throws Exception {
        logger.info("Insert Register...");
        return this.upsertRegister(command);
    }

    @Override
    public OperationData<?> findRegister(FindItemByParameterCommand find) throws Exception {
        logger.info("Get Register...");

        Boolean isDeleted = find.getIsDeleted();

        UserType userType;
        try {
            userType = (UserType) find.getType();
            if (UtilsValidation.isNull(userType)) {
                logger.error(String.format("not found matching value to userType=[%s] in UserType", find.getType()));
                throw new NotFoundException("not found value to UserType");
            }
        } catch (Exception ex) {
            logger.error(String.format("not found matching value to userType=[%s] in UserType", find.getType()), ex);
            throw new NotFoundException("not found value to UserType", ex);
        }

        User user = null;
        if (!UtilsValidation.isNull(find.getId())) {
            user = userRepository.findByIdAndIsDeletedIs(find.getId(), isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found uniqueUser with id=[%s] and isDeleted=[%s]", find.getId(), isDeleted)
            ));
        } else if (!UtilsValidation.isNull(find.getUniqueKey())) {
            user = userRepository.findByIdentifierNameAndUserTypeAndIsDeleted(find.getUniqueKey(), userType, isDeleted)
                    .orElseThrow(() -> new NotFoundException(String.format(
                            "not found uniqueUser with uniqueKey=[%s], userType=[%s] and isDeleted=[%s]", find.getUniqueKey(),
                            userType, isDeleted)
                    ));
        }

        List<User> values;
        if (!UtilsValidation.isNull(find.getName())) {
            values = userRepository.findByNameAndUserTypeAndIsDeleted(find.getName(), userType, isDeleted);
        } else {
            values = userRepository.findAllByUserType(userType);
        }

        if (!UtilsValidation.isNull(user)) {
            values.add(user);
        } else if (UtilsValidation.isNullOrEmpty(values)) {
            throw new NotFoundException(String.format(
                    "not found values in database to combination id=[%s], name=[%s], uniqueKey=[%s], isDeleted=[%s], userType=[%s]",
                    find.getId(), find.getName(), find.getUniqueKey(), isDeleted, userType
            ));
        }

        logger.info("Finished Get Register...");
        return new OperationData<>(new HashSet<>(values), null);
    }

    @Override
    public OperationData<?> findAllRegister() throws Exception {
        logger.info("Get All Register...");
        return new OperationData<>(new HashSet<>(userRepository.findAll()), null);
    }
}
