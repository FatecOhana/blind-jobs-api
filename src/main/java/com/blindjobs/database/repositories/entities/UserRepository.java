package com.blindjobs.database.repositories.entities;

import com.blindjobs.database.models.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Student, UUID> {

    /**
     * Get only register not deleted (configured with tag "isDeleted") in database
     */
    Optional<Student> findByIdAndIsDeletedIsFalse(UUID id);

    List<Student> findAllByIsDeletedIs(Boolean isDeleted);

    Optional<Student> findByIdAndIsDeletedIs(UUID id, Boolean isDeleted);

    Optional<Student> findByIdentifierNameAndIsDeleted(String username, Boolean isDeleted);

    List<Student> findByNameAndIsDeleted(String name, Boolean isDeleted);

    Optional<Student> findByEmailAndPasswordAndIsDeleted(String email, String password, Boolean isDeleted);
}
