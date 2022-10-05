package com.blindjobs.database.models;

import com.blindjobs.dto.types.DocumentTypes;
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
@Entity
public class UserModel {

    // Unique Identifier
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, columnDefinition = "uuid")
    private UUID id;

    // Personal Values
    @Schema(description = "name's user", example = "Gabriel")
    @Column(nullable = false)
    private String name;

    @Schema(description = "lastName's user", example = "Luis")
    @Column(nullable = false)
    private String lastName;

    @Schema(description = "user's username", example = "gabriel_1513")
    @Column(unique = true, nullable = false)
    private String username;

    @Schema(description = "user's phone", example = "119284928470")
    private String contactNumber;


    // Acess Data
    @Schema(description = "user's email", example = "gabriel@email.com")
    @Column(unique = true, nullable = false)
    private String email;

    @Schema(description = "user's password. This password will be encrypted in the database", example = "gabriel852109712")
    @Column(nullable = false)
    private String password;

    // Documents used in Register (CPF, CNPJ, RNE, RG)
    @Schema(description = "user's document value. Only the numbers will be saved in the database", example = "12346578901")
    @Column(nullable = false)
    private String documentValue;

    @Schema(description = "user's document type. Defines the type of document inserted", example = "CPF")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentTypes documentType;

    @Schema(description = "defines if a user is deleted. this tag allows you to retrieve possible excluded cases", defaultValue = "false")
    private Boolean isDeleted = Boolean.FALSE;

    // User Address Data
//    private PersonalAddress address;

    // User Skills and Qualifications
//    private List<Skills> skills;

    // User Professional History (Jobs, Ongs, etc)
//    private List<WorkExperience> workExperiences;

    // User sStudent Qualification
//    private List<SchoolQualification> schoolQualification;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (id == null || obj == null || getClass() != obj.getClass())
            return false;
        UserModel that = (UserModel) obj;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}
