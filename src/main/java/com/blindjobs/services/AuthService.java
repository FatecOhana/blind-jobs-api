package com.blindjobs.services;

import com.blindjobs.database.models.entities.User;
import com.blindjobs.database.repositories.entities.UserRepository;
import com.blindjobs.dto.Login;
import com.blindjobs.dto.OperationData;
import com.blindjobs.dto.exceptions.NotFoundException;
import com.blindjobs.utils.UtilsValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public OperationData<?> checkCredential(Login data) throws Exception {
        if (UtilsValidation.isNull(data) || data.hasNullValue()) {
            throw new IllegalArgumentException("Login can't be null or contains null value");
        }

        User value = userRepository.findByEmailOrIdentifierNameAndPasswordAndIsDeleted(data.getCredentialIdentification(),
                data.getCredentialIdentification(), data.getCredentialValue(), Boolean.FALSE).orElse(null);

        if (UtilsValidation.isNull(value)) {
            logger.error(String.format("Not found user with credentialIdentification=[%s], credentialValue=[%s] and isDeleted=[%s]",
                    data.getCredentialIdentification(), "******", Boolean.FALSE
            ));
            throw new NotFoundException("Not found user");
        } else {
            return new OperationData<>(value);
        }
    }
}
