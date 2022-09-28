package com.herokuapp.blindjobs.database.repositories;

import com.herokuapp.blindjobs.database.models.UserModel;
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

    Optional<UserModel> findByIdAndIsDeleted(UUID id, Boolean isDeleted);

    Optional<UserModel> findByUsernameAndIsDeleted(String username, Boolean isDeleted);

    List<UserModel> findByNameAndIsDeleted(String name, Boolean isDeleted);

}
