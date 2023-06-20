package com.blindjobs.services.interfaces;

import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.command.DeleteItemsCommand;
import com.blindjobs.dto.command.FindItemsByParametersCommand;
import com.blindjobs.dto.command.UpsertItemsCommand;

import java.util.UUID;

public interface ManyRegisterOperationsTemplateV2<T> extends UniqueRegisterOperationsTemplate<T> {

    /**
     * CREATE many object (if object not exists) or UPDATE the existing objects
     */
    OperationData<?> upsertRegisters(UpsertItemsCommand<T> value) throws Exception;

    /**
     * Sets true in tag isDeleted in many object of table
     */
    OperationData<UUID> softDeleteRegisters(DeleteItemsCommand value) throws Exception;

    /**
     * Find Matches Register in Database
     */
    OperationData<?> findManyMatchRegisters(FindItemsByParametersCommand find) throws Exception;

}
