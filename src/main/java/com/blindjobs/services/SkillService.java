package com.blindjobs.services;

import com.blindjobs.database.models.complement.Skill;
import com.blindjobs.database.repositories.complement.SkillRepository;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.exceptions.NotFoundException;
import com.blindjobs.services.interfaces.UniqueRegisterOperationsInterface;
import com.blindjobs.utils.UtilsValidation;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class SkillService implements UniqueRegisterOperationsInterface<Skill> {

    private static final Logger logger = LoggerFactory.getLogger(SkillService.class);
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public OperationData<?> upsertRegister(Skill value) throws Exception {
        logger.info("Upsert Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("Skill can't be null");
        }

        Skill skillSaved = null;

        // Get the skill in the database (if exists) and copy its values to the received skill (value)
        if (!UtilsValidation.isNull(value.getId())) {
            Skill existentUser = skillRepository.findById(value.getId()).orElse(null);
            if (!UtilsValidation.isNull(existentUser)) {
                BeanUtils.copyProperties(value, existentUser);
                skillSaved = skillRepository.save(existentUser);
            }
        }

        if (UtilsValidation.isNull(skillSaved)) {
            skillSaved = skillRepository.save(value);
        }

        logger.info("Finished Upsert Register...");
        return new OperationData<>(skillSaved);
    }

    @Override
    public OperationData<UUID> softDeleteRegister(UUID value) throws Exception {
        logger.info("Soft Delete Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("Skill id can't be null");
        }

        Skill skill = skillRepository.findByIdAndIsDeletedIs(value, Boolean.FALSE).orElse(null);
        if (UtilsValidation.isNull(skill)) {
            throw new NotFoundException(String.format("not found Skill with id=[%s] and isDeleted=[%s]", value, false));
        }

        skill.setIsDeleted(Boolean.TRUE);
        skillRepository.save(skill);

        if (skillRepository.findByIdAndIsDeletedIsFalse(skill.getId()).isPresent()) {
            throw new NotFoundException(String.format(
                    "Skill: id=[%s], identifierName=[%s] not configured with delet in database",
                    skill.getId(), skill.getIdentifierName())
            );
        }

        logger.info("Finished Soft Delete Register...");
        return new OperationData<>(skill.getId());
    }

    @Override
    public OperationData<?> updateRegister(Skill value) {
        throw new NotImplementedException("method \"updateRegister\" in UserService not Implemented. Check upsert");
    }

    @Override
    public OperationData<?> createRegister(Skill value) {
        throw new NotImplementedException("method \"updateRegister\" in UserService not Implemented. Check upsert");
    }

    @Override
    public OperationData<Skill> findRegister(
            UUID id, String name, String uniqueKey, Object type, Boolean isDeleted
    ) throws Exception {
        logger.info("Get Register...");

        Skill skillModel = null;
        if (!UtilsValidation.isNull(id)) {
            skillModel = skillRepository.findByIdAndIsDeletedIs(id, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found skill with id=[%s] and isDeleted=[%s]", id, isDeleted)
            ));
        } else if (!UtilsValidation.isNull(uniqueKey)) {
            skillModel = skillRepository.findByIdentifierNameAndIsDeleted(uniqueKey, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found skill with identifierName=[%s] and isDeleted=[%s]", uniqueKey, isDeleted)
            ));
        }

        List<Skill> values = new ArrayList<>();
        if (!UtilsValidation.isNull(name)) {
            values = skillRepository.findByTitleAndIsDeleted(name, isDeleted);
        }

        if (!UtilsValidation.isNull(skillModel)) {
            values.add(skillModel);
        } else if (UtilsValidation.isNullOrEmpty(values)) {
            throw new NotFoundException(String.format(
                    "not found values in database to combination id=[%s], name=[%s], identifierName=[%s], isDeleted=[%s]",
                    id, name, uniqueKey, isDeleted
            ));
        }

        logger.info("Finished Get Register...");
        return new OperationData<>(new HashSet<>(values), null);
    }

    @Override
    public OperationData<?> findAllRegister() {
        return new OperationData<>(new HashSet<>(skillRepository.findAll()), null);
    }

}
