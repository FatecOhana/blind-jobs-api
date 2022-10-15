package com.blindjobs.database.repositories.entities;

import com.blindjobs.database.models.entities.UniqueUser;
import com.blindjobs.dto.types.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UniqueUserRepository extends JpaRepository<UniqueUser, UUID> {

    /**
     * Get only register not deleted (configured with tag "isDeleted") in database
     */
    Optional<UniqueUser> findByIdAndIsDeletedIsFalse(UUID id);

    List<UniqueUser> findAllByIsDeletedIs(Boolean isDeleted);

    Optional<UniqueUser> findByIdAndIsDeletedIs(UUID id, Boolean isDeleted);

    Optional<UniqueUser> findByIdentifierNameAndIsDeleted(String identifierName, Boolean isDeleted);

    List<UniqueUser> findByNameAndIsDeleted(String name, Boolean isDeleted);

    Optional<UniqueUser> findByEmailAndPasswordAndIsDeleted(String email, String password, Boolean isDeleted);

    List<UniqueUser> findAllByUserType(UserType userType);

}
