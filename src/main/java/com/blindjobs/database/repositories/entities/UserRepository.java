package com.blindjobs.database.repositories.entities;

import com.blindjobs.database.models.entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {

    /**
     * Get only register not deleted (configured with tag "isDeleted") in database
     */
    Optional<UserModel> findByIdAndIsDeletedIsFalse(UUID id);

    List<UserModel> findAllByIsDeletedIs(Boolean isDeleted);

    Optional<UserModel> findByIdAndIsDeletedIs(UUID id, Boolean isDeleted);

    Optional<UserModel> findByUsernameAndIsDeleted(String username, Boolean isDeleted);

    List<UserModel> findByNameAndIsDeleted(String name, Boolean isDeleted);

    Optional<UserModel> findByEmailAndPasswordAndIsDeleted(String email, String password, Boolean isDeleted);
}
