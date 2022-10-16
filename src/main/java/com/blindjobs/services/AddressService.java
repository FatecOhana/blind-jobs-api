package com.blindjobs.services;

import com.blindjobs.database.models.complement.Address;
import com.blindjobs.database.repositories.complement.AddressRepository;
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
public class AddressService implements UniqueRegisterOperationsInterface<Address> {

    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public OperationData<?> upsertRegister(Address value) throws Exception {
        logger.info("Upsert Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("Address can't be null");
        }

        Address addressSaved = null;

        // Get the address in the database (if exists) and copy its values to the received address (value)
        if (!UtilsValidation.isNull(value.getId())) {    //   if (!UtilsValidation.isNull(existentUser)) {
            Address existentUser = addressRepository.findById(value.getId()).orElse(null);
            if (!UtilsValidation.isNull(existentUser)) {
                BeanUtils.copyProperties(value, existentUser);
                addressSaved = addressRepository.save(existentUser);
            }
        }

        if (UtilsValidation.isNull(addressSaved)) {
            addressSaved = addressRepository.save(value);
        }


        logger.info("Finished Upsert Register...");
        return new OperationData<>(addressSaved);
    }

    @Override
    public OperationData<UUID> softDeleteRegister(UUID value) throws Exception {
        logger.info("Soft Delete Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("Address id can't be null");
        }

        Address address = addressRepository.findByIdAndIsDeletedIs(value, Boolean.FALSE).orElse(null);
        if (UtilsValidation.isNull(address)) {
            throw new NotFoundException(String.format("not found Address with id=[%s] and isDeleted=[%s]", value, false));
        }

        address.setIsDeleted(Boolean.TRUE);
        addressRepository.save(address);

        if (addressRepository.findByIdAndIsDeletedIsFalse(address.getId()).isPresent()) {
            throw new NotFoundException(String.format(
                    "Address: id=[%s], identifierName=[%s] not configured with delet in database",
                    address.getId(), address.getIdentifierName())
            );
        }

        logger.info("Finished Soft Delete Register...");
        return new OperationData<>(address.getId());
    }

    @Override
    public OperationData<?> updateRegister(Address value) {
        throw new NotImplementedException("method \"updateRegister\" in UserService not Implemented. Check upsert");
    }

    @Override
    public OperationData<?> createRegister(Address value) {
        throw new NotImplementedException("method \"updateRegister\" in UserService not Implemented. Check upsert");
    }

    @Override
    public OperationData<?> findRegister(UUID id, String name, String uniqueKey, Object type, Boolean isDeleted) throws Exception {
        logger.info("Get Register...");

        Address addressModel = null;
        if (!UtilsValidation.isNull(id)) {
            addressModel = addressRepository.findByIdAndIsDeletedIs(id, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found address with id=[%s] and isDeleted=[%s]", id, isDeleted)
            ));
        } else if (!UtilsValidation.isNull(uniqueKey)) {
            addressModel = addressRepository.findByIdentifierNameAndIsDeleted(uniqueKey, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found address with identifierName=[%s] and isDeleted=[%s]", uniqueKey, isDeleted)
            ));
        }

        List<Address> values = new ArrayList<>();
        if (!UtilsValidation.isNull(name)) {
            values = addressRepository.findByNameAndIsDeleted(name, isDeleted);
        }

        if (!UtilsValidation.isNull(addressModel)) {
            values.add(addressModel);
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
        return new OperationData<>(new HashSet<>(addressRepository.findAll()), null);
    }

}
