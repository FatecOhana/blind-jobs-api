package com.herokuapp.blindjobs.services.utils;

import com.herokuapp.blindjobs.dto.OperationData;

import java.util.Set;
import java.util.UUID;

public interface ManyRegisterOperationsInterface<T> extends UniqueRegisterOperationsInterface<T> {

    /**
     * CREATE many object (if object not exists) or UPDATE the existing objects
     */
    OperationData<?> upsertRegisters(Set<T> value) throws Exception;

    /**
     * Sets true in tag isDeleted in many object of table
     */
    OperationData<UUID> softDeleteRegisters(Set<T> value) throws Exception;

    /**
     * Find Matches Register in Database
     */
    OperationData<?> findManyMatchRegisters(Set<UUID> id, Set<String> name, Set<String> uniqueKey, Boolean isDeleted) throws Exception;

}
