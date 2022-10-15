package com.blindjobs.services;

import com.blindjobs.database.repositories.entities.UserRepository;
import com.blindjobs.database.repositories.entities.StudentRepository;
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
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public AuthService(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    public OperationData<?> checkCredential(Login data, String module) throws Exception {
        if (UtilsValidation.isNull(data) || data.hasNullValue()) {
            throw new IllegalArgumentException("Login can't be null or contains null value");
        }

        Object value = switch (module) {
            case "user" -> studentRepository.findByEmailAndPasswordAndIsDeleted(data.getCredentialIdentification(),
                    data.getCredentialValue(), Boolean.FALSE).orElse(null);
            case "enterprise" ->
                    userRepository.findByEmailAndPasswordAndIsDeleted(data.getCredentialIdentification(),
                            data.getCredentialValue(), Boolean.FALSE).orElse(null);
            default -> null;
        };

        if (UtilsValidation.isNull(value)) {
            logger.error(String.format("Not found [%s] with credentialIdentification=[%s], credentialValue=[%s] and isDeleted=[%s]",
                    module, data.getCredentialIdentification(), "******", Boolean.FALSE
            ));
            throw new NotFoundException(String.format("Not found %s", module));
        } else {
            return new OperationData<>(value);
        }
    }
}
