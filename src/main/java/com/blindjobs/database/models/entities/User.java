package com.blindjobs.database.models.entities;

import com.blindjobs.dto.types.DocumentTypes;
import com.blindjobs.dto.types.UserType;
import com.blindjobs.utils.UtilsValidation;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "TB_USER")
public class User {

    // Default class values
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Schema(description = "name of entity", example = "Generic name of Entity")
    @Column(nullable = false)
    private String name;

    @Schema(description = "unique name of entity", example = "Entity-UnIqUe_USERNAME")
    @Column(nullable = false, unique = true)
    private String identifierName;

    @Schema(description = "defines if the register is deleted. this tag allows you to retrieve possible excluded cases",
            defaultValue = "false")
    private Boolean isDeleted = Boolean.FALSE;


    @Schema(description = "user's tyoe", example = "ESTUDANTE")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    // Personal Values
    @Schema(description = "user's phone", example = "119284928470")
    private String contactNumber;

    @Schema(description = "public contact email", example = "companyRecruiter@email.com")
    private String contactEmail;

    // Acess Data
    @Schema(description = "email used in login or authentication process", example = "gabriel@email.com")
    @Column(unique = true, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private String email;

    @Schema(description = "user's password. This password will be encrypted in the database", example = "gabriel852109712")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    // Documents used in Register (CPF, CNPJ, RNE, RG)
    @Schema(description = "user identification document type. Defines the type of document inserted", example = "CNPJ")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentTypes documentType;

    @Schema(description = "user identification document. Only the numbers will be saved in the database", example = "12346578901")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String documentValue;

    // User Address Data
//    private Address address;

    // User Skills and Qualifications
//    private List<Skills> skills;
//    private List<SchoolQualification> schoolQualification;

    // User Professional History (Jobs, Ongs, etc)
//    private List<WorkExperience> workExperiences;


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (UtilsValidation.isNull(obj) || getClass() != obj.getClass() || UtilsValidation.isNull(this.getId()))
            return false;
        User that = (User) obj;
        return id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return UtilsValidation.isNull(this.getId()) ? 0 : this.getId().hashCode();
    }

}
