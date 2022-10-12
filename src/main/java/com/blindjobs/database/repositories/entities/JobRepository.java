package com.blindjobs.database.repositories.entities;

import com.blindjobs.database.models.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {

    /**
     * Get only register not deleted (configured with tag "isDeleted") in database
     */
    Optional<Job> findByIdAndIsDeletedIsFalse(UUID id);

    Set<Job> findByEnterprise_IdAndIsDeletedIs(UUID id, Boolean isDeleted);

    List<Job> findAllByIsDeletedIs(Boolean isDeleted);

    Optional<Job> findByIdAndIsDeletedIs(UUID id, Boolean isDeleted);

    Optional<Job> findByIdentifierNameAndIsDeleted(String username, Boolean isDeleted);

    List<Job> findByTitleAndIsDeleted(String name, Boolean isDeleted);

}
