package com.blindjobs.services.interfaces;

import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.command.FindItemByParameterCommand;
import com.blindjobs.dto.command.UpsertItemCommand;

import java.util.UUID;

public interface UniqueRegisterOperationsTemplateV2<T> {

    /**
     * CREATE one object (if object not exists) or UPDATE the existing object
     */
    OperationData<?> upsertRegister(UpsertItemCommand<T> value) throws Exception;

    /**
     * Sets true in tag isDeleted in specify object of table
     */
    OperationData<UUID> softDeleteRegister(UUID value) throws Exception;

    /**
     * Update one register in Database
     */
    OperationData<?> updateRegister(UpsertItemCommand<T> value) throws Exception;

    /**
     * Create one Register in Database
     */
    OperationData<?> createRegister(UpsertItemCommand<T> value) throws Exception;

    /**
     * Find Matches Register in Database
     */
    OperationData<?> findRegister(FindItemByParameterCommand find) throws Exception;

    /**
     * Find all Register in Database
     */
    OperationData<?> findAllRegister() throws Exception;

}
