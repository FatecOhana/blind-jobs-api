package com.blindjobs.services;

import com.blindjobs.database.models.Enterprise;
import com.blindjobs.database.repositories.EnterpriseRepository;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.exceptions.NotFoundException;
import com.blindjobs.services.utils.UniqueRegisterOperationsInterface;
import com.blindjobs.services.utils.UtilsValidation;
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
public class EnterpriseService implements UniqueRegisterOperationsInterface<Enterprise> {

    private static final Logger logger = LoggerFactory.getLogger(EnterpriseService.class);
    private final EnterpriseRepository enterpriseRepository;

    public EnterpriseService(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }

    @Override
    public OperationData<?> upsertRegister(Enterprise value) throws Exception {
        logger.info("Upsert Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("Enterprise can't be null");
        }

        Enterprise userSaved = null;

        // Get the user in the database (if exists) and copy its values to the received user (value)
        if (!UtilsValidation.isNull(value.getId())) {    //   if (!UtilsValidation.isNull(existentUser)) {
            Enterprise existentUser = enterpriseRepository.findById(value.getId()).orElse(null);
            if (!UtilsValidation.isNull(existentUser)) {
                BeanUtils.copyProperties(value, existentUser);
                userSaved = enterpriseRepository.save(existentUser);
            }
        }

        if (UtilsValidation.isNull(userSaved)) {
            userSaved = enterpriseRepository.save(value);
        }


        logger.info("Finished Upsert Register...");
        return new OperationData<>(userSaved);
    }

    @Override
    public OperationData<UUID> softDeleteRegister(UUID value) throws Exception {
        logger.info("Soft Delete Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("Enterprise id can't be null");
        }

        Enterprise user = enterpriseRepository.findByIdAndIsDeletedIs(value, Boolean.FALSE).orElse(null);
        if (UtilsValidation.isNull(user)) {
            throw new NotFoundException(String.format("not found Enterprise with id=[%s] and isDeleted=[%s]", value, false));
        }

        user.setIsDeleted(Boolean.TRUE);
        enterpriseRepository.save(user);

        if (enterpriseRepository.findByIdAndIsDeletedIsFalse(user.getId()).isPresent()) {
            throw new NotFoundException(String.format(
                    "Enterprise: id=[%s], username=[%s], email=[%s] not configured with delet in database",
                    user.getId(), user.getUsername(), user.getEmail())
            );
        }

        logger.info("Finished Soft Delete Register...");
        return new OperationData<>(user.getId());
    }

    @Override
    public OperationData<?> updateRegister(Enterprise value) {
        throw new NotImplementedException("method \"updateRegister\" in UserService not Implemented. Check upsert");
    }

    @Override
    public OperationData<?> createRegister(Enterprise value) {
        throw new NotImplementedException("method \"updateRegister\" in UserService not Implemented. Check upsert");
    }

    @Override
    public OperationData<?> findRegister(UUID id, String name, String uniqueKey, Boolean isDeleted) throws Exception {
        logger.info("Get Register...");

        Enterprise userModel = null;
        if (!UtilsValidation.isNull(id)) {
            userModel = enterpriseRepository.findByIdAndIsDeletedIs(id, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found user with id=[%s] and isDeleted=[%s]", id, isDeleted)
            ));
        } else if (!UtilsValidation.isNull(uniqueKey)) {
            userModel = enterpriseRepository.findByUsernameAndIsDeleted(uniqueKey, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found user with username=[%s] and isDeleted=[%s]", uniqueKey, isDeleted)
            ));
        }

        List<Enterprise> values = new ArrayList<>();
        if (!UtilsValidation.isNull(name)) {
            values = enterpriseRepository.findByNameAndIsDeleted(name, isDeleted);
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
        return new OperationData<>(new HashSet<>(enterpriseRepository.findAll()), null);
    }

}
