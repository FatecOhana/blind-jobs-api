package com.blindjobs.dto;

import com.blindjobs.utils.UtilsValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Login {

    @Schema(description = "Credential used as identifier", example = "gabriel@email.com")
    private String credentialIdentification;

    @Schema(description = "Password used to validate a credential", example = "gabriel412891")
    private String credentialValue;


    public boolean hasNullValue() {
        return UtilsValidation.isNullOrEmpty(credentialIdentification) || UtilsValidation.isNullOrEmpty(credentialValue);
    }
}
