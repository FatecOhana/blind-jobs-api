package com.blindjobs.services;

import com.blindjobs.database.models.entities.Enterprise;
import com.blindjobs.database.repositories.entities.EnterpriseRepository;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.exceptions.NotFoundException;
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

        Enterprise enterpriseSaved = null;

        // Get the enterprise in the database (if exists) and copy its values to the received enterprise (value)
        if (!UtilsValidation.isNull(value.getId())) {    //   if (!UtilsValidation.isNull(existentUser)) {
            Enterprise existentUser = enterpriseRepository.findById(value.getId()).orElse(null);
            if (!UtilsValidation.isNull(existentUser)) {
                BeanUtils.copyProperties(value, existentUser);
                enterpriseSaved = enterpriseRepository.save(existentUser);
            }
        }

        if (UtilsValidation.isNull(enterpriseSaved)) {
            enterpriseSaved = enterpriseRepository.save(value);
        }


        logger.info("Finished Upsert Register...");
        return new OperationData<>(enterpriseSaved);
    }

    @Override
    public OperationData<UUID> softDeleteRegister(UUID value) throws Exception {
        logger.info("Soft Delete Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("Enterprise id can't be null");
        }

        Enterprise enterprise = enterpriseRepository.findByIdAndIsDeletedIs(value, Boolean.FALSE).orElse(null);
        if (UtilsValidation.isNull(enterprise)) {
            throw new NotFoundException(String.format("not found Enterprise with id=[%s] and isDeleted=[%s]", value, false));
        }

        enterprise.setIsDeleted(Boolean.TRUE);
        enterpriseRepository.save(enterprise);

        if (enterpriseRepository.findByIdAndIsDeletedIsFalse(enterprise.getId()).isPresent()) {
            throw new NotFoundException(String.format(
                    "Enterprise: id=[%s], uniqueKey=[%s], email=[%s] not configured with delet in database",
                    enterprise.getId(), enterprise.getUsername(), enterprise.getEmail())
            );
        }

        logger.info("Finished Soft Delete Register...");
        return new OperationData<>(enterprise.getId());
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
    public OperationData<Enterprise> findRegister(UUID id, String name, String uniqueKey, Boolean isDeleted) throws Exception {
        logger.info("Get Register...");

        Enterprise enterprise = null;
        if (!UtilsValidation.isNull(id)) {
            enterprise = enterpriseRepository.findByIdAndIsDeletedIs(id, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found enterprise with id=[%s] and isDeleted=[%s]", id, isDeleted)
            ));
        } else if (!UtilsValidation.isNull(uniqueKey)) {
            enterprise = enterpriseRepository.findByUsernameAndIsDeleted(uniqueKey, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found enterprise with uniqueKey=[%s] and isDeleted=[%s]", uniqueKey, isDeleted)
            ));
        }

        List<Enterprise> values = new ArrayList<>();
        if (!UtilsValidation.isNull(name)) {
            values = enterpriseRepository.findByNameAndIsDeleted(name, isDeleted);
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
        return new OperationData<>(new HashSet<>(enterpriseRepository.findAll()), null);
    }

}
