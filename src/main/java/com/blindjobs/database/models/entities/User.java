package com.blindjobs.database.models.entities;

import com.blindjobs.database.models.base.BaseEntity;
import com.blindjobs.dto.types.DocumentTypes;
import com.blindjobs.dto.types.UserType;
import com.blindjobs.utils.UtilsValidation;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "TB_USER")
public class User extends BaseEntity {

    @Schema(description = "user's tyoe", example = "ESTUDANTE")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    // Personal Values
    @Schema(description = "user's phone", example = "119284928470")
    private String contactNumber;

    @Schema(description = "user's public contact email", example = "company@somecompany.com.ua")
    private String contactEmail;

    // Acess Data
    @Schema(description = "enterprise private email", example = "comapnyprivate@somecompany.com")
    @Column(unique = true, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private String email;

    @Schema(description = "user's password. This password will be encrypted in the database", example = "gabriel852109712")
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // Documents used in Register (CPF, CNPJ, RNE, RG)
    @Schema(description = "enterprise's document value. Only the numbers will be saved in the database", example = "12346578901")
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String documentValue;

    @Schema(description = "enterprise's document type. Defines the type of document inserted", example = "CNPJ")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentTypes documentType;

    // User Address Data
//    private Address address;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (UtilsValidation.isNull(obj) || getClass() != obj.getClass() || UtilsValidation.isNull(super.getId()))
            return false;
        User that = (User) obj;
        return super.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return UtilsValidation.isNull(super.getId()) ? 0 : super.getId().hashCode();
    }

}
