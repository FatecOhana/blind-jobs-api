package com.blindjobs.database.repositories.complement;

import com.blindjobs.database.models.complement.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkillRepository extends JpaRepository<Skill, UUID> {

    Optional<Skill> findByIdAndIsDeletedIsFalse(UUID id);

    List<Skill> findAllByIsDeletedIs(Boolean isDeleted);

    Optional<Skill> findByIdAndIsDeletedIs(UUID id, Boolean isDeleted);

    Optional<Skill> findByIdentifierNameAndIsDeleted(String identifierName, Boolean isDeleted);

    List<Skill> findByTitleAndIsDeleted(String name, Boolean isDeleted);
}
