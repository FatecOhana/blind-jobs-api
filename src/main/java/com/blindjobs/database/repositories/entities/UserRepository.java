package com.blindjobs.database.repositories.entities;

import com.blindjobs.database.models.entities.User;
import com.blindjobs.dto.types.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Get only register not deleted (configured with tag "isDeleted") in database
     */
    Optional<User> findByIdAndIsDeletedIsFalse(UUID id);

    List<User> findAllByIsDeletedIs(Boolean isDeleted);

    Optional<User> findByIdAndIsDeletedIs(UUID id, Boolean isDeleted);

    Optional<User> findByIdentifierNameAndUserTypeAndIsDeleted(String identifierName, UserType type, Boolean isDeleted);

    List<User> findByNameAndUserTypeAndIsDeleted(String name, UserType type, Boolean isDeleted);

    Optional<User> findByEmailAndPasswordAndIsDeletedOrIdentifierNameAndPasswordAndIsDeleted(
            String email, String password, Boolean isDeleted, String identifierName, String otherPassword, Boolean otherIsDeleted);

    List<User> findAllByUserType(UserType userType);

}
