package com.blindjobs.services;

import com.blindjobs.database.models.entities.UniqueUser;
import com.blindjobs.database.repositories.entities.UniqueUserRepository;
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
public class EnterpriseService implements UniqueRegisterOperationsInterface<UniqueUser> {

    private static final Logger logger = LoggerFactory.getLogger(EnterpriseService.class);
    private static final UserType USER_TYPE = UserType.ENTERPRISE;
    private final UniqueUserRepository uniqueUserRepository;

    public EnterpriseService(UniqueUserRepository uniqueUserRepository) {
        this.uniqueUserRepository = uniqueUserRepository;
    }

    @Override
    public OperationData<?> upsertRegister(UniqueUser value) throws Exception {
        logger.info("Upsert Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("UniqueUser can't be null");
        }

        UniqueUser enterprise = null;

        // Get the enterprise in the database (if exists) and copy its values to the received enterprise (value)
        if (!UtilsValidation.isNull(value.getId())) {
            UniqueUser existentUser = uniqueUserRepository.findById(value.getId()).orElse(null);
            if (!UtilsValidation.isNull(existentUser)) {
                BeanUtils.copyProperties(value, existentUser);
                enterprise = uniqueUserRepository.save(existentUser);
            }
        }

        if (UtilsValidation.isNull(enterprise)) {
            enterprise = uniqueUserRepository.save(value);
        }


        logger.info("Finished Upsert Register...");
        return new OperationData<>(enterprise);
    }

    @Override
    public OperationData<UUID> softDeleteRegister(UUID value) throws Exception {
        logger.info("Soft Delete Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("UniqueUser id can't be null");
        }

        UniqueUser enterprise = uniqueUserRepository.findByIdAndIsDeletedIs(value, Boolean.FALSE).orElse(null);
        if (UtilsValidation.isNull(enterprise)) {
            throw new NotFoundException(String.format("not found UniqueUser with id=[%s] and isDeleted=[%s]", value, false));
        }

        enterprise.setIsDeleted(Boolean.TRUE);
        uniqueUserRepository.save(enterprise);

        if (uniqueUserRepository.findByIdAndIsDeletedIsFalse(enterprise.getId()).isPresent()) {
            throw new NotFoundException(String.format(
                    "UniqueUser: id=[%s], uniqueKey=[%s], email=[%s] not configured with delet in database",
                    enterprise.getId(), enterprise.getIdentifierName(), enterprise.getEmail())
            );
        }

        logger.info("Finished Soft Delete Register...");
        return new OperationData<>(enterprise.getId());
    }

    @Override
    public OperationData<?> updateRegister(UniqueUser value) {
        throw new NotImplementedException("method \"updateRegister\" in UserService not Implemented. Check upsert");
    }

    @Override
    public OperationData<?> createRegister(UniqueUser value) {
        throw new NotImplementedException("method \"updateRegister\" in UserService not Implemented. Check upsert");
    }

    @Override
    public OperationData<UniqueUser> findRegister(UUID id, String name, String uniqueKey, Boolean isDeleted) throws Exception {
        logger.info("Get Register...");

        UniqueUser enterprise = null;
        if (!UtilsValidation.isNull(id)) {
            enterprise = uniqueUserRepository.findByIdAndIsDeletedIs(id, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found enterprise with id=[%s] and isDeleted=[%s]", id, isDeleted)
            ));
        } else if (!UtilsValidation.isNull(uniqueKey)) {
            enterprise = uniqueUserRepository.findByIdentifierNameAndIsDeleted(uniqueKey, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found enterprise with uniqueKey=[%s] and isDeleted=[%s]", uniqueKey, isDeleted)
            ));
        }

        List<UniqueUser> values = new ArrayList<>();
        if (!UtilsValidation.isNull(name)) {
            values = uniqueUserRepository.findByNameAndIsDeleted(name, isDeleted);
        }

        if (!UtilsValidation.isNull(enterprise)) {
            values.add(enterprise);
        } else if (UtilsValidation.isNullOrEmpty(values)) {
            throw new NotFoundException(String.format(
                    "not found values in database to combination id=[%s], name=[%s], uniqueKey=[%s], isDeleted=[%s]",
                    id, name, uniqueKey, isDeleted
            ));
        }

        logger.info("Finished Get Register...");
        return new OperationData<>(new HashSet<>(values), null);
    }

    @Override
    public OperationData<?> findAllRegister() {
        return new OperationData<>(new HashSet<>(uniqueUserRepository.findAllByUserType(USER_TYPE)), null);
    }

}
