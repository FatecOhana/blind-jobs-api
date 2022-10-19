package com.blindjobs.services;

import com.blindjobs.database.models.complement.Address;
import com.blindjobs.database.models.entities.Job;
import com.blindjobs.database.models.entities.User;
import com.blindjobs.database.repositories.entities.JobRepository;
import com.blindjobs.dto.CandidaturePayload;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.exceptions.NotFoundException;
import com.blindjobs.dto.types.UserType;
import com.blindjobs.services.interfaces.ManyRegisterOperationsInterface;
import com.blindjobs.utils.UtilsValidation;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JobService implements ManyRegisterOperationsInterface<Job> {

    private static final Logger logger = LoggerFactory.getLogger(JobService.class);
    private final JobRepository jobRepository;
    private final AddressService addressService;
    private final UserService userService;

    public JobService(JobRepository jobRepository, AddressService addressService, UserService userService) {
        this.jobRepository = jobRepository;
        this.addressService = addressService;
        this.userService = userService;
    }

    @Override
    public OperationData<?> upsertRegisters(Set<Job> value) throws Exception {
        logger.info("Upsert Many Register...");
        if (UtilsValidation.isNullOrEmpty(value)) {
            throw new NotFoundException("Job items can't be null");
        }

        logger.debug(String.format("Recived [%s] items to Upsert", value.size()));

        Set<Job> createdJobs = new HashSet<>();
        StringBuilder errors = new StringBuilder();
        value.forEach(v -> {
            try {
                // Expected one register
                createdJobs.addAll(this.upsertRegister(v).getData());
            } catch (Exception ex) {
                logger.error(String.format("error in upsert Job. id=[%s], identifierName=[%s]", v.getId(),
                        v.getIdentifierName()), ex);
                errors.append("\n").append(ex.getMessage());
            }
        });

        if (value.size() != createdJobs.size()) {
            logger.error(String.format("Check logs. Quantity of Received Items (size=[%s]) is not equal " +
                    "to the Quantity of Processed (size=[%s]) items.", value.size(), createdJobs.size()));
        }

        logger.info("Finished Upsert Many Register...");
        return new OperationData<>(createdJobs, errors.toString());
    }

    @Override
    public OperationData<UUID> softDeleteRegisters(Set<UUID> value) throws Exception {
        logger.info("Soft Delete Many Register...");
        if (UtilsValidation.isNullOrEmpty(value)) {
            throw new NotFoundException("Job items can't be null");
        }

        logger.debug(String.format("Recived [%s] items to Soft Delete", value.size()));

        Set<UUID> softDeletIds = new HashSet<>();
        StringBuilder errors = new StringBuilder();
        value.forEach(v -> {
            try {
                // Expected one register
                softDeletIds.addAll(this.softDeleteRegister(v).getData());
            } catch (Exception ex) {
                logger.error(String.format("error in delete Job, id=[%s]", v), ex);
                errors.append("\n").append(ex.getMessage());
            }
        });

        if (value.size() != softDeletIds.size()) {
            logger.error(String.format("Check logs. Quantity of Received Items (size=[%s]) is not equal " +
                    "to the Quantity of Processed (size=[%s]) items.", value.size(), softDeletIds.size()));
        }

        logger.info("Finished Delete Many Register...");
        return new OperationData<>(softDeletIds, errors.toString());
    }

    @Override
    public OperationData<?> findManyMatchRegisters(Set<UUID> id, Set<String> name, Set<String> uniqueKey, Object type, Boolean isDeleted) throws Exception {
        throw new NotImplementedException("method \"findManyMatchRegisters\" in JobService not Implemented. Check findRegister");
    }

    @Override
    public OperationData<Job> upsertRegister(Job value) throws Exception {
        logger.info("Upsert Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("Job can't be null");
        }

        Job jobSaved = null;

        // Get the user in the database (if exists) and copy its values to the received user (value)
        if (!UtilsValidation.isNull(value.getId())) {
            Job existentUser = jobRepository.findById(value.getId()).orElse(null);
            if (!UtilsValidation.isNull(existentUser)) {
                BeanUtils.copyProperties(value, existentUser);
                jobSaved = jobRepository.save(existentUser);
            }
        }

        if (UtilsValidation.isNull(jobSaved)) {
            checkAddressExistent(value);
            jobSaved = jobRepository.save(value);
        }

        logger.info("Finished Upsert Register...");
        return new OperationData<>(jobSaved);
    }

    /**
     * Check in database if the {@link Address} in {@link Job} exists.
     *
     * @param job contains the {@link Address}. Your {@link Address} can be updated if exists in database, to sync your
     *            data and ID
     */
    private void checkAddressExistent(Job job) {
        if (UtilsValidation.isNull(job) || UtilsValidation.isNull(job.getAddress())) return;

        try {
            Address address = job.getAddress();
            addressService.findRegister(address.getId(), null, address.getIdentifierName(), null,
                    address.getIsDeleted()).getData().stream().findFirst().ifPresent(job::setAddress);
        } catch (Exception ex) {
            logger.info("Not found address in register job");
        }
    }

    @Override
    public OperationData<UUID> softDeleteRegister(UUID value) throws Exception {
        logger.info("Soft Delete Register...");
        if (UtilsValidation.isNull(value)) {
            throw new NotFoundException("Job id can't be null");
        }

        Job job = jobRepository.findByIdAndIsDeletedIs(value, Boolean.FALSE).orElse(null);
        if (UtilsValidation.isNull(job)) {
            throw new NotFoundException(String.format("not found Job with id=[%s] and isDeleted=[%s]", value, false));
        }

        job.setIsDeleted(Boolean.TRUE);
        jobRepository.save(job);

        if (jobRepository.findByIdAndIsDeletedIsFalse(job.getId()).isPresent()) {
            throw new NotFoundException(String.format(
                    "Job: id=[%s], identifierName=[%s], title=[%s] not configured with delet in database",
                    job.getId(), job.getIdentifierName(), job.getTitle())
            );
        }

        logger.info("Finished Soft Delete Register...");
        return new OperationData<>(job.getId());
    }

    @Override
    public OperationData<?> updateRegister(Job value) throws Exception {
        throw new NotImplementedException("method \"updateRegister\" in JobService not Implemented. Check upsert");
    }

    @Override
    public OperationData<?> createRegister(Job value) throws Exception {
        throw new NotImplementedException("method \"createRegister\" in JobService not Implemented. Check upsert");
    }

    @Override
    public OperationData<Job> findRegister(UUID id, String name, String uniqueKey, Object type, Boolean isDeleted) throws Exception {
        logger.info("Get Register...");

        Job job = null;
        if (!UtilsValidation.isNull(id)) {
            job = jobRepository.findByIdAndIsDeletedIs(id, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found job with id=[%s] and isDeleted=[%s]", id, isDeleted)
            ));
        } else if (!UtilsValidation.isNull(uniqueKey)) {
            job = jobRepository.findByIdentifierNameAndIsDeleted(uniqueKey, isDeleted).orElseThrow(() -> new NotFoundException(
                    String.format("not found job with username=[%s] and isDeleted=[%s]", uniqueKey, isDeleted)
            ));
        }

        List<Job> values = new ArrayList<>();
        if (!UtilsValidation.isNull(name)) {
            values = jobRepository.findByTitleAndIsDeleted(name, isDeleted);
        }

        if (!UtilsValidation.isNull(job)) {
            values.add(job);
        } else if (UtilsValidation.isNullOrEmpty(values)) {
            throw new NotFoundException(String.format(
                    "not found values in database to combination id=[%s], name=[%s], username=[%s], isDeleted=[%s]",
                    id, name, uniqueKey, isDeleted
            ));
        }

        logger.info("Finished Get Register...");
        return new OperationData<>(new HashSet<>(values), null);
    }

    @Override
    public OperationData<?> findAllRegister() throws Exception {
        return new OperationData<>(new HashSet<>(jobRepository.findAll()), null);
    }

    public OperationData<?> candidateUserInJob(CandidaturePayload value) throws Exception {
        logger.info("Candidate User in Job...");

        if (UtilsValidation.isNull(value) || UtilsValidation.isNull(value.getJob()) || UtilsValidation.isNull(value.getUser())) {
            logger.error(String.format(
                    "the candidature payload values can't be null. candidaturePayload=[%s], job=[%s], user=[%s]",
                    value, UtilsValidation.isNull(value) ? null : value.getJob(),
                    UtilsValidation.isNull(value) ? null : value.getUser()
            ));
            throw new NotFoundException(String.format("not found values in candidaturePayload=[%s]", value));
        }

        User userDatabase = userService.findRegister(value.getUser().getId(), value.getUser().getName(),
                        value.getUser().getIdentifierName(), UserType.STUDENT, Boolean.FALSE)
                .getData().stream()
                .findFirst().orElseThrow(() -> new NotFoundException(String.format("not found values in database to " +
                                "combination id=[%s], name=[%s], username=[%s], userType=[%s] isDeleted=[%s]",
                        value.getUser().getId(), value.getUser().getName(), value.getUser().getIdentifierName(),
                        UserType.STUDENT, value.getUser().getIsDeleted())));

        Job job = value.getJob();

        Job jobDatabase = this.findRegister(job.getId(), job.getTitle(), job.getIdentifierName(), null, Boolean.FALSE)
                .getData().stream().findFirst().orElseThrow(() -> new NotFoundException(String.format(
                        "not found values in database to combination id=[%s], name=[%s], username=[%s], isDeleted=[%s]",
                        job.getId(), job.getTitle(), job.getIdentifierName(), job.getIsDeleted())));

        jobDatabase.addCandidate(userDatabase);
        jobRepository.save(jobDatabase);

        logger.info("Finished Candidated User in Job...");
        return new OperationData<>(jobDatabase);
    }

}
