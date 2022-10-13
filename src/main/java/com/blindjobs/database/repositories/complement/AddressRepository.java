package com.blindjobs.database.repositories.complement;

import com.blindjobs.database.models.complement.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

    /**
     * Get only register not deleted (configured with tag "isDeleted") in database
     */
    Optional<Address> findByIdAndIsDeletedIsFalse(UUID id);

    List<Address> findAllByIsDeletedIs(Boolean isDeleted);

    Optional<Address> findByIdAndIsDeletedIs(UUID id, Boolean isDeleted);

    Optional<Address> findByIdentifierNameAndIsDeleted(String identifierName, Boolean isDeleted);

    List<Address> findByNameAndIsDeleted(String name, Boolean isDeleted);

}
