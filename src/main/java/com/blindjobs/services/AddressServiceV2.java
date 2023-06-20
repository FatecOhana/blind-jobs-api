package com.blindjobs.services;

import com.blindjobs.database.models.complement.Address;
import com.blindjobs.database.repositories.complement.AddressRepository;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.command.DeleteItemCommand;
import com.blindjobs.dto.command.FindItemByParameterCommand;
import com.blindjobs.dto.command.UpsertItemCommand;
import com.blindjobs.dto.exceptions.NotFoundException;
import com.blindjobs.services.interfaces.UniqueRegisterOperationsTemplateV2;
import com.blindjobs.utils.UtilsValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class AddressServiceV2 implements UniqueRegisterOperationsTemplateV2<Address> {

    private static final Logger logger = LoggerFactory.getLogger(AddressServiceV2.class);
    private final AddressRepository addressRepository;

    public AddressServiceV2(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public OperationData<?> upsertRegister(UpsertItemCommand<Address> command) throws Exception {
        logger.info("Upsert Register...");
        if (UtilsValidation.isNull(command) || UtilsValidation.isNull(command.getData())) {
            throw new NotFoundException("Address Payload can't be null");
        }

        Address data = command.getData().getData();
        Address addressToSave = data;

        // Get the Address in the database (if exists) and copy its values to the received uniqueAddress (value)
        if (!UtilsValidation.isNull(data.getId())) {
            Address existentAddress = addressRepository.findById(data.getId()).orElse(null);
            if (!UtilsValidation.isNull(existentAddress)) {
                BeanUtils.copyProperties(data, existentAddress);
                addressToSave = existentAddress;
            }
        }

        Address newAddress = addressRepository.save(addressToSave);

        logger.info("Finished Upsert Register...");
        return new OperationData<>(newAddress);
    }

    @Override
    public OperationData<UUID> softDeleteRegister(DeleteItemCommand command) throws Exception {
        logger.info("Soft Delete Register...");
        if (UtilsValidation.isNull(command) || UtilsValidation.isNull(command.getId())) {
            throw new NotFoundException("Address's id can't be null");
        }

        Address address = addressRepository.findByIdAndIsDeletedIs(command.getId(), Boolean.FALSE).orElse(null);
        if (UtilsValidation.isNull(address)) {
            throw new NotFoundException(String.format("not found Address with id=[%s] and isDeleted=[%s]", command.getId(), false));
        }

        address.setIsDeleted(Boolean.TRUE);
        addressRepository.save(address);

        if (addressRepository.findByIdAndIsDeletedIsFalse(address.getId()).isPresent()) {
            throw new NotFoundException(String.format(
                    "Address: id=[%s], uniqueKey=[%s] not configured as delete in database",
                    address.getId(), address.getIdentifierName())
            );
        }

        logger.info("Finished Soft Delete Register...");
        return new OperationData<>(address.getId());
    }

    @Override
    public OperationData<?> updateRegister(UpsertItemCommand<Address> command) throws Exception {
        logger.info("Update Register...");
        return this.upsertRegister(command);
    }

    @Override
    public OperationData<?> createRegister(UpsertItemCommand<Address> command) throws Exception {
        logger.info("Insert Register...");
        return this.upsertRegister(command);
    }

    @Override
    public OperationData<?> findRegister(FindItemByParameterCommand find) throws Exception {
        logger.info("Get Register...");

        Boolean isDeleted = find.getIsDeleted();
        UUID id = find.getId();

        Address addressModel = null;
        if (!UtilsValidation.isNull(id)) {
            addressModel = addressRepository.findByIdAndIsDeletedIs(id, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found address with id=[%s] and isDeleted=[%s]", id, isDeleted)
            ));
        } else if (!UtilsValidation.isNull(find.getUniqueKey())) {
            addressModel = addressRepository.findByIdentifierNameAndIsDeleted(find.getUniqueKey(), isDeleted)
                    .orElseThrow(() -> new NotFoundException(String.format(
                            "not found address with identifierName=[%s] and isDeleted=[%s]", find.getUniqueKey(), isDeleted)
                    ));
        }

        List<Address> values = new ArrayList<>();
        if (!UtilsValidation.isNull(find.getName())) {
            values = addressRepository.findByNameAndIsDeleted(find.getName(), isDeleted);
        }

        if (!UtilsValidation.isNull(addressModel)) {
            values.add(addressModel);
        } else if (UtilsValidation.isNullOrEmpty(values)) {
            throw new NotFoundException(String.format(
                    "not found values in database to combination id=[%s], name=[%s], identifierName=[%s], isDeleted=[%s]",
                    id, find.getName(), find.getUniqueKey(), isDeleted
            ));
        }

        logger.info("Finished Get Register...");
        return new OperationData<>(new HashSet<>(values), null);
    }

    @Override
    public OperationData<?> findAllRegister() throws Exception {
        return new OperationData<>(new HashSet<>(addressRepository.findAll()), null);
    }
}
