package com.blindjobs.database.repositories;

import com.blindjobs.database.models.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, UUID> {

    /**
     * Get only register not deleted (configured with tag "isDeleted") in database
     */
    Optional<Enterprise> findByIdAndIsDeletedIsFalse(UUID id);

    List<Enterprise> findAllByIsDeletedIs(Boolean isDeleted);

    Optional<Enterprise> findByIdAndIsDeletedIs(UUID id, Boolean isDeleted);

    Optional<Enterprise> findByUsernameAndIsDeleted(String username, Boolean isDeleted);

    List<Enterprise> findByNameAndIsDeleted(String name, Boolean isDeleted);

    Optional<Enterprise> findByEmailAndPasswordAndIsDeleted(String email, String password, Boolean isDeleted);
}
